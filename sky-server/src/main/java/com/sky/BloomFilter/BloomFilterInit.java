package com.sky.BloomFilter;

import com.sky.constant.BloomKeysConstant;
import com.sky.entity.Category;
import com.sky.entity.SetMeal;
import com.sky.service.impl.CategoryServiceImpl;
import com.sky.service.impl.SetMealServiceImpl;
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
//实现ApplicationRunner接口，重写里面的run方法，
public class BloomFilterInit implements ApplicationRunner {
    private final CategoryServiceImpl categoryService ;
    private final SetMealServiceImpl setMealService ;

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

    public void run(){
        log.info("开始初始化布隆过滤器");
        List<SetMeal> setMealList = setMealService.list();
        List<Category> categoryList = categoryService.list();
        List<Long> list1 = EntityUtils.toList(setMealList, SetMeal::getId);
        List<Long> list2 = EntityUtils.toList(categoryList, Category::getId);
        BloomFilterUtils.addToBloom(BloomKeysConstant.SetMeal,list1);
        BloomFilterUtils.addToBloom(BloomKeysConstant.Category,list2);
    }
}
