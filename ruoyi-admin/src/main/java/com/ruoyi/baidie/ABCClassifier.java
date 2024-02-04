package com.ruoyi.baidie;

import com.ruoyi.web.controller.system.ABCAnalyseController;
import com.ruoyi.web.controller.utils.BaidieUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 这个类有所有ABC分析的计算逻辑。
 */
public class ABCClassifier {
    /**
     * 以平均库存占用资金为主要分类依据的物料ABC分类
     * @param inventoryInfo 输入的表单
     * @return 基于占用资金做的ABC分析表单结果List。该List是根据物料的平均资金占用额的由大到小的顺序排序过的。
     */
    public static List<ABCAnalyseController.Data2Entry> sortByAccumulativeValue(
            List<ABCAnalyseController.Data1Entry> inventoryInfo) {
        // step1：首先计算总销售金额
        final List<ABCAnalyseController.Data2Entry> entryWithTotalValue =
                inventoryInfo
                        .stream()
                        .map(ABCClassifier::calculateTotalValue)
                        .collect(Collectors.toList());

        // step2：对总销售金额进行降序排序
        final List<ABCAnalyseController.Data2Entry> entryWithTotalValueSorted =
                entryWithTotalValue
                        .stream()
                        // 因为降序，所以将m1和m2的顺序在Double.compare里颠倒。
                        .sorted((m1, m2) ->
                                Double.compare(m2.getAverageFundsOccupied(), m1.getAverageFundsOccupied()))
                        .collect(Collectors.toList());

        // step3：计算总库存资金金额.
        final double totalInventoryValue = entryWithTotalValueSorted.stream()
                .map(ABCAnalyseController.Data2Entry::getAverageFundsOccupied)
                .mapToDouble(Double::doubleValue) // convert to DoubleStream to use .sum() method
                .sum();

        // step4: 计算在降序顺序下的每一行在当前行的累计资金和累计资金占总资金百分比。
        // 这里因为不是stateless的运算，所以不能用stream operation.
        double currentAccumulatedValue = 0;  // 当前累计资金金额。
        int currentIndex = 0;
        final int numEntries = entryWithTotalValueSorted.size();
        for (ABCAnalyseController.Data2Entry data2Entry : entryWithTotalValueSorted) {
            // 计算当前累计资金金额和累计资金占总金额比例。
            currentAccumulatedValue = currentAccumulatedValue + data2Entry.getAverageFundsOccupied();
            data2Entry.setCumulativeAverageFundsOccupied(currentAccumulatedValue);
            data2Entry.setCumulativeAverageFundsOccupiedPercentage(
                    BaidieUtils.toPercentageOfString(currentAccumulatedValue, totalInventoryValue));

            // 计算当前的物料累计品目数， 和物料累计品目数占总物料种类数的百分比。
            final int oneBasedIndex = currentIndex + 1;
            data2Entry.setCumulativeItemNumber(oneBasedIndex);  // convert from 0 based index to 1 based
            data2Entry.setCumulativeItemNumberPercentage(
                    BaidieUtils.toPercentageOfString(oneBasedIndex, numEntries));
            currentIndex++;
        }

        return entryWithTotalValueSorted;
    }

    // 由原始数据计算总库存的价值
    // 库存 * 单价
    // 其余信息复制。
    private static ABCAnalyseController.Data2Entry calculateTotalValue(
            ABCAnalyseController.Data1Entry inventoryInfo) {
        ABCAnalyseController.Data2Entry entryWithTotalValue = new ABCAnalyseController.Data2Entry();
        entryWithTotalValue.setMaterialCode(inventoryInfo.getMaterialCode());
        entryWithTotalValue.setUnitPrice(inventoryInfo.getSellingPrice());
        entryWithTotalValue.setAverageInventory(inventoryInfo.getAverageInventory());
        entryWithTotalValue.setAverageFundsOccupied(
                inventoryInfo.getSellingPrice() * inventoryInfo.getAverageInventory());

        return entryWithTotalValue;
    }
}
