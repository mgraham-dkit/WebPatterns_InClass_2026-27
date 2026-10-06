package solutions.dao_exercises.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector implements Connector {
    // Create variables to hold database details
    // This supports clearer intention in code, and avoids "magic" numbers/strings
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
    private String username = "root";
    private String password = "";

    private Connection connection;

    public Connection getConnection() {
        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try {
                connection = DriverManager.getConnection(url, username, password);
                return connection;
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot establish a connection to " + url);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tNo driver files found - please check dependencies.");
        }
        return null;
    }

    public void freeConnection(){
        if(connection != null){
            try{
                connection.close();
                connection = null;
            }catch (SQLException e){
                System.out.println("An exception occurred when attempting to close the connection to the " +
                        "database. \nException: "+ e.getMessage());
            }
        }
    }
}
