package com.ruoyi.system.process;


import com.ruoyi.system.utils.WarehousingUtil;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.equipment.Tray;
import com.ruoyi.system.resource.facilities.storage.Storage;
import com.ruoyi.system.resource.personnel.Emp;
import com.ruoyi.system.result.EmpLog;
import com.ruoyi.system.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Putaway {
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    /**
     * 上架流程---
     * @param emps 上架人员
     * @param storage  货位
     */
    public static List<EmpLog> work(Storage storage, List<Tray> trays, double putaway_speed, List<Emp> emps) {
        Date runTime = DateUtils.convertString2Date("HH:mm:ss", "08:00:00");//当前时间
        Date endTime = DateUtils.convertString2Date("HH:mm:ss", "18:00:00");//当前时间
        List<EmpLog> list = new ArrayList<>();
        while (runTime.getTime()<endTime.getTime()) {
            if (isEmpNull(emps)&&TrayisNotNull(trays)) {
                EmpToStorage(emps,trays,storage);
            }

            for (Emp emp:emps){
                EmpLog empLog = new EmpLog();
                empLog.setTime(sdf.format(runTime));
                empLog.setEmpNo(emp.getName());
                switch (emp.getStatus()){
                    case 0:
                        empLog.setEmpStatus(0);
                        break;
                    case 1:
                        empLog.setEmpStatus(1);
                        if (WarehousingUtil.getDistance(emp.getCurr(), emp.getTar()) < 1){
                            emp.addStatus();
                            emp.setT2(90);
                        }else {

                            double distance = 0.0;
                            emp.setCurr(WarehousingUtil.getPath(emp, putaway_speed));
                            if (putaway_speed>1) {
                                for (int speed = 0;speed < putaway_speed-1;speed++){
                                    emp.setCurr(WarehousingUtil.getPath(emp, 1));
                                    if (WarehousingUtil.getDistance(emp.getCurr(), emp.getTar())>0){
                                        distance++;
                                    }

                                }
                                emp.setCurr(WarehousingUtil.getPath(emp, putaway_speed-Math.floor(putaway_speed)));
                            }else {
                                emp.setCurr(WarehousingUtil.getPath(emp, putaway_speed));
                                distance+=putaway_speed;
                            }
                            empLog.setDistance(distance);
                        }
                        break;
                    case 2:
                        empLog.setEmpStatus(1);
                        if (emp.getT2() > 0) {
                            emp.dispose();
                        } else {
                            emp.setStatus(3);
                            emp.setTar(new Point(0,0,0));
                        }
                        break;
                    case 3:
                        empLog.setEmpStatus(1);
                        if (WarehousingUtil.getDistance(emp.getCurr(), emp.getTar()) < 1){
                            emp.setStatus(0);
                        }else {
                            empLog.setDistance(putaway_speed);
                            emp.setCurr(WarehousingUtil.getPath(emp, putaway_speed));
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

    private static void EmpToStorage(List<Emp> emps, List<Tray> trays, Storage storage) {
         for (Emp emp:emps){
             if (emp.getStatus()==0){
                 emp.setStatus(1);
                 Tray tray = trays.get(0);
                 emp.setTary(tray);
                 trays.remove(0);
                 emp.setTar(storage.getEmpTar(emp));
             }
         }
    }



    /**
     * 是否已经上架完成
     * @param trays
     * @return
     */
    private static boolean TrayisNotNull(List<Tray> trays) {
        boolean isnull =false;
        if (trays!=null&&trays.size()>0){
            isnull = true;
        }
        return isnull;
    }

    private static boolean isEmpNull(List<Emp> emps) {
        boolean isnull =false;
        for (Emp emp:emps){
            if (emp.getStatus()==0){
                isnull = true;
            }
        }

        return isnull;
    }


}
