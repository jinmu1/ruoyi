package com.ruoyi.baidie;

import com.ruoyi.data.eiq.*;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EIQAnalysisTableTest {
    private List<EIQBasicTable> testEIQBasicTable;
    private List<Date> testSortByDates;
    private List<Integer> testEAnalysis;
    private List<Integer> testNAnalysis;
    private List<Double> testQAnalysis;
    private List<Integer> testCumulativeItemNumber;
    private List<String> testOrderNumber;
    private List<Integer> testOrderLineCount;
    private int expectedTotalNumOrders;
    @Before
    public void setUp() {
        // Set up some test data
        EIQBasicTable obj1 = createBasicTableInfoWithDate(createDate(2024, 2, 5), "1234", "M1", "Material 1", 100.0, "kg", 10.0, 2.0, 1.0, 0.5);
        EIQBasicTable obj2 = createBasicTableInfoWithDate(createDate(2024, 2, 5), "5678", "M2", "Material 2", 150.0, "lbs", 15.0, 3.0, 1.5, 0.7);
        EIQBasicTable obj3 = createBasicTableInfoWithDate(createDate(2024, 2, 5), "91011", "M3", "Material 3", 200.0, "tons", 20.0, 4.0, 2.0, 1.0);
        EIQBasicTable obj4 = createBasicTableInfoWithDate(createDate(2024, 2, 5), "121314", "M4", "Material 4", 250.0, "grams", 25.0, 5.0, 2.5, 1.2);
        EIQBasicTable obj5 = createBasicTableInfoWithDate(createDate(2024, 2, 4), "151617", "M5", "Material 5", 300.0, "kg", 30.0, 6.0, 3.0, 1.5);
        EIQBasicTable obj6 = createBasicTableInfoWithDate(createDate(2024, 2, 4), "181920", "M6", "Material 6", 350.0, "lbs", 35.0, 7.0, 3.5, 1.7);
        EIQBasicTable obj7 = createBasicTableInfoWithDate(createDate(2024, 2, 3), "212223", "M7", "Material 7", 400.0, "tons", 40.0, 8.0, 4.0, 2.0);
        EIQBasicTable obj8 = createBasicTableInfoWithDate(createDate(2024, 2, 3), "212223", "M9", "Material 9", 500.0, "kg", 50.0, 10.0, 5.0, 2.5);
        EIQBasicTable obj9 = createBasicTableInfoWithDate(createDate(2024, 2, 2), "303132", "M10", "Material 10", 550.0, "lbs", 55.0, 11.0, 5.5, 2.7);

        testEIQBasicTable =
                Arrays.asList(obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9);
        testSortByDates =
                Arrays.asList(createDate(2024, 2, 2), createDate(2024, 2, 3), createDate(2024, 2, 4), createDate(2024, 2, 5));
        testEAnalysis = Arrays.asList(1, 1, 2, 4);
        testNAnalysis = Arrays.asList(1, 2, 2, 4);
        testQAnalysis = Arrays.asList(550.0, 900.0, 650.0, 700.0);
        expectedTotalNumOrders = 8;
        testCumulativeItemNumber = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        testOrderNumber = Arrays.asList("212223", "151617", "91011", "121314", "181920", "1234", "303132", "5678");
        testOrderLineCount = Arrays.asList(2, 1, 1, 1, 1, 1, 1, 1);
    }

    /****
     * 测试EIQ分析的处理结果
     */
    @Test
    public void getEIQAnalysisTableTest() {
        List<EIQAnalysisTable> result = EIQClassifier.getEIQAnalysisTable(testEIQBasicTable);

        // 保证结果行数跟输入一样
        assertEquals(testSortByDates.size(), result.size());

        // 保证结果是按照日期升序排列。
        assertEquals(
                testSortByDates,
                result.stream()
                        .map(EIQAnalysisTable::getDate)
                        .collect(Collectors.toList())
        );

        // 检查N分析是对的
        assertEquals(
                testEAnalysis,
                result.stream()
                        .map(EIQAnalysisTable::getEAnalysis)
                        .collect(Collectors.toList())
        );
        // 检查I分析是对的
        assertEquals(
                testNAnalysis,
                result.stream()
                        .map(EIQAnalysisTable::getNAnalysis)
                        .collect(Collectors.toList())
        );
        // 检查Q分析是对的
        assertEquals(
                testQAnalysis,
                result.stream()
                        .map(EIQAnalysisTable::getQAnalysis)
                        .collect(Collectors.toList())
        );

        // 检查所有的属性都不是空
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (EIQAnalysisTable entry : result) {
            final Set<ConstraintViolation<EIQAnalysisTable>> violations =
                    validator.validate(entry);
            assertTrue(violations.isEmpty());
        }

    }
    @Test
    public void getENAnalysisTableTest() {
        List<ENAnalysisTable> result = EIQClassifier.getENAnalysisTable(testEIQBasicTable);
        // Ensure the result size matches the input
        assertEquals(expectedTotalNumOrders, result.size());

        // Check if EN cumulative item numbers are correct
        assertEquals(
                testCumulativeItemNumber,
                result.stream()
                        .map(ENAnalysisTable::getCumulativeItemNumber)
                        .collect(Collectors.toList())
        );
        // Check if EN order numbers are correct
        assertEquals(
                testOrderNumber,
                result.stream()
                        .map(ENAnalysisTable::getOrderNumber)
                        .collect(Collectors.toList())
        );
        // Check if EN order line counts are correct
        assertEquals(
                testOrderLineCount,
                result.stream()
                        .map(ENAnalysisTable::getOrderLineCount)
                        .collect(Collectors.toList())
        );

    }
    //创建EIQ对象
    private EIQBasicTable createBasicTableInfoWithDate(Date deliveryDate,
                                                       String orderNumber,
                                                       String materialNumber,
                                                       String materialName,
                                                       double deliveryQuantity,
                                                       String deliveryUnit,
                                                       double unitPrice,
                                                       double palletizedItems,
                                                       double conversionUnit,
                                                       double conversionUnit1) {
        EIQBasicTable eiqBasicTable = new EIQBasicTable();
        eiqBasicTable.setDeliveryDate(deliveryDate);
        eiqBasicTable.setOrderNumber(orderNumber);
        eiqBasicTable.setMaterialNumber(materialNumber);
        eiqBasicTable.setMaterialName(materialName);
        eiqBasicTable.setDeliveryQuantity(deliveryQuantity);
        eiqBasicTable.setDeliveryUnit(deliveryUnit);
        eiqBasicTable.setUnitPrice(unitPrice);
        eiqBasicTable.setPalletizedItems(palletizedItems);
        eiqBasicTable.setConversionUnit(conversionUnit);
        eiqBasicTable.setConversionUnit1(conversionUnit1);
        return eiqBasicTable;
    }

    private Date createDate(int year,
                            int month,
                            int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar.FEBRUARY 是 1 月
        calendar.set(Calendar.DAY_OF_MONTH, day);
        // 将时间部分设置为 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date1 = calendar.getTime();
        return date1;
    }
}
