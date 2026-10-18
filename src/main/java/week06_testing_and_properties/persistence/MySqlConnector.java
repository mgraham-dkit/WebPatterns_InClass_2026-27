package week06_testing_and_properties.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlConnector implements Connector {
    private Properties properties = new Properties();
    // Create variables to hold database details
    // This supports clearer intention in code, and avoids "magic" numbers/strings
    private static String defaultDriver = "com.mysql.cj.jdbc.Driver";
    private static String defaultUrl = "jdbc:mysql://127.0.0.1:3306/";
    private static String defaultDatabase = "classicmodels";
    private static String defaultUsername = "root";
    private static String defaultPassword = "";

    private Connection connection;

    public MySqlConnector(String propertiesFilename){
        try {
            // Open stream to the specified properties file
            InputStream rootPath =
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFilename);
            if(rootPath == null){
                throw new IOException("Could not open stream to properties file \"" + propertiesFilename + "\"");
            }
            // Load in all key-value pairs from properties file
            properties.load(rootPath);
        }catch(IOException e){
            System.out.println("An exception occurred when attempting to load properties from \"" + propertiesFilename + "\": " + e.getMessage());
        }
    }

    public MySqlConnector(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        // If we already have a connection, use that one (this ensures it's shared)
        if(connection != null){
            return connection;
        }

        //String driver = "com.mysql.cj.jdbc.Driver";
        String driver = properties.getProperty("driver", defaultDriver);
        //String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
        String url = properties.getProperty("url", defaultUrl);
        String database = properties.getProperty("database", defaultDatabase);
        //String username = "root";
        String username = properties.getProperty("username", defaultUsername);
        //String password = "";
        String password = properties.getProperty("password", defaultPassword);


        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try {
                connection = DriverManager.getConnection(url+database, username, password);
                return connection;
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tConnection could not be established - incorrect URL or database not switched on" +
                        ". URL attempted: " + url);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tDriver files have not been loaded. Please check pom driver dependencies details");
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

    public void disableConnectionAutoCommit(){
        if(connection != null){
            try{
                connection.setAutoCommit(false);
            }catch (SQLException e){
                System.out.println("An exception occurred when attempting to set autocommit to false. \nException: "+ e.getMessage());
            }
        }
    }

    public void commit(){
        if(connection != null){
            try{
                connection.commit();
            }catch (SQLException e){
                System.out.println("An exception occurred when attempting to commit changes. \nException: "+ e.getMessage());
            }
        }
    }
}
