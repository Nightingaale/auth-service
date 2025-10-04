package org.nightingaale.authservice.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;
import org.nightingaale.authservice.event.KafkaUserUpdateRequestEvent;
import org.nightingaale.authservice.model.entity.UserLoginEntity;
import org.nightingaale.authservice.model.entity.UserRegistrationEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserUpdateRequestMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    void toEvent(KafkaUserUpdateRequestEvent event, @MappingTarget UserRepresentation userRep);

    void updateFromKeycloak(UserRepresentation userRep, @MappingTarget UserRegistrationEntity entity);
    void updateToLoginEntity(UserRepresentation userRep, @MappingTarget UserLoginEntity loginEntity);
}
