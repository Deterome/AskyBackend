package org.senla_project.application.util.connectionUtil;

import lombok.NonNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class ConnectionHolder implements DisposableBean {

    private final DataSource dataSource;
    private final Map<Thread, Connection> usedConnections;

    @Value("${connection_pool_time.seconds}")
    private int idleConnectionTimeInSeconds;
    private final ConnectionPool idleConnectionsPool;
    private volatile ThreadLocal<Boolean> transactionOpened;

    @Autowired
    public ConnectionHolder(@NonNull DataSource dataSource, @NonNull ConnectionPool idleConnectionsPool) {
        this.dataSource = dataSource;
        this.idleConnectionsPool = idleConnectionsPool;
        this.usedConnections = new ConcurrentHashMap<>();
        this.transactionOpened = new ThreadLocal<>();
    }

    public Connection getConnection() {
        Connection connection = usedConnections.get(Thread.currentThread());
        if (connection != null) {
            return connection;
        } else {
            try {
                connection = idleConnectionsPool.pop().orElse(createConnection());
                usedConnections.put(Thread.currentThread(), connection);
                transactionOpened.set(false);
                return connection;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void releaseConnectionIfTransactionClosed() {
        if (!isTransactionOpened()) {
            Connection connection = usedConnections.get(Thread.currentThread());
            usedConnections.remove(Thread.currentThread());
            idleConnectionsPool.push(connection, idleConnectionTimeInSeconds, TimeUnit.SECONDS);
        }
    }

    public void closeUsedConnections() {
        usedConnections.values().forEach(connection -> {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isTransactionOpened() {
        return transactionOpened.get();
    }

    public void setTransactionOpened(boolean isTransactionOpened) {
        transactionOpened.set(isTransactionOpened);
    }

    @Override
    public void destroy() throws Exception {
        closeUsedConnections();
    }
}
