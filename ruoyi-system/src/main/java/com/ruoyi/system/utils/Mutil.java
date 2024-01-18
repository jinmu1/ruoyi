package com.ruoyi.system.utils;

import java.math.BigDecimal;


public final class Mutil {
    /**
     *  * @描述: 加法 <br/>
     *  * @方法名: add <br/>
     *  * @param v1 <br/>
     *  * @param v2 <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月5日下午9:37:50 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月5日下午9:37:50 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws  
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     *  * @描述: 减法 <br/>
     *  * @方法名: subtract <br/>
     *  * @param v1 <br/>
     *  * @param v2 <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月5日下午9:38:16 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月5日下午9:38:16 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws  
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     *  * @描述: 乘法 <br/>
     *  * @方法名: mul <br/>
     *  * @param d1 <br/>
     *  * @param d2 <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月5日下午9:38:33 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月5日下午9:38:33 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws  
     */
    public static double multiply(double d1, double d2) {// 进行乘法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     *  * @描述: 除法 ，四舍五入<br/>
     *  * @方法名: div <br/>
     *  * @param d1 <br/>
     *  * @param d2 <br/>
     *  * @param len ，保留的小数位数<br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月5日下午9:38:59 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月5日下午9:38:59 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws  
     */
    public static double divide(double d1, double d2, int len) {// 进行除法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);

        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     *  * @描述:  除法，四舍五入取整数 ,例如：5/2=3(2.5四舍五入); 5/3=2(1.6四舍五入);<br/>
     *  * @方法名: div   <br/>
     *  * @param d1 <br/>
     *  * @param d2 <br/>
     *  * @return   <br/>
     *  * @返回类型 double  <br/>
     *  * @创建人 micheal   <br/>
     *  * @创建时间 2019年1月5日下午10:54:51 <br/>
     *  * @修改人 micheal   <br/>
     *  * @修改时间 2019年1月5日下午10:54:51 <br/>
     *  * @修改备注   <br/>
     *  * @since   <br/>
     *  * @throws   <br/>
     *  
     */
    public static double divide(double d1, double d2) {// 进行除法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     *  * @描述: 四舍五入 <br/>
     *  * @方法名: round  * @param d <br/>
     *  * @param len <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月5日下午9:39:13 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月5日下午9:39:13 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double round(double d, int len) {
        BigDecimal b1 = new BigDecimal(d);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double find_average(double[] arr) {
        double sum=0.0d;
        for(int i=0;i<arr.length;i++){
            sum+=arr[i];
        }
        //将计算结果返回
        return sum/arr.length;
    }


    public static double min(double[] res) {
        double Min = res[0];
        for (int i = 1; i < res.length; i++) {
            if (Min > res[i]) {
                Min = res[i];
            }
        }
        return Min;
    }
    public static double max(double[] res) {
        double Max = res[0];
        for (int i = 1; i < res.length; i++) {
            if (Max < res[i]) {
                Max = res[i];
            }
        }
        return Max;
    }
}