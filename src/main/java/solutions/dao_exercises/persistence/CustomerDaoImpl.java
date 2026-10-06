package solutions.dao_exercises.persistence;

import solutions.dao_exercises.entities.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao{
    @Override
    public List<Customer> selectCustomersByName(String name) {
        List<Customer> customers = new ArrayList<>();

        // Create variables to hold database details
        // This supports clearer intention in code, and avoids "magic" numbers/strings
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
        String username = "root";
        String password = "";

        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try(Connection conn = DriverManager.getConnection(url, username, password)){
                // Prepare statement - Write an SQL statement and compile it into something
                // the database can actually run
                String sql = "SELECT * FROM customers WHERE ?";
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    ps.setString(1, name);
                    // Run query - Execute the SQL that has been compiled and get the results
                    try(ResultSet rs = ps.executeQuery()) {
                        // Process results - loop through each row in resultset until it's empty
                        while (rs.next()) {
                            // Extract pieces from the result row - we can explicitly ask for each piece by column name
                            Customer customer = new Customer(
                                    rs.getInt("customerNumber"),
                                    rs.getString("customerName"),
                                    rs.getString("contactLastName"),
                                    rs.getString("contactFirstName"),
                                    rs.getString("phone"),
                                    rs.getString("addressLine1"),
                                    rs.getString("addressLine2"),
                                    rs.getString("city"),
                                    rs.getString("state"),
                                    rs.getString("postalCode"),
                                    rs.getString("country"),
                                    rs.getInt("salesRepEmployeeNumber"),
                                    rs.getDouble("creditLimit")
                            );
                            customers.add(customer);
                        }
                    }catch(SQLException e){
                        System.out.println("Exception: \"" + e.getMessage() + "\"");
                        System.out.println("\tIssue occurred when processing query or results");
                    }
                }catch(SQLException e){
                    System.out.println("Exception: \"" + e.getMessage() + "\"");
                    System.out.println("\tCannot prepare statement: " + sql);
                }
            }catch(SQLException e){
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot establish a connection to " + url);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tNo driver files found - please check dependencies.");
        }

        return customers;
    }
}
