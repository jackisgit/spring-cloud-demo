package org.example.common.feign.fallback;

import org.example.common.entity.Product;
import org.example.common.feign.ProductFeignClient;
import org.example.common.result.Result;
import org.springframework.stereotype.Component;

@Component
public class ProductFeignFallback implements ProductFeignClient {
    
    @Override
    public Result<Product> getProductById(Long id) {
        return Result.error("商品服务暂时不可用");
    }
    
    @Override
    public Result<Product> getProductByName(String name) {
        return Result.error("商品服务暂时不可用");
    }
    
    @Override
    public Result<Product> createProduct(Product product) {
        return Result.error("商品服务暂时不可用");
    }
    
    @Override
    public Result<Product> updateProduct(Long id, Product product) {
        return Result.error("商品服务暂时不可用");
    }
    
    @Override
    public Result<Void> updateStock(Long id, Integer quantity) {
        return Result.error("商品服务暂时不可用");
    }
    
    @Override
    public Result<Void> deleteProduct(Long id) {
        return Result.error("商品服务暂时不可用");
    }
}
