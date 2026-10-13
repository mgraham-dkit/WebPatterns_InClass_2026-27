package week05_repositories.repositories;

import week05_repositories.dtos.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDto> getOrder(int orderNumber);
    List<OrderDto> getOrdersForCustomer(int customerNumber);
}
