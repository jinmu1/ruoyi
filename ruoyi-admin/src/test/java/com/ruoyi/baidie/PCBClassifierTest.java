package com.ruoyi.baidie;


import com.ruoyi.data.pcb.MaterialOutboundTypeInfo;
import com.ruoyi.data.pcb.MaterialPackagingInfo;
import com.ruoyi.data.pcb.OutboundBasicInfo;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PCBClassifierTest {
    private List<OutboundBasicInfo> outboundBasicInfos = new ArrayList<>();
    private List<String> testMaterialCode;
    private List<Integer> testCorrespondenceA;
    private List<Double> testOutQuantityN;
    private List<Integer> testCorrespondenceB;
    private List<Integer> testProductBoxRelation;
    private List<Double> testOutDivision;
    private List<Double> testDemandPalletQuantity;
    private List<Double> testRemainder;
    private List<Double> testDivisionByA;
    private List<Double> testDemandBoxQuantity;
    private List<Double> testDemandProductQuantity;
    private List<Double> testPalletQuantity;
    private List<Double> testBoxQuantity;
    private List<Double> testProductQuantity;
    private List<String> testPackagingMode;

    @Before
    public void setUp() {
        OutboundBasicInfo info1 = createOutboundBasicInfo("HBDD10916488", "4704404", "阿萨姆奶茶（统一，500ML*15瓶/件）", 544.0, "15瓶/件", "60件/托");
        OutboundBasicInfo info2 = createOutboundBasicInfo("HBDD10916489", "4001842", "奥古特（500ML*12瓶/件）", 450.0, "12瓶/件", "90件/托");
        OutboundBasicInfo info3 = createOutboundBasicInfo("HBDD10916492", "4003064", "百岁山纯净水（570ML*24瓶/件）", 1341.0, "24瓶/件", "30件/托");
        OutboundBasicInfo info4 = createOutboundBasicInfo("HBDD10916494", "4003288", "桂花酸梅汤（信远斋，300ML*12瓶/件）", 1912.0, "12瓶/件", "72件/托");
        OutboundBasicInfo info5 = createOutboundBasicInfo("HBDD10916495", "4000869", "哈尔滨冰纯（500ML*12瓶/件）", 1451.0, "12瓶/件", "90件/托");
        OutboundBasicInfo info6 = createOutboundBasicInfo("HBDD10916496", "4002609", "哈尔滨小麦王啤酒（500ML*12瓶/件）", 1258.0, "12瓶/件", "80件/托");
        OutboundBasicInfo info7 = createOutboundBasicInfo("HBDD10916501", "4000451", "牛栏山白酒42度（500ML*12瓶/件）", 521.0, "12瓶/件", "32件/托");
        OutboundBasicInfo info8 = createOutboundBasicInfo("HBDD10916503", "4000966", "青岛纯生（500ML*12瓶/件）", 1588.0, "12瓶/件", "75件/托");
        OutboundBasicInfo info9 = createOutboundBasicInfo("HBDD10916505", "4000710", "雪碧（1.25L*12瓶/件）", 1088.0, "12瓶/件", "40件/托");
        OutboundBasicInfo info10 = createOutboundBasicInfo("HBDD10916506", "4001019", "雪花纯生（500ML*12瓶/件）", 1107.0, "12瓶/件", "75件/托");
        OutboundBasicInfo info11 = createOutboundBasicInfo("HBDD10916507", "4001543", "雪花勇闯天涯（500ML*12瓶/件）", 2497.0, "12瓶/件", "68件/托");
        OutboundBasicInfo info12 = createOutboundBasicInfo("HBDD10916509", "4001548", "燕京鲜啤（500ML*12瓶/件）", 3124.0, "12瓶/件", "90件/托");

        outboundBasicInfos = Arrays.asList(info1, info2, info3, info4, info5, info6, info7, info8, info9, info10, info11, info12);
        testMaterialCode = Arrays.asList("4704404", "4001842", "4003064", "4003288", "4000869", "4002609", "4000451", "4000966", "4000710", "4001019", "4001543", "4001548");
        testCorrespondenceA = Arrays.asList(15, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12);
        testOutQuantityN = Arrays.asList(544.0, 450.0, 1341.0, 1912.0, 1451.0, 1258.0, 521.0, 1588.0, 1088.0, 1107.0, 2497.0, 3124.0);
        testCorrespondenceB = Arrays.asList(60, 90, 30, 72, 90, 80, 32, 75, 40, 75, 68, 90);
        testProductBoxRelation = Arrays.asList(900, 1080, 720, 864, 1080, 960, 384, 900, 480, 900, 816, 1080);
        testOutDivision = Arrays.asList(0.6, 0.42, 1.86, 2.21, 1.34, 1.31, 1.36, 1.76, 2.27, 1.23, 3.06, 2.89);
        testDemandPalletQuantity = Arrays.asList(0.0, 0.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 3.0, 2.0);
        testRemainder = Arrays.asList(544.0, 450.0, 621.0, 184.0, 371.0, 298.0, 137.0, 688.0, 128.0, 207.0, 49.0, 964.0);
        testDivisionByA = Arrays.asList(36.27, 37.5, 25.88, 15.33, 30.92, 24.83, 11.42, 57.33, 10.67, 17.25, 4.08, 80.33);
        testDemandBoxQuantity = Arrays.asList(36.0, 37.0, 25.0, 15.0, 30.0, 24.0, 11.0, 57.0, 10.0, 17.0, 4.0, 80.0);
        testDemandProductQuantity = Arrays.asList(4.0, 6.0, 21.0, 4.0, 11.0, 10.0, 5.0, 4.0, 8.0, 3.0, 1.0, 4.0);
        testPalletQuantity = Arrays.asList(0.0, 0.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 3.0, 2.0);
        testBoxQuantity = Arrays.asList(36.0, 37.0, 25.0, 15.0, 30.0, 24.0, 11.0, 57.0, 10.0, 17.0, 4.0, 80.0);
        testProductQuantity = Arrays.asList(4.0, 6.0, 21.0, 4.0, 11.0, 10.0, 5.0, 4.0, 8.0, 3.0, 1.0, 4.0);
        testPackagingMode = Arrays.asList("C+B", "C+B", "P+C+B", "P+C+B", "P+C+B", "P+C+B", "P+C+B", "P+C+B", "P+C+B", "P+C+B", "P+C+B", "P+C+B");
    }


    @Test
    public void testTransformPackaging() {
        List<MaterialPackagingInfo> result =
                PCBClassifier.transformPackaging(outboundBasicInfos);

        // 保证结果行数跟输入一样
        assertEquals(outboundBasicInfos.size(), result.size());

        // 检查物料编码不会变化。
        assertEquals(
                testMaterialCode,
                result.stream()
                        .map(MaterialPackagingInfo::getMaterialCode)
                        .collect(Collectors.toList())
        );
        // 检查单品与箱的对应数量关系不会变化。
        assertEquals(
                testCorrespondenceA,
                result.stream()
                        .map(MaterialPackagingInfo::getCorrespondenceA)
                        .collect(Collectors.toList())
        );
        // 检查出库数量不会变化。
        assertEquals(
                testOutQuantityN,
                result.stream()
                        .map(MaterialPackagingInfo::getOutQuantityN)
                        .collect(Collectors.toList())
        );
        // 检查箱与托盘的对应数量关系不会变化。
        assertEquals(
                testCorrespondenceB,
                result.stream()
                        .map(MaterialPackagingInfo::getCorrespondenceB)
                        .collect(Collectors.toList())
        );
        // 检查托盘对于的件数的对应数量关系不会变化。
        assertEquals(
                testProductBoxRelation,
                result.stream()
                        .map(MaterialPackagingInfo::getProductBoxRelation)
                        .collect(Collectors.toList())
        );
        // 检查箱数和件数相乘的结果是不是正确。
        assertEquals(
                testProductBoxRelation,
                result.stream()
                        .map(MaterialPackagingInfo::getProductBoxRelation)
                        .collect(Collectors.toList())
        );
        // 检查按托换算后的剩下的物料数量。
        assertEquals(
                testOutDivision,
                result.stream()
                        .map(MaterialPackagingInfo::getOutDivision)
                        .collect(Collectors.toList())
        );
        // 检查需求数量对应托盘数量不会变化。
        assertEquals(
                testDemandPalletQuantity,
                result.stream()
                        .map(MaterialPackagingInfo::getDemandPalletQuantity)
                        .collect(Collectors.toList())
        );
        // 检查经过箱数转换后的箱数量。
        assertEquals(
                testRemainder,
                result.stream()
                        .map(MaterialPackagingInfo::getRemainder)
                        .collect(Collectors.toList())
        );
        // 检查经过箱数转换后生下来的箱数量。
        assertEquals(
                testDivisionByA,
                result.stream()
                        .map(MaterialPackagingInfo::getDivisionByA)
                        .collect(Collectors.toList())
        );
        // 检查最后按照单品计算的数量。
        assertEquals(
                testDemandBoxQuantity,
                result.stream()
                        .map(MaterialPackagingInfo::getDemandBoxQuantity)
                        .collect(Collectors.toList())
        );
        // 检查最后按照单品计算的数量。
        assertEquals(
                testDemandProductQuantity,
                result.stream()
                        .map(MaterialPackagingInfo::getDemandProductQuantity)
                        .collect(Collectors.toList())
        );
        // 检查所有的属性都不是空
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (MaterialPackagingInfo entry : result) {
            final Set<ConstraintViolation<MaterialPackagingInfo>> violations =
                    validator.validate(entry);
            assertTrue(violations.isEmpty());
        }
    }

    @Test
    public void testPickPackagingType() {
        List<MaterialOutboundTypeInfo> result =
                PCBClassifier.pickPackagingType(outboundBasicInfos);

        // 保证结果行数跟输入一样
        assertEquals(outboundBasicInfos.size(), result.size());

        // 检查物料编码不会变化。
        assertEquals(
                testMaterialCode,
                result.stream()
                        .map(MaterialOutboundTypeInfo::getMaterialCode)
                        .collect(Collectors.toList())
        );
        // 检查托盘数量是否一致
        assertEquals(
                testPalletQuantity,
                result.stream()
                        .map(MaterialOutboundTypeInfo::getPalletQuantity)
                        .collect(Collectors.toList())
        );
        // 检查箱数是否一致。
        assertEquals(
                testBoxQuantity,
                result.stream()
                        .map(MaterialOutboundTypeInfo::getBoxQuantity)
                        .collect(Collectors.toList())
        );
        // 检查单品数量是否一致。
        assertEquals(
                testProductQuantity,
                result.stream()
                        .map(MaterialOutboundTypeInfo::getProductQuantity)
                        .collect(Collectors.toList())
        );
        // 检查最后计算的包装类型是否一致。
        assertEquals(
                testPackagingMode,
                result.stream()
                        .map(MaterialOutboundTypeInfo::getPackagingMode)
                        .collect(Collectors.toList())
        );

        // 检查所有的属性都不是空
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (MaterialOutboundTypeInfo entry : result) {
            final Set<ConstraintViolation<MaterialOutboundTypeInfo>> violations =
                    validator.validate(entry);
            assertTrue(violations.isEmpty());
        }
    }

    private OutboundBasicInfo createOutboundBasicInfo(String orderNumber, String materialCode, String materialName, Double outQuantity, String outUnit, String outPalletUnit) {
        OutboundBasicInfo outboundBasicInfo = new OutboundBasicInfo();
        outboundBasicInfo.setMaterialCode(materialCode);
        outboundBasicInfo.setOrderNumber(orderNumber);
        outboundBasicInfo.setOutUnit(outUnit);
        outboundBasicInfo.setOutQuantity(outQuantity);
        outboundBasicInfo.setOutPalletUnit(outPalletUnit);
        outboundBasicInfo.setMaterialName(materialName);
        return outboundBasicInfo;
    }
}