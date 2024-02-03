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

import static java.util.Map.entry;

public class BaidieService {
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
            List<ABCAnalyseController.Data1Entry> data1Entries =
                    BaidieUtils.parseFromExcelFile(importFile, ABCAnalyseController.Data1Entry.class);

            // 将 "data1" 转换为数组
            List<ABCAnalyseController.Data2Entry> list = new ArrayList<>();
            // 现在你可以使用data1Entries了
            //step1：首先计算总销售金额
            for (ABCAnalyseController.Data1Entry entry : data1Entries) {
                ABCAnalyseController.Data2Entry data2Entry = new ABCAnalyseController.Data2Entry();
                data2Entry.setMaterialCode(entry.getMaterialCode());
                data2Entry.setUnitPrice(entry.getSellingPrice());
                data2Entry.setAverageInventory(entry.getAverageInventory());
                data2Entry.setAverageFundsOccupied(entry.getSellingPrice() * entry.getAverageInventory());
                list.add(data2Entry);
            }
            //step2：对总销售金额进行降序排序
            Collections.sort(list, (m1, m2) -> Double.compare(m2.getAverageFundsOccupied(), m1.getAverageFundsOccupied()));
            //step3：对降序排序后的占比进行计算，下面都是计算过程
            double occupied = 0;
            int next = 0;
            double allInventory = 0d;
            int all = 0;
            /**
             * all：统计总数据条数
             * allInventory：统计数据总的库存金额
             */
            for (ABCAnalyseController.Data2Entry data2Entry : list) {
                all = all + 1;
                allInventory = allInventory + data2Entry.getAverageFundsOccupied();
            }
            for (ABCAnalyseController.Data2Entry data2Entry : list) {
                occupied = occupied + data2Entry.getAverageFundsOccupied();
                data2Entry.setCumulativeAverageFundsOccupied(occupied);
                data2Entry.setCumulativeAverageFundsOccupiedPercentage(new BigDecimal(occupied * 100 / allInventory).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
                next = next + 1;
                data2Entry.setCumulativeItemNumber(next);
                data2Entry.setCumulativeItemNumberPercentage(new BigDecimal(next * 100 / all).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
            }
            return Map.ofEntries(
                    entry("data1", data1Entries),
                    entry("data2", list));
        } catch (Exception e) {
            throw new IOException(IMPORT_FAILURE_MSG);
        }
    }
}
