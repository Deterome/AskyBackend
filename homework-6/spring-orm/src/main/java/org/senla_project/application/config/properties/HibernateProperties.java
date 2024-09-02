package org.senla_project.application.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@AllArgsConstructor @Getter
public class HibernateProperties {

    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibrnate.show_sql}")
    private String showSql;
    @Value("${hibernate.format_sql}")
    private String formatSql;

    private Properties properties;

    public HibernateProperties() {
        properties.put("dialect", dialect);
        properties.put("show_sql", showSql);
        properties.put("format_sql", formatSql);
    }

}
