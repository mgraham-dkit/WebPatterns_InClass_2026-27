package week06_testing_and_properties.persistence;


import week06_testing_and_properties.entities.OrderDetail;

import java.util.List;

public interface OrderDetailDao {
    List<OrderDetail> getOrderDetailsByOrderNumber(int orderNumber);
}
