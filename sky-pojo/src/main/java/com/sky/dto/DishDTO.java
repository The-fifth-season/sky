package com.sky.dto;

import com.sky.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DishDTO implements Serializable {

    private long categoryId;
    private String description;
    private List<DishFlavor> flavors;
    private long id;
    private String image;
    private String name;
    private BigDecimal price;
    private Long status;

}
