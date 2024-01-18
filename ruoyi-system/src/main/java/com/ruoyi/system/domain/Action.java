package com.ruoyi.system.domain;


import com.ruoyi.system.utils.DateUtils;
import com.ruoyi.system.form.*;
import com.ruoyi.system.process.*;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.equipment.*;
import com.ruoyi.system.resource.facilities.buffer.Park;
import com.ruoyi.system.resource.facilities.buffer.Tally;
import com.ruoyi.system.resource.facilities.platform.Platform;
import com.ruoyi.system.resource.facilities.storage.Storage;
import com.ruoyi.system.resource.personnel.Emp;
import com.ruoyi.system.result.*;
import com.ruoyi.system.utils.DateUtils;
import com.ruoyi.system.utils.RandomUtil;
import com.ruoyi.system.utils.WarehousingUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.system.utils.DateUtils.randomDate;

public class Action {
    private static Date runTime = DateUtils.convertString2Date("HH:mm:ss", "08:00:00");//当前时间
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private static Date startTime = new Date();//开始时间
    private static Date endTime = new Date();//工作结束时间

    /**
     * 月台卸货初始化类
     *
     * @param carLength               车辆类型
     * @param transportNum            运输托数
     * @param unloading_time          卸货车辆数
     * @param everyDay_unloading_time 每日卸货时间
     * @param platform_width          月台宽度
     * @param platform_length         月台长度
     * @param supplier                供应商数量
     * @param goods_num               物料数量
     * @param utilization             最大利用率
     * @return
     */
    public static Result runPlatform(String carLength, double transportNum, double unloading_time, double everyDay_unloading_time, double platform_width, double platform_length, double supplier, double goods_num, double utilization, double batch, double platform_a) {
        Platform platform = new Platform();
        int parking_num = platform.getParking_num(carLength, transportNum, unloading_time, everyDay_unloading_time, batch); //计算月台数量
        double area = platform.getPlatformArea(parking_num, platform_width, platform_length);
        List<Supplier> suppliers = WarehousingUtil.initSupplier(supplier);
        List<Goods> list = WarehousingUtil.createGoodsMessage(goods_num, suppliers);
        List<Car> cars = WarehousingUtil.initCar(list, transportNum, carLength);
        List<Platform> platforms = WarehousingUtil.initPlat(parking_num, platform_width);
        List<Emp> emps = WarehousingUtil.initEmp(10);
        Park park = new Park();
        park.initPoints(cars.size(), 2);
        Result result = new Result();
        result.setCarNum(cars.size());
        double distance = 0.0;
        double rate = 0.0;
        List<EmpLog> list1 = Upload.work(cars, platforms, emps, park, platform_a);
        for (EmpLog empLog : list1) {
            distance += empLog.getDistance();
            rate += empLog.getEmpStatus();
        }


        result.setPlatformArea(area);
        result.setPlatformNum(parking_num);
        result.setDistance(distance);

        result.setUploadRate(rate / 10 / 60 / 60 / emps.size());
        int emp = (int) Math.ceil(emps.size() * everyDay_unloading_time / 8);
        int emp1 = (int) Math.ceil(emps.size() * everyDay_unloading_time / 8 * result.getUploadRate() / (utilization / 100));
        result.setUploadEmp(emp1 + (int) Math.ceil(batch * 3));
        result.setUploadRate(emp * result.getUploadRate() / emp1);
        return result;
    }

    /**
     * @param transportNum     理货总托盘量
     * @param batch            理货批次
     * @param tallyEmpCapacity 理货行走速度
     * @param tally_channel    理货通道
     * @param tray_clearance
     * @return
     */
    public static Result getTally(double transportNum, String carLength, double batch, double tallyEmpCapacity, double tally_channel, double tray_clearance, double utilization, double tallyTime, double goods_num) {
        Tally tally = new Tally();
        tally = tally.getTallyArea(transportNum, carLength, batch, tally_channel, tray_clearance);
        Tray tray = new Tray();

        tally.initTally(tally.getTally_transverse(), tally.getTally_longitudinal(), tally_channel, tray_clearance, tray.getWidth(), tray.getLength());
        List<Goods> list = WarehousingUtil.createGoods(goods_num);
        List<Tray> trays = Tray.initTrays(list, transportNum);

        List<Emp> emps = WarehousingUtil.initEmp(tally.getTally_transverse());

        double distance = 0.0;
        double rate = 0.0;
        List<EmpLog> list1 = Tallying.work(tally, trays, tallyEmpCapacity, emps, batch, tallyTime);
        for (EmpLog empLog : list1) {
            distance += empLog.getDistance();
            rate += empLog.getEmpStatus();
        }

        Result result = new Result();
        result.setTallyArea(tally.getArea());
        result.setDistance(distance);
        result.setTallyRate(rate / 10 / 60 / 60 / emps.size());
        result.setTally_transverse(tally.getTally_transverse());
        result.setTally_longitudinal(tally.getTally_longitudinal());
        int emp = (int) Math.ceil(emps.size());
        int emp1 = (int) Math.ceil(emps.size() * result.getTallyRate() / (utilization / 100));
        result.setTallyEmp(emp1 + (int) Math.sqrt(batch * 4));
        result.setTallyRate(emp * result.getTallyRate() / emp1);
        return result;
    }

    public static Result getPutaway(double putawayNum, double storageNum, double height, double forklift_channel, double utilization, double putaway_speed, double shelf_space, double shelf_height) {
        Storage storage = new Storage();
        storage = storage.getHightStorage(storageNum, height, forklift_channel, shelf_space, shelf_height);
        List<Goods> list = WarehousingUtil.createGoods(putawayNum);
        List<Cargo> cargos = WarehousingUtil.initCargos(list, storageNum, storage);
        storage.initStorage(storage, list);
        List<Emp> emps = WarehousingUtil.initEmp((int) (putawayNum / 50));
        List<Tray> trays = Tray.initTrays(list, putawayNum);
        double distance = 0.0;
        double rate = 0.0;
        List<EmpLog> list1 = Putaway.work(storage, trays, putaway_speed, emps);
        for (EmpLog empLog : list1) {
            distance += empLog.getDistance();
            rate += empLog.getEmpStatus();
        }

        Result result = new Result();
        result.setStorageArea(storage.getArea());
        result.setDistance(distance);
        result.setCargo(storage.getCargo());
        result.setPutawayRate(rate / 10 / 60 / 60 / emps.size());
        int emp = (int) Math.ceil(emps.size());
        int emp1 = (int) Math.ceil(emps.size() * result.getPutawayRate() / (utilization / 100));
        result.setPutawayEmp(emp1);
        result.setPutawayRate(emp * result.getPutawayRate() / emp1);
        return result;
    }

    public static Result getStorage(double storageNum, double height, double forklift_channel, double shelf_space, double shelf_height) {
        Storage storage = new Storage();
        storage = storage.getHightStorage(storageNum, height, forklift_channel, shelf_space, shelf_height);
        Result result = new Result();
        result.setStorageArea(storage.getArea());
        result.setCargo(storage.getCargo());
        result.setRow(storage.getRow());
        result.setLine(storage.getLine());
        result.setLayer(storage.getLayer());
        return result;
    }

    public static Result getTakeDown(double take_downNum, double storageNum, double height, double forklift_channel, double utilization, double putaway_speed, double shelf_space, double shelf_height) {
        Storage storage = new Storage();
        storage = storage.getHightStorage(storageNum, height, forklift_channel, shelf_space, shelf_height);
        List<Goods> list = WarehousingUtil.createGoods(take_downNum);
        List<Cargo> cargos = WarehousingUtil.initCargos(list, storageNum, storage);
        storage.initStorage(storage, list);
        List<Emp> emps = WarehousingUtil.initEmp((int) (take_downNum / 50));
        List<Tray> trays = Tray.initTrays(list, take_downNum);
        double distance = 0.0;
        double rate = 0.0;
        List<EmpLog> list1 = TakeDown.work(storage, trays, putaway_speed, emps);
        for (EmpLog empLog : list1) {
            distance += empLog.getDistance();
            rate += empLog.getEmpStatus();
        }

        Result result = new Result();
        result.setStorageArea(storage.getArea());
        result.setDistance(distance);
        result.setCargo(storage.getCargo());
        result.setTakeDownEmpRate(rate / 10 / 60 / 60 / emps.size());
        int emp = (int) Math.ceil(emps.size());
        int emp1 = (int) Math.ceil(emps.size() * result.getTakeDownEmpRate() / (utilization / 100));
        result.setTakeDownEmp(emp1);
        result.setTakeDownEmpRate(emp * result.getTakeDownEmpRate() / emp1);
        return result;
    }

    public static Result getSorting(double transportNum, double batch, int orderLine, double sortingSpeed, double tally_channel, double tray_clearance, double utilization, String sort_type, double goods_num, double sortingTime) {
        Tally tally = new Tally();
        tally = tally.getTallyArea(transportNum, "小车4米6", batch, tally_channel, tray_clearance);
        Tray tray = new Tray();

        tally.initTally(tally.getTally_transverse() * orderLine, tally.getTally_longitudinal() * orderLine, tally_channel, tray_clearance, tray.getWidth(), tray.getLength());
        List<Goods> list = WarehousingUtil.createGoods(goods_num);
        List<Tray> trays = Tray.initTrays(list, transportNum * orderLine);
        List<Emp> emps = WarehousingUtil.initEmp(tally.getTally_transverse());

//        double distance = 0.0;
//        double rate = 0.0;
//        List<EmpLog> list1 = Sorting.work(tally, trays, sortingSpeed, emps, batch, sort_type, orderLine / 2, sortingTime);
//        for (EmpLog empLog : list1) {
//            distance += empLog.getDistance();
//            rate += empLog.getEmpStatus();
//        }

        Result result = new Result();
        result.setSortingArea(tally.getArea());
//        result.setDistance(distance);
//        result.setSortingRate(rate / 10 / 60 / 60 / emps.size());
        result.setTally_longitudinal(tally.getTally_longitudinal());
        result.setTally_transverse(tally.getTally_transverse());
//        int emp = (int) Math.ceil(emps.size());
//        int emp1 = (int) Math.ceil(emps.size() * result.getSortingRate() / (utilization / 100));
//        result.setSortingEmp(emp1 + (int) Math.sqrt(batch));
//        result.setSortingRate(emp * result.getSortingRate() / emp1);
        return result;
    }

    public static Result getDelivery(double deliveryNum, double batch, double delivery_speed, double tally_channel, double tray_clearance, double utilization, double delivery_Time) {
        Tally tally = new Tally();
        tally = tally.getTallyArea(deliveryNum, "小车4米6", batch, tally_channel, tray_clearance);
        Tray tray = new Tray();

        tally.initTally(tally.getTally_transverse(), tally.getTally_longitudinal(), tally_channel, tray_clearance, tray.getWidth(), tray.getLength());
        List<Goods> list = WarehousingUtil.createGoods(150);
        List<Tray> trays = Tray.initTrays(list, deliveryNum);

        List<Emp> emps = WarehousingUtil.initEmp(tally.getTally_transverse());

        double distance = 0.0;
        double rate = 0.0;
        List<EmpLog> list1 = Delivery.work(tally, trays, delivery_speed, emps, batch, delivery_Time);
        for (EmpLog empLog : list1) {
            distance += empLog.getDistance();
            rate += empLog.getEmpStatus();
        }

        Result result = new Result();
        result.setDeliveryArea(tally.getArea());
        result.setDistance(distance);
        result.setDeliveryRate(rate / 10 / 60 / 60 / emps.size());
        int emp = (int) Math.ceil(emps.size());
        int emp1 = (int) Math.ceil(emps.size() * result.getDeliveryRate() / (utilization / 100));
        result.setDeliveryEmp(emp1 + (int) batch);
        result.setDeliveryRate(emp * result.getDeliveryRate() / emp1);
        return result;

    }

    public static Result runLoading(String carLength, double transportNum, double unloading_time, double everyDay_unloading_time, double platform_width, double platform_length, double customer, double goods_num, double utilization, double platform_a) {
        Platform platform = new Platform();
        int parking_num = platform.getParking_num(carLength, transportNum, unloading_time, everyDay_unloading_time, 2);
        double area = platform.getPlatformArea(parking_num, platform_width, platform_length);
        List<Customer> coustomers = WarehousingUtil.initCustomer(customer);
        List<Goods> list = WarehousingUtil.createGoodsDeliveryMessage(goods_num, coustomers);
        List<Car> cars = WarehousingUtil.initCar1(list, transportNum, carLength);
        List<Platform> platforms = WarehousingUtil.initPlat(parking_num, platform_length);
        List<Emp> emps = WarehousingUtil.initEmp(parking_num);
        Park park = new Park();
        park.initPoints(cars.size(), 2);
        Result result = new Result();
        result.setCarNum(cars.size());
        double distance = 0.0;
        double rate = 0.0;
        List<EmpLog> list1 = Loading.work(cars, platforms, emps, park, platform_a);
        for (EmpLog empLog : list1) {
            distance += empLog.getDistance();
            rate += empLog.getEmpStatus();
        }


        result.setPlatformArea(area);
        result.setPlatformNum(parking_num);
        result.setDistance(distance);

        result.setUploadRate(rate / 12 / 60 / 60 / emps.size());
        int emp = (int) Math.ceil(emps.size() * everyDay_unloading_time / 8);
        int emp1 = (int) Math.ceil(emps.size() * everyDay_unloading_time / 8 * result.getUploadRate() / (utilization / 100));
        result.setUploadEmp(emp1);
        result.setUploadRate(emp * result.getUploadRate() / emp1);
        return result;

    }

    public static List<EmpLog> getPutawayLog(double putawayNum, double storageNum, double height, double forklift_channel, double utilization, double putaway_speed, double shelf_space, double shelf_height) {
        Storage storage = new Storage();
        storage = storage.getHightStorage(storageNum, height, forklift_channel, shelf_space, shelf_height);
        List<Goods> list = WarehousingUtil.createGoods(putawayNum);
        List<Cargo> cargos = WarehousingUtil.initCargos(list, storageNum, storage);
        storage.initStorage(storage, list);
        List<Emp> emps = WarehousingUtil.initEmp((int) (putawayNum / 50));
        List<Tray> trays = Tray.initTrays(list, putawayNum);
        List<EmpLog> list1 = Putaway.work(storage, trays, putaway_speed, emps);
        return list1;
    }

    public static List<Result> getPutawayOrder(double putawayNum, double storageNum, double height, double forklift_channel, double utilization, double putaway_speed, double shelf_space, double shelf_height) {
        Storage storage = new Storage();
        storage = storage.getHightStorage(storageNum, height, forklift_channel, shelf_space, shelf_height);
        List<Goods> list = WarehousingUtil.createGoods(putawayNum);
        List<Cargo> cargos = WarehousingUtil.initCargos(list, storageNum, storage);
        storage.initStorage(storage, list);
        List<Emp> emps = WarehousingUtil.initEmp((int) (putawayNum / 50));
        List<Tray> trays = Tray.initTrays(list, putawayNum);
        List<Result> results = new ArrayList<>();
        Random random = new Random();
        for (Tray tray : trays) {
            Result result = new Result();
            result.setOrder_code(RandomUtil.toFixdLengthString(random.nextInt(1000000), 8));
            result.setGoods_code(tray.getGoodsList().get(0).getGoodsCode());
            result.setTime(sdf.format(DateUtils.randomDate("2021-08-01 08:00:00", "2021-08-01 18:00:00")));
            result.setGoods_num(tray.getGoodsList().get(0).getPlutNum());
            results.add(result);
        }
        return results;
    }

    public static List<EmpLog> getPutawayEmp(double putawayNum, double storageNum, double height, double forklift_channel, double utilization, double putaway_speed, double shelf_space, double shelf_height) {
        Storage storage = new Storage();
        storage = storage.getHightStorage(storageNum, height, forklift_channel, shelf_space, shelf_height);
        List<Goods> list = WarehousingUtil.createGoods(putawayNum);
        List<Cargo> cargos = WarehousingUtil.initCargos(list, storageNum, storage);
        storage.initStorage(storage, list);
        List<Emp> emps = WarehousingUtil.initEmp((int) (putawayNum / 50));
        List<Tray> trays = Tray.initTrays(list, putawayNum);

        List<EmpLog> list1 = Putaway.work(storage, trays, putaway_speed, emps);
        List<EmpLog> empLog = new ArrayList<>();
        int k = 0;
        int statues = 0;
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getEmpStatus() == 1) {
                statues++;
            }
            if (k == emps.size()) {
                EmpLog empLog1 = new EmpLog();
                empLog1.setTime(list1.get(i).getTime());
                empLog1.setEmpStatus(statues);
                empLog.add(empLog1);
                statues = 0;
                k = 0;
            }
            k++;
        }

        return empLog;
    }

    public static List<Result> runRuOrder(double transportNum, double supplier, double goods_num, double volatility, double iq) {
        List<Supplier> suppliers = WarehousingUtil.initSupplier(supplier);
        List<Goods> list = WarehousingUtil.createGoodsMessage(goods_num, suppliers);
        int carSize = 16;
        int carNum = (int) (Math.ceil(transportNum / carSize));
        Map<Supplier, List<Goods>> outOrdersList = list.stream().collect(Collectors.groupingBy(Goods::getSupplier));//出库单
        List<List<Goods>> goodsList = new ArrayList<>();
        for (Supplier supplier1 : outOrdersList.keySet()) {
            goodsList.add(outOrdersList.get(supplier1));
        }
        List<Result> results = new ArrayList<>();
        Random random = new Random();
        int[] nums = WarehousingUtil.splitInteger(list.size(), (int) transportNum * 90, false);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPlutNum(nums[i]);
        }


        for (int i = 0; i < carNum * 90; i++) {
            Car car = new Car();
            car.setCarNo("川A" + RandomUtil.toFixdLengthString(random.nextInt(1000), 5));
            car.setArrinveTime(randomDate("2021-01-01 08:00:00", "2021-01-01 18:00:00"));
            car.setPoint(new Point(0, 0, 0, 1));
            car.setGoodsList(goodsList.get(i % goodsList.size()));
            String orederCode = "ADZ" + RandomUtil.toFixdLengthString(random.nextInt(1000), 5);
            for (Goods goods : car.getGoodsList()) {
                Result result = new Result();
                result.setTime(sdf.format(car.getArrinveTime()));
                result.setOrder_code(orederCode);
                result.setGoods_code(goods.getGoodsCode());
                result.setRow(goods.getCases());
                result.setLine(goods.getNum());
                result.setDateTime(sdf1.format(randomDate("2021-01-01 08:00:00", "2021-03-01 18:00:00")));
                result.setSullier(goods.getSupplier().getSupplierCode());
                results.add(result);
            }
        }

        double[] numl = WarehousingUtil.splitDouble(results.size(), transportNum * 90, false);
        int k = 0;
        for (Result result : results) {
            result.setGoods_num2(new BigDecimal(numl[k]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + (int) WarehousingUtil.random(-1, 1));
            result.setGoods_num1((int) result.getGoods_num2() * result.getRow());
            result.setGoods_num((int) result.getGoods_num1() * result.getLine());
            k++;
        }
        return results;
    }

    public static List<Result1> getInventory(double storageNum, double height, double goods_num, double shelf_space, double shelf_height) {
        Storage storage = new Storage();
        storage = storage.getHightStorage(storageNum, height, 3.2, shelf_space, shelf_height);
        List<Goods> list = WarehousingUtil.createGoods(goods_num);
        List<Cargo> cargos = WarehousingUtil.initCargos(list, storageNum, storage);
        List<Result1> results = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < storageNum; i++) {
            Result1 result1 = new Result1();
            result1.setGoods_code(list.get(i % (int) (goods_num - 1)).getGoodsCode());
            result1.setGoods_num(WarehousingUtil.random(1, 1000));
            result1.setRow((int) (cargos.get(i % cargos.size()).getPoint().getX() / 1.2));
            result1.setLine((int) cargos.get(i % cargos.size()).getPoint().getZ());
            result1.setLayer((int) (cargos.get(i % cargos.size()).getPoint().getY() / shelf_height));
            results.add(result1);
        }

        return results;
    }

    public static List<Result2> getSortingOrder(double transportNum, int orderLine, double goods_num, double iq) {
        List<Goods> list = WarehousingUtil.createGoods(goods_num);
        List<Tray> trays = Tray.initTrays(list, transportNum * orderLine);
        List<Result2> results = new ArrayList<>();
        Random random = new Random();
        for (Tray tray : trays) {
            String orederCode = "ADZ" + RandomUtil.toFixdLengthString(random.nextInt(1000), 5);
            String time = sdf.format(DateUtils.randomDate("2021-01-01 08:00:00", "2021-01-01 18:00:00"));
            for (Goods goods : tray.getGoodsList()) {
                Result2 result = new Result2();
                result.setTime(time);
                result.setOrder_code(orederCode);
                result.setGoods_code(goods.getGoodsCode());
                result.setRow(goods.getCases());
                result.setLine(goods.getNum());
                result.setDateTime(sdf1.format(randomDate("2021-01-01 08:00:00", "2021-03-01 18:00:00")));
                results.add(result);
            }

        }
        double[] numl = WarehousingUtil.splitDouble(results.size(), transportNum * 90, false);
        int k = 0;
        for (Result2 result : results) {
            result.setGoods_num2(new BigDecimal(numl[k]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + (int) WarehousingUtil.random(-1, 1));
            result.setGoods_num1((int) result.getGoods_num2() * result.getRow());
            result.setGoods_num((int) result.getGoods_num1() * result.getLine());
            k++;
        }
        return results;
    }

    public static List<Result3> getDeliveryOrder(double transportNum, int orderLine, double goods_num, double iq) {
        List<Goods> list = WarehousingUtil.createGoods(goods_num);
        List<Tray> trays = Tray.initTrays1(list, transportNum, orderLine);
        List<Result3> results = new ArrayList<>();
        Random random = new Random();
        for (Tray tray : trays) {
            String orederCode = RandomUtil.toFixdLengthString(random.nextInt(1000), 5);
            String time = sdf.format(DateUtils.randomDate("2021-01-01 08:00:00", "2021-01-01 18:00:00"));
            for (Goods goods : tray.getGoodsList()) {
                Result3 result = new Result3();
                result.setTime(time);
                result.setOrder_code(orederCode);
                result.setGoods_code(goods.getGoodsCode());
                result.setRow(goods.getCases());
                result.setLine(goods.getNum());
                result.setDateTime(sdf1.format(randomDate("2021-01-01 08:00:00", "2021-03-01 18:00:00")));
                results.add(result);
            }

        }
        double[] numl = WarehousingUtil.splitDouble(results.size(), transportNum * 90, false);
        int k = 0;
        for (Result3 result : results) {
            result.setGoods_num2(new BigDecimal(numl[k]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + (int) WarehousingUtil.random(-1, 1));
            result.setGoods_num1((int) result.getGoods_num2() * result.getRow());
            result.setGoods_num((int) result.getGoods_num1() * result.getLine());
            k++;
        }
        return results;
    }

//    public static List<Point> initStorageShelf(double shelf_length, double shelf_width, double shelf_height,double xRow,double yRow,double yColumnNum,double xColumnNum,double zColumnNum,double xSpacing,double ySpacing) {
//        MWNumericArray a = null;
//        MWNumericArray b = null;
//        MWNumericArray c = null;
//        MWNumericArray d = null;
//        MWNumericArray e1 = null;
//        MWNumericArray f = null;
//        MWNumericArray g = null;
//        MWNumericArray h = null;
//        MWNumericArray i = null;
//        MWNumericArray j = null;
//         Layout layout = null ;
//        Object[] result = null;
//        try {
//            layout= new Layout();
//            a = new MWNumericArray(Double.valueOf(shelf_length), MWClassID.DOUBLE);
//            b = new MWNumericArray(Double.valueOf(shelf_width),MWClassID.DOUBLE);
//            c = new MWNumericArray(Double.valueOf(shelf_height),MWClassID.DOUBLE);
//            d = new MWNumericArray(Double.valueOf(xRow),MWClassID.DOUBLE);
//            e1 = new MWNumericArray(Double.valueOf(yRow),MWClassID.DOUBLE);
//            f = new MWNumericArray(Double.valueOf(yColumnNum),MWClassID.DOUBLE);
//            g = new MWNumericArray(Double.valueOf(xColumnNum),MWClassID.DOUBLE);
//            h = new MWNumericArray(Double.valueOf(zColumnNum),MWClassID.DOUBLE);
//            i = new MWNumericArray(Double.valueOf(xSpacing),MWClassID.DOUBLE);
//            j = new MWNumericArray(Double.valueOf(ySpacing),MWClassID.DOUBLE);
//            result = layout.palletSingleShelvesLayout(1,a,b,c,d,e1,f,g,h,i,j);
//        } catch (MWException e) {
//            e.printStackTrace();
//        }
//        String path = System.getProperty("user.dir")+"\\dat.mat";
//        File file1 = new File(path);
//        Matrix sampleData= ImportMatrixMAT.fromFile(file1);
//        double[][] doubles = new double[][]{};
//        doubles = sampleData.toDoubleArray();
//        List<Point> list = new ArrayList<>();
//        for (int s=0;s<doubles.length;s++){
//            list.add(new Point(doubles[s][0],doubles[s][1],doubles[s][2]));
//        }
//        return list;
//
//    }
}
