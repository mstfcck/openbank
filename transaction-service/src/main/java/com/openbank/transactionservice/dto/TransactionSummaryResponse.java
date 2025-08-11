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
 * Simplified transaction summary response DTO.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Transaction summary information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryResponse {

    @Schema(description = "Unique transaction identifier",
           example = "1")
    @JsonProperty("id")
    private Long id;

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

    @Schema(description = "Transaction creation timestamp",
           example = "2023-01-01T10:00:00")
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @Schema(description = "Other account involved in the transaction",
           example = "2")
    @JsonProperty("otherAccountId")
    private Long otherAccountId;

    @Schema(description = "Direction of the transaction from perspective of the queried account",
           example = "OUTGOING")
    @JsonProperty("direction")
    private String direction; // INCOMING, OUTGOING, INTERNAL
}
