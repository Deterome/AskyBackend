package org.senla_project.application.controller;

import org.senla_project.application.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Controller implements ControllerInterface{

    private final ServiceInterface service;

    @Autowired
    Controller(ServiceInterface service) {
        this.service = service;
    }

    @Override
    public void execute() {
        service.execute();
    }
}
