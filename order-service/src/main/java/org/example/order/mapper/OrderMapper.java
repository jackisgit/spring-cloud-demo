package org.example.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.common.entity.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
