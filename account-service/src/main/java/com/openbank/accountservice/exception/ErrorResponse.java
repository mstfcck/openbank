package com.openbank.accountservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Error response structure")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    @Schema(description = "Timestamp when the error occurred", 
           example = "2023-01-01T10:00:00")
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @Schema(description = "HTTP status code", 
           example = "400")
    @JsonProperty("status")
    private int status;
    
    @Schema(description = "Error type", 
           example = "Validation Failed")
    @JsonProperty("error")
    private String error;
    
    @Schema(description = "Error message", 
           example = "Input validation failed")
    @JsonProperty("message")
    private String message;
    
    @Schema(description = "Request path where the error occurred", 
           example = "/api/accounts")
    @JsonProperty("path")
    private String path;
    
    @Schema(description = "Validation errors for specific fields")
    @JsonProperty("validationErrors")
    private Map<String, String> validationErrors;
}
