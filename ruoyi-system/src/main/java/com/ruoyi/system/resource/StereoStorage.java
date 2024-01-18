package com.ruoyi.system.resource;

/**
 * 自动化立库内容
 */
public class StereoStorage extends Storage{

    private int  row;//排
    private int layer;//层
    private int  line;//列
    private  double area;//面积
    private double price;//设备总价
    private int  cargo;//货位数量
    private int stacker;//堆垛机数量
    private double belt;//传送带长度

    protected static double cargo_box_length = 1.2;//单个货格的长度
    protected static double cargo_box_width = 1;//单个货格的宽度
    protected static double shelf_space = 0.1;//货架间隙
    protected static double light_cargo_box_height = 0.6;//轻型货架单个货格的高度
    protected static int light_shelf_layer = 4;//轻型货架层数
    protected static int high_shelf_layer = 5;//高位货架层数
    protected static double beam_high_cargo_box_height = 1.7;//横梁式高位货架单个货格的高度
    protected static double shuttle_high_cargo_box_height = 1.9;//穿梭式高位货架单个货格的高度
    protected static double state1_high_cargo_box_width =1.65;//提升机巷道宽度
    protected static double state1_high_cargo_box_height = 1.6;//单\双升立体货架高度
    protected static double state2_high_cargo_box_height = 1.6;//多升立体货架
    protected static double heap_height = 1.5;//地堆货位的高
    protected static double tray_price=280;//托盘-塑料（带RFID)
    protected static double automation_forklift_price = 65000;//自动化叉车价格
    protected static double manual_forklift_price = 2000;//手动化叉车
    protected static double light_cargo_price = 200;//轻型货架价格
    protected static double high_cargo_price = 200;//横梁式货位的价格
    protected static double high_cross_cargo_price = 400;//穿梭式货架价格
    protected static double workload = 50;//没人每天处理托数
    protected static double state1_high_cargo_box_price = 420;//单\双升立体货架高度
    protected static double state2_high_cargo_box_price = 520;//多升立体货架堆垛机
    protected static double state1_high_cargo_box_stacker = 800000;//单个堆垛机价格
    protected static double state2_high_cargo_box_stacker = 950000;//多升堆垛机价格
    protected static double y_cargo_box_stacker_speed = 1.2;//堆垛机向上初速度
    protected static double x_cargo_box_stacker_speed = 2;//堆垛机向前速度
    protected static double ay_cargo_box_stacker_speed = 0.2;//向上加速度
    protected static double ax_cargo_box_stacker_speed = 0.2;//堆垛机向前加速度
    protected static double belt_price = 2000;//传送带每米的价格

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public int getStacker() {
        return stacker;
    }

    public void setStacker(int stacker) {
        this.stacker = stacker;
    }

    public double getBelt() {
        return belt;
    }

    public void setBelt(double belt) {
        this.belt = belt;
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
