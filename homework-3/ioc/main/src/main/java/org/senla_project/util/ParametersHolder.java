package org.senla_project.util;

import org.senla_project.ioc.annotations.Component;
import org.senla_project.ioc.annotations.Value;

@Component
public class ParametersHolder {

    @Value("${my.param.db}")
    private String someText;

    public String getSomeText() {
        return someText;
    }
}
