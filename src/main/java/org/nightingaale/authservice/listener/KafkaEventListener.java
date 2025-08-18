package org.nightingaale.authservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nightingaale.authservice.dto.UserRegisteredDto;
import org.nightingaale.authservice.dto.UserRemovedDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventListener {

    private final AuthServiceListener authServiceListener;

    @KafkaListener(topics = "user-removed", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRemoved")
    public void userRemoved(UserRemovedDto userRemovedDto) {
        log.info("Received user-remove Kafka event: {}", userRemovedDto);
        authServiceListener.saveRemovedEvent(userRemovedDto);
    }

    @KafkaListener(topics = "user-registered", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRegistered")
    public void saveRegisteredEvent(UserRegisteredDto event) {
        if (event.isUserExists()) {
            log.info("[User has been successfully registered with ID: {}]", event.getCorrelationId());
        }
    }
}
