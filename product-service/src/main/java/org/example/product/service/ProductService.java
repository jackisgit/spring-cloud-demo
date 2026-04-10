package org.example.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.entity.Product;

public interface ProductService extends IService<Product> {
    
    Product getProductByName(String name);
    
    IPage<Product> getProductPage(int current, int size);
    
    boolean updateStock(Long productId, Integer quantity);
}
