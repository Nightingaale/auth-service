package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.nightingaale.authservice.dto.UserRemovedDto;
import org.nightingaale.authservice.entity.UserRemovedEntity;

@Mapper(componentModel = "spring")
public interface UserRemovedMapper {
    UserRemovedEntity toEntity(UserRemovedDto dto);
    UserRemovedDto toDto(UserRemovedEntity entity);
}
