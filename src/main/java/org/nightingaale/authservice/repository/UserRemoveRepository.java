package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.entity.UserRemoveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRemoveRepository extends JpaRepository<UserRemoveEntity, Long> {
    Optional<UserRemoveEntity> findByCorrelationId(String correlationId);
}
