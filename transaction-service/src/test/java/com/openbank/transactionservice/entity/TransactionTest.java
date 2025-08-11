package com.openbank.transactionservice.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Transaction entity.
 */
class TransactionTest {

    @Test
    void testTransactionCreation() {
        Transaction transaction = Transaction.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(new BigDecimal("100.00"))
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .description("Test transfer")
                .reference("TXN-TEST-001")
                .currency("USD")
                .fee(new BigDecimal("2.50"))
                .build();

        assertNotNull(transaction);
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals(TransactionType.TRANSFER, transaction.getTransactionType());
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
        assertEquals("Test transfer", transaction.getDescription());
        assertEquals("TXN-TEST-001", transaction.getReference());
        assertEquals("USD", transaction.getCurrency());
        assertEquals(new BigDecimal("2.50"), transaction.getFee());
        assertEquals(new BigDecimal("102.50"), transaction.getTotalAmount());
    }

    @Test
    void testValidTransfer() {
        Transaction transaction = Transaction.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .transactionType(TransactionType.TRANSFER)
                .build();

        assertTrue(transaction.isValidTransfer());
    }

    @Test
    void testInvalidTransferSameAccount() {
        Transaction transaction = Transaction.builder()
                .fromAccountId(1L)
                .toAccountId(1L)
                .transactionType(TransactionType.TRANSFER)
                .build();

        assertFalse(transaction.isValidTransfer());
    }

    @Test
    void testValidDeposit() {
        Transaction transaction = Transaction.builder()
                .toAccountId(1L)
                .transactionType(TransactionType.DEPOSIT)
                .build();

        assertTrue(transaction.isValidDeposit());
    }

    @Test
    void testInvalidDepositWithFromAccount() {
        Transaction transaction = Transaction.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .transactionType(TransactionType.DEPOSIT)
                .build();

        assertFalse(transaction.isValidDeposit());
    }

    @Test
    void testValidWithdrawal() {
        Transaction transaction = Transaction.builder()
                .fromAccountId(1L)
                .transactionType(TransactionType.WITHDRAWAL)
                .build();

        assertTrue(transaction.isValidWithdrawal());
    }

    @Test
    void testInvalidWithdrawalWithToAccount() {
        Transaction transaction = Transaction.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .transactionType(TransactionType.WITHDRAWAL)
                .build();

        assertFalse(transaction.isValidWithdrawal());
    }

    @Test
    void testMarkAsCompleted() {
        Transaction transaction = Transaction.builder()
                .status(TransactionStatus.PENDING)
                .build();

        transaction.markAsCompleted();

        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        assertNotNull(transaction.getProcessedAt());
        assertNull(transaction.getErrorMessage());
    }

    @Test
    void testMarkAsFailed() {
        Transaction transaction = Transaction.builder()
                .status(TransactionStatus.PENDING)
                .build();

        String errorMessage = "Insufficient funds";
        transaction.markAsFailed(errorMessage);

        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        assertNotNull(transaction.getProcessedAt());
        assertEquals(errorMessage, transaction.getErrorMessage());
    }

    @Test
    void testMarkAsCancelled() {
        Transaction transaction = Transaction.builder()
                .status(TransactionStatus.PENDING)
                .build();

        transaction.markAsCancelled();

        assertEquals(TransactionStatus.CANCELLED, transaction.getStatus());
        assertNotNull(transaction.getProcessedAt());
    }

    @Test
    void testIsFinalState() {
        Transaction completedTransaction = Transaction.builder()
                .status(TransactionStatus.COMPLETED)
                .build();
        assertTrue(completedTransaction.isFinalState());

        Transaction failedTransaction = Transaction.builder()
                .status(TransactionStatus.FAILED)
                .build();
        assertTrue(failedTransaction.isFinalState());

        Transaction cancelledTransaction = Transaction.builder()
                .status(TransactionStatus.CANCELLED)
                .build();
        assertTrue(cancelledTransaction.isFinalState());

        Transaction reversedTransaction = Transaction.builder()
                .status(TransactionStatus.REVERSED)
                .build();
        assertTrue(reversedTransaction.isFinalState());

        Transaction pendingTransaction = Transaction.builder()
                .status(TransactionStatus.PENDING)
                .build();
        assertFalse(pendingTransaction.isFinalState());

        Transaction processingTransaction = Transaction.builder()
                .status(TransactionStatus.PROCESSING)
                .build();
        assertFalse(processingTransaction.isFinalState());
    }

    @Test
    void testGetTotalAmount() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("100.00"))
                .fee(new BigDecimal("5.00"))
                .build();

        assertEquals(new BigDecimal("105.00"), transaction.getTotalAmount());
    }

    @Test
    void testGetTotalAmountWithZeroFee() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("100.00"))
                .fee(BigDecimal.ZERO)
                .build();

        assertEquals(new BigDecimal("100.00"), transaction.getTotalAmount());
    }
}
