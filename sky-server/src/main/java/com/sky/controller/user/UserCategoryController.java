package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 * @author author
 * @since 2024-01-13
 */
@RestController
@RequestMapping("/user/category")
@Api(tags = "用户端分类接口")
@RequiredArgsConstructor
public class UserCategoryController {
    private final ICategoryService categoryService;
    private final RedisTemplate redisTemplate;
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> queryByType(@RequestParam(required = false) String type){
        List<Category> category = categoryService.queryByType(type);
        return Result.success(category);
    }

}
