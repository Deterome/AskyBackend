package org.senla_project.application;

import org.senla_project.application.config.ApplicationConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {

        ApplicationContext application = new AnnotationConfigApplicationContext(ApplicationConfig.class);

    }
}