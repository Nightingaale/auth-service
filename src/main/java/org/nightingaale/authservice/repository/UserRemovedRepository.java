package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.entity.UserRemovedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRemovedRepository extends JpaRepository<UserRemovedEntity, String> {
}
