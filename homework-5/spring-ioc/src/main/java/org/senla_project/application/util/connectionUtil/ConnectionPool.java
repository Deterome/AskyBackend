package org.senla_project.application.util.connectionUtil;

import lombok.NonNull;
import org.senla_project.application.util.TimedPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConnectionPool extends TimedPool<Connection> implements DisposableBean {

    @Override
    protected void preElementDelete(@NonNull Connection element) {
        try {
            if (!element.isClosed()) {
                element.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized public void closeAllConnections() {
        while (!poolMap.isEmpty()) {
            pop().ifPresent(this::preElementDelete);
        }
    }

    @Override
    public void destroy() throws Exception {
        closeAllConnections();
        stopScheduler();
    }
}
