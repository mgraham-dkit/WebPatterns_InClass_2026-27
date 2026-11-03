package Intro_ex;

import Intro_ex.entities.ProductDetails;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public  class SellectLine {
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

                Scanner keyboard = new Scanner(System.in);  // Create a Scanner object
                System.out.println("Enter Product line: ");
                String productline = keyboard.nextLine();

                String sql = "SELECT * FROM products WHERE productLine = ? ORDER BY productCode DESC";
                // 4) Prepare SQL for execution - compile it into something that can be run
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    ps.setString(1, productline);

                    // 5) Execute query and store results in ResultSet
                    ArrayList<ProductDetails> pd = new ArrayList<>();

                    try(ResultSet rs = ps.executeQuery()){
                        // 6) Process results
                        while(rs.next()){
                            String productName = rs.getString("ProductName");
                            String productCode = rs.getString("ProductCode");
                            String productLine = rs.getString("productLine");
                            String productScale = rs.getString("productScale");
                            String productVendor = rs.getString("productVendor");
                            String productDescription = rs.getString("productDescription");
                            int quantityInStock = rs.getInt("quantityInStock");
                            double buyPrice = rs.getDouble("buyPrice");
                            double MSRP = rs.getDouble("MSRP");

                            ProductDetails currentProduct = new ProductDetails(productName,productCode,productLine,productScale,productVendor,productDescription,quantityInStock,buyPrice,MSRP);

                            pd.add(currentProduct);
                        }

                        System.out.println(pd);
                    }catch(SQLException e){
                        System.out.println("Query \"" + sql + "\" could not be executed or result extraction could " +
                                "not be completed.");
                        System.out.println("Exception reads: " + e.getMessage());
                    }
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
