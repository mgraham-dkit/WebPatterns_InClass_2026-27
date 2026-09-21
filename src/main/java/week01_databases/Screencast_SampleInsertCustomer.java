package week01_databases;

import java.sql.*;

public class Screencast_SampleInsertCustomer {
    static void main(String[] args) {
        // Declare database constants
        String driver = "com.mysql.cj.jdbc.Driver";
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/classicmodels";
        String username = "root";
        String password = "";

        try {
            // 1) Add driver files
            Class.forName(driver);

            // 2) Create connection to database
            try(Connection conn = DriverManager.getConnection(dbUrl, username, password)){
                // 3) Write SQL to be used
                String sql = "INSERT INTO customers VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                // 4) Prepare SQL for execution - compile it into something that can be run
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    // Populate placeholders with real data
                    ps.setInt(1, 5151);
                    ps.setString(2, "Dolly Dollerson");
                    ps.setString(3, "Ellinson");
                    ps.setString(4, "Emmy");
                    ps.setString(5, "0458312448");
                    ps.setString(6, "42 Wallaby Way");
                    ps.setString(7, "Sydney");
                    ps.setString(8, "Sydney?");
                    ps.setString(9, "Unknown");
                    ps.setString(10, "N/A");
                    ps.setString(11, "Australia");
                    ps.setInt(12, 1102);
                    ps.setDouble(13, 1000000.50);

                    // 5) Execute insert (a form of update) and see how many rows are impacted
                    int rowsAffected = ps.executeUpdate();
                    System.out.println("Number of rows added = " + rowsAffected);
                }catch(SQLException e){
                    System.out.println("Could not prepare SQL: \"" + sql + "\"");
                    System.out.println("Exception reads: " + e.getMessage());
                }
            }catch(SQLException e){
                System.out.println("Could not establish a connection to " + dbUrl +
                        " using " +  username + "as username");
                System.out.println("Exception reads: " + e.getMessage());
            }

        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: Driver cannot be found");
            System.out.println("Exception reads: " + e.getMessage());
            System.out.println("System terminating...");
        }
    }
}
