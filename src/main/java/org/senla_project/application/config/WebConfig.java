package org.senla_project.application.config;

import org.senla_project.application.util.convertor.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToAnswerSortTypeConvertor());
        registry.addConverter(new StringToCollabSortTypeConvertor());
        registry.addConverter(new StringToProfileSortTypeConvertor());
        registry.addConverter(new StringToQuestionSortTypeConvertor());
        registry.addConverter(new StringToRoleSortTypeConvertor());
        registry.addConverter(new StringToUserSortTypeConvertor());
        registry.addConverter(new StringToSortOrderConvertor());
    }
}
