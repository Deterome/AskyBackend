package org.senla_project.application.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class LiquibaseConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${liquibase.driver}")
    String liquibaseDriver;
    @Value("${liquibase.changeLogFile}")
    String changeLogFile;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changeLogFile);
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

}
