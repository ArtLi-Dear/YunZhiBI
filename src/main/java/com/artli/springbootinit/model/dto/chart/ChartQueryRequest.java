package com.artli.springbootinit.model.dto.chart;

import com.artli.springbootinit.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 分析目标
     */
    private String goal;



    /**
     * 图表类型
     */
    private String chartType;


    /**
     * 创建用户 id
     */
    private Long userId;



    private static final long serialVersionUID = 1L;
}