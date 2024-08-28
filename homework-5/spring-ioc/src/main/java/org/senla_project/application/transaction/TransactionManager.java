package org.senla_project.application.transaction;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.senla_project.application.util.connectionUtil.ConnectionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Aspect
@Component
public class TransactionManager {

    private final ConnectionHolder connectionHolder;

    @Autowired
    public TransactionManager(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Pointcut("@annotation(org.senla_project.application.transaction.Transaction)")
    public void transaction() {}

    @Around("transaction()")
    public Object transactionAdvice(ProceedingJoinPoint joinPoint) {
        Object returnValue = null;
        Connection connection = connectionHolder.getConnection();
        connectionHolder.setTransactionOpened(true);
        try {
            connection.setAutoCommit(false);
            returnValue = joinPoint.proceed();
            connection.commit();
            connectionHolder.setTransactionOpened(false);
        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            connectionHolder.releaseConnectionIfTransactionClosed();
        }
        return returnValue;
    }

}
