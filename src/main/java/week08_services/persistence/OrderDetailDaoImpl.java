package week08_services.persistence;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week08_services.entities.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailDaoImpl.class);
    private Connector connector;

    public OrderDetailDaoImpl(Connector connector) {
        this.connector = connector;
    }

    private static @NonNull OrderDetail mapRow(ResultSet rs) throws SQLException {
        OrderDetail orderDetail = new OrderDetail(
                rs.getInt("orderNumber"),
                rs.getString("productCode"),
                rs.getInt("quantityOrdered"),
                rs.getDouble("priceEach"),
                rs.getInt("orderLineNumber")
        );
        return orderDetail;
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderNumber(int orderNumber)  {
        List<OrderDetail> orderDetails = new ArrayList<>();

        Connection conn = connector.getConnection();
        String sql = "SELECT * FROM orderdetails WHERE orderNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail od = mapRow(rs);
                    orderDetails.add(od);
                }
            } catch (SQLException e) {
                LOGGER.error("An error occurred in getOrderDetailsByOrderNumber() when attempting to select " +
                        "all order details for order # " +
                        "{}" +
                        ".\nException: \"{}\"", orderNumber, e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.error("An error occurred in getOrderDetailsByOrderNumber() when attempting to prepare statement " +
                    "for {}. \nException: \"{}\"", sql, e.getMessage());
        }

        return orderDetails;
    }

    @Override
    public boolean addOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null) {
            throw new IllegalArgumentException("Cannot add a null OrderDetail to database");
        }

        Connection conn = connector.getConnection();
        String sql = "INSERT INTO orderdetails (orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderDetail.orderNumber());
            ps.setString(2, orderDetail.productCode());
            ps.setInt(3, orderDetail.quantityOrdered());
            ps.setDouble(4, orderDetail.priceEach());
            ps.setInt(5, orderDetail.orderLineNumber());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.error("An error occurred in addOrderDetail() when attempting to add an order detail for # {}.\nException: \"{}\"", orderDetail.orderNumber(), e.getMessage());
        }
        return false;
    }

    static void main(String[] args) {
        String propertiesFilename = "db_properties/database.properties";
        // Create connection manager object
        Connector connector = new MySqlConnector(propertiesFilename);
        OrderDetailDao orderDetailDao = new OrderDetailDaoImpl(connector);
        int orderNumber = 10100;
        List<OrderDetail> orderDetails = orderDetailDao.getOrderDetailsByOrderNumber(orderNumber);

        System.out.println("Order Details for order #" + orderNumber);
        for (OrderDetail orderDetail : orderDetails) {
            System.out.println(orderDetail);
        }
    }
}
