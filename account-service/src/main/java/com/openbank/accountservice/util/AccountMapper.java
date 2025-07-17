package com.openbank.accountservice.util;

import com.openbank.accountservice.dto.*;
import com.openbank.accountservice.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountMapper {
    
    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .currency(account.getCurrency())
                .overdraftLimit(account.getOverdraftLimit())
                .openedAt(account.getOpenedAt())
                .closedAt(account.getClosedAt())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
    
    public AccountSummaryResponse toSummaryResponse(Account account) {
        return AccountSummaryResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .currency(account.getCurrency())
                .build();
    }
    
    public List<AccountResponse> toResponseList(List<Account> accounts) {
        return accounts.stream()
                .map(this::toResponse)
                .toList();
    }
    
    public List<AccountSummaryResponse> toSummaryResponseList(List<Account> accounts) {
        return accounts.stream()
                .map(this::toSummaryResponse)
                .toList();
    }
    
    public <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
    
    public Account.AccountType parseAccountType(String accountType) {
        if (accountType == null || accountType.isEmpty()) {
            return Account.AccountType.SAVINGS;
        }
        
        try {
            return Account.AccountType.valueOf(accountType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
    }
    
    public Account.AccountStatus parseAccountStatus(String status) {
        if (status == null || status.isEmpty()) {
            return Account.AccountStatus.ACTIVE;
        }
        
        try {
            return Account.AccountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account status: " + status);
        }
    }
}
