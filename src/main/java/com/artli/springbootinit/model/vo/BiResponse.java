package com.artli.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BiResponse implements Serializable {
    private String genChart;
    private String genResult;
    private Long chartId;



}
