package org.example.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.common.entity.Product;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
