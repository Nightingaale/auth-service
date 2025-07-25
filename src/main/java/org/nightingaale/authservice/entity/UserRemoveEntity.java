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
@Table(name = "remove")
public class UserRemoveEntity {
    @Id
    private String correlationId;
    private String userId;

    @CreationTimestamp
    private LocalDateTime removeDate;
}
