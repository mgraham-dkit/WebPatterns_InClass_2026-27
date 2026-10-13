package week05_repositories.persistence;

import org.jspecify.annotations.NonNull;
import week05_repositories.entities.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {
    private Connector connector;

    public OrderDetailDaoImpl(Connector connector){
        this.connector = connector;
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderNumber(int orderNumber) {
        List<OrderDetail> orderDetails = new ArrayList();

        Connection conn = connector.getConnection();
        if (conn == null) {
            System.out.println("No connection available!");
            return null;
        }
            String sql = "SELECT * FROM orderdetails WHERE orderNumber = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, orderNumber);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        OrderDetail od = mapRow(rs);
                        orderDetails.add(od);
                    }
                } catch (SQLException e) {
                    System.out.println("Exception: \"" + e.getMessage() + "\"");
                    System.out.println("\tIssue occurred when processing query or results for getOrderDetailsByOrderNumber()");
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot prepare statement for getOrderDetailsByOrderNumber(): " + sql);
            }

        return orderDetails;
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

    static void main(String[] args) {
        Connector connector = new MySqlConnector();
        OrderDetailDao orderDetailDao = new OrderDetailDaoImpl(connector);
        int orderNumber = 10100;
        List<OrderDetail> orderDetails = orderDetailDao.getOrderDetailsByOrderNumber(orderNumber);

        System.out.println("Order Details for order #" + orderNumber);
        for (OrderDetail orderDetail : orderDetails) {
            System.out.println(orderDetail);
        }
    }
}
