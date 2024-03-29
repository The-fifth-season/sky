package com.sky.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("employee")
public class EmployeeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 265603589514106109L;
    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @ApiModelProperty(value = "姓名",required = true)
    private String name;
    @ApiModelProperty(value = "电话",required = true)
    private String phone;
    @ApiModelProperty(value = "性别0/1",required = true)
    private String sex;
    @ApiModelProperty(value = "身份证",required = true)
    private String idNumber;

}
