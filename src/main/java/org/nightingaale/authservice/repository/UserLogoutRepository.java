package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.entity.UserLogoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogoutRepository extends JpaRepository<UserLogoutEntity, String> {
}
