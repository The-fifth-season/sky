package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.IDishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 * 菜品 服务实现类
 * </p>
 * @author yjl
 * @since 2024-01-27
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    private final DishMapper dishMapper ;
    private final DishFlavorMapper dishFlavorMapper ;
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

    @Override
    public ArrayList<DishVO> queryByCategory(Long categoryId) {
        log.info("当前分类ID为:{}",categoryId);
        ArrayList<DishVO> dishVOS = new ArrayList<>();
        List<Dish> dishes = lambdaQuery()
                .eq(Dish::getCategoryId, categoryId)
                //.eq(Dish::getStatus, StatusConstant.ENABLE)
                .list();
        for (Dish dish : dishes) {
            DishVO dishVO = new DishVO();                       //必须在循环中反复创建对象，要不使用同一对象的话，
            Long id = dish.getId();
            List<DishFlavor> dishFlavor = dishFlavorMapper.getByDishId(id);
            dishVO.setFlavors(dishFlavor);
            BeanUtils.copyProperties(dish,dishVO);
            dishVOS.add(dishVO);
        }
        return dishVOS;
    }
}
