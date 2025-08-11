package com.openbank.transactionservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI/Swagger documentation.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8092}")
    private int serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        // Define the Basic Authentication security scheme
        SecurityScheme basicAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .description("Basic Authentication (username: user, password: check server logs for generated password)");
        
        // Create security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("basicAuth");
        
        return new OpenAPI()
                .info(new Info()
                        .title("Transaction Service API")
                        .description("Transaction Service for Open Bank Application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Open Bank Development Team")
                                .email("dev@openbank.com")
                                .url("https://openbank.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development server"),
                        new Server()
                                .url("https://api.openbank.com/transactions")
                                .description("Production server")))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", basicAuth))
                .addSecurityItem(securityRequirement);
    }
}
