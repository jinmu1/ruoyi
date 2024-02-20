package com.ruoyi.baidie;


import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.data.common.ObjectMap;
import com.ruoyi.data.eiq.*;
import com.ruoyi.web.controller.utils.BaidieUtils;

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
     * @param eiqBasicInfoList 初始化的数据表
     * @return 返回的是包含EIQ单项要素分析的数据（data2）
     */
    public static List<EIQAnalysisInfo> getEIQAnalysisTable(List<EIQBasicInfo> eiqBasicInfoList) {
        //step1: 将日期标准化，防止同一天出现两个值
        final List<EIQBasicInfo> standardizedList = eiqBasicInfoList.stream()
                .map(basicTable -> {
                    EIQBasicInfo newBasicTable = new EIQBasicInfo();
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
        final Map<Date, List<EIQBasicInfo>> dataGroupByDeliveryDate =
                standardizedList.stream().collect(
                        Collectors.groupingBy(EIQBasicInfo::getDeliveryDate));
        //step3: 统计每个出库日期的数据，然后升序排序
        final List<EIQAnalysisInfo> eIQAnalysisInfoSortedByDeliveryDate =
                dataGroupByDeliveryDate.entrySet().stream()
                        .map(entry -> calculateENQ(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(EIQAnalysisInfo::getDate))
                        .collect(Collectors.toList());

        return eIQAnalysisInfoSortedByDeliveryDate;

    }

    /***
     * EIQ分析--以日期作为统计数据的分类
     * @param key  日期
     * @param eiqBasicTablesGroupByDate 导入的基本数据类
     * @return 返回处理过的EIQ分析的值
     */
    private static EIQAnalysisInfo calculateENQ(Date key,
                                                List<EIQBasicInfo> eiqBasicTablesGroupByDate) {
        EIQAnalysisInfo eiqAnalysisInfo = new EIQAnalysisInfo();
        final int numOrders =
                eiqBasicTablesGroupByDate.stream().collect(
                        Collectors.groupingBy(EIQBasicInfo::getOrderNumber)).size();
        eiqAnalysisInfo.setDate(key);
        eiqAnalysisInfo.setEAnalysis(numOrders);
        eiqAnalysisInfo.setNAnalysis(eiqBasicTablesGroupByDate.size());
        double sum = 0;
        // 遍历当前日期的 EIQBasicTables 列表，累加出库量
        for (EIQBasicInfo dataItem : eiqBasicTablesGroupByDate) {
            sum += dataItem.getDeliveryQuantity();
        }
        eiqAnalysisInfo.setQAnalysis(sum);
        return eiqAnalysisInfo;
    }

    /**
     * 将日期格式标准化 比如将2023-2-5 14:46:12 转换为 2023-2-5 00:00:00
     *
     * @param deliveryDate
     * @return
     */
    private static Date normalizeDate(Date deliveryDate) {
        return DateUtils.truncate(deliveryDate, Calendar.DATE);
    }

    /**
     * 将导入的数据转换为EN综合分析数据统计表(data3)
     * 1.首先按照订单编码分组
     * 2.按照订单行数统计赋值
     *
     * @param eiqBasicInfoList 导入的基本数据表
     * @return EN综合分析数据统计表
     */
    public static List<ENAnalysisInfo> getENAnalysisTable(List<EIQBasicInfo> eiqBasicInfoList) {
        // Step 1: 首先将数据按订单编码重组
        final Map<String, List<EIQBasicInfo>> dataGroupByOrderNumber =
                eiqBasicInfoList.stream().collect(Collectors.groupingBy(EIQBasicInfo::getOrderNumber));

        // Step 2: 统计每个订单编码的行数，然后降序排序
        final List<ENAnalysisInfo> enAnalysisInfoSortedByOrderLineCount =
                dataGroupByOrderNumber.entrySet().stream()
                        .map(entry -> calculateOrderInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(ENAnalysisInfo::getOrderLineCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        // Step 3: 设置一个自增的序号
        IntStream.range(0, enAnalysisInfoSortedByOrderLineCount.size())
                .forEach(i -> enAnalysisInfoSortedByOrderLineCount.get(i).setCumulativeItemNumber(i + 1));

        return enAnalysisInfoSortedByOrderLineCount;
    }

    /**
     * EN分析--以订单编码作为统计数据的分类
     *
     * @param key            订单编码
     * @param eiqBasicInfos 导入的基本数据类
     * @return 返回处理过的EN分析的值
     */
    private static ENAnalysisInfo calculateOrderInfo(String key, List<EIQBasicInfo> eiqBasicInfos) {
        ENAnalysisInfo enAnalysisInfo = new ENAnalysisInfo();
        enAnalysisInfo.setOrderNumber(key);
        enAnalysisInfo.setOrderLineCount(eiqBasicInfos.size());
        return enAnalysisInfo;
    }

    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data4)
     * 1.按照订单编码分组
     * 2.按照订货量进行统计
     * @param eiqBasicInfoList 导入的基础数据
     * @return 处理后的数据
     */
    public static List<EQAnalysisInfo> getEQAnalysisTable(List<EIQBasicInfo> eiqBasicInfoList) {
        //step1: 首先将数据按订单编码重组
        final Map<String, List<EIQBasicInfo>> dataGroupByOrderNumber =
                eiqBasicInfoList.stream().collect(
                        Collectors.groupingBy(EIQBasicInfo::getOrderNumber));

        //step2: 统计每个订单编码的数据，然后降序序排序
        final List<EQAnalysisInfo> eqAnalysisInfoSortedByOrderLineCount =
                dataGroupByOrderNumber.entrySet().stream()
                        .map(entry -> calculateOrderQuantityInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(EQAnalysisInfo::getTotalDeliveredQuantity, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        //step3:设置一个自增的序号
        IntStream.range(0, eqAnalysisInfoSortedByOrderLineCount.size())
                .forEach(i -> eqAnalysisInfoSortedByOrderLineCount.get(i).setCumulativeItemNumber(i + 1));
        return eqAnalysisInfoSortedByOrderLineCount;
    }

    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data5)
     *1.统计订单出库数量
     * @param eiqBasicInfos
     * @return
     */
    private static EQAnalysisInfo calculateOrderQuantityInfo(String key,
                                                             List<EIQBasicInfo> eiqBasicInfos) {
        EQAnalysisInfo eqAnalysisInfo = new EQAnalysisInfo();
        eqAnalysisInfo.setOrderNumber(key);
        eqAnalysisInfo.setTotalDeliveredQuantity(eiqBasicInfos.stream()
                .mapToDouble(EIQBasicInfo::getDeliveryQuantity)
                .sum());
        return eqAnalysisInfo;
    }

    /***
     * 将导入的数据转换为EI综合分析数据统计表
     * 1.将数据按照订单编码分组
     * 2.统计订单内物料的行数，然后降序排序
     * @param eiqBasicInfos 导入的基础数据
     * @return 处理后的数据
     */
    public static List<EIAnalysisInfo> getEIAnalysisTable(List<EIQBasicInfo> eiqBasicInfos) {
        //step1: 首先将数据按订单编码重组
        final Map<String, List<EIQBasicInfo>> dataGroupByOrderNumber =
                eiqBasicInfos.stream().collect(
                        Collectors.groupingBy(EIQBasicInfo::getOrderNumber));
        //step2: 统计每个订单编码的物料行数的数据，然后降序序排序
        final List<EIAnalysisInfo> eiAnalysisInfoSortedByMaterialNumber =
                dataGroupByOrderNumber.entrySet().stream()
                        .map(entry -> calculateEIAnalysisInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(EIAnalysisInfo::getMaterialVarietiesCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
        //step3:设置一个自增的序号
        IntStream.range(0, eiAnalysisInfoSortedByMaterialNumber.size())
                .forEach(i -> eiAnalysisInfoSortedByMaterialNumber.get(i).setCumulativeItemNumber(i + 1));
        return eiAnalysisInfoSortedByMaterialNumber;
    }

    /***
     * 将导入的数据转换为EI综合分析数据统计表
     *1.统计订单内物料的种类数
     * @param key 订单编码
     * @param eiqBasicInfos 分组后的数据表
     * @return 处理过的数据
     */
    private static EIAnalysisInfo calculateEIAnalysisInfo(String key,
                                                          List<EIQBasicInfo> eiqBasicInfos) {
        EIAnalysisInfo eiAnalysisInfo = new EIAnalysisInfo();
        eiAnalysisInfo.setOrderNumber(key);
        eiAnalysisInfo.setMaterialVarietiesCount(eiqBasicInfos.stream()
                .collect(Collectors.groupingBy(EIQBasicInfo::getMaterialNumber)).size());
        return eiAnalysisInfo;
    }

    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data6)
     * 1.将数据按照物料编码分组
     * 2.统计分组后物料编码出现的次数
     * @param eiqBasicInfos
     * @return
     */
    public static List<IKAnalysisInfo> getIKAnalysisInfoTest(List<EIQBasicInfo> eiqBasicInfos) {
        //step1: 首先将数据按物料编码重组
        final Map<String, List<EIQBasicInfo>> dataGroupByMaterialNumber =
                eiqBasicInfos.stream().collect(
                        Collectors.groupingBy(EIQBasicInfo::getMaterialNumber));

        //step2: 统计每个物料编码的物料行数的数据，然后降序序排序
        final List<IKAnalysisInfo> analysisTableSortedByMaterialNumber =
                dataGroupByMaterialNumber.entrySet().stream()
                        .map(entry -> calculateIKAnalysisInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(IKAnalysisInfo::getOccurrenceCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        //step3:设置一个自增的序号
        IntStream.range(0, analysisTableSortedByMaterialNumber.size())
                .forEach(i -> analysisTableSortedByMaterialNumber.get(i).setCumulativeItemNumber(i + 1));
        // 返回生成的 IKAnalysisTables 列表
        return analysisTableSortedByMaterialNumber;
    }

    /***
     * 处理后的基本数据
     * 将按照物料编码处理过的数据，统计物料出现的次数---行数
     * @param key 物料编码
     * @param eiqBasicInfos 分组后的数据
     * @return 处理过的数据
     */
    private static IKAnalysisInfo calculateIKAnalysisInfo(String key,
                                                          List<EIQBasicInfo> eiqBasicInfos) {
        IKAnalysisInfo ikAnalysisEntry = new IKAnalysisInfo();
        ikAnalysisEntry.setMaterialCode(key);
        ikAnalysisEntry.setMaterialName(eiqBasicInfos.get(0).getMaterialName());
        ikAnalysisEntry.setOccurrenceCount(eiqBasicInfos.size());
        return ikAnalysisEntry;
    }

    /**
     * EI分析的直方统计
     * 统计订单对应物料品种数的区间范围
     *
     * @param eiqBasicInfos EIQ基本数据
     * @return EI分析的区间和区间对应的值的数量
     */
    public static List<ObjectMap> getEIHistogram(List<EIQBasicInfo> eiqBasicInfos) {
        final List<EIAnalysisInfo> eiAnalysisInfoList =
                getEIAnalysisTable(eiqBasicInfos);
        final double[] eiCount = getMaterialVarietiesCountArray(eiAnalysisInfoList);
        return BaidieUtils.generateIntervalData(eiCount, 5);
    }

    /**
     * 将EI分析后的订单对应物料品种数放到double数组中
     *
     * @param eiAnalysisInfoList
     * @return
     */
    public static double[] getMaterialVarietiesCountArray(List<EIAnalysisInfo> eiAnalysisInfoList) {
        // 使用 Java Stream 将 materialVarietiesCount 提取到一个数组中
        double[] materialVarietiesCountArray = eiAnalysisInfoList.stream()
                .mapToInt(EIAnalysisInfo::getMaterialVarietiesCount)
                .asDoubleStream()
                .toArray();

        return materialVarietiesCountArray;
    }

    /**
     * IK分析的直方统计
     * 统计订单对应物料品种数的区间范围
     *
     * @param eiqBasicInfos EIQ基本数据
     * @return EI分析的区间和区间对应的值的数量
     */
    public static List<ObjectMap> getEQHistogram(List<EIQBasicInfo> eiqBasicInfos) {
        final List<EQAnalysisInfo> eqAnalysisInfoList =
                getEQAnalysisTable(eiqBasicInfos);
        final double[] ikCount = getMaterialOccurrenceCount(eqAnalysisInfoList);
        return BaidieUtils.generateIntervalData(ikCount, 5);
    }
    /**
     * EN分析的直方统计
     * 统计订单对应物料品种数的区间范围
     *
     * @param eiqBasicInfos EIQ基本数据
     * @return EN分析的区间和区间对应的值的数量
     */
    public static List<ObjectMap> getENHistogram(List<EIQBasicInfo> eiqBasicInfos) {
        final List<ENAnalysisInfo> enAnalysisInfoList =
                getENAnalysisTable(eiqBasicInfos);
        final double[] enCount = getOrderOccurrenceCount(enAnalysisInfoList);
        return BaidieUtils.generateIntervalData(enCount, 5);
    }

    private static double[] getOrderOccurrenceCount(List<ENAnalysisInfo> enAnalysisInfoList) {
        // 使用 Java Stream 将 OccurrenceCount 提取到一个数组中
        double[] materialVarietiesCountArray = enAnalysisInfoList.stream()
                .mapToInt(ENAnalysisInfo::getOrderLineCount)
                .asDoubleStream()
                .toArray();

        return materialVarietiesCountArray;
    }

    /**
     * 统计IK分析的出现的次数
     *
     * @param ikAnalysisInfoList
     * @return
     */
    private static double[] getMaterialOccurrenceCount(List<EQAnalysisInfo> eqAnalysisInfoList) {
        // 使用 Java Stream 将 OccurrenceCount 提取到一个数组中
        double[] materialVarietiesCountArray = eqAnalysisInfoList.stream()
                .mapToDouble(EQAnalysisInfo::getTotalDeliveredQuantity)
                .toArray();

        return materialVarietiesCountArray;
    }
}
