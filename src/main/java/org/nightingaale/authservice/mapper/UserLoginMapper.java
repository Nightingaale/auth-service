package org.nightingaale.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nightingaale.authservice.dto.UserLoginDto;
import org.nightingaale.authservice.entity.UserLoginEntity;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    @Mapping(target = "loginTime", ignore = true)
    UserLoginEntity toEntity(UserLoginDto dto);
    UserLoginDto toDto(UserLoginEntity entity);
}
