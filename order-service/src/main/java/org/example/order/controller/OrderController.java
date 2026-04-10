package org.example.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.entity.Order;
import org.example.common.result.Result;
import org.example.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }
    
    @GetMapping("/user/{userId}")
    public Result<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return Result.success(orders);
    }
    
    @GetMapping("/orderNo/{orderNo}")
    public Result<Order> getOrderByOrderNo(@PathVariable String orderNo) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }
    
    @GetMapping("/all")
    public Result<List<Order>> getAllOrders() {
        List<Order> orders = orderService.list();
        return Result.success(orders);
    }
    
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return Result.success("订单创建成功", createdOrder);
    }
    
    @PutMapping("/{id}")
    public Result<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order existOrder = orderService.getById(id);
        if (existOrder == null) {
            return Result.error("订单不存在");
        }
        order.setId(id);
        order.setUpdateTime(LocalDateTime.now());
        orderService.updateById(order);

        return Result.success("订单更新成功", order);
    }

    
    @PutMapping("/status/{id}/{status}")
    public Result<Void> updateOrderStatus(@PathVariable Long id, @PathVariable Integer status) {
        boolean result = orderService.updateOrderStatus(id, status);
        if (!result) {
            return Result.error("订单状态更新失败");
        }
        return Result.success("订单状态更新成功", null);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderWithStock(id);
        return Result.success("删除成功", null);
    }
}
