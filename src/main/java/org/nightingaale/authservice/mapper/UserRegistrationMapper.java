package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.nightingaale.authservice.dto.UserRegistrationDto;
import org.nightingaale.authservice.entity.UserRegistrationEntity;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {
    UserRegistrationEntity toEntity(UserRegistrationDto dto);
    UserRegistrationDto toDto(UserRegistrationEntity entity);
}