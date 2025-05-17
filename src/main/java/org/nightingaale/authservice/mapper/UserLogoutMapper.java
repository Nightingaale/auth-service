package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.nightingaale.authservice.dto.UserLogoutDto;
import org.nightingaale.authservice.entity.UserLogoutEntity;

@Mapper(componentModel = "spring")
public interface UserLogoutMapper {
    UserLogoutEntity toEntity(UserLogoutDto dto);
    UserLogoutDto toDto(UserLogoutEntity entity);
}
