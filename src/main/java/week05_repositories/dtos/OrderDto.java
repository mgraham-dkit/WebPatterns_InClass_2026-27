package week05_repositories.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(int orderNumber, String customerName, LocalDateTime orderDate, String status,
                       List<OrderDetailDto> items) {
}
