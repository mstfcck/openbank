package com.openbank.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for transaction information.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Transaction information response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    @Schema(description = "Unique transaction identifier",
           example = "1")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Source account ID",
           example = "1")
    @JsonProperty("fromAccountId")
    private Long fromAccountId;

    @Schema(description = "Destination account ID",
           example = "2")
    @JsonProperty("toAccountId")
    private Long toAccountId;

    @Schema(description = "Transaction amount",
           example = "100.50")
    @JsonProperty("amount")
    private BigDecimal amount;

    @Schema(description = "Transaction type",
           example = "TRANSFER")
    @JsonProperty("transactionType")
    private String transactionType;

    @Schema(description = "Transaction status",
           example = "COMPLETED")
    @JsonProperty("status")
    private String status;

    @Schema(description = "Transaction description",
           example = "Monthly salary payment")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Unique transaction reference",
           example = "TXN-1234567890")
    @JsonProperty("reference")
    private String reference;

    @Schema(description = "Transaction currency",
           example = "USD")
    @JsonProperty("currency")
    private String currency;

    @Schema(description = "Transaction fee",
           example = "2.50")
    @JsonProperty("fee")
    private BigDecimal fee;

    @Schema(description = "Total amount including fees",
           example = "103.00")
    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @Schema(description = "Transaction creation timestamp",
           example = "2023-01-01T10:00:00")
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @Schema(description = "Transaction last update timestamp",
           example = "2023-01-01T10:05:00")
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @Schema(description = "Transaction processing timestamp",
           example = "2023-01-01T10:01:00")
    @JsonProperty("processedAt")
    private LocalDateTime processedAt;

    @Schema(description = "Error message if transaction failed",
           example = "Insufficient funds")
    @JsonProperty("errorMessage")
    private String errorMessage;

    @Schema(description = "User who created the transaction",
           example = "john.doe")
    @JsonProperty("createdBy")
    private String createdBy;

    @Schema(description = "User who last modified the transaction",
           example = "system")
    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy;
}
