package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.User;
import org.senla_project.application.util.Exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Named("UserMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleMapper.class})
public abstract class UserMapper {

    @Autowired
    private UserDao userDao;

    @Named("toUserFromName")
    public User toUserFromName(String userName) {
        return userDao.findUserByNickname(userName).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Mapping(target = "role", source = "roleName", qualifiedByName = {"RoleMapper", "toRoleEntityFromName"})
    public abstract User toEntity(UserCreateDto dto);
    @Mapping(target = "roleName", expression = "java(entity.getRole().getRoleName())")
    public abstract UserCreateDto toUserCreateDto(User entity);
    @Mapping(target = "roleName", expression = "java(entity.getRole().getRoleName())")
    public abstract UserResponseDto toUserResponseDto(User entity);
    public abstract List<User> toEntityList(List<UserResponseDto> dtoList);
    public abstract List<UserResponseDto> toUserResponseDtoList(List<User> entityList);

}