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

    private List<ABCAnalyseController.Data5Entry> testOutboundInfoEntries;

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


        // Create outboutInfo table with total 15 entries.
        // 0001 x 2
        // 0002 x 1
        // 0003 x 5
        // 0004 x 4
        // 0005 x 3
        testOutboundInfoEntries =
                Arrays.asList(
                        // 0001 x 2
                        createOutboundInfoEntry("0001"),
                        createOutboundInfoEntry("0001"),
                        // 0002 x 1
                        createOutboundInfoEntry("0002"),
                        // 0003 x 5
                        createOutboundInfoEntry("0003"),
                        createOutboundInfoEntry("0003"),
                        createOutboundInfoEntry("0003"),
                        createOutboundInfoEntry("0003"),
                        createOutboundInfoEntry("0003"),
                        // 0004 x 4
                        createOutboundInfoEntry("0004"),
                        createOutboundInfoEntry("0004"),
                        createOutboundInfoEntry("0004"),
                        createOutboundInfoEntry("0004"),
                        // 0005 x 3
                        createOutboundInfoEntry("0005"),
                        createOutboundInfoEntry("0005"),
                        createOutboundInfoEntry("0005"));
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

    @Test
    public void testSortByOutboundFrequency() {
        List<ABCAnalyseController.Data3Entry> result =
                ABCClassifier.sortByOutboundFrequency(testOutboundInfoEntries);

        // total 5 types of materials.
        assertEquals(5, result.size());
        // 保证结果是按出库频次降序排列
        assertEquals(
                Arrays.asList(5, 4, 3, 2, 1),
                result.stream()
                        .map(ABCAnalyseController.Data3Entry::getOutboundFrequency)
                        .collect(Collectors.toList()));
        assertEquals(
                Arrays.asList("0003", "0004", "0005", "0001", "0002"),
                result.stream()
                        .map(ABCAnalyseController.Data3Entry::getMaterialCode)
                        .collect(Collectors.toList()));

        // 累计品目数
        assertEquals(
                Arrays.asList(1, 2, 3, 4, 5),
                result.stream()
                        .map(ABCAnalyseController.Data3Entry::getCumulativeItemCount)
                        .collect(Collectors.toList()));

        assertEquals(
                Arrays.asList("33.33%", "60.00%", "80.00%", "93.33%", "100.00%"),
                result.stream()
                        .map(ABCAnalyseController.Data3Entry::getCumulativeOutboundFrequencyPercentage)
                        .collect(Collectors.toList()));

        assertEquals(
                Arrays.asList("20.00%", "40.00%", "60.00%", "80.00%", "100.00%"),
                result.stream()
                        .map(ABCAnalyseController.Data3Entry::getCumulativeItemCountPercentage)
                        .collect(Collectors.toList()));

        // 检查所有的属性都不是空
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for(ABCAnalyseController.Data3Entry entry : result) {
            final Set<ConstraintViolation<ABCAnalyseController.Data3Entry>> violations =
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

    private ABCAnalyseController.Data5Entry createOutboundInfoEntry(String materialCode) {
        ABCAnalyseController.Data5Entry data5Entry = new ABCAnalyseController.Data5Entry();
        data5Entry.setMaterialNumber(materialCode);
        return data5Entry;
    }
}