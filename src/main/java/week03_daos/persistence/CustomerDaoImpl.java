package week03_daos.persistence;

import week03_daos.entities.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
    private String username = "root";
    private String password = "";

    public List<Customer> selectCustomersByName(String name){
        List<Customer> CustomerList = new ArrayList<>();
        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try(Connection conn = DriverManager.getConnection(url, username, password)){
                // Prepare statement - Write an SQL statement and compile it into something
                // the database can actually run
                String sql = "SELECT * FROM customers WHERE customerName = ?";
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    // Populate placeholder
                    ps.setString(1,name);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int customerNumber = rs.getInt("customerNumber");
                        String customerName = rs.getString("customerName");
                        String contactLastName = rs.getString("contactLastName");
                        String contactFirstName = rs.getString("contactFirstName");
                        String phone = rs.getString("phone");
                        String addressLine1 = rs.getString("addressLine1");
                        String addressLine2 = rs.getString("addressLine2");
                        String city = rs.getString("city");
                        String state = rs.getString("state");
                        String postalCode = rs.getString("postalCode");
                        String country = rs.getString("country");
                        Integer salesRepEmployeeNumber = rs.getInt("salesRepEmployeeNumber");
                        Double creditLimit = rs.getDouble("creditLimit");

                        Customer c = new Customer(customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit);

                        CustomerList.add(c);
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
        return CustomerList;
    }
    public List<Customer> selectCustomersContainingName(String name){
        List<Customer> CustomerList = new ArrayList<>();
        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try(Connection conn = DriverManager.getConnection(url, username, password)){
                // Prepare statement - Write an SQL statement and compile it into something
                // the database can actually run
                String sql = "SELECT * FROM customers WHERE customerName LIKE ?";
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    // Populate placeholder
                    ps.setString(1,"%" + name + "%");

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int customerNumber = rs.getInt("customerNumber");
                        String customerName = rs.getString("customerName");
                        String contactLastName = rs.getString("contactLastName");
                        String contactFirstName = rs.getString("contactFirstName");
                        String phone = rs.getString("phone");
                        String addressLine1 = rs.getString("addressLine1");
                        String addressLine2 = rs.getString("addressLine2");
                        String city = rs.getString("city");
                        String state = rs.getString("state");
                        String postalCode = rs.getString("postalCode");
                        String country = rs.getString("country");
                        Integer salesRepEmployeeNumber = rs.getInt("salesRepEmployeeNumber");
                        Double creditLimit = rs.getDouble("creditLimit");

                        Customer c = new Customer(customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit);

                        CustomerList.add(c);
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
        return CustomerList;
    }
}
