package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.DishDTO;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.service.IDishFlavorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜品口味关系表 服务实现类
 * </p>
 *
 * @author yjl
 * @since 2024-01-30
 */
@Service
@RequiredArgsConstructor
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements IDishFlavorService {
    private final DishFlavorMapper dishFlavorMapper;

    @Override
    public void updateDishFlavor(DishDTO dishDTO) {
        long id = dishDTO.getId();
        //用lambda的方法不行，delete里面得是条件构造器，这个不行
        //LambdaQueryChainWrapper<DishFlavor> eq1 = lambdaQuery().eq(DishFlavor::getDishId, id);

        //条件构造器，配合delete删除数据的两种方法，，，，
        //方法一：
        /*QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        QueryWrapper<DishFlavor> wrapper = dishFlavorQueryWrapper.eq("dish_id", id);*/

        //方法二：
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DishFlavor> eq = queryWrapper.eq(DishFlavor::getDishId, id);
        //先把原本的数据删除，再插入新的数据
        dishFlavorMapper.delete(eq);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (!flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
            flavors.forEach(dishFlavorMapper::insert);
        }
    }
}
