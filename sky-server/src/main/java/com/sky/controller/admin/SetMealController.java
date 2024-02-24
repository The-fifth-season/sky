package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.PageResult;
import com.sky.entity.SetMeal;
import com.sky.result.Result;
import com.sky.service.ISetMealService;
import com.sky.service.ISetmealDishService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@RequiredArgsConstructor
public class SetMealController {
    private final ISetMealService setMealService;
    private final ISetmealDishService setmealDishService;
    /**
     * 菜品分页查询
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
    public Result<String> remove(@RequestParam String ids){
        String[] split = ids.split(",");
        setMealService.removeByIds(Arrays.asList(split));
        return Result.success();
    }
    @PostMapping
    @ApiOperation("新增套餐")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO){
        setMealService.insert(setmealDTO);
        setmealDishService.saveBatch(setmealDTO.getSetmealDishes());
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据类型查询套餐")
    public Result<SetmealVO> queryById(@PathVariable(required = false) Long id){
        SetmealVO meal = setMealService.queryById(id);
        return Result.success(meal);
    }

    @PutMapping
    public Result<String> updateById(@RequestBody SetmealDTO setmealDTO){
        SetMeal setMeal = setMealService.getById(setmealDTO.getId());
        BeanUtils.copyProperties(setmealDTO,setMeal);
        setMealService.saveOrUpdate(setMeal);
        return Result.success();
    }
}
