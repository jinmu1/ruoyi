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
import java.net.URL;
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
        List<Data7Item>  data7Entries = new ArrayList<>();
        try {
            /**
             * 获取老师给的数据，将数据计算后转换位对象Data2Entry
             */
            // 读取整个JSON文件为JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            // 获取 "data1" 部分
            JsonNode data1Node = rootNode.get("data1");
            // 将 "data1" 转换为数组
            Data7Item[] data7Entrie = objectMapper.treeToValue(data1Node, Data7Item[].class);
            data7Entries = Arrays.asList(data7Entrie.clone());
            /***
             * 将转换后的数据穿给Json对象
             */
        }catch (Exception e){

        }
        List<Data1Item> data1Entries = new ArrayList<>();
        for(Data7Item data7Item :data7Entries){
            Data1Item data1Item = new Data1Item();
            data1Item.setDeliveryDate(sdf1.parse(data7Item.getDeliveryDate()));
            data1Item.setConversionUnit(data7Item.getConversionUnit());
            data1Item.setMaterialName(data7Item.getMaterialName());
            data1Item.setOrderNumber(data7Item.getOrderNumber());
            data1Item.setUnitPrice(data7Item.getUnitPrice());
            data1Item.setPalletizedItems(data7Item.getPalletizedItems());
            data1Item.setConversionUnit1(data7Item.getConversionUnit1());
            data1Item.setDeliveryQuantity(data7Item.getDeliveryQuantity());
            data1Item.setMaterialNumber(data7Item.getMaterialNumber());
            data1Item.setDeliveryUnit(data7Item.getDeliveryUnit());
            data1Entries.add(data1Item);
        }
        List<Data2Item> data2Entries = getData2(data1Entries);
        List<Data3Item> data3Entries = getData3(data1Entries);
        List<Data4Item> data4Entries = getData4(data1Entries);
        List<Data5Item> data5Entries = getData5(data1Entries);
        List<Data6Item> data6Entries = getData6(data1Entries);

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
        ExcelUtil<Data1Item> util = new ExcelUtil<>(Data1Item.class);
        List<Data1Item> data1Entries = util.importExcel(file.getInputStream());
        List<Data2Item> data2Entries= getData2(data1Entries);
        List<Data3Item> data3Entries= getData3(data1Entries);
        List<Data4Item> data4Entries= getData4(data1Entries);
        List<Data5Item> data5Entries= getData5(data1Entries);
        List<Data6Item> data6Entries= getData6(data1Entries);

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
    private   List<Data2Item> getData2(List<Data1Item> data1Entries){
      // 将 "data1" 转换为数组
      Map<Date, List<Data1Item>> categorizedMap = data1Entries.stream()
              .collect(Collectors.groupingBy(Data1Item::getDeliveryDate));
      List<Data2Item> data2Items = new ArrayList<>();
      for(Date key : categorizedMap.keySet()){
          Data2Item data2Item= new Data2Item();
          data2Item.setDate(sdf.format(key));
          List<Data1Item> data1Items = categorizedMap.get(key);
          Map<String, List<Data1Item>> collect = data1Items.stream()
                  .collect(Collectors.groupingBy(Data1Item::getOrderNumber));
          data2Item.seteAnalysis(collect.size());//查看订单数量
          data2Item.setnAnalysis(data1Items.size());
          double sum = 0;
          // 遍历列表，累加 amount 属性值
          for (Data1Item dataItem : data1Items) {
              sum += dataItem.getDeliveryQuantity();
          }
          data2Item.setqAnalysis(sum);
          data2Items.add(data2Item);
      }
      Collections.sort(data2Items,(m1, m2) -> Double.compare(m2.geteAnalysis(),m1.geteAnalysis()));//按照出库频次降序排序
        return data2Items;
    }
    /***
     * 将导入的数据转换为EN综合分析数据统计表(data3)
     * @param data1Entries
     * @return
     */
    private   List<Data3Item> getData3(List<Data1Item> data1Entries){
        Map<String, List<Data1Item>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(Data1Item::getOrderNumber));
        List<Data3Item> data3Items = new ArrayList<>();

        for(String key : categorizedMap1.keySet()){
            Data3Item data3Item = new Data3Item();

            data3Item.setOrderNumber(key);
            data3Item.setOrderLineCount(categorizedMap1.get(key).size());
            data3Items.add(data3Item);
        }
        Collections.sort(data3Items,(m1, m2) -> Double.compare(m2.getOrderLineCount(),m1.getOrderLineCount()));//按照出库频次降序排序
        int k1 = 0;
        for( Data3Item data3Item : data3Items) {
            k1++;
            data3Item.setCumulativeItemNumber(k1);
        }
        return data3Items;
    }
    /***
     * 将导入的数据转换为EQ综合分析数据统计表(data4)
     * @param data1Entries
     * @return
     */
    private   List<Data4Item> getData4(List<Data1Item> data1Entries){
        Map<String, List<Data1Item>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(Data1Item::getOrderNumber));
        List<Data4Item> data4Items = new ArrayList<>();

        for(String key : categorizedMap1.keySet()){

            Data4Item data4Item = new Data4Item();
            data4Item.setOrderNumber(key);

            double sum = 0;
            // 遍历列表，累加 amount 属性值
            for (Data1Item dataItem : categorizedMap1.get(key)) {
                sum += dataItem.getDeliveryQuantity();
            }
            data4Item.setTotalDeliveredQuantity(sum);
            data4Items.add(data4Item);
        }
        Collections.sort(data4Items,(m1, m2) -> Double.compare(m2.getTotalDeliveredQuantity(),m1.getTotalDeliveredQuantity()));//按照出库频次降序排序
        int k1 = 0;
        for( Data4Item data4Item : data4Items) {
            k1++;
            data4Item.setCumulativeItemNumber(k1);
        }
        return data4Items;
    }
    /***
     * 将导入的数据转换为EI综合分析数据统计表(data5)
     * @param data1Entries
     * @return
     */
    private   List<Data5Item> getData5(List<Data1Item> data1Entries){
        Map<String, List<Data1Item>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(Data1Item::getOrderNumber));
        List<Data5Item> data5Items = new ArrayList<>();
        for(String key : categorizedMap1.keySet()){
            Data5Item data5Item = new Data5Item();
            data5Item.setOrderNumber(key);
            data5Item.setMaterialVarietiesCount(categorizedMap1.get(key).size());
            data5Items.add(data5Item);
        }
        Collections.sort(data5Items,(m1, m2) -> Double.compare(m2.getMaterialVarietiesCount(),m1.getMaterialVarietiesCount()));//按照出库频次降序排序
        int k1 = 0;
        for( Data5Item data5Item : data5Items) {
            k1++;
            data5Item.setCumulativeItemNumber(k1);
        }
        return data5Items;
    }
    /***
     * 将导入的数据转换为IK综合分析数据统计表(data6)
     * @param data1Entries
     * @return
     */
    private   List<Data6Item> getData6(List<Data1Item> data1Entries){
        List<Data6Item> data6Items = new ArrayList<>();
        Map<String, List<Data1Item>> categorizedMap2 = data1Entries.stream()
                .collect(Collectors.groupingBy(Data1Item::getMaterialNumber));
        for(String key : categorizedMap2.keySet()){
           Data6Item data6Item = new Data6Item();
            data6Item.setMaterialCode(key);
            data6Item.setMaterialName(categorizedMap2.get(key).get(0).getMaterialName());
            data6Item.setOccurrenceCount(categorizedMap2.get(key).size());
            data6Items.add(data6Item);
        }
        Collections.sort(data6Items,(m1, m2) -> Double.compare(m2.getOccurrenceCount(),m1.getOccurrenceCount()));//按照出库频次降序排序
        int k1 = 0;
        for( Data6Item data6Item : data6Items) {
            k1++;
            data6Item.setCumulativeItemNumber(k1);
        }
        return data6Items;
    }

    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson1(List<Data1Item> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson2(List<Data2Item> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson3(List<Data3Item> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson4(List<Data4Item> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson5(List<Data5Item> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson6(List<Data6Item> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }



}
