package com.openbank.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Transaction validation response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionValidationResponse {
    
    @Schema(description = "Whether the transaction is valid", example = "true")
    @JsonProperty("valid")
    private boolean valid;
    
    @Schema(description = "Validation message", example = "Transaction is valid")
    @JsonProperty("message")
    private String message;
    
    @Schema(description = "Error code if validation failed", example = "INSUFFICIENT_FUNDS")
    @JsonProperty("errorCode")
    private String errorCode;
}
