package com.sky.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.IUserService;
import com.sky.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author yjl
 * @since 2024-03-03
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final UserMapper userMapper;
    private final WeChatProperties weChatProperties;

    //根据微信小程序开发者平台：https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
    private static final String url = "https://api.weixin.qq.com/sns/jscode2session" ;      //Get请求
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String openId = getOpenId(userLoginDTO.getCode());
        if (openId==null){
            throw new LoginFailedException("登录失败");
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        User user = userMapper.selectOne(userQueryWrapper.eq("openid", openId));

        if (user==null){
            //该用户为新用户
            user = new User();
            user.setOpenid(openId);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        }
        return user;
    }

    //获取openid
    private String getOpenId(String code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        //发送Get请求，请求参数需要根据微信小程序开发者平台，，目的：获取openid（微信用户的唯一标识）
        String json = HttpClientUtil.doGet(url, map);

        //解析返回的json数据
        //转换为json格式
        JSONObject jsonObject = JSONObject.parseObject(json);
        //获取返回的openid
        return jsonObject.getString("openid");
    }
}
