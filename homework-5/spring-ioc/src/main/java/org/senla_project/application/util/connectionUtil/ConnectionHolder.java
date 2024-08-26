package org.senla_project.application.util.connectionUtil;

import lombok.NonNull;
import org.senla_project.application.util.TimedPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConnectionHolder {

    private final DataSource dataSource;
    private final TimedPool<Connection> idleConnectionsPool;
    private final Map<Thread, Connection> usedConnections;

    @Value("60000")
    static private int idleConnectionTimeInMillis;
    private ThreadLocal<Boolean> transactionOpened;

    @Autowired
    public ConnectionHolder(@NonNull DataSource dataSource) {
        this.dataSource = dataSource;
        this.idleConnectionsPool = new TimedPool<>();
        this.usedConnections = new HashMap<>();
    }

    public Connection getConnection() {
        Connection connection = usedConnections.get(Thread.currentThread());
        if (connection != null) {
            return connection;
        } else {
            try {
                connection = idleConnectionsPool.pop().orElse(createConnection());
                usedConnections.put(Thread.currentThread(), connection);
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
        if (!transactionOpened.get()) {
            Connection connection = usedConnections.get(Thread.currentThread());
            usedConnections.remove(Thread.currentThread());
            idleConnectionsPool.push(connection, idleConnectionTimeInMillis);
        }
    }

}
