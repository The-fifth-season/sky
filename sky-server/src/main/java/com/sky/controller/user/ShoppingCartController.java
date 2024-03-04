package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.IShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author yjl
 * @since 2024-03-04
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "用户购物车接口")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final IShoppingCartService shoppingCartService ;
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    @CacheEvict(cacheNames = "shoppingCartList" ,  key = "#baseContext.getCurrentId()")
    public Result<String> insert(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.insert(shoppingCartDTO);
        return Result.success();
    }
    @GetMapping("/list")
    @ApiOperation("查询购物车列表")
    @Cacheable(cacheNames = "shoppingCartList" , key = "#baseContext.getCurrentId()")
    public Result<List<ShoppingCart>> listResult(){
        List<ShoppingCart> shoppingCartList = shoppingCartService.listByUser();
        return Result.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    @ApiOperation("删除购物车")
    @CacheEvict(cacheNames = "shoppingCartList" ,  key = "#baseContext.getCurrentId()")
    public Result<String> delete(){
        shoppingCartService.deleteByUser();
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("删除购物车的单个商品")
    @CacheEvict(cacheNames = "shoppingCartList" ,  key = "#baseContext.getCurrentId()")
    public Result<String> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }
}
