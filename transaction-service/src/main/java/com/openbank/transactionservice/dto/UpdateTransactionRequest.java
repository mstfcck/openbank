package com.openbank.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Transaction update request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequest {
    
    @Schema(description = "Transaction description", 
           example = "Updated payment for services")
    @JsonProperty("description")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @Schema(description = "Transaction fee", 
           example = "5.00")
    @JsonProperty("fee")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fee must be non-negative")
    private BigDecimal fee;
    
    @Schema(description = "Transaction status", 
           example = "COMPLETED")
    @JsonProperty("status")
    private String status;
}
