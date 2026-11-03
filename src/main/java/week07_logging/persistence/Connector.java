package week07_logging.persistence;

import week07_logging.exceptions.DatabaseConnectionException;

import java.sql.Connection;

public interface Connector {
    Connection getConnection() throws DatabaseConnectionException;
    void freeConnection();
    void disableConnectionAutoCommit();
}
