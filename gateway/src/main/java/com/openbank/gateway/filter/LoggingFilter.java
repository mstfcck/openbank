package com.openbank.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    /**
     * This filter logs the request and response details.
     * It generates a unique request ID for tracking purposes.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = UUID.randomUUID().toString();

        logger.info("Request: {} {} [ID: {}]",
                request.getMethod(),
                request.getURI(),
                requestId);

        long startTime = System.currentTimeMillis();

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    long endTime = System.currentTimeMillis();
                    logger.info("Response: {} [ID: {}] completed in {} ms",
                            exchange.getResponse().getStatusCode(),
                            requestId,
                            (endTime - startTime));
                }));
    }

    /**
     * This filter should run before the authentication filter
     * to log the request before any authentication checks.
     */
    @Override
    public int getOrder() {
        return -1; // High precedence to execute this filter early
    }
}