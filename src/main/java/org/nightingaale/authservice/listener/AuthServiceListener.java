package org.nightingaale.authservice.listener;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.nightingaale.authservice.dto.*;
import org.nightingaale.authservice.dto.UserRegistrationDto;
import org.nightingaale.authservice.entity.*;
import org.nightingaale.authservice.mapper.*;
import org.nightingaale.authservice.repository.*;
import org.nightingaale.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceListener {

    private final KafkaTemplate<String, UserRegistrationDto> userRegistrationTemplate;
    private final KafkaTemplate<String, UserRemoveDto> userRemoveTemplate;
    private final AuthService authService;
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserRegistrationRepository userRegistrationRepository;
    private final UserLoginMapper userLoginMapper;
    private final UserLoginRepository userLoginRepository;
    private final UserRemoveMapper userRemoveMapper;
    private final UserRemoveRepository userRemoveRepository;

    public void saveRegistrationEvent(UserRegistrationDto event) {
        try {
            if (userRegistrationRepository.existsById(event.getCorrelationId())) {
                log.warn("User with correlationID: {} already exists]", event.getCorrelationId());
                return;
            }

            String keycloakUserId = authService.registerUser(event.getUsername(), event.getEmail(), event.getPassword());

            UserRegistrationDto registrationEvent = new UserRegistrationDto(event.getCorrelationId(), keycloakUserId, event.getEmail(), event.getUsername(), event.getPassword());

            UserRegistrationEntity entity = userRegistrationMapper.toEntity(registrationEvent);
            userRegistrationRepository.save(entity);

            userRegistrationTemplate.send("user-registration", registrationEvent);
            log.info("[User has been successfully registered! Keycloak ID: {}]", keycloakUserId);
        } catch (Exception e) {
            log.error("[User's registration failed. Error: {}]", e.getMessage());
        }
    }

    @KafkaListener(topics = "user-registered", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRegistered")
    public void saveRegisteredEvent(UserRegisteredDto event) {
        if (event.isUserExists()) {
            log.info("[User has been successfully registered with ID: {}]", event.getCorrelationId());
        }
    }

    @KafkaListener(topics = "user-removed", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRemoved")
    public void saveRemovedEvent(UserRemovedDto event) {
        try {
            if (!event.isUserExists()) {
                log.warn("[User with ID: {} successfully removed]", event.getUserId());
                return;
            }
            userRegistrationRepository.deleteByUserId(event.getUserId());
            authService.removeUser(event.getUserId());
        } catch (Exception e) {
            log.error("Removed failed for user {}. Error: {}]", event.getUserId(), e.getMessage());
        }
    }

    @Transactional
    public void saveRemoveEvent(UserRemoveDto event) {
        try {
            if (!userRegistrationRepository.existsByUserId(event.getUserId())) {
                log.warn("[User with ID: {}", event.getUserId() + "does not exist]");
                return;
            }

            UserRemoveEntity entity = userRemoveMapper.toEntity(event);
            userRemoveRepository.save(entity);

            userRemoveTemplate.send("user-remove", event);
        } catch (Exception e) {
            log.error("[Logout failed. Error: {}]", e.getMessage());
        }
    }

    public void saveLoginEvent(UserLoginDto event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(event.getUsername(), event.getPassword());

            log.info("[User has successfully logged in! Access token has been generated]");
            Response.ok(tokenResponse);

            UserLoginEntity entity = userLoginMapper.toEntity(event);
            userLoginRepository.save(entity);

        } catch (Exception e) {
            log.error("[User's login failed. Error: {}]", e.getMessage());
            ResponseEntity.status(401).build();
        }
    }
}
