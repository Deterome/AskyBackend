package org.senla_project.application.mapper;

import org.senla_project.application.dto.UserDto;
import org.senla_project.application.entity.User;

public interface UserMapper {

    User toEntity(UserDto dto);
    UserDto toDto(User entity);

}