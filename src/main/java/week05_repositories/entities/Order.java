package week05_repositories.entities;

import java.time.LocalDateTime;

public record Order(int orderNumber, LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate,
                    String status,
                    String comments, int customerNumber) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;
        return orderNumber == order.orderNumber;
    }

    @Override
    public int hashCode() {
        return orderNumber;
    }
}
