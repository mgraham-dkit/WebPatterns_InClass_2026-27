package week05_repositories.persistence;

import week05_repositories.entities.Order;

import java.util.List;

public interface OrderDao {
    Order getOrderByOrderNumber(int orderNumber);
    List<Order> getOrdersByCustomerNumber(int customerNum);
}
