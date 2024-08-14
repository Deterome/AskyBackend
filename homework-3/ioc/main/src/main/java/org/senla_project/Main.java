package org.senla_project;

import org.senla_project.controller.Controller;
import org.senla_project.ioc.context.IocContext;

public class Main {
    public static void main(String[] args) {
        IocContext context = new IocContext(Main.class.getPackageName());

        Controller controller = context.findComponent("controller", Controller.class);

        controller.execute();
    }
}