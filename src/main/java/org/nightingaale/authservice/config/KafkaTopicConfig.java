package org.nightingaale.authservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userRegistrationTopic() {
        return TopicBuilder.name("user-registration")
                .build();
    }

    @Bean
    public NewTopic userRemoveTopic() {
        return TopicBuilder.name("user-remove")
                .build();
    }
}