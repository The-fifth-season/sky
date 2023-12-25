package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@ApiModel(description = "员工登录时传递的数据模型")
public class EmployeeLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7818048581325052429L;
    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @ApiModelProperty("密码")
    private String password;

}
