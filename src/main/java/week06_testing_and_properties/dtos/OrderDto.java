package week06_testing_and_properties.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(int orderNumber, String customerName, LocalDateTime orderDate, String status,
                       List<OrderDetailDto> items) {
}
