package Intro_ex;

import java.sql.*;
import java.util.ArrayList;
import Intro_ex.entities.ProductDetails;


public class SellectAllProducts {
    static void main() {
        // Create variables to hold database details
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
        String username = "root";
        String password = "";

        //Load Driver
        try {
            Class.forName(driver);

            //Connect to database
            try(Connection conn = DriverManager.getConnection(url, username, password)){
                //Prepare statement
                String sql = "SELECT * FROM products";
                try(PreparedStatement ps = conn.prepareStatement(sql)){

                    //Creating the ArrayList
                    ArrayList<ProductDetails> pd = new ArrayList<>();
                    //Run Query
                    ResultSet rs = ps.executeQuery();
                    //Process Results
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
                }
                catch(SQLException e){
                    System.out.println("Cannot prepare statement: " + sql);
                }

            }catch(SQLException e){
                System.out.println("Cannot establish a connection to " + url);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("No driver files found - please check dependencies");
        }

    }
}
