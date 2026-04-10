package org.example.common.feign;

import org.example.common.entity.Product;
import org.example.common.feign.fallback.ProductFeignFallback;
import org.example.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service", path = "/products", fallback = ProductFeignFallback.class)
public interface ProductFeignClient {
    
    @GetMapping("/{id}")
    Result<Product> getProductById(@PathVariable("id") Long id);
    
    @GetMapping("/name/{name}")
    Result<Product> getProductByName(@PathVariable("name") String name);
    
    @PostMapping
    Result<Product> createProduct(@RequestBody Product product);
    
    @PutMapping("/{id}")
    Result<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product);
    
    @PutMapping("/stock/{id}/{quantity}")
    Result<Void> updateStock(@PathVariable("id") Long id, @PathVariable("quantity") Integer quantity);
    
    @DeleteMapping("/{id}")
    Result<Void> deleteProduct(@PathVariable("id") Long id);
}
