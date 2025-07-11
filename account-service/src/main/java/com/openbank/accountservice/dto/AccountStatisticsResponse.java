package com.openbank.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Account statistics response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatisticsResponse {
    
    @Schema(description = "Total number of accounts", 
           example = "1000")
    @JsonProperty("totalAccounts")
    private long totalAccounts;
    
    @Schema(description = "Number of active accounts", 
           example = "850")
    @JsonProperty("activeAccounts")
    private long activeAccounts;
    
    @Schema(description = "Number of closed accounts", 
           example = "100")
    @JsonProperty("closedAccounts")
    private long closedAccounts;
    
    @Schema(description = "Number of frozen accounts", 
           example = "50")
    @JsonProperty("frozenAccounts")
    private long frozenAccounts;
}
