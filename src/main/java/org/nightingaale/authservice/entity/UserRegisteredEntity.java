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
@Table(name = "registered")
public class UserRegisteredEntity {
    @Id
    private String correlationId;
    private String userId;
    private boolean userExists;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime registeredDate;
}
