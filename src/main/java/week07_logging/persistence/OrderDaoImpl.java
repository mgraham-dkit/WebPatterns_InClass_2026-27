package week07_logging.persistence;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week07_logging.entities.Order;
import week07_logging.exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
    private Connector connector;

    public OrderDaoImpl(Connector connector){
        this.connector = connector;
    }

    @Override
    public Order getOrderByOrderNumber(int orderNumber){
        Order order = null;

        Connection conn = connector.getConnection();

        String sql = "SELECT * FROM orders WHERE orderNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    order = mapRow(rs);
                }
            } catch (SQLException e) {
                logger.error("getOrderByOrderNumber(): An issue occurred when running the query or processing " +
                        "the resultset. \nException: {}", e.getMessage());
            }
        } catch (SQLException e) {
            logger.error("getOrderByOrderNumber() - The SQL query could not be prepared. \nException: {}",
                    e.getMessage());
        }

        return order;
    }

    @Override
    public List<Order> getOrdersByCustomerNumber(int customerNum){
        List<Order> orders = new ArrayList<>();

        Connection conn = connector.getConnection();

        String sql = "SELECT * FROM orders WHERE customerNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerNum);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRow(rs);
                    orders.add(order);
                }
            }  catch (SQLException e) {
                logger.error("getOrdersByCustomerNumber(): An issue occurred when running the query or processing " +
                        "the resultset. \nException: {}", e.getMessage());
            }
        } catch (SQLException e) {
            logger.error("getOrdersByCustomerNumber() - The SQL query could not be prepared. \nException: {}",
                    e.getMessage());
        }

        return orders;
    }

    private static @NonNull Order mapRow(ResultSet rs) throws SQLException {
        // Example of parsing a datetime out of database into an object - useful!
        Order order = new Order(
                rs.getInt("orderNumber"),
                rs.getObject("orderDate", LocalDateTime.class),
                rs.getObject("requiredDate", LocalDateTime.class),
                rs.getObject("shippedDate", LocalDateTime.class),
                rs.getString("status"),
                rs.getString("comments"),
                rs.getInt("customerNumber")
        );
        return order;
    }

    static void main(String[] args) {
        String propertiesFilename = "db_properties/database.properties";
        // Create connection manager object
        Connector connector = new MySqlConnector(propertiesFilename);
        OrderDao orderDao = new OrderDaoImpl(connector);

        int orderNumber = 10100;
        try {
            Order order = orderDao.getOrderByOrderNumber(orderNumber);
            System.out.println("Order for order #" + orderNumber + ": ");
            System.out.println(order);
        } catch (DatabaseConnectionException e) {
            System.out.println("An error occurred while attempting to retrieve Order with order number # " + orderNumber);
            logger.error("Error occurred when searching for order # {}. \nException: {}", orderNumber, e.getMessage());
        }
        System.out.println("--------------------------------------------------");
        int customerNumber = 121;
        try {
            List<Order> orders = orderDao.getOrdersByCustomerNumber(customerNumber);
            System.out.println("Orders for customer #" + customerNumber + ": ");
            for (Order o : orders) {
                System.out.println(o);
            }
        } catch (DatabaseConnectionException e) {
            System.out.println("An error occurred while attempting to retrieve orders for customer # " + customerNumber);
            logger.error("Error occurred when searching for orders for customer # {}. \nException: {}",
                    customerNumber, e.getMessage());
        }
    }
}
