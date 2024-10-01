package org.senla_project.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = "org.senla_project.application", excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)})
public class ApplicationConfigTest {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}