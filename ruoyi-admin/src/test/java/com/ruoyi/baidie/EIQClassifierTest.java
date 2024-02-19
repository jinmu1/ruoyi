package com.ruoyi.baidie;

import com.ruoyi.data.common.ObjectMap;
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

public class EIQClassifierTest {
    private List<EIQBasicInfo> testEIQBasicInfo;
    private List<Date> testSortByDates;
    private List<Integer> testEAnalysis;
    private List<Integer> testNAnalysis;
    private List<Double> testQAnalysis;
    private List<Integer> testCumulativeItemNumber;
    private List<String> testOrderNumber;
    private List<Integer> testOrderLineCount;
    private int expectedTotalNumOrders;
    private List<Double> testEQAnalysis;
    private List<String> testEQOrderNumber;
    private List<Integer> testEIAnalysis;
    private List<String> testEIOrderNumber;
    private int testSortByOrderNumber;
    private List<String> testIKMaterialCode;
    private List<String> testMaterialName;
    private List<Integer> testOccurrenceCount;
    private List<Integer> testMaterialNumerCumulativeItemNumber;
    private int testSortByMaterialNumber;
    private int testIntervalNumber;
    private List<String> testEIInterval;
    private List<Integer> testEIIntervalNumber;
    private int testIKNumber;
    private List<String> testIKInterval;
    private List<Integer> testIKIntervalNumber;
    @Before
    public void setUp() {
        // Set up some test data
        EIQBasicInfo obj1 = createBasicTableInfoWithDate(createDate(2024, 2, 5, 3, 8, 2), "1234", "M1", "Material 1", 100.0, "kg", 10.0, 2.0, 1.0, 0.5);
        EIQBasicInfo obj2 = createBasicTableInfoWithDate(createDate(2024, 2, 5, 3, 6, 2), "5678", "M2", "Material 2", 150.0, "lbs", 15.0, 3.0, 1.5, 0.7);
        EIQBasicInfo obj3 = createBasicTableInfoWithDate(createDate(2024, 2, 5, 3, 4, 22), "91011", "M3", "Material 3", 200.0, "tons", 20.0, 4.0, 2.0, 1.0);
        EIQBasicInfo obj4 = createBasicTableInfoWithDate(createDate(2024, 2, 5, 3, 9, 23), "121314", "M4", "Material 4", 250.0, "grams", 25.0, 5.0, 2.5, 1.2);
        EIQBasicInfo obj5 = createBasicTableInfoWithDate(createDate(2024, 2, 4, 3, 5, 21), "151617", "M5", "Material 5", 300.0, "kg", 30.0, 6.0, 3.0, 1.5);
        EIQBasicInfo obj6 = createBasicTableInfoWithDate(createDate(2024, 2, 4, 3, 5, 2), "181920", "M4", "Material 4", 350.0, "lbs", 35.0, 7.0, 3.5, 1.7);
        EIQBasicInfo obj7 = createBasicTableInfoWithDate(createDate(2024, 2, 3, 3, 5, 2), "212223", "M7", "Material 7", 400.0, "tons", 40.0, 8.0, 4.0, 2.0);
        EIQBasicInfo obj8 = createBasicTableInfoWithDate(createDate(2024, 2, 3, 3, 5, 2), "212223", "M2", "Material 2", 500.0, "kg", 50.0, 10.0, 5.0, 2.5);
        EIQBasicInfo obj9 = createBasicTableInfoWithDate(createDate(2024, 2, 2, 3, 5, 2), "303132", "M10", "Material 10", 550.0, "lbs", 55.0, 11.0, 5.5, 2.7);

        testEIQBasicInfo =
                Arrays.asList(obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9);
        testSortByDates =
                Arrays.asList(createDate(2024, 2, 2, 0, 0, 0), createDate(2024, 2, 3, 0, 0, 0), createDate(2024, 2, 4, 0, 0, 0), createDate(2024, 2, 5, 0, 0, 0));
        testEAnalysis = Arrays.asList(1, 1, 2, 4);
        testNAnalysis = Arrays.asList(1, 2, 2, 4);
        testQAnalysis = Arrays.asList(550.0, 900.0, 650.0, 700.0);
        expectedTotalNumOrders = 8;
        testCumulativeItemNumber = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        testOrderNumber = Arrays.asList("212223", "151617", "91011", "121314", "181920", "1234", "303132", "5678");
        testOrderLineCount = Arrays.asList(2, 1, 1, 1, 1, 1, 1, 1);
        testEQAnalysis = Arrays.asList(900.0, 550.0, 350.0, 300.0, 250.0, 200.0, 150.0, 100.0);
        testEQOrderNumber = Arrays.asList("212223", "303132", "181920", "151617", "121314", "91011", "5678", "1234");
        testEIAnalysis = Arrays.asList(2, 1, 1, 1, 1, 1, 1, 1);
        testEIOrderNumber = Arrays.asList("212223", "151617", "91011", "121314", "181920", "1234", "303132", "5678");
        testSortByOrderNumber = 8;
        testIKMaterialCode = Arrays.asList("M2", "M4", "M1", "M3", "M5", "M7", "M10");
        testMaterialName = Arrays.asList("Material 2", "Material 4", "Material 1", "Material 3", "Material 5", "Material 7", "Material 10");
        testOccurrenceCount = Arrays.asList(2, 2, 1, 1, 1, 1, 1);
        testSortByMaterialNumber = 7;
        testMaterialNumerCumulativeItemNumber = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        testIntervalNumber = 5;
        testEIInterval = Arrays.asList("[1, 2)", "[2, 3)", "[3, 4)", "[4, 5)", "[5, 6)");
        testEIIntervalNumber = Arrays.asList(7, 1, 0, 0, 0);
        testIKNumber = 5;
        testIKInterval = Arrays.asList("[1, 2)", "[2, 3)", "[3, 4)", "[4, 5)", "[5, 6)");
        testIKIntervalNumber = Arrays.asList(5, 2, 0, 0, 0);
    }

    /****
     * 测试EIQ分析的处理结果
     */
    @Test
    public void getEIQAnalysisTableTest() {
        List<EIQAnalysisInfo> result = EIQClassifier.getEIQAnalysisTable(testEIQBasicInfo);

        // 保证结果行数跟输入一样
        assertEquals(testSortByDates.size(), result.size());

        // 保证结果是按照日期升序排列。
        assertEquals(
                testSortByDates,
                result.stream()
                        .map(EIQAnalysisInfo::getDate)
                        .collect(Collectors.toList())
        );

        // 检查N分析是对的
        assertEquals(
                testEAnalysis,
                result.stream()
                        .map(EIQAnalysisInfo::getEAnalysis)
                        .collect(Collectors.toList())
        );
        // 检查I分析是对的
        assertEquals(
                testNAnalysis,
                result.stream()
                        .map(EIQAnalysisInfo::getNAnalysis)
                        .collect(Collectors.toList())
        );
        // 检查Q分析是对的
        assertEquals(
                testQAnalysis,
                result.stream()
                        .map(EIQAnalysisInfo::getQAnalysis)
                        .collect(Collectors.toList())
        );

        // 检查所有的属性都不是空
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (EIQAnalysisInfo entry : result) {
            final Set<ConstraintViolation<EIQAnalysisInfo>> violations =
                    validator.validate(entry);
            assertTrue(violations.isEmpty());
        }

    }

    @Test
    public void getENAnalysisTableTest() {
        List<ENAnalysisInfo> result = EIQClassifier.getENAnalysisTable(testEIQBasicInfo);
        // Ensure the result size matches the input
        assertEquals(expectedTotalNumOrders, result.size());

        // Check if EN cumulative item numbers are correct
        assertEquals(
                testCumulativeItemNumber,
                result.stream()
                        .map(ENAnalysisInfo::getCumulativeItemNumber)
                        .collect(Collectors.toList())
        );
        // Check if EN order numbers are correct
        assertEquals(
                testOrderNumber,
                result.stream()
                        .map(ENAnalysisInfo::getOrderNumber)
                        .collect(Collectors.toList())
        );
        // Check if EN order line counts are correct
        assertEquals(
                testOrderLineCount,
                result.stream()
                        .map(ENAnalysisInfo::getOrderLineCount)
                        .collect(Collectors.toList())
        );

    }

    @Test
    public void getEQAnalysisTableTest() {
        List<EQAnalysisInfo> result = EIQClassifier.getEQAnalysisTable(testEIQBasicInfo);
        // 保证结果行数跟输入一样
        assertEquals(expectedTotalNumOrders, result.size());

        // 检查EQ-序列号是对的
        assertEquals(
                testCumulativeItemNumber,
                result.stream()
                        .map(EQAnalysisInfo::getCumulativeItemNumber)
                        .collect(Collectors.toList())
        );
        // 检查EQ-订单号是对的
        assertEquals(
                testEQOrderNumber,
                result.stream()
                        .map(EQAnalysisInfo::getOrderNumber)
                        .collect(Collectors.toList())
        );
        // 检查EQ-订货数量是对的
        assertEquals(
                testEQAnalysis,
                result.stream()
                        .map(EQAnalysisInfo::getTotalDeliveredQuantity)
                        .collect(Collectors.toList())
        );

    }

    @Test
    public void getEIAnalysisInfoTest() {
        List<EIAnalysisInfo> result = EIQClassifier.getEIAnalysisTable(testEIQBasicInfo);
        // 保证结果行数跟输入一样
        assertEquals(testSortByOrderNumber, result.size());

        // 检查EI-序列号是对的
        assertEquals(
                testCumulativeItemNumber,
                result.stream()
                        .map(EIAnalysisInfo::getCumulativeItemNumber)
                        .collect(Collectors.toList())
        );
        // 检查EI-订单号是对的
        assertEquals(
                testEIOrderNumber,
                result.stream()
                        .map(EIAnalysisInfo::getOrderNumber)
                        .collect(Collectors.toList())
        );
        // 检查EI-物料种类数是对的
        assertEquals(
                testEIAnalysis,
                result.stream()
                        .map(EIAnalysisInfo::getMaterialVarietiesCount)
                        .collect(Collectors.toList())
        );

    }

    @Test
    public void getIKAnalysisTableTest() {
        List<IKAnalysisInfo> result = EIQClassifier.getIKAnalysisInfoTest(testEIQBasicInfo);
        // 保证结果行数跟输入一样
        assertEquals(testSortByMaterialNumber, result.size());

        // 检查IK-序列号是对的
        assertEquals(
                testMaterialNumerCumulativeItemNumber,
                result.stream()
                        .map(IKAnalysisInfo::getCumulativeItemNumber)
                        .collect(Collectors.toList())
        );
        // 检查IK-物料号是对的
        assertEquals(
                testIKMaterialCode,
                result.stream()
                        .map(IKAnalysisInfo::getMaterialCode)
                        .collect(Collectors.toList())
        );
        // 检查IK-物料名称是对的
        assertEquals(
                testMaterialName,
                result.stream()
                        .map(IKAnalysisInfo::getMaterialName)
                        .collect(Collectors.toList())
        );

        // 检查IK-物料出现次数是对的
        assertEquals(
                testOccurrenceCount,
                result.stream()
                        .map(IKAnalysisInfo::getOccurrenceCount)
                        .collect(Collectors.toList())
        );

    }

    @Test
    public void getEIHistogramTest() {
        List<ObjectMap> result = EIQClassifier.getEIHistogram(testEIQBasicInfo);
        // 保证结果行数跟输入一样
        assertEquals(testIntervalNumber, result.size());
        // 检查EI-直方图的区间是对的
        assertEquals(
                testEIInterval,
                result.stream()
                        .map(ObjectMap::getKey)
                        .collect(Collectors.toList())
        );
        // 检查EI-直方图的值是对的
        assertEquals(
                testEIIntervalNumber,
                result.stream()
                        .map(ObjectMap::getValue)
                        .collect(Collectors.toList())
        );
    }
    @Test
    public void getIKHistogramTest() {
        List<ObjectMap> result = EIQClassifier.getIKHistogram(testEIQBasicInfo);
        // 保证结果行数跟输入一样
        assertEquals(testIKNumber, result.size());
        // 检查EI-直方图的区间是对的
        assertEquals(
                testIKInterval,
                result.stream()
                        .map(ObjectMap::getKey)
                        .collect(Collectors.toList())
        );
        // 检查EI-直方图的值是对的
        assertEquals(
                testIKIntervalNumber,
                result.stream()
                        .map(ObjectMap::getValue)
                        .collect(Collectors.toList())
        );
    }

    //创建EIQ对象
    private EIQBasicInfo createBasicTableInfoWithDate(Date deliveryDate,
                                                      String orderNumber,
                                                      String materialNumber,
                                                      String materialName,
                                                      double deliveryQuantity,
                                                      String deliveryUnit,
                                                      double unitPrice,
                                                      double palletizedItems,
                                                      double conversionUnit,
                                                      double conversionUnit1) {
        EIQBasicInfo eiqBasicInfo = new EIQBasicInfo();
        eiqBasicInfo.setDeliveryDate(deliveryDate);
        eiqBasicInfo.setOrderNumber(orderNumber);
        eiqBasicInfo.setMaterialNumber(materialNumber);
        eiqBasicInfo.setMaterialName(materialName);
        eiqBasicInfo.setDeliveryQuantity(deliveryQuantity);
        eiqBasicInfo.setDeliveryUnit(deliveryUnit);
        eiqBasicInfo.setUnitPrice(unitPrice);
        eiqBasicInfo.setPalletizedItems(palletizedItems);
        eiqBasicInfo.setConversionUnit(conversionUnit);
        eiqBasicInfo.setConversionUnit1(conversionUnit1);
        return eiqBasicInfo;
    }

    private Date createDate(int year,
                            int month,
                            int day,
                            int hour,
                            int minute,
                            int second) {
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
