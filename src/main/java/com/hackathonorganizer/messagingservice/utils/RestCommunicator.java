package com.hackathonorganizer.messagingservice.utils;

import com.hackathonorganizer.messagingservice.exception.MessagingException;
import com.hackathonorganizer.messagingservice.utils.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestCommunicator {

    private final RestTemplate restTemplate;

    @Value("${API_GATEWAY_URL}")
    private String gatewayUrl;

    public UserResponse getUserDetails(String keycloakId) {

        log.info("Trying to fetch details user with id: {} ", keycloakId);

        try {

            return restTemplate.getForObject(gatewayUrl + "/api/v1/read/users/keycloak/" + keycloakId,
                    UserResponse.class);
        } catch (HttpServerErrorException.ServiceUnavailable ex) {

            log.warn("User service is unavailable. Can't get user details. {}", ex.getMessage());
            throw new MessagingException("User service is unavailable. Can't get user details",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
