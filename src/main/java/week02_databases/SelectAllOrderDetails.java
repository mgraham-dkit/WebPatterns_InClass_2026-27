package week02_databases;

import week02_databases.entities.OrderDetails;

import java.sql.*;

public class SelectAllOrderDetails {
    static void main(String[] args) {
        // Declare database constants
        String driver = "com.mysql.cj.jdbc.Driver";
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/classicmodels";
        String username = "root";
        String password = "";

        try{
            // STep 1:
            Class.forName(driver);

            // STep 2:
            try(Connection conn = DriverManager.getConnection(dbUrl, username, password)){
                String sql = "SELECT * FROM orderdetails";
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    ResultSet rs = ps.executeQuery();

                    while(rs.next()){
                        int orderNumber = rs.getInt("orderNumber");
                        String productCode = rs.getString("productCode");
                        int quantityOrdered = rs.getInt("quantityOrdered");
                        double priceEach = rs.getDouble("priceEach");
                        int orderLineNumber = rs.getInt("orderLineNumber");

                        OrderDetails od = new OrderDetails(orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber);

                        System.out.println(od);
                    }
                }catch (SQLException e){
                    System.out.println("Exception: " + e.getMessage());
                    System.out.println("Error occured when preparing SQL");
                }
            }catch(SQLException e){
                System.out.println("Exception: " + e.getMessage());
                System.out.println("Issue occurred when making connection to database on \"" + dbUrl + "\" using \"" + username + "\"");
            }
        }catch(ClassNotFoundException e){
            System.out.println("Exception: " + e.getMessage());
            System.out.println("No driver found");
        }
    }
}
