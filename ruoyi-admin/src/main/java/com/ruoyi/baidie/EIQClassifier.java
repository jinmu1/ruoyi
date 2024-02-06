package com.ruoyi.baidie;

import com.ruoyi.data.eiq.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * EIQ处理类：将EIQ分析的各项统计指标进行转换
 */
public class EIQClassifier {

    /**
     * 将导入的数据转换为EN综合分析数据统计表(data3)
     * 1.首先按照订单编码分组
     * 2.按照订单行数统计赋值
     *
     * @param eiqBasicTableList 导入的基本数据表
     * @return EN综合分析数据统计表
     */
    public static List<ENAnalysisTable> getENAnalysisTable(List<EIQBasicTable> eiqBasicTableList) {
        // Step 1: 首先将数据按订单编码重组
        final Map<String, List<EIQBasicTable>> dataGroupByOrderNumber =
                eiqBasicTableList.stream().collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));

        // Step 2: 统计每个订单编码的行数，然后降序排序
        final List<ENAnalysisTable> eNAnalysisTableSortedByOrderNumber =
                dataGroupByOrderNumber.entrySet().stream()
                        .map(entry -> calculateOrderInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(ENAnalysisTable::getOrderLineCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        // Step 3: 设置一个自增的序号
        IntStream.range(0, eNAnalysisTableSortedByOrderNumber.size())
                .forEach(i -> eNAnalysisTableSortedByOrderNumber.get(i).setCumulativeItemNumber(i + 1));

        return eNAnalysisTableSortedByOrderNumber;
    }

    /**
     * EN分析--以订单编码作为统计数据的分类
     *
     * @param key 订单编码
     * @param eiqBasicTables 导入的基本数据类
     * @return 返回处理过的EN分析的值
     */
    private static ENAnalysisTable calculateOrderInfo(String key, List<EIQBasicTable> eiqBasicTables) {
        ENAnalysisTable enAnalysisTable = new ENAnalysisTable();
        enAnalysisTable.setOrderNumber(key);
        enAnalysisTable.setOrderLineCount(eiqBasicTables.size());
        return enAnalysisTable;
    }
}
