package com.ruoyi.system.algorithm.ANT;

import java.util.Random;

public class Ant {
    public int[] path;   //节点路径
    public int path_length;   //路径长度
//    public int that_length;   //除去环的路径长度
    private int node_num;           //节点总数
    public int[] visited;   //取值是0或1，1表示访问过，0表示没访问过

    //index，该选择第index个节点
    //随机选择下一节点，node_num为当前节点编号
    //tao   全局的信息素信息
    //distance  全局的距离矩阵信息
    //s_node 起点
    //d_node 终点
    public void SelectNextNode(int index,double[][]tao,int[][]distance,int s_node,int d_node){

        //计算选中概率所需系数
        double possibility[];
        possibility=new double[node_num];     //不确定是总数还是备选节点数，可能要改
        double alpha=1.0;
        double beta=2.0;
        double sum=0.0;                  //预选所有路径的信息素浓度之和，由于计算选择概率
//
        int current_node=path[index-1];   //蚂蚁当前所处节点位置
//        int[] temp=new int[nodes];
//        for (int i=0;i<nodes;i++){
//            temp[i]=next_node[i];
//        }
        for(int i=0;i<node_num;i++){
            if(visited[i]==0){           //只在还没走过的节点中选择，根据信息素浓度作为下一个
                sum+=(Math.pow(tao[current_node][i],alpha)*
                        Math.pow(1.0/distance[current_node][i],beta));
            }
        }
        //计算每个节点被选中的概率

        for(int i=0;i<node_num;i++){
            if(visited[i]==1){
                possibility[i]=0.0;   //已经过节点，选中概率就是0
            }else {
                possibility[i]=(Math.pow(tao[current_node][i],alpha)*
                        Math.pow(1.0/distance[current_node][i],beta))/sum;
            }
        }
        long r1=System.currentTimeMillis();    //获取当前时间作为种子
        Random random=new Random(r1);
        double select_node=random.nextDouble();
        //轮盘赌选择一个节点
        double sum_possibility=0;
        int select=-1;         //初始化被选中的节点为-1
        //选择随机，直到n个概率加起来大于随机数，则选择该节点
//        for (int i=0;i<node_num;i++){
//            if(visited[i]==0){               //要求没被选过
//                sum_possibility+=possibility[i];
//                if(sum_possibility>=select_node){
//                    select=i;
//
//                    break;
//                }else {
//                    select_node=random.nextDouble();    //如果没跳出来再随机一次
//                }
//            }
//        }
        while (sum_possibility<select_node){
            for (int i=0;i<node_num;i++){
                if(visited[i]==0){
                    sum_possibility+=possibility[i];
                    if(sum_possibility>=select_node){
                        select=i;
                        break;
                    }
                }
            }
            select_node=random.nextDouble();    //如果没跳出来再随机一次
        }
        if(select==-1)
            System.out.println("Fa♂！！！");
        System.out.println("此时选中准备作为下一个的节点编号为：\n"+select);
        path[index]=select;    //将选定的节点编号写入路径
        visited[select]=1;        //将所确定的下一个节点编号修改状态为已访问
    }

    //计算路径代价
    public void Cal_pathLength(int [][]distance){
        path_length=0;
//        that_length=0;
//        path[node_num]=path[0];    //第一个点等于最后一个要到达的点
        for(int i=0;i<node_num;i++){
            path_length+=distance[path[i]][path[i+1]];//计算此时的总代价
        }
//        for(int i=0;i<node_num-1;i++){
//           that_length+=distance[path[i]][path[i+1]];//计算此时的总代价
//        }


    }
    //初始化蚂蚁子，把他们全扔起点去
    public void RandomSelect(int node_count){
        node_num=node_count;
        visited=new int[node_num+1];
        path=new int[node_count+1];

        for (int i=0;i<node_num;i++){
            visited[i]=0;
            path[i]=-1;    //初始路径中都是-1(节点编号)
        }//初始化
        long r1=System.currentTimeMillis();
        Random random=new Random(r1);
        int first_node=random.nextInt(node_num);
        visited[first_node]=1;
        path[0]=first_node;   //起始点
    }
}
