package com.ruoyi.baidie;

import com.ruoyi.data.abc.*;
import com.ruoyi.web.controller.utils.BaidieUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 这个类有所有ABC分析的计算逻辑。
 */
public class ABCClassifier {
    /**
     * 以平均库存占用资金为主要分类依据的物料ABC分类
     * @param materialBasicInfo 输入的表单
     * @return 基于占用资金做的ABC分析表单结果List。该List是根据物料的平均资金占用额的由大到小的顺序排序过的。
     */
    public static List<MaterialValueInfo> sortByAccumulativeValue(
            List<MaterialBasicInfo> materialBasicInfo) {
        // step1：首先计算总销售金额
        final List<MaterialValueInfo> entryWithTotalValue =
                materialBasicInfo
                        .stream()
                        .map(ABCClassifier::calculateTotalValue)
                        .collect(Collectors.toList());

        // step2：对总销售金额进行降序排序
        final List<MaterialValueInfo> entryWithTotalValueSorted =
                entryWithTotalValue
                        .stream()
                        // 因为降序，所以将m1和m2的顺序在Double.compare里颠倒。
                        .sorted((m1, m2) ->
                                Double.compare(m2.getAverageFundsOccupied(), m1.getAverageFundsOccupied()))
                        .collect(Collectors.toList());

        // step3：计算总库存资金金额.
        final double totalInventoryValue = entryWithTotalValueSorted.stream()
                .map(MaterialValueInfo::getAverageFundsOccupied)
                .mapToDouble(Double::doubleValue) // convert to DoubleStream to use .sum() method
                .sum();

        // step4: 计算在降序顺序下的每一行在当前行的累计资金和累计资金占总资金百分比。
        // 这里因为不是stateless的运算，所以不能用stream operation.
        double currentAccumulatedValue = 0;  // 当前累计资金金额。
        int currentIndex = 0;
        final int numEntries = entryWithTotalValueSorted.size();
        for (MaterialValueInfo materialValueInfo : entryWithTotalValueSorted) {
            // 计算当前累计资金金额和累计资金占总金额比例。
            currentAccumulatedValue = currentAccumulatedValue + materialValueInfo.getAverageFundsOccupied();
            materialValueInfo.setCumulativeAverageFundsOccupied(currentAccumulatedValue);
            materialValueInfo.setCumulativeAverageFundsOccupiedPercentage(
                    BaidieUtils.toPercentageOfString(currentAccumulatedValue, totalInventoryValue));

            // 计算当前的物料累计品目数， 和物料累计品目数占总物料种类数的百分比。
            final int oneBasedIndex = currentIndex + 1;
            materialValueInfo.setCumulativeItemNumber(oneBasedIndex);  // convert from 0 based index to 1 based
            materialValueInfo.setCumulativeItemNumberPercentage(
                    BaidieUtils.toPercentageOfString(oneBasedIndex, numEntries));
            currentIndex++;
        }

        return entryWithTotalValueSorted;
    }

    /**
     * 以出库频次为主要分类依据的物料ABC分类
     * @param orderMaterialInfoEntries 输入的表单
     * @return 基于出库频次做的ABC分析表单结果List。该List是根据物料的出库频次的由大到小的顺序排序过的。
     */
    public static List<MaterialOutboundFrequencyInfo> sortByOutboundFrequency(
            List<OrderMaterialInfo> orderMaterialInfoEntries) {
        // 首先将数据按物料编码重组。
        final Map<String, List<OrderMaterialInfo>> orderInfoGroupByMaterialNumber =
                orderMaterialInfoEntries.stream().collect(
                        Collectors.groupingBy(OrderMaterialInfo::getMaterialNumber));

        // 统计每种物料的出库频次然后降序排序
        final List<MaterialOutboundFrequencyInfo> data3EntriesSortedByOutboundFrequency =
                orderInfoGroupByMaterialNumber.entrySet().stream()
                        .map(entry -> calculateOutboundFrequency(entry.getKey(), entry.getValue()))
                        .sorted((m1, m2) -> Double.compare(m2.getOutboundFrequency(), m1.getOutboundFrequency()))
                        .collect(Collectors.toList());

        // 计算累计出库频次。
        final int totalNumMaterial = data3EntriesSortedByOutboundFrequency.size();  //总物料数量
        // 总出库频次
        final int totalOutboundFrequency = data3EntriesSortedByOutboundFrequency
                .stream()
                .map(MaterialOutboundFrequencyInfo::getOutboundFrequency)
                .mapToInt(Integer::intValue)
                .sum();

        int currentAccumulatedFrequency = 0;//累计频次
        int currentIndex = 0;
        for (MaterialOutboundFrequencyInfo materialOutboundFrequencyInfo : data3EntriesSortedByOutboundFrequency) {
            currentAccumulatedFrequency += materialOutboundFrequencyInfo.getOutboundFrequency();
            materialOutboundFrequencyInfo.setCumulativeOutboundFrequency(currentAccumulatedFrequency);
            materialOutboundFrequencyInfo.setCumulativeOutboundFrequencyPercentage(
                    BaidieUtils.toPercentageOfString(currentAccumulatedFrequency, totalOutboundFrequency));

            // 计算当前的物料累计品目数， 和物料累计品目数占总物料种类数的百分比。
            final int oneBasedIndex = currentIndex + 1;
            materialOutboundFrequencyInfo.setCumulativeItemCount(oneBasedIndex);
            materialOutboundFrequencyInfo.setCumulativeItemCountPercentage(
                    BaidieUtils.toPercentageOfString(oneBasedIndex, totalNumMaterial));
            currentIndex++;
        }

        return data3EntriesSortedByOutboundFrequency;
    }

    /**
     * 以出库量为主要分类依据的物料ABC分类
     * @param orderMaterialInfoEntries 输入的表单
     * @return 基于出库量做的ABC分析表单结果List。该List是根据物料的出库量的由大到小的顺序排序过的。
     */
    public static List<MaterialOutboundQuantityInfo> sortByMaterialOutboundQuantity(
            List<OrderMaterialInfo> orderMaterialInfoEntries) {
        // 将订单行按照物料编码属性分类
        Map<String, List<OrderMaterialInfo>> orderInfoGroupByMaterialNumber =
                orderMaterialInfoEntries.stream()
                        .collect(Collectors.groupingBy(OrderMaterialInfo::getMaterialNumber));

        // 统计每种物料的出库量然后降序排序
        final List<MaterialOutboundQuantityInfo> outboundQuantityEntriesSorted =
                orderInfoGroupByMaterialNumber.entrySet().stream()
                        .map(entry -> getMaterialOutboundQuantityEntry(entry.getValue()))
                        .sorted(Comparator.comparingDouble(MaterialOutboundQuantityInfo::getOutboundQuantity)
                                .reversed())
                        .collect(Collectors.toList());

        // 计算
        // 累计出库量 和 累计出库量百分比
        // 物料累计品目数 和 物料累计品目数百分比
        final int numOfMaterials = outboundQuantityEntriesSorted.size();//总物料数量
        final double totalOutboundQuantity = outboundQuantityEntriesSorted.stream()
                .mapToDouble(MaterialOutboundQuantityInfo::getOutboundQuantity)
                .sum();

        double cumulativeOutboundQuantity = 0.0;
        int oneBasedIndex = 1;
        for (MaterialOutboundQuantityInfo outboundInfoEntry : outboundQuantityEntriesSorted) {
            cumulativeOutboundQuantity += outboundInfoEntry.getOutboundQuantity();
            outboundInfoEntry.setCumulativeOutboundQuantity(cumulativeOutboundQuantity);
            outboundInfoEntry.setCumulativeOutboundQuantityPercentage(BaidieUtils.toPercentageOfString(
                    cumulativeOutboundQuantity, totalOutboundQuantity));

            outboundInfoEntry.setCumulativeItemCount(oneBasedIndex);
            outboundInfoEntry.setCumulativeItemCountPercentage(BaidieUtils.toPercentageOfString(
                    oneBasedIndex, numOfMaterials));
            oneBasedIndex++;
        }

        return outboundQuantityEntriesSorted;
    }

    // 由原始数据计算总库存的价值
    // 库存 * 单价
    // 其余信息复制。
    private static MaterialValueInfo calculateTotalValue(
            MaterialBasicInfo materialBasicInfo) {
        MaterialValueInfo entryWithTotalValue = new MaterialValueInfo();
        entryWithTotalValue.setMaterialCode(materialBasicInfo.getMaterialCode());
        entryWithTotalValue.setUnitPrice(materialBasicInfo.getSellingPrice());
        entryWithTotalValue.setAverageInventory(materialBasicInfo.getAverageInventory());
        entryWithTotalValue.setAverageFundsOccupied(
                materialBasicInfo.getSellingPrice() * materialBasicInfo.getAverageInventory());

        return entryWithTotalValue;
    }

    private static MaterialOutboundFrequencyInfo calculateOutboundFrequency(
            String materialCode, List<OrderMaterialInfo> data5Entries) {
        MaterialOutboundFrequencyInfo materialOutboundFrequencyInfo = new MaterialOutboundFrequencyInfo();
        materialOutboundFrequencyInfo.setMaterialCode(materialCode);
        materialOutboundFrequencyInfo.setOutboundFrequency(data5Entries.size());
        return materialOutboundFrequencyInfo;
    }

    private static MaterialOutboundQuantityInfo getMaterialOutboundQuantityEntry(
            List<OrderMaterialInfo> orderMaterialInfoEntries) {
        final OrderMaterialInfo firstOrderMaterialInfoEntry = orderMaterialInfoEntries.get(0);
        MaterialOutboundQuantityInfo materialOutboundQuantityEntry = new MaterialOutboundQuantityInfo();
        materialOutboundQuantityEntry.setMaterialCode(firstOrderMaterialInfoEntry.getMaterialNumber());
        materialOutboundQuantityEntry.setMaterialDescription(firstOrderMaterialInfoEntry.getMaterialName());
        // 计算总出库量
        materialOutboundQuantityEntry.setOutboundQuantity(
                orderMaterialInfoEntries.stream()
                        .mapToDouble(OrderMaterialInfo::getShippedQuantity)
                        .sum()
        );

        return materialOutboundQuantityEntry;
    }
}
