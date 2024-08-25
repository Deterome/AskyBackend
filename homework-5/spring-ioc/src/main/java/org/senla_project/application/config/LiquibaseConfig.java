package org.senla_project.application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@NoArgsConstructor @AllArgsConstructor @Getter
public class LiquibaseConfig {

    @Value("${liquibase.driver}")
    String liquibaseDriver;
    @Value("${liquibase.changeLogFile}")
    String changeLogFile;
    @Autowired
    PGSimpleDataSource dataSource;

}
