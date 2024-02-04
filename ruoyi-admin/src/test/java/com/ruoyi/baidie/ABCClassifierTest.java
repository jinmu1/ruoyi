package com.ruoyi.baidie;

import com.ruoyi.web.controller.system.ABCAnalyseController;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ABCClassifierTest {
    private List<ABCAnalyseController.Data1Entry> testInventoryInfoEntries;
    private List<String> accumulativeValuePercentages;

    @Before
    public void setUp() {
        // Set up some test data
        ABCAnalyseController.Data1Entry inventoryEntry1 =
                createInventoryInfoWithPriceAndInventory(5, 100);

        ABCAnalyseController.Data1Entry inventoryEntry2 =
                createInventoryInfoWithPriceAndInventory(3.6, 1000);

        ABCAnalyseController.Data1Entry inventoryEntry3 =
                createInventoryInfoWithPriceAndInventory(4001, 1);

        ABCAnalyseController.Data1Entry inventoryEntry4 =
                createInventoryInfoWithPriceAndInventory(65, 100);

        testInventoryInfoEntries =
                Arrays.asList(inventoryEntry1, inventoryEntry2, inventoryEntry3, inventoryEntry4);
        accumulativeValuePercentages = Arrays.asList("44.52%", "71.92%", "96.58%", "100.00%");
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

        // 检查所有的属性都不是空
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for(ABCAnalyseController.Data2Entry entry : result) {
            final Set<ConstraintViolation<ABCAnalyseController.Data2Entry>> violations =
                    validator.validate(entry);
            assertTrue(violations.isEmpty());
        }
    }

    private ABCAnalyseController.Data1Entry createInventoryInfoWithPriceAndInventory(
            double price, int inventory) {
        ABCAnalyseController.Data1Entry entry = new ABCAnalyseController.Data1Entry();
        entry.setAverageInventory(inventory);
        entry.setSellingPrice(price);

        entry.setMaterialCode("xxxxx");
        return entry;
    }
}