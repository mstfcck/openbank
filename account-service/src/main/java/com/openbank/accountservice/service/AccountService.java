package com.openbank.accountservice.service;

import com.openbank.accountservice.client.UserServiceClient;
import com.openbank.accountservice.dto.*;
import com.openbank.accountservice.entity.Account;
import com.openbank.accountservice.exception.AccountNotFoundException;
import com.openbank.accountservice.exception.InvalidAccountOperationException;
import com.openbank.accountservice.exception.UserNotFoundException;
import com.openbank.accountservice.repository.AccountRepository;
import com.openbank.accountservice.util.AccountMapper;
import com.openbank.accountservice.util.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final UserServiceClient userServiceClient;
    private final AccountMapper accountMapper;
    private final AccountNumberGenerator accountNumberGenerator;
    
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account for user ID: {}", request.getUserId());
        
        // Validate user exists
        if (!userServiceClient.userExists(request.getUserId())) {
            throw new UserNotFoundException(request.getUserId());
        }
        
        // Generate unique account number
        String accountNumber = accountNumberGenerator.generateAccountNumber(request.getUserId());
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = accountNumberGenerator.generateAccountNumber(request.getUserId());
        }
        
        // Parse account type
        Account.AccountType accountType = accountMapper.parseAccountType(request.getAccountType());
        
        // Create account
        Account account = Account.builder()
                .userId(request.getUserId())
                .accountNumber(accountNumber)
                .accountType(accountType)
                .balance(BigDecimal.ZERO)
                .status(Account.AccountStatus.ACTIVE)
                .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .overdraftLimit(request.getOverdraftLimit() != null ? request.getOverdraftLimit() : BigDecimal.ZERO)
                .build();
        
        // Handle initial credit before saving
        if (request.getInitialCredit() != null && request.getInitialCredit().compareTo(BigDecimal.ZERO) > 0) {
            account.deposit(request.getInitialCredit());
            log.info("Initial credit of {} will be added to account {}", request.getInitialCredit(), account.getAccountNumber());
        }
        
        Account savedAccount = accountRepository.save(account);
        
        log.info("Account created successfully: {}", savedAccount.getAccountNumber());
        return accountMapper.toResponse(savedAccount);
    }
    
    @Transactional(readOnly = true)
    public Optional<AccountResponse> getAccount(Long id) {
        log.debug("Fetching account with ID: {}", id);
        return accountRepository.findById(id)
                .map(accountMapper::toResponse);
    }
    
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long id) {
        log.debug("Fetching account with ID: {}", id);
        return accountRepository.findById(id)
                .map(accountMapper::toResponse)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }
    
    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {
        log.debug("Fetching account with number: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .map(accountMapper::toResponse)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        log.debug("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        return accountMapper.toResponseList(accounts);
    }
    
    @Transactional(readOnly = true)
    public PagedResponse<AccountResponse> getAllAccountsPaged(Pageable pageable) {
        log.debug("Fetching all accounts with pagination");
        Page<Account> accountPage = accountRepository.findAll(pageable);
        Page<AccountResponse> responsePage = accountPage.map(accountMapper::toResponse);
        return accountMapper.toPagedResponse(responsePage);
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUserId(Long userId) {
        log.debug("Fetching accounts for user ID: {}", userId);
        
        // Validate user exists
        if (!userServiceClient.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accountMapper.toResponseList(accounts);
    }
    
    @Transactional(readOnly = true)
    public PagedResponse<AccountResponse> getAccountsByUserIdPaged(Long userId, Pageable pageable) {
        log.debug("Fetching accounts for user ID: {} with pagination", userId);
        
        // Validate user exists
        if (!userServiceClient.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        
        Page<Account> accountPage = accountRepository.findByUserId(userId, pageable);
        Page<AccountResponse> responsePage = accountPage.map(accountMapper::toResponse);
        return accountMapper.toPagedResponse(responsePage);
    }
    
    @Transactional
    public AccountResponse updateAccount(Long id, UpdateAccountRequest request) {
        log.info("Updating account with ID: {}", id);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        
        // Update fields if provided
        if (request.getStatus() != null) {
            Account.AccountStatus newStatus = accountMapper.parseAccountStatus(request.getStatus());
            
            // Validate status transition
            if (account.getStatus() == Account.AccountStatus.CLOSED && newStatus != Account.AccountStatus.CLOSED) {
                throw new InvalidAccountOperationException("Cannot reopen closed account");
            }
            
            account.setStatus(newStatus);
            if (newStatus == Account.AccountStatus.CLOSED) {
                account.close();
            }
        }
        
        if (request.getCurrency() != null) {
            account.setCurrency(request.getCurrency());
        }
        
        if (request.getOverdraftLimit() != null) {
            account.setOverdraftLimit(request.getOverdraftLimit());
        }
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully: {}", updatedAccount.getAccountNumber());
        
        return accountMapper.toResponse(updatedAccount);
    }
    
    @Transactional
    public void deleteAccount(Long id) {
        log.info("Deleting account with ID: {}", id);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        
        // Check if account can be deleted
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InvalidAccountOperationException("Cannot delete account with non-zero balance");
        }
        
        accountRepository.delete(account);
        log.info("Account deleted successfully: {}", account.getAccountNumber());
    }
    
    @Transactional
    public AccountResponse closeAccount(Long id) {
        log.info("Closing account with ID: {}", id);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        
        if (account.getStatus() == Account.AccountStatus.CLOSED) {
            throw new InvalidAccountOperationException("Account is already closed");
        }
        
        account.close();
        Account closedAccount = accountRepository.save(account);
        log.info("Account closed successfully: {}", closedAccount.getAccountNumber());
        
        return accountMapper.toResponse(closedAccount);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(Long id) {
        log.debug("Fetching balance for account ID: {}", id);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        
        return account.getBalance();
    }
    
    @Transactional(readOnly = true)
    public List<AccountSummaryResponse> getAccountSummaries(Long userId) {
        log.debug("Fetching account summaries for user ID: {}", userId);
        
        // Validate user exists
        if (!userServiceClient.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accountMapper.toSummaryResponseList(accounts);
    }
    
    @Transactional(readOnly = true)
    public long getAccountCount() {
        return accountRepository.count();
    }
    
    @Transactional(readOnly = true)
    public long getAccountCountByUserId(Long userId) {
        return accountRepository.countByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public long getAccountCountByStatus(Account.AccountStatus status) {
        return accountRepository.countByStatus(status);
    }
}
