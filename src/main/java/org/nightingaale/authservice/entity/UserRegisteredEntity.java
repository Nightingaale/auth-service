package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registered")
public class UserRegisteredEntity {
    @Id
    private String correlationId;
    private String userId;
    private boolean userExists;
    private LocalDateTime registeredAt;

    @PrePersist
    public void prePersist() {
        registeredAt = LocalDateTime.now();
    }
}
