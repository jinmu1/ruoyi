package com.ruoyi.baidie;


import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.data.eiq.*;

import java.util.*;
import java.util.stream.Collectors;


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
        List<EIQBasicTable> standardizedList = eiqBasicTableList.stream()
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
                        .map(entry -> calculateOutboundInfo(entry.getKey(), entry.getValue()))
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
    private static EIQAnalysisTable calculateOutboundInfo(Date key,
                                                          List<EIQBasicTable> eiqBasicTablesGroupByDate) {
        EIQAnalysisTable eiqAnalysisTable = new EIQAnalysisTable();
        Map<String, List<EIQBasicTable>> collect = eiqBasicTablesGroupByDate.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
        eiqAnalysisTable.setDate(key);
        eiqAnalysisTable.setEAnalysis(collect.size());
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
    private static Date normalizeDate(Date deliveryDate){
        return DateUtils.truncate(deliveryDate, Calendar.DATE);
    }

}
