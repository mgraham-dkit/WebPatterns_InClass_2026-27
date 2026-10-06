package solutions.dao_exercises.persistence;

import org.jspecify.annotations.NonNull;
import solutions.dao_exercises.entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public List<Customer> selectCustomersByName(String name) {
        List<Customer> customers = new ArrayList<>();

        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        if (conn == null) {
            System.out.println("No connection available!");
            return customers;
        }

            // Prepare statement - Write an SQL statement and compile it into something
            // the database can actually run
            String sql = "SELECT * FROM customers WHERE customerName = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                // Run query - Execute the SQL that has been compiled and get the results
                try (ResultSet rs = ps.executeQuery()) {
                    // Process results - loop through each row in resultset until it's empty
                    while (rs.next()) {
                        // Extract pieces from the result row - we can explicitly ask for each piece by column name
                        Customer customer = mapRow(rs);
                        customers.add(customer);
                    }
                } catch (SQLException e) {
                    System.out.println("Exception: \"" + e.getMessage() + "\"");
                    System.out.println("\tIssue occurred when processing query or results");
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot prepare statement: " + sql);
            }

        return customers;
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
    public List<Customer> selectCustomersContainingName(String name) {
        List<Customer> customers = new ArrayList<>();

        Connector connector = new Connector();
        Connection conn = connector.getConnection();

        // Prepare statement - Write an SQL statement and compile it into something
        // the database can actually run
        String sql = "SELECT * FROM customers WHERE customerName LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            // Run query - Execute the SQL that has been compiled and get the results
            try (ResultSet rs = ps.executeQuery()) {
                // Process results - loop through each row in resultset until it's empty
                while (rs.next()) {
                    // Extract pieces from the result row - we can explicitly ask for each piece by column name
                    Customer customer = mapRow(rs);
                    customers.add(customer);
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tIssue occurred when processing query or results");
            }
        } catch (SQLException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tCannot prepare statement: " + sql);
        }
        return customers;
    }

    static void main(String[] args) {
        CustomerDao customerDao = new CustomerDaoImpl();
        List<Customer> customers = customerDao.selectCustomersByName("Atelier Graphique");

        System.out.println("Customers with matching Atelier Graphique:");
        System.out.println(customers);

        customers = customerDao.selectCustomersContainingName("Z");
        System.out.println("Customers with name containing z:");
        System.out.println(customers);
    }
}
