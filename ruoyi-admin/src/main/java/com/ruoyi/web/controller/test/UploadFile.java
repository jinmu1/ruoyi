package com.ruoyi.web.controller.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 这是ABC测试类，测试将json静态数据转换为可动态处理数据
 */
public class UploadFile  {

    /**
     * 第一步测试：这里是转换逻辑和计算方法
     */
    @Test
    public void   UploadFile()
    {
//        String jsonString = null;
        //读取本地数据
//        File jsonFile = new File(new ClassPathResource("static/file/abc.json").getPath());
        // 指定JSON文件路径
        String filePath = System.getProperty("user.dir")+"/src/main/resources/static/file/abc.json";;

        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            /**
             * 获取老师给的数据，将数据计算后转换位对象Data2Entry
             */
            // 读取整个JSON文件为JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // 获取 "data1" 部分
            JsonNode data1Node = rootNode.get("data1");

            // 将 "data1" 转换为数组
            Data1Entry[] data1Entries = objectMapper.treeToValue(data1Node, Data1Entry[].class);
            List<Data2Entry> list = new ArrayList<>();
            // 现在你可以使用data1Entries了
            //step1：首先计算总销售金额
            for (Data1Entry entry : data1Entries) {
              Data2Entry data2Entry = new Data2Entry();
              data2Entry.setMaterialCode(entry.getMaterialCode());
              data2Entry.setUnitPrice(entry.getSellingPrice());
              data2Entry.setAverageInventory(entry.getAverageInventory());
              data2Entry.setAverageFundsOccupied(entry.getSellingPrice()*entry.getAverageInventory());
              list.add(data2Entry);
            }
            //step2：对总销售金额进行降序排序
            Collections.sort(list,(m1, m2) -> Double.compare(m2.getAverageFundsOccupied(),m1.getAverageFundsOccupied()));
            //step3：对降序排序后的占比进行计算，下面都是计算过程
            double occupied=0;
            int next =0;
            double allInventory = 0d;
            int all = 0;
            /**
             * all：统计总数据条数
             * allInventory：统计数据总的库存金额
             */
            for (Data2Entry data2Entry:list){
                all = all+ 1;
                allInventory = allInventory+ data2Entry.getAverageFundsOccupied();
            }
            for(Data2Entry data2Entry:list){
                occupied =occupied + data2Entry.getAverageFundsOccupied();
                data2Entry.setCumulativeAverageFundsOccupied(occupied);
                data2Entry.setCumulativeAverageFundsOccupiedPercentage(new BigDecimal(occupied*100/allInventory).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
                next=next+1;
                data2Entry.setCumulativeItemNumber(next);
                data2Entry.setCumulativeItemNumberPercentage(new BigDecimal(next*100/all).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            }
            /**
             * 获取原始出库表，将原始出库表data5转换为data3,和data4
             */
            // 获取 "data5" 部分
            JsonNode data5Node = rootNode.get("data5");

            // 将 "data1" 转换为数组
            Data5Entry[] data5Entries = objectMapper.treeToValue(data5Node, Data5Entry[].class);
            List<Data5Entry> data5EntryList =new ArrayList<>(Arrays.asList(data5Entries));;


            // 使用流式操作和Collectors按照物料编码属性分类
            Map<String, List<Data5Entry>> categorizedMap = data5EntryList.stream()
                    .collect(Collectors.groupingBy(Data5Entry::getMaterialNumber));

            // 输出分类结果
            List<Data3Entry> data3Entries= new ArrayList<>();
            List<Data4Entry> data4Entries =new ArrayList<>();
            int tatol = categorizedMap.size();//总物料数量
            int AllOutboundFrequency =0;//总出库频次
            double AllShippedQuantity = 0.0;//总出库数量
           for(String key : categorizedMap.keySet()){
               List<Data5Entry> data5EntryList1 = categorizedMap.get(key);
               Data3Entry data3Entry = new Data3Entry();
               Data4Entry data4Entry = new Data4Entry();
               data3Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
               data4Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
               data4Entry.setMaterialDescription(data5EntryList1.get(0).getMaterialName());
               int count = 0;//出库频次统计
               double num = 0.0;//出库数量统计
               for(Data5Entry data5Entry:data5EntryList1){
                   count++;
                   num = num+data5Entry.getShippedQuantity();
               }
               AllOutboundFrequency+=count;
               AllShippedQuantity+=num;
               data3Entry.setOutboundFrequency(count);
               data4Entry.setOutboundQuantity(num);
               data3Entries.add(data3Entry);
               data4Entries.add(data4Entry);
            }
            Collections.sort(data3Entries,(m1, m2) -> Double.compare(m2.getOutboundFrequency(),m1.getOutboundFrequency()));//按照出库频次降序排序
            Collections.sort(data4Entries,(m1, m2) -> Double.compare(m2.getOutboundQuantity(),m1.getOutboundQuantity()));//按照出库数量降序排序
            int frequency =0;//累计频次
            int num1=0;
            for(Data3Entry data3Entry:data3Entries){
                num1 ++;
                frequency +=data3Entry.getOutboundFrequency();
                data3Entry.setCumulativeOutboundFrequency(frequency);
                data3Entry.setCumulativeOutboundFrequencyPercentage(new BigDecimal(frequency*100/AllOutboundFrequency).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
                data3Entry.setCumulativeItemCount(num1);
                data3Entry.setCumulativeItemCountPercentage(new BigDecimal(num1*100/tatol).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            }

            int num2=0;//排行
            double quantity = 0.0;//累计数量
            for (Data4Entry data4Entry:data4Entries){
                num2 ++;
                quantity +=data4Entry.getOutboundQuantity();
                data4Entry.setCumulativeOutboundQuantity(quantity);
                data4Entry.setCumulativeOutboundQuantityPercentage(new BigDecimal(quantity*100/AllShippedQuantity).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
                data4Entry.setCumulativeItemCount(num2);
                data4Entry.setCumulativeItemCountPercentage(new BigDecimal(num2*100/tatol).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            }
            System.out.println(data1Entries);
            System.out.println(list);
            System.out.println(data3Entries);
            System.out.println(data4Entries);
            System.out.println(data5Entries);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

  static public class Data1Entry {
        @JsonProperty("物料编码")
        private String materialCode;

        @JsonProperty("平均库存")
        private double averageInventory;

        @JsonProperty("当月出库总量")
        private double monthlyTotalOutbound;

        @JsonProperty("库存周转次数")
        private int inventoryTurnover;

        @JsonProperty("库存周转天数")
        private int inventoryTurnoverDays;

        @JsonProperty("销售单价")
        private double sellingPrice;


        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public double getAverageInventory() {
            return averageInventory;
        }

        public void setAverageInventory(double averageInventory) {
            this.averageInventory = averageInventory;
        }

        public double getMonthlyTotalOutbound() {
            return monthlyTotalOutbound;
        }

        public void setMonthlyTotalOutbound(double monthlyTotalOutbound) {
            this.monthlyTotalOutbound = monthlyTotalOutbound;
        }

        public int getInventoryTurnover() {
            return inventoryTurnover;
        }

        public void setInventoryTurnover(int inventoryTurnover) {
            this.inventoryTurnover = inventoryTurnover;
        }

        public int getInventoryTurnoverDays() {
            return inventoryTurnoverDays;
        }

        public void setInventoryTurnoverDays(int inventoryTurnoverDays) {
            this.inventoryTurnoverDays = inventoryTurnoverDays;
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        @Override
        public String toString() {
            return "Data1Entry{" +
                    "materialCode=" + materialCode +
                    ", averageInventory=" + averageInventory +
                    ", monthlyTotalOutbound=" + monthlyTotalOutbound +
                    ", inventoryTurnover=" + inventoryTurnover +
                    ", inventoryTurnoverDays=" + inventoryTurnoverDays +
                    ", sellingPrice=" + sellingPrice +
                    '}';
        }
    }
  static public class Data2Entry {
        @JsonProperty("物料编码")
        private String materialCode;

        @JsonProperty("销售单价")
        private double unitPrice;

        @JsonProperty("平均库存")
        private double averageInventory;

        @JsonProperty("平均资金占用额")
        private double averageFundsOccupied;

        @JsonProperty("平均资金占用额累计")
        private double cumulativeAverageFundsOccupied;

        @JsonProperty("平均资金占用额累计百分比")
        private String cumulativeAverageFundsOccupiedPercentage;

        @JsonProperty("物料累计品目数")
        private int cumulativeItemNumber;

        @JsonProperty("物料累计品目数百分比")
        private String cumulativeItemNumberPercentage;

        // 添加其他字段...

        // 添加构造函数、getter和setter方法...


        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getAverageInventory() {
            return averageInventory;
        }

        public void setAverageInventory(double averageInventory) {
            this.averageInventory = averageInventory;
        }

        public double getAverageFundsOccupied() {
            return averageFundsOccupied;
        }

        public void setAverageFundsOccupied(double averageFundsOccupied) {
            this.averageFundsOccupied = averageFundsOccupied;
        }

        public double getCumulativeAverageFundsOccupied() {
            return cumulativeAverageFundsOccupied;
        }

        public void setCumulativeAverageFundsOccupied(double cumulativeAverageFundsOccupied) {
            this.cumulativeAverageFundsOccupied = cumulativeAverageFundsOccupied;
        }

        public String getCumulativeAverageFundsOccupiedPercentage() {
            return cumulativeAverageFundsOccupiedPercentage;
        }

        public void setCumulativeAverageFundsOccupiedPercentage(String cumulativeAverageFundsOccupiedPercentage) {
            this.cumulativeAverageFundsOccupiedPercentage = cumulativeAverageFundsOccupiedPercentage;
        }

        public int getCumulativeItemNumber() {
            return cumulativeItemNumber;
        }

        public void setCumulativeItemNumber(int cumulativeItemNumber) {
            this.cumulativeItemNumber = cumulativeItemNumber;
        }

        public String getCumulativeItemNumberPercentage() {
            return cumulativeItemNumberPercentage;
        }

        public void setCumulativeItemNumberPercentage(String cumulativeItemNumberPercentage) {
            this.cumulativeItemNumberPercentage = cumulativeItemNumberPercentage;
        }
    }
  static public class Data3Entry{
      @JsonProperty("物料编码")
      private String materialCode;

      @JsonProperty("出库频次")
      private int outboundFrequency;

      @JsonProperty("累计出库频次")
      private int cumulativeOutboundFrequency;

      @JsonProperty("累计出库频次百分比")
      private String cumulativeOutboundFrequencyPercentage;

      @JsonProperty("物料累计品目数")
      private int cumulativeItemCount;

      @JsonProperty("物料累计品目数百分比")
      private String cumulativeItemCountPercentage;

      // 添加构造函数、getter和setter方法...


      public String getMaterialCode() {
          return materialCode;
      }

      public void setMaterialCode(String materialCode) {
          this.materialCode = materialCode;
      }

      public int getOutboundFrequency() {
          return outboundFrequency;
      }

      public void setOutboundFrequency(int outboundFrequency) {
          this.outboundFrequency = outboundFrequency;
      }

      public int getCumulativeOutboundFrequency() {
          return cumulativeOutboundFrequency;
      }

      public void setCumulativeOutboundFrequency(int cumulativeOutboundFrequency) {
          this.cumulativeOutboundFrequency = cumulativeOutboundFrequency;
      }

      public String getCumulativeOutboundFrequencyPercentage() {
          return cumulativeOutboundFrequencyPercentage;
      }

      public void setCumulativeOutboundFrequencyPercentage(String cumulativeOutboundFrequencyPercentage) {
          this.cumulativeOutboundFrequencyPercentage = cumulativeOutboundFrequencyPercentage;
      }

      public int getCumulativeItemCount() {
          return cumulativeItemCount;
      }

      public void setCumulativeItemCount(int cumulativeItemCount) {
          this.cumulativeItemCount = cumulativeItemCount;
      }

      public String getCumulativeItemCountPercentage() {
          return cumulativeItemCountPercentage;
      }

      public void setCumulativeItemCountPercentage(String cumulativeItemCountPercentage) {
          this.cumulativeItemCountPercentage = cumulativeItemCountPercentage;
      }
  }
  static public class Data4Entry{
      @JsonProperty("物料编码")
      private String materialCode;

      @JsonProperty("物料描述")
      private String materialDescription;

      @JsonProperty("出库量")
      private double outboundQuantity;

      @JsonProperty("累计出库量")
      private double cumulativeOutboundQuantity;

      @JsonProperty("累计出库量百分比")
      private String cumulativeOutboundQuantityPercentage;

      @JsonProperty("物料累计品目数")
      private int cumulativeItemCount;

      @JsonProperty("物料累计品目数百分比")
      private String cumulativeItemCountPercentage;

      // 添加构造函数、getter和setter方法...


      public String getMaterialCode() {
          return materialCode;
      }

      public void setMaterialCode(String materialCode) {
          this.materialCode = materialCode;
      }

      public String getMaterialDescription() {
          return materialDescription;
      }

      public void setMaterialDescription(String materialDescription) {
          this.materialDescription = materialDescription;
      }

      public double getOutboundQuantity() {
          return outboundQuantity;
      }

      public void setOutboundQuantity(double outboundQuantity) {
          this.outboundQuantity = outboundQuantity;
      }

      public double getCumulativeOutboundQuantity() {
          return cumulativeOutboundQuantity;
      }

      public void setCumulativeOutboundQuantity(double cumulativeOutboundQuantity) {
          this.cumulativeOutboundQuantity = cumulativeOutboundQuantity;
      }

      public String getCumulativeOutboundQuantityPercentage() {
          return cumulativeOutboundQuantityPercentage;
      }

      public void setCumulativeOutboundQuantityPercentage(String cumulativeOutboundQuantityPercentage) {
          this.cumulativeOutboundQuantityPercentage = cumulativeOutboundQuantityPercentage;
      }

      public int getCumulativeItemCount() {
          return cumulativeItemCount;
      }

      public void setCumulativeItemCount(int cumulativeItemCount) {
          this.cumulativeItemCount = cumulativeItemCount;
      }

      public String getCumulativeItemCountPercentage() {
          return cumulativeItemCountPercentage;
      }

      public void setCumulativeItemCountPercentage(String cumulativeItemCountPercentage) {
          this.cumulativeItemCountPercentage = cumulativeItemCountPercentage;
      }
  }
  static public class Data5Entry{
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
      @JsonProperty("出库日期")
      private Date deliveryDate;

      @JsonProperty("订单编号")
      private long orderNumber;

      @JsonProperty("物料编号")
      private String materialNumber;

      @JsonProperty("物料名称")
      private String materialName;

      @JsonProperty("出货数量")
      private double shippedQuantity;

      @JsonProperty("出货单位")
      private String shipmentUnit;

      @JsonProperty("销售单价")
      private double unitPrice;

      @JsonProperty("托盘装件数")
      private double palletizedItemCount;

      // 添加构造函数、getter和setter方法...


      public Date getDeliveryDate() {
          return deliveryDate;
      }

      public void setDeliveryDate(Date deliveryDate) {
          this.deliveryDate = deliveryDate;
      }

      public long getOrderNumber() {
          return orderNumber;
      }

      public void setOrderNumber(long orderNumber) {
          this.orderNumber = orderNumber;
      }

      public String getMaterialNumber() {
          return materialNumber;
      }

      public void setMaterialNumber(String materialNumber) {
          this.materialNumber = materialNumber;
      }

      public String getMaterialName() {
          return materialName;
      }

      public void setMaterialName(String materialName) {
          this.materialName = materialName;
      }

      public double getShippedQuantity() {
          return shippedQuantity;
      }

      public void setShippedQuantity(double shippedQuantity) {
          this.shippedQuantity = shippedQuantity;
      }

      public String getShipmentUnit() {
          return shipmentUnit;
      }

      public void setShipmentUnit(String shipmentUnit) {
          this.shipmentUnit = shipmentUnit;
      }

      public double getUnitPrice() {
          return unitPrice;
      }

      public void setUnitPrice(double unitPrice) {
          this.unitPrice = unitPrice;
      }

      public double getPalletizedItemCount() {
          return palletizedItemCount;
      }

      public void setPalletizedItemCount(double palletizedItemCount) {
          this.palletizedItemCount = palletizedItemCount;
      }
  }
}
