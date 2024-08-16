package org.senla_project.application.db;

import org.senla_project.util.ParametersHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBManager implements DatabaseInterface {

    @Autowired
    ParametersHolder parametersHolder;
    @Override
    public void execute() {
        System.out.println(parametersHolder.getSomeText());
    }
}
