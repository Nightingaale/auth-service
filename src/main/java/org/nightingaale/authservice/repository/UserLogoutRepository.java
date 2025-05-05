package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.entity.UserLogoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLogoutRepository extends JpaRepository<UserLogoutEntity, Long> {
    Optional<UserLogoutEntity> findByCorrelationId(String correlationId);
}
