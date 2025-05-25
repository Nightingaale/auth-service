package org.nightingaale.authservice.listener;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
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

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthServiceListener {

    private final KafkaTemplate<String, UserRegistrationDto> userRegistrationTemplate;
    private final KafkaTemplate<String, UserRemoveDto> userRemoveTemplate;
    private final AuthService authService;
    private static final Logger logger = Logger.getLogger(AuthServiceListener.class.getName());
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserRegistrationRepository userRegistrationRepository;
    private final UserLoginMapper userLoginMapper;
    private final UserLoginRepository userLoginRepository;
    private final UserRemoveMapper userRemoveMapper;
    private final UserRemoveRepository userRemoveRepository;

    @Transactional
    public void saveRegistrationEvent(UserRegistrationDto event) {
        try {
            if (userRegistrationRepository.existsById(event.getCorrelationId())) {
                logger.warning("User with correlationId already exists: " + event.getCorrelationId());
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
            logger.info("[User has been successfully registered! Keycloak ID: " + keycloakUserId + "]");
        } catch (Exception e) {
            logger.severe("[User's registration failed. Error: " + e.getMessage() + "]");
        }
    }

    @KafkaListener(topics = "user-registered", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRegistered")
    @Transactional
    public void saveRegisteredEvent(UserRegisteredDto event) {
        if (event.isUserExists()) {
            logger.info("[User has been successfully registered with ID: [" + event.getUserId() + "]");;
        }
    }

    @KafkaListener(topics = "user-removed", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRemoved")
    @Transactional
    public ResponseEntity<?> saveRemovedEvent(UserRemovedDto event) {
        try {
            if (!event.isUserExists()) {
                authService.removeUser(event.getUserId());
                logger.info("[User with ID: " + event.getUserId() + " successfully removed.]");
            }
            return ResponseEntity.ok("[User logged out successfully!]");
        } catch (Exception e) {
            logger.info("[Logout failed. Error: " + e.getMessage() + "]");
            return ResponseEntity.status(500).body("[Logout failed: " + e.getMessage() + "]");
        }
    }

    @Transactional
    public void saveRemoveEvent(UserRemoveDto event) {
        try {
            userRemoveTemplate.send("user-remove", event);
            logger.info("[User remove successfully!]");

            UserRemoveEntity entity = userRemoveMapper.toEntity(event);
            userRemoveRepository.save(entity);

        } catch (Exception e) {
            logger.severe("[Logout failed. Error: [ " + e.getMessage() + "]");
        }
    }

    @Transactional
    public void saveLoginEvent(UserLoginDto event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(
                    event.getUsername(),
                    event.getPassword()
            );

            logger.info("[User has successfully logged in! Access token generated.]");
            Response.ok(tokenResponse);

            UserLoginEntity entity = userLoginMapper.toEntity(event);
            userLoginRepository.save(entity);

        } catch (Exception e) {
            logger.severe("[User's login failed. Error: " + e.getMessage() + "]");
            ResponseEntity.status(401).build();
        }
    }
}
