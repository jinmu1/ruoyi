package com.ruoyi.baidie;


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

    @Before
    public void setUp() {
        outboundBasicInfos = Arrays.asList(
                new OutboundBasicInfo("2018/3/1", "HBDD10916488", "4704404", "阿萨姆奶茶（统一，500ML*15瓶/件）", 544.0, "15瓶/件", "60件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916489", "4001842", "奥古特（500ML*12瓶/件）", 450.0, "12瓶/件", "90件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916492", "4003064", "百岁山纯净水（570ML*24瓶/件）", 1341.0, "24瓶/件", "30件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916494", "4003288", "桂花酸梅汤（信远斋，300ML*12瓶/件）", 1912.0, "12瓶/件", "72件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916495", "4000869", "哈尔滨冰纯（500ML*12瓶/件）", 1451.0, "12瓶/件", "90件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916496", "4002609", "哈尔滨小麦王啤酒（500ML*12瓶/件）", 1258.0, "12瓶/件", "80件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916501", "4000451", "牛栏山白酒42度（500ML*12瓶/件）", 521.0, "12瓶/件", "32件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916503", "4000966", "青岛纯生（500ML*12瓶/件）", 1588.0, "12瓶/件", "75件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916505", "4000710", "雪碧（1.25L*12瓶/件）", 1088.0, "12瓶/件", "40件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916506", "4001019", "雪花纯生（500ML*12瓶/件）", 1107.0, "12瓶/件", "75件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916507", "4001543", "雪花勇闯天涯（500ML*12瓶/件）", 2497.0, "12瓶/件", "68件/托"),
                new OutboundBasicInfo("2018/3/1", "HBDD10916509", "4001548", "燕京鲜啤（500ML*12瓶/件）", 3124.0, "12瓶/件", "90件/托")
        );
        testMaterialCode = Arrays.asList("4704404", "4001842", "4003064", "4003288", "4000869", "4002609", "4000451", "4000966", "4000710", "4001019", "4001543", "4001548");
        testCorrespondenceA = Arrays.asList(15, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12);
        testOutQuantityN = Arrays.asList(544.0, 450.0, 1341.0, 1912.0, 1451.0, 1258.0, 521.0, 1588.0, 1088.0, 1107.0, 2497.0, 3124.0);
        testCorrespondenceB = Arrays.asList(60, 90, 30, 72, 90, 80, 32, 75, 40, 75, 68, 90);
        testProductBoxRelation = Arrays.asList(900, 1080, 720, 864, 1080, 960, 384, 900, 480, 900, 816, 1080);

    }

    @Test
    public void transformPackaging() {
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
        // 检查所有的属性都不是空
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (MaterialPackagingInfo entry : result) {
            final Set<ConstraintViolation<MaterialPackagingInfo>> violations =
                    validator.validate(entry);
            assertTrue(violations.isEmpty());
        }
    }


}