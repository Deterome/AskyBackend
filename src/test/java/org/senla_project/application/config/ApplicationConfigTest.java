package org.senla_project.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.senla_project.application.util.data.DataInitializer;
import org.springframework.context.annotation.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = "org.senla_project.application", excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class), @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {DataInitializer.class})})
public class ApplicationConfigTest {

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor validationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}