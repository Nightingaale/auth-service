package org.nightingaale.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "remove")
public class UserRemoveEntity {
    @Id
    private String correlationId;
    private String userId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime removeDate;
}
