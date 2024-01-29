package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.result.PageResult;

/**
 * <p>
 * 菜品 服务类
 * </p>
 *
 * @author yjl
 * @since 2024-01-27
 */
public interface IDishService extends IService<Dish> {

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void updateDishById(DishDTO dishDTO);
}
