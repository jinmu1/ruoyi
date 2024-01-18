package com.ruoyi.system.result;

import com.ruoyi.common.annotation.Excel;

/**
 *网络规划结果输出
 */
public class Result {
    private String city;//RDC
    private String range;//覆盖范围
    private  double transportCost;//运输成本输出
    private double  storageCost;//仓储成本输出
    private  double buildCost;//仓库建设成本输出
    private double  inventoryCost;//库存持有成本输出
    private double  all;//总成本输出
    private double rate;//总物流费率
    private double emp;//总的仓库人数
    private double area;//总的网络面积
    private double storage_area;
    private double storage;//仓储量
    private double car;//车辆数
    private double order_rate;//订单满足率
    private double replenishment_rate;//补货及时率
    private double distribution_rate;//配送效率
    private double storage_rate;//仓库作业效率
    private double inventory_num;//存储能力
    private double throughput_num;//吞吐能力
    private double sales_account;//销售金额
    private double rate1;//费率
    private double plat_storage;//单拖储存成本
    private double plat_transport;//单拖配送成本
    private double plat_cost;
    private double order_cost;
    private double area_cost;
    private int batch;//批次
    private double cost;//成本
    private double distance;//行走距离
    private double sortdistance;//行走距离
    private double putawaydistance;//行走距离

    private int uploadEmp;//卸货人员数量
    private int tallyEmp;//理货能力
    private int putawayEmp;//上架人员
    private int takeDownEmp;//下架人员
    private int sortingEmp;//分拣人员
    private int deliveryEmp;//装车人员
    private double uploadRate;//卸货人员利用率
    private double putawayRate;//上架人员利用率
    private double takeDownEmpRate;//下架人员利用率
    private double sortingRate;//分拣人员利用率
    private double deliveryRate;//装车人员利用率
    private double forklift;//卸货叉车
    private double empCost;//人员成本
    private double forkliftCost;//设备成本
    private int carNum;
    private double deliveryArea;//出库理货面积

    private double sortingArea;//分拣区面积
    private double platformNum;//卸货月台数量
    private double platformArea;//卸货月台面积
    private double platformRate;//卸货月台利用率
    private double tallyArea;//理货区面积
    private double tallyRate;//理货区面积利用率
    private double storageArea;//存储区面积
    private double tally1Area;//分拣区面积
    private double tally1Rate;//分拣区面积利用率
    private double platform1Num;//装车月台数量
    private double platform1Area;//装车月台面积
    private double platform1Rate;//装车月台利用率

    private int tally_transverse;// 纵向数量
    private int tally_longitudinal;//横向数量

    @Excel(name = "订单编码")
    private String order_code;//订单编码
    @Excel(name = "物料编码")
    private String goods_code;//物料编码
    @Excel(name = "物料数量")
    private double goods_num;//物料量
    @Excel(name = "物料数量（件）")
    private double goods_num1;//物料量
    @Excel(name = "物料数量（托）")
    private double goods_num2;//物料量
    @Excel(name = "时间")
    private String time;//订单到达时间
    @Excel(name = "日期")
    private String DateTime;//日期
    @Excel(name = "供应商")
    private String sullier;

    private int cargo;//货位数量
    private int layer;
    private int line;
    private int row;

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getGoods_code() {
        return goods_code;
    }

    public void setGoods_code(String goods_code) {
        this.goods_code = goods_code;
    }

    public double getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(double goods_num) {
        this.goods_num = goods_num;
    }

    public double getGoods_num1() {
        return goods_num1;
    }

    public void setGoods_num1(double goods_num1) {
        this.goods_num1 = goods_num1;
    }

    public double getGoods_num2() {
        return goods_num2;
    }

    public void setGoods_num2(double goods_num2) {
        this.goods_num2 = goods_num2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getSullier() {
        return sullier;
    }

    public void setSullier(String sullier) {
        this.sullier = sullier;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSortdistance() {
        return sortdistance;
    }

    public void setSortdistance(double sortdistance) {
        this.sortdistance = sortdistance;
    }

    public double getPutawaydistance() {
        return putawaydistance;
    }

    public void setPutawaydistance(double putawaydistance) {
        this.putawaydistance = putawaydistance;
    }

    public int getUploadEmp() {
        return uploadEmp;
    }

    public void setUploadEmp(int uploadEmp) {
        this.uploadEmp = uploadEmp;
    }

    public int getTallyEmp() {
        return tallyEmp;
    }

    public void setTallyEmp(int tallyEmp) {
        this.tallyEmp = tallyEmp;
    }

    public int getPutawayEmp() {
        return putawayEmp;
    }

    public void setPutawayEmp(int putawayEmp) {
        this.putawayEmp = putawayEmp;
    }

    public int getTakeDownEmp() {
        return takeDownEmp;
    }

    public void setTakeDownEmp(int takeDownEmp) {
        this.takeDownEmp = takeDownEmp;
    }

    public int getSortingEmp() {
        return sortingEmp;
    }

    public void setSortingEmp(int sortingEmp) {
        this.sortingEmp = sortingEmp;
    }

    public int getDeliveryEmp() {
        return deliveryEmp;
    }

    public void setDeliveryEmp(int deliveryEmp) {
        this.deliveryEmp = deliveryEmp;
    }

    public double getPutawayRate() {
        return putawayRate;
    }

    public void setPutawayRate(double putawayRate) {
        this.putawayRate = putawayRate;
    }

    public double getTakeDownEmpRate() {
        return takeDownEmpRate;
    }

    public void setTakeDownEmpRate(double takeDownEmpRate) {
        this.takeDownEmpRate = takeDownEmpRate;
    }

    public double getSortingRate() {
        return sortingRate;
    }

    public void setSortingRate(double sortingRate) {
        this.sortingRate = sortingRate;
    }

    public double getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(double deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    public double getForklift() {
        return forklift;
    }

    public void setForklift(double forklift) {
        this.forklift = forklift;
    }

    public double getEmpCost() {
        return empCost;
    }

    public void setEmpCost(double empCost) {
        this.empCost = empCost;
    }

    public double getForkliftCost() {
        return forkliftCost;
    }

    public void setForkliftCost(double forkliftCost) {
        this.forkliftCost = forkliftCost;
    }

    public double getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(double deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public double getSortingArea() {
        return sortingArea;
    }

    public void setSortingArea(double sortingArea) {
        this.sortingArea = sortingArea;
    }

    public double getPlatformNum() {
        return platformNum;
    }

    public void setPlatformNum(double platformNum) {
        this.platformNum = platformNum;
    }

    public double getPlatformRate() {
        return platformRate;
    }

    public void setPlatformRate(double platformRate) {
        this.platformRate = platformRate;
    }

    public double getTallyArea() {
        return tallyArea;
    }

    public void setTallyArea(double tallyArea) {
        this.tallyArea = tallyArea;
    }

    public double getTallyRate() {
        return tallyRate;
    }

    public void setTallyRate(double tallyRate) {
        this.tallyRate = tallyRate;
    }

    public double getStorageArea() {
        return storageArea;
    }

    public void setStorageArea(double storageArea) {
        this.storageArea = storageArea;
    }

    public double getTally1Area() {
        return tally1Area;
    }

    public void setTally1Area(double tally1Area) {
        this.tally1Area = tally1Area;
    }

    public double getTally1Rate() {
        return tally1Rate;
    }

    public void setTally1Rate(double tally1Rate) {
        this.tally1Rate = tally1Rate;
    }

    public double getPlatform1Num() {
        return platform1Num;
    }

    public void setPlatform1Num(double platform1Num) {
        this.platform1Num = platform1Num;
    }

    public double getPlatform1Area() {
        return platform1Area;
    }

    public void setPlatform1Area(double platform1Area) {
        this.platform1Area = platform1Area;
    }

    public double getPlatform1Rate() {
        return platform1Rate;
    }

    public void setPlatform1Rate(double platform1Rate) {
        this.platform1Rate = platform1Rate;
    }

    public int getTally_transverse() {
        return tally_transverse;
    }

    public void setTally_transverse(int tally_transverse) {
        this.tally_transverse = tally_transverse;
    }

    public int getTally_longitudinal() {
        return tally_longitudinal;
    }

    public void setTally_longitudinal(int tally_longitudinal) {
        this.tally_longitudinal = tally_longitudinal;
    }

    public double getPlatformArea() {
        return platformArea;
    }

    public void setPlatformArea(double platformArea) {
        this.platformArea = platformArea;
    }



    public void setPlatformNum(int platformNum) {
        this.platformNum = platformNum;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getUploadRate() {
        return uploadRate;
    }

    public void setUploadRate(double uploadRate) {
        this.uploadRate = uploadRate;
    }

    public int getCarNum() {
        return carNum;
    }

    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    public double getRate1() {
        return rate1;
    }

    public void setRate1(double rate1) {
        this.rate1 = rate1;
    }

    public double getPlat_cost() {
        return plat_cost;
    }

    public void setPlat_cost(double plat_cost) {
        this.plat_cost = plat_cost;
    }

    public double getOrder_cost() {
        return order_cost;
    }

    public void setOrder_cost(double order_cost) {
        this.order_cost = order_cost;
    }

    public double getArea_cost() {
        return area_cost;
    }

    public void setArea_cost(double area_cost) {
        this.area_cost = area_cost;
    }

    public double getPlat_storage() {
        return plat_storage;
    }

    public void setPlat_storage(double plat_storage) {
        this.plat_storage = plat_storage;
    }

    public double getPlat_transport() {
        return plat_transport;
    }

    public void setPlat_transport(double plat_transport) {
        this.plat_transport = plat_transport;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    public double getStorageCost() {
        return storageCost;
    }

    public void setStorageCost(double storageCost) {
        this.storageCost = storageCost;
    }

    public double getBuildCost() {
        return buildCost;
    }

    public void setBuildCost(double buildCost) {
        this.buildCost = buildCost;
    }

    public double getInventoryCost() {
        return inventoryCost;
    }



    public void setInventoryCost(double inventoryCost) {
        this.inventoryCost = inventoryCost;
    }

    public double getAll() {
        return all;
    }

    public double getStorage() {
        return storage;
    }

    public void setStorage(double storage) {
        this.storage = storage;
    }

    public void setAll(double all) {
        this.all = all;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getEmp() {
        return emp;
    }

    public void setEmp(double emp) {
        this.emp = emp;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getCar() {
        return car;
    }

    public void setCar(double car) {
        this.car = car;
    }

    public double getOrder_rate() {
        return order_rate;
    }

    public void setOrder_rate(double order_rate) {
        this.order_rate = order_rate;
    }

    public double getReplenishment_rate() {
        return replenishment_rate;
    }

    public void setReplenishment_rate(double replenishment_rate) {
        this.replenishment_rate = replenishment_rate;
    }

    public double getDistribution_rate() {
        return distribution_rate;
    }

    public void setDistribution_rate(double distribution_rate) {
        this.distribution_rate = distribution_rate;
    }

    public double getStorage_area() {
        return storage_area;
    }

    public void setStorage_area(double storage_area) {
        this.storage_area = storage_area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getStorage_rate() {
        return storage_rate;
    }

    public void setStorage_rate(double storage_rate) {
        this.storage_rate = storage_rate;
    }

    public double getInventory_num() {
        return inventory_num;
    }

    public void setInventory_num(double inventory_num) {
        this.inventory_num = inventory_num;
    }

    public double getThroughput_num() {
        return throughput_num;
    }

    public void setThroughput_num(double throughput_num) {
        this.throughput_num = throughput_num;
    }

    public double getSales_account() {
        return sales_account;
    }

    public void setSales_account(double sales_account) {
        this.sales_account = sales_account;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
