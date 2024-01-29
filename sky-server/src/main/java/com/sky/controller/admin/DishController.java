package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.IDishFlavorService;
import com.sky.service.IDishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 菜品 前端控制器
 * </p>
 *
 * @author yjl
 * @since 2024-01-27
 */
@RestController
@RequestMapping("/admin/dish")
@RequiredArgsConstructor
@Api(tags = "菜品相关接口")
public class DishController {
    private final IDishService dishService;
    private final IDishFlavorService dishFlavorService;

    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String> updateByIds(@RequestBody DishDTO dishDTO){
        dishService.updateDishById(dishDTO);
        dishFlavorService.updateDishFlavor(dishDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("菜品分页")
    public Result<PageResult> queryPage(DishPageQueryDTO dishPageQueryDTO){

        PageResult pageQuery = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageQuery);
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    public Result<String> deleteByIds(String ids){
        String[] split = ids.split(",");
        //List<Integer> list = Arrays.stream(split).map(Integer::parseInt).toList();
        dishService.removeByIds(Arrays.asList(split));

        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("菜品查询")
    public Result<DishVO> queryById(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        List<DishFlavor> list = dishFlavorService.lambdaQuery().
                eq(DishFlavor::getDishId, id)
                .list();
        DishVO dishVO = new DishVO();
        dishVO.setFlavors(list);
        BeanUtils.copyProperties(dish,dishVO);
        return Result.success(dishVO);
    }

    @GetMapping("list")
    public Result<Dish> queryDish(Long id){
        Dish dish = dishService.getById(id);
        return Result.success(dish);
    }
    @PostMapping("/status/{status}")
    @ApiOperation("状态修改")
    public Result<String> updateStatus(@PathVariable String status , String id) {
        Dish dish = dishService.getById(id);
            dish.setStatus(Integer.parseInt(status));
            dishService.updateById(dish);
            return Result.success();
        }

}
