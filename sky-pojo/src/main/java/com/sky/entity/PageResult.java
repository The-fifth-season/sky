package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 8888942523003174299L;
    private long total;
    private List records ;

//    public PageResult(long total, List<Employee> pageList) {
//        this.total = total;
//        this.pageList = pageList;
//    }
}
