package week06_hashing.persistence;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    boolean register(String username, String password) throws SQLException;
    boolean login(String username, String password) throws SQLException;
    List<String> getUsernames() throws SQLException;
    void closeConnection();
}
