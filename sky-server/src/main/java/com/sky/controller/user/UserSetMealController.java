package com.sky.controller.user;
import com.sky.entity.SetMeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.impl.SetMealServiceImpl;
import com.sky.service.impl.SetmealDishServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "用户端套餐相关接口")
public class UserSetMealController {
    private final SetMealServiceImpl setMealService;
    private final SetmealDishServiceImpl setmealDishService;
    @GetMapping("/user/setmeal/list")
    @Cacheable(cacheNames = "SetMeal" , key = "#categoryId")
    public Result<List<SetMeal>> queryByCategoryId(Long categoryId){
        List<SetMeal> list = setMealService.queryByCategoryId(categoryId);
        return Result.success(list);
    }
    @GetMapping("/user/setmeal/dish/{id}")
    @Cacheable(cacheNames = "SetMeal::Dish" , key = "#id")
    public Result<List<SetmealDish>> queryById(@PathVariable Long id){
        List<SetmealDish> list = setmealDishService.queryBySetMealId(id);
        if (list.isEmpty()){
            return Result.error("查询异常");
        }
        return Result.success(list);
    }
}
