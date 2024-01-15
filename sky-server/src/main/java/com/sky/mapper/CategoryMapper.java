package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品及套餐分类 Mapper 接口
 * </p>
 * @author author
 * @since 2024-01-13
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
