package week05_repositories.persistence;


import week05_repositories.entities.OrderDetail;

import java.util.List;

public interface OrderDetailDao {
    List<OrderDetail> getOrderDetailsByOrderNumber(int orderNumber);
}
