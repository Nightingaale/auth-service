package org.nightingaale.authservice.repository;

import org.nightingaale.authservice.model.entity.UserRemoveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRemoveRepository extends JpaRepository<UserRemoveEntity, String> {
}
