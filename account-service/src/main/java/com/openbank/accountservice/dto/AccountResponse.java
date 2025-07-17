package com.openbank.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Account information response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    
    @Schema(description = "Unique account identifier", 
           example = "1")
    @JsonProperty("id")
    private Long id;
    
    @Schema(description = "User ID who owns the account", 
           example = "1")
    @JsonProperty("userId")
    private Long userId;
    
    @Schema(description = "Account number", 
           example = "ACC-1234567890")
    @JsonProperty("accountNumber")
    private String accountNumber;
    
    @Schema(description = "Account type", 
           example = "SAVINGS")
    @JsonProperty("accountType")
    private String accountType;
    
    @Schema(description = "Current account balance", 
           example = "1500.00")
    @JsonProperty("balance")
    private BigDecimal balance;
    
    @Schema(description = "Account status", 
           example = "ACTIVE")
    @JsonProperty("status")
    private String status;
    
    @Schema(description = "Account currency", 
           example = "USD")
    @JsonProperty("currency")
    private String currency;
    
    @Schema(description = "Overdraft limit", 
           example = "500.00")
    @JsonProperty("overdraftLimit")
    private BigDecimal overdraftLimit;
    
    @Schema(description = "Account opening date", 
           example = "2023-01-01T10:00:00")
    @JsonProperty("openedAt")
    private LocalDateTime openedAt;
    
    @Schema(description = "Account closing date", 
           example = "2023-12-31T10:00:00")
    @JsonProperty("closedAt")
    private LocalDateTime closedAt;
    
    @Schema(description = "Account creation timestamp", 
           example = "2023-01-01T10:00:00")
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @Schema(description = "Account last update timestamp", 
           example = "2023-01-02T10:00:00")
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
