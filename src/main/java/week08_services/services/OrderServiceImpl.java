package week08_services.services;

import week08_services.dtos.OrderDetailDto;
import week08_services.entities.Order;
import week08_services.entities.OrderDetail;
import week08_services.repositories.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Places a new order for a customer and returns the generated order number.
     */
    public boolean placeOrder(int orderNum, int customerNumber, LocalDateTime requiredDate,
                              String comments, List<OrderDetailDto> items) {
        if (orderNum <= 0) {
            throw new IllegalArgumentException("A valid order number must be supplied");
        }
        if (customerNumber <= 0) {
            throw new IllegalArgumentException("A valid customer number must be supplied");
        }
        if (requiredDate == null || requiredDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Required date must be today or in the future");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one item");
        }

        // Create the Order entity. The database generates the order number.
        Order order = new Order(orderNum, LocalDateTime.now(), requiredDate,null, "In Process", comments, customerNumber
        );

        // Convert the requested order items into OrderDetail entities.
        List<OrderDetail> orderDetails = new ArrayList<>();
        int orderLineNumber = 1;

        // Validate all order detail information before adding it
        for (OrderDetailDto item : items) {
            if (item == null || item.product() == null) {
                throw new IllegalArgumentException("Order items and products cannot be null");
            }
            if (item.product().productCode() == null || item.product().productCode().isBlank()) {
                throw new IllegalArgumentException("Each order item must have a product code");
            }
            if (item.quantity() <= 0) {
                throw new IllegalArgumentException("Order item quantity must be greater than zero");
            }
            if (item.price() < 0) {
                throw new IllegalArgumentException("Order item price cannot be negative");
            }

            // Note: A dummy orderNumber is used here - the repository will replace this
            // with the generated order number.
            OrderDetail orderDetail = new OrderDetail(0,  item.product().productCode(), item.quantity(), item.price(), orderLineNumber++
            );
            orderDetails.add(orderDetail);
        }

        // Call repository to handle actually adding the order
        return orderRepository.addOrder(order, orderDetails);
    }
}
