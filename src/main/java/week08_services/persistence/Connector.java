package week08_services.persistence;

import week08_services.exceptions.DatabaseConnectionException;

import java.sql.Connection;

public interface Connector {
    Connection getConnection() throws DatabaseConnectionException;
    void freeConnection();
    void disableConnectionAutoCommit();
    void commit();
    void rollback();
}
