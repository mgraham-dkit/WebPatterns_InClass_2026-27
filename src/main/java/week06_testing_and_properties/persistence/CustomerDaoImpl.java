package week06_testing_and_properties.persistence;

import org.jspecify.annotations.NonNull;
import week06_testing_and_properties.entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDaoImpl implements CustomerDao {
    private Connector connector;

    public CustomerDaoImpl(Connector connector){
        this.connector = connector;
    }

    @Override
    public Customer getCustomerByCustomerNumber(int customerNumber) {
        Customer customer = null;

        Connection conn = connector.getConnection();
        if (conn == null) {
            System.out.println("No connection available!");
            return null;
        }
            String sql = "SELECT * FROM customers WHERE customerNumber = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, customerNumber);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        customer = mapRow(rs);
                    }
                } catch (SQLException e) {
                    System.out.println("Exception: \"" + e.getMessage() + "\"");
                    System.out.println("\tIssue occurred when processing query or results for getCustomerById()");
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot prepare statement for getCustomerById(): " + sql);
            }

        return customer;
    }

    private static @NonNull Customer mapRow(ResultSet rs) throws SQLException {
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
        return customer;
    }

    static void main(String[] args) {
        String propertiesFilename = "db_properties/database.properties";
        // Create connection manager object
        Connector connector = new MySqlConnector(propertiesFilename);
        CustomerDao customerDao = new CustomerDaoImpl(connector);
        int customerNumber = 5;
        Customer customer = customerDao.getCustomerByCustomerNumber(customerNumber);
        System.out.println("Customer matching customerNumber #" + customerNumber +":");
        System.out.println(customer);

        customerNumber = 103;
        customer = customerDao.getCustomerByCustomerNumber(customerNumber);
        System.out.println("Customer matching customerNumber #" + customerNumber +":");
        System.out.println(customer);
    }
}
