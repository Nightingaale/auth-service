package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.model.entity.UserRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistrationEntity, String> {
    boolean existsByUserId(String userId);
    void deleteByUserId(String userId);

    @Query("select u from UserRegistrationEntity u where u.userId = ?1")
    Optional<UserRegistrationEntity> findByUserId(String userId);
}
