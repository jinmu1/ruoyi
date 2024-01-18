package com.ruoyi.system.resource.facilities.buffer;

import com.ruoyi.system.utils.WarehousingUtil;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.equipment.Tray;

import java.util.ArrayList;
import java.util.List;

public class Picking {
    private double area;//分拣区面积
    private List<Tray> trays;//物料数量
    private List<Point> points;//坐标
    private static double x=0;//初始化点的x坐标
    private static double y=40;//初始点的y坐标

    /**
     * 初始化分拣区位置
     * @param tally_transverse 横向托盘数量
     * @param tally_longitudinal 纵向托盘数量
     * @param channel_width 通道宽度
     * @param tray_clearance  托盘间隙
     */
    private void initPicking(int tally_transverse,int tally_longitudinal,double channel_width,double tray_clearance,double tally_width,double tally_length){
        points = new ArrayList<>();
          for (int i=0;i<tally_transverse;i++){
              for (int j =0;j<tally_longitudinal;j++){
                  Point point =new Point(x+i*(tally_width+channel_width)+tally_width/2,y+j*(tally_length+tray_clearance)+tally_length/2,1,0);
                  points.add(point);
              }
          }
    }

    private void addTrays(Tray tray){
        trays = new ArrayList<>();
        for(Point point:points){
            if(point.getStatus()==0){
                point.setStatus(1);
                trays.add(tray);
            }
        }
    }
    private void removeTray(Tray tray){
        trays = new ArrayList<>();
        for(Point point:points){
            if(point.getStatus()==1&& WarehousingUtil.getDistance(point,tray.getPoint())==0){
                point.setStatus(0);
                trays.remove(tray);
            }
        }
    }


    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public List<Tray> getTrays() {
        return trays;
    }

    public void setTrays(List<Tray> trays) {
        this.trays = trays;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
