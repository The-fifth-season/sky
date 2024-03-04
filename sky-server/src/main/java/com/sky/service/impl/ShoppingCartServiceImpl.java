package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.SetMeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.IShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/**
 * <p>
 * 购物车 服务实现类
 * </p>
 * @author yjl
 * @since 2024-03-04
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;
    private final SetMealMapper setMealMapper;
    @Override
    public void insert(ShoppingCartDTO shoppingCartDTO) {
        //获取用户id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .number(1)
                .createTime(LocalDateTime.now())
                .build();
        BeanUtil.copyProperties(shoppingCartDTO, shoppingCart);

        //根据传过来的参数，查询数据库中是否存在
        List<ShoppingCart> list = lambdaQuery().eq(shoppingCartDTO.getDishFlavor() != null, ShoppingCart::getDishFlavor, shoppingCartDTO.getDishFlavor())
                .eq(shoppingCartDTO.getDishId() != null, ShoppingCart::getDishId, shoppingCartDTO.getDishId())
                .eq(shoppingCartDTO.getSetmealId() != null, ShoppingCart::getSetMealId, shoppingCartDTO.getSetmealId())
                .eq(ShoppingCart::getUserId, userId)
                .list();
        //如果存在
        if(list != null && !list.isEmpty()){
            //则菜品数量在原有的基础上加一
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateById(cart);
        }
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        //如果不存在，则插入数据,判断传过来的是菜品还是套餐，因为要查询图片和价格
        if (dishId != null){
            Dish dish = dishMapper.selectById(dishId);
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
        }else if (setmealId != null){
            SetMeal setMeal = setMealMapper.selectById(setmealId);
            shoppingCart.setName(setMeal.getName());
            shoppingCart.setImage(setMeal.getImage());
            shoppingCart.setAmount(setMeal.getPrice());
        }
        shoppingCartMapper.insert(shoppingCart);
    }

    @Override
    public List<ShoppingCart> listByUser() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        return shoppingCartMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByUser() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartMapper.delete(queryWrapper);
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = lambdaQuery().eq(shoppingCartDTO.getDishFlavor() != null, ShoppingCart::getDishFlavor, shoppingCartDTO.getDishFlavor())
                .eq(shoppingCartDTO.getDishId() != null, ShoppingCart::getDishId, shoppingCartDTO.getDishId())
                .eq(shoppingCartDTO.getSetmealId() != null, ShoppingCart::getSetMealId, shoppingCartDTO.getSetmealId())
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                .getEntity();
        Integer number = shoppingCart.getNumber();
        if (number > 1){
            shoppingCart.setNumber(number - 1);
            shoppingCartMapper.updateById(shoppingCart);
        }else{
            shoppingCartMapper.deleteById(shoppingCart.getId());
        }
    }

}
