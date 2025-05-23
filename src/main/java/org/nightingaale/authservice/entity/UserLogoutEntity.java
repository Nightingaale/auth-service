package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "logout")
public class UserLogoutEntity {
    @Id
    private String correlationId;
    private String userId;
    private LocalDateTime logoutTime;

    @PrePersist
    public void prePersist() {
        logoutTime = LocalDateTime.now();
    }
}
