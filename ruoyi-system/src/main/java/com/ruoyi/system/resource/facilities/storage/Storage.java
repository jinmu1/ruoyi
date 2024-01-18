package com.ruoyi.system.resource.facilities.storage;


import com.ruoyi.system.form.Cargo;
import com.ruoyi.system.form.Goods;
import com.ruoyi.system.process.GoodsLayout;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.personnel.Emp;

import java.util.ArrayList;
import java.util.List;

public class Storage {
     private String type;
     private double area;
     private double price;//价格
     private  List<Cargo> cargos= new ArrayList<>();
     private List<Point> points =new ArrayList<>();
    public static double cargo_box_length = 1.2;//单个货格的长度
    public static double cargo_box_width = 1;//单个货格的宽度
    public static double high_cargo_price = 200;//横梁式货位的价格
     private int cargo;//货位数
    private int layer;
    private int line;
    private int row;

    public Point getEmpTar(Emp emp) {
        Point point =new Point();
        for (Cargo cargo: cargos){
            if (emp.getTary().getGoodsList().get(0).equals(cargo.getGoods())){
                point =  cargo.getPoint();
            }
        }
        return point;
    }
     public void initStorage(Storage storage, List<Goods> goods){
        storage.setCargos(GoodsLayout.initGoodsLayout(storage,goods));

     }


    public  Storage getHightStorage(double storageNum, double height, double forklift_channel, double shelf_space, double shelf_height){
        int rows = 0;
        int line = 0;
        int layer = 0;
        int cargo = 0;
        double area = 0.0;
        double price =Double.MAX_VALUE;
        int high_shelf_layer = (int)Math.floor(height/shelf_height);
        for (int i=1;i<200;i++){
            for (int j=1;j<200;j++){
                int cargos = i*j*high_shelf_layer;
                if (cargos>storageNum  && i>5){
                    double num = (i*cargo_box_length+shelf_space)*(j*cargo_box_width*2+forklift_channel);
                    double num1 = i*j*2*high_shelf_layer*high_cargo_price;//货架的价格
                    if (price>num1&&(int)Math.ceil(j/2)>0){
                        price = num1;
                        rows = i;
                        line = (int) (Math.ceil(j/2));
                        layer = high_shelf_layer;
                        cargo = cargos;
                        area = num;

                    }
                }
            }
        }
        ;

        Storage storage = new Storage();
        storage.setArea(area);
        storage.setCargo(cargo);
        storage.setLayer(layer);
        storage.setLine(line);
        storage.setRow(rows);
        return storage;
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

    public  Goods  getGoods(int i, int j, int k, List<Cargo> cargos){
         Goods goods= new Goods();
         for (Cargo cargo :cargos){
             if (i==cargo.getPoint().getX()&&j==cargo.getPoint().getY()&&k==cargo.getPoint().getZ()){
                 goods = cargo.getGoods();
             }
         }
         return goods;
     }
     public String getType() {
        return type;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<Cargo> getCargos() {
        return cargos;
    }

    public void setCargos(List<Cargo> cargos) {
        this.cargos = cargos;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }


}
