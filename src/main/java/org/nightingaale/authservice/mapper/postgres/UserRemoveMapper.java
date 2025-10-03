package org.nightingaale.authservice.mapper.postgres;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.model.dto.UserRemoveDto;
import org.nightingaale.authservice.model.entity.UserRemoveEntity;

@Mapper(componentModel = "spring")
public interface UserRemoveMapper {
    @Mapping(target = "removeDate", ignore = true)
    UserRemoveEntity toEntity(UserRemoveDto dto);
    UserRemoveDto toDto(UserRemoveEntity entity);
}
