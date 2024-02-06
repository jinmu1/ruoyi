package com.ruoyi.baidie;

import com.ruoyi.data.eiq.*;
import io.swagger.models.auth.In;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EQAnalysisTableTest {
    private List<EIQBasicTable> testEIQBasicTable;
    private int testSortByOrderNumer;
    private List<Integer> testCumulativeItemNumber;
    private List<Double> testEQAnalysis;
    private List<String> testEQOrderNumber;
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
        testSortByOrderNumer = 8;
        testCumulativeItemNumber = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        testEQAnalysis = Arrays.asList(900.0, 550.0, 350.0, 300.0, 250.0, 200.0, 150.0, 100.0);
        testEQOrderNumber = Arrays.asList("212223", "303132", "181920", "151617", "121314", "91011", "5678", "1234");
    }

    @Test
    public void getEQAnalysisTableTest(){
        List<EQAnalysisTable> result =  EIQClassifier.getEQAnalysisTable(testEIQBasicTable);
        // 保证结果行数跟输入一样
        assertEquals(testSortByOrderNumer, result.size());

        // 检查EQ-序列号是对的
        assertEquals(
                testCumulativeItemNumber,
                result.stream()
                        .map(EQAnalysisTable::getCumulativeItemNumber)
                        .collect(Collectors.toList())
        );
        // 检查EQ-订单号是对的
        assertEquals(
                testEQOrderNumber,
                result.stream()
                        .map(EQAnalysisTable::getOrderNumber)
                        .collect(Collectors.toList())
        );
        // 检查EQ-订货数量是对的
        assertEquals(
                testEQAnalysis,
                result.stream()
                        .map(EQAnalysisTable::getTotalDeliveredQuantity)
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
