package week06_testing_and_properties.repositories;

import week06_testing_and_properties.dtos.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDto> getOrder(int orderNumber);
    List<OrderDto> getOrdersForCustomer(int customerNumber);
}
