package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -2714453793748197477L;
    private Long total;
    private List records;
}
