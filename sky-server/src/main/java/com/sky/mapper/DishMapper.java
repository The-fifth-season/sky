package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品 Mapper 接口
 * </p>
 *
 * @author yjl
 * @since 2024-01-27
 */

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
