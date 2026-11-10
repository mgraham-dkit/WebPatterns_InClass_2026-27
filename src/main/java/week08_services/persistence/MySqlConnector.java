package week08_services.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week08_services.exceptions.DatabaseConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlConnector implements Connector {
    // Create logger for use throughout class
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlConnector.class);
    // Create variables to hold database details
    // This supports clearer intention in code, and avoids "magic" numbers/strings
    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_URL = "jdbc:mysql://127.0.0.1:3306/";
    private static final String DEFAULT_DATABASE = "classicmodels";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";

    private Properties properties = new Properties();
    private Connection connection;

    public MySqlConnector(String propertiesFilename) {
        try {
            // Open stream to the specified properties file
            InputStream rootPath =
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFilename);
            if (rootPath == null) {
                throw new IOException("Could not open stream to properties file \"" + propertiesFilename + "\"");
            }
            // Load in all key-value pairs from properties file
            properties.load(rootPath);
        } catch (IOException e) {
            System.out.println("An exception occurred when attempting to load properties from \"" + propertiesFilename + "\": " + e.getMessage());

            LOGGER.error("Error: " + e.getMessage());
        }
    }

    public MySqlConnector(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() throws DatabaseConnectionException {
        // If we already have a connection, use that one (this ensures it's shared)
        if (connection != null) {
            return connection;
        }

        //String driver = "com.mysql.cj.jdbc.Driver";
        String driver = properties.getProperty("driver", DEFAULT_DRIVER);
        //String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
        String url = properties.getProperty("url", DEFAULT_URL);
        String database = properties.getProperty("database", DEFAULT_DATABASE);
        //String username = "root";
        String username = properties.getProperty("username", DEFAULT_USERNAME);
        //String password = "";
        String password = properties.getProperty("password", DEFAULT_PASSWORD);


        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try {
                connection = DriverManager.getConnection(url + database, username, password);
                return connection;
            } catch (SQLException e) {
                throw new DatabaseConnectionException("Connection could not be established - incorrect URL or " +
                        "database not switched on", e);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("Driver files have not been loaded. Please check pom driver dependencies details. \n " +
                    "Exception:" +
                    " {}", e.getMessage());
        }
        return null;
    }

    public void freeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                LOGGER.error("An exception occurred when attempting to close the connection to the database \n " +
                        "Exception:" +
                        " {}", e.getMessage());
            }
        }
    }

    public void disableConnectionAutoCommit() {
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("An exception occurred when attempting to set autocommit to false. \nException: {}",
                        e.getMessage());
            }
        }
    }

    public void commit() {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                LOGGER.error("An exception occurred when attempting to commit changes. \nException: {}",
                        e.getMessage());
            }
        }
    }

    @Override
    public void rollback() {
        if (connection != null) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new DatabaseConnectionException("An exception occurred when attempting to roll back changes", e);
            }
        }
    }
}
