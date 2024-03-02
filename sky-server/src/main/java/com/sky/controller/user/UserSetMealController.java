package com.sky.controller.user;

import com.sky.constant.BloomKeysConstant;
import com.sky.entity.SetMeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.impl.SetMealServiceImpl;
import com.sky.service.impl.SetmealDishServiceImpl;
import com.sky.utils.BloomFilterUtils;
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
/*    private static final String Key1 = "SetMeal";
    private static final String Key2 = "SetMealDish";*/
    private final SetMealServiceImpl setMealService;
    private final SetmealDishServiceImpl setmealDishService;

//TODO 查询数据库中不存在的数据，依旧会缓存入redis中，后续优化！！！
    @GetMapping("/user/setmeal/list")
    @ApiOperation("根据分类ID查询套餐")
    //@BitMap(key = "CategoryId" , id = "#categoryId")
    @Cacheable(cacheNames = "SetMeal" , key = "#categoryId")
    public Result<List<SetMeal>> queryByCategoryId(Long categoryId){
        List<SetMeal> list = setMealService.queryByCategoryId(categoryId);
        return Result.success(list);
    }
    @GetMapping("/user/setmeal/dish/{id}")
    @ApiOperation("根据套餐ID查询菜品")
    //@BitMap(key = "SetMeal" , id = "#id")
    //要用unless调用返回的结果result，如果用condition判断，则是在执行方法之前判断，result必定为null，因此不走缓存
    @Cacheable(cacheNames = "SetMeal:Dish" , key = "#id" ,unless= "#result==null")              //redis中的二级目录，在SetMeal目录下，便于操作
    public Result<List<SetmealDish>> queryById(@PathVariable Long id){
        //如果缓存中没有，则执行方法体；
        //查询布隆过滤器，没有则返回null，有的话则查询数据库，不为null则存缓存
        if (BloomFilterUtils.isNotInBloom(BloomKeysConstant.SetMeal,id)) {
            System.out.println(id+"不存在");
            return null;
        }else {
            List<SetmealDish> list = setmealDishService.queryBySetMealId(id);
            if (list.isEmpty()){
                return null;
            }
            System.out.println(Result.success(list));
            BloomFilterUtils.addToBloom(BloomKeysConstant.SetMeal,id);      //并再次添加布隆过滤器，因为可能hash冲突
            return Result.success(list);
        }
    }
}
