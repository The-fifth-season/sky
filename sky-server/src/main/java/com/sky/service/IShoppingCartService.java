package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author yjl
 * @since 2024-03-04
 */
public interface IShoppingCartService extends IService<ShoppingCart> {

    void insert(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> listByUser();

    void deleteByUser();

    void sub(ShoppingCartDTO shoppingCartDTO);
}
