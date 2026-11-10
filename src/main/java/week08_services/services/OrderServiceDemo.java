package week08_services.services;

import week08_services.dtos.OrderDetailDto;
import week08_services.dtos.ProductDto;
import week08_services.persistence.*;
import week08_services.repositories.OrderRepository;
import week08_services.repositories.OrderRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceDemo {
    public static void main(String[] args) {
        String propertiesFilename = "db_properties/database.properties";

        // Create one shared connector for all DAOs and the repository transaction.
        Connector connector = new MySqlConnector(propertiesFilename);
        // Create a transaction manager to handle commit and rollback on order transaction
        TransactionManager transactionManager = new JdbcTransactionManager(connector);

        // Create DAOs.
        OrderDao orderDao = new OrderDaoImpl(connector);
        OrderDetailDao orderDetailDao = new OrderDetailDaoImpl(connector);
        CustomerDao customerDao = new CustomerDaoImpl(connector);
        ProductDao productDao = new ProductDaoImpl(connector);

        // Create Repository.
        OrderRepository orderRepository = new OrderRepositoryImpl(
                orderDao, orderDetailDao, customerDao, productDao, transactionManager
        );

        // Create Service.
        OrderService orderService = new OrderServiceImpl(orderRepository);

        // Example order:
        List<OrderDetailDto> items = List.of(
                new OrderDetailDto(new ProductDto("S10_1678", "1969 Harley Davidson Ultimate Chopper",
                        "1:18 scale replica"), 2, 48.81),
                new OrderDetailDto(new ProductDto("S10_1949", "1952 Alpine Renault 1300",
                        "1:10 scale replica"), 1, 98.58)
        );

        try {
            boolean added = orderService.placeOrder(5555, 103, LocalDateTime.now().plusDays(14), "Example order " +
                            "placed through OrderService", items
            );
            if(added) {
                System.out.println("New order created successfully");
            }else{
                System.out.println("Order could not be added.");
            }
        } catch (RuntimeException e) {
            System.out.println("The order could not be created: " + e.getMessage());
        } finally {
            connector.freeConnection();
        }
    }
}
