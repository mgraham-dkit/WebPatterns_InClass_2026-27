package week07_logging.persistence;

import week07_logging.entities.Order;

import java.util.List;

public interface OrderDao {
    Order getOrderByOrderNumber(int orderNumber);
    List<Order> getOrdersByCustomerNumber(int customerNum);
}
