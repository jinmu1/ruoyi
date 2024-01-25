package com.ruoyi.web.controller.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.eiq.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/***
 * @jinmu
 * 该controller是将导入的excel表格转换为基本数据，然后在数据上分别得到EIQ的数据表格，订单分析、订单行分析
 */
@Controller
@RequestMapping("/system/eiq")
public class EIQAnalyseController {
    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");


    /**
     * eiq分析的导入数据,然后将基本的数据对订单数量、订单行数、物料数量、等数据做转换统计
     */
    @PostMapping("/importStaticData")
    @ResponseBody
    public AjaxResult importStaticData(HttpServletRequest request) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * data1Entries:用户导入的数据对象
         * data1Entries >data6Entries 转换后的数据
         */
        String rootPath =
               getClass().getResource("/static/file/eiq1.json").getPath();;


        String filePath =rootPath;
        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        List<BasicTable>  data7Entries = new ArrayList<>();
        try {
            /**
             * 获取老师给的数据，将数据计算后转换位对象Data2Entry
             */
            // 读取整个JSON文件为JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            // 获取 "data1" 部分
            JsonNode data1Node = rootNode.get("data1");
            // 将 "data1" 转换为数组
            BasicTable[] data7Entrie = objectMapper.treeToValue(data1Node, BasicTable[].class);
            data7Entries = Arrays.asList(data7Entrie.clone());
            /***
             * 将转换后的数据穿给Json对象
             */
        }catch (Exception e){

        }
        List<EIQBasicTable> data1Entries = new ArrayList<>();
        for(BasicTable basicTable :data7Entries){
            EIQBasicTable EIQBasicTable = new EIQBasicTable();
            EIQBasicTable.setDeliveryDate(sdf1.parse(basicTable.getDeliveryDate()));
            EIQBasicTable.setConversionUnit(basicTable.getConversionUnit());
            EIQBasicTable.setMaterialName(basicTable.getMaterialName());
            EIQBasicTable.setOrderNumber(basicTable.getOrderNumber());
            EIQBasicTable.setUnitPrice(basicTable.getUnitPrice());
            EIQBasicTable.setPalletizedItems(basicTable.getPalletizedItems());
            EIQBasicTable.setConversionUnit1(basicTable.getConversionUnit1());
            EIQBasicTable.setDeliveryQuantity(basicTable.getDeliveryQuantity());
            EIQBasicTable.setMaterialNumber(basicTable.getMaterialNumber());
            EIQBasicTable.setDeliveryUnit(basicTable.getDeliveryUnit());
            data1Entries.add(EIQBasicTable);
        }
        List<EIQAnalysisTable> data2Entries = getData2(data1Entries);
        List<ENAnalysisTable> data3Entries = getData3(data1Entries);
        List<EQAnalysisTable> data4Entries = getData4(data1Entries);
        List<EIAnalysisTable> data5Entries = getData5(data1Entries);
        List<IKAnalysisTable> data6Entries = getData6(data1Entries);

        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data1", praseJson1(data1Entries)); // 将导入的数据放到data1中
        json.put("data2", praseJson2(data2Entries)); // 将转换后的数据放到data2中
        json.put("data3", praseJson3(data3Entries)); // 将转换后的数据放到data3中
        json.put("data4", praseJson4(data4Entries)); // 将转换后的数据放到data4中
        json.put("data5", praseJson5(data5Entries)); // 将转换后的数据放到data5中
        json.put("data6", praseJson6(data6Entries)); // 将转换后的数据放到data6中
        return AjaxResult.success(json);
    }

    /**
     * eiq分析的导入数据,然后将基本的数据对订单数量、订单行数、物料数量、等数据做转换统计
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * data1Entries:用户导入的数据对象
         * data1Entries >data6Entries 转换后的数据
         */
        ExcelUtil<EIQBasicTable> util = new ExcelUtil<>(EIQBasicTable.class);
        List<EIQBasicTable> data1Entries = util.importExcel(file.getInputStream());
        List<EIQAnalysisTable> data2Entries= getData2(data1Entries);
        List<ENAnalysisTable> data3Entries= getData3(data1Entries);
        List<EQAnalysisTable> data4Entries= getData4(data1Entries);
        List<EIAnalysisTable> data5Entries= getData5(data1Entries);
        List<IKAnalysisTable> data6Entries= getData6(data1Entries);

        /***
         * 将转换后的数据穿给Json对象
         */
        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data1", praseJson1(data1Entries)); // 将导入的数据放到data1中
        json.put("data2", praseJson2(data2Entries)); // 将转换后的数据放到data2中
        json.put("data3", praseJson3(data3Entries)); // 将转换后的数据放到data3中
        json.put("data4", praseJson4(data4Entries)); // 将转换后的数据放到data4中
        json.put("data5", praseJson5(data5Entries)); // 将转换后的数据放到data5中
        json.put("data6", praseJson6(data6Entries)); // 将转换后的数据放到data6中
        return AjaxResult.success(json);
    }

    /***
     * 将导入的数据转换为EIQ单项要素分析数据统计表(data2)
     * @param data1Entries
     * @return
     */
    private   List<EIQAnalysisTable> getData2(List<EIQBasicTable> data1Entries){
      // 将 "data1" 转换为数组
      Map<Date, List<EIQBasicTable>> categorizedMap = data1Entries.stream()
              .collect(Collectors.groupingBy(EIQBasicTable::getDeliveryDate));
      List<EIQAnalysisTable> EIQAnalysisTables = new ArrayList<>();
      for(Date key : categorizedMap.keySet()){
          EIQAnalysisTable EIQAnalysisTable = new EIQAnalysisTable();
          EIQAnalysisTable.setDate(sdf.format(key));
          List<EIQBasicTable> EIQBasicTables = categorizedMap.get(key);
          Map<String, List<EIQBasicTable>> collect = EIQBasicTables.stream()
                  .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
          EIQAnalysisTable.seteAnalysis(collect.size());//查看订单数量
          EIQAnalysisTable.setnAnalysis(EIQBasicTables.size());
          double sum = 0;
          // 遍历列表，累加 amount 属性值
          for (EIQBasicTable dataItem : EIQBasicTables) {
              sum += dataItem.getDeliveryQuantity();
          }
          EIQAnalysisTable.setqAnalysis(sum);
          EIQAnalysisTables.add(EIQAnalysisTable);
      }
      Collections.sort(EIQAnalysisTables,(m1, m2) -> Double.compare(m2.geteAnalysis(),m1.geteAnalysis()));//按照出库频次降序排序
        return EIQAnalysisTables;
    }
    /***
     * 将导入的数据转换为EN综合分析数据统计表(data3)
     * @param data1Entries
     * @return
     */
    private   List<ENAnalysisTable> getData3(List<EIQBasicTable> data1Entries){
        Map<String, List<EIQBasicTable>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
        List<ENAnalysisTable> ENAnalysisTables = new ArrayList<>();

        for(String key : categorizedMap1.keySet()){
            ENAnalysisTable ENAnalysisTable = new ENAnalysisTable();

            ENAnalysisTable.setOrderNumber(key);
            ENAnalysisTable.setOrderLineCount(categorizedMap1.get(key).size());
            ENAnalysisTables.add(ENAnalysisTable);
        }
        Collections.sort(ENAnalysisTables,(m1, m2) -> Double.compare(m2.getOrderLineCount(),m1.getOrderLineCount()));//按照出库频次降序排序
        int k1 = 0;
        for( ENAnalysisTable ENAnalysisTable : ENAnalysisTables) {
            k1++;
            ENAnalysisTable.setCumulativeItemNumber(k1);
        }
        return ENAnalysisTables;
    }
    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data4)
     * @param data1Entries
     * @return
     */
    private   List<EQAnalysisTable> getData4(List<EIQBasicTable> data1Entries){
        Map<String, List<EIQBasicTable>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
        List<EQAnalysisTable> EQAnalysisTables = new ArrayList<>();

        for(String key : categorizedMap1.keySet()){

            EQAnalysisTable EQAnalysisTable = new EQAnalysisTable();
            EQAnalysisTable.setOrderNumber(key);

            double sum = 0;
            // 遍历列表，累加 amount 属性值
            for (EIQBasicTable dataItem : categorizedMap1.get(key)) {
                sum += dataItem.getDeliveryQuantity();
            }
            EQAnalysisTable.setTotalDeliveredQuantity(sum);
            EQAnalysisTables.add(EQAnalysisTable);
        }
        Collections.sort(EQAnalysisTables,(m1, m2) -> Double.compare(m2.getTotalDeliveredQuantity(),m1.getTotalDeliveredQuantity()));//按照出库频次降序排序
        int k1 = 0;
        for( EQAnalysisTable EQAnalysisTable : EQAnalysisTables) {
            k1++;
            EQAnalysisTable.setCumulativeItemNumber(k1);
        }
        return EQAnalysisTables;
    }
    /***
     * 将导入的数据转换为EI综合分析数据统计表(data5)
     * @param data1Entries
     * @return
     */
    private   List<EIAnalysisTable> getData5(List<EIQBasicTable> data1Entries){
        Map<String, List<EIQBasicTable>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getOrderNumber));
        List<EIAnalysisTable> EIAnalysisTables = new ArrayList<>();
        for(String key : categorizedMap1.keySet()){
            EIAnalysisTable EIAnalysisTable = new EIAnalysisTable();
            EIAnalysisTable.setOrderNumber(key);
            EIAnalysisTable.setMaterialVarietiesCount(categorizedMap1.get(key).size());
            EIAnalysisTables.add(EIAnalysisTable);
        }
        Collections.sort(EIAnalysisTables,(m1, m2) -> Double.compare(m2.getMaterialVarietiesCount(),m1.getMaterialVarietiesCount()));//按照出库频次降序排序
        int k1 = 0;
        for( EIAnalysisTable EIAnalysisTable : EIAnalysisTables) {
            k1++;
            EIAnalysisTable.setCumulativeItemNumber(k1);
        }
        return EIAnalysisTables;
    }
    /***
     * 将导入的数据转换为IK综合分析数据统计表(data6)
     * @param data1Entries
     * @return
     */
    private   List<IKAnalysisTable> getData6(List<EIQBasicTable> data1Entries){
        List<IKAnalysisTable> IKAnalysisTables = new ArrayList<>();
        Map<String, List<EIQBasicTable>> categorizedMap2 = data1Entries.stream()
                .collect(Collectors.groupingBy(EIQBasicTable::getMaterialNumber));
        for(String key : categorizedMap2.keySet()){
           IKAnalysisTable IKAnalysisTable = new IKAnalysisTable();
            IKAnalysisTable.setMaterialCode(key);
            IKAnalysisTable.setMaterialName(categorizedMap2.get(key).get(0).getMaterialName());
            IKAnalysisTable.setOccurrenceCount(categorizedMap2.get(key).size());
            IKAnalysisTables.add(IKAnalysisTable);
        }
        Collections.sort(IKAnalysisTables,(m1, m2) -> Double.compare(m2.getOccurrenceCount(),m1.getOccurrenceCount()));//按照出库频次降序排序
        int k1 = 0;
        for( IKAnalysisTable IKAnalysisTable : IKAnalysisTables) {
            k1++;
            IKAnalysisTable.setCumulativeItemNumber(k1);
        }
        return IKAnalysisTables;
    }

    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson1(List<EIQBasicTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson2(List<EIQAnalysisTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson3(List<ENAnalysisTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson4(List<EQAnalysisTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson5(List<EIAnalysisTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson6(List<IKAnalysisTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }



}
