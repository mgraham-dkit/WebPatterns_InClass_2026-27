package week05_repositories.persistence;

import org.jspecify.annotations.NonNull;
import week05_repositories.entities.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private Connector connector;

    public OrderDaoImpl(Connector connector){
        this.connector = connector;
    }

    @Override
    public Order getOrderByOrderNumber(int orderNumber) {
        Order order = null;

        Connection conn = connector.getConnection();
        if (conn == null) {
            System.out.println("No connection available!");
            return null;
        }
            String sql = "SELECT * FROM orders WHERE orderNumber = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, orderNumber);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        order = mapRow(rs);
                    }
                } catch (SQLException e) {
                    System.out.println("Exception: \"" + e.getMessage() + "\"");
                    System.out.println("\tIssue occurred when processing query or results for getOrderByOrderNumber()");
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot prepare statement for getOrderByOrderNumber(): " + sql);
            }

        return order;
    }

    @Override
    public List<Order> getOrdersByCustomerNumber(int customerNum) {
        List<Order> orders = new ArrayList<>();

        Connection conn = connector.getConnection();
        if (conn == null) {
            System.out.println("No connection available!");
            return null;
        }
        String sql = "SELECT * FROM orders WHERE customerNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerNum);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRow(rs);
                    orders.add(order);
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tIssue occurred when processing query or results for getOrdersByCustomerNumber()");
            }
        } catch (SQLException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tCannot prepare statement for getOrdersByCustomerNumber(): " + sql);
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
        Connector connector = new MySqlConnector();
        OrderDao orderDao = new OrderDaoImpl(connector);

        int orderNumber = 10100;
        Order order = orderDao.getOrderByOrderNumber(orderNumber);
        System.out.println("Order for order #" + orderNumber + ": ");
        System.out.println(order);

        int customerNumber = 121;
        List<Order> orders = orderDao.getOrdersByCustomerNumber(customerNumber);
        System.out.println("Orders for customer #" + customerNumber + ": ");
        for (Order o : orders) {
            System.out.println(o);
        }
    }
}
