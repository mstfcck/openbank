package com.openbank.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for account-specific transaction statistics.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Account transaction statistics information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransactionStatisticsResponse {

    @Schema(description = "Account identifier",
           example = "1")
    @JsonProperty("accountId")
    private Long accountId;

    @Schema(description = "Total number of transactions for this account",
           example = "150")
    @JsonProperty("totalTransactions")
    private long totalTransactions;

    @Schema(description = "Net amount of transactions for this month",
           example = "2500.75")
    @JsonProperty("netAmountThisMonth")
    private BigDecimal netAmountThisMonth;

    @Schema(description = "Largest transaction amount for this account",
           example = "5000.00")
    @JsonProperty("largestTransaction")
    private BigDecimal largestTransaction;
}
