package org.example.common.feign.fallback;

import org.example.common.entity.Order;
import org.example.common.feign.OrderFeignClient;
import org.example.common.result.Result;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFeignFallback implements OrderFeignClient {
    
    @Override
    public Result<Order> getOrderById(Long id) {
        return Result.error("订单服务暂时不可用");
    }
    
    @Override
    public Result<List<Order>> getOrdersByUserId(Long userId) {
        return Result.error("订单服务暂时不可用");
    }
    
    @Override
    public Result<Order> getOrderByOrderNo(String orderNo) {
        return Result.error("订单服务暂时不可用");
    }
    
    @Override
    public Result<Order> createOrder(Order order) {
        return Result.error("订单服务暂时不可用");
    }
    
    @Override
    public Result<Order> updateOrder(Long id, Order order) {
        return Result.error("订单服务暂时不可用");
    }
    
    @Override
    public Result<Void> updateOrderStatus(Long id, Integer status) {
        return Result.error("订单服务暂时不可用");
    }
    
    @Override
    public Result<Void> deleteOrder(Long id) {
        return Result.error("订单服务暂时不可用");
    }
}
