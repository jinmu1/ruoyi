package com.ruoyi.baidie;

import com.google.common.collect.ImmutableSet;
import com.ruoyi.web.controller.system.ABCAnalyseController;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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
            BaidieProcessor.importFileForGroupOne(mockFile);
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
        Map<String, List<?>> resultMap = BaidieProcessor.importFileForGroupOne(mockMultipartFile);

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

        // Test that the result keyset is data1 and data2.
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
}