package week08_services.persistence;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week08_services.entities.Order;
import week08_services.exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class);
    private final Connector connector;

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
                LOGGER.error("getOrderByOrderNumber(): An issue occurred when running the query or processing " +
                        "the resultset. \nException: {}", e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.error("getOrderByOrderNumber() - The SQL query could not be prepared. \nException: {}",
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
                LOGGER.error("getOrdersByCustomerNumber(): An issue occurred when running the query or processing " +
                        "the resultset. \nException: {}", e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.error("getOrdersByCustomerNumber() - The SQL query could not be prepared. \nException: {}",
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

    @Override
    public boolean addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Cannot add a null Order to database");
        }

        Connection conn = connector.getConnection();
        String sql = "INSERT INTO orders (orderNumber, orderDate, requiredDate, shippedDate, status, comments, " +
                "customerNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.orderNumber());
            // When inserting a date, it's easier to use setObject and let the database figure it out!
            ps.setObject(2, order.orderDate());
            ps.setObject(3, order.requiredDate());
            ps.setObject(4, order.shippedDate());
            ps.setString(5, order.status());
            ps.setString(6, order.comments());
            ps.setInt(7, order.customerNumber());

            int addedRows = ps.executeUpdate();
            return addedRows == 1;
        } catch (SQLException e) {
            LOGGER.error("addOrder() - An issue occurred when inserting the order. Exception: {}", e.getMessage());
        }
        return false;
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
            LOGGER.error("Error occurred when searching for order # {}. \nException: {}", orderNumber, e.getMessage());
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
            LOGGER.error("Error occurred when searching for orders for customer # {}. \nException: {}",
                    customerNumber, e.getMessage());
        }
    }
}
