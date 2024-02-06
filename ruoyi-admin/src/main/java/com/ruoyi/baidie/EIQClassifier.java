package com.ruoyi.baidie;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.data.eiq.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/****
 * EIQ 处理类：将EIQ分析的各项统计指标进行转换
 */
public class EIQClassifier {
    /***
     * 将导入的数据转换为EIQ单项要素分析数据统计表(data2)
     * 1.首先对数据按照日期分组
     * 2.按照每天统计
     * @param eiqBasicTableList 初始化的数据表
     * @return 返回的是包含EIQ单项要素分析的数据（data2）
     */
    public static List<EIQAnalysisTable> getEIQAnalysisTable(List<EIQBasicTable> eiqBasicTableList){
        //step1: 将日期标准化，防止同一天出现两个值
        final List<EIQBasicTable> standardizedList = eiqBasicTableList.stream()
                .map(basicTable -> {
                    EIQBasicTable newBasicTable = new EIQBasicTable();
                    newBasicTable.setDeliveryDate(normalizeDate(basicTable.getDeliveryDate()));
                    newBasicTable.setMaterialNumber(basicTable.getMaterialNumber());
                    newBasicTable.setDeliveryDate(basicTable.getDeliveryDate());
                    newBasicTable.setOrderNumber(basicTable.getOrderNumber());
                    newBasicTable.setMaterialName(basicTable.getMaterialName());
                    newBasicTable.setDeliveryQuantity(basicTable.getDeliveryQuantity());
                    newBasicTable.setDeliveryUnit(basicTable.getDeliveryUnit());
                    newBasicTable.setUnitPrice(basicTable.getUnitPrice());
                    newBasicTable.setPalletizedItems(basicTable.getPalletizedItems());
                    newBasicTable.setConversionUnit(basicTable.getConversionUnit());
                    newBasicTable.setConversionUnit1(basicTable.getConversionUnit1());
                    return newBasicTable;
                })
                .collect(Collectors.toList());
        //step2: 首先将数据按出库日期重组
        final  Map<Date, List<EIQBasicTable>> dataGroupByDeliveryDate =
                standardizedList.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getDeliveryDate));
        //step3: 统计每个出库日期的数据，然后升序排序
        final List<EIQAnalysisTable> eIQAnalysisTableSortedByDeliveryDate =
                dataGroupByDeliveryDate.entrySet().stream()
                        .map(entry -> calculateENQ(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(EIQAnalysisTable::getDate))
                        .collect(Collectors.toList());

        return eIQAnalysisTableSortedByDeliveryDate;

    }
    /***
     * EIQ分析--以日期作为统计数据的分类
     * @param key  日期
     * @param eiqBasicTablesGroupByDate 导入的基本数据类
     * @return 返回处理过的EIQ分析的值
     */
    private static EIQAnalysisTable calculateENQ(Date key,
                                                          List<EIQBasicTable> eiqBasicTablesGroupByDate) {
        EIQAnalysisTable eiqAnalysisTable = new EIQAnalysisTable();
        final int numOrders =
                eiqBasicTablesGroupByDate.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getOrderNumber)).size();
        eiqAnalysisTable.setDate(key);
        eiqAnalysisTable.setEAnalysis(numOrders);
        eiqAnalysisTable.setNAnalysis(eiqBasicTablesGroupByDate.size());
        double sum = 0;
        // 遍历当前日期的 EIQBasicTables 列表，累加出库量
        for (EIQBasicTable dataItem : eiqBasicTablesGroupByDate) {
            sum += dataItem.getDeliveryQuantity();
        }
        eiqAnalysisTable.setQAnalysis(sum);
        return eiqAnalysisTable;
    }
    /**
     * 将日期格式标准化 比如将2023-2-5 14:46:12 转换为 2023-2-5 00:00:00
     * @param deliveryDate
     * @return
     */
    private static Date normalizeDate(Date deliveryDate) {
        return DateUtils.truncate(deliveryDate, Calendar.DATE);
    }
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
