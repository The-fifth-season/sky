package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.SetmealDish;

import java.util.List;

/**
 * <p>
 * 套餐菜品关系 服务类
 * </p>
 *
 * @author yjl
 * @since 2024-02-24
 */
public interface ISetmealDishService extends IService<SetmealDish> {
    void removeSetMealDish(Long id);

    List<SetmealDish> queryBySetMealId(Long id);
}
