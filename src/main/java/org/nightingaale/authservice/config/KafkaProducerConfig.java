package org.nightingaale.authservice.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.nightingaale.authservice.dto.UserRegistrationDto;
import org.nightingaale.authservice.dto.UserRemoveDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return configProps;
    }

    @Bean
    public ProducerFactory<String, UserRegistrationDto> userRegistrationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseConfig());
    }

    @Bean
    public KafkaTemplate<String, UserRegistrationDto> userRegistrationKafkaTemplate() {
        return new KafkaTemplate<>(userRegistrationProducerFactory());
    }

    @Bean
    public ProducerFactory<String, UserRemoveDto> userRemoveProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseConfig());
    }

    @Bean
    public KafkaTemplate<String, UserRemoveDto> userRemoveKafkaTemplate() {
        return new KafkaTemplate<>(userRemoveProducerFactory());
    }
}