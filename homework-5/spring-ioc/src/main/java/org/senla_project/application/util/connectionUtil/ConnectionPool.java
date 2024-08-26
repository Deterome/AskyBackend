package org.senla_project.application.util.connectionUtil;

import org.senla_project.application.util.TimedPool;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool extends TimedPool<Connection> {

    @Override
    protected void preElementDelete(Connection element) {
        try {
            element.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
