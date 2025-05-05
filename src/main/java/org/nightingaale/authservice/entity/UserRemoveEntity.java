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
@Table(name = "remove")
public class UserRemoveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String correlationId;
    private String userId;
    private LocalDateTime removedDate;
}
