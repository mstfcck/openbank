package com.openbank.transactionservice.client;

import com.openbank.transactionservice.exception.AccountNotFoundException;
import com.openbank.transactionservice.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Client for communicating with the Account Service.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class AccountServiceClient {

    private final RestTemplate restTemplate;
    private final String accountServiceUrl;

    public AccountServiceClient(RestTemplate restTemplate,
                               @Value("${app.services.account-service.url:http://localhost:8090}") String accountServiceUrl) {
        this.restTemplate = restTemplate;
        this.accountServiceUrl = accountServiceUrl;
    }

    /**
     * Get account information by account ID
     */
    public AccountResponse getAccount(Long accountId) {
        try {
            log.debug("Fetching account with ID: {}", accountId);
            String url = accountServiceUrl + "/api/accounts/" + accountId;
            AccountResponse account = restTemplate.getForObject(url, AccountResponse.class);

            if (account == null) {
                throw new AccountNotFoundException(accountId);
            }

            log.debug("Successfully fetched account: {}", account.getAccountNumber());
            return account;
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Account not found with ID: {}", accountId);
            throw new AccountNotFoundException(accountId);
        } catch (Exception e) {
            log.error("Error fetching account with ID: {}", accountId, e);
            throw new ExternalServiceException("Failed to fetch account information", e);
        }
    }

    /**
     * Check if account exists
     */
    public boolean accountExists(Long accountId) {
        try {
            AccountResponse account = getAccount(accountId);
            return account != null;
        } catch (AccountNotFoundException e) {
            return false;
        }
    }

    /**
     * Validate if account can perform a debit operation
     */
    public boolean canDebit(Long accountId, BigDecimal amount) {
        try {
            AccountResponse account = getAccount(accountId);
            
            // Check if account is active
            if (!"ACTIVE".equals(account.getStatus())) {
                log.warn("Cannot debit from inactive account: {}", accountId);
                return false;
            }

            // Check available balance (including overdraft limit)
            BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
            boolean canDebit = availableBalance.compareTo(amount) >= 0;
            
            if (!canDebit) {
                log.warn("Insufficient funds for account {}: available={}, requested={}", 
                        accountId, availableBalance, amount);
            }
            
            return canDebit;
        } catch (Exception e) {
            log.error("Error checking debit capability for account {}", accountId, e);
            return false;
        }
    }

    /**
     * Validate if account can receive a credit operation
     */
    public boolean canCredit(Long accountId) {
        try {
            AccountResponse account = getAccount(accountId);
            
            // Check if account is active and not closed
            boolean canCredit = "ACTIVE".equals(account.getStatus());
            
            if (!canCredit) {
                log.warn("Cannot credit to inactive account: {}", accountId);
            }
            
            return canCredit;
        } catch (Exception e) {
            log.error("Error checking credit capability for account {}", accountId, e);
            return false;
        }
    }

    /**
     * Update account balance (debit operation)
     */
    public void debitAccount(Long accountId, BigDecimal amount, String description) {
        try {
            log.debug("Debiting account {} with amount {}", accountId, amount);
            
            String url = accountServiceUrl + "/api/accounts/" + accountId + "/debit";
            
            Map<String, Object> request = new HashMap<>();
            request.put("amount", amount);
            request.put("description", description);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            
            log.debug("Successfully debited account {}", accountId);
        } catch (HttpClientErrorException e) {
            log.error("Error debiting account {}: {}", accountId, e.getMessage());
            throw new ExternalServiceException("Failed to debit account: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error debiting account {}", accountId, e);
            throw new ExternalServiceException("Failed to debit account", e);
        }
    }

    /**
     * Update account balance (credit operation)
     */
    public void creditAccount(Long accountId, BigDecimal amount, String description) {
        try {
            log.debug("Crediting account {} with amount {}", accountId, amount);
            
            String url = accountServiceUrl + "/api/accounts/" + accountId + "/credit";
            
            Map<String, Object> request = new HashMap<>();
            request.put("amount", amount);
            request.put("description", description);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            
            log.debug("Successfully credited account {}", accountId);
        } catch (HttpClientErrorException e) {
            log.error("Error crediting account {}: {}", accountId, e.getMessage());
            throw new ExternalServiceException("Failed to credit account: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error crediting account {}", accountId, e);
            throw new ExternalServiceException("Failed to credit account", e);
        }
    }

    /**
     * Validate accounts involved in a transfer operation
     */
    public void validateTransferAccounts(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        // Validate source account
        if (!canDebit(fromAccountId, amount)) {
            throw new ExternalServiceException("Source account cannot be debited");
        }

        // Validate destination account
        if (!canCredit(toAccountId)) {
            throw new ExternalServiceException("Destination account cannot be credited");
        }

        // Ensure accounts are different
        if (fromAccountId.equals(toAccountId)) {
            throw new ExternalServiceException("Source and destination accounts cannot be the same");
        }
    }
}
