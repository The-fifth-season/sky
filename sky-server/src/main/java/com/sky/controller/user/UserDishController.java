package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserDishController {
    private final DishServiceImpl dishService;
    private final RedisTemplate redisTemplate;
    @GetMapping("/user/dish/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<ArrayList<DishVO>> queryByCategory(Long categoryId){

        String Key = "dish_" + categoryId;

        ArrayList<DishVO> dishList;

        dishList = (ArrayList<DishVO>) redisTemplate.opsForValue().get(Key);

        if (dishList!=null && !dishList.isEmpty()){
            return Result.success(dishList);
        }

        dishList = dishService.queryByCategory(categoryId);

        //ArrayList<DishVO> dishList2 = (ArrayList<DishVO>) redisTemplate.opsForValue().getAndDelete(Key);

        //log.info("返回的数据列表：：：{}",dishList2);

        if (dishList!=null && !dishList.isEmpty()){
            redisTemplate.opsForValue().set(Key,dishList);
        }
        return Result.success(dishList);
    }
}
