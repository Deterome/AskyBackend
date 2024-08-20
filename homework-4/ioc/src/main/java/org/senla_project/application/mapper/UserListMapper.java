package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.senla_project.application.dto.UserDto;
import org.senla_project.application.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {

    List<User> toEntityList(List<UserDto> dtoList);
    List<UserDto> toDtoList(List<User> entityList);

}


