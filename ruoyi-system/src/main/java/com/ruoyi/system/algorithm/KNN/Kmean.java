package com.ruoyi.system.algorithm.KNN;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.List;

public class Kmean {
    // 用来聚类的点集
    public static List<Point> points;

    // 将聚类结果保存到文件
    FileWriter out = null;

    // 格式化double类型的输出，保留两位小数
    DecimalFormat dFormat = new DecimalFormat("00.00");

    // 具体执行聚类的对象
    public KMeansCluster kMeansCluster;

    // 簇的数量，迭代次数
    public static int numCluster ;
    public static int numIterator = 1000;

    // 点集的数量，生成指定数量的点集
    public int numPoints = 50;



    public static String run(List<Point> list, int num)
    {
        points = list;
        numCluster = num;
        //指定点集个数，簇的个数，迭代次数
        Kmean kmeans = new Kmean(list.size(), num, 5);

//        //初始化点集、KMeansCluster对象
//        kmeans.init();

        //使用KMeansCluster对象进行聚类
        kmeans.runKmeans();

        return kmeans.printRes();

    }

    public static List<Point> runResult(List<Point> list, int num)
    {
        points = list;
        numCluster = num;
        //指定点集个数，簇的个数，迭代次数
        Kmean kmeans = new Kmean(list.size(), num, 1000);

        //初始化点集、KMeansCluster对象
//        kmeans.init();

        //使用KMeansCluster对象进行聚类
        kmeans.runKmeans1();

        return points;

    }
       public static List<Point> runPoint(List<Point> list, int num)
    {
        points = list;
        numCluster = num;
        //指定点集个数，簇的个数，迭代次数
        Kmean kmeans = new Kmean(list.size(), num, 1000);

        //初始化点集、KMeansCluster对象
//        kmeans.init();

        //使用KMeansCluster对象进行聚类
        kmeans.runKmeans();

        return kmeans.printPoint();

    }

    public Kmean(int numPoints, int cluster_number, int iterrator_number) {
        this.numPoints = numPoints;
        this.numCluster = cluster_number;
        this.numIterator = iterrator_number;
    }

//    private void init()
//    {
//        kMeansCluster = new KMeansCluster(numCluster, numIterator, points);
//    }

    private void runKmeans()
    {
        kMeansCluster.runKmeans();
    }
    private void runKmeans1()
    {
        kMeansCluster.runKmeans1();
    }


    public String printRes()
    {
        Point point1 = new Point(116.4007533787,39.9031834643,"北京");
        double all = 0.0;
        for (Point point : points)
        {
            for (Point center : kMeansCluster.centers) {
                if (point.getClusterID() == center.getClusterID()) {
                    all +=KMeansCluster.getDistance(point.getX(), point.getY(), center.getX(), center.getY());
                }
            }
        }
        for (Point center : kMeansCluster.centers)
        {
            all +=KMeansCluster.getDistance(point1.getX(),point1.getY(),center.getX(),center.getY());
        }
        return String.valueOf(all);
    }
    public List<Point> printPoint()
    {

        System.out.println("==================Centers-I====================");
        for (Point center : kMeansCluster.centers)
        {
            System.out.println(center.toString());
        }

        System.out.println("==================Points====================");

        for (Point point : points)
        {
            System.out.println(point.toString());
        }
        return kMeansCluster.points;
    }


}
