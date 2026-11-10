package week08_services.persistence;


import week08_services.entities.OrderDetail;

import java.util.List;

public interface OrderDetailDao {
    List<OrderDetail> getOrderDetailsByOrderNumber(int orderNumber) ;
    boolean addOrderDetail(OrderDetail orderDetail);
}
