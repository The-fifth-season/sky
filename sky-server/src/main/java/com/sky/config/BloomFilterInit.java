package com.sky.config;

import com.sky.constant.BloomKeysConstant;
import com.sky.entity.Category;
import com.sky.entity.SetMeal;
import com.sky.service.impl.CategoryServiceImpl;
import com.sky.service.impl.SetMealServiceImpl;
import com.sky.utils.BloomFilterUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import xin.altitude.cms.common.util.EntityUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BloomFilterInit implements ApplicationRunner {
    private final CategoryServiceImpl categoryService ;
    private final SetMealServiceImpl setMealService ;
/*    public String list(){
        log.info("开始初始化布隆过滤器");
        List<SetMeal> setMealList = setMealService.list();
        List<Category> categoryList = categoryService.list();
        List<Long> list1 = EntityUtils.toList(setMealList, SetMeal::getId);
        List<Long> list2 = EntityUtils.toList(categoryList, Category::getId);
        BloomFilterUtils.addToBloom(BloomKeysConstant.SetMeal,list1);
        BloomFilterUtils.addToBloom(BloomKeysConstant.Category,list2);
        return "布隆过滤器初始化成功！";
    }*/

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始初始化布隆过滤器");
        List<SetMeal> setMealList = setMealService.list();
        List<Category> categoryList = categoryService.list();
        List<Long> list1 = EntityUtils.toList(setMealList, SetMeal::getId);
        List<Long> list2 = EntityUtils.toList(categoryList, Category::getId);
        BloomFilterUtils.addToBloom(BloomKeysConstant.SetMeal,list1);
        BloomFilterUtils.addToBloom(BloomKeysConstant.Category,list2);
    }
}
