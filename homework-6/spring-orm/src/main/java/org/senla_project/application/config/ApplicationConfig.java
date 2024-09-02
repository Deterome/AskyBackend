package org.senla_project.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.integration.spring.SpringLiquibase;
import lombok.NonNull;
import org.postgresql.ds.PGSimpleDataSource;
import org.senla_project.application.config.properties.DatabaseProperties;
import org.senla_project.application.config.properties.HibernateProperties;
import org.senla_project.application.config.properties.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan("org.senla_project.application")
public class ApplicationConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public TransactionManager transactionManager(@NonNull DatabaseProperties databaseProperties) {
        return new DataSourceTransactionManager(dataSource(databaseProperties));
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@NonNull DataSource dataSource, @NonNull HibernateProperties hibernateProperties) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("org.senla_project.application.entity");

        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(hibernateProperties.getProperties());

        return entityManagerFactory;
    }

    @Bean
    public PGSimpleDataSource dataSource(@NonNull DatabaseProperties databaseProperties) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(databaseProperties.getUrl());
        dataSource.setUser(databaseProperties.getUsername());
        dataSource.setPassword(databaseProperties.getPassword());
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(@NonNull LiquibaseProperties liquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(liquibaseProperties.getChangeLogFile());
        liquibase.setDataSource(liquibaseProperties.getDataSource());
        return liquibase;
    }

}
