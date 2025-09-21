package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.model.entity.UserRegisteredEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegisteredRepository extends JpaRepository<UserRegisteredEntity, String> {
    void deleteByUserId(String userId);
}
