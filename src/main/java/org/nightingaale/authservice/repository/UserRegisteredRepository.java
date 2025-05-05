package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.entity.UserRegisteredEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRegisteredRepository extends JpaRepository<UserRegisteredEntity, Long> {
    Optional<UserRegisteredEntity> findByCorrelationId(String correlationId);
}
