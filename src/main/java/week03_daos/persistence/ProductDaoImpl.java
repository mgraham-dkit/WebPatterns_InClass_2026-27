package week03_daos.persistence;

import week03_daos.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
    private String username = "root";
    private String password = "";

    public boolean addProduct(String productCode, String productName,
                                     String productLine, String productScale,
                                     String productVendor, String productDescription,
                                     int quantityInStock, double buyPrice, double msrp){
        // Create variables to hold database details
        // This supports clearer intention in code, and avoids "magic" numbers/string

        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try(Connection conn = DriverManager.getConnection(url, username, password)){
                // Prepare statement - Write an SQL statement and compile it into something
                // the database can actually run
                String sql = "INSERT INTO products VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    // Populate placeholder
                    ps.setString(1, productCode);
                    ps.setString(2, productName);
                    ps.setString(3, productLine);
                    ps.setString(4, productScale);
                    ps.setString(5, productVendor);
                    ps.setString(6, productDescription);
                    ps.setInt(7, quantityInStock);
                    ps.setDouble(8, buyPrice);
                    ps.setDouble(9, msrp);

                    // Run query - Execute the SQL that has been compiled and get the results
                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected > 0){
                        return true;
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
        return false;
    }

    public static List<Product> getProductsByProductLine(String prodLine) {
        // Create variable to hold list of matching Products
        List<Product> products = new ArrayList<>();

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
                String sql = "SELECT * FROM products WHERE productLine = ?";
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    // Populate placeholder
                    ps.setString(1, prodLine);
                    // Run query - Execute the SQL that has been compiled and get the results
                    try(ResultSet rs = ps.executeQuery()) {
                        // Process results - loop through each row in resultset until it's empty
                        while (rs.next()) {
                            // Extract pieces from the result row - we can explicitly ask for each piece by column name
                            String productCode = rs.getString("productCode");
                            String productName = rs.getString("productName");
                            String productLine = rs.getString("productLine");
                            String productScale = rs.getString("productScale");
                            String productVendor = rs.getString("productVendor");
                            String productDescription = rs.getString("productDescription");
                            int quantityInStock = rs.getInt("quantityInStock");
                            double buyPrice = rs.getDouble("buyPrice");
                            double msrp = rs.getDouble("msrp");

                            Product p = new Product(productCode, productName, productLine,
                                    productScale, productVendor, productDescription,
                                    quantityInStock, buyPrice, msrp);
                            products.add(p);
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

        return products;
    }
    public static boolean addProduct(String productCode, String productName, String productLine, String productScale, String productVendor, String productDescription, int quantityInStock, double buyPrice, double msrp) {
        // Create variables to hold database details
        // This supports clearer intention in code, and avoids "magic" numbers/strings
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
        String username = "root";
        String password = "";

        boolean added = false;

        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try(Connection conn = DriverManager.getConnection(url, username, password)){

                String sql = "INSERT INTO products VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try(PreparedStatement ps = conn.prepareStatement(sql)){

                    ps.setString(1, productCode);
                    ps.setString(2, productName);
                    ps.setString(3, productLine);
                    ps.setString(4, productScale);
                    ps.setString(5, productVendor);
                    ps.setString(6, productDescription);
                    ps.setInt(7, quantityInStock);
                    ps.setDouble(8, buyPrice);
                    ps.setDouble(9, msrp);

                    // 5) Execute insert (a form of update) and see how many rows are impacted
                    int rowsAffected = ps.executeUpdate();
                    System.out.println("Number of rows added = " + rowsAffected);
                    if (rowsAffected > 0) {
                        return true;
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
        return added;
    }
}
