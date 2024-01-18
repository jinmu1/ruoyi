package com.ruoyi.system.utils;


import java.util.*;

public final class MathUtils {
    public static double Variance(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        return dVar/m;
    }
    public static double avg(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        return dAve;
    }
    public static double sum(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        return sum;
    }
    public static double StandardDiviation(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        return Math.sqrt(dVar/m);
    }

    /**
     *
     *  * @描述:图像：偏度 <br/>
     *  * @方法名: skewness <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double 返回值小于零为负偏度，大于0为正偏度，等于0是正态分布 <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月3日下午2:55:11 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月3日下午2:55:11 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double skewness(double[] in) {
        double mean = mean(in);
        double median = median(in);
        double SD = standardDeviation2(in);
        return 3 * Mutil.divide(Mutil.subtract(mean, median), SD, 2);
    }



    /**
     *
     *  * @描述:集中趋势量数：均值/算术平均数（arithmetic mean) <br/>
     *  * @方法名: mean <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:45:24 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:45:24 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double mean(double[] in) {
        if (in == null) {
            throw new NumberFormatException();
        }
        if (in.length == 1) {
            return in[0];
        }
        double sum = 0;
        for (int i = 0; i < in.length; i++) {
            sum = Mutil.add(sum, in[i]);
            // sum += in[i];
        }
        // return sum/in.length;
        return Mutil.divide(sum, in.length, 2);
    }
    /**
     *
     *  * @描述:集中趋势量数：计算中位数 <br/>
     *  * @方法名: median <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:45:33 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:45:33 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double median(double[] in) {
        if (in == null) {
            throw new NumberFormatException();
        }
        Arrays.sort(in);

        // for (int i = 0; i < in.length; i++) {
        // log.debug("sort: "+i+":::"+in[i]);
        // }
        if (in.length % 2 == 1) {
            return in[(int) Math.floor(in.length / 2)];
        } else {
            double[] avg = new double[2];
            avg[0] = in[(int) Math.floor(in.length / 2) - 1];
            avg[1] = in[(int) Math.floor(in.length / 2)];
            return mean(avg);

        }
    }

    /**
     *
     *  * @描述:集中趋势量数：计算众数 <br/>
     *  * @方法名: mode <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 List <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:45:42 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:45:42 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static List mode(double[] in) {
        HashMap map = new HashMap();
        double imode = 0;
        for (int i = 0; i < in.length; i++) {
            double x = in[i];
            if (map.containsKey(String.valueOf(x))) {
                // 如果出现多次，取出以前的计数，然后加1
                int len = Integer.parseInt(map.get(String.valueOf(x)).toString());
                map.put(String.valueOf(x), String.valueOf(len + 1));
                imode = Math.max(imode, len + 1);
            } else {
                // 如果是第一次出现，计数为1
                map.put(String.valueOf(x), String.valueOf(1));
                imode = Math.max(imode, 1);
            }
        }
        Iterator iter = map.keySet().iterator();
        ArrayList lst = new ArrayList();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object v = map.get(key);
            if (Integer.parseInt(v.toString()) == imode) {
                lst.add(key);
            }
        }
          if (lst.size()>1){
              Object a = lst.get(0);
              lst.clear();
              lst.add(a);
          }
        return lst;
    }
    /**
     *
     *  * @描述:集中趋势量数：极差（不包含） <br/>
     *  * @方法名: range <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:26:20 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:26:20 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/ >  * @throws <br/>
     *  
     */
    public static double range(double[] in) {
        if (in == null) {
            throw new NumberFormatException();
        }
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < in.length; i++) {
            max = Math.max(max, in[i]);
            min = Math.min(min, in[i]);
        }
        // return max - min;
        return Mutil.subtract(max, min);
    }

    /**
     *
     *  * @描述: 变异性量数：极差（包含） <br/>
     *  * @方法名: range2 <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:26:08 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:26:08 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double range2(double[] in) {
        if (in == null) {
            throw new NumberFormatException();
        }
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < in.length; i++) {
            max = Math.max(max, in[i]);
            min = Math.min(min, in[i]);
        }
        // return max - min + 1;
        return Mutil.subtract(max, min) + 1;

    }
    /**
     *
     *  * @描述:变异性量数：方差 <br/>
     *  * @方法名: variance <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:47:27 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:47:27 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double variance(double[] in) {
        double t_mean = mean(in);
        double t_sumPerPow = 0;
        for (int i = 0; i < in.length; i++) {
            t_sumPerPow = Mutil.add(t_sumPerPow, Math.pow(Mutil.subtract(in[i], t_mean), 2));
        }
        return Mutil.divide(t_sumPerPow, (in.length - 1), 2);
    }

    /**
     *
     *  * @描述:变异性量数：标准差（无偏估计） <br/>
     *  * @方法名: standardDeviation <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double standardDeviation(double[] in) {
        return Math.sqrt(variance(in));
    }
    /**
     *
     *  * @描述:变异性量数：标准误差（无偏估计） <br/>
     *  * @方法名: standardDeviation <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double standardError(double[] in) {
        return MathUtils.standardDeviation(in)/Math.sqrt(in.length);
    }
    /**
     *
     *  * @描述:变异性量数：标准差（有偏估计） <br/>
     *  * @方法名: SD <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月2日下午10:32:07 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double standardDeviation2(double[] in) {
        double t_mean = mean(in);
        double t_sumPerPow = 0;
        for (int i = 0; i < in.length; i++) {
            t_sumPerPow = Mutil.add(t_sumPerPow, Math.pow(Mutil.subtract(in[i], t_mean), 2));
        }
        return Math.sqrt(Mutil.divide(t_sumPerPow, (in.length), 2));


    }
    /**
     *
     *  * @描述:图像:峰度 <br/>
     *  * @方法名: kurtosis <br/>
     *  * @param in <br/>
     *  * @return <br/>
     *  * @返回类型 double 用于计算一组数据是平滑还是陡峭，返回0=常峰或者正态，否则表示陡峭 <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月3日下午3:00:57 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月3日下午3:00:57 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double kurtosis(double[] in) {
        double mean = mean(in);
        double SD = standardDeviation(in);
        int n = in.length;
        double sum = 0;
        for (int i = 0; i < in.length; i++) {
            sum = Mutil.add(sum, Math.pow(Mutil.divide(Mutil.subtract(in[i], mean), SD, 2), 4));
        }
        return Mutil.round(Mutil.divide(sum, n, 2) - 3, 2);
    }
    /**
     *
     *@描述: 相关系数 <br/>
     *@方法名: correlation <br/>
     *@param x <br/>
     *@param y <br/>
     *@return  * @返回类型 double 返回值[-1,1]{[-1,0]负相关；[0,1]正相关}， 取绝对值，越大表示相关性越强,
     *          .8~1： 非常强 ，.6~.8： 强相关 ，.4~.6： 中度相关，.2~.4： 弱相关，.0~.： 弱相关或者无关 <br/>
     * @创建人 micheal <br/>
     *@创建时间 2019年1月3日下午8:51:27 <br/>
     *@修改人 micheal <br/>
     *@修改时间 2019年1月3日下午8:51:27 <br/>
     *@修改备注 <br/>
     *@since <br/>
     *@throws <br/>
     *  
     */
    public static double correlation(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new NumberFormatException();
        }
        double xSum = 0;
        double ySum = 0;
        double xP2Sum = 0;
        double yP2Sum = 0;
        double xySum = 0;
        int len = x.length;
        for (int i = 0; i < y.length; i++) {

            xSum = Mutil.add(xSum, x[i]);
            ySum = Mutil.add(ySum, y[i]);
            xP2Sum = Mutil.add(xP2Sum, Math.pow(x[i], 2));
            yP2Sum = Mutil.add(yP2Sum, Math.pow(y[i], 2));
            xySum = Mutil.add(xySum, Mutil.multiply(x[i], y[i]));

        }
        double Rxy = Mutil.subtract(Mutil.multiply(len, xySum), Mutil.multiply(xSum, ySum)) / (Math.sqrt((Mutil.multiply(len, xP2Sum) - Math.pow(xSum, 2)) * (Mutil.multiply(len, yP2Sum) - Math.pow(ySum, 2))));
        return Mutil.round(Rxy, 2);

    }

    /**
     *
     *  * @描述: 决定系数 <br/>
     *  * @方法名: correlationOfDetermination <br/>
     *  * @param x <br/>
     *  * @param y <br/>
     *  * @return  * @返回类型 double 表示两个变量共享的方差，越大，表示越相关，表示X有百分之多少可以被Y解释。 <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月4日下午10:40:28 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月4日下午10:40:28 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws  
     */
    public static double correlationOfDetermination(double[] x, double[] y) {
        return Math.pow(correlation(x, y), 2);

    }

    /**
     *
     *  * @描述: 内在一致性信度 <br/>
     *  * @方法名: ICR <br/>
     *  * @param values <br/>
     *  * @return <br/>
     *  * @返回类型 double 克隆巴赫系数 [.00 ~ +1.00],越大越可信 <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月4日下午11:38:24 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月4日下午11:38:24 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double ICR(double[]... values) {
        int k = values.length;
        double varianceSum = 0.0;
        double scoreSum = 0.0;
        double[] Tscore = new double[values[0].length];
        for (int i = 0; i < k; i++) {
            double[] x = values[i];
            varianceSum = varianceSum + variance(x);
            for (int j = 0; j < x.length; j++) {
                Tscore[j] = Tscore[j] + x[j];
            }
        }
        scoreSum = variance(Tscore);
        return Mutil.multiply(k / (k - 1), Mutil.divide(Mutil.subtract(varianceSum, scoreSum), varianceSum, 2));

    }

    /**
     *
     *  * @描述: 标准值 <br/>
     *  * @方法名: zScore <br/>
     *  * @param x 数组<br/>
     *  * @param n 数组成员<br/>
     *  * @return <br/>
     *  * @返回类型 double 表示x1偏离了多少个标准差，大于1.65表示“可能性极值” 不是随机因数导致的 <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月4日下午11:32:39 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月4日下午11:32:39 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws <br/>
     *  
     */
    public static double zScore(double[] x, double n) {
        return Mutil.divide(Mutil.subtract(n, mean(x)), standardDeviation(x), 2);

    }
    /**
     *
     *  * @描述: 独立样本T检验 ，用于检测群体x和群体y之间是否支持零假设<br/>
     *  * @方法名: independentSamplesT <br/>
     *  * @param x <br/>
     *  * @param y <br/>
     *  * @return <br/>
     *  * @返回类型 double 分双重检测与单侧检测，结合自由度查询"临界值"对照表，如果实际值大于临界值
     * 表示不能接受零假设，否则零假设是最有力的假设 <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月5日下午8:57:00 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月5日下午8:57:00 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws  
     */
    public static double independentSamplesT(double[] x, double[] y) {
        double Xmean = mean(x);
        double Ymean = mean(y);
        int n1 = x.length;
        int n2 = y.length;
        double Xvariance = variance(x);
        double Yvariance = variance(y);
        return Mutil.divide(Mutil.subtract(Xmean, Ymean), Math.sqrt(Mutil.multiply(Mutil.divide(Mutil.add(Mutil.multiply((n1 - 1), Xvariance), Mutil.multiply((n2 - 1), Yvariance)), (n1 + n2 - 2), 2), Mutil.divide((n1 + n2), Mutil.multiply(n1, n2), 2))), 2);

    }
    /**
     *
     *  * @描述: 计算效应量,简单方式 <br/>
     *  * @方法名: ES <br/>
     *  * @param x <br/>
     *  * @param y <br/>
     *  * @return <br/>
     *  * @返回类型 double 越大表示两个群体重叠的部分越少 (小效应量: .0~.2 ; 中效应量: .2~.5;大效应量: .5~以上)
     * <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月5日下午10:58:11 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月5日下午10:58:11 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws  
     */
    public static double ES(double[] x, double[] y) {
        double xMean = mean(x);
        double yMean = mean(y);
        double xSD = standardDeviation(x);
        double ySD = standardDeviation(y);

        return Mutil.divide(Mutil.subtract(xMean, yMean), xSD, 2);
    }

    /**
     *
     *  * @描述: 计算效应量，标准方式，更准确 <br/>
     *  * @方法名: ES2 <br/>
     *  * @param x <br/>
     *  * @param y <br/>
     *  * @return <br/>
     *  * @返回类型 double 越大表示两个群体重叠的部分越少 (小效应量: .0~.2 ; 中效应量: .2~.5;大效应量: .5~以上)
     * <br/>
     *  * @创建人 micheal <br/>
     *  * @创建时间 2019年1月5日下午10:58:11 <br/>
     *  * @修改人 micheal <br/>
     *  * @修改时间 2019年1月5日下午10:58:11 <br/>
     *  * @修改备注 <br/>
     *  * @since <br/>
     *  * @throws  
     */
    public static double ES2(double[] x, double[] y) {
        double xMean = mean(x);
        double yMean = mean(y);
        double xVariance = variance(x);
        double yVariance = variance(y);

        return Mutil.divide(Mutil.subtract(xMean, yMean), Math.sqrt(Mutil.divide(Mutil.add(xVariance, yVariance), 2, 2)), 2);

    }


    /**
     *
      * @描述:  线性回归  <br/>
      * @方法名: lineRegression    <br/>
      * @param x 自变量  <br/>
      * @param y 因变量  <br/>
      * @param X 用于估计的值   <br/>
      * @return    <br/>
      * @返回类型 double  返回X对应的预测值  <br/>
      * @创建人 micheal    <br/>
      * @创建时间 2019年1月7日下午12:57:12    <br/>
      * @修改人 micheal    <br/>
      * @修改时间 2019年1月7日下午12:57:12    <br/>
      * @修改备注    <br/>
      * @since    <br/>
      * @throws    <br/>
      
     */
    public static double lineRegression(double[] x, double[] y,double X) {
        int n = x.length;
        if (x.length != y.length) {
            throw new NumberFormatException();
        }
        double xSum = 0;
        double ySum = 0;
        double xP2Sum = 0;
        double yP2Sum = 0;
        double xySum = 0;
        int len = x.length;
        for (int i = 0; i < n; i++) {
            xSum = Mutil.add(xSum, x[i]);
            ySum = Mutil.add(ySum, y[i]);
            xP2Sum = Mutil.add(xP2Sum, Math.pow(x[i], 2));
            yP2Sum = Mutil.add(yP2Sum, Math.pow(y[i], 2));
            xySum = Mutil.add(xySum, Mutil.multiply(x[i], y[i]));

        }
        double b = (xySum - (xSum * ySum) / n) / (xP2Sum - (Math.pow(xSum, 2) / n));
        double a = (ySum - (b * xSum)) / n;
        double Y = (b * X + a);
        double[] seoe = new double[n];
        for (int i = 0; i < n; i++) {
            seoe[i] = Math.abs(((b * x[i]) + a) - y[i]);
            System.out.println(" 估计误差：" + seoe[i]);
        }

        System.out.println("标准估计误差：" + mean(seoe));
        return Mutil.round(Y, 2);

    }
}