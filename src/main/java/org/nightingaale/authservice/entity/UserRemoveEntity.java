package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "remove")
public class UserRemoveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String correlationId;
    private String userId;
    private LocalDateTime removeDate;

    @PrePersist
    public void prePersist() {
        removeDate = LocalDateTime.now();
    }
}
