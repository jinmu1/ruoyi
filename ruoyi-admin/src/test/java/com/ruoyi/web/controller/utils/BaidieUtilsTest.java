package com.ruoyi.web.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.web.controller.system.ABCAnalyseController;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
}