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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜品 前端控制器
 * </p>
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
    private final RedisTemplate redisTemplate;

    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String> updateByIds(@RequestBody DishDTO dishDTO) {
        cleanCache("dish_*");
        dishService.updateDishById(dishDTO);
        dishFlavorService.updateDishFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页")
    public Result<PageResult> queryPage(DishPageQueryDTO dishPageQueryDTO) {

        PageResult pageQuery = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageQuery);
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    public Result<String> deleteByIds(List<Long> ids) {
        cleanCache("dish_*");
        //String[] split = ids.split(",");              如果传入的是字符串，则可以这样拆解
        //List<Integer> list = Arrays.stream(split).map(Integer::parseInt).toList();
        //dishService.removeByIds(Arrays.asList(split));
        dishService.removeByIds(ids);
        return Result.success();
    }

    @PostMapping
    @ApiOperation("菜品新增")
    public Result<String> insertById(@RequestBody DishDTO dishDTO) {
        long categoryId = dishDTO.getCategoryId();
        String key = "dish_"+categoryId;
        cleanCache(key);
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishService.save(dish);
        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
        dishFlavorService.saveBatch(flavors);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("菜品查询")
    public Result<DishVO> queryById(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        List<DishFlavor> list = dishFlavorService.lambdaQuery().
                eq(DishFlavor::getDishId, id)
                .list();
        DishVO dishVO = new DishVO();
        dishVO.setFlavors(list);
        BeanUtils.copyProperties(dish, dishVO);
        return Result.success(dishVO);
    }

    @GetMapping("list")
    public Result<List<Dish>> queryDish(String categoryId) {
        List<Dish> list = dishService.lambdaQuery().eq(Dish::getCategoryId, categoryId).list();
        return Result.success(list);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("状态修改")
    public Result<String> updateStatus(@PathVariable String status, String id) {
        Dish dish = dishService.getById(id);
        Long categoryId = dish.getCategoryId();
        String key = "dish_"+categoryId;
        cleanCache(key);
        dish.setStatus(Integer.parseInt(status));
        dishService.updateById(dish);
        return Result.success();
    }

    //提取的清理缓存的方法
    private void cleanCache(String patten){
        Set keys = redisTemplate.keys(patten);
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
}
