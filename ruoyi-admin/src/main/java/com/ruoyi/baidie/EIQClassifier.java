package com.ruoyi.baidie;

import com.ruoyi.data.eiq.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/****
 * EIQ 处理类：将EIQ分析的各项统计指标进行转换
 */
public class EIQClassifier {
    /***
     * 将导入的数据转换为EI综合分析数据统计表(data5)
     * 1.将数据按照订单编码分组
     * 2.统计订单内物料的行数，然后降序排序
     * @param eiqBasicTableList 导入的基础数据
     * @return 处理后的数据
     */
    public static List<EIAnalysisTable> getEIAnalysisTable(List<EIQBasicTable> eiqBasicTableList) {
        //step1: 首先将数据按订单编码重组
        final Map<String, List<EIQBasicTable>> dataGroupByOrderNumber =
                eiqBasicTableList.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getOrderNumber));

        //step2: 统计每个订单编码的物料行数的数据，然后降序序排序
        final List<EIAnalysisTable> analysisMaterialLineCount =
                dataGroupByOrderNumber.entrySet().stream()
                        .map(entry -> calculateOrderMaterialInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(EIAnalysisTable::getMaterialVarietiesCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        //step3:设置一个自增的序号
        IntStream.range(0, analysisMaterialLineCount.size())
                .forEach(i -> analysisMaterialLineCount.get(i).setCumulativeItemNumber(i + 1));
        return analysisMaterialLineCount;
    }

    /***
     * 将导入的数据转换为EI综合分析数据统计表
     *1.统计订单内物料的种类数
     * @param key 订单编码
     * @param eiqBasicTables 分组后的数据表
     * @return 处理过的数据
     */
    private static EIAnalysisTable calculateOrderMaterialInfo(String key,
                                                              List<EIQBasicTable> eiqBasicTables) {
        EIAnalysisTable eiAnalysisTable = new EIAnalysisTable();
        eiAnalysisTable.setOrderNumber(key);
        eiAnalysisTable.setMaterialVarietiesCount(eiqBasicTables.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getMaterialNumber)).size());
        return eiAnalysisTable;
    }

}
