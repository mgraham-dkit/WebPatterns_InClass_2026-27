package week08_services.services;

import week08_services.dtos.OrderDetailDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    boolean placeOrder(int orderNum, int customerNumber, LocalDateTime requiredDate,
                              String comments, List<OrderDetailDto> items);
}
