package com.ruoyi.system.process;

import com.ruoyi.system.utils.WarehousingUtil;
import com.ruoyi.system.resource.equipment.Tray;
import com.ruoyi.system.resource.facilities.buffer.Tally;
import com.ruoyi.system.resource.personnel.Emp;
import com.ruoyi.system.result.EmpLog;
import com.ruoyi.system.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Delivery {
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    /**
     * 出库理货流程
     * @param emps
     * @param
     * @param
     * @param tally
     */

    public static List<EmpLog> work(Tally tally, List<Tray> trays, double delivery_speed, List<Emp> emps, double batch,double delivery_Time) {
        Date runTime = DateUtils.convertString2Date("HH:mm:ss", "08:00:00");//当前时间
        Date endTime = DateUtils.convertString2Date("HH:mm:ss", "18:00:00");//当前时间
        List<EmpLog> list = new ArrayList<>();
        List<String> dates = DateUtils.getIntervalTimeList(sdf.format(runTime),sdf.format(endTime),(int)((endTime.getTime()-runTime.getTime())/1000/60/batch));
        List<List<Tray>> lists = WarehousingUtil.averageAssign(trays,(int)batch);
        int i = 0;

        while (i<batch||TallyIsNotNull(tally)||runTime.getTime() < (endTime.getTime()+36000)) {
            if (TimeBatch(runTime,dates)){
                if (i<batch) {
                    List<Tray> trays1 = lists.get(i);
                    tally.putInTrays(trays1);
                }
                i++;
            }

            if (isEmpNull(emps)&&TallyIsNotNull(tally)) {
                EmpToTally(emps,tally);
            }


            for (Emp emp:emps){
                EmpLog empLog = new EmpLog();
                empLog.setEmpNo(emp.getCode());
                empLog.setTime(sdf.format(runTime));
                switch (emp.getStatus()){
                    case 0:
                        empLog.setEmpStatus(0);
                        break;
                    case 1:
                        empLog.setEmpStatus(1);
                        double distance = 0.0;
                        if (WarehousingUtil.getDistance(emp.getCurr(), emp.getTar()) ==0){
                            emp.addStatus();
                            emp.setT2(delivery_Time);
                        }else {

                            if (delivery_speed>1) {
                                for (int speed = 0;speed < delivery_speed-1;speed++){
                                    emp.setCurr(WarehousingUtil.getPath(emp, 1));
                                    if (WarehousingUtil.getDistance(emp.getCurr(), emp.getTar())>0){
                                        distance++;
                                    }

                                }
                                emp.setCurr(WarehousingUtil.getPath(emp, delivery_speed-Math.floor(delivery_speed)));
                            }else {
                                emp.setCurr(WarehousingUtil.getPath(emp, delivery_speed));
                                distance+=delivery_speed;
                            }

                        }
                        empLog.setDistance(distance);
                        break;
                    case 2:
                        empLog.setEmpStatus(1);
                        if (emp.getT2() > 0) {
                            emp.dispose();
                        } else {
                            emp.addStatus();
                            emp.setStatus(0);
                        }
                        break;
                }
                list.add(empLog);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(runTime);
            calendar.add(Calendar.SECOND, 1);
            runTime = calendar.getTime();

        }

        return list;
    }

    /**
     * 为空闲的人员分配任务
     * @param emps
     * @param tally
     */
    private static void EmpToTally(List<Emp> emps, Tally tally) {
        for (Emp emp:emps){
            if (emp.getStatus()==0&&TallyIsNotNull(tally)){
                Tray tray = new Tray();
                double distance = Double.MAX_VALUE;
                for (Tray tray1:tally.getTrays()){
                    if (WarehousingUtil.getDistance(tray1.getPoint(),emp.getCurr())<distance){
                        tray = tray1;
                    }
                }
                emp.setStatus(1);
                emp.setTary(tray);
                emp.setTar(tray.getPoint());
                tally.getTrays().remove(tray);
            }
        }
    }

    /**
     * 理货区是否为空
     * @param tally
     * @return
     */
    private static boolean TallyIsNotNull(Tally tally) {
        boolean isnull = false;
        if (tally.getTrays()!=null&&tally.getTrays().size()>0){
            isnull = true;
        }
        return isnull;
    }

    /**
     * 是否有人员空闲
     * @param emps
     * @return
     */
    private static boolean isEmpNull(List<Emp> emps) {
        boolean isnull =false;
        for (Emp emp:emps){
            if (emp.getStatus()==0){
                isnull = true;
            }
        }

        return isnull;
    }

    /**
     * 是否到达批次运行时间
     * @param runTime
     * @param dates
     * @return
     */
    private static boolean TimeBatch(Date runTime, List<String> dates) {
        boolean is = false;
        for (String str:dates) {
            if (sdf.format(runTime).equals(str)) {
                is = true;
            }
        }
        return is;
    }
}
