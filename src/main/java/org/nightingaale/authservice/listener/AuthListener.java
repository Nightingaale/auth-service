package org.nightingaale.authservice.listener;

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
@Transactional
public class AuthListener {

    private final KafkaTemplate<String, UserRegistrationDto> userRegistrationTemplate;
    private final KafkaTemplate<String, UserRemoveDto> userRemoveTemplate;
    private final AuthService authService;
    private static final Logger logger = Logger.getLogger(AuthListener.class.getName());
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserRegistrationRepository userRegistrationRepository;
    private final UserRegisteredMapper userRegisteredMapper;
    private final UserRegisteredRepository userRegisteredRepository;
    private final UserRemovedMapper userRemovedMapper;
    private final UserRemovedRepository userRemovedRepository;
    private final UserLoginMapper userLoginMapper;
    private final UserLoginRepository userLoginRepository;
    private final UserRemoveMapper userRemoveMapper;
    private final UserRemoveRepository userRemoveRepository;

    public void saveRegistrationEvent(UserRegistrationDto event) {
        try {
            String keycloakUserId = authService.registerUser(event.getUsername(), event.getEmail(), event.getPassword());

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
    public void saveRegisteredEvent(UserRegisteredDto event) {
        if (event.isUserExists()) {
            logger.info("[User has been successfully registered with ID: [" + event.getUserId() + "]");

            UserRegisteredEntity entity = userRegisteredMapper.toEntity(event);
            userRegisteredRepository.save(entity);
        }
    }

    @KafkaListener(topics = "user-removed", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRemoved")
    public ResponseEntity<?> saveRemovedEvent(UserRemovedDto event) {
        try {
            if (!event.isUserExists()) {
                authService.removeUser(event.getUserId());
                logger.info("[User with ID: " + event.getUserId() + " successfully removed.]");

                UserRemovedEntity entity = userRemovedMapper.toEntity(event);
                userRemovedRepository.save(entity);
            }
            return ResponseEntity.ok("[User logged out successfully!]");
        } catch (Exception e) {
            logger.info("[Logout failed. Error: " + e.getMessage() + "]");
            return ResponseEntity.status(500).body("[Logout failed: " + e.getMessage() + "]");
        }
    }

    public void saveRemoveEvent(UserRemoveDto event) {
        try {
            userRemoveTemplate.send("user-remove", event);
            logger.info("[User logged out successfully!]");

            UserRemoveEntity entity = userRemoveMapper.toEntity(event);
            userRemoveRepository.save(entity);

        } catch (Exception e) {
            logger.severe("[Logout failed. Error: [ " + e.getMessage() + "]");
        }
    }

    public void saveLoginEvent(UserLoginDto event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(event.getUsername(), event.getCorrelationId());
            logger.info("[User's has been successfully login! Generation access token.]");
            ResponseEntity.ok(tokenResponse);

            UserLoginEntity entity = userLoginMapper.toEntity(event);
            userLoginRepository.save(entity);

        } catch (Exception e) {
            logger.severe("[User's login failed. Error: " + e.getMessage() + "]");
            ResponseEntity.status(401).build();
        }
    }
}
