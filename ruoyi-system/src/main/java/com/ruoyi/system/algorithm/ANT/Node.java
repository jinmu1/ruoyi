package com.ruoyi.system.algorithm.ANT;

public class Node {
    private int num;       //节点编号
    private Node node;     //本节点
//    private Node[] next_node;    //邻接节点

    public Node(int num){
        this.num=num;
//        this.next_node=null;
    }

//    public void setNext_node(Node[] next_node) {
//        this.next_node = next_node;
//    }



    public void setNode(Node node) {
        this.node = node;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }



//    public Node[] getNext_node() {
//        return next_node;
//    }

    public Node getNode() {
        return node;
    }

}
