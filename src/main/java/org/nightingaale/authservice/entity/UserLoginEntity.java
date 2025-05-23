package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "login")
public class UserLoginEntity {
    @Id
    private String correlationId;
    private String username;
    private LocalDateTime loginTime;

    @PrePersist
    public void prePersist() {
        loginTime = LocalDateTime.now();
    }
}
