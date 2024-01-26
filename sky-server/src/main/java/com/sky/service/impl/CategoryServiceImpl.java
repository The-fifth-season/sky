package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.Result;
import com.sky.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 * @author author
 * @since 2024-01-13
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    private final CategoryMapper categoryMapper;
    @Override
    public Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        String name = categoryPageQueryDTO.getName();
        Integer type = categoryPageQueryDTO.getType();
        Page<Category> page = Page.of(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        return lambdaQuery()
                .like(name!=null,Category::getName,name)        //名字模糊查询
                .eq(type!=null,Category::getType,type)          //
                .page(page);
    }

    @Override
    public Result<String> insert(CategoryDTO categoryDto) {
        /*Category category2 = Category.builder()
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .status(StatusConstant.ENABLE)
                .createUser(BaseContext.threadLocal.get())
                .updateUser(BaseContext.threadLocal.get())
                .build();*/
        Category category = new Category();

        BeanUtils.copyProperties(categoryDto,category);

        category.setUpdateTime(LocalDateTime.now());
        category.setCreateTime(LocalDateTime.now());
        category.setStatus(StatusConstant.ENABLE);
        category.setCreateUser(BaseContext.threadLocal.get());
        category.setUpdateUser(BaseContext.threadLocal.get());

        categoryMapper.insert(category);
        return Result.success();
    }
}
