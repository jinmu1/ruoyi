package com.ruoyi.system.algorithm.ANT;

import java.util.Arrays;

public class ACO {
    Ant []ants;   //蚂蚁
    private int ant_num;   //蚂蚁数
    public int [][]distance;  //节点间代价
    double [][]tao;   //信息素矩阵
    int node_num;     //节点数量
    public int[] best_path;   //最佳路径
    int bestlength;//求的最优解的长度
    int[] node;                 //节点
//    Node first_node;
    int[]next_node;
    int this_node;
    int[] result_path;    //起点到终点的路径
//    int result_length;    //起点到终点的长度
    private int[] length_array;   //每次迭代得到的最优长度的数组集合

    public void init(int[][] map,int ant_count){
        ant_num=ant_count;
        ants=new Ant[ant_num];
        node=new int[5];
        node_num=5;
        this_node=0;
        next_node=new int[node_num];
        distance=new int[node_num][node_num];
        for (int i=0;i<map.length;i++){

            node[i]=i;
            for(int j=0;j<map.length;j++){
                distance[i][j]=map[i][j];     //距离
            }
        }
//        distance[node_num-1][node_num-1]=0;   //??????
        //初始化信息素矩阵
        tao=new double[node_num][node_num];
        for(int i=0;i<node_num;i++){
            for(int j=0;j<node_num;j++){
                tao[i][j]=0.1;
            }
        }
        bestlength=Integer.MAX_VALUE;
        best_path=new int[node_num+1];
        result_path=new  int[node_num];
        length_array=new int[1000];
        //随机放蚂蚁
        for (int i=0;i<ant_count;i++){
            ants[i]=new Ant();
            ants[i].RandomSelect(node_num);
        }
    }

    //更新信息素矩阵
    public void UpdateTao(){
        double rou=0.5;   //挥发率
        //信息素挥发
        for(int i=0;i<node_num;i++){
            node[i]=i;
            for(int j=0;j<node_num;j++){
                tao[i][j]=tao[i][j]*(1-rou);

            }
        }
        //信息素更新，
        for(int i=0;i<ant_num;i++){
            for (int j=0;j<node_num;j++){
                tao[ants[i].path[j]][ants[i].path[j+1]]+=1.0/ants[i].path_length;
            }
        }
    }
    //maxgen ACO的最多循环次数
    //s_node 源点编号
    //d_node 目的点编号
    //源和目的节点暂时没用上
    public void run(int maxgen,int s_node,int d_node){

        for (int runtime=0;runtime<maxgen;runtime++){
            //每次迭代，所有蚂蚁都要跟新一遍，走一遍
            for (int i=0;i<ant_num;i++){

                for (int j=1;j<node_num;j++){
                    ants[i].SelectNextNode(j,tao,distance,s_node,d_node); //每只蚂蚁的城市规划
                }
                ants[i].Cal_pathLength(distance);  //计算蚂蚁获得的路径长度
                if(ants[i].path_length<bestlength){
                    //保留最优路径
                    bestlength=ants[i].path_length;

                    System.out.println("第"+runtime+"代(次迭代)，发现新的最优路径长度："+bestlength);
                       //更新路径
                    for(int j=0;j<node_num+1;j++){
                        best_path[j]=ants[i].path[j];
                    }
                }

            }
            length_array[runtime]=ants[0].path_length;   //哪一个无所谓，一次迭代下蚂蚁的都一样
            //更新信息素
            UpdateTao();
            //重新设置蚂蚁到起点
            for(int i=0;i<ant_num;i++){
                ants[i].RandomSelect(node_num);
            }
        }
    }
    public void ReportResult(){
        System.out.println("最优路径长度是"+bestlength);
        System.out.println("蚁群算法最优路径输出：");
        System.out.print(Arrays.toString(best_path) +">>\n");//输出最优路径
        System.out.println("蚁群算法每一次迭代最优路径长度输出：");
        System.out.print(Arrays.toString(length_array) +">>\n");//输出所有最优路径长度
//        for (int i=2;i<4;i++){
//            System.out.println("蚁群算法两点间最优路径输出：");
//            for (int j=0;j<node_num;j++){
//                if(best_path[j]==i){
//                    System.out.print(best_path[i] +">>");//输出两点间最优路径
//
//                }
//            }
//        }


    }

}
