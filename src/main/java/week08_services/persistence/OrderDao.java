package week08_services.persistence;

import week08_services.entities.Order;

import java.util.List;

public interface OrderDao {
    Order getOrderByOrderNumber(int orderNumber);
    List<Order> getOrdersByCustomerNumber(int customerNum);
    boolean addOrder(Order order);
}
