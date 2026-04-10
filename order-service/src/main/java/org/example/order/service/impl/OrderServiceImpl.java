package org.example.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.example.common.entity.Order;
import org.example.common.entity.Product;
import org.example.common.entity.User;
import org.example.common.feign.ProductFeignClient;
import org.example.common.feign.UserFeignClient;
import org.example.common.result.Result;
import org.example.order.mapper.OrderMapper;
import org.example.order.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
    private final UserFeignClient userFeignClient;
    private final ProductFeignClient productFeignClient;
    
    @Override
    public Order getOrderByOrderNo(String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        return this.getOne(wrapper);
    }
    
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        wrapper.orderByDesc(Order::getCreateTime);
        return this.list(wrapper);
    }
    
    @Override
    @Transactional
    public boolean updateOrderStatus(Long orderId, Integer status) {
        Order order = this.getById(orderId);
        if (order == null) {
            return false;
        }
        order.setStatus(status);
        return this.updateById(order);
    }
    
    @Override
    @GlobalTransactional(name = "create-order-tx", rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        // 验证用户存在
        Result<User> userResult = userFeignClient.getUserById(order.getUserId());
        if (userResult.getData() == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证商品存在
        Result<Product> productResult = productFeignClient.getProductById(order.getProductId());
        if (productResult.getData() == null) {
            throw new RuntimeException("商品不存在");
        }
        
        Product product = productResult.getData();
        if (product.getStock() < order.getQuantity()) {
            throw new RuntimeException("库存不足");
        }
        
        // 生成订单号
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setTotalAmount(product.getPrice().multiply(new java.math.BigDecimal(order.getQuantity())));
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 扣减库存
        productFeignClient.updateStock(order.getProductId(), -order.getQuantity());
        // 保存订单
        this.save(order);
        //int i = 1 / 0;
        return order;
    }
    
    @Override
    @GlobalTransactional(name = "delete-order-tx", rollbackFor = Exception.class)
    public void deleteOrderWithStock(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 恢复库存
        productFeignClient.updateStock(order.getProductId(), order.getQuantity());
        
        // 删除订单
        this.removeById(orderId);
    }
}
