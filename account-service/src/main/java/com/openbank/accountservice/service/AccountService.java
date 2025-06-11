package com.openbank.accountservice.service;

import com.openbank.accountservice.entity.Account;
import com.openbank.accountservice.model.AccountRequest;
import com.openbank.accountservice.model.AccountResponse;
import com.openbank.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        Account account = Account.builder()
                .userId(request.getUserId())
                // set other fields from request if needed
                .build();
        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    public Optional<AccountResponse> getAccount(Long id) {
        return accountRepository.findById(id).map(this::mapToResponse);
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                // map other fields as needed
                .build();
    }
}
