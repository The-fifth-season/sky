package com.sky.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜品 服务实现类
 * </p>
 *
 * @author yjl
 * @since 2024-01-27
 */
@Service
@RequiredArgsConstructor
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    private final DishMapper dishMapper ;
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        String name = dishPageQueryDTO.getName();
        Integer status = dishPageQueryDTO.getStatus();
        Integer categoryId = dishPageQueryDTO.getCategoryId();

        Page<Dish> page = Page.of(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        //条件构造器，用于筛选。
        Page<Dish> page1 = lambdaQuery().like(name != null, Dish::getName, name)
                .eq(status != null, Dish::getStatus, status)
                .eq(categoryId != null, Dish::getCategoryId, categoryId)
                .orderByAsc(Dish::getPrice)
                .page(page);
        //要返回的是，筛选完后的总记录数
        long total = page1.getTotal();
        List<Dish> records = page1.getRecords();

        return new PageResult(total, records);
    }

    @Override
    public void updateDishById(DishDTO dishDTO) {
        long id = dishDTO.getId();
        Dish dish = dishMapper.selectById(id);
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateById(dish);
    }
}
