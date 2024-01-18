package com.ruoyi.system.algorithm.KNN;




import com.ruoyi.system.resource.Storage;

import java.util.ArrayList;
import java.util.List;

public class KMeansCluster {
    // 聚类中心数
    public int k = 5;
    // 迭代最大次数
    public int maxIter = 50;

    // 测试点集
    public List<Point> points;

    // 中心点
    public List<Point> centers;

    //仓储类输入
    public List<Storage> storages;



    public static final double MINDISTANCE = 10000.00;
    private static double EARTH_RADIUS = 6378.137;

    public KMeansCluster(int k, int maxIter, List<Point> points) {
        this.k = k;
        this.maxIter = maxIter;
        this.points = points;

        //初始化中心点
        initCenters();
    }

    /*
     * 初始化聚类中心
     * 这里的选取策略是，从点集中按序列抽取K个作为初始聚类中心
     */
    public void initCenters()
    {
        centers = new ArrayList<>(k);

        for (int i = 0; i < k; i++)
        {
            Point tmPoint = points.get(i * 2);
            Point center = new Point(tmPoint.getX(), tmPoint.getY(),tmPoint.getCity());
            center.setClusterID(i + 1);
            centers.add(center);
        }
    }


    /*
     * 停止条件是满足迭代次数
     */
    public void runKmeans()
    {
        // 已迭代次数
        int count = 1;

        while (count++ <= maxIter)
        {
            // 遍历每个点，确定其所属簇
            for (Point point : points)
            {
                assignPointToCluster(point);
            }

            //调整中心点
            adjustCenters();
        }
    }
    /*
     * 停止条件是满足迭代次数
     */
    public void runKmeans1()
    {
        // 已迭代次数
        int count = 1;

        while (count++ <= maxIter)
        {
            // 遍历每个点，确定其所属簇
            for (Point point : points)
            {
                assignPointToCluster1(point);
            }
            //调整中心点
            adjustCenters();
        }
    }


    /*
     * 调整聚类中心，按照求平衡点的方法获得新的簇心
     */
    public void adjustCenters()
    {
        double sumx[] = new double[k];
        double sumy[] = new double[k];
        int count[] = new int[k];

        // 保存每个簇的横纵坐标之和
        for (int i = 0; i < k; i++)
        {
            sumx[i] = 0.0;
            sumy[i] = 0.0;
            count[i] = 0;
        }

        // 计算每个簇的横纵坐标总和、记录每个簇的个数
        for (Point point : points)
        {
            int clusterID = point.getClusterID();
            sumx[clusterID - 1] += point.getX();
            sumy[clusterID - 1] += point.getY();
            count[clusterID - 1]++;
        }

        // 更新簇心坐标
        for (int i = 0; i < k; i++)
        {
            Point tmpPoint = centers.get(i);

            Point recPoint = getRecPoint(sumx[i] / count[i],sumy[i] / count[i]);
            tmpPoint.setX(recPoint.getX());
            tmpPoint.setY(recPoint.getY());
            tmpPoint.setCity(recPoint.getCity());
            tmpPoint.setClusterID(i + 1);
            centers.set(i, tmpPoint);
        }
    }

    private Point getRecPoint(double x, double y) {
        Point point =null;
        Double d = Double.MAX_VALUE;
        Double n = Double.MAX_VALUE;
        for (int i=0;i<points.size();i++){
           double c = getDistance(x,y,points.get(i).getX(),points.get(i).getY());
           double time = getDistance(x,y,points.get(i).getX(),points.get(i).getY())/60+storages.get(i).getArea();
           if (c<d&&time<n){
               d=c;
               time = n;
               point=points.get(i);
           }

        }
        return point;
    }
    private Point getRecPoint1(double x, double y) {
        Point point =null;
        Double d = Double.MAX_VALUE;
        Double n = Double.MAX_VALUE;
        for (int i=0;i<points.size();i++){
            double c = getDistance(x,y,points.get(i).getX(),points.get(i).getY());
            double time = getDistance(x,y,points.get(i).getX(),points.get(i).getY())/60+storages.get(i).getArea();
            if (c<d){
                d=c;
                point.setSize(time);
                point=points.get(i);
            }

        }
        return point;
    }

    /*划分点到某个簇中，欧式距离标准
     * 对传入的每个点，找到与其最近的簇中心点，将此点加入到簇
     */
    public void assignPointToCluster(Point point)
    {
        double minDistance = MINDISTANCE;

        int clusterID = -1;

        for (Point center : centers)
        {
            double dis = EurDistance1(point, center);
            if (dis < minDistance)
            {
                minDistance = dis;
                clusterID = center.getClusterID();
            }
        }
        point.setClusterID(clusterID);
    }
    /*划分点到某个簇中，欧式距离标准
     * 对传入的每个点，找到与其最近的簇中心点，将此点加入到簇
     */
    public void assignPointToCluster1(Point point)
    {
        double minDistance = MINDISTANCE;

        int clusterID = -1;

        for (Point center : centers)
        {
            double dis = EurDistance1(point, center);
            if (dis < minDistance)
            {
                minDistance = dis;
                clusterID = center.getClusterID();
            }
        }
        point.setClusterID(clusterID);

    }

//    //欧式距离，计算两点距离
//    public double EurDistance(Point point, Point center)
//    {
//        double detX = point.getX() - center.getX();
//        double detY = point.getY() - center.getY();
//
//        return Math.sqrt(detX * detX + detY * detY);
//    }
    /**
     * 欧氏距离
     * @param point1
     * @param point2
     * @return
     */
    private  double EurDistance(Point point1, Point point2){
        return getDistance(point1.getX(),point1.getY(),point2.getX(),point2.getY());
    }


    /**
     * 欧氏距离
     * @param point1
     * @param point2
     * @return
     */
    public   double EurDistance1(Point point1, Point point2){
//        QueryWrapper<Distance> wrapper = new QueryWrapper<>();
//        wrapper.eq("src_city",point1.getCity());
//        wrapper.eq("dst_city",point2.getCity());
//        Distance distance = distanceDao.selectOne(wrapper);
//        if (distance==null){
//            QueryWrapper<Distance> wrapper1 = new QueryWrapper<>();
//            wrapper1.eq("src_city",point2.getCity());
//            wrapper1.eq("dst_city",point1.getCity());
//             distance = distanceDao.selectOne(wrapper1);
//        }else {
            return getDistance(point1.getX(),point1.getY(),point2.getX(),point2.getY());
//        }
//        return distance.getDistance();
    }
//    public  Double getAdd(double lat1, double lng1, double lat2,
//                          double lng2){
//        Double address = 0.0;
//        try{
//            URL url = new URL("http://api.map.baidu.com/telematics/v3/distance?ak=gZRVFrIj60poMESUZK1H7OVYASU1YIz6&waypoints="+lat1 + "," + lng1 +";"+lat2+ ","+lng2);
//            HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
//            ucon.connect();
//
//            InputStream in = ucon.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
//            String str = reader.readLine();
//            //outprint.print(str);
//
//            str = str.substring(str.indexOf("(") + 1, str.length()-1);
//
//            str = str.replaceAll("\\[", "").replaceAll("\\]", "");
//            String[] distanceArray = str.split(",");
//            address = Double.parseDouble(distanceArray[0]);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return address;
//    }
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        return s;
    }
    public static double getDistance1(double lat1, double lng1,double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        return s;
    }
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
