package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    private String password;

    @CreationTimestamp
    private LocalDateTime loginTime;
}
