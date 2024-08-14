package org.senla_project.db;

import org.senla_project.ioc.annotations.Autowire;
import org.senla_project.ioc.annotations.Component;
import org.senla_project.util.ParametersHolder;

@Component
public class DBManager implements DatabaseInterface {

    @Autowire
    ParametersHolder parametersHolder;
    @Override
    public void execute() {
        System.out.println(parametersHolder.getSomeText());
    }
}
