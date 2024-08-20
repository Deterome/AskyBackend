package org.senla_project.application.mapper;

import lombok.NonNull;
import org.senla_project.application.dao.RoleDao;
import org.senla_project.application.dto.UserDto;
import org.senla_project.application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleDao roleDao;

    @Override
    public User toEntity(@NonNull UserDto dto) {
        return User.builder()
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .role(roleDao.findRoleByName(dto.getRoleName()))
            .build();
    }

    @Override
    public UserDto toDto(@NonNull User entity) {
        return UserDto.builder()
                .nickname(entity.getNickname())
                .password(entity.getPassword())
                .roleName(entity.getRole().getRoleName())
            .build();
    }
}
