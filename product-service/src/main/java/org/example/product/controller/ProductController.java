package org.example.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.common.entity.Product;
import org.example.common.result.Result;
import org.example.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }
    
    @GetMapping("/name/{name}")
    public Result<Product> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }
    
    @GetMapping
    public Result<List<Product>> getAllProducts() {
        List<Product> products = productService.list();
        return Result.success(products);
    }
    
    @GetMapping("/page")
    public Result<IPage<Product>> getProductPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Product> page = productService.getProductPage(current, size);
        return Result.success(page);
    }
    
    @PostMapping
    public Result<Product> createProduct(@RequestBody Product product) {
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productService.save(product);
        return Result.success("商品创建成功", product);
    }
    
    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existProduct = productService.getById(id);
        if (existProduct == null) {
            return Result.error("商品不存在");
        }
        product.setId(id);
        product.setUpdateTime(LocalDateTime.now());
        productService.updateById(product);
        return Result.success("商品更新成功", product);
    }
    
    @PutMapping("/stock/{id}/{quantity}")
    public Result<Void> updateStock(@PathVariable Long id, @PathVariable Integer quantity) {
        boolean result = productService.updateStock(id, quantity);
        if (!result) {
            return Result.error("库存更新失败");
        }
        return Result.success("库存更新成功", null);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        boolean result = productService.removeById(id);
        if (!result) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功", null);
    }
}
