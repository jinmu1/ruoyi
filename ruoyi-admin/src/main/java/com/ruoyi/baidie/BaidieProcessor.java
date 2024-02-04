package com.ruoyi.baidie;

import com.ruoyi.web.controller.system.ABCAnalyseController;
import com.ruoyi.web.controller.utils.BaidieUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class BaidieProcessor {
    // Error message for import file error.
    public static final String IMPORT_FAILURE_MSG = "导入文件失败，请检查格式或者模版是否正确";

    /** Import and parse data from an uploaded file, and transforms the input data into
     *  multiple array of data.
     *
     * @param importFile The uploaded file
     * @return a map from json key to its corresponding data arrays. This method will return
     *         a map with two keys:
     *         {
     *             "data1":  [...],
     *             "data2":  [...]
     *         }
     */
    public static Map<String, List<?>> importFileForGroupOne(MultipartFile importFile) throws IOException {
        try {
            // 将导入的EXCEL文件转换为List对象，可能会throw exception.
            List<ABCAnalyseController.Data1Entry> inventoryInfo =
                    BaidieUtils.parseFromExcelFile(importFile, ABCAnalyseController.Data1Entry.class);

            List<ABCAnalyseController.Data2Entry> accumulativeValueSorted =
                    ABCClassifier.sortByAccumulativeValue(inventoryInfo);

            return Map.ofEntries(
                    entry("data1", inventoryInfo),
                    entry("data2", accumulativeValueSorted));
        } catch (Exception e) {
            throw new IOException(IMPORT_FAILURE_MSG);
        }
    }
}
