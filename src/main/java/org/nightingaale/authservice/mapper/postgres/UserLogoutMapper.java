package org.nightingaale.authservice.mapper.postgres;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.model.dto.UserLogoutDto;
import org.nightingaale.authservice.model.entity.UserLogoutEntity;

@Mapper(componentModel = "spring")
public interface UserLogoutMapper {
    @Mapping(target = "logoutDate", ignore = true)
    UserLogoutEntity toEntity(UserLogoutDto dto);
    UserLogoutDto toDto(UserLogoutEntity entity);
}
