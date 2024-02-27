package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺营业接口
*/

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺状态接口")
public class ShopController {

    private final RedisTemplate redisTemplate;
    static final String Key1 = "Shop_Status";

    public ShopController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus(){
        Integer o = (Integer) redisTemplate.opsForValue().get(Key1);
        return Result.success(o);
    }

    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result<Integer> putStatus(@PathVariable Integer status){
         redisTemplate.opsForValue().set(Key1,status);
        return Result.success();
    }
}
