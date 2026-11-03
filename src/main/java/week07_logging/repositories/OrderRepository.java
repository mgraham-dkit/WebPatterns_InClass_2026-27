package week07_logging.repositories;

import week07_logging.dtos.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDto> getOrder(int orderNumber);
    List<OrderDto> getOrdersForCustomer(int customerNumber);
}
