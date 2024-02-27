package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.PageResult;
import com.sky.entity.SetMeal;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface ISetMealService extends IService<SetMeal> {
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    Long insert(SetmealDTO setmealDTO);

    SetmealVO queryById(Long id);

    List<SetMeal> queryByCategoryId(Long categoryId);

}
