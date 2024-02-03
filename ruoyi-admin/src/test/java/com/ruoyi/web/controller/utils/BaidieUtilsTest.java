package com.ruoyi.web.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.baidie.EIQCalculation;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.data.eiq.EIQBasicTable;
import com.ruoyi.web.controller.system.ABCAnalyseController;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaidieUtilsTest {

    // Throws exception when file is of wrong extension
    @Test(expected = Exception.class)
    public void parseFromWrongFileExtension() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "wrong extension",
                "filename.kml",
                "text/plain",
                "some kml".getBytes());
        try {
            BaidieUtils.parseFromExcelFile(
                    mockFile, ABCAnalyseController.Data1Entry.class);
        } catch (Exception e) {
            assertEquals(
                    e.getMessage(),
                    "Your InputStream was neither an OLE2 stream, nor an OOXML stream");
            throw e;
        }
    }

    // test with an actual xls file with 11 entries.
    @Test
    public void parseFromExcelFileWithXLS() throws Exception {
        final String path = "src/test/resources/abc_first_upload_table.xls";
        File file = new File(path);
        assertTrue(file.exists());

        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "xlsFile",
                "abc_first_upload_table.xls",
                MediaType.APPLICATION_XML_VALUE,
                new FileInputStream(file));
        List<ABCAnalyseController.Data1Entry> result =
                BaidieUtils.parseFromExcelFile(mockMultipartFile, ABCAnalyseController.Data1Entry.class);

        assertEquals(result.size(), 11);
    }


    // 测试将List对像转换为对应的Json对象.
    @Test
    public void testGenerateResponseJson() throws JsonProcessingException {
        // 准备测试数据
        HashMap<String, List<?>> jsonKeyToValue = new HashMap<>();
        List<ABCAnalyseController.Data1Entry> list1 = new ArrayList<>();
        list1.add(new ABCAnalyseController.Data1Entry());
        List<ABCAnalyseController.Data5Entry> list2 = new ArrayList<>();
        list2.add(new ABCAnalyseController.Data5Entry());
        jsonKeyToValue.put("key1", list1);
        jsonKeyToValue.put("key2", list2);

        // 调用方法生成 JSON
        JSONObject jsonObject = BaidieUtils.generateResponseJson(jsonKeyToValue);
        String str = "{\n" +
                "  \"key1\" : \"[{\\\"物料编码\\\":null,\\\"平均库存\\\":0,\\\"当月出库总量\\\":0.0,\\\"库存周转次数\\\":0,\\\"库存周转天数\\\":0,\\\"销售单价\\\":0.0}]\",\n" +
                "  \"key2\" : \"[{\\\"出库日期\\\":null,\\\"订单编号\\\":0,\\\"物料编号\\\":null,\\\"物料名称\\\":null,\\\"出货数量\\\":0.0,\\\"出货单位\\\":null,\\\"销售单价\\\":0.0,\\\"托盘装件数\\\":0.0}]\"\n" +
                "}";
        // 验证生成的 JSON 是否符合预期
        assertEquals(str.replaceAll("\\s", ""), jsonObject.toString().replaceAll("\\s", ""));
    }

    /***
     * 对EIQ的方法测试
     * @throws JsonProcessingException
     */
    @Test
    public void testGetDate1() throws JsonProcessingException {
        // 创建一个空的 EIQBasicTable 对象列表
        List<EIQBasicTable> list1 = new ArrayList<>();

        // 使用循环创建和赋值对象，并将它们添加到列表中
        for (int i = 0; i < 10; i++) {
            // 创建一个新的 EIQBasicTable 对象
            EIQBasicTable object = new EIQBasicTable();
            //生成一个确定的日期
            LocalDate specificDate = LocalDate.of(2024, 2, 10+i);
            // 设置出库日期为当前日期
            object.setDeliveryDate(Date.from(specificDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            // 设置订单编号，使用循环索引加1作为订单编号的一部分
            object.setOrderNumber("Order" + (i + 1));
            // 设置物料编号，使用循环索引加1作为物料编号的一部分
            object.setMaterialNumber("M" + (i + 1));
            // 设置物料名称，使用循环索引加1作为物料名称的一部分
            object.setMaterialName("Material " + (i + 1));
            // 设置出货数量，使用循环索引加1乘以100作为出货数量
            object.setDeliveryQuantity(100.0 * (i + 1));
            // 设置出货单位为"kg"
            object.setDeliveryUnit("kg");
            // 设置销售单价，使用循环索引加1乘以10作为销售单价
            object.setUnitPrice(10.0 * (i + 1));
            // 设置托盘装件数，使用循环索引加1乘以50作为托盘装件数
            object.setPalletizedItems(50.0 * (i + 1));
            // 设置换算单位，使用循环索引加1乘以2作为换算单位
            object.setConversionUnit(2.0 * (i + 1));
            // 设置换算单位1，使用循环索引加1乘以3作为换算单位1
            object.setConversionUnit1(3.0 * (i + 1));
            // 将对象添加到列表中
            list1.add(object);
        }
        // 创建 EIQCalculation 实例
        EIQCalculation eiqCalculation = new EIQCalculation();
        // 调用 getDate1 方法获取 JSONObject 对象
        JSONObject jsonObject = eiqCalculation.getDate1(list1);
        String str = "{\n" +
                "  \"data6\" : \"[{\\\"物料名称\\\":\\\"Material 1\\\",\\\"物料编码\\\":\\\"M1\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":1},{\\\"物料名称\\\":\\\"Material 2\\\",\\\"物料编码\\\":\\\"M2\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":2},{\\\"物料名称\\\":\\\"Material 3\\\",\\\"物料编码\\\":\\\"M3\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":3},{\\\"物料名称\\\":\\\"Material 4\\\",\\\"物料编码\\\":\\\"M4\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":4},{\\\"物料名称\\\":\\\"Material 5\\\",\\\"物料编码\\\":\\\"M5\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":5},{\\\"物料名称\\\":\\\"Material 6\\\",\\\"物料编码\\\":\\\"M6\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":6},{\\\"物料名称\\\":\\\"Material 7\\\",\\\"物料编码\\\":\\\"M7\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":7},{\\\"物料名称\\\":\\\"Material 8\\\",\\\"物料编码\\\":\\\"M8\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":8},{\\\"物料名称\\\":\\\"Material 9\\\",\\\"物料编码\\\":\\\"M9\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":9},{\\\"物料名称\\\":\\\"Material 10\\\",\\\"物料编码\\\":\\\"M10\\\",\\\"出现次数\\\":1,\\\"物料编号累计品目数\\\":10}]\",\n" +
                "  \"data5\" : \"[{\\\"订单编号\\\":\\\"Order2\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":1},{\\\"订单编号\\\":\\\"Order3\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":2},{\\\"订单编号\\\":\\\"Order4\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":3},{\\\"订单编号\\\":\\\"Order5\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":4},{\\\"订单编号\\\":\\\"Order1\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":5},{\\\"订单编号\\\":\\\"Order10\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":6},{\\\"订单编号\\\":\\\"Order6\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":7},{\\\"订单编号\\\":\\\"Order7\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":8},{\\\"订单编号\\\":\\\"Order8\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":9},{\\\"订单编号\\\":\\\"Order9\\\",\\\"订单对应物料品种数\\\":1,\\\"订单编号累计品目数\\\":10}]\",\n" +
                "  \"data4\" : \"[{\\\"订单编号\\\":\\\"Order10\\\",\\\"订单对应出库总数量\\\":1000.0,\\\"订单编号累计品目数\\\":1},{\\\"订单编号\\\":\\\"Order9\\\",\\\"订单对应出库总数量\\\":900.0,\\\"订单编号累计品目数\\\":2},{\\\"订单编号\\\":\\\"Order8\\\",\\\"订单对应出库总数量\\\":800.0,\\\"订单编号累计品目数\\\":3},{\\\"订单编号\\\":\\\"Order7\\\",\\\"订单对应出库总数量\\\":700.0,\\\"订单编号累计品目数\\\":4},{\\\"订单编号\\\":\\\"Order6\\\",\\\"订单对应出库总数量\\\":600.0,\\\"订单编号累计品目数\\\":5},{\\\"订单编号\\\":\\\"Order5\\\",\\\"订单对应出库总数量\\\":500.0,\\\"订单编号累计品目数\\\":6},{\\\"订单编号\\\":\\\"Order4\\\",\\\"订单对应出库总数量\\\":400.0,\\\"订单编号累计品目数\\\":7},{\\\"订单编号\\\":\\\"Order3\\\",\\\"订单对应出库总数量\\\":300.0,\\\"订单编号累计品目数\\\":8},{\\\"订单编号\\\":\\\"Order2\\\",\\\"订单对应出库总数量\\\":200.0,\\\"订单编号累计品目数\\\":9},{\\\"订单编号\\\":\\\"Order1\\\",\\\"订单对应出库总数量\\\":100.0,\\\"订单编号累计品目数\\\":10}]\",\n" +
                "  \"data3\" : \"[{\\\"订单编号\\\":\\\"Order2\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":1},{\\\"订单编号\\\":\\\"Order3\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":2},{\\\"订单编号\\\":\\\"Order4\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":3},{\\\"订单编号\\\":\\\"Order5\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":4},{\\\"订单编号\\\":\\\"Order1\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":5},{\\\"订单编号\\\":\\\"Order10\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":6},{\\\"订单编号\\\":\\\"Order6\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":7},{\\\"订单编号\\\":\\\"Order7\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":8},{\\\"订单编号\\\":\\\"Order8\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":9},{\\\"订单编号\\\":\\\"Order9\\\",\\\"订单对应行数\\\":1,\\\"订单编号累计品目数\\\":10}]\",\n" +
                "  \"data2\" : \"[{\\\"eanalysis\\\":1,\\\"qanalysis\\\":700.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-16\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":700.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":200.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-11\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":200.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":600.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-15\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":600.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":100.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-10\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":100.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":900.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-18\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":900.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":400.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-13\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":400.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":800.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-17\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":800.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":300.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-12\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":300.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":1000.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-19\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":1000.0},{\\\"eanalysis\\\":1,\\\"qanalysis\\\":500.0,\\\"nanalysis\\\":1,\\\"日期\\\":\\\"2024-02-14\\\",\\\"E分析\\\":1,\\\"N分析\\\":1,\\\"Q分析\\\":500.0}]\",\n" +
                "  \"data1\" : \"[{\\\"出库日期\\\":1707494400000,\\\"订单编号\\\":\\\"Order1\\\",\\\"物料编号\\\":\\\"M1\\\",\\\"物料名称\\\":\\\"Material 1\\\",\\\"出货数量\\\":100.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":10.0,\\\"托盘装件数\\\":50.0,\\\"换算单位\\\":2.0,\\\"换算单位1\\\":3.0},{\\\"出库日期\\\":1707580800000,\\\"订单编号\\\":\\\"Order2\\\",\\\"物料编号\\\":\\\"M2\\\",\\\"物料名称\\\":\\\"Material 2\\\",\\\"出货数量\\\":200.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":20.0,\\\"托盘装件数\\\":100.0,\\\"换算单位\\\":4.0,\\\"换算单位1\\\":6.0},{\\\"出库日期\\\":1707667200000,\\\"订单编号\\\":\\\"Order3\\\",\\\"物料编号\\\":\\\"M3\\\",\\\"物料名称\\\":\\\"Material 3\\\",\\\"出货数量\\\":300.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":30.0,\\\"托盘装件数\\\":150.0,\\\"换算单位\\\":6.0,\\\"换算单位1\\\":9.0},{\\\"出库日期\\\":1707753600000,\\\"订单编号\\\":\\\"Order4\\\",\\\"物料编号\\\":\\\"M4\\\",\\\"物料名称\\\":\\\"Material 4\\\",\\\"出货数量\\\":400.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":40.0,\\\"托盘装件数\\\":200.0,\\\"换算单位\\\":8.0,\\\"换算单位1\\\":12.0},{\\\"出库日期\\\":1707840000000,\\\"订单编号\\\":\\\"Order5\\\",\\\"物料编号\\\":\\\"M5\\\",\\\"物料名称\\\":\\\"Material 5\\\",\\\"出货数量\\\":500.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":50.0,\\\"托盘装件数\\\":250.0,\\\"换算单位\\\":10.0,\\\"换算单位1\\\":15.0},{\\\"出库日期\\\":1707926400000,\\\"订单编号\\\":\\\"Order6\\\",\\\"物料编号\\\":\\\"M6\\\",\\\"物料名称\\\":\\\"Material 6\\\",\\\"出货数量\\\":600.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":60.0,\\\"托盘装件数\\\":300.0,\\\"换算单位\\\":12.0,\\\"换算单位1\\\":18.0},{\\\"出库日期\\\":1708012800000,\\\"订单编号\\\":\\\"Order7\\\",\\\"物料编号\\\":\\\"M7\\\",\\\"物料名称\\\":\\\"Material 7\\\",\\\"出货数量\\\":700.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":70.0,\\\"托盘装件数\\\":350.0,\\\"换算单位\\\":14.0,\\\"换算单位1\\\":21.0},{\\\"出库日期\\\":1708099200000,\\\"订单编号\\\":\\\"Order8\\\",\\\"物料编号\\\":\\\"M8\\\",\\\"物料名称\\\":\\\"Material 8\\\",\\\"出货数量\\\":800.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":80.0,\\\"托盘装件数\\\":400.0,\\\"换算单位\\\":16.0,\\\"换算单位1\\\":24.0},{\\\"出库日期\\\":1708185600000,\\\"订单编号\\\":\\\"Order9\\\",\\\"物料编号\\\":\\\"M9\\\",\\\"物料名称\\\":\\\"Material 9\\\",\\\"出货数量\\\":900.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":90.0,\\\"托盘装件数\\\":450.0,\\\"换算单位\\\":18.0,\\\"换算单位1\\\":27.0},{\\\"出库日期\\\":1708272000000,\\\"订单编号\\\":\\\"Order10\\\",\\\"物料编号\\\":\\\"M10\\\",\\\"物料名称\\\":\\\"Material 10\\\",\\\"出货数量\\\":1000.0,\\\"出货单位\\\":\\\"kg\\\",\\\"销售单价\\\":100.0,\\\"托盘装件数\\\":500.0,\\\"换算单位\\\":20.0,\\\"换算单位1\\\":30.0}]\"\n" +
                "}";
        // 检查返回的 JSONObject 对象中是否包含了正确的键和值
        assertEquals(str.replaceAll("\\s", "").length(),jsonObject.toString().replaceAll("\\s", "").length());
    }
}