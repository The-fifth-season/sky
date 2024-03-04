package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.IUserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户端登录接口")
@RequiredArgsConstructor
public class UserController {
    //声明一个IUserService类型的变量
private final IUserService userService;
    //声明一个JwtProperties类型的变量
    private final JwtProperties jwtProperties ;


    @PostMapping("/user/login")
    @ApiOperation("微信登录功能")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        User user = userService.login(userLoginDTO);
        HashMap<String, Object> claim = new HashMap<>();
        claim.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claim);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .token(token)
                .openid(user.getOpenid())
                .id(user.getId())
                .build();

        return Result.success(userLoginVO);
    }

    @PostMapping("/user/logout")
    @Operation(summary = "退出")
    public Result<String> logout() {
        return Result.success();
    }


}
