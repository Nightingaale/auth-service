package org.nightingaale.authservice.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;
import org.nightingaale.authservice.event.KafkaUserUpdateRequestEvent;
import org.nightingaale.authservice.model.entity.UserLoginEntity;
import org.nightingaale.authservice.model.entity.UserRegistrationEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserUpdateRequestMapper {
    void toEvent(KafkaUserUpdateRequestEvent dto, @MappingTarget UserRepresentation userRep);
    void updateFromKeycloak(UserRepresentation userRep, @MappingTarget UserRegistrationEntity entity);
    void updateToLoginEntity(UserRepresentation userRep, @MappingTarget UserLoginEntity loginEntity);
}
