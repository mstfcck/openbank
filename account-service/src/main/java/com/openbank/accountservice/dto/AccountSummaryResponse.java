package com.openbank.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Account summary information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryResponse {
    
    @Schema(description = "Unique account identifier", 
           example = "1")
    @JsonProperty("id")
    private Long id;
    
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
}
