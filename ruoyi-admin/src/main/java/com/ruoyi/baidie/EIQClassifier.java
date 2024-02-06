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
     * 将导入的数据转换为EQ综合分析数据统计表(data4)
     * 1.按照订单编码分组
     * 2.按照订货量进行统计
     * @param eiqBasicTableList 导入的基础数据
     * @return 处理后的数据
     */
    public static List<EQAnalysisTable> getEQAnalysisTable(List<EIQBasicTable> eiqBasicTableList) {
        //step1: 首先将数据按订单编码重组
        final Map<String, List<EIQBasicTable>> dataGroupByOrderNumber =
                eiqBasicTableList.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getOrderNumber));

        //step2: 统计每个订单编码的数据，然后降序序排序
        final List<EQAnalysisTable> eQAnalysisTableSortedByOrderNumber =
                dataGroupByOrderNumber.entrySet().stream()
                        .map(entry -> calculateOrderQuantityInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(EQAnalysisTable::getTotalDeliveredQuantity, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        //step3:设置一个自增的序号
        IntStream.range(0, eQAnalysisTableSortedByOrderNumber.size())
                .forEach(i -> eQAnalysisTableSortedByOrderNumber.get(i).setCumulativeItemNumber(i + 1));
        return eQAnalysisTableSortedByOrderNumber;
    }

    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data5)
     *1.统计订单出库数量
     * @param eiqBasicTables
     * @return
     */
    private static EQAnalysisTable calculateOrderQuantityInfo(String key,
                                                              List<EIQBasicTable> eiqBasicTables) {
        EQAnalysisTable eqAnalysisTable = new EQAnalysisTable();
        eqAnalysisTable.setOrderNumber(key);
        eqAnalysisTable.setTotalDeliveredQuantity(eiqBasicTables.stream()
                .mapToDouble(EIQBasicTable::getDeliveryQuantity)
                .sum());
        return eqAnalysisTable;
    }

}
