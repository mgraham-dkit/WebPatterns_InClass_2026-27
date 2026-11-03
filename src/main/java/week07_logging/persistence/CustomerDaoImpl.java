package week07_logging.persistence;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week07_logging.entities.Customer;
import week07_logging.exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDaoImpl implements CustomerDao {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);
    private Connector connector;

    public CustomerDaoImpl(Connector connector) {
        this.connector = connector;
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

    @Override
    public Customer getCustomerByCustomerNumber(int customerNumber){
        Customer customer = null;

        Connection conn = connector.getConnection();
        String sql = "SELECT * FROM customers WHERE customerNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = mapRow(rs);
                }
            } catch (SQLException e) {
                logger.error("getCustomerByCustomerNumber(): An issue occurred when running the query or processing " +
                        "the resultset. \nException: {}", e.getMessage());
            }
        } catch (SQLException e) {
            logger.error("getCustomerByCustomerNumber() - The SQL query could not be prepared. \nException: {}",
                    e.getMessage());
        }

        return customer;
    }

    static void main(String[] args) {
        String propertiesFilename = "db_properties/database.properties";
        // Create connection manager object
        Connector connector = new MySqlConnector(propertiesFilename);
        CustomerDao customerDao = new CustomerDaoImpl(connector);
        int customerNumber = 5;
        try {
            Customer customer = customerDao.getCustomerByCustomerNumber(customerNumber);
            System.out.println("Customer matching customerNumber #" + customerNumber + ":");
            System.out.println(customer);
        } catch (DatabaseConnectionException e) {
            System.out.println("An error occurred while attempting to retrieve customer with id # " + customerNumber);
            logger.error("Error occurred when searching for customer # {}", customerNumber);
        }

        customerNumber = 103;
        try {
            Customer customer = customerDao.getCustomerByCustomerNumber(customerNumber);
            System.out.println("Customer matching customerNumber #" + customerNumber + ":");
            System.out.println(customer);
        } catch (DatabaseConnectionException e) {
            System.out.println("An error occurred while attempting to retrieve customer with id # " + customerNumber);
            logger.error("Error occurred when searching for customer # {}", customerNumber);
        }
    }
}
