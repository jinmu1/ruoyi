package com.ruoyi.baidie;

import com.google.common.collect.ImmutableMap;
import com.ruoyi.data.abc.MaterialBasicInfo;
import com.ruoyi.data.abc.OrderMaterialInfo;
import com.ruoyi.data.eiq.EIQBasicInfo;
import com.ruoyi.data.pcb.OutboundBasicInfo;
import com.ruoyi.web.controller.utils.BaidieUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class BaidieProcessor {
    // Error message for import file error.
    public static final String IMPORT_FAILURE_MSG = "导入文件失败，请检查格式或者模版是否正确";

    /**
     * Import and parse data from an uploaded file, and transforms the input data into
     * multiple array of data.
     *
     * @param importFile The uploaded file
     * @return a map from json key to its corresponding data arrays. This method will return
     * a map with two keys:
     * {
     * "data1":  [...],
     * "data2":  [...]
     * }
     */
    public static Map<String, List<?>> importABCGroupOne(MultipartFile importFile) throws IOException {
        return importFromFileAndProcess(
                importFile,
                MaterialBasicInfo.class,
                "data1",
                Map.of("data2", ABCClassifier::sortByAccumulativeValue));
    }

    /**
     * Import and parse data from an uploaded file, and transforms the input data into
     * multiple array of data.
     *
     * @param importFile The uploaded file
     * @return a map from json key to its corresponding data arrays. This method will return
     * a map with two keys:
     * {
     * "data3":  [...],
     * "data5":  [...]
     * }
     */
    public static Map<String, List<?>> importABCGroupTwo(MultipartFile importFile) throws IOException {
        return importFromFileAndProcess(
                importFile,
                OrderMaterialInfo.class,
                "data5",
                Map.of("data3", ABCClassifier::sortByOutboundFrequency));
    }

    /**
     * Import and parse data from an uploaded file, and transforms the input data into
     * multiple array of data.
     *
     * @param importFile The uploaded file
     * @return a map from json key to its corresponding data arrays. This method will return
     * a map with two keys:
     * {
     * "data4":  [...],
     * "data5":  [...]
     * }
     */
    public static Map<String, List<?>> importABCGroupThree(MultipartFile importFile) throws IOException {
        return importFromFileAndProcess(
                importFile,
                OrderMaterialInfo.class,
                "data5",
                Map.of("data4", ABCClassifier::sortByMaterialOutboundQuantity));
    }


    /**
     * 核心方法。用于所有基于一下逻辑的上传功能实现。
     * 上传-》阅读数据 -》N 个计算转换 -》 返回原始数据加上计算数据（总共1 + N条）
     *
     * @param inputFile       - 上传文件， 直接从Controller传进来
     * @param inputClazz      - inputFile里面数据的类型。 例如： EIBasicTable.class
     * @param inputDataKey    - 上传文件的数据在返回值里的key   'data1'
     * @param keyToCalculator - 传入一个存储计算函数的Map。  {'data2': ABCClassifier::sortByMaterialOutboundQuantity }
     * @return 将输入的原始数据以及根据原始数据计算出来的数据都以Map<String, List < ?>>的形式返回。
     * 原始数据的key是inputDataKey，每一个计算出来的数据的key是对应keyToProcessor里的每个函数自己的key。
     * @throws IOException 上传文件读入失败。
     *                     <p>
     *                     例子：
     *                     importTmpl(
     *                     importFile,
     *                     ABCAnalyseController.Data5Entry.class,
     *                     "data5",
     *                     Map.of("data4", ABCClassifier::sortByMaterialOutboundQuantity));
     *                     返回值
     *                     {
     *                     "data1": [EIBasicTable]，   // 从inputFile里读出来的数据
     *                     “data2": [...],            //  keyToCalculator['data2'].apply(input)的结果。
     *                     }
     */
    private static <InputType> Map<String, List<?>> importFromFileAndProcess(
            MultipartFile inputFile,
            Class<InputType> inputClazz,
            String inputDataKey,
            Map<String, Function<List<InputType>, List<?>>> keyToCalculator) throws IOException {
        try {
            final List<InputType> input =
                    BaidieUtils.parseFromExcelFile(inputFile, inputClazz);
            final Map<String, List<?>> derivedDataMap =
                    keyToCalculator.entrySet().stream().collect(
                            Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().apply(input)));
            final Map<String, List<?>> inputDataMap = Map.ofEntries(entry(inputDataKey, input));

            return ImmutableMap.<String, List<?>>builder()
                    .putAll(inputDataMap)
                    .putAll(derivedDataMap)
                    .build();
        } catch (Exception e) {
            throw new IOException(IMPORT_FAILURE_MSG);
        }
    }

    /**
     * 将EIQ的文件导入后转为对应的字段
     *
     * @param importFile
     * @return
     * @throws IOException
     */
    public static Map<String, List<?>> importEIQGroup(MultipartFile importFile) throws IOException {
        return importFromFileAndProcess(
                importFile,
                EIQBasicInfo.class,
                "data1",
                Map.of(
                        "data2", EIQClassifier::getEIQAnalysisTable,
                        "data3", EIQClassifier::getENAnalysisTable,
                        "data4", EIQClassifier::getEQAnalysisTable,
                        "data5", EIQClassifier::getEIAnalysisTable,
                        "data6", EIQClassifier::getIKAnalysisInfoTest,
                        "en_percentage_data", EIQClassifier::getENHistogram,
                        "eq_distribution_data", EIQClassifier::getEQHistogram,
                        "ei_percentage_data", EIQClassifier::getEIHistogram
                ));
    }

    /**
     * pcb的页面转换逻辑，将导入的文件转换为处理逻辑的结果
     *
     * @param importFile 导入的文件
     * @return
     */
    public static Map<String, List<?>> importPCBQGroup(MultipartFile importFile) throws IOException {
        return importFromFileAndProcess(
                importFile,
                OutboundBasicInfo.class,
                "data3",
                Map.of(
                        "data1", PCBClassifier::transformPackaging,
                        "data2", PCBClassifier::pickPackagingType
                ));
    }
}
