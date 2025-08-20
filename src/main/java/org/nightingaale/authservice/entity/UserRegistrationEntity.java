package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @NotNull
    @Email
    @Size(max = 60)
    private String email;

    @NotNull
    @Size(min = 5, max = 20)
    private String username;

    @NotNull
    @Size(min = 8)
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime registrationAt;
}
