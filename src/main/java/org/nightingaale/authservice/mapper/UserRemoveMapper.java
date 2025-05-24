package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.dto.UserRemoveDto;
import org.nightingaale.authservice.entity.UserRemoveEntity;

@Mapper(componentModel = "spring")
public interface UserRemoveMapper {
    @Mapping(target = "removeDate", ignore = true)
    UserRemoveEntity toEntity(UserRemoveDto dto);
    UserRemoveDto toDto(UserRemoveEntity entity);
}
