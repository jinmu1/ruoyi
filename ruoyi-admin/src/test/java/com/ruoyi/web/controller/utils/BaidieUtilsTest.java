package com.ruoyi.web.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.data.abc.MaterialBasicInfo;
import com.ruoyi.data.abc.OrderMaterialInfo;
import com.ruoyi.data.common.ObjectMap;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruoyi.web.controller.utils.BaidieUtils.toPercentageOfString;
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
                    mockFile, MaterialBasicInfo.class);
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
        List<MaterialBasicInfo> result =
                BaidieUtils.parseFromExcelFile(mockMultipartFile, MaterialBasicInfo.class);

        assertEquals(result.size(), 11);
    }


    // 测试将List对像转换为对应的Json对象.
    @Test
    public void testGenerateResponseJson() throws JsonProcessingException {
        // 准备测试数据
        HashMap<String, List<?>> jsonKeyToValue = new HashMap<>();
        List<MaterialBasicInfo> list1 = new ArrayList<>();
        list1.add(new MaterialBasicInfo());
        List<OrderMaterialInfo> list2 = new ArrayList<>();
        list2.add(new OrderMaterialInfo());
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

    @Test
    public void testToPercentageOfString() {
        assertEquals("0.00%", toPercentageOfString(0, 90));
        assertEquals("1.00%", toPercentageOfString(1, 100));
        assertEquals("33.46%", toPercentageOfString(33.4567, 100));
        assertEquals("33.45%", toPercentageOfString(33.4512, 100));
    }
    // 测试将List对像转换为对应的Json对象.
    @Test
    public void testGenerateIntervalData() throws JsonProcessingException {
        // 准备测试数据
        double[] data = {1.2, 42.3, 43.5, 34.5, 5.6, 26.7, 7.8, 18.9, 19.0, 10.1, 50};

        // 调用方法生成 JSON
        List<ObjectMap> objectMaps = BaidieUtils.generateIntervalData(data, 5);
        List<String> str = Arrays.asList("[1, 11)", "[11, 21)", "[21, 31)", "[31, 41)", "[41, 51)");
        List<Integer> testIKIntervalNumber=Arrays.asList(4, 2, 1, 1, 3);;
        // 验证生成的 JSON 是否符合预期
        assertEquals(str,
                objectMaps.stream()
                        .map(ObjectMap::getKey)
                        .collect(Collectors.toList()));
        assertEquals(testIKIntervalNumber,
                objectMaps.stream()
                        .map(ObjectMap::getValue)
                        .collect(Collectors.toList()));
    }
}