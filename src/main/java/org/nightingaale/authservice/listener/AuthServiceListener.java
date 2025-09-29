package org.nightingaale.authservice.listener;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.nightingaale.authservice.event.KafkaUserUpdateRequestEvent;
import org.nightingaale.authservice.model.dto.*;
import org.nightingaale.authservice.model.dto.UserRegistrationDto;
import org.nightingaale.authservice.model.entity.*;
import org.nightingaale.authservice.mapper.*;
import org.nightingaale.authservice.model.entity.UserRemoveEntity;
import org.nightingaale.authservice.repository.*;
import org.nightingaale.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceListener {

    private final KafkaTemplate<String, UserRegistrationDto> userRegistrationTemplate;
    private final KafkaTemplate<String, UserRemoveDto> userRemoveTemplate;
    private final KafkaTemplate<String, KafkaUserUpdateRequestEvent> userUpdatedEvent;
    private final AuthService authService;
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserRegistrationRepository userRegistrationRepository;
    private final UserLoginMapper userLoginMapper;
    private final UserLoginRepository userLoginRepository;
    private final UserRemoveMapper userRemoveMapper;
    private final UserRemoveRepository userRemoveRepository;
    private final UserRemovedMapper userRemovedMapper;
    private final UserRemovedRepository userRemovedRepository;

    public void saveRegistrationEvent(UserRegistrationDto event) {
        try {
            if (userRegistrationRepository.existsById(event.getCorrelationId())) {
                log.warn("[User with correlationID: {} already exists]", event.getCorrelationId());
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

    @Transactional
    public void saveRemovedEvent(UserRemovedDto event) {
        try {
            if (event.isUserExists()) {
                log.warn("[Waiting for user to be removed...]");
                return;
            }

            authService.removeUser(event.getUserId());
            userRegistrationRepository.deleteByUserId(event.getUserId());

            UserRemovedEntity entity = userRemovedMapper.toEntity(event);
            userRemovedRepository.save(entity);

            log.info("[User with ID: {} successfully removed]", event.getUserId());
        } catch (Exception e) {
            log.error("[Removed failed for user {}. Error: {}]", event.getUserId(), e.getMessage());
        }
    }

    public void saveRemoveEvent(UserRemoveDto event) {
        try {
            if (!userRegistrationRepository.existsByUserId(event.getUserId())) {
                log.warn("[User with ID: {}", event.getUserId() + "does not exist]");
                return;
            }

            UserRemoveEntity entity = userRemoveMapper.toEntity(event);
            userRemoveRepository.save(entity);

            userRemoveTemplate.send("user-remove", event);
            log.info("[Send Kafka user-remove event to user-service: {}", event.getUserId());
        } catch (Exception e) {
            log.error("[Removal failed. Error: {}]", e.getMessage());
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

    @Transactional
    public void updatedUserEvent(KafkaUserUpdateRequestEvent userUpdateRequestEvent) {
        try {
            authService.updateUser(userUpdateRequestEvent);

            userRegistrationRepository.findById(userUpdateRequestEvent.getUserId()).ifPresent(user -> {
                user.setUsername(userUpdateRequestEvent.getUsername());
                userRegistrationRepository.save(user);
            });

            userLoginRepository.findById(userUpdateRequestEvent.getUserId()).ifPresent(user -> {
                user.setUsername(userUpdateRequestEvent.getUsername());
                userLoginRepository.save(user);
            });

            userUpdatedEvent.send("user-updated", userUpdateRequestEvent);
            log.info("[Send Kafka user-updated event to user-service: {}]", userUpdateRequestEvent.getUserId());
        }
        catch (RuntimeException e) {
            log.error("[User update with ID {} has been failed. Error: {}]", userUpdateRequestEvent.getUserId(), e.getMessage());
            throw e;
        }
    }
}
