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

import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final List<String> openApiEndpoints = Arrays.asList(
            "/actuator",
            "/v3/api-docs",
            "/swagger-ui",
            "/webjars",
            "/fallback"
    );

    /**
     * This filter checks if the request is authenticated.
     * It skips authentication for open endpoints and adds a custom header
     * with the authenticated user's information.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Skip auth for open endpoints
        if (isOpenEndpoint(path)) {
            return chain.filter(exchange);
        }

        // Log the auth check (JWT validation is handled by Spring Security)
        logger.debug("Authenticating request to: {}", path);

        // Add custom header with authenticated user info
        // In a real implementation, you would extract this from the security context
        return exchange.getPrincipal()
                .flatMap(principal -> {
                    String userId = principal.getName();
                    ServerHttpRequest modifiedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-User-ID", userId)
                            .build();
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                });
    }

    private boolean isOpenEndpoint(String path) {
        return openApiEndpoints.stream()
                .anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        return 0; // After logging, before other filters
    }
}