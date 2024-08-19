package com.artli.springbootinit.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.artli.springbootinit.common.BaseResponse;
import com.artli.springbootinit.common.ErrorCode;
import com.artli.springbootinit.common.ResultUtils;
import com.artli.springbootinit.exception.BusinessException;
import com.artli.springbootinit.exception.ThrowUtils;
import com.artli.springbootinit.manager.AiManager;
import com.artli.springbootinit.manager.GuavaLimiterUtils;
import com.artli.springbootinit.manager.RedisLimitManager;
import com.artli.springbootinit.mapper.ChartMapper;
import com.artli.springbootinit.model.dto.chart.ChartgetRequest;
import com.artli.springbootinit.model.entity.Chart;
import com.artli.springbootinit.model.entity.User;
import com.artli.springbootinit.model.vo.BiResponse;
import com.artli.springbootinit.service.ChartService;
import com.artli.springbootinit.service.UserService;
import com.artli.springbootinit.utils.ExeclUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.google.common.util.concurrent.RateLimiter;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
* @author XIAO ming
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-08-12 09:24:56
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {

    @Resource
    private AiManager aiManager;
    @Resource
    private UserService userService;
    private static final Long AI_ID = 1823539496730746882L;
    @Resource
    private RedisLimitManager redisLimitManager;
    @Override
    public BaseResponse<BiResponse> intelGetByAi(MultipartFile multipartFile, ChartgetRequest chartgetRequest, HttpServletRequest request) {
                String goal = chartgetRequest.getGoal();
        String name = chartgetRequest.getName();
        String chartType = chartgetRequest.getChartType();
        //校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR,"目标为空！");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() >100 ,ErrorCode.PARAMS_ERROR,"字符长度不能大于100！");

        /**
         * 文件校验
         */
//        获取用户的文件

//        取到原始文件大小
        long size = multipartFile.getSize();
//取到原始文件名
        String originalFilename = multipartFile.getOriginalFilename();
        /**
         * 校验文件大小
         *
         * 定义1MB常量
         * 一兆 = 1024 * 1024L
         *
         */
        Long biz = 1024 * 1024L;
        //如果文件大小，大于一兆，抛出异常，并作提示
        ThrowUtils.throwIf ( size > biz ,ErrorCode.PARAMS_ERROR,"上传文件大小不能大于1MB!");

        /**
         * 校验文件后缀，取到 .png 点后面的内容
         * 利用FileUtil工具类的getSuffix方法获取文件后缀名（例：a.png ,得到的是png）
         */
        String suffix = FileUtil.getSuffix(originalFilename);

        //定义合法的后缀列表
        final List<String> list = Arrays.asList("xlsx","xls");

        //如果不在列表内，则抛出异常
        if (list.contains(suffix)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"文件格式非法！");
        }


        //拼接用户输入
        StringBuilder userInput = new StringBuilder();

        userInput.append("分析需求:").append("\n");
        //拼接分析目标
        if (StrUtil.isNotBlank(chartType)) {
            goal+= goal + ",请使用" + chartType;
        }
        userInput.append(goal).append("\n");
        userInput.append("原始数据").append("\n");
        //拼接分析数据
        String toCsv = ExeclUtils.excelToCsv(multipartFile);
        userInput.append(toCsv).append("\n");
        String result = aiManager.doChat(AI_ID, userInput.toString());

        String[] split = result.split("【【【【【");

        if (split.length < 3) {
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"AI生成错误");
        }
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        //限流操作：
        redisLimitManager.doRateLimit("AiChart" + loginUser.getId());

        String genChart = split[1].trim();
        String genResult = split[2].trim();
        Chart chart = new Chart();
        chart.setGoal(goal);
        chart.setName(name);
        chart.setChartData(toCsv);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());

        boolean save = save(chart);
        ThrowUtils.throwIf(!save,ErrorCode.SYSTEM_ERROR,"图表保存失败");
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(chart.getGenChart());
        biResponse.setGenResult(chart.getGenResult());
        biResponse.setChartId(chart.getId());

        return ResultUtils.success(biResponse);
    }
}




