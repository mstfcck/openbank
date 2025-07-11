package com.openbank.accountservice.client;

import com.openbank.accountservice.exception.ExternalServiceException;
import com.openbank.accountservice.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Slf4j
public class UserServiceClient {
    
    private final RestTemplate restTemplate;
    private final String userServiceUrl;
    
    public UserServiceClient(RestTemplate restTemplate, 
                            @Value("${app.services.user-service.url:http://localhost:8091}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }
    
    public UserResponse getUser(Long userId) {
        try {
            log.debug("Fetching user with ID: {}", userId);
            String url = userServiceUrl + "/api/users/" + userId;
            UserResponse user = restTemplate.getForObject(url, UserResponse.class);
            
            if (user == null) {
                throw new UserNotFoundException(userId);
            }
            
            log.debug("Successfully fetched user: {}", user);
            return user;
        } catch (HttpClientErrorException.NotFound e) {
            log.error("User not found with ID: {}", userId);
            throw new UserNotFoundException(userId);
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", userId, e);
            throw new ExternalServiceException("Failed to fetch user information", e);
        }
    }
    
    public boolean userExists(Long userId) {
        try {
            UserResponse user = getUser(userId);
            return user != null;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}
