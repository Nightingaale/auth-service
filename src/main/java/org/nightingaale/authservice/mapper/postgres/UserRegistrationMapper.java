package org.nightingaale.authservice.mapper.postgres;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.model.dto.UserRegistrationDto;
import org.nightingaale.authservice.model.entity.UserRegistrationEntity;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {
    @Mapping(target = "registrationAt", ignore = true)
    UserRegistrationEntity toEntity(UserRegistrationDto dto);
    UserRegistrationDto toDto(UserRegistrationEntity entity);
}