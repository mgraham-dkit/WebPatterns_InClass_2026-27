package week07_logging.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import week07_logging.dtos.OrderDetailDto;
import week07_logging.dtos.OrderDto;
import week07_logging.dtos.ProductDto;
import week07_logging.entities.Customer;
import week07_logging.entities.Order;
import week07_logging.entities.OrderDetail;
import week07_logging.entities.Product;
import week07_logging.exceptions.DatabaseConnectionException;
import week07_logging.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {
    private static Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    private final OrderDao orderDao;
    private final OrderDetailDao orderDetailDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    public OrderRepositoryImpl(OrderDao orderDao, OrderDetailDao orderDetailDao,
                               CustomerDao customerDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderDetailDao = orderDetailDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    @Override
    public Optional<OrderDto> getOrder(int orderNumber) {
        Order order = orderDao.getOrderByOrderNumber(orderNumber);
        if(order == null){
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

    static void main(String[] args) {
        String propertiesFilename = "db_properties/database.properties";
        // Create connection manager object
        Connector connector = new MySqlConnector(propertiesFilename);

        // Create DAOs to access database using this connection manager
        OrderDao orderDao = new OrderDaoImpl(connector);
        OrderDetailDao orderDetailDao = new OrderDetailDaoImpl(connector);
        ProductDao productDao = new ProductDaoImpl(connector);
        CustomerDao customerDao = new CustomerDaoImpl(connector);

        // Create repository that uses DAOs to carry out an action and get a result
        OrderRepository orderRepository = new OrderRepositoryImpl(orderDao, orderDetailDao, customerDao, productDao);

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
        }catch(DatabaseConnectionException e){
            logger.error("Could not establish connection to database", e);
        }
    }
}
