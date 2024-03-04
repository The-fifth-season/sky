package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author yjl
 * @since 2024-03-04
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
