package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.model.dto.UserRegisteredDto;
import org.nightingaale.authservice.model.entity.UserRegisteredEntity;

@Mapper(componentModel = "spring")
public interface UserRegisteredMapper {
    @Mapping(target = "registeredDate", ignore = true)
    UserRegisteredEntity toEntity(UserRegisteredDto dto);
    UserRegisteredDto toDto(UserRegisteredEntity entity);

}
