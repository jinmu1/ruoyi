package com.ruoyi.system.utils;


import com.ruoyi.system.enumType.CarType;
import com.ruoyi.system.form.*;
import com.ruoyi.system.queue.Order;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.equipment.Elevator;
import com.ruoyi.system.resource.equipment.Tray;
import com.ruoyi.system.resource.facilities.buffer.Tally;
import com.ruoyi.system.resource.facilities.platform.Platform;
import com.ruoyi.system.resource.facilities.storage.Storage;
import com.ruoyi.system.resource.personnel.Emp;
import org.apache.commons.collections4.ListUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.ruoyi.system.utils.DateUtils.randomDate;
import static java.util.stream.Collectors.*;
public class WarehousingUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");


    public static void initTime(Date runTime, Date startTime, Date endTime) {
        startTime = randomDate("2019-12-24 08:00:00", "2019-12-24 08:01:00");
        runTime = randomDate("2019-12-24 08:00:00", "2019-12-24 08:01:00");
        endTime = randomDate("2019-12-24 24:00:00", "2019-12-24 24:01:00");
    }

    public static List<Platform> initPlat(double num,double platform_width) {
        List<Platform> platforms = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            platforms.add(new Platform("月台" + i + "号", 0,  new Point(0 + i * platform_width, 0, 0)));
        }
        return platforms;
    }

    /**
     * 初始化门洞
     */
    public static List<Point> initDoor() {
        List<Point> door = new ArrayList<>();
        door.add(new Point(8, 3, 0));
        door.add(new Point(34, 3, 0));
        door.add(new Point(40, 3, 0));
        return door;
    }

    /**
     * 初始化电梯
     */
    public static List<Elevator> initElevator(int num, int floor) {
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int f = (int) (Math.random() * floor);
            elevators.add(new Elevator("电梯" + 0 + "号", 3, f, WorkTime.e_v0, WorkTime.e_v1, 0, new Point(23, 0, f)));
        }
        return elevators;
    }

    /**
     * 获取电梯区域位置
     *
     * @return
     */
    public static List<Point> initElevatorPark() {
        List<Point> points = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            if (i != 2) {
                points.add(new Point(23, 1, (i - 1) * 5));
            }
        }
        return points;
    }

    /**
     * 获取月台工作人员
     *
     * @param emp
     * @return
     */
    public static Point getPlatform(Emp emp, List<Platform> platforms) {
        Point point = new Point();
        for (Platform platform : platforms) {
            if (emp.getCode().equals(platform.getCode())) {
                point = platform.getPosition();
            }
        }
        return point;
    }

    /**
     * 获取月台工作人员
     *
     * @param
     * @return
     */
    public static Platform getPlatform(List<Platform> platforms) {
        Platform point = new Platform();
        for (Platform platform : platforms) {
            if (platform.getStatus()==0) {
              point = platform;
              platform.setStatus(1);
              return point;
            }
        }
        return point;
    }

    public static Point getPath(Emp emp, double num) {
        Point curr = emp.getCurr();
        Point dest = emp.getTar();
        Point curLocation = new Point(curr.getX(), curr.getY(), curr.getZ());
        if (getDistance(curr,dest)<=3){
            return dest;
        }
        if (Math.abs(curLocation.getX() - dest.getX()) >= num) {
            double x1Diff = Math.abs(dest.getX() - curLocation.east(num).getX());  //go east, x + 1
            double x2Diff = Math.abs(dest.getX() - curLocation.west(num).getX());  //go west, x - 1
            if (x1Diff <= x2Diff) {
                Point newPt = new Point(curLocation.east(num).getX(), curLocation.getY(), curLocation.getZ());
                curLocation = newPt;
            } else {
                Point newPt = new Point(curLocation.west(num).getX(), curLocation.getY(), curLocation.getZ());
                curLocation = newPt;
            }
        }else if (Math.abs(curLocation.getY() - dest.getY()) >= num) {
            double y1Diff = Math.abs(dest.getY() - curLocation.north(num).getY()); //go north, y + 1
            double y2Diff = Math.abs(dest.getY() - curLocation.south(num).getY()); //go south, y - 1
            if (y1Diff <= y2Diff) {
                Point newPt = new Point(curLocation.getX(), curLocation.north(num).getY(), curLocation.getZ());
                curLocation = newPt;
            } else {
                Point newPt = new Point(curLocation.getX(), curLocation.south(num).getY(), curLocation.getZ());
                curLocation = newPt;
            }
        }
        return curLocation;
    }

    /**
     * 获取当前到达坐标和目标坐标的距离
     *
     * @param point
     * @param tar
     * @return
     */
    public static double getDistance(Point point, Point tar) {
        return Math.abs(point.getX() - tar.getX()) + Math.abs(point.getY() - tar.getY());
    }

    /**
     * 到达理货区
     *
     * @param tally
     * @param tar
     * @param i
     */
    public static void emptys(Tally tally, Point tar, int i) {
        Iterator<Point> iterator = tally.getPoints().iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            if (getDistance(point, tar) == 0) {
                point.setStatus(i);
            }
        }

    }

    /**
     * 获取当前理货区物料
     *
     * @return
     */
    public static List<Goods> getGoodsPullt(Tally tally) {
        List<Goods> goods = new ArrayList<>();
        List<Goods> goodsList = tally.getGoodsList();
        for (int i = 0; i < goodsList.size() / tally.getTorr(); i++) {
            goods.add(goodsList.get(i));
        }
        Iterator<Goods> iterator = tally.getGoodsList().iterator();
        while (iterator.hasNext()) {
            Goods goods1 = iterator.next();
            int m = 0;
            for (Goods goods2 : goods) {
                if (goods1.getGoodsCode().equals(goods2.getGoodsCode()) && goods1.getPlutNum() == goods2.getPlutNum() && m == 0) {
                    iterator.remove();
                    m++;
                }
            }
        }
        return goods;
    }

    /**
     * 获取电梯
     *
     * @param emp
     * @return
     */
    public static Point getPark(Emp emp, List<Point> elevatorPark) {
        Point p = new Point();
        for (Point point : elevatorPark) {
            if (point.getZ() == emp.getCurr().getZ()) {
                p = point;
            }
        }
        return p;
    }

    /**
     * 获取最快的电梯
     *
     * @param num
     * @param elevators
     * @return
     */
    public static Elevator waitEle(double num, List<Elevator> elevators) {
        int i = 1000;
        Elevator elevator = new Elevator();
        for (Elevator elevator1 : elevators) {
            if (elevator1.getStatus() == 0 && Math.abs(elevator1.getFloor() - num) < i) {
                elevator = elevator1;
                i = elevator1.getFloor();
            }
        }
        return elevator;
    }

    /**
     * 设置电梯是否运行
     *
     * @param elevator
     * @param status
     * @param elevators
     */
    public static void freeEle(Elevator elevator, int status, List<Elevator> elevators) {
        for (Elevator elevator1 : elevators) {
            if (elevator != null && elevator.getCode() != null && elevator.getCode().equals(elevator1.getCode())) {
                elevator1.setStatus(status);
            }
        }
    }

    /**
     * 获取路径
     *
     * @param curr
     * @param dest
     * @param num
     * @return
     */
    public static Point getPath1(Point curr, Point dest, double num) {
        Point curLocation = new Point(curr.getX(), curr.getY(), curr.getZ());
        if (Math.abs(curLocation.getX() - dest.getX()) >= 1) {
            double x1Diff = Math.abs(dest.getX() - curLocation.east(num).getX());  //go east, x + 1
            double x2Diff = Math.abs(dest.getX() - curLocation.west(num).getX());  //go west, x - 1
            if (x1Diff <= x2Diff) {
                Point newPt = new Point(curLocation.east(num).getX(), curLocation.getY(), curLocation.getZ());
                curLocation = newPt;
            } else {
                Point newPt = new Point(curLocation.west(num).getX(), curLocation.getY(), curLocation.getZ());
                curLocation = newPt;
            }
        } else if (Math.abs(curLocation.getY() - dest.getY()) >= 1) {
            double y1Diff = Math.abs(dest.getY() - curLocation.north(num).getY()); //go north, y + 1
            double y2Diff = Math.abs(dest.getY() - curLocation.south(num).getY()); //go south, y - 1
            if (y1Diff <= y2Diff) {
                Point newPt = new Point(curLocation.getX(), curLocation.north(num).getY(), curLocation.getZ());
                curLocation = newPt;
            } else {
                Point newPt = new Point(curLocation.getX(), curLocation.south(num).getY(), curLocation.getZ());
                curLocation = newPt;
            }
        } else if (curLocation.getZ() != dest.getZ()) {
            double z1Diff = Math.abs(dest.getZ() - curLocation.up(num).getZ()); //go north, y + 1
            double z2Diff = Math.abs(dest.getZ() - curLocation.down(num).getZ()); //go south, y - 1
            if (z1Diff <= z2Diff) {
                Point newPt = new Point(curLocation.getX(), curLocation.getY(), curLocation.up().getZ());
                curLocation = newPt;
            } else {
                Point newPt = new Point(curLocation.getX(), curLocation.getY(), curLocation.down().getZ());
                curLocation = newPt;

            }
        } else {
            curLocation = dest;
        }
        return curLocation;
    }

    /**
     * 高层理货区
     *
     * @param tar
     * @param i
     */
    public static void emptys1(Point tar, int i, Tally tally) {
        Iterator<Point> iterator = tally.getPoints().iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            if (getDistance(point, tar) == 0) {
                point.setStatus(i);
            }
        }
    }

    /**
     * 获取高层理货区
     *
     * @param tally
     * @return
     */
    public static Point getTally1(Tally tally) {
        Point point = new Point();
        for (Point point1 : tally.getPoints()) {
            if (point1.getStatus() == 0) {
                point = point1;
                break;
            }
        }
        return point;
    }

    /***
     * 获取物料存储区位置
     * @param emp
     * @param cargos
     * @return
     */
    public static Point getGoodsPosition(Emp emp, List<Cargo> cargos) {
        Point point = new Point(35, 15, 15);
        List<Goods> list = emp.getGoods();
        if (list.size() > 0) {
            Goods goods = list.get(0);
            for (Cargo cargo : cargos) {
                if (cargo.getGoods() != null && cargo.getPoint() != null && cargo.getGoods().getGoodsCode() != null && goods != null && cargo.getGoods().getGoodsCode().equals(goods.getGoodsCode())) {
                    point = cargo.getPoint();
                }
            }
            emp.getGoods().remove(0);
        }
        return point;
    }

    /**
     * 从车辆上搬出货物
     *
     * @param code
     */
    public static void removeCar(String code, List<Platform> platforms) {
        Iterator<Platform> platformIterator = platforms.iterator();
        while (platformIterator.hasNext()) {
            Platform platform = platformIterator.next();
            if (platform.getCode().equals(code)) {
                platform.getCarLine().removeCar();


                if (platform.getCarLine().getTora() <= 0) {
                    platform.setStatus(-1);
                    platform.setSign(WorkTime.T);
                }
            }
        }
    }

    /**
     * 获取仓库门洞位置
     *
     * @param curr
     * @return
     */
    public static Point getDoor(Point curr, List<Point> door) {
        double num = 1000000;
        Point point = new Point();
        for (Point point1 : door) {
            if (num > (Math.abs(point1.getX() - curr.getX()) + Math.abs(point1.getY() - curr.getY()))) {
                num = Math.abs(point1.getX() - curr.getX()) + Math.abs(point1.getY() - curr.getY());
                point = point1;
            }
        }
        return point;
    }

    /**
     * 获取缓存区存储点
     *
     * @param tally
     * @return
     */
    public static Point getTally(Tally tally) {
        Point point = new Point();
        for (Point point1 : tally.getPoints()) {
            if (point1.getStatus() == 0) {
                point = point1;
                break;
            }
        }
        return point;
    }


    public static List<Emp> initEmp(int s) {
        List<Emp> emps = new ArrayList<>();
        for (int i = 0; i < s; i++) {
            emps.add(new Emp("上架" + i + "号", 0, new Point(0, 0, 0)));
        }
        return emps;

    }

    public static Tally initTally() {
        Tally tally = new Tally();
        List<Point> points = new ArrayList<>();
        for (int i = 10; i < 20; i++) {
            for (int j = 4; j < 12; j++) {
                points.add(new Point(i, j, 0, 0));
            }
        }
        tally.setPoints(points);
        tally.setTorr(0);
        tally.setTorr(0);
        return tally;


    }

    public static Tally initTally1() {
        Tally tally1 = new Tally();
        List<Point> points1 = new ArrayList<>();
        for (int i = 20; i < 32; i++) {
            for (int j = 4; j < 12; j++) {
                if (i < 24 || i > 28) {
                    points1.add(new Point(i, j, 10, 0));
                }
            }
        }
//        tally1.setArea(points1);
        return tally1;
    }
    /**
     * 为每个物料分配物料名称
     *
     * @param
     * @param rangeNum
     * @return
     */
    public static List<Goods> createGoodsMessage(double rangeNum,List<Supplier> suppliers) {
        List<Goods> materialList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < rangeNum; i++) {
            Goods material = new Goods();
            material.setGoodsCode(RandomUtil.toFixdLengthString(random.nextInt(1000000), 8));
            material.setVolume(initNormalDistribution1(100,0.05)[(int)random(0,99)]);
            material.setSupplier(suppliers.get((int)random(0,suppliers.size())));
            material.setCases((int)random(8,50));
            material.setNum((int)random(1,10));
            materialList.add(material);
        }
        return materialList;

    }
    public static List<Goods> createGoodsDeliveryMessage(double rangeNum,List<Customer> customerList) {
        List<Goods> materialList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < rangeNum; i++) {
            Goods material = new Goods();
            material.setGoodsCode(RandomUtil.toFixdLengthString(random.nextInt(1000000), 8));
            material.setVolume(initNormalDistribution1(100,0.05)[(int)random(0,99)]);
            material.setCustomer(customerList.get((int)random(0,customerList.size())));
            materialList.add(material);
        }
        return materialList;

    }
    /**
     * 为每个物料分配物料名称
     *
     * @param
     * @param rangeNum
     * @return
     */
    public static List<Goods> createGoods(double rangeNum) {
        List<Goods> materialList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < rangeNum; i++) {
            Goods material = new Goods();
            material.setGoodsCode(RandomUtil.toFixdLengthString(random.nextInt(1000000), 8));
            material.setPlutNum(random(1,40));
            material.setCases((int)random(8,50));
            material.setNum((int)random(1,10));
            materialList.add(material);
        }
        return materialList;

    }
    public static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }
    /**
     * 生成订单
     *
     * @param
     * @return
     */
    public static List<Order> initOrders(List<Goods> list, double transportNum) {
        List<Order> orderList = new ArrayList<>();
        for (int i =0;i< list.size()*transportNum/100;i++){
            orderList.add(new Order());
        }
        Random random = new Random();
        for (Order order : orderList) {
            String OrderCode = "D" + RandomUtil.toFixdLengthString(random.nextInt(10000), 4);
            String orderDate = sdf.format(randomDate("2021-01-01 08:00:00", "2021-12-31 18:00:00"));
            order.setOrderCode(OrderCode);
            order.setGoodsCode(list.get((int)random(0,list.size())).getGoodsCode());
            order.setGoodsNum(random(0,5));
            try {
                order.setCreateDate(sdf.parse(orderDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return orderList;
    }

    /**
     * 生成订单
     *
     * @param
     * @return
     */
    public static List<Emp> initEmpSortingOrder(List<Emp> emps1, List<Order> orderList, String sort_type) {

        if (sort_type.equals("批量拣选")) {
            List<Order> orders = new ArrayList<>();
            Map<String, List<Order>> outOrdersList = orderList.stream().collect(Collectors.groupingBy(Order::getGoodsCode));//出库单
            Random random = new Random();
            for (String goods : outOrdersList.keySet()) {
                double num = 0.0;
                for (Order orders1 : outOrdersList.get(goods)) {
                    num += orders1.getGoodsNum();
                }
                String orderCode = "D" + RandomUtil.toFixdLengthString(random.nextInt(10000), 4);
                orders.add(new Order(orderCode,goods,num));
            }
        }
        List<List<Order>> list = ListUtils.partition(orderList, emps1.size());
        int i = 0;
        for (Emp emp : emps1) {
            emp.setOrders(list.get(i));
            emp.setT0(list.get(i).size());
            i++;
        }
        return emps1;
    }


    public static List<Emp> initEmpOrder(List<Emp> emps1, List<Order> orderList) {
        List<List<Order>> list = ListUtils.partition(orderList, emps1.size());
        int i = 0;
        for (Emp emp : emps1) {
            emp.setOrders(list.get(i));
            emp.setT0(list.get(i).size());
            i++;
        }
        return emps1;
    }

    public static List<Cargo> initCargo(List<Order> list, double total) {
        LinkedList<Goods> materials = new LinkedList<>();
        total = 0.0;//总托
        List<Cargo> cargos = new ArrayList<>();
        for (int i = 1; i <= AreaUtils.getHightStorage(total, total).getLayer(); i++) {
            for (int j = 1; j <= AreaUtils.getHightStorage(total, total).getLine(); j++) {
                for (int k = 1; k <= AreaUtils.getHightStorage(total, total).getRow(); k++) {
                    Goods goods1 = materials.poll();
                    Point point = new Point(i, j, k);
                    cargos.add(new Cargo(point, goods1));
                }
            }
        }
        return cargos;
    }
    public static List<Cargo> initCargos(List<Goods> list, double total, Storage storage) {
        LinkedList<Goods> materials = new LinkedList<>(list);
        List<Cargo> cargos = new ArrayList<>();
        for (int i = 1; i <= storage.getLayer(); i++) {
            for (int j = 1; j <= storage.getLine(); j++) {
                for (int k = 1; k <= storage.getRow(); k++) {
                    Goods goods1 = materials.poll();
                    Point point = new Point(i, j, k);
                    cargos.add(new Cargo(point, goods1));
                }
            }
        }
        return cargos;
    }
    public static List<Supplier> initSupplier(double num) {
        List<Supplier> suppliers = new ArrayList<>();
        Random random = new Random();
        for (int i=0;i<=num;i++){
                Supplier supplier = new Supplier();
                supplier.setSupplierCode(RandomUtil.toFixdLengthString(random.nextInt(10000000),8));
                supplier.setLeadTime(random(3,6));
                suppliers.add(supplier);
        }
        return suppliers;
    }
    public static List<Customer> initCustomer(double num) {
        List<Customer> customerList = new ArrayList<>();
        Random random = new Random();
        for (int i=0;i<=num;i++){
            Customer customer = new Customer();
            customer.setCustomerCode(RandomUtil.toFixdLengthString(random.nextInt(10000000),8));
            customerList.add(customer);
        }
        return customerList;
    }


    public static int[] splitInteger(int n, int sum,boolean flag) {
        //随机抽取n-1个小于sum的数
        List<Integer> list = new ArrayList();
        //将0和sum加入到里list中
        list.add(0);
        //判断生成的正整数集合中是否允许为0，true元素可以为0  false元素不可以为0
        if (!flag) {
            sum = sum - n;
        }
        list.add(sum);
        int temp = 0;
        for (int i = 0; i < n - 1 ; i++) {
            temp = (int) (Math.random() * sum);
            list.add(temp);
        }
        Collections.sort(list);
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = list.get(i + 1) - list.get(i);
            if (!flag) {
                nums[i] += 1;
            }
        }
        return nums;
    }
    public static double[] splitDouble(int n, double sum,boolean flag) {
        //随机抽取n-1个小于sum的数
        List<Double> list = new ArrayList();
        //将0和sum加入到里list中
        list.add(0.0);
        //判断生成的正整数集合中是否允许为0，true元素可以为0  false元素不可以为0
        if (!flag) {
            sum = sum - n;
        }
        list.add(sum);
        double temp = 0;
        for (int i = 0; i < n - 1 ; i++) {
            temp = (Math.random() * sum);
            list.add(temp);
        }
        Collections.sort(list);
        double[] nums = new double[n];
        for (int i = 0; i < n; i++) {
            nums[i] = list.get(i + 1) - list.get(i);
            if (!flag) {
                nums[i] += 1;
            }
        }
        return nums;
    }
    public static List<Goods> initMaterialVolume(List<Goods> list, double b, double m, double s) {
        double all = b + m + s;
        Random random = new Random();
        double num3 = 0.0;
        int i = 0;
        for (Goods material : list){
            if (i<=s/all*list.size()){
                material.setVolume(initNormalDistribution1(100,0.05)[(int)random(0,99)]);
                num3 += material.getVolume();
            }else if (i<=(m+s)/all*list.size()){
                material.setVolume(initNormalDistribution1(100,0.45)[(int)random(0,99)]+0.05);
                num3 += material.getVolume();
            }else if (i<=list.size()){
                material.setVolume(initNormalDistribution1(100,4.5)[(int)random(0,99)]+0.5);
                num3 += material.getVolume();
            }
            i++;
        }
        System.out.println(num3);
        return list;

    }

    public static double[] initNormalDistribution1(int length,double max){
        double[] num = new double[length];
        for (int i = 0;i<length;i+=1){
            num[i] = Math.abs(NormalDistribution(500,(float)10000));
        }
        double avg = MathUtils.sum(num)/num.length;
        for (int i = 0; i<num.length;i++){
            num[i] = num[i]*max/avg;
        }
        return num;
    }


    //普通正态随机分布
    //参数 u 均值
    //参数 v 方差
    public static double NormalDistribution(float u,float v){
        Random random = new Random();
        return Math.sqrt(v)*random.nextGaussian()+u;
    }

    public static List<Goods> initMaterialSupplier(List<Supplier> suppliers, List<Goods> list) {
        Random random = new Random();
        for (Goods material:list){
            int i = (int)random(1,suppliers.size());
            material.setSupplier(suppliers.get(i));
        }
        return list;
    }

    public static List<Customer> initCustomer(int customerNum) {
        Random random = new Random();
        List<Customer> customerList = new ArrayList<>();


            for (int i=0;i<=customerNum;i++){
                Customer customer = new Customer();
                customer.setCustomerCode(RandomUtil.toFixdLengthString(random.nextInt(10000000),8));
                customerList.add(customer);
            }



        return customerList;
    }

    public static List<Order> initOrdersCustomerList(List<Order> orderList, List<Customer> customerList) {
        Random random = new Random();
        for (Order order:orderList){
            int i = random.nextInt(customerList.size());
            order.setCustomerCode(customerList.get(i).getCustomerCode());
        }
        return  orderList;
    }

    public static List<Order> getReplenishment(List<Order> orders, List<Goods> list) {

        Map<Date, List<Order>> outOrdersList = orders.stream().collect(Collectors.groupingBy(Order::getCreateDate));//出库单
        List<Order> orderList = new ArrayList<>();
        List<Order> runOrder = new ArrayList<>();
        double replenishmentDate = 0.0;
        int i = 0;
        Random random = new Random();
        double[] in = new double[outOrdersList.keySet().size()];
        double[] out = new double[outOrdersList.keySet().size()];
        for (Date date : outOrdersList.keySet()) {
            List<Order> list1 = outOrdersList.get(date);
            if (runOrder.size() > 0) {
                list1.addAll(runOrder);
                runOrder = new ArrayList<>();
            }
            Map<String, List<Order>> listMap = list1.stream().collect(Collectors.groupingBy(Order::getGoodsCode));//出库单
            double ins = 0.0;
            double out1 = 0.0;
            for (Goods material : list) {
                List<Order> list2 = listMap.get(material.getGoodsCode());
                if (list2 != null && list2.size() != 0) {
                    for (Order order : list2) {
                        material.setPlutNum(material.getPlutNum() - order.getGoodsNum());
                        out1 += order.getGoodsNum() * material.getVolume();
                    }
                    if (material.getPlutNum() < material.getVolume()) {
                        replenishmentDate = material.getSupplier().getLeadTime();

                    }


                }
            }
            in[i] = ins;
            out[i] = out1;
        }
//        for(Order order:orderList){
//            order.setCustomerCode(getCustomerCode(order.getGoodsCode()));
//        }

        return orderList;
    }

    public static List<Car> initCar(List<Goods> list, double transportNum,String carLength) {
        List<Car> carList = new ArrayList<>();
        int carSize = Integer.parseInt(CarType.valueOf(carLength).getCode());
        int carNum = (int)(Math.ceil(transportNum/carSize)*1.5);
        Map<Supplier, List<Goods>> outOrdersList = list.stream().collect(Collectors.groupingBy(Goods::getSupplier));//出库单
        List<List<Goods>> goodsList = new ArrayList<>();
        for(Supplier supplier:outOrdersList.keySet()){
            goodsList.add(outOrdersList.get(supplier));
        }
        Random random = new Random();
        for(int i= 0;i<carNum;i++){
            Car car=new Car();
            car.setCarNo("川A"+RandomUtil.toFixdLengthString(random.nextInt(1000),5));
            car.setArrinveTime(randomDate("2021-01-01 08:00:00", "2021-01-01 18:00:00"));
            car.setPoint(new Point(0,0,0,1));
            car.setGoodsList(goodsList.get(i%goodsList.size()));
            List<Tray> trays = new ArrayList<>();
            List<List<Goods>> goods =averageAssign(car.getGoodsList(),carSize);
            for (int m=0;m<carSize;m++){
                Tray tray  =new Tray(goods.get(m));
                trays.add(tray);
            }
            car.setTrays(trays);
            carList.add(car);
        }
        return  carList;
    }



    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source,int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
    /**
     * 从集合中取组合
     * @param list
     * @param k
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> combinations(List<T> list, int k) {
        if (k == 0 || list.isEmpty()) {//去除K大于list.size的情况。即取出长度不足K时清除此list
            return Collections.emptyList();
        }
        if (k == 1) {//递归调用最后分成的都是1个1个的，从这里面取出元素
            return list.stream().map(e -> Stream.of(e).collect(toList())).collect(toList());
        }
        Map<Boolean, List<T>> headAndTail = split(list, 1);
        List<T> head = headAndTail.get(true);
        List<T> tail = headAndTail.get(false);
        List<List<T>> c1 = combinations(tail, (k - 1)).stream().map(e -> {
            List<T> l = new ArrayList<>();
            l.addAll(head);
            l.addAll(e);
            return l;
        }).collect(toList());
        List<List<T>> c2 = combinations(tail, k);
        c1.addAll(c2);
        return c1;
    }

    /**
     *根据n将集合分成两组
     **/
    public static <T> Map<Boolean, List<T>> split(List<T> list, int n) {
        return IntStream
                .range(0, list.size())
                .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, list.get(i)))
                .collect(partitioningBy(entry -> entry.getKey() < n, mapping(AbstractMap.SimpleEntry::getValue, toList())));
    }

    public static Tally initTally2(Tally tally) {
        Tray tray =new Tray();
        tally.initTally((int)tally.getTally_transverse(),(int)tally.getTally_longitudinal(),tally.forklift_channel,tally.tally_channel,tray.getWidth(),tray.getLength());
        return tally;
    }

    public static List<Car> initCar1(List<Goods> list, double transportNum, String carLength) {
        List<Car> carList = new ArrayList<>();
        int carSize = Integer.parseInt(CarType.valueOf(carLength).getCode());
        int carNum = (int)(Math.ceil(transportNum/carSize)*1.5);
        Map<Customer, List<Goods>> outOrdersList = list.stream().collect(Collectors.groupingBy(Goods::getCustomer));//出库单
        List<List<Goods>> goodsList = new ArrayList<>();
        for(Customer customer:outOrdersList.keySet()){
            goodsList.add(outOrdersList.get(customer));
        }
        Random random = new Random();
        for(int i= 0;i<carNum;i++){
            Car car=new Car();
            car.setCarNo("川A"+RandomUtil.toFixdLengthString(random.nextInt(1000),5));
            car.setArrinveTime(randomDate("2021-01-01 08:00:00", "2021-01-01 18:00:00"));
            car.setPoint(new Point(0,0,0,1));
            car.setGoodsList(goodsList.get(i%goodsList.size()));
            List<Tray> trays = new ArrayList<>();
            List<List<Goods>> goods =averageAssign(car.getGoodsList(),carSize);
            for (int m=0;m<carSize;m++){
                Tray tray  =new Tray(goods.get(m));
                trays.add(tray);
            }
            car.setTrays(trays);
            carList.add(car);
        }
        return  carList;
    }




}
