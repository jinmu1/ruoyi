package com.ruoyi.system.queue;

/*
 * This gives the coordinates of each place.
 * Basically print out the layout of floor
 */


import com.ruoyi.system.resource.facilities.Warehouse;

public class Point {
    private double x;
    private double y;
    private double z;
    private int status;//点的区域
    private double num;
    private String type;
    private String code;

    public Point() {
        super();
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(double x, double y, double z, String type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public Point(double x, double y, double z, int status) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public Point north() {
        double yNorth = y + 1;
        Point newPt = new Point(x, yNorth,z);
        //if yNorth > Floor.Width, stay at the old place
        if (yNorth > Warehouse.Width) {
            return new Point(x, y, z);
        }
        else {
            return newPt;
        }
    }

    public Point south() {
        double ySou = y - 1;
        Point newPt = new Point(x, ySou,z);
        //if ySou < 0, stay at the old place
        if (ySou < 0) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }

    public Point west() {
        double xWest = x - 1;
        Point newPt = new Point(xWest, y ,z);
        //if xWest < 0, stay at the old place
        if (xWest < 0) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }

    public Point east() {
        double xEast = x + 1;
        Point newPt = new Point(xEast, y ,z);
        //if xEest > Floor.Length, stay at the old place
        if (xEast > Warehouse.Length) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }
    public Point north(double m) {
        double yNorth = y + m;
        Point newPt = new Point(x, yNorth,z);
        //if yNorth > Floor.Width, stay at the old place
        if (yNorth > Warehouse.Width) {
            return new Point(x, y, z);
        }
        else {
            return newPt;
        }
    }

    public Point south(double m) {
        double ySou = y - m;
        Point newPt = new Point(x, ySou,z);
        //if ySou < 0, stay at the old place
        if (ySou < 0) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }

    public Point west(double m) {
        double xWest = x - m;
        Point newPt = new Point(xWest, y ,z);
        //if xWest < 0, stay at the old place
        if (xWest < 0) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }

    public Point east(double m) {
        double xEast = x + m;
        Point newPt = new Point(xEast, y ,z);
        //if xEest > Floor.Length, stay at the old place
        if (xEast > Warehouse.Length) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }

    public Point up() {
        double zUp = z + 1;
        Point newPt = new Point(x, y ,zUp);
        //if xEest > Floor.Length, stay at the old place
        if (zUp > Warehouse.height) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }
    public Point down() {
        double zUp = z - 1;
        Point newPt = new Point(x, y ,zUp);
        //if xEest > Floor.Length, stay at the old place
        if (zUp < 0) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }
    public Point up(double num) {
        double zUp = z + num;
        Point newPt = new Point(x, y ,zUp);
        //if xEest > Floor.Length, stay at the old place
        if (zUp > Warehouse.height) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }
    public Point down(double num) {
        double zUp = z - num;
        Point newPt = new Point(x, y ,zUp);
        //if xEest > Floor.Length, stay at the old place
        if (zUp < 0) {
            return new Point(x, y ,z);
        }
        else {
            return newPt;
        }
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String toString() {
        return "X is " + x + " Y is " + y + " Z is " + z + "\n" ;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
