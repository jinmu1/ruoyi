package com.ruoyi.system.process;


import com.ruoyi.system.form.Car;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.facilities.buffer.Park;
import com.ruoyi.system.resource.facilities.platform.Platform;
import com.ruoyi.system.resource.personnel.Emp;
import com.ruoyi.system.result.EmpLog;
import com.ruoyi.system.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 卸货流程模拟
 */
public class Upload{

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 卸货流程仿真
     * @param emps
     * @param platforms
     */
    public static List<EmpLog> work(List<Car> cars, List<Platform> platforms,  List<Emp> emps,Park park,double platform_a) {
        Date runTime = DateUtils.convertString2Date("yyyy-MM-dd HH:mm:ss", "2021-01-01 08:00:00");//当前时间
        Date endTime = DateUtils.convertString2Date("yyyy-MM-dd HH:mm:ss", "2021-01-01 20:00:00");//当前时间

       //获取当前时间节点的倒库车辆
        List<EmpLog> list = new ArrayList<>();
        while (cars.size()>0||!isEmpIsNull(emps)) {
            while (isPlatformNotNull(platforms)&&parkIsNotNull(park)){
                    PlatforAddParkCar(park,platforms);
                }

            Iterator<Car> iterator = cars.listIterator();
            List<Car> carList = new ArrayList<>();
            while (iterator.hasNext()){
                Car car = iterator.next();
                if (sdf.format(car.getArrinveTime()).equals(sdf.format(runTime))){
                    carList.add(car);

                    iterator.remove();
                }
            }
            if (carList!=null&&carList.size()>0){
                AriveCar(carList,park,platforms);
            }
            isEmpToPlatformIsNull(emps,platforms,platform_a);

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
                        if (emp.getT2() > 0) {
                            emp.dispose();
                        } else {

                            emp.setStatus(0);
                            emp.setTar(new Point(platforms.size()*platforms.get(0).platform_width*2, platforms.get(0).platform_length*platforms.size()/2,0));
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
     * 分配员工给月台
     * @param emps
     * @param platforms
     * @return
     */
    private static void isEmpToPlatformIsNull(List<Emp> emps, List<Platform> platforms,double platform_a) {

        if (isEmpIsNull(emps)&&isPlatformEmpNotNull(platforms)){
            for (Platform platform:platforms){
                if (platform.getCarLine()==null||platform.getCarLine().getTrays().size()==0){
                    platform.setStatus(0);
                    platform.setEmps(null);
                    platform.setCarLine(null);
                    return;
                }
                if (platform.getCarLine()!=null&&platform.getStatus()==1) {
                    for (Emp emp : emps) {
                        if (platform.getEmps() == null && emp.getStatus() == 0) {
                            emp.setStatus(1);
                            platform.addEmp(emp);
                            emp.setT2(platform_a);
                            emp.setTar(platform.getPosition());
                            emp.setTary(platform.getCarLine().getTrays().get(0));
                            platform.getCarLine().removeCar();
                        }else if (platform.getStatus()==1&&platform.getEmps()!=null&&emp.getStatus()==0&&platform.getEmps().get(0).getName().equals(emp.getName())){
                            emp.setStatus(1);
                            platform.addEmp(emp);

                            emp.setT2(platform_a);
                            emp.setTar(platform.getPosition());
                            emp.setTary(platform.getCarLine().getTrays().get(0));
                            platform.getCarLine().removeCar();
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断是否月台人员为空
      * @param platforms
     * @return
     */
    private static boolean isPlatformEmpNotNull(List<Platform> platforms) {
        boolean isNull = true;
         for (Platform platform:platforms){
             if (platform.getCarLine()==null){
                  isNull = false;
             }
         }
         return isNull;
    }

    /**
     * 判断是否员工都在忙
     * @param emps
     * @return
     */
    private static boolean isEmpIsNull(List<Emp> emps) {
        boolean isNull = false;
        for (Emp emp: emps){
            if (emp.getStatus()==0) {
                isNull = true;
            }
        }
        return isNull;
    }

    /**
     * 为空的月台添加车辆
     * @param park
     * @param platforms
     */
    private static void PlatforAddParkCar(Park park, List<Platform> platforms) {
        while (isPlatformNotNull(platforms)&&parkIsNotNull(park)) {
            for (Platform platform : platforms) {
                if ((platform.getCarLine()==null||platform.getCarLine().getTrays()==null||platform.getCarLine().getTrays().size()==0)&&parkIsNotNull(park)) {
                    platform.setCarLine(park.getCar()); //获取停车区域月台
                    park.getCars().remove(0);
                    platform.setStatus(1);
                }
            }
        }

    }

    /**
     * 获取停车区域是否为空
     * @param park
     * @return
     */
    private static boolean parkIsNotNull(Park park) {
        boolean isnull = false;
        if (park.getCars()!=null&&park.getCars().size()>0){
            isnull = true;
        }
        return isnull;
    }

    /**
     * 获取车辆到达的方法
     */
    private static void AriveCar(List<Car> carsMap, Park park, List<Platform> platforms) {
        if (!isPlatformNotNull(platforms)){
           for (Car car: carsMap){
               park.add(car);

           }
        }else {
                PlatformAddCarFormArrive(carsMap,platforms);
        }

    }

    private static void PlatformAddCarFormArrive(List<Car> carsMap, List<Platform> platforms) {
        if (isPlatformNotNull(platforms)){
            for (Platform platform:platforms){
                if (platform.getCarLine()==null){
                    platform.addCarLine(carsMap.get(0)); //获取停车区域月台
                    carsMap.remove(carsMap.get(0));
                    platform.setStatus(1);
                    return;
                }
            }
        }

    }

    /**
     * 判断到来车辆是否已经分配完成
     * @param carsMap
     * @return
     */
    private static boolean ArriveCarIsNotNull(List<Car> carsMap) {
        boolean isnull = false;
        if (carsMap.size()>0){
            isnull = true;
        }
        return isnull;
    }

    /**
     * 判断月台是否为空
     * @param platforms
     * @return
     */
    private static boolean isPlatformNotNull(List<Platform> platforms) {
        boolean isnull = false;
        for (Platform platform:platforms){
             if (platform.getCarLine()==null){
                 isnull = true;
             }
        }
        return isnull;
    }


}
