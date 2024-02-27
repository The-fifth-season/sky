package com.sky.mapper;

import com.sky.entity.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜品口味关系表 Mapper 接口
 * </p>
 *
 * @author yjl
 * @since 2024-01-30
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    @Select("select * from dish_flavor where dish_id = ${id}")
    List<DishFlavor> getByDishId(Long id);
}
