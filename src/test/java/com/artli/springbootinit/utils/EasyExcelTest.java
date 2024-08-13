package com.artli.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel 测试
 *
 */
@SpringBootTest
public class EasyExcelTest {

    @Test
    public String ExeclUtils() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:超警信息统计.xlsx");
        List<Map<Integer, String>> list = EasyExcel.read(file)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .headRowNumber(0)
                .doReadSync();

        if (CollUtil.isEmpty(list)) {
            return "";
        }
//转换为csv
        //读取表头
        LinkedMap<Integer, String> stringLinkedMap = (LinkedMap) list.get(0);

        //读取数据

        System.out.println(StringUtils.join(stringLinkedMap,","));

        LinkedMap<Integer, String> map = (LinkedMap) list.get(1);
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(StringUtils.join(entry.getValue(),","));
        }

        return "";
    }

}