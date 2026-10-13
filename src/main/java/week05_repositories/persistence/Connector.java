package week05_repositories.persistence;

import java.sql.Connection;

public interface Connector {
    Connection getConnection();
    void freeConnection();
}
