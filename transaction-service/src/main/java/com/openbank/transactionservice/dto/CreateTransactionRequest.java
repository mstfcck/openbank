package com.openbank.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new transaction.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Transaction creation request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {

    @Schema(description = "Source account ID for the transaction (null for deposits from external sources)",
           example = "1")
    @JsonProperty("fromAccountId")
    private Long fromAccountId;

    @Schema(description = "Destination account ID for the transaction (null for withdrawals to external)",
           example = "2")
    @JsonProperty("toAccountId")
    private Long toAccountId;

    @Schema(description = "Transaction amount (must be positive)",
           example = "100.50")
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Transaction amount must have at most 2 decimal places")
    @JsonProperty("amount")
    private BigDecimal amount;

    @Schema(description = "Type of transaction",
           example = "TRANSFER",
           allowableValues = {"DEPOSIT", "WITHDRAWAL", "TRANSFER", "PAYMENT", "REFUND"})
    @NotBlank(message = "Transaction type is required")
    @JsonProperty("transactionType")
    private String transactionType;

    @Schema(description = "Transaction description",
           example = "Monthly salary payment")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Transaction currency (3-letter ISO code)",
           example = "USD",
           defaultValue = "USD")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter uppercase code")
    @Builder.Default
    @JsonProperty("currency")
    private String currency = "USD";

    @Schema(description = "Transaction fee (optional)",
           example = "2.50")
    @DecimalMin(value = "0.00", message = "Fee cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Fee must have at most 2 decimal places")
    @Builder.Default
    @JsonProperty("fee")
    private BigDecimal fee = BigDecimal.ZERO;

    @Schema(description = "External reference for the transaction",
           example = "PAY-123456789")
    @Size(max = 50, message = "External reference cannot exceed 50 characters")
    @JsonProperty("externalReference")
    private String externalReference;
}
