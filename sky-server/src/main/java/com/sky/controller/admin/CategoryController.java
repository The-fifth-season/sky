package com.sky.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.CategoryPageResult;
import com.sky.result.Result;
import com.sky.service.ICategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 * @author author
 * @since 2024-01-13
 */
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    /**
     * 菜品分页查询
     * @param categoryPageQueryDTO 前端传递参数
     * @return 菜品数据
     */
    @GetMapping("/page")
    public Result<CategoryPageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Category> page1 = categoryService.pageQuery(categoryPageQueryDTO);
        List<Category> records = page1.getRecords();
        int size = records.size();
        long pages = page1.getPages();
        long pages1 = page1.getPages();
        System.out.println(pages+"============"+pages1);
        CategoryPageResult categoryPageResult = new CategoryPageResult((long) size, records);

        return Result.success(categoryPageResult);
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
        Category category = categoryService.getById(id);
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        categoryService.updateById(category);
        return Result.success();
    }

    /**
     * 删除菜品
     */
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result<String> remove(@RequestParam String id){
        categoryService.removeById(id);
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增分类")
    public Result<String> save(@RequestBody CategoryDTO categoryDto){
        categoryService.insert(categoryDto);
        return Result.success();
    }
}
