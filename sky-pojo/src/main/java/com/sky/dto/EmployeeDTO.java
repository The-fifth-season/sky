package com.sky.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private String username;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

}
