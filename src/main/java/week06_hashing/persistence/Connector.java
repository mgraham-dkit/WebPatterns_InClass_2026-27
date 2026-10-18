package week06_hashing.persistence;

import java.sql.Connection;

public interface Connector {
    Connection getConnection();
    void freeConnection();
    void disableConnectionAutoCommit();
}
