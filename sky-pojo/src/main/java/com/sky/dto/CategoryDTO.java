package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 排序，按照升序排序
     */
    private long sort;
    /**
     * 分类类型：1为菜品分类，2为套餐分类
     */
    private long type;
}
