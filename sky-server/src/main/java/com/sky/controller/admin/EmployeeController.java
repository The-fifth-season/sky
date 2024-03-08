package com.sky.controller.admin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.CacheNameConstant;
import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.entity.EmployeePageResult;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@Api(tags = "员工相关接口")
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
        claims.put(JwtClaimsConstant.EMPNAME, employee.getName());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)                   //校验成功为前端返回token值
                .build();
        return Result.success(employeeLoginVO);
    }
    /**
     * 退出功能
     * @return  无
     */
    @PostMapping("/logout")
    @Operation(summary = "退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("增加员工")
    @CacheEvict(cacheNames = CacheNameConstant.employee,allEntries = true)
    public Result<EmployeeVO> save(@RequestBody EmployeeDTO employeeDTO){
        System.out.println("当前线程3:::"+Thread.currentThread().getId());
        EmployeeVO employeeVO = employeeService.save(employeeDTO);
        return Result.success(employeeVO);
    }
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    @Cacheable(cacheNames = CacheNameConstant.employeePage , key = "#employeePageQueryDTO.page")
    public Result<EmployeePageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询。参数为：{}",employeePageQueryDTO);
        Page<Employee> page1 = employeeService.pageQuery(employeePageQueryDTO);
        long total = page1.getTotal();                  //按要求筛选后的分页数据总数
        List<Employee> records =  page1.getRecords();
        //向前端传送密码时，加密，确保安全性
        for (Employee record : records) {
            record.setPassword("密码******");
        }
        //records.forEach(record->record.setPassword(""));
        records.forEach(System.out::println);
        long pages = page1.getPages();                  //查询的数据的页数————多少页
        System.out.println(pages);
        int size = records.size();                      //查询的数据的每页数量
        EmployeePageResult employeePageResult = new EmployeePageResult(total, records);

        System.out.println(BaseContext.getCurrentName());
        System.out.println(BaseContext.getCurrentId());
        return Result.success(employeePageResult);

    }

    @PostMapping("/status/{status}")
    @ApiOperation("更改员工状态")
    @CacheEvict(cacheNames = CacheNameConstant.employee,allEntries = true)
    public Result<String> status(@PathVariable String status , String id){
        employeeService.status(status,id);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改员工信息")
    @CacheEvict(cacheNames = CacheNameConstant.employee , allEntries = true)
    public Result<Employee> modify(@RequestBody EmployeeDTO employeeDTO){
        Employee employee = employeeService.modify(employeeDTO);
        return Result.success(employee);
    }

    /**
     *根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    @Cacheable(cacheNames = CacheNameConstant.employeeById , key = "#id" ,unless = "#result.code!=1")
    public Result<Employee> query(@PathVariable Long id){
        Employee byId = employeeService.getById(id);
        if (byId==null){
            return Result.error("查询错误");
        }
        byId.setPassword("*********");
        return Result.success(byId);
    }
}
