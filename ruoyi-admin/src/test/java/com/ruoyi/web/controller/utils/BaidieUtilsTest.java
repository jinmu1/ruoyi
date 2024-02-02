package com.ruoyi.web.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.web.controller.system.ABCAnalyseController;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
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
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<String> list2 = new ArrayList<>();
        list2.add("a");
        list2.add("b");
        jsonKeyToValue.put("key1", list1);
        jsonKeyToValue.put("key2", list2);

        // 调用方法生成 JSON
        JSONObject jsonObject = BaidieUtils.generateResponseJson(jsonKeyToValue);

        // 验证生成的 JSON 是否符合预期
        assertEquals("{\"key1\":\"[1,2]\",\"key2\":\"[a,b]\"}", jsonObject.toString());
    }
}