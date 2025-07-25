package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.dto.UserLogoutDto;
import org.nightingaale.authservice.entity.UserLogoutEntity;

@Mapper(componentModel = "spring")
public interface UserLogoutMapper {
    @Mapping(target = "logoutDate", ignore = true)
    UserLogoutEntity toEntity(UserLogoutDto dto);
    UserLogoutDto toDto(UserLogoutEntity entity);
}
