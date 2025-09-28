package org.nightingaale.authservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaUserUpdateRequestEvent {
    private String userId;
    private String correlationId;
    private String username;
    private String email;
    private String password;
}
