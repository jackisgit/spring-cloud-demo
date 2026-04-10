package org.example.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.entity.Product;
import org.example.product.mapper.ProductMapper;
import org.example.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    
    @Override
    public Product getProductByName(String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getName, name);
        return this.getOne(wrapper);
    }
    
    @Override
    public IPage<Product> getProductPage(int current, int size) {
        Page<Product> page = new Page<>(current, size);
        return this.page(page);
    }
    
    @Override
    @Transactional
    public boolean updateStock(Long productId, Integer quantity) {
        Product product = this.getById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        if (product.getStock() + quantity < 0) {
            throw new RuntimeException("库存不足");
        }
        //try {
        //    Thread.sleep(5000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        product.setStock(product.getStock() + quantity);
        return this.updateById(product);
    }
}
