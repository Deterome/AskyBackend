package org.senla_project.service;

import org.senla_project.db.DatabaseInterface;
import org.senla_project.ioc.annotations.Autowire;
import org.senla_project.ioc.annotations.Component;

@Component
public class Service implements ServiceInterface {

    public DatabaseInterface getDatabase() {
        return database;
    }

    @Autowire
    public void setDatabase(DatabaseInterface database) {
        this.database = database;
    }

    DatabaseInterface database;

    @Override
    public void execute() {
        database.execute();
    }
}
