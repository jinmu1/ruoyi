package com.ruoyi.baidie;

import com.google.common.collect.ImmutableSet;
import com.ruoyi.web.controller.system.ABCAnalyseController;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static org.junit.Assert.assertEquals;

public class BaidieProcessorTest {
    @Test(expected = IOException.class)
    public void importFileForGroupOne_ParseFailure() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "wrong extension",
                "filename.kml",
                "text/plain",
                "some kml".getBytes());
        try {
            BaidieProcessor.importABCGroupOne(mockFile);
        } catch (IOException e) {
            assertEquals(
                    e.getMessage(),
                    BaidieProcessor.IMPORT_FAILURE_MSG);
            throw e;
        }
    }

    @Test
    public void importFileForGroupOne() throws Exception {
        final String path = "src/test/resources/abc_first_upload_table.xls";

        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "xlsFile",
                "abc_first_upload_table.xls",
                MediaType.APPLICATION_XML_VALUE,
                Files.newInputStream(Paths.get(path)));
        Map<String, List<?>> resultMap = BaidieProcessor.importABCGroupOne(mockMultipartFile);

        // Test that the result keyset is data1 and data2.
        assertEquals(resultMap.keySet(), ImmutableSet.of("data1", "data2"));

        // Make sure data from 'data1' key is correct.
        final List<ABCAnalyseController.Data1Entry> data1Entries =
                (List<ABCAnalyseController.Data1Entry>) resultMap.get("data1");
        assertEquals(11, data1Entries.size());
        assertEquals("4001543", data1Entries.get(0).getMaterialCode());
        assertEquals(Double.valueOf(100), Double.valueOf(data1Entries.get(0).getSellingPrice()));

        assertEquals("4714999", data1Entries.get(10).getMaterialCode());
        assertEquals(Double.valueOf(60), Double.valueOf(data1Entries.get(10).getSellingPrice()));

        // Make sure data from 'data2' key is correct.
        final List<ABCAnalyseController.Data2Entry> data2Entries =
                (List<ABCAnalyseController.Data2Entry>) resultMap.get("data2");
        assertEquals(data2Entries.size(), 11);

        // Pick some random entry to verify
        // index 1
        // {"物料编码":"4001543","销售单价":100.0,"平均库存":404.0,"平均资金占用额":40400.0,"平均资金占用额累计":235966.0,"平均资金占用额累计百分比":"66.65%","物料累计品目数":2,"物料累计品目数百分比":"18.00%"}
        assertEquals(Double.valueOf(40400.0),
                Double.valueOf(data2Entries.get(1).getAverageFundsOccupied()));
        assertEquals("4001543", data2Entries.get(1).getMaterialCode());

        // index 4.
        // {"物料编码":"4000966","销售单价":157.0,"平均库存":103.0,"平均资金占用额":16171.0,"平均资金占用额累计":326860.0,"平均资金占用额累计百分比":"92.32%","物料累计品目数":5,"物料累计品目数百分比":"45.00%"}
        assertEquals(Double.valueOf(103),
                Double.valueOf(data2Entries.get(4).getAverageInventory()));
        assertEquals("4000966", data2Entries.get(4).getMaterialCode());
    }


    @Test(expected = Exception.class)
    public void testImportABCGroupTwo_ParseFileFailure() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "wrong extension",
                "filename.kml",
                "text/plain",
                "some kml".getBytes());
        try {
            BaidieProcessor.importABCGroupTwo(mockFile);
        } catch (IOException e) {
            assertEquals(
                    e.getMessage(),
                    BaidieProcessor.IMPORT_FAILURE_MSG);
            throw e;
        }
    }

    @Test
    public void testImportABCGroupTwo() throws IOException {
        final String path = "src/test/resources/abc_second_upload_table.xlsx";

        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "xlsFile",
                "abc_second_upload_table.xlsx",
                MediaType.APPLICATION_XML_VALUE,
                Files.newInputStream(Paths.get(path)));
        Map<String, List<?>> resultMap = BaidieProcessor.importABCGroupTwo(mockMultipartFile);

        // Test that the result keyset is data3 and data5.
        assertEquals(resultMap.keySet(), ImmutableSet.of("data3", "data5"));

        // Make sure data from 'data3' key is correct.
        final List<ABCAnalyseController.Data3Entry> data3Entries =
                (List<ABCAnalyseController.Data3Entry>) resultMap.get("data3");
        assertEquals(5, data3Entries.size());
        assertEquals("4703710", data3Entries.get(0).getMaterialCode());
        assertEquals(223, data3Entries.get(0).getOutboundFrequency());

        assertEquals("4704404", data3Entries.get(3).getMaterialCode());
        assertEquals(18, data3Entries.get(3).getOutboundFrequency());

        // Make sure data from 'data5' key is correct.
        final List<ABCAnalyseController.Data5Entry> data5Entries =
                (List<ABCAnalyseController.Data5Entry>) resultMap.get("data5");
        assertEquals(340, data5Entries.size());

        // Pick some random entry to verify
        // index 100
        //出库日期:"2018/03/04"
        //出货单位:"308克/包"
        //出货数量:4
        //托盘装件数:60
        //物料名称:"香脆椒（黄飞红，308G*10包/件）"
        //物料编号:"4703710"
        //订单编号:11930498
        //销售单价:135
        assertEquals(11930498, data5Entries.get(100).getOrderNumber());
        assertEquals(4.0, data5Entries.get(100).getShippedQuantity(), 0.1);

        // index 167.
        //出库日期:"2018/03/12"
        //出货单位:"308克/包"
        //出货数量:5
        //托盘装件数:60
        //物料名称:"香脆椒（黄飞红，308G*10包/件）"
        //物料编号:"4703710"
        //订单编号:11962202
        //销售单价:135
        assertEquals("308克/包", data5Entries.get(167).getShipmentUnit());
        assertEquals("4703710", data5Entries.get(167).getMaterialNumber());
    }

    @Test(expected = IOException.class)
    public void testImportABCGroupThree_ParseFailure() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "wrong extension",
                "filename.kml",
                "text/plain",
                "some kml".getBytes());
        try {
            BaidieProcessor.importABCGroupThree(mockFile);
        } catch (IOException e) {
            assertEquals(
                    e.getMessage(),
                    BaidieProcessor.IMPORT_FAILURE_MSG);
            throw e;
        }
    }

    @Test
    public void testImportABCGroupThree() throws IOException {
        final String path = "src/test/resources/abc_second_upload_table.xlsx";

        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "xlsFile",
                "abc_second_upload_table.xlsx",
                MediaType.APPLICATION_XML_VALUE,
                Files.newInputStream(Paths.get(path)));
        Map<String, List<?>> resultMap = BaidieProcessor.importABCGroupThree(mockMultipartFile);

        // Test that the result keyset is data4 and data5.
        assertEquals(ImmutableSet.of("data4", "data5"), resultMap.keySet());

        // Make sure data from 'data4' key is correct.
        final List<ABCAnalyseController.Data4Entry> data4Entries =
                (List<ABCAnalyseController.Data4Entry>) resultMap.get("data4");
        assertEquals(5, data4Entries.size());

        //出库量:764
        //物料描述/:"香脆椒（黄飞红，308G*10包/件）"
        //物料累计品目数:1
        //物料累计品目数百分比:"20.00%"
        //物料编码:"4703710"
        //累计出库量:764
        //累计出库量百分比:67.55%"
        assertEquals("4703710", data4Entries.get(0).getMaterialCode());
        assertEquals(764, data4Entries.get(0).getOutboundQuantity(),0.1);
        assertEquals("67.55%", data4Entries.get(0).getCumulativeOutboundQuantityPercentage());


        //出库量:14
        //物料描述:"咖啡伴侣（雀巢，10ML*50个*6包/件）"
        //物料累计品目数:5
        //物料累计品目数百分比:"100.00%"
        //物料编码/:"4705461"
        //累计出库量:1131
        //累计出库量百分比:"100.00%"
        assertEquals("4705461", data4Entries.get(4).getMaterialCode());
        assertEquals(14, data4Entries.get(4).getOutboundQuantity(), 0.1);
        assertEquals(1131, data4Entries.get(4).getCumulativeOutboundQuantity(), 0.1);
    }

    // 测试这个通用方法。
    // 这是一个private static 方法，通常情况下不应该直接测试这个private方法，可是基于这个方法会被所有上传功能的实现
    // 所调用，这里做一个例外的直接测试。
    @Test
    public void testImportTemplateMethod() throws Exception {
        final String path = "src/test/resources/abc_first_upload_table.xls";
        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "xlsFile",
                "abc_first_upload_table.xls",
                MediaType.APPLICATION_XML_VALUE,
                Files.newInputStream(Paths.get(path)));

        // 通过reflection来得到需要被测试的private函数。
        Method importTemplateMethod =
                BaidieProcessor.class.getDeclaredMethod(
                        "importFromFileAndProcess",
                        MultipartFile.class,
                        Class.class,
                        String.class,
                        Map.class);
        importTemplateMethod.setAccessible(true);

        // 构造一个keyToCalculator Map来做任意基于List<Data5Entry>的计算。
        Map<String, Function<List<ABCAnalyseController.Data1Entry>, List<?>>> keyToCalculator =
                Map.ofEntries(
                        entry("inputSizeCalculator", // 返回一个大小为1的List，里面是输入的List的大小
                                data1Entries ->  List.of(data1Entries.size())),
                        entry("materialCodeCalculator",  // 返回一个跟输入大小一样的List，里面是输入的物料编码。
                                data1Entries -> data1Entries.stream()
                                        .map(ABCAnalyseController.Data1Entry::getMaterialCode)
                                        .collect(Collectors.toList())),
                        entry("firstAndLastCalculator",  // 返回第一个List,里面由输入的第一个和最后一个数据。
                                data1Entries ->
                                        Arrays.asList(
                                                data1Entries.get(0),
                                                data1Entries.get(data1Entries.size() - 1)))
        );
        Map<String, List<?>> result = (Map<String, List<?>>) importTemplateMethod.invoke(
                null,
                mockMultipartFile,
                ABCAnalyseController.Data1Entry.class,
                "inputKey",
                keyToCalculator
                );

        // 首先结果为 1 + keyToCalculator.size() 输入加所有的计算结果。
        assertEquals(keyToCalculator.size() + 1, result.size());

        // 检查calculator的结果: 输入数据有11行。
        assertEquals(1, result.get("inputSizeCalculator").size());
        assertEquals(11, result.get("inputSizeCalculator").get(0));

        // 检查第二个计算器的结果。
        assertEquals(11, result.get("materialCodeCalculator").size());

        // 检查第三个计算器的结果。
        assertEquals(2, result.get("firstAndLastCalculator").size());
    }
}