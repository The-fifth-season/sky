package com.sky.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.entity.PageResult;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import com.sky.vo.EmployeeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@RequiredArgsConstructor
//@Tag(name = "员工相关接口")
@Api(tags = "员工相关接口1")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtProperties jwtProperties;
    /**
     * 登陆功能
     * @param employeeLoginDTO
     * @return EmployeeLoginVO
     */
    @ApiOperation("登陆")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }
    /**
     * 退出功能
     *
     * @return  无
     */
    @PostMapping("/logout") 
    @Operation(summary = "退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("增加员工")
    public Result<EmployeeVO> save(@RequestBody EmployeeDTO employeeDTO){
        EmployeeVO employeeVO = employeeService.save(employeeDTO);
        return Result.success(employeeVO);
    }

    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询。参数为：{}",employeePageQueryDTO);
        Page<Employee> page1 = employeeService.pageQuery(employeePageQueryDTO);
        List<Employee> records =  page1.getRecords();
        records.forEach(System.out::println);
        int size = records.size();
        PageResult pageResult = new PageResult(size, records);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("更改员工状态")
    public Result<String> status(@PathVariable String status , String id){
        employeeService.status(status,id);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改员工信息")
    public Result<Employee> modify(@RequestBody EmployeeDTO employeeDTO){
        Employee employee = employeeService.modify(employeeDTO);
        return Result.success(employee);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<Employee> query(@PathVariable Long id){
        Employee byId = employeeService.getById(id);
        byId.setPassword("*********");
        return Result.success(byId);
    }
}