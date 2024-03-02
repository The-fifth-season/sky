package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "查询店铺状态")
public class UserStatusController {
    private final RedisTemplate redisTemplate;
    @GetMapping("/user/shop/status")
    @Cacheable(cacheNames = "Shop_Status",key = "2")
    public Result<Integer> userStatus() {
        /*Integer status = (Integer) redisTemplate.opsForValue().get("Shop_Status");
        return Result.success(status);*/
        return Result.success();
    }
}
