package week08_services.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week08_services.dtos.OrderDetailDto;
import week08_services.dtos.OrderDto;
import week08_services.dtos.ProductDto;
import week08_services.entities.Customer;
import week08_services.entities.Order;
import week08_services.entities.OrderDetail;
import week08_services.entities.Product;
import week08_services.exceptions.DatabaseConnectionException;
import week08_services.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class);

    private final OrderDao orderDao;
    private final OrderDetailDao orderDetailDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    // Add an object to manage transactions
    private final TransactionManager transactionManager;

    public OrderRepositoryImpl(OrderDao orderDao, OrderDetailDao orderDetailDao,
                               CustomerDao customerDao, ProductDao productDao, TransactionManager transactionManager) {
        this.orderDao = orderDao;
        this.orderDetailDao = orderDetailDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
        this.transactionManager = transactionManager;
    }

    static void main(String[] args) {
        String propertiesFilename = "db_properties/database.properties";
        // Create connection manager object
        Connector connector = new MySqlConnector(propertiesFilename);

        // Create DAOs to access database using this connection manager
        OrderDao orderDao = new OrderDaoImpl(connector);
        OrderDetailDao orderDetailDao = new OrderDetailDaoImpl(connector);
        ProductDao productDao = new ProductDaoImpl(connector);
        CustomerDao customerDao = new CustomerDaoImpl(connector);

        // Create an instance of the TransactionManager to manage the transaction
        TransactionManager transactionManager = new JdbcTransactionManager(connector);

        // Create repository that uses DAOs to carry out an action and get a result
        OrderRepository orderRepository = new OrderRepositoryImpl(orderDao, orderDetailDao, customerDao, productDao,
                transactionManager);

        try {
            int orderNumber = 10100;
            Optional<OrderDto> orderDto = orderRepository.getOrder(orderNumber);
            if (orderDto.isPresent()) {
                // Extract the order from the optional wrapper
                OrderDto order = orderDto.get();

                System.out.println("Order number: " + order.orderNumber());
                System.out.println("\tOrdered by: " + order.customerName());
                System.out.println("\tOrder date: " + order.orderDate());
                System.out.println("Order items: ");
                for (OrderDetailDto item : order.items()) {
                    System.out.println("\tProduct #: " + item.product().productCode() + " -> " + item.product().productName());
                    System.out.println("\t\tPrice: €" + item.price());
                    System.out.println("\t\tQuantity ordered: " + item.quantity());
                }
                System.out.println("Order status: " + order.status());
            } else {
                System.out.println("No order found for order #" + orderNumber);
            }
        } catch (DatabaseConnectionException e) {
            LOGGER.error("Could not establish connection to database", e);
        }
    }

    @Override
    public Optional<OrderDto> getOrder(int orderNumber) {
        Order order = orderDao.getOrderByOrderNumber(orderNumber);
        if (order == null) {
            return Optional.empty();
        }
        // Get the associated Customer
        Customer customer = customerDao.getCustomerByCustomerNumber(order.customerNumber());

        // Get all components of the order
        List<OrderDetail> orderDetails = orderDetailDao.getOrderDetailsByOrderNumber(orderNumber);
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            // For each line in the order, get the associated Product
            Product product = productDao.getProductByProductCode(orderDetail.productCode());
            // Convert it to a DTO version
            ProductDto productDto = new ProductDto(product.productCode(), product.productName(), product.productDescription());
            // Create a DTO of the order item
            OrderDetailDto orderDetailDto = new OrderDetailDto(productDto, orderDetail.quantityOrdered(),
                    orderDetail.priceEach());
            // Add it to the list of order line dtos
            orderDetailDtos.add(orderDetailDto);
        }

        // Create final DTO of complete order:
        OrderDto orderDto = new OrderDto(orderNumber, customer.customerName(), order.orderDate(), order.status(),
                orderDetailDtos);
        return Optional.of(orderDto);
    }

    @Override
    public List<OrderDto> getOrdersForCustomer(int customerNumber) {
        return List.of();
    }

    @Override
    public boolean addOrder(Order order, List<OrderDetail> orderDetails) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (orderDetails == null || orderDetails.isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one order detail");
        }

        // Use the same connection for every insert so the order is saved as one transaction.
        // Start transaction
        transactionManager.begin();
        boolean completed = true;
        boolean added = orderDao.addOrder(order);

        if(added) {
            for (OrderDetail detail : orderDetails) {
                OrderDetail detailWithOrderNumber = new OrderDetail(
                        order.orderNumber(),
                        detail.productCode(),
                        detail.quantityOrdered(),
                        detail.priceEach(),
                        detail.orderLineNumber()
                );

                if (!orderDetailDao.addOrderDetail(detailWithOrderNumber)) {
                    LOGGER.error("Could not add order detail {}", detail);
                    completed = false;
                }
            }
        }
        if (added && completed) {
            // Transaction completed successfully - commit action and return new order's #
            transactionManager.commit();
            return true;
        } else {
            // Transaction failed - rollback action
            transactionManager.rollback();
            LOGGER.error("addOrder() - Order could not be saved. Transaction rolled back.");
        }
        return false;
    }
}
