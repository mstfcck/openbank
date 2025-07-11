package com.openbank.accountservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8092}")
    private int serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Account Service API")
                        .description("Account Service for Open Bank Application")
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
                                .url("https://api.openbank.com")
                                .description("Production server")));
    }
}
