package com.ruoyi.baidie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.data.eiq.*;
import com.ruoyi.web.controller.utils.BaidieUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/****
 * EIQ 处理类：将EIQ分析的各项统计指标进行转换
 */
public class EIQCalculation {
    // SimpleDateFormat 用于格式化日期
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // 处理数据并生成各种分析数据统计表
    public JSONObject getDate1(List<EIQBasicTable> data1Entries) throws JsonProcessingException {
        // 生成各种分析数据统计表
        List<EIQAnalysisTable> eiqAnalysisTableList = getEIQAnalysisTable(data1Entries);// 获取EIQ单项要素分析数据统计表
        List<ENAnalysisTable> enAnalysisTableList = getENAnalysisTable(data1Entries);// 获取EN综合分析数据统计表
        List<EQAnalysisTable> eqAnalysisTableList = getEQAnalysisTable(data1Entries);// 获取EQ综合分析数据统计表
        List<EIAnalysisTable> eiAnalysisTableList = getEIAnalysisTable(data1Entries);// 获取EI综合分析数据统计表
        List<IKAnalysisTable> ikAnalysisTableList = getIKAnalysisTable(data1Entries);// 获取IK综合分析数据统计表
        return BaidieUtils.generateResponseJson(new HashMap<String, List<?>>(){{
            put("data1", data1Entries);
            put("data2", eiqAnalysisTableList);
            put("data3", enAnalysisTableList);
            put("data4", eqAnalysisTableList);
            put("data5", eiAnalysisTableList);
            put("data6", ikAnalysisTableList);
        }});
    }
    /***
     * 将导入的数据转换为EIQ单项要素分析数据统计表(data2)
     * @param data1Entries
     * @return
     */
    private   List<EIQAnalysisTable> getEIQAnalysisTable(List<EIQBasicTable> data1Entries){
        // 将 data1Entries 按照 DeliveryDate 属性进行分组，生成一个以 Date 为键，EIQBasicTable 列表为值的 Map
        Map<Date, List<EIQBasicTable>> categorizedMap = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getDeliveryDate));
        // 创建一个用于存储 EIQAnalysisTable 实例的列表
        List<EIQAnalysisTable> eIQAnalysisTables = new ArrayList<>();
        // 遍历 categorizedMap 中的每个键
        for(Date key : categorizedMap.keySet()){
            // 创建一个新的 EIQAnalysisTable 实例
            EIQAnalysisTable eIQAnalysisTable = new EIQAnalysisTable();
            // 设置 EIQAnalysisTable 的日期属性为 key 的格式化字符串
            eIQAnalysisTable.setDate(sdf.format(key));
            // 获取当前日期对应的 EIQBasicTable 列表
            List<EIQBasicTable> EIQBasicTables = categorizedMap.get(key);
            // 将当前日期的 EIQBasicTables 列表按照 OrderNumber 属性进行分组，生成一个以 OrderNumber 为键，EIQBasicTable 列表为值的 Map
            Map<String, List<EIQBasicTable>> collect = EIQBasicTables.stream()
                    .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
            // 设置 EIQAnalysisTable 的 EAnalysis 属性为订单数量的统计结果
            eIQAnalysisTable.setEAnalysis(collect.size());
            // 设置 EIQAnalysisTable 的 NAnalysis 属性为当前日期的 EIQBasicTable 列表大小
            eIQAnalysisTable.setNAnalysis(EIQBasicTables.size());
            // 初始化总出库量为0
            double sum = 0;
            // 遍历当前日期的 EIQBasicTables 列表，累加出库量
            for (EIQBasicTable dataItem : EIQBasicTables) {
                sum += dataItem.getDeliveryQuantity();
            }
            // 设置 EIQAnalysisTable 的 QAnalysis 属性为总出库量
            eIQAnalysisTable.setQAnalysis(sum);
            // 将当前日期的 EIQAnalysisTable 实例添加到 EIQAnalysisTables 列表中
            eIQAnalysisTables.add(eIQAnalysisTable);
        }
        // 按照 EAnalysis 属性的降序排列 EIQAnalysisTables 列表
        Collections.sort(eIQAnalysisTables, (m1, m2) -> Double.compare(m2.getEAnalysis(), m1.getEAnalysis()));
        return eIQAnalysisTables;

    }
    /***
     * 将导入的数据转换为EN综合分析数据统计表(data3)
     * @param data1Entries
     * @return
     */
    private   List<ENAnalysisTable> getENAnalysisTable(List<EIQBasicTable> data1Entries){
        // 将 data1Entries 按照 OrderNumber 属性进行分组，生成一个以 OrderNumber 为键，EIQBasicTable 列表为值的 Map
        Map<String, List<EIQBasicTable>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
        // 创建一个用于存储 ENAnalysisTable 实例的列表
        List<ENAnalysisTable> ENAnalysisTables = new ArrayList<>();
        // 遍历 categorizedMap1 中的每个键
        for(String key : categorizedMap1.keySet()){
            // 创建一个新的 ENAnalysisTable 实例
            ENAnalysisTable ENAnalysisTable = new ENAnalysisTable();
            // 设置 ENAnalysisTable 的 OrderNumber 属性为当前键
            ENAnalysisTable.setOrderNumber(key);
            // 设置 ENAnalysisTable 的 OrderLineCount 属性为当前键对应的 EIQBasicTable 列表的大小
            ENAnalysisTable.setOrderLineCount(categorizedMap1.get(key).size());
            // 将当前 ENAnalysisTable 实例添加到 ENAnalysisTables 列表中
            ENAnalysisTables.add(ENAnalysisTable);
        }
        // 按照 OrderLineCount 属性的降序排列 ENAnalysisTables 列表
        Collections.sort(ENAnalysisTables, (m1, m2) -> Double.compare(m2.getOrderLineCount(), m1.getOrderLineCount()));
        // 初始化计数器为0
        int k1 = 0;
        // 遍历 ENAnalysisTables 列表中的每个 ENAnalysisTable 实例
        for( ENAnalysisTable ENAnalysisTable : ENAnalysisTables) {
            // 计数器自增
            k1++;
            // 设置当前 ENAnalysisTable 实例的 CumulativeItemNumber 属性为计数器的值
            ENAnalysisTable.setCumulativeItemNumber(k1);
        }
        return ENAnalysisTables;

    }
    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data4)
     * @param data1Entries
     * @return
     */
    private   List<EQAnalysisTable> getEQAnalysisTable(List<EIQBasicTable> data1Entries){
        // 将 data1Entries 按照 OrderNumber 属性进行分组，生成一个以 OrderNumber 为键，EIQBasicTable 列表为值的 Map
        Map<String, List<EIQBasicTable>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
        // 创建一个用于存储 EQAnalysisTable 实例的列表
        List<EQAnalysisTable> EQAnalysisTables = new ArrayList<>();
        // 遍历 categorizedMap1 中的每个键
        for(String key : categorizedMap1.keySet()){
            // 创建一个新的 EQAnalysisTable 实例
            EQAnalysisTable EQAnalysisTable = new EQAnalysisTable();
            // 设置 EQAnalysisTable 的 OrderNumber 属性为当前键
            EQAnalysisTable.setOrderNumber(key);
            // 初始化总出库数量为0
            double sum = 0;
            // 遍历当前键对应的 EIQBasicTable 列表
            for (EIQBasicTable dataItem : categorizedMap1.get(key)) {
                // 累加出库数量
                sum += dataItem.getDeliveryQuantity();
            }
            // 设置 EQAnalysisTable 的 TotalDeliveredQuantity 属性为累加的出库数量
            EQAnalysisTable.setTotalDeliveredQuantity(sum);
            // 将当前 EQAnalysisTable 实例添加到 EQAnalysisTables 列表中
            EQAnalysisTables.add(EQAnalysisTable);
        }
        // 按照 TotalDeliveredQuantity 属性的降序排列 EQAnalysisTables 列表
        Collections.sort(EQAnalysisTables, (m1, m2) -> Double.compare(m2.getTotalDeliveredQuantity(), m1.getTotalDeliveredQuantity()));
        // 初始化计数器为0
        int k1 = 0;
        // 遍历 EQAnalysisTables 列表中的每个 EQAnalysisTable 实例
        for( EQAnalysisTable EQAnalysisTable : EQAnalysisTables) {
            // 计数器自增
            k1++;
            // 设置当前 EQAnalysisTable 实例的 CumulativeItemNumber 属性为计数器的值
            EQAnalysisTable.setCumulativeItemNumber(k1);
        }
        return EQAnalysisTables;
    }
    /***
     * 将导入的数据转换为EI综合分析数据统计表(data5)
     * @param data1Entries
     * @return
     */
    private   List<EIAnalysisTable> getEIAnalysisTable(List<EIQBasicTable> data1Entries){
        // 将 data1Entries 按照 OrderNumber 属性进行分组，生成一个以 OrderNumber 为键，EIQBasicTable 列表为值的 Map
        Map<String, List<EIQBasicTable>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
        // 创建一个用于存储 EIAnalysisTable 实例的列表
        List<EIAnalysisTable> EIAnalysisTables = new ArrayList<>();
        // 遍历 categorizedMap1 中的每个键
        for(String key : categorizedMap1.keySet()){
            // 创建一个新的 EIAnalysisTable 实例
            EIAnalysisTable EIAnalysisTable = new EIAnalysisTable();
            // 设置 EIAnalysisTable 的 OrderNumber 属性为当前键
            EIAnalysisTable.setOrderNumber(key);
            // 设置 EIAnalysisTable 的 MaterialVarietiesCount 属性为当前键对应的 EIQBasicTable 列表的大小
            EIAnalysisTable.setMaterialVarietiesCount(categorizedMap1.get(key).size());
            // 将当前 EIAnalysisTable 实例添加到 EIAnalysisTables 列表中
            EIAnalysisTables.add(EIAnalysisTable);
        }
        // 按照 MaterialVarietiesCount 属性的降序排列 EIAnalysisTables 列表
        Collections.sort(EIAnalysisTables, (m1, m2) -> Double.compare(m2.getMaterialVarietiesCount(), m1.getMaterialVarietiesCount()));
        // 初始化计数器为0
        int k1 = 0;
        // 遍历 EIAnalysisTables 列表中的每个 EIAnalysisTable 实例
        for( EIAnalysisTable EIAnalysisTable : EIAnalysisTables) {
            // 计数器自增
            k1++;
            // 设置当前 EIAnalysisTable 实例的 CumulativeItemNumber 属性为计数器的值
            EIAnalysisTable.setCumulativeItemNumber(k1);
        }
        return EIAnalysisTables;
    }
    /***
     * 将导入的数据转换为IK综合分析数据统计表(data6)
     * @param data1Entries
     * @return
     */
    private   List<IKAnalysisTable> getIKAnalysisTable(List<EIQBasicTable> data1Entries){
        // 创建一个空的 IKAnalysisTable 列表
        List<IKAnalysisTable> IKAnalysisTables = new ArrayList<>();
        // 将 data1Entries 按照 MaterialNumber 属性进行分组，生成一个以 MaterialNumber 为键，EIQBasicTable 列表为值的 Map
        Map<String, List<EIQBasicTable>> categorizedMap2 = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getMaterialNumber));
        // 遍历 categorizedMap2 中的每个键
        for(String key : categorizedMap2.keySet()){
            // 创建一个新的 IKAnalysisTable 实例
            IKAnalysisTable IKAnalysisTable = new IKAnalysisTable();
            // 设置 IKAnalysisTable 的 MaterialCode 属性为当前键
            IKAnalysisTable.setMaterialCode(key);
            // 设置 IKAnalysisTable 的 MaterialName 属性为当前键对应的第一个 EIQBasicTable 实例的 MaterialName 属性
            IKAnalysisTable.setMaterialName(categorizedMap2.get(key).get(0).getMaterialName());
            // 设置 IKAnalysisTable 的 OccurrenceCount 属性为当前键对应的 EIQBasicTable 列表的大小
            IKAnalysisTable.setOccurrenceCount(categorizedMap2.get(key).size());
            // 将当前 IKAnalysisTable 实例添加到 IKAnalysisTables 列表中
            IKAnalysisTables.add(IKAnalysisTable);
        }
        // 按照 OccurrenceCount 属性的降序排列 IKAnalysisTables 列表
        Collections.sort(IKAnalysisTables, (m1, m2) -> Double.compare(m2.getOccurrenceCount(), m1.getOccurrenceCount()));
        // 初始化计数器为0
        int k1 = 0;
        // 遍历 IKAnalysisTables 列表中的每个 IKAnalysisTable 实例
        for( IKAnalysisTable IKAnalysisTable : IKAnalysisTables) {
            // 计数器自增
            k1++;
            // 设置当前 IKAnalysisTable 实例的 CumulativeItemNumber 属性为计数器的值
            IKAnalysisTable.setCumulativeItemNumber(k1);
        }
        // 返回生成的 IKAnalysisTables 列表
        return IKAnalysisTables;
    }
}
