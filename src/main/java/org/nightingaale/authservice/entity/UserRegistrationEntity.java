package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registration")
public class UserRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String correlationId;
    private String userId;
    private String email;
    private String username;
    private String password;
    private LocalDateTime createdAt;
}
