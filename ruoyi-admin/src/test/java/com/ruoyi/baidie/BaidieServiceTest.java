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

public class BaidieServiceTest {

    @Test(expected = IOException.class)
    public void importFileForGroupOne_ParseFailure() throws IOException {
            MockMultipartFile mockFile = new MockMultipartFile(
                    "wrong extension",
                    "filename.kml",
                    "text/plain",
                    "some kml".getBytes());
            try {
                BaidieService.importFileForGroupOne(mockFile);
            } catch (IOException e) {
                assertEquals(
                        e.getMessage(),
                        "Your InputStream was neither an OLE2 stream, nor an OOXML stream");
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
        Map<String, List<?>> resultMap = BaidieService.importFileForGroupOne(mockMultipartFile);

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
}