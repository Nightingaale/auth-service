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
                log.warn("User with correlationId already exists: " + event.getCorrelationId());
                return;
            }

            String keycloakUserId = authService.registerUser(
                    event.getUsername(),
                    event.getEmail(),
                    event.getPassword()
            );

            UserRegistrationDto registrationEvent = new UserRegistrationDto(
                    event.getCorrelationId(),
                    keycloakUserId,
                    event.getEmail(),
                    event.getUsername(),
                    event.getPassword()
            );

            UserRegistrationEntity entity = userRegistrationMapper.toEntity(registrationEvent);
            userRegistrationRepository.save(entity);

            userRegistrationTemplate.send("user-registration", registrationEvent);
            log.info("[User has been successfully registered! Keycloak ID: " + keycloakUserId + "]");
        } catch (Exception e) {
            log.error("[User's registration failed. Error: " + e.getMessage() + "]");
        }
    }

    @KafkaListener(topics = "user-registered", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRegistered")
    public void saveRegisteredEvent(UserRegisteredDto event) {
        if (event.isUserExists()) {
            log.info("[User has been successfully registered with ID: [" + event.getUserId() + "]");;
        }
    }

    @KafkaListener(topics = "user-removed", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRemoved")
    public ResponseEntity<?> saveRemovedEvent(UserRemovedDto event) {
        try {
            if (!event.isUserExists()) {
                authService.removeUser(event.getUserId());
                log.info("[User with ID: " + event.getUserId() + " successfully removed]");
            }
            return ResponseEntity.ok("[User logged out successfully!]");
        } catch (Exception e) {
            log.info("[Logout failed. Error: " + e.getMessage() + "]");
            return ResponseEntity.status(500).body("[Logout failed: " + e.getMessage() + "]");
        }
    }

    public void saveRemoveEvent(UserRemoveDto event) {
        try {
            if (!userRegistrationRepository.existsByUserId(event.getUserId())) {
                log.warn("User's with ID: " + event.getUserId() + " is not exists");
                return;
            }

            UserRemoveEntity entity = userRemoveMapper.toEntity(event);
            userRemoveRepository.save(entity);
            userRegistrationRepository.deleteByUserId(event.getUserId());

            userRemoveTemplate.send("user-remove", event);
            log.info("[User has been successfully removed!]");
        } catch (Exception e) {
            log.error("[Logout failed. Error: [ " + e.getMessage() + "]");
        }
    }



    public void saveLoginEvent(UserLoginDto event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(
                    event.getUsername(),
                    event.getPassword()
            );

            log.info("[User has successfully logged in! Access token has been generated]");
            Response.ok(tokenResponse);

            UserLoginEntity entity = userLoginMapper.toEntity(event);
            userLoginRepository.save(entity);

        } catch (Exception e) {
            log.error("[User's login failed. Error: " + e.getMessage() + "]");
            ResponseEntity.status(401).build();
        }
    }
}
