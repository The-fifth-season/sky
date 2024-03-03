package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author yjl
 * @since 2024-03-03
 */
public interface IUserService extends IService<User> {

    User login(UserLoginDTO userLoginDTO);
}
