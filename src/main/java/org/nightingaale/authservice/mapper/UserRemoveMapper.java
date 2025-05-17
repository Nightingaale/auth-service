package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.nightingaale.authservice.dto.UserRemoveDto;
import org.nightingaale.authservice.entity.UserRemoveEntity;

@Mapper(componentModel = "spring")
public interface UserRemoveMapper {
    UserRemoveEntity toEntity(UserRemoveDto dto);
    UserRemoveDto toDto(UserRemoveEntity entity);
}
