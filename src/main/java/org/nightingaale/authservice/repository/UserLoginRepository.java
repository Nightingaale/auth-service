package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.entity.UserLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, String> {
}
