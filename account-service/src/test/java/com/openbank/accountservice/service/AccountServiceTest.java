package com.openbank.accountservice.service;

import com.openbank.accountservice.client.UserServiceClient;
import com.openbank.accountservice.dto.CreateAccountRequest;
import com.openbank.accountservice.dto.AccountResponse;
import com.openbank.accountservice.entity.Account;
import com.openbank.accountservice.exception.AccountNotFoundException;
import com.openbank.accountservice.exception.UserNotFoundException;
import com.openbank.accountservice.repository.AccountRepository;
import com.openbank.accountservice.util.AccountMapper;
import com.openbank.accountservice.util.AccountNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private UserServiceClient userServiceClient;
    
    @Mock
    private AccountMapper accountMapper;
    
    @Mock
    private AccountNumberGenerator accountNumberGenerator;
    
    @InjectMocks
    private AccountService accountService;
    
    private Account testAccount;
    private CreateAccountRequest createRequest;
    private AccountResponse accountResponse;
    
    @BeforeEach
    void setUp() {
        testAccount = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0001")
                .accountType(Account.AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .overdraftLimit(BigDecimal.ZERO)
                .build();
        
        // Set id via reflection since it's auto-generated
        testAccount.setId(1L);
        
        createRequest = CreateAccountRequest.builder()
                .userId(1L)
                .accountType("SAVINGS")
                .initialCredit(BigDecimal.valueOf(100))
                .currency("USD")
                .overdraftLimit(BigDecimal.ZERO)
                .build();
        
        accountResponse = AccountResponse.builder()
                .id(1L)
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0001")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .currency("USD")
                .overdraftLimit(BigDecimal.ZERO)
                .build();
    }
    
    @Test
    void createAccount_ShouldCreateAccountSuccessfully() {
        // Given
        when(userServiceClient.userExists(anyLong())).thenReturn(true);
        when(accountNumberGenerator.generateAccountNumber(anyLong())).thenReturn("ACC-20231201-0001-0001");
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountMapper.parseAccountType(anyString())).thenReturn(Account.AccountType.SAVINGS);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(accountMapper.toResponse(any(Account.class))).thenReturn(accountResponse);
        
        // When
        AccountResponse result = accountService.createAccount(createRequest);
        
        // Then
        assertNotNull(result);
        assertEquals(accountResponse.getId(), result.getId());
        verify(userServiceClient).userExists(1L);
        verify(accountRepository).save(any(Account.class));
        verify(accountMapper).toResponse(any(Account.class));
    }
    
    @Test
    void createAccount_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Given
        when(userServiceClient.userExists(anyLong())).thenReturn(false);
        
        // When & Then
        assertThrows(UserNotFoundException.class, () -> accountService.createAccount(createRequest));
        verify(userServiceClient).userExists(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }
    
    @Test
    void getAccountById_ShouldReturnAccount_WhenAccountExists() {
        // Given
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(testAccount));
        when(accountMapper.toResponse(any(Account.class))).thenReturn(accountResponse);
        
        // When
        AccountResponse result = accountService.getAccountById(1L);
        
        // Then
        assertNotNull(result);
        assertEquals(accountResponse.getId(), result.getId());
        verify(accountRepository).findById(1L);
        verify(accountMapper).toResponse(testAccount);
    }
    
    @Test
    void getAccountById_ShouldThrowAccountNotFoundException_WhenAccountDoesNotExist() {
        // Given
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(1L));
        verify(accountRepository).findById(1L);
        verify(accountMapper, never()).toResponse(any(Account.class));
    }
    
    @Test
    void getAccountBalance_ShouldReturnBalance_WhenAccountExists() {
        // Given
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(testAccount));
        
        // When
        BigDecimal balance = accountService.getAccountBalance(1L);
        
        // Then
        assertNotNull(balance);
        assertEquals(BigDecimal.valueOf(1000), balance);
        verify(accountRepository).findById(1L);
    }
    
    @Test
    void getAccountBalance_ShouldThrowAccountNotFoundException_WhenAccountDoesNotExist() {
        // Given
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountBalance(1L));
        verify(accountRepository).findById(1L);
    }
    
    @Test
    void deleteAccount_ShouldDeleteAccount_WhenAccountHasZeroBalance() {
        // Given
        testAccount.setBalance(BigDecimal.ZERO);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(testAccount));
        
        // When
        accountService.deleteAccount(1L);
        
        // Then
        verify(accountRepository).findById(1L);
        verify(accountRepository).delete(testAccount);
    }
    
    @Test
    void deleteAccount_ShouldThrowException_WhenAccountHasNonZeroBalance() {
        // Given
        testAccount.setBalance(BigDecimal.valueOf(100));
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(testAccount));
        
        // When & Then
        assertThrows(Exception.class, () -> accountService.deleteAccount(1L));
        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).delete(any(Account.class));
    }
}
