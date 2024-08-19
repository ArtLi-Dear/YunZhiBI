package com.artli.springbootinit.service;

import com.artli.springbootinit.common.BaseResponse;
import com.artli.springbootinit.model.dto.chart.ChartgetRequest;
import com.artli.springbootinit.model.entity.Chart;
import com.artli.springbootinit.model.vo.BiResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
* @author XIAO ming
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-08-12 09:24:56
*/
public interface ChartService extends IService<Chart> {

    BaseResponse<BiResponse> intelGetByAi(MultipartFile multipartFile, ChartgetRequest chartgetRequest, HttpServletRequest request);
}
