package com.artli.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表格处理工具
 */
@Slf4j
public class ExeclUtils {

    public static String excelToCsv(MultipartFile multipartFile) {

        List<Map<Integer, String>> list = null;

        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
           log.error("表格处理错误",e);
        }

        if (CollUtil.isEmpty(list)) {
                return null;
        }
//转换为csv
        // 创建StringBuilder对象用于构建字符串（提高拼接效率）
        StringBuilder stringBuilder = new StringBuilder();

        // 读取表头信息
        LinkedHashMap<Integer, String> stringLinkedMap = (LinkedHashMap) list.get(0);
        // 过滤掉空的表头项，并收集到一个新列表中
        List<String> list2 = stringLinkedMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        // 将表头项用逗号拼接，并追加到stringBuilder中
        stringBuilder.append(StringUtils.join(list2,","));

        // 读取数据行信息
        // 从第二个元素开始遍历list，假设其余元素为数据行
        for (int i = 1; i < list.size(); i++) {
            // 将每个数据行转换为LinkedHashMap
            LinkedHashMap<Integer, String> map = (LinkedHashMap) list.get(i);
            // 过滤掉空的数据项，并收集到一个新列表中
            List<String> list1 = map.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            // 将数据项用逗号拼接，并追加到stringBuilder中，每行数据后追加换行符
            stringBuilder.append(StringUtils.join(list1,",")).append("\n");
        }

        // 返回构建完成的字符串
        return stringBuilder.toString();


    }


}