package org.senla_project.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.senla_project.application.util.data.DataInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

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
