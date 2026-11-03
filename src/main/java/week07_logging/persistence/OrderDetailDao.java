package week07_logging.persistence;


import week07_logging.entities.OrderDetail;

import java.util.List;

public interface OrderDetailDao {
    List<OrderDetail> getOrderDetailsByOrderNumber(int orderNumber) ;
}
