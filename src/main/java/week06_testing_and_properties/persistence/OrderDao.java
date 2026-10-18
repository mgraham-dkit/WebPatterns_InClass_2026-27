package week06_testing_and_properties.persistence;

import week06_testing_and_properties.entities.Order;

import java.util.List;

public interface OrderDao {
    Order getOrderByOrderNumber(int orderNumber);
    List<Order> getOrdersByCustomerNumber(int customerNum);
}
