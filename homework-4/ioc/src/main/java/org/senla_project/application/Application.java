package org.senla_project.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public Application() {
        this.applicationContext = new AnnotationConfigApplicationContext(Application.class);
    }

    ApplicationContext applicationContext;
}
