package com.openbank.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for overall transaction statistics.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Transaction statistics information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatisticsResponse {

    @Schema(description = "Total number of transactions",
           example = "1000")
    @JsonProperty("totalTransactions")
    private long totalTransactions;

    @Schema(description = "Number of completed transactions",
           example = "950")
    @JsonProperty("completedTransactions")
    private long completedTransactions;

    @Schema(description = "Number of pending transactions",
           example = "30")
    @JsonProperty("pendingTransactions")
    private long pendingTransactions;

    @Schema(description = "Number of failed transactions",
           example = "20")
    @JsonProperty("failedTransactions")
    private long failedTransactions;

    @Schema(description = "Success rate as percentage",
           example = "95.0")
    @JsonProperty("successRate")
    private double successRate;
}
