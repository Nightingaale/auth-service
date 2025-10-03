package org.nightingaale.authservice.mapper.postgres;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.model.dto.UserLoginDto;
import org.nightingaale.authservice.model.entity.UserLoginEntity;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    @Mapping(target = "loginTime", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserLoginEntity toEntity(UserLoginDto dto);
    UserLoginDto toDto(UserLoginEntity entity);
}
