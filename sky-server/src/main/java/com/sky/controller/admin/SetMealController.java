package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.PageResult;
import com.sky.entity.SetMeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.ISetMealService;
import com.sky.service.ISetmealDishService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
@RequiredArgsConstructor
public class SetMealController {

    private final ISetMealService setMealService;
    private final ISetmealDishService setmealDishService;
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 前端传递参数
     * @return 菜品数据
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 修改菜品状态
     * @param status 修改的状态
     * @param id     ID
     * @return 无
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品状态")
    @CacheEvict(cacheNames = "SetMeal",allEntries = true)
    public Result<Object> status(@PathVariable Integer status , @RequestParam Long id){
        SetMeal setMeal = setMealService.getById(id);
        setMeal.setStatus(status);
        setMeal.setUpdateTime(LocalDateTime.now());
        setMealService.updateById(setMeal);
        return Result.success();
    }
    /**
     * 批量删除套餐
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "SetMeal",allEntries = true)
    public Result<String> remove(@RequestParam List<Long> ids){
        ids.forEach(setmealDishService::removeSetMealDish);
        setMealService.removeByIds(ids);
        return Result.success();
    }
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "SetMeal",key = "#setmealDTO.categoryId")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO){
        Long setmealId = setMealService.insert(setmealDTO);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishService.saveBatch(setmealDishes);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据类型查询套餐")
    public Result<SetmealVO> queryById(@PathVariable(required = true) Long id){
        SetmealVO meal = setMealService.queryById(id);
        return Result.success(meal);
    }

    //修改套餐
    @PutMapping
    @CacheEvict(cacheNames = "SetMeal",allEntries = true)
    public Result<String> updateById(@RequestBody SetmealDTO setmealDTO){
        SetMeal setMeal = setMealService.getById(setmealDTO.getId());
        Long setmealId = setMeal.getId();
        //移除之前套餐绑定的菜品
        setmealDishService.removeSetMealDish(setmealId);
        //循环添加套餐中的新菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishService.saveBatch(setmealDishes);

        BeanUtils.copyProperties(setmealDTO,setMeal);
        setMealService.saveOrUpdate(setMeal);
        return Result.success();
    }
}
