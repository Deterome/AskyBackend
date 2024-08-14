package org.senla_project.controller;

import org.senla_project.ioc.annotations.Autowire;
import org.senla_project.ioc.annotations.Component;
import org.senla_project.service.ServiceInterface;

@Component
public class Controller implements ControllerInterface{

    private final ServiceInterface service;

    @Autowire
    Controller(ServiceInterface service) {
        this.service = service;
    }

    @Override
    public void execute() {
        service.execute();
    }
}
