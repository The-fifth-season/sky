package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.entity.DishFlavor;

/**
 * <p>
 * 菜品口味关系表 服务类
 * </p>
 *
 * @author yjl
 * @since 2024-01-30
 */
public interface IDishFlavorService extends IService<DishFlavor> {
    void updateDishFlavor(DishDTO dishDTO);
}
