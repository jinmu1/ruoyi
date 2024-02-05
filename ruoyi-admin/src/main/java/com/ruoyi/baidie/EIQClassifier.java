package com.ruoyi.baidie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.data.eiq.*;
import com.ruoyi.web.controller.system.ABCAnalyseController;
import com.ruoyi.web.controller.utils.BaidieUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/****
 * EIQ 处理类：将EIQ分析的各项统计指标进行转换
 */
public class EIQClassifier {
    // SimpleDateFormat 用于格式化日期
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /***
     * 将导入的数据转换为EIQ单项要素分析数据统计表(data2)
     * 1.首先对数据按照日期分组
     * 2.按照每天统计
     * @param data1Entries 初始化的数据表
     * @return 返回的是包含EIQ单项要素分析的数据（data2）
     */
    public static   List<EIQAnalysisTable> getEIQAnalysisTable(List<EIQBasicTable> data1Entries){
           data1Entries.stream().forEach(obj -> obj.setDeliveryDate(standardizedDate(obj.getDeliveryDate())));
        //step1: 首先将数据按出库日期重组
        final  Map<Date, List<EIQBasicTable>> dataGroupByDeliveryDate =
                data1Entries.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getDeliveryDate));

        //step2: 统计每个出库日期的数据，然后升序排序
        final List<EIQAnalysisTable> EIQAnalysisTableSortedByDeliveryDate =
                dataGroupByDeliveryDate.entrySet().stream()
                        .map(entry -> calculateOutboundInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(EIQAnalysisTable::getDate))
                        .collect(Collectors.toList());

        return EIQAnalysisTableSortedByDeliveryDate;

    }


    /***
     * 将导入的数据转换为EN综合分析数据统计表(data3)
     * 1.首先按照订单编码分组
     * 2.按照订单行数统计赋值
     * @param data1Entries
     * @return
     */
    public   static  List<ENAnalysisTable> getENAnalysisTable(List<EIQBasicTable> data1Entries){
        //step1: 首先将数据按订单编码重组
        final  Map<String, List<EIQBasicTable>> dataGroupByOrderNumber =
                data1Entries.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getOrderNumber));

        //step2: 统计每个订单编码的行数，然后降序序排序
        final List<ENAnalysisTable> eNAnalysisTableSortedByOrderNumber =
                dataGroupByOrderNumber.entrySet().stream()
                        .map(entry -> calculateOrderInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(ENAnalysisTable::getOrderLineCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        //step3:设置一个自增的序号
        IntStream.range(0, eNAnalysisTableSortedByOrderNumber.size())
                .forEach(i -> eNAnalysisTableSortedByOrderNumber.get(i).setCumulativeItemNumber(i + 1));
        return eNAnalysisTableSortedByOrderNumber;

    }



    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data4)
     * 1.按照订单编码分组
     * 2.按照订货量进行统计
     * @param data1Entries 导入的基础数据
     * @return 处理后的数据
     */
    public static  List<EQAnalysisTable> getEQAnalysisTable(List<EIQBasicTable> data1Entries){
        //step1: 首先将数据按订单编码重组
        final  Map<String, List<EIQBasicTable>> dataGroupByOrderNumber =
                data1Entries.stream().collect(
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
     * 将导入的数据转换为EI综合分析数据统计表(data5)
     * 1.将数据按照订单编码分组
     * 2.统计订单内物料的行数，然后降序排序
     * @param data1Entries 导入的基础数据
     * @return 处理后的数据
     */
    public static  List<EIAnalysisTable> getEIAnalysisTable(List<EIQBasicTable> data1Entries){
        //step1: 首先将数据按订单编码重组
        final  Map<String, List<EIQBasicTable>> dataGroupByOrderNumber =
                data1Entries.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getOrderNumber));

        //step2: 统计每个订单编码的物料行数的数据，然后降序序排序
        final List<EIAnalysisTable> eiAnalysisTableSortedByOrderNumber =
                dataGroupByOrderNumber.entrySet().stream()
                        .map(entry -> calculateOrderMaterialInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(EIAnalysisTable::getMaterialVarietiesCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        //step3:设置一个自增的序号
        IntStream.range(0, eiAnalysisTableSortedByOrderNumber.size())
                .forEach(i -> eiAnalysisTableSortedByOrderNumber.get(i).setCumulativeItemNumber(i + 1));
        return eiAnalysisTableSortedByOrderNumber;
    }



    /***
     * 将导入的数据转换为IK综合分析数据统计表(data6)
     * 1.将数据按照物料编码分组
     * 2.统计分组后物料编码出现的次数
     * @param data1Entries
     * @return
     */
    public static  List<IKAnalysisTable> getIKAnalysisTable(List<EIQBasicTable> data1Entries){
        //step1: 首先将数据按物料编码重组
        final  Map<String, List<EIQBasicTable>> dataGroupByMaterialNumber =
                data1Entries.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getMaterialNumber));

        //step2: 统计每个物料编码的物料行数的数据，然后降序序排序
        final List<IKAnalysisTable> ikAnalysisTableSortedByMaterialNumber =
                dataGroupByMaterialNumber.entrySet().stream()
                        .map(entry -> calculateMaterialInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(IKAnalysisTable::getOccurrenceCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        //step3:设置一个自增的序号
        IntStream.range(0, ikAnalysisTableSortedByMaterialNumber.size())
                .forEach(i -> ikAnalysisTableSortedByMaterialNumber.get(i).setCumulativeItemNumber(i + 1));
        // 返回生成的 IKAnalysisTables 列表
        return ikAnalysisTableSortedByMaterialNumber;
    }


    /***
     * EIQ分析--以日期作为统计数据的分类
     * @param key  日期
     * @param eiqBasicTables 导入的基本数据类
     * @return 返回处理过的EIQ分析的值
     */
    private static EIQAnalysisTable calculateOutboundInfo(Date key, List<EIQBasicTable> eiqBasicTables) {
        EIQAnalysisTable eIQAnalysisTables = new EIQAnalysisTable();

        Map<String, List<EIQBasicTable>> collect = eiqBasicTables.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
        eIQAnalysisTables.setDate(sdf.format(key));
        eIQAnalysisTables.setEAnalysis(collect.size());
        eIQAnalysisTables.setNAnalysis(eiqBasicTables.size());
        double sum = 0;
        // 遍历当前日期的 EIQBasicTables 列表，累加出库量
        for (EIQBasicTable dataItem : eiqBasicTables) {
            sum += dataItem.getDeliveryQuantity();
        }
        eIQAnalysisTables.setQAnalysis(sum);

        return eIQAnalysisTables;
    }

    /***
     * EN分析--以订单编码作为统计数据的分类
     * @param key 订单编码
     * @param eiqBasicTables  导入的基本数据类
     * @return 返回处理过的EN分析的值
     */
    private static ENAnalysisTable calculateOrderInfo(String key, List<EIQBasicTable> eiqBasicTables) {
        ENAnalysisTable enAnalysisTable = new ENAnalysisTable();
        enAnalysisTable.setOrderNumber(key);
        enAnalysisTable.setOrderLineCount(eiqBasicTables.size());
        return  enAnalysisTable;
    }
    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data5)
     *1.统计订单出库数量
     * @param eiqBasicTables
     * @return
     */
    private static EQAnalysisTable calculateOrderQuantityInfo(String key, List<EIQBasicTable> eiqBasicTables) {
        EQAnalysisTable eqAnalysisTable = new EQAnalysisTable();
        eqAnalysisTable.setOrderNumber(key);
        eqAnalysisTable.setTotalDeliveredQuantity(eiqBasicTables.stream()
                .mapToDouble(EIQBasicTable::getDeliveryQuantity)
                .sum());
        return  eqAnalysisTable;
    }

    /***
     * 将导入的数据转换为EI综合分析数据统计表
     *1.统计订单内物料的种类数
     * @param key 订单编码
     * @param eiqBasicTables 分组后的数据表
     * @return 处理过的数据
     */
    private static EIAnalysisTable calculateOrderMaterialInfo(String key, List<EIQBasicTable> eiqBasicTables) {
        EIAnalysisTable eiAnalysisTable = new EIAnalysisTable();
        eiAnalysisTable.setOrderNumber(key);
        eiAnalysisTable.setMaterialVarietiesCount(eiqBasicTables.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getMaterialNumber)).size());
        return  eiAnalysisTable;
    }

    /***
     * 处理后的基本数据
     * 将按照物料编码处理过的数据，统计物料出现的次数---行数
     * @param key 物料编码
     * @param eiqBasicTables 分组后的数据
     * @return 处理过的数据
     */
    private static IKAnalysisTable calculateMaterialInfo(String key, List<EIQBasicTable> eiqBasicTables) {
        IKAnalysisTable ikAnalysisTable = new IKAnalysisTable();
        ikAnalysisTable.setMaterialCode(key);
        ikAnalysisTable.setMaterialName(eiqBasicTables.get(0).getMaterialName());
        ikAnalysisTable.setOccurrenceCount(eiqBasicTables.size());
        return  ikAnalysisTable;
    }
    /**
     * 将日期格式标准化 比如将2023-2-5 14:46:12 转换为 2023-2-5 00:00:00
     * @param deliveryDate
     * @return
     */
    private static Date  standardizedDate(Date deliveryDate){
        Instant instant = deliveryDate.toInstant();
        // 将 Instant 对象转换为 LocalDate 对象
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        return createDate(year, month, day);
    }
    /***
     * 将数据里面的年月日进行转换确保同一天不会出现两个数值
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 某一天的date格式
     */
    private static Date createDate(int year, int month, int day) {
        // 创建 Calendar 对象并设置为指定日期
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar 中月份从 0 开始
        calendar.set(Calendar.DAY_OF_MONTH, day);
        // 将时间部分设置为 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 获取设置后的 Date 对象
        Date date = calendar.getTime();
        return date;
    }
}
