package com.sky.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Service
@Slf4j

public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对进行md5加密，然后再进行比对
        String password2 = DigestUtils.md5Hex(password);
        System.out.println(password2);

        if (!password2.equals(DigestUtils.md5Hex(employee.getPassword()))) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //3、返回实体对象
        return employee;
    }

    //    @SneakyThrows           //用于处理异常，不用一层层的往上抛
    @Override
    @ApiOperation("新增员工2")
    public EmployeeVO save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);                         /*
        boolean exists = lambdaQuery().eq(Employee::getName, employeeDTO.getName()).exists();                   //判断数据库中，有没有重复的用户名存在
        System.out.println(exists);
        if (exists){
            throw new SQLIntegrityConstraintViolationException("用户"+employeeDTO.getName()+"已存在");           //用了@SneakyThrows注解后，就不需要catch异常再抛RuntimeException了
        }*/
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
//        employee.setPassword(DigestUtils.md5Hex(PasswordConstant.DEFAULT_PASSWORD));
        //TODO 通过令牌获取ID
        employee.setCreateUser(10L);
        employee.setUpdateUser(10L);
        employeeMapper.insert(employee);
        EmployeeVO employeeVO = new EmployeeVO();
        BeanUtils.copyProperties(employee, employeeVO);
        return employeeVO;
    }

    //分页查询
    @Override
    @Operation(summary = "分页查询")
    public Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        Page<Employee> page = Page.of(employeePageQueryDTO.getPage(),
                employeePageQueryDTO.getPageSize(),
                employeeMapper.selectCount(null));
        String name = employeePageQueryDTO.getName();

        return lambdaQuery().like(name != null, Employee::getName, name)
                //.orderByDesc(Employee::getCreateTime)             //创建时间倒序排序
                .page(page);
    }

    /**
     * 更改用户状态
     *
     * @param status
     * @param id
     * @return
     */
    @Override
    public Result<String> status(String status, String id) {
        Employee employee = employeeMapper.selectById(id);
        Integer status2 = Integer.parseInt(status);
        /*switch (status2) {
            case 0:
                status2 = StatusConstant.DISABLE;
                break;
            case 1:
                status2 = StatusConstant.ENABLE;
                break;
            default:
               return Result.error("账号状态错误");
        }*/
        employee.setStatus(status2);
        employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.updateById(employee);
        return Result.success();
    }

    @Override
    public Employee modify(@RequestParam EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.selectById(employeeDTO.getId());
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setUpdateTime(LocalDateTime.now());
        log.info("修改后员工的详细信息{}",employee);
        employeeMapper.updateById(employee);
        employee.setPassword("你猜猜看");
        return employee;
    }
}
