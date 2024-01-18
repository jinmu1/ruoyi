package com.ruoyi.system.utils;


import com.ruoyi.system.enumType.CarType;
import com.ruoyi.system.enumType.StorageType;
import com.ruoyi.system.resource.equipment.LightStorage;
import com.ruoyi.system.resource.equipment.StereoStorage;
import com.ruoyi.system.resource.facilities.buffer.Tally;
import com.ruoyi.system.resource.facilities.platform.Platform;
import com.ruoyi.system.resource.facilities.storage.Storage;

public  class AreaUtils {

    public static double platform_width = 3.5;//月台的宽度
    public static double platform_length = 3;//单个月台的宽度 车辆宽度一般在1.8-2米之间
    public static double platform_length1 = 2.4;//单个月台的宽度 车辆宽度一般在1.8-2米之间
    public static double unloading_time = 0.5;//卸货时间
    public static double everyDay_unloading_time = 8;//日收货作业时间
    public static double peak_rate = 1.5;//高峰概率
    public static double volumetric_coefficient=0.8;//容积系数
    public static double uploadEmpCapacity = 150;

    public static double tally_batch=4;//理货波次
    public static double tally_rate=1.5;//高峰理货系数
    public static double tray_length = 1.2;//托盘长度
    public static double tray_width = 1;//托盘宽度
    public static double tray_clearance = 0.1;//托盘间隙
    public static double tally_channel = 0.6;//托盘间隙
    public static double forklift_channel=3;//叉车通道
    public static double shelf_channel = 1;//人员拣货通道
    public static double tally_area_rate = 0.7;//面积利用率



    public static double cargo_box_length = 1.2;//单个货格的长度
    public static double cargo_box_width = 1;//单个货格的宽度
    public static double shelf_space = 0.1;//货架间隙
    public static double light_cargo_box_height = 0.6;//轻型货架单个货格的高度
    public static int light_shelf_layer = 4;//轻型货架层数
    public static int high_shelf_layer = 5;//高位货架层数
    public static double beam_high_cargo_box_height = 1.7;//横梁式高位货架单个货格的高度
    public static double shuttle_high_cargo_box_height = 1.9;//穿梭式高位货架单个货格的高度
    public static double state1_high_cargo_box_width =1.65;//提升机巷道宽度
    public static double state1_high_cargo_box_height = 1.6;//单\双升立体货架高度
    public static double state2_high_cargo_box_height = 1.6;//多升立体货架
    public static double heap_height = 1.5;//地堆货位的高
    public static double tray_price=280;//托盘-塑料（带RFID)
    public static double automation_forklift_price = 65000;//自动化叉车价格
    public static double manual_forklift_price = 2000;//手动化叉车
    public static double light_cargo_price = 200;//轻型货架价格
    public static double high_cargo_price = 200;//横梁式货位的价格
    public static double high_cross_cargo_price = 400;//穿梭式货架价格
    public static double workload = 50;//没人每天处理托数

    public static double state1_high_cargo_box_price = 420;//单\双升立体货架高度
    protected static double state2_high_cargo_box_price = 520;//多升立体货架堆垛机
    protected static double state1_high_cargo_box_stacker = 800000;//单个堆垛机价格
    protected static double state2_high_cargo_box_stacker = 950000;//多升堆垛机价格
    protected static double y_cargo_box_stacker_speed = 1.2;//堆垛机向上初速度
    protected static double x_cargo_box_stacker_speed = 2;//堆垛机向前速度
    protected static double ay_cargo_box_stacker_speed = 0.2;//向上加速度
    protected static double ax_cargo_box_stacker_speed = 0.2;//堆垛机向前加速度
    protected static double belt_price = 2000;//传送带每米的价格


    /**
     *  计算月台数量与面积
     * @param total
     * @param
     * @return
     */
    public static Platform getPlatform(double  total, String carType){
        double car_volumetric = Double.parseDouble(CarType.valueOf(carType).getCode());//计算车辆容积 通过车辆类型
        platform_width = 3 * 1.25;

        double car_num = Math.ceil(total/volumetric_coefficient/car_volumetric);//计算车辆数量 通过车辆容积
        double platform_num =  Math.ceil(car_num*peak_rate*unloading_time/everyDay_unloading_time);//月台数量
        double platform_area = platform_num*platform_width*platform_length;//月台面积
        Platform platform = new Platform();
        platform.setPlatform_num(platform_num);
        platform.setPlatform_area(platform_area);
        platform.setForklift((int)(total/uploadEmpCapacity));
        platform.setForkliftCost(platform.getForklift()*12000);
        return platform;

    }



    /**
     *  计算月台数量与面积
     * @param total
     * @param
     * @return
     */
    public static Platform getPlatform1(double  total, String carType,double uploadEmpCapacity,double unloading_time,double everyDay_unloading_time , double platform_length){
        double car_volumetric = Double.parseDouble(CarType.valueOf(carType).getCode());//计算车辆容积 通过车辆类型
        platform_width = 3 * 1.25;
        double milk_run = 1;
        double car_num = Math.ceil(total/volumetric_coefficient/car_volumetric)*milk_run;//计算车辆数量 通过车辆容积
        double platform_num =  Math.ceil(car_num*peak_rate*unloading_time/everyDay_unloading_time);//月台数量
        double platform_area = platform_num*(platform_width+3)*platform_length;//月台面积
        Platform platform = new Platform();
        platform.setPlatform_num(platform_num);
        platform.setPlatform_area(platform_area);
        platform.setForklift((int)Math.ceil(total/uploadEmpCapacity));

        platform.setForkliftCost(platform.getForklift()*12000);
        return platform;

    }
    /**
     * 计算理货区面积
     *
     */
    public static Tally getTally(double  total, String carType){
        double pallet_num = total*tally_rate/tally_batch;//理货平均托盘量
        int tally_transverse = 1;//纵向数量


        int tally_longitudinal= (int)Math.ceil(pallet_num/tally_transverse);//横向数量
        double tally_area = tally_longitudinal*(tray_length+tray_clearance)*(tray_width+tally_channel+3)*tally_transverse/tally_area_rate;//理货区面积
        Tally tally = new Tally();
        tally.setPallet(pallet_num);
        tally.setArea(tally_area);
        tally.setTally_longitudinal(tally_longitudinal);
        tally.setTally_transverse(tally_transverse);
        return tally;
    }


    /***
     * 计算地堆存储区面积
     */
    public static Storage getHeapStorage(double total){
        double area = total*tray_width*tray_length;//地堆区面积
        Storage storage = new Storage();
        storage.setType(StorageType.地堆区域.getCode());
        storage.setPrice(total*tray_price);
        storage.setArea(area);
        return storage;
    }

    /***
     * 计算立库区域面积----总成本最优
     * 规则一：价格最低
     * 规则二：高度小于建筑物高度
     * 规则三：吞吐能力能够满足需求
     * @param total
     * @return
     */
    public static StereoStorage getStereoStorage(double total, double throughput, double height){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int  cargo = 0;
        int stacker = 0;
        double belt = 0;
        double area = 0.0;
        double price = 1000000000;;
        for (int i=2;i<1000;i+=2){
            for (int j=1;j<1000;j++){
                for (int k=1;k<height/state1_high_cargo_box_height;k++){
                    if (i*j*k>total){
                        double  efficiency = getTime(k*state1_high_cargo_box_height,y_cargo_box_stacker_speed,ay_cargo_box_stacker_speed)+getTime(0.5*j*cargo_box_length,x_cargo_box_stacker_speed,ax_cargo_box_stacker_speed);
                        double  workNum = 8*60*60*i/2/efficiency;
                        if (workNum>throughput) {
                            double area1 = (i * cargo_box_width + i / 2 * state1_high_cargo_box_width) * ((cargo_box_length+shelf_space)*j+4);
                            double num = i * j * k * state1_high_cargo_box_price + i / 2 * state1_high_cargo_box_stacker+(i/2*state1_high_cargo_box_width+i*cargo_box_width)*2*belt_price;
                            if (price > num) {
                                area= area1;
                                price = num;
                                rows = i;
                                line = j;
                                layer = k;
                                cargo = i*j*k;
                                stacker = i/2;
                                belt = (i/2*state1_high_cargo_box_width+i*cargo_box_width)*2;
                            }
                        }
                    }
                }
            }
        }
        StereoStorage storage = new StereoStorage();
        storage.setLayer(layer);
        storage.setRow(rows);
        storage.setLine(line);
        storage.setArea(area);
        storage.setPrice(price/10000);
        storage.setCargo(cargo);
        storage.setStacker(stacker);
        storage.setBelt(belt);
        return storage;
    }
    /***
     * 计算立库区域面积----面积最优单升
     * 规则一：价格最低
     * 规则二：高度小于建筑物高度
     * 规则三：吞吐能力能够满足需求
     * @param total
     * @return
     */
    public static StereoStorage getStereoStorageArea(double total,double throughput,double height){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int  cargo = 0;
        int stacker = 0;
        double belt = 0;
        double area =1000000000;
        double price = 1000000000;;
        for (int i=2;i<1000;i+=2){
            for (int j=1;j<1000;j++){
                for (int k=1;k<height/state1_high_cargo_box_height;k++){
                    if (i*j*k>total){
                        double  efficiency = getTime(k*state1_high_cargo_box_height,y_cargo_box_stacker_speed,ay_cargo_box_stacker_speed)+getTime(0.5*j*cargo_box_length,x_cargo_box_stacker_speed,ax_cargo_box_stacker_speed);
                        double  workNum = 8*60*60*i/2/efficiency;
                        if (workNum>throughput) {
                            double area1 = (i * cargo_box_width + i / 2 * state1_high_cargo_box_width) * ((cargo_box_length+shelf_space)*j+4);
                            double num = i * j * k * state1_high_cargo_box_price + i / 2 * state1_high_cargo_box_stacker+(i/2*state1_high_cargo_box_width+i*cargo_box_width)*2*belt_price;
                            if (area > area1) {
                                area= area1;
                                price = num;
                                rows = i;
                                line = j;
                                layer = k;
                                cargo = i*j*k;
                                stacker = i/2;
                                belt = (i/2*state1_high_cargo_box_width+i*cargo_box_width)*2;
                            }
                        }
                    }
                }
            }
        }
        StereoStorage storage = new StereoStorage();
        storage.setLayer(layer);
        storage.setRow(rows);
        storage.setLine(line);
        storage.setArea(area);
        storage.setPrice(price/10000);
        storage.setCargo(cargo);
        storage.setStacker(stacker);
        storage.setBelt(belt);
        return storage;
    }
    /***
     * 计算立库区域面积
     * 规则一：价格最低
     * 规则二：高度小于建筑物高度
     * 规则三：吞吐能力能够满足需求
     * @param total
     * @return
     */
    public static StereoStorage getDoubleStereoStorage(double total,double throughput,double height){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int  cargo = 0;
        int stacker = 0;
        double belt = 0;
        double area = 0.0;
        double price = 1000000000;;
        for (int i=2;i<1000;i+=2){
            for (int j=1;j<1000;j++){
                for (int k=1;k<height/state1_high_cargo_box_height;k++){
                    if (i*j*k>total){
                        double  efficiency = getTime(k*state1_high_cargo_box_height,y_cargo_box_stacker_speed,ay_cargo_box_stacker_speed)+getTime(0.5*j*cargo_box_length,x_cargo_box_stacker_speed,ax_cargo_box_stacker_speed);
                        double  workNum = 8*60*60*i/4/efficiency;
                        if (workNum>throughput) {
                            area = (i * cargo_box_width + i / 4 * state1_high_cargo_box_width) * (cargo_box_length + shelf_space+4)*j;
                            double num = i * j * k * state1_high_cargo_box_price + i / 4 * state1_high_cargo_box_stacker+(i/4*state1_high_cargo_box_width+i*cargo_box_width)*2*belt_price;
                            if (price > num) {
                                price = num;
                                rows = i;
                                line = j;
                                layer = k;
                                cargo = i*j*k;
                                stacker = i/4;
                                belt = (i/4*state1_high_cargo_box_width+i*cargo_box_width)*2;
                            }
                        }
                    }
                }
            }
        }
        StereoStorage storage = new StereoStorage();
        storage.setLayer(layer);
        storage.setRow(rows);
        storage.setLine(line);
        storage.setArea(area);
        storage.setPrice(price/10000);
        storage.setCargo(cargo);
        storage.setStacker(stacker);
        storage.setBelt(belt);
        return storage;
    }


    public static void getSorting(){

    }

    /***
     * 计算立库区域面积----面积最优双升
     * 规则一：价格最低
     * 规则二：高度小于建筑物高度
     * 规则三：吞吐能力能够满足需求
     * @param total
     * @return
     */
    public static StereoStorage getDoubuleStereoStorageArea(double total,double throughput,double height){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int  cargo = 0;
        int stacker = 0;
        double belt = 0;
        double area =1000000000;
        double price = 1000000000;;
        for (int i=2;i<1000;i+=2){
            for (int j=1;j<1000;j++){
                for (int k=1;k<height/state1_high_cargo_box_height;k++){
                    if (i*j*k>total){
                        double  efficiency = getTime(k*state1_high_cargo_box_height,y_cargo_box_stacker_speed,ay_cargo_box_stacker_speed)+getTime(0.5*j*cargo_box_length,x_cargo_box_stacker_speed,ax_cargo_box_stacker_speed);
                        double  workNum = 8*60*60*i/4/efficiency;
                        if (workNum>throughput) {
                            double area1 = (i * cargo_box_width + i / 4 * state1_high_cargo_box_width) * ((cargo_box_length+shelf_space)*j+4);
                            double num = i * j * k * state1_high_cargo_box_price + i / 4 * state1_high_cargo_box_stacker+(i/4*state1_high_cargo_box_width+i*cargo_box_width)*4*belt_price;
                            if (area > area1) {
                                area= area1;
                                price = num;
                                rows = i;
                                line = j;
                                layer = k;
                                cargo = i*j*k;
                                stacker = i/4;
                                belt = (i/4*state1_high_cargo_box_width+i*cargo_box_width)*2;
                            }
                        }
                    }
                }
            }
        }
        StereoStorage storage = new StereoStorage();
        storage.setLayer(layer);
        storage.setRow(rows);
        storage.setLine(line);
        storage.setArea(area);
        storage.setPrice(price/10000);
        storage.setCargo(cargo);
        storage.setStacker(stacker);
        storage.setBelt(belt);
        return storage;
    }
    /***
     * 计算立库区域面积--成本最低多升立体货架
     * 规则一：价格最低
     * 规则二：高度小于建筑物高度
     * 规则三：吞吐能力能够满足需求
     * @param total
     * @return
     */
    public static StereoStorage getMoreStereoStorage(double total,double throughput,double height){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int  cargo = 0;
        int stacker = 0;
        double belt = 0;
        double area = 0.0;
        double price = 1000000000;;
        for (int i=2;i<1000;i+=2){
            for (int j=1;j<1000;j++){
                for (int k=1;k<height/state1_high_cargo_box_height;k++){
                    if (i*j*k>total){
                        double  efficiency = getTime(k*state1_high_cargo_box_height,y_cargo_box_stacker_speed,ay_cargo_box_stacker_speed)+getTime(0.5*j*cargo_box_length,x_cargo_box_stacker_speed,ax_cargo_box_stacker_speed);
                        double  workNum = 8*60*60*i/8.4/efficiency;
                        if (workNum>throughput) {
                            area = (i * cargo_box_width + i / 4 * state1_high_cargo_box_width) * (cargo_box_length + shelf_space+4)*j;
                            double num = i * j * k * state2_high_cargo_box_price + i / 12 * state2_high_cargo_box_stacker+(i/12*state1_high_cargo_box_width+i*cargo_box_width)*2*belt_price;
                            if (price > num) {
                                price = num;
                                rows = i;
                                line = j;
                                layer = k;
                                cargo = i*j*k;
                                stacker = i/12;
                                belt = (i/12*state1_high_cargo_box_width+i*cargo_box_width)*2;
                            }
                        }
                    }
                }
            }
        }
        StereoStorage storage = new StereoStorage();
        storage.setLayer(layer);
        storage.setRow(rows);
        storage.setLine(line);
        storage.setArea(area);
        storage.setPrice(price/10000);
        storage.setCargo(cargo);
        storage.setStacker(stacker);
        storage.setBelt(belt);
        return storage;
    }



    /***
     * 计算立库区域面积----面积最低多升立体货架
     * 规则一：价格最低
     * 规则二：高度小于建筑物高度
     * 规则三：吞吐能力能够满足需求
     * @param total
     * @return
     */
    public static StereoStorage getMoreStereoStorageArea(double total,double throughput,double height){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int cargo = 0;
        int stacker = 0;
        double belt = 0;
        double area =1000000000;
        double price = 1000000000;;
        for (int i=2;i<1000;i+=2){
            for (int j=1;j<1000;j++){
                for (int k=1;k<height/state1_high_cargo_box_height;k++){
                    if (i*j*k>total){
                        double  efficiency = getTime(k*state1_high_cargo_box_height,y_cargo_box_stacker_speed,ay_cargo_box_stacker_speed)+getTime(0.5*j*cargo_box_length,x_cargo_box_stacker_speed,ax_cargo_box_stacker_speed);
                        double  workNum = 8*60*60*i/12/efficiency;
                        if (workNum>throughput) {
                            double area1 = (i * cargo_box_width + i / 12 * state1_high_cargo_box_width) * ((cargo_box_length+shelf_space)*j+4);
                            double num = i * j * k * state2_high_cargo_box_price + i / 12 * state2_high_cargo_box_stacker+(i/12*state1_high_cargo_box_width+i*cargo_box_width)*4*belt_price;
                            if (area > area1) {
                                area= area1;
                                price = num;
                                rows = i;
                                line = j;
                                layer = k;
                                cargo = i*j*k;
                                stacker = i/12;
                                belt = (i/12*state1_high_cargo_box_width+i*cargo_box_width)*2;
                            }
                        }
                    }
                }
            }
        }
        StereoStorage storage = new StereoStorage();
        storage.setLayer(layer);
        storage.setRow(rows);
        storage.setLine(line);
        storage.setArea(area);
        storage.setPrice(price/10000);
        storage.setCargo(cargo);
        storage.setStacker(stacker);
        storage.setBelt(belt);
        return storage;
    }

    /***
     * 轻型货架区
     * @return
     */
  public static LightStorage getLightStorage(double total, double throughput){
      int rows = 0;
      int line = 0;
      int layer = 0;
      int cargo = 0;
      double area = 0.0;
      double price =Double.MAX_VALUE;
      double emp = throughput/workload;
      for (int i=1;i<200;i++){
          for (int j=1;j<200;j++){
             int cargos = i*2*j*light_shelf_layer;
              if (cargos>total && j>emp &&i>5){
                  double num = (i*cargo_box_length)*(j*cargo_box_width*2+shelf_channel);
                  double num1 = i*j*2*light_shelf_layer*light_cargo_price;//货架的价格
                  if (price>num1){
                      price = num1;
                      rows = i;
                      line = j/2;
                      layer = light_shelf_layer;
                      cargo = cargos;
                      area = num;
                      emp = emp;
                  }
              }
          }
      }
;

      LightStorage storage = new LightStorage();
      storage.setArea(area);
      storage.setCargo(cargo);
      storage.setLayer(layer);
      storage.setLine(line);
      storage.setRow(rows);
      storage.setPrice(price/10000);
      storage.setEmp((int)emp);
      return storage;
  }

    /***
     * 高位货架型货架区
     * @return
     */
    public static LightStorage getHightStorage(double total,double throughput){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int cargo = 0;
        double area = 0.0;
        double price =Double.MAX_VALUE;
        double emp = Math.ceil(throughput/workload);
        for (int i=1;i<200;i++){
            for (int j=1;j<200;j++){
                int cargos = i*j*high_shelf_layer;
                if (cargos>total && j>emp && i>5){
                    double num = (i*cargo_box_length)*(j*cargo_box_width*2+forklift_channel);
                    double num1 = i*j*2*high_shelf_layer*high_cargo_price;//货架的价格
                    if (price>num1){
                        price = num1;
                        rows = i;
                        line = j/2;
                        layer = high_shelf_layer;
                        cargo = cargos;
                        area = num;

                    }
                }
            }
        }
        ;

        LightStorage storage = new LightStorage();
        storage.setArea(area);
        storage.setCargo(cargo);
        storage.setLayer(layer);
        storage.setLine(line);
        storage.setRow(rows);
        storage.setPrice(price/10000);
        storage.setEmp((int)emp);
        return storage;
    }

    /***
     * 高位货架型货架区
     * @return
     */
    public static LightStorage getHightStorage1(double total,double throughput,double throughput1,double putaway,double sorting,double height,double forklift_channel,double shelf_space,double shelf_height){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int cargo = 0;
        double area = 0.0;
        double price =Double.MAX_VALUE;
        double emp = throughput/workload;
        for (int i=1;i<200;i++){
            for (int j=1;j<200;j++){
                int high_shelf_layer = (int)Math.floor(height/shelf_height);
                int cargos =i*j* high_shelf_layer;
                if (cargos>total && j>emp && i>5){
                    double num = (i*cargo_box_length+shelf_space)*(j*cargo_box_width*2+forklift_channel);
                    double num1 = i*j*2*high_shelf_layer*high_cargo_price;//货架的价格
                    if (price>num1){
                        price = num1;
                        rows = i;
                        line = j/2;
                        layer = high_shelf_layer;
                        cargo = cargos;
                        area = num;

                    }
                }
            }
        }
        ;

        LightStorage storage = new LightStorage();
        storage.setArea(area);
        storage.setCargo(cargo);
        storage.setLayer(layer);
        storage.setLine(line);
        storage.setRow(rows);
        storage.setPrice(price/10000);
        storage.setPutawayemp((int)(throughput/putaway));
        storage.setSortingemp((int)(throughput1/sorting));
        return storage;
    }

    /**
     * 根据加速度和速度求时间
     * @param distance
     * @param speed
     * @param aspeed
     * @return
     */

    public static double getTime(double distance,double speed,double aspeed){
        double time = Math.sqrt(2*distance/aspeed);
        if (time*aspeed>speed){
            time = distance/speed+speed/aspeed;
        }
        return time;
    }
}
