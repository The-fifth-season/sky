package com.sky.controller.user;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.impl.EmployeeServiceImpl;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserEmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;
    @Autowired
    private JwtProperties jwtProperties ;

    @PostMapping("/user/login")
    public Result<UserLoginVO> login(EmployeeLoginDTO employeeLoginDTO) {
        Employee employee = employeeService.login(employeeLoginDTO);
        HashMap<String, Object> claim = new HashMap<>();
        claim.put("name",employee.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claim);
        UserLoginVO build = UserLoginVO.builder()
                .token(token)
                .openid("...")
                .id(1L)
                .build();
        return Result.success(build);
    }


}
