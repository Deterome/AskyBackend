package org.senla_project.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.senla_project.application.dto.collabRole.CollabRoleCreateDto;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.service.CollabRoleService;
import org.senla_project.application.service.RoleService;
import org.senla_project.application.service.UserService;
import org.senla_project.application.service.impl.CollabRoleServiceImpl;
import org.senla_project.application.service.impl.RoleServiceImpl;
import org.senla_project.application.service.impl.UserServiceImpl;
import org.senla_project.application.util.application.data.DataInitializer;
import org.senla_project.application.util.enums.DefaultCollabRoles;
import org.senla_project.application.util.enums.DefaultRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan("org.senla_project.application")
public class ApplicationConfig {

    @Bean
    public MethodValidationPostProcessor validationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> contextRefreshedEventApplicationListener(DataInitializer dataInitializer) {
        return event -> {
            dataInitializer.initDefaultData();
        };
    }

}
