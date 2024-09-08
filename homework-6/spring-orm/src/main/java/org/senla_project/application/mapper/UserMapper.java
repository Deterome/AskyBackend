package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.User;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Named("UserMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UuidMapper.class})
public abstract class UserMapper {

    @Autowired
    private UserRepository userRepository;

    @Named("toUserFromName")
    public User toUserFromName(String userName) {
        return userRepository.findUserByNickname(userName).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public abstract User toEntity(UUID id, UserCreateDto dto);
    public User toEntity(UserCreateDto dto) {
        return toEntity(UUID.randomUUID(), dto);
    }
    public abstract UserCreateDto toCreateDto(User entity);
    public abstract UserResponseDto toResponseDto(User entity);
    public abstract List<User> toEntityList(List<UserResponseDto> dtoList);
    public abstract List<UserResponseDto> toUserResponseDtoList(List<User> entityList);

}