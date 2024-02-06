package com.ruoyi.baidie;

import com.ruoyi.data.eiq.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/****
 * EIQ 处理类：将EIQ分析的各项统计指标进行转换
 */
public class EIQClassifier {

    /***
     * 将导入的数据转换为IK综合分析数据统计表(data6)
     * 1.将数据按照物料编码分组
     * 2.统计分组后物料编码出现的次数
     * @param eiqBasicTableList
     * @return
     */
    public static List<IKAnalysisTable> getIKAnalysisTable(List<EIQBasicTable> eiqBasicTableList) {
        //step1: 首先将数据按物料编码重组
        final Map<String, List<EIQBasicTable>> dataGroupByMaterialNumber =
                eiqBasicTableList.stream().collect(
                        Collectors.groupingBy(EIQBasicTable::getMaterialNumber));

        //step2: 统计每个物料编码的物料行数的数据，然后降序序排序
        final List<IKAnalysisTable> analysisTableSortedByMaterialNumber =
                dataGroupByMaterialNumber.entrySet().stream()
                        .map(entry -> calculateMaterialInfo(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(IKAnalysisTable::getOccurrenceCount, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        //step3:设置一个自增的序号
        IntStream.range(0, analysisTableSortedByMaterialNumber.size())
                .forEach(i -> analysisTableSortedByMaterialNumber.get(i).setCumulativeItemNumber(i + 1));
        // 返回生成的 IKAnalysisTables 列表
        return analysisTableSortedByMaterialNumber;
    }

    /***
     * 处理后的基本数据
     * 将按照物料编码处理过的数据，统计物料出现的次数---行数
     * @param key 物料编码
     * @param eiqBasicTables 分组后的数据
     * @return 处理过的数据
     */
    private static IKAnalysisTable calculateMaterialInfo(String key,
                                                         List<EIQBasicTable> eiqBasicTables) {
        IKAnalysisTable ikAnalysisEntry = new IKAnalysisTable();
        ikAnalysisEntry.setMaterialCode(key);
        ikAnalysisEntry.setMaterialName(eiqBasicTables.get(0).getMaterialName());
        ikAnalysisEntry.setOccurrenceCount(eiqBasicTables.size());
        return ikAnalysisEntry;
    }
}
