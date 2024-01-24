package com.ruoyi.web.controller.system;

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

    /**
     * ABC出库金额分析的导入数据
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * todo:返回导入和转换的错误参数
         */
        ExcelUtil<Data1Item> util = new ExcelUtil<>(Data1Item.class);
        List<Data1Item> data1Entries = util.importExcel(file.getInputStream());
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
        Map<String, List<Data1Item>> categorizedMap1 = data1Entries.stream()
                .collect(Collectors.groupingBy(Data1Item::getOrderNumber));
        List<Data3Item> data3Items = new ArrayList<>();
        int k = 0;
        for(String key : categorizedMap1.keySet()){
            k++;
            Data3Item data3Item = new Data3Item();
            data3Item.setCumulativeItemNumber(k);
            data3Item.setOrderNumber(key);
            data3Item.setOrderLineCount(categorizedMap1.get(key).size());
            data3Items.add(data3Item);
        }
        Collections.sort(data3Items,(m1, m2) -> Double.compare(m2.getOrderLineCount(),m1.getOrderLineCount()));//按照出库频次降序排序
        List<Data4Item> data4Items = new ArrayList<>();
        int k1 = 0;
        for(String key : categorizedMap1.keySet()){
            k1++;
            Data4Item data4Item = new Data4Item();
            data4Item.setOrderNumber(key);
            data4Item.setTotalDeliveredQuantity(k1);
            double sum = 0;
            // 遍历列表，累加 amount 属性值
            for (Data1Item dataItem : categorizedMap1.get(key)) {
                sum += dataItem.getDeliveryQuantity();
            }
            data4Item.setTotalDeliveredQuantity(sum);
            data4Items.add(data4Item);
        }
        Collections.sort(data4Items,(m1, m2) -> Double.compare(m2.getTotalDeliveredQuantity(),m1.getTotalDeliveredQuantity()));//按照出库频次降序排序
        List<Data5Item> data5Items = new ArrayList<>();
        int k2 = 0;
        for(String key : categorizedMap1.keySet()){
            k2++;
            Data5Item data5Item = new Data5Item();
            data5Item.setOrderNumber(key);
            data5Item.setCumulativeItemNumber(k2);
            data5Item.setMaterialVarietiesCount(categorizedMap1.get(key).size());
            data5Items.add(data5Item);
        }
        Collections.sort(data5Items,(m1, m2) -> Double.compare(m2.getMaterialVarietiesCount(),m1.getMaterialVarietiesCount()));//按照出库频次降序排序
        List<Data6Item> data6Items = new ArrayList<>();
        Map<String, List<Data1Item>> categorizedMap2 = data1Entries.stream()
                .collect(Collectors.groupingBy(Data1Item::getMaterialNumber));
        int k3=0;
        for(String key : categorizedMap2.keySet()){
            k3++;
            Data6Item data6Item = new Data6Item();
            data6Item.setMaterialCode(key);
            data6Item.setMaterialName(categorizedMap2.get(key).get(0).getMaterialName());
            data6Item.setCumulativeItemNumber(k3);
            data6Item.setOccurrenceCount(categorizedMap2.get(key).size());
            data6Items.add(data6Item);
        }
        Collections.sort(data6Items,(m1, m2) -> Double.compare(m2.getOccurrenceCount(),m1.getOccurrenceCount()));//按照出库频次降序排序
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(data1Entries);
        ObjectMapper objectMapper2 = new ObjectMapper();
        String jsonString2 = objectMapper2.writeValueAsString(data2Items);
        ObjectMapper objectMapper3 = new ObjectMapper();
        String jsonString3 = objectMapper3.writeValueAsString(data3Items);
        ObjectMapper objectMapper4 = new ObjectMapper();
        String jsonString4 = objectMapper4.writeValueAsString(data4Items);
        ObjectMapper objectMapper5 = new ObjectMapper();
        String jsonString5 = objectMapper5.writeValueAsString(data5Items);
        ObjectMapper objectMapper6 = new ObjectMapper();
        String jsonString6 = objectMapper6.writeValueAsString(data6Items);
        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data1", jsonString); // 将导入的数据放到data1中
        json.put("data2", jsonString2); // 将转换后的数据放到data2中
        json.put("data3", jsonString3); // 将转换后的数据放到data2中
        json.put("data4", jsonString4); // 将转换后的数据放到data2中
        json.put("data5", jsonString5); // 将转换后的数据放到data2中
        json.put("data6", jsonString6); // 将转换后的数据放到data2中
        return AjaxResult.success(json);
    }








}
