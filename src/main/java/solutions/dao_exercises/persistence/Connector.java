package solutions.dao_exercises.persistence;

import java.sql.Connection;

public interface Connector {
    Connection getConnection();
    void freeConnection();
}
