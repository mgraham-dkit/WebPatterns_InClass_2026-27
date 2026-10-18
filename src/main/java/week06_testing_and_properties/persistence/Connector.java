package week06_testing_and_properties.persistence;

import java.sql.Connection;

public interface Connector {
    Connection getConnection();
    void freeConnection();
    void disableConnectionAutoCommit();
}
