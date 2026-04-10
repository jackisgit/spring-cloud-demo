package org.example.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.entity.Order;

import java.util.List;

public interface OrderService extends IService<Order> {
    
    Order getOrderByOrderNo(String orderNo);
    
    List<Order> getOrdersByUserId(Long userId);
    
    boolean updateOrderStatus(Long orderId, Integer status);
    
    Order createOrder(Order order);
    
    void deleteOrderWithStock(Long orderId);
}
