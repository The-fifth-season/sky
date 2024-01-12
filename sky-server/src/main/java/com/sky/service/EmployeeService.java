package com.sky.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.Result;
import com.sky.vo.EmployeeVO;

public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    EmployeeVO save(EmployeeDTO employeeDTO);

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    Result<String> status(String status, String id);

    Employee modify(EmployeeDTO employeeDTO);
}
