package com.ruoyi.system.kmeans;

import com.ruoyi.system.node.City;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * @date 2018-02-09 下午 2:18
 * @author: <a href=mailto:huangyr@bonree.com>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class Cluster {

    private List<City> points = new ArrayList<City>(); // 属于该分类的点集
    private City centroid; // 该分类的中心质点
    private boolean isConvergence = false;

    public City getCentroid() {
        return centroid;
    }

    public void setCentroid(City centroid) {
        this.centroid = centroid;
    }

    @Override
    public String toString() {
        return centroid.toString();
    }

    public List<City> getPoints() {
        return points;
    }

    public void setPoints(List<City> points) {
        this.points = points;
    }


    public void initPoint() {
        points.clear();
    }

    public boolean isConvergence() {
        return isConvergence;
    }

    public void setConvergence(boolean convergence) {
        isConvergence = convergence;
    }
}
