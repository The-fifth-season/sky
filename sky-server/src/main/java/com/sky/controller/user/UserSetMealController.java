package com.sky.controller.user;

import com.sky.BloomFilter.BloomFilterUtils;
import com.sky.BloomFilter.BloomKeysConstant;
import com.sky.entity.SetMeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.impl.SetMealServiceImpl;
import com.sky.service.impl.SetmealDishServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

//TODO 查询数据库中不存在的数据，依旧会缓存入redis中，后续优化！！！ (完成了！通过布隆过滤器实现！！)
    @GetMapping("/user/setmeal/list")
    @ApiOperation("根据分类ID查询套餐")
    @Cacheable(cacheNames = "SetMeal" , key = "#categoryId" , unless= "#result.code==0")
    public Result<List<SetMeal>> queryByCategoryId(Long categoryId){

        if (BloomFilterUtils.isNotInBloom(BloomKeysConstant.Category,categoryId)) {
            return Result.error("查询错误");
        }else {
            List<SetMeal> list = setMealService.queryByCategoryId(categoryId);
            if (list.isEmpty()){
                return Result.error("查询错误");
            }
            BloomFilterUtils.addToBloom(BloomKeysConstant.Category,categoryId);      //并再次添加布隆过滤器，因为可能hash冲突
            return Result.success(list);
        }
    }
    @GetMapping("/user/setmeal/dish/{id}")
    @ApiOperation("根据套餐ID查询菜品")
    //cacheManager ="cacheManager1Hour" ,
    //要用unless调用返回的结果result，如果用condition判断，则是在执行方法之前判断，result必定为null，因此不走缓存
    @Cacheable(cacheNames = "SetMeal:Dish" , key = "#id", unless= "#result.code==0")              //redis中的二级目录，在SetMeal目录下，便于操作
    public Result<List<SetmealDish>> queryById(@PathVariable Long id){
        //如果缓存中没有，则执行方法体；
        //查询布隆过滤器，没有则返回null，有的话则查询数据库，不为null则存缓存
        if (BloomFilterUtils.isNotInBloom(BloomKeysConstant.SetMeal,id)) {
            return Result.error("查询错误");
        }else {
            List<SetmealDish> list = setmealDishService.queryBySetMealId(id);
            if (list.isEmpty()){
                return Result.error("查询错误");
            }
            BloomFilterUtils.addToBloom(BloomKeysConstant.SetMeal,id);      //并再次添加布隆过滤器，因为可能hash冲突
            return Result.success(list);
        }
    }
}
