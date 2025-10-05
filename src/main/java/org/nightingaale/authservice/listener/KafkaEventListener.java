package org.nightingaale.authservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nightingaale.authservice.event.KafkaUserUpdateRequestEvent;
import org.nightingaale.authservice.model.dto.UserRegisteredDto;
import org.nightingaale.authservice.model.dto.UserRemovedDto;
import org.nightingaale.authservice.model.entity.UserRegisteredEntity;
import org.nightingaale.authservice.mapper.postgres.UserRegisteredMapper;
import org.nightingaale.authservice.repository.UserRegisteredRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventListener {

    private final AuthServiceListener authServiceListener;
    private final UserRegisteredRepository userRegisteredRepository;
    private final UserRegisteredMapper userRegisteredMapper;

    @KafkaListener(topics = "user-removed", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRemoved")
    public void userRemoved(UserRemovedDto event) {
        authServiceListener.saveRemovedEvent(event);
        userRegisteredRepository.deleteByUserId(event.getUserId());
        log.info("[Received user-removed Kafka event from user-service. User has been successfully removed with ID: {}]", event.getUserId());
    }

    @KafkaListener(topics = "user-registered", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserRegistered")
    public void saveRegisteredEvent(UserRegisteredDto event) {
        if (event.isUserExists()) {
            UserRegisteredEntity entity = userRegisteredMapper.toEntity(event);
            userRegisteredRepository.save(entity);
            log.info("[Received user-registered Kafka event from user-service. User has been successfully registered with ID: {}]", event.getUserId());
        }
    }

//    @KafkaListener(topics = "user-update", groupId = "auth-service", containerFactory = "kafkaListenerContainerFactoryUserUpdate")
//    public void updateUser(KafkaUserUpdateRequestEvent event) {
//        log.info("[Received user-update Kafka event from user-service with ID: {}]", event.getUserId());
//    }
}
