package week08_services.repositories;

import week08_services.dtos.OrderDto;
import week08_services.entities.Order;
import week08_services.entities.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDto> getOrder(int orderNumber);
    List<OrderDto> getOrdersForCustomer(int customerNumber);
    boolean addOrder(Order order, List<OrderDetail> orderDetails);
}
