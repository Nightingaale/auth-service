package org.nightingaale.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "removed")
public class UserRemovedEntity {
    @Id
    private String correlationId;
    private String userId;
    private boolean userExists;
}
