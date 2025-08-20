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
@Table(name = "logout")
public class UserLogoutEntity {
    @Id
    private String correlationId;
    private String userId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime logoutDate;

}
