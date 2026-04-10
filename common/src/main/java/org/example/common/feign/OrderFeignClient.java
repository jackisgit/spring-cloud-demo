package org.example.common.feign;

import org.example.common.entity.Order;
import org.example.common.feign.fallback.OrderFeignFallback;
import org.example.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "order-service", path = "/orders", fallback = OrderFeignFallback.class)
public interface OrderFeignClient {
    
    @GetMapping("/{id}")
    Result<Order> getOrderById(@PathVariable("id") Long id);
    
    @GetMapping("/user/{userId}")
    Result<List<Order>> getOrdersByUserId(@PathVariable("userId") Long userId);
    
    @GetMapping("/orderNo/{orderNo}")
    Result<Order> getOrderByOrderNo(@PathVariable("orderNo") String orderNo);
    
    @PostMapping
    Result<Order> createOrder(@RequestBody Order order);
    
    @PutMapping("/{id}")
    Result<Order> updateOrder(@PathVariable("id") Long id, @RequestBody Order order);
    
    @PutMapping("/status/{id}/{status}")
    Result<Void> updateOrderStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status);
    
    @DeleteMapping("/{id}")
    Result<Void> deleteOrder(@PathVariable("id") Long id);
}
