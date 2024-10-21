package org.senla_project.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.service.UserService;
import org.senla_project.application.util.enums.RolesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan("org.senla_project.application")
public class ApplicationConfig {

    @Value("${application.firstAdmin.name}")
    private String firstAdminName;
    @Value("${application.firstAdmin.password}")
    private String firstAdminPassword;
    @Autowired
    private UserService userService;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> contextRefreshedEventApplicationListener() {
        return event -> addAdminToDatabase();
    }

    private void addAdminToDatabase() {
        if (!userService.existsByUsername(firstAdminName)) {
            UserCreateDto adminCreateDto = UserCreateDto.builder()
                    .username(firstAdminName)
                    .password(firstAdminPassword)
                    .roles(new ArrayList<>(List.of(RolesEnum.USER.toString(), RolesEnum.ADMIN.toString())))
                    .build();
            userService.create(adminCreateDto);
        }
    }

}
