package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.model.entity.UserLoginEntity;
import org.nightingaale.authservice.model.entity.UserRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, String> {

    @Query("select u from UserLoginEntity u where u.correlationId = ?1")
    Optional<UserLoginEntity> findByCorrelationId(String correlationId);
}
