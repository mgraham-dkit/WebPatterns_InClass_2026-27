package week07_logging.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(int orderNumber, String customerName, LocalDateTime orderDate, String status,
                       List<OrderDetailDto> items) {
}
