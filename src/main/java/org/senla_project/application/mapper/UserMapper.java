package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.dto.user.UserUpdateDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.entity.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Named("UserMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UuidMapper.class, RoleMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserMapper {

    public abstract User partialUserToUser(@MappingTarget User targetUser, User user);

    @Named("toUserFromName")
    public User toUserFromName(String username) {
        return User.builder()
                .username(username)
                .build();
    }

    @Mapping(source = "id", target = "userId")
    public abstract User toUser(UUID id, UserCreateDto dto);

    @Mapping(target = "userId", ignore = true)
    public abstract User toUser(UserCreateDto dto);

    @Mapping(source = "roles", target = "roles", qualifiedByName = {"RoleMapper", "toRoleSetFromStringList"})
    public abstract User toUser(UserUpdateDto updateDto);

    @Mapping(source = "roles", target = "roles", qualifiedByName = {"RoleMapper", "toRoleSetFromStringList"})
    public abstract User toUser(UserResponseDto userResponseDto);

    public abstract UserCreateDto toUserCreateDto(User entity);

    @Mapping(source = "roles", target = "roles", qualifiedByName = {"RoleMapper", "toStringListFromRoleSet"})
    public abstract UserResponseDto toUserResponseDto(User entity);

    public abstract List<User> toUserList(List<UserResponseDto> dtoList);

    public abstract List<UserResponseDto> toUserResponseDtoList(List<User> entityList);

    public String toStringFromUser(User user) {
        return user.getUsername();
    }

    @Named("toStringListFromUserSet")
    public abstract List<String> toStringListFromUserSet(Set<User> usersSet);

    @Named("toUserSetFromStringList")
    public Set<User> toUserSetFromStringList(List<String> usersList) {
        return usersList.stream().map(this::toUserFromName).collect(Collectors.toSet());
    }

}