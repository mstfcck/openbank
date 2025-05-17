package com.openbank.gateway.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

    /**
     * Handles exceptions thrown during request processing.
     *
     * @param exchange the server web exchange
     * @param ex       the exception
     * @return a Mono that completes when the error response is sent
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof NotFoundException) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatusCode());
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", response.getStatusCode() != null ? response.getStatusCode().value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Unexpected error occurred");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", exchange.getRequest().getURI().getPath());

        logger.error("Gateway error: {}", ex.getMessage(), ex);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(
                        errorResponse.toString().getBytes()
                ))
        );
    }
}