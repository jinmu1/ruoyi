package com.ruoyi.baidie;

import com.ruoyi.web.controller.system.ABCAnalyseController;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ABCClassifierTest {
    private List<ABCAnalyseController.Data1Entry> testInventoryInfoEntries;
    private List<String> accumulativeValuePercentages;

    @Before
    public void setUp() {
        // Set up some test data
        ABCAnalyseController.Data1Entry inventoryEntry1 = new ABCAnalyseController.Data1Entry();
        inventoryEntry1.setAverageInventory(5);
        inventoryEntry1.setSellingPrice(100);

        ABCAnalyseController.Data1Entry inventoryEntry2 = new ABCAnalyseController.Data1Entry();
        inventoryEntry2.setAverageInventory(1000);
        inventoryEntry2.setSellingPrice(3.6);

        ABCAnalyseController.Data1Entry inventoryEntry3 = new ABCAnalyseController.Data1Entry();
        inventoryEntry3.setAverageInventory(1);
        inventoryEntry3.setSellingPrice(4001);

        ABCAnalyseController.Data1Entry inventoryEntry4 = new ABCAnalyseController.Data1Entry();
        inventoryEntry4.setAverageInventory(100);
        inventoryEntry4.setSellingPrice(65);

        testInventoryInfoEntries =
                Arrays.asList(inventoryEntry1, inventoryEntry2, inventoryEntry3, inventoryEntry4);
        accumulativeValuePercentages = Arrays.asList("45.00%", "72.00%", "97.00%", "100.00%");
    }
    @Test
    public void sortByAccumulativeValue() {
        List<ABCAnalyseController.Data2Entry> result =
                ABCClassifier.sortByAccumulativeValue(testInventoryInfoEntries);

        // 保证结果行数跟输入一样
        assertEquals(testInventoryInfoEntries.size(), result.size());

        // 保证结果是按照资金降序排列。
        assertEquals(
                Arrays.asList(6500.0, 4001.0, 3600.0, 500.0),
                result.stream()
                        .map(ABCAnalyseController.Data2Entry::getAverageFundsOccupied)
                        .collect(Collectors.toList())
        );

        // 检查累计资金占总资金比例是对的
        assertEquals(
                accumulativeValuePercentages,
                result.stream()
                        .map(ABCAnalyseController.Data2Entry::getCumulativeAverageFundsOccupiedPercentage)
                        .collect(Collectors.toList())
        );
    }
}