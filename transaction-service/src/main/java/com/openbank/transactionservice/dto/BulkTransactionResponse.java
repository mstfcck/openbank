package com.openbank.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Bulk transaction processing response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkTransactionResponse {
    
    @Schema(description = "List of successfully processed transactions")
    @JsonProperty("successfulTransactions")
    private List<TransactionResponse> successfulTransactions;
    
    @Schema(description = "List of failed transaction requests")
    @JsonProperty("failedTransactions")
    private List<CreateTransactionRequest> failedTransactions;
    
    @Schema(description = "Total number of transactions processed", example = "100")
    @JsonProperty("totalProcessed")
    private int totalProcessed;
    
    @Schema(description = "Number of successfully processed transactions", example = "95")
    @JsonProperty("successCount")
    private int successCount;
    
    @Schema(description = "Number of failed transactions", example = "5")
    @JsonProperty("failureCount")
    private int failureCount;
}
