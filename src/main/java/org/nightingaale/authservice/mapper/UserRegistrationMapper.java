package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.dto.UserRegistrationDto;
import org.nightingaale.authservice.entity.UserRegistrationEntity;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {
    @Mapping(target = "registrationAt", ignore = true)
    UserRegistrationEntity toEntity(UserRegistrationDto dto);
    UserRegistrationDto toDto(UserRegistrationEntity entity);
}