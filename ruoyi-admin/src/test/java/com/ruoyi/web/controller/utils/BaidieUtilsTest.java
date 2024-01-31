package com.ruoyi.web.controller.utils;

import com.ruoyi.web.controller.system.ABCAnalyseController;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
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
}