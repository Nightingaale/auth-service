package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "removed")
public class UserRemovedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String correlationId;
    private String userId;
    private boolean userExists;
}
