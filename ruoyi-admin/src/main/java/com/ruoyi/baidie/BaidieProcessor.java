package com.ruoyi.baidie;

import com.ruoyi.web.controller.system.ABCAnalyseController;
import com.ruoyi.web.controller.utils.BaidieUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

            List<?> accumulativeValueSorted =
                    ABCClassifier.sortByAccumulativeValue(inventoryInfo);

            return Map.ofEntries(
                    entry("data1", inventoryInfo),
                    entry("data2", accumulativeValueSorted));
        } catch (Exception e) {
            throw new IOException(IMPORT_FAILURE_MSG);
        }
    }

    public static Map<String, List<?>> importABCGroupTwo(MultipartFile importFile) throws IOException {
        try {
            // 将导入的EXCEL文件转换为List对象，可能会throw exception.
            List<ABCAnalyseController.Data5Entry> data5Entries =
                    BaidieUtils.parseFromExcelFile(importFile, ABCAnalyseController.Data5Entry.class);

            // 使用流式操作和Collectors按照物料编码属性分类
            Map<String, List<ABCAnalyseController.Data5Entry>> categorizedMap = data5Entries.stream()
                    .collect(Collectors.groupingBy(ABCAnalyseController.Data5Entry::getMaterialNumber));

            // 输出分类结果
            List<ABCAnalyseController.Data3Entry> data3Entries = new ArrayList<>();
            int tatol = categorizedMap.size();//总物料数量
            int AllOutboundFrequency = 0;//总出库频次
            double AllShippedQuantity = 0.0;//总出库数量
            for (String key : categorizedMap.keySet()) {
                List<ABCAnalyseController.Data5Entry> data5EntryList1 = categorizedMap.get(key);
                ABCAnalyseController.Data3Entry data3Entry = new ABCAnalyseController.Data3Entry();
                data3Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
                int count = 0;//出库频次统计
                double num = 0.0;//出库数量统计
                for (ABCAnalyseController.Data5Entry data5Entry : data5EntryList1) {
                    count++;
                    num = num + data5Entry.getShippedQuantity();
                }
                AllOutboundFrequency += count;
                AllShippedQuantity += num;
                data3Entry.setOutboundFrequency(count);
                data3Entries.add(data3Entry);
            }
            Collections.sort(data3Entries, (m1, m2) -> Double.compare(m2.getOutboundFrequency(), m1.getOutboundFrequency()));//按照出库频次降序排序

            int frequency = 0;//累计频次
            int num1 = 0;
            for (ABCAnalyseController.Data3Entry data3Entry : data3Entries) {
                num1++;
                frequency += data3Entry.getOutboundFrequency();
                data3Entry.setCumulativeOutboundFrequency(frequency);
                data3Entry.setCumulativeOutboundFrequencyPercentage(new BigDecimal(frequency * 100 / AllOutboundFrequency).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
                data3Entry.setCumulativeItemCount(num1);
                data3Entry.setCumulativeItemCountPercentage(new BigDecimal(num1 * 100 / tatol).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
            }

            return Map.ofEntries(
                    entry("data3", data3Entries),
                    entry("data5", data5Entries));
        } catch (Exception e) {
            throw new IOException(IMPORT_FAILURE_MSG);
        }
    }
}
