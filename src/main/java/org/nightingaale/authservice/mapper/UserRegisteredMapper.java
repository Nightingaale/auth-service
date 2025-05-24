package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.dto.UserRegisteredDto;
import org.nightingaale.authservice.entity.UserRegisteredEntity;

@Mapper(componentModel = "spring")
public interface UserRegisteredMapper {
    @Mapping(target = "registeredAt", ignore = true)
    UserRegisteredEntity toEntity(UserRegisteredDto dto);
    UserRegisteredDto toDto(UserRegisteredEntity entity);

}
