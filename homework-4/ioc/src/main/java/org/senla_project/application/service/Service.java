package org.senla_project.application.service;

import org.senla_project.application.db.DatabaseInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service implements ServiceInterface {

    public DatabaseInterface getDatabase() {
        return database;
    }

    @Autowired
    public void setDatabase(DatabaseInterface database) {
        this.database = database;
    }

    DatabaseInterface database;

    @Override
    public void execute() {
        database.execute();
    }
}
