package week01_databases;

import java.sql.*;

public class SampleScreencastCode {
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
                String sql = "SELECT * FROM offices";
                // 4) Prepare SQL for execution - compile it into something that can be run
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    // 5) Execute query and store results in ResultSet
                    try(ResultSet rs = ps.executeQuery()){
                        // 6) Process results
                        while(rs.next()){
                            String officeCode = rs.getString("officeCode");
                            String city = rs.getString("city");
                            String addressLine1 = rs.getString("addressLine1");
                            String territory = rs.getString("territory");

                            System.out.println("Office code: " + officeCode);
                            System.out.println(addressLine1 + ", " + city);
                            System.out.println(territory);
                            System.out.println("-------------------------------");
                        }
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
