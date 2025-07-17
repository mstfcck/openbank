package com.openbank.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Account creation request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    
    @Schema(description = "User ID who owns the account", 
           example = "1")
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    @JsonProperty("userId")
    private Long userId;
    
    @Schema(description = "Account type", 
           example = "SAVINGS",
           allowableValues = {"CHECKING", "SAVINGS", "BUSINESS", "INVESTMENT"})
    @NotNull(message = "Account type is required")
    @JsonProperty("accountType")
    private String accountType;
    
    @Schema(description = "Initial deposit amount", 
           example = "100.00")
    @DecimalMin(value = "0.0", inclusive = false, message = "Initial credit must be positive")
    @Digits(integer = 17, fraction = 2, message = "Invalid amount format")
    @JsonProperty("initialCredit")
    private BigDecimal initialCredit;
    
    @Schema(description = "Account currency", 
           example = "USD",
           defaultValue = "USD")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be uppercase 3-letter code")
    @JsonProperty("currency")
    private String currency;
    
    @Schema(description = "Overdraft limit", 
           example = "500.00")
    @DecimalMin(value = "0.0", inclusive = true, message = "Overdraft limit cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Invalid overdraft limit format")
    @JsonProperty("overdraftLimit")
    private BigDecimal overdraftLimit;
}
