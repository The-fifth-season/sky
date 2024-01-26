package com.sky.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.Result;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 * @author author
 * @since 2024-01-13
 */
public interface ICategoryService extends IService<Category> {

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    Result<String> insert(CategoryDTO categoryDto);
}
