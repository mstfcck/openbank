package com.openbank.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Account update request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {
    
    @Schema(description = "Account status", 
           example = "ACTIVE",
           allowableValues = {"ACTIVE", "INACTIVE", "CLOSED", "FROZEN"})
    @JsonProperty("status")
    private String status;
    
    @Schema(description = "Account currency", 
           example = "USD")
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
