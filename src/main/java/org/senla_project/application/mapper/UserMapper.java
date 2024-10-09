package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Named("UserMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UuidMapper.class, RoleMapper.class})
public abstract class UserMapper {

    @Named("toUserFromName")
    public abstract User toUserFromName(String userName);
    @Mapping(source = "id", target = "userId")
    public abstract User toUser(UUID id, UserCreateDto dto);
    public User toUser(UserCreateDto dto) {
        return toUser(null, dto);
    }
    @Mapping(source = "roles", target = "roles", qualifiedByName = {"RoleMapper", "toRoleSetFromStringList"})
    public abstract User toUser(UserResponseDto userResponseDto);
    public abstract UserCreateDto toUserCreateDto(User entity);
    @Mapping(source = "roles", target = "roles", qualifiedByName = {"RoleMapper", "toStringListFromRoleSet"})
    public abstract UserResponseDto toUserResponseDto(User entity);
    public abstract List<User> toUserList(List<UserResponseDto> dtoList);
    public abstract List<UserResponseDto> toUserResponseDtoList(List<User> entityList);

}