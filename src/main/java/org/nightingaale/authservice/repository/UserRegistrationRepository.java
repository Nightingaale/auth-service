package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.model.entity.UserRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistrationEntity, String> {
    boolean existsByUserId(String userId);
    void deleteByUserId(String userId);
}
