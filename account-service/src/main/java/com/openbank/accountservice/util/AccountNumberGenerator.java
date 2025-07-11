package com.openbank.accountservice.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AccountNumberGenerator {
    
    private static final String ACCOUNT_PREFIX = "ACC";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    public String generateAccountNumber() {
        String dateStr = LocalDateTime.now().format(DATE_FORMATTER);
        String randomNumber = String.format("%06d", RANDOM.nextInt(1000000));
        return ACCOUNT_PREFIX + "-" + dateStr + "-" + randomNumber;
    }
    
    public String generateAccountNumber(Long userId) {
        String dateStr = LocalDateTime.now().format(DATE_FORMATTER);
        String userIdStr = String.format("%04d", userId % 10000);
        String randomNumber = String.format("%04d", RANDOM.nextInt(10000));
        return ACCOUNT_PREFIX + "-" + dateStr + "-" + userIdStr + "-" + randomNumber;
    }
}
