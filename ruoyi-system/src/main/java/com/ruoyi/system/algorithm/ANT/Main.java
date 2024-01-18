package com.ruoyi.system.algorithm.ANT;

public class Main {

    public static void main(String[] args){
        ACO aco;
        aco=new ACO();
        int Max=1000;

        int[][] G=new int[][]{
                {Max,2,Max,Max,Max},
                {2,Max,2,3,Max},
                {Max,2,Max,4,Max},
                {Max,3,4,Max,5},
                {Max,Max,Max,5,Max}
                };
        aco.init(G,100);
        aco.run(1000,2,4);
        aco.ReportResult();


    }
}
