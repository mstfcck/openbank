package com.openbank.accountservice.repository;

import com.openbank.accountservice.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private AccountRepository accountRepository;
    
    private Account testAccount1;
    private Account testAccount2;
    private Account testAccount3;
    
    @BeforeEach
    void setUp() {
        testAccount1 = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0001")
                .accountType(Account.AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .overdraftLimit(BigDecimal.ZERO)
                .openedAt(LocalDateTime.now())
                .build();
        
        testAccount2 = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0002")
                .accountType(Account.AccountType.CHECKING)
                .balance(BigDecimal.valueOf(2000))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .overdraftLimit(BigDecimal.valueOf(500))
                .openedAt(LocalDateTime.now())
                .build();
        
        testAccount3 = Account.builder()
                .userId(2L)
                .accountNumber("ACC-20231201-0002-0001")
                .accountType(Account.AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(500))
                .status(Account.AccountStatus.FROZEN)
                .currency("USD")
                .overdraftLimit(BigDecimal.ZERO)
                .openedAt(LocalDateTime.now())
                .build();
    }
    
    @Test
    void findByUserId_ShouldReturnAccountsForUser() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);
        entityManager.persistAndFlush(testAccount3);
        
        // When
        List<Account> accounts = accountRepository.findByUserId(1L);
        
        // Then
        assertThat(accounts).hasSize(2);
        assertThat(accounts).extracting(Account::getUserId)
                .containsOnly(1L);
        assertThat(accounts).extracting(Account::getAccountNumber)
                .containsExactlyInAnyOrder("ACC-20231201-0001-0001", "ACC-20231201-0001-0002");
    }
    
    @Test
    void findByUserIdWithPagination_ShouldReturnPagedResults() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);
        entityManager.persistAndFlush(testAccount3);
        
        Pageable pageable = PageRequest.of(0, 1);
        
        // When
        Page<Account> accounts = accountRepository.findByUserId(1L, pageable);
        
        // Then
        assertThat(accounts.getContent()).hasSize(1);
        assertThat(accounts.getTotalElements()).isEqualTo(2);
        assertThat(accounts.getTotalPages()).isEqualTo(2);
        assertThat(accounts.getContent().get(0).getUserId()).isEqualTo(1L);
    }
    
    @Test
    void findByAccountNumber_ShouldReturnAccount_WhenAccountExists() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        
        // When
        Optional<Account> account = accountRepository.findByAccountNumber("ACC-20231201-0001-0001");
        
        // Then
        assertThat(account).isPresent();
        assertThat(account.get().getAccountNumber()).isEqualTo("ACC-20231201-0001-0001");
        assertThat(account.get().getUserId()).isEqualTo(1L);
    }
    
    @Test
    void findByAccountNumber_ShouldReturnEmpty_WhenAccountDoesNotExist() {
        // When
        Optional<Account> account = accountRepository.findByAccountNumber("NON-EXISTENT");
        
        // Then
        assertThat(account).isEmpty();
    }
    
    @Test
    void existsByAccountNumber_ShouldReturnTrue_WhenAccountExists() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        
        // When
        boolean exists = accountRepository.existsByAccountNumber("ACC-20231201-0001-0001");
        
        // Then
        assertThat(exists).isTrue();
    }
    
    @Test
    void existsByAccountNumber_ShouldReturnFalse_WhenAccountDoesNotExist() {
        // When
        boolean exists = accountRepository.existsByAccountNumber("NON-EXISTENT");
        
        // Then
        assertThat(exists).isFalse();
    }
    
    @Test
    void findByUserIdAndStatus_ShouldReturnFilteredAccounts() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);
        entityManager.persistAndFlush(testAccount3);
        
        // When
        List<Account> activeAccounts = accountRepository.findByUserIdAndStatus(1L, Account.AccountStatus.ACTIVE);
        
        // Then
        assertThat(activeAccounts).hasSize(2);
        assertThat(activeAccounts).extracting(Account::getStatus)
                .containsOnly(Account.AccountStatus.ACTIVE);
        assertThat(activeAccounts).extracting(Account::getUserId)
                .containsOnly(1L);
    }
    
    @Test
    void findByStatus_ShouldReturnAccountsWithStatus() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);
        entityManager.persistAndFlush(testAccount3);
        
        Pageable pageable = PageRequest.of(0, 10);
        
        // When
        Page<Account> activeAccounts = accountRepository.findByStatus(Account.AccountStatus.ACTIVE, pageable);
        
        // Then
        assertThat(activeAccounts.getContent()).hasSize(2);
        assertThat(activeAccounts.getContent()).extracting(Account::getStatus)
                .containsOnly(Account.AccountStatus.ACTIVE);
    }
    
    @Test
    void findByAccountType_ShouldReturnAccountsWithType() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);
        entityManager.persistAndFlush(testAccount3);
        
        Pageable pageable = PageRequest.of(0, 10);
        
        // When
        Page<Account> savingsAccounts = accountRepository.findByAccountType(Account.AccountType.SAVINGS, pageable);
        
        // Then
        assertThat(savingsAccounts.getContent()).hasSize(2);
        assertThat(savingsAccounts.getContent()).extracting(Account::getAccountType)
                .containsOnly(Account.AccountType.SAVINGS);
    }
    
    @Test
    void countByUserId_ShouldReturnCorrectCount() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);
        entityManager.persistAndFlush(testAccount3);
        
        // When
        long count = accountRepository.countByUserId(1L);
        
        // Then
        assertThat(count).isEqualTo(2);
    }
    
    @Test
    void countByStatus_ShouldReturnCorrectCount() {
        // Given
        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);
        entityManager.persistAndFlush(testAccount3);
        
        // When
        long activeCount = accountRepository.countByStatus(Account.AccountStatus.ACTIVE);
        long frozenCount = accountRepository.countByStatus(Account.AccountStatus.FROZEN);
        
        // Then
        assertThat(activeCount).isEqualTo(2);
        assertThat(frozenCount).isEqualTo(1);
    }
    
    @Test
    void saveAccount_ShouldPersistAccount() {
        // Given
        Account newAccount = Account.builder()
                .userId(3L)
                .accountNumber("ACC-20231201-0003-0001")
                .accountType(Account.AccountType.CHECKING)
                .balance(BigDecimal.valueOf(750))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .overdraftLimit(BigDecimal.valueOf(250))
                .openedAt(LocalDateTime.now())
                .build();
        
        // When
        Account savedAccount = accountRepository.save(newAccount);
        
        // Then
        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getAccountNumber()).isEqualTo("ACC-20231201-0003-0001");
        assertThat(savedAccount.getUserId()).isEqualTo(3L);
        assertThat(savedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(750));
    }
    
    @Test
    void updateAccount_ShouldModifyExistingAccount() {
        // Given
        Account savedAccount = entityManager.persistAndFlush(testAccount1);
        
        // When
        savedAccount.setBalance(BigDecimal.valueOf(1500));
        savedAccount.setStatus(Account.AccountStatus.FROZEN);
        
        Account updatedAccount = accountRepository.save(savedAccount);
        
        // Then
        assertThat(updatedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(1500));
        assertThat(updatedAccount.getStatus()).isEqualTo(Account.AccountStatus.FROZEN);
        assertThat(updatedAccount.getId()).isEqualTo(savedAccount.getId());
    }
    
    @Test
    void deleteAccount_ShouldRemoveAccount() {
        // Given
        Account savedAccount = entityManager.persistAndFlush(testAccount1);
        Long accountId = savedAccount.getId();
        
        // When
        accountRepository.delete(savedAccount);
        
        // Then
        Optional<Account> deletedAccount = accountRepository.findById(accountId);
        assertThat(deletedAccount).isEmpty();
    }
}
