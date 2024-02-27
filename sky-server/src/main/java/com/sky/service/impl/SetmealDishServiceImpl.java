package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.ISetmealDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 套餐菜品关系 服务实现类
 * </p>
 *
 * @author yjl
 * @since 2024-02-24
 */
@Service
@RequiredArgsConstructor
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements ISetmealDishService {

    private final SetmealDishMapper setmealDishMapper;
    @Override
    //通过套餐ID删除套餐中的餐品Setmeal_Dish
    public void removeSetMealDish(Long id) {
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SetmealDish> eq = setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        setmealDishMapper.delete(eq);
    }

    @Override
    //根据套餐id查询套餐中的菜品SetMeal_Dish
    public List<SetmealDish> queryBySetMealId(Long id) {
        return lambdaQuery().eq(SetmealDish::getSetmealId, id).list();
    }
}
