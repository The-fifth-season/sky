package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.PageResult;
import com.sky.entity.SetMeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.ISetMealService;
import com.sky.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 * @author yjl
 * @since 2024-01-27
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements ISetMealService {
    private final SetMealMapper setMealMapper;
    private final SetmealDishMapper setmealDishMapper;
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {

        String name = setmealPageQueryDTO.getName();
        Integer status = setmealPageQueryDTO.getStatus();
        Integer categoryId = setmealPageQueryDTO.getCategoryId();

        Page<SetMeal> page = Page.of(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetMeal> page1 = lambdaQuery().like(name != null, SetMeal::getName, name)
                .eq(status != null, SetMeal::getStatus, status)
                .eq(categoryId != null, SetMeal::getCategoryId, categoryId)
                .orderByAsc(SetMeal::getPrice)
                .page(page);
        long total = page1.getTotal();
        List<SetMeal> records = page1.getRecords();
        return new PageResult(total,records);
    }

    @Override
    public Long insert(SetmealDTO setmealDTO) {
        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setmealDTO,setMeal);
        int insert = setMealMapper.insert(setMeal);
        log.info("插入成功的返回值：{}",insert);
        return setMeal.getId();
    }

    @Override
    public SetmealVO queryById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        SetMeal setMeal = setMealMapper.selectById(id);
        BeanUtils.copyProperties(setMeal,setmealVO);
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        QueryWrapper<SetmealDish> setmealId = setmealDishQueryWrapper.eq("setmeal_id", id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(setmealId);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    public List<SetMeal> queryByCategoryId(Long categoryId) {
        return lambdaQuery().eq(SetMeal::getCategoryId, categoryId).list();
    }

}
