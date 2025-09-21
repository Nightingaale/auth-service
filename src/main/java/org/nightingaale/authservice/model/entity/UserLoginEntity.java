package org.nightingaale.authservice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Size(min = 5, max = 20)
    private String username;

    @Size(min = 8)
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime loginTime;
}
