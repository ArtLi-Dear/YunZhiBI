package com.artli.springbootinit.service.impl;

import com.artli.springbootinit.mapper.ChartMapper;
import com.artli.springbootinit.model.entity.Chart;
import com.artli.springbootinit.service.ChartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
* @author XIAO ming
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-08-12 09:24:56
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {

}




