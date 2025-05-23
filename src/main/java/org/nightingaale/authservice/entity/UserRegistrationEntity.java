package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registration")
public class UserRegistrationEntity {
    @Id
    private String correlationId;
    private String userId;
    private String email;
    private String username;
    private String password;
    private LocalDateTime registrationAt;

    @PrePersist
    public void prePersist() {
        registrationAt = LocalDateTime.now();
    }
}
