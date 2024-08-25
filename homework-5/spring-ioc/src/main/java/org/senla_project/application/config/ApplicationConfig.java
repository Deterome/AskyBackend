package org.senla_project.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.integration.spring.SpringLiquibase;
import lombok.NonNull;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ComponentScan("org.senla_project.application")
public class ApplicationConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Connection connection(@NonNull DatabaseConfig databaseConfig) {
        try {
            return DriverManager.getConnection(databaseConfig.getUrl(),
                    databaseConfig.getUsername(),
                    databaseConfig.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PGSimpleDataSource dataSource(@NonNull DatabaseConfig databaseConfig) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(databaseConfig.getUrl());
        dataSource.setUser(databaseConfig.getUsername());
        dataSource.setPassword(databaseConfig.getPassword());
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(@NonNull LiquibaseConfig liquibaseConfig) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(liquibaseConfig.changeLogFile);
        liquibase.setDataSource(liquibaseConfig.getDataSource());
        return liquibase;
    }

}
