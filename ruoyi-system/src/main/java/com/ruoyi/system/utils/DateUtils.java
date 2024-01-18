package com.ruoyi.system.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 *
 * @author luox
 * @version 1.0
 * @since 2017-09-10
 */
public final class DateUtils {

    public static final long SECOND = 1000;

    public static final long MINUTE = SECOND * 60;

    public static final long HOUR = MINUTE * 60;

    public static final long DAY = HOUR * 24;

    public static final long WEEK = DAY * 7;

    public static final long YEAR = DAY * 365;

    public static final String FOMTER_TIMES = "yyyy-MM-dd HH:mm:ss";

    private static final Map<Integer, String> WEEK_DAY = new HashMap<Integer, String>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    static {
        WEEK_DAY.put(7, "星期六");
        WEEK_DAY.put(1, "星期天");
        WEEK_DAY.put(2, "星期一");
        WEEK_DAY.put(3, "星期二");
        WEEK_DAY.put(4, "星期三");
        WEEK_DAY.put(5, "星期四");
        WEEK_DAY.put(6, "星期五");
    }

    private static final Map<String, Integer> DATE_FIELD = new HashMap<String, Integer>();

    static {
        DATE_FIELD.put("y", Calendar.YEAR);
        DATE_FIELD.put("M", Calendar.MONTH);
        DATE_FIELD.put("d", Calendar.DATE);
        DATE_FIELD.put("H", Calendar.HOUR);
        DATE_FIELD.put("m", Calendar.MINUTE);
        DATE_FIELD.put("s", Calendar.SECOND);
        DATE_FIELD.put("w", Calendar.WEDNESDAY);
    }

    /**
     * 解析日期
     *
     * @param date    日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String date, String pattern) {
        Date resultDate = null;
        try {
            resultDate = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }
    public static Date randomDate(String beginDate,String endDate){
        try {
            Date start = sdf.parse(beginDate);
            Date end = sdf.parse(endDate);
            if(start.getTime() >= end.getTime()){
                return null;
            }
            long date = random(start.getTime(),end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }
    /**
     * 解析日期 yyyy-MM-dd
     *
     * @param date    日期字符串
     * @return
     */
    public static Timestamp parseSimple(String date) {
        Date result = null;
        try {
            DateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
            result = yyyyMMdd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result != null ? new Timestamp(result.getTime()) : null;
    }

    /**
     * 解析日期字符串
     *
     * @param date
     * @return
     */
    public static Timestamp parseFull(String date) {
        Date result = null;
        try {
            DateFormat yyyyMMddHHmmss = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            result = yyyyMMddHHmmss.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result != null ? new Timestamp(result.getTime()) : null;
    }
    /**
     * 获取固定间隔时刻集合
     * @param start 开始时间
     * @param end 结束时间
     * @param interval 时间间隔(单位：分钟)
     * @return
     */
    public static List<String> getIntervalTimeList(String start,String end,int interval){
        Date startDate = convertString2Date("HH:mm:ss",start);
        Date endDate = convertString2Date("HH:mm:ss",end);
        List<String> list = new ArrayList<>();
        while(startDate.getTime()<=endDate.getTime()){
            list.add(convertDate2String("HH:mm:ss",startDate));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE,interval);
            if(calendar.getTime().getTime()>endDate.getTime()){
                if(!startDate.equals(endDate)){
                    list.add(convertDate2String("HH:mm:ss",endDate));
                }
                startDate = calendar.getTime();
            }else{
                startDate = calendar.getTime();
            }

        }
        return list;
    }
    public static Date convertString2Date(String format, String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date date = simpleDateFormat.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String convertDate2String(String format,Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 解析日期 yyyy-MM-dd
     *
     * @param
     * @return
     */
    public static Timestamp parse(Object object) {
        if (object instanceof Date) {
            return new Timestamp(((Date) object).getTime());
        }
        if (StringUtils.isEmpty(object))
            return null;
        String date = StringUtils.asString(object);
        try {
            if (date.length() == 10) {
                return parseSimple(date);
            } else if (date.length() == 8) {
                DateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                Date d = yyyyMMdd.parse(date);
                return new Timestamp(d.getTime());
            } else if (date.length() == 9) {
                if (date.matches("\\d{4}-\\d-\\d{2}")) {//yyyy-M-dd
                    DateFormat yyyyMdd = new SimpleDateFormat("yyyy-M-dd");
                    Date d = yyyyMdd.parse(date);
                    return new Timestamp(d.getTime());
                } else if (date.matches("\\d{4}-\\d{2}-\\d")) {//yyyy-MM-d
                    DateFormat yyyyMdd = new SimpleDateFormat("yyyy-MM-d");
                    Date d = yyyyMdd.parse(date);
                    return new Timestamp(d.getTime());
                }
            } else if (date.length() == 16) {//yyyy-MM-dd HH:mm
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date d = format.parse(date);
                return new Timestamp(d.getTime());
            } else if (date.length() == 18) {//yyyy-MM-ddHH:mm:ss 医保返回日期可能出现
                DateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                Date d = format.parse(date);
                return new Timestamp(d.getTime());
            } else if (date.length() == 19) {//yyyy-MM-dd HH:mm:ss
                return parseFull(date);
            } else if (date.length() >= 20 && date.length() <= 23) {//yyyy-MM-dd HH:mm:ss.SSS
                if (date.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{1,3}")) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date d = format.parse(date);
                    return new Timestamp(d.getTime());
                }
            } else {
                return Timestamp.valueOf(object.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期字符串
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return yyyy年MM月dd日
     */
    public static String formatCHS(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    /**
     * 格式化日期字符串
     *
     * @param date 日期
     * @return
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
        return YYYY_MM_DD.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatFull(Date date) {
        DateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return YYYY_MM_DD_HH_MM_SS.format(date);
    }

    /**
     * 取得当前日期
     *
     * @return
     */
    public static Timestamp getNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 取得当前日期
     *
     * @return
     */
    public static Integer getNowYear() {
        return getYear(getNow());
    }

    /**
     * 取得当前月份
     *
     * @return
     */
    public static Integer getNowMonth() {
        return getMonth(getNow());
    }

    /**
     * 取得年度
     *
     * @param value
     * @return
     */
    public static Integer getYear(Object value) {
        Calendar c = Calendar.getInstance();
        Date date = getDate(value);
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 取得月份
     *
     * @param value
     * @return
     */
    public static Integer getMonth(Object value) {
        Calendar c = Calendar.getInstance();
        Date date = getDate(value);
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 取得日
     *
     * @param value
     * @return
     */
    public static Integer getDay(Object value) {
        Calendar c = Calendar.getInstance();
        Date date = getDate(value);
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得日期对象
     *
     * @param value
     * @return
     */
    private static Date getDate(Object value) {
        Date date = null;
        if (value instanceof Date) {
            date = (Date) value;
        } else {
            date = parse((String) value);
        }
        if (date == null) {
            throw new RuntimeException("日期格式解析错误!date=" + value);
        }
        return date;
    }

    /**
     * 取得年龄
     *
     * @param value
     * @return
     */
    @SuppressWarnings("all")
    public static Integer getAge(Object value) {
        Timestamp date = DateUtils.parse(value);
        if (date == null) {
            return null;
        }
        Timestamp now = DateUtils.getNow();
        int year1 = date.getYear(), year2 = now.getYear();
        int month1 = date.getMonth(), month2 = now.getMonth();
        int date1 = date.getDate(), date2 = now.getDate();
        int months = (year2 - year1) * 12 + month2 - month1;
        if (date1 > date2) {
            months = months - 1;
        }
        int age = months / 12;
        return age;
    }

    /**
     * @param offsetYear
     * @return 当前时间 + offsetYear
     */
    public static Timestamp getNowExpiredYear(int offsetYear) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, offsetYear);
        return new Timestamp(now.getTime().getTime());
    }

    /**
     * @param offset
     * @return 当前时间 + offsetMonth
     */
    public static Timestamp getNowExpiredMonth(int offset) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, offset);
        return new Timestamp(now.getTime().getTime());
    }

    /**
     * @param offset
     * @return 当前时间 + offsetDay
     */
    public static Timestamp getNowExpiredDay(int offset) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, offset);
        return new Timestamp(now.getTime().getTime());
    }

    /**
     * @param offset
     * @return 当前时间 + offsetDay
     */
    public static Timestamp getNowExpiredHour(int offset) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, offset);
        return new Timestamp(now.getTime().getTime());
    }

    /**
     * @param offsetSecond
     * @return 当前时间 + offsetSecond
     */
    public static Timestamp getNowExpiredSecond(int offsetSecond) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, offsetSecond);
        return new Timestamp(now.getTime().getTime());
    }

    /**
     * @param offset
     * @return 当前时间 + offset
     */
    public static Timestamp getNowExpiredMinute(int offset) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, offset);
        return new Timestamp(now.getTime().getTime());
    }

    /**
     * @param offset
     * @return 指定时间 + offsetDay
     */
    public static Timestamp getExpiredDay(Date givenDate, int offset) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.DATE, offset);
        return new Timestamp(date.getTime().getTime());
    }

    /**
     * 实现ORACLE中ADD_MONTHS函数功能
     *
     * @param offset
     * @return 指定时间 + offsetMonth
     */
    public static Timestamp getExpiredMonth(Date givenDate, int offset) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.MONTH, offset);
        return new Timestamp(date.getTime().getTime());
    }

    /**
     * @return 指定时间 + offsetSecond
     */
    public static Timestamp getExpiredYear(Date givenDate, int offsetHour) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.YEAR, offsetHour);
        return new Timestamp(date.getTime().getTime());
    }

    /**
     * @param second
     * @return 指定时间 + offsetSecond
     */
    public static Timestamp getExpiredSecond(Date givenDate, int second) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.SECOND, second);
        return new Timestamp(date.getTime().getTime());
    }

    /**
     * 计算时间差
     *
     * @param givenDate 日期
     * @param offset    2
     * @param type      日期字段类型
     * @return
     */
    public static Timestamp getExpired(Date givenDate, int offset, Integer type) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(type, offset);
        return new Timestamp(date.getTime().getTime());
    }

    /**
     * 根据日期取得日历
     *
     * @param date
     * @return
     */
    public static Calendar getCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * @return 指定时间 + offsetSecond
     */
    public static Timestamp getExpiredHour(Date givenDate, int offsetHour) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.HOUR, offsetHour);
        return new Timestamp(date.getTime().getTime());
    }

    /**
     * @return 给出指定日期的月份的第一天
     */
    public static Date getMonthFirstDay(Date givenDate) {
        Date date = DateUtils.parse(DateUtils.format(givenDate, "yyyy-MM"),
                "yyyy-MM");
        return date;
    }

    /**
     * 取得当前是周几？
     *
     * @param givenDate
     * @return
     */
    public static int getDayOfWeek(Date givenDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(givenDate);
        int day = c.get(Calendar.DAY_OF_WEEK);
        return day;
    }

    /**
     * 取得中文星期？
     *
     * @param dayOfWeek
     * @return
     */
    public static String getChineseDayOfWeek(int dayOfWeek) {
        return WEEK_DAY.get(dayOfWeek);
    }

    /**
     * 给定日期是否在范围内
     *
     * @param date  给定日期
     * @param begin 开始日期
     * @param end   结束日期
     * @return true 在指定范围内
     */
    public static Boolean between(Date date, Date begin, Date end) {
        if (date == null || begin == null || end == null) {
            return true;
        }
        return date.after(begin) && date.before(end);
    }

    /**
     * 当前日期是否在范围内
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return true 在指定范围内
     */
    public static Boolean between(Date begin, Date end) {
        Date now = getNow();
        return between(now, begin, end);
    }

    /**
     * 取得今天零点日期
     *
     * @return
     */
    public static Calendar getTodayZero() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    /**
     * 是否是日期格式
     *
     * @param value
     * @return
     */
    public static boolean isDate(String value) {
        return parse(value) != null;
    }

    /**
     * <p>
     * Parses a string representing a date by trying a variety of different
     * parsers.
     * </p>
     * <p>
     * <p>
     * The parse will try each parse pattern in turn. A parse is only deemed
     * sucessful if it parses the whole of the input string. If no parse
     * patterns match, a ParseException is thrown.
     * </p>
     *
     * @param str           the date to parse, not null
     * @param parsePatterns the date format patterns to use, see SimpleDateFormat, not
     *                      null
     * @return the parsed date
     * @throws IllegalArgumentException if the date string or pattern array is null
     * @throws ParseException           if none of the date patterns were suitable
     */
    public static Date parseDate(String str, String[] parsePatterns)
            throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException(
                    "Date and Patterns must not be null");
        }
        SimpleDateFormat parser = null;
        ParsePosition pos = new ParsePosition(0);
        for (int i = 0; i < parsePatterns.length; i++) {
            if (i == 0) {
                parser = new SimpleDateFormat(parsePatterns[0]);
            } else {
                parser.applyPattern(parsePatterns[i]);
            }
            pos.setIndex(0);
            Date date = parser.parse(str, pos);
            if (date != null && pos.getIndex() == str.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }

    /**
     * 取得java.sql.Date
     *
     * @param value
     * @return
     */
    public static java.sql.Date getSqlDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return new java.sql.Date(((Date) value).getTime());
        } else if (value instanceof String) {
            Timestamp date = parse((String) value);
            return date != null ? new java.sql.Date(date.getTime()) : null;
        }
        return null;
    }

    /**
     * 取得系统日期变量
     *
     * @return
     */
    public static Map<String, Object> getDateVariableMap() {
        Map<String, Object> dateMap = new HashMap<String, Object>();
        Date now = getNow();
        dateMap.put("now", now);// 当前时间
        dateMap.put("year", getYear(now));// 年
        dateMap.put("month", getMonth(now));// 月
        dateMap.put("day", getDay(now));// 日
        dateMap.put("simple", format(now));// 简单日期
        dateMap.put("full", formatFull(now));// 全日期
        dateMap.put("chs", formatCHS(now));// 中文日期
        dateMap.put("year_ago", getYear(now) - 1);//去年年份
        dateMap.put("month_next", getExpiredMonth(now, 1));// 下月
        dateMap.put("month_pre", getExpiredMonth(now, -1));// 上月
        dateMap.put("quarter_next", getExpiredMonth(now, 3));// 下季度
        dateMap.put("quarter_pre", getExpiredMonth(now, -3));// 上季度
        dateMap.put("year_next", getExpiredMonth(now, 12));// 明年
        dateMap.put("year_pre", getExpiredMonth(now, -12));// 今年
        dateMap.put("day_next", getExpiredDay(now, 1));// 明天
        dateMap.put("day_pre", getExpiredDay(now, -1));// 昨天
        dateMap.put("day_pre_simple", format(getExpiredDay(now, -1)));
        dateMap.put("day_net_week_simple", format(getExpiredDay(now, 7)));//下周
        dateMap.put("day_pre_week_simple", format(getExpiredDay(now, -7)));//上周
        int month = DateUtils.getMonth(now);
        int year = DateUtils.getYear(now);
        int yearjs = (month > 9 ? year + 1 : year);
        dateMap.put("yearjs", yearjs);// 计生年度
        dateMap.put("yearjs_pre", yearjs - 1);// 计生年度去年
        dateMap.put("yearjs_next", yearjs + 1);// 计生年度明年
        return dateMap;
    }
    public static Date addSeconds(Date date, int amount) {
        return date;
    }
    /**
     * 计算日期表达式
     *

     * @return
     */
    public static Date getExpired(Object givenDate, String off) {
        Date date = null;
        if (givenDate instanceof Date) {
            date = (Date) givenDate;
        } else {
            date = parse(StringUtils.asString(givenDate));
        }
        String op = String.valueOf(off.charAt(0));
        int offset = Integer.parseInt(String.valueOf(off.charAt(1)));
        String type = String.valueOf(off.charAt(2));
        Date result = null;
        int dateField = DATE_FIELD.get(type);
        if (op.equals("+")) {
            result = DateUtils.getExpired(date, +offset, dateField);
        } else if (op.equals("-")) {
            result = DateUtils.getExpired(date, -offset, dateField);
        }
        return result;
    }

    /**
     * 计算日期表达式
     *
     * @param expr today + 1d 或 2014-06-29 + 1d
     * @return
     */
    public static Date eval(String expr) {
        expr = expr.trim();
        if (expr.equals("today") || expr.equals("now")) {
            return getNow();
        }
        String EXPR = "(.+)\\s*(\\+|\\-)\\s*(\\w+)";
        String p1 = expr.replaceAll(EXPR, "$1").trim();
        String p2 = expr.replaceAll(EXPR, "$2").trim();
        String p3 = expr.replaceAll(EXPR, "$3").trim();
        Date date = null;
        if (p1.equals("today")) {
            date = getNow();
        } else {
            p1 = p1.replace("'", "").trim();
            date = parse(p1);
        }
        Date result = null;
        result = getExpired(date, p2 + p3);
        return result;
    }

    /**
     * 计算日期差
     *
     * @param a    日期A
     * @param b    日期B
     * @param type 类型
     * @return 计算结果
     */
    public static Integer sub(Object a, Object b, String type) {
        Date d1 = parse(a);//开始日期
        Date d2 = parse(b);//截止日期
        if (d1 == null || d2 == null) {
            return null;
        }
        Long result = null;
        long DAY = 24 * 60 * 60 * 1000;
        long sub = d1.getTime() - d2.getTime();
        long daysub = (sub / DAY);
        long y1 = d1.getYear(), y2 = d2.getYear();
        long m1 = d1.getMonth(), m2 = d2.getMonth();
        long monthsub = (y1 - y2) * 12 + (m1 - m2);
        if (type.equals("m")) {//月
            result = monthsub;
        } else if (type == "y") {//年
            result = monthsub / 12;
        } else if (type.equals("d")) {//天
            result = daysub;
        }
        return Integer.valueOf(result.intValue());
    }

    public static void main(String[] args) {
        System.out.println(getYear(new Date()));
        System.out.println(getMonth(new Date()));
        System.out.println(eval("today + 1d"));
        System.out.println(eval("today - 1d"));
        System.out.println(eval("today + 1M"));
        System.out.println(eval("today - 1M"));
        System.out.println(eval("today + 1y"));
        System.out.println(eval("today - 1y"));

        System.out.println(eval("2014-06-29 + 1d"));
        System.out.println(eval("2014-06-29 - 1d"));
        System.out.println(eval("2014-06-29 + 1M"));
        System.out.println(eval("2014-06-29 - 1M"));
        System.out.println(eval("2014-06-29 + 1y"));
        System.out.println(eval("2014-06-29 - 1y"));
        System.out.println(eval("2014-06-29 - 1w"));
        System.out.println(getExpired(new Date(), "-1M"));
        String now = DateUtils.getNow().toString();
        System.out.println(now + "=" + now + " parse=" + parse(now) + "");
        System.out.println(sub("2014-07-30", "2013-06-29", "d"));
        System.out.println(getAge("1982-09-29"));
        System.out.println(parse("2015-03-2519:35:21"));
    }
    /**
     * 切割時間段
     *
     * @param dateType  交易類型 M/D/H/N -->每月/每天/每小時/每分鐘
     * @param start yyyy-MM-dd HH:mm:ss
     * @param end   yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static List<String> cutDate(String dateType, String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dBegin = sdf.parse(start);
            Date dEnd = sdf.parse(end);
            return findDates(dateType, dBegin, dEnd);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public static List<String> findDates(String dateType, Date dBegin, Date dEnd) throws Exception {
        List<String> listDate = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (calEnd.after(calBegin)) {
            switch (dateType) {
                case "M":
                    calBegin.add(Calendar.MONTH, 1);
                    break;
                case "D":
                    calBegin.add(Calendar.DAY_OF_YEAR, 1);break;
                case "H":
                    calBegin.add(Calendar.HOUR, 1);break;
                case "N":
                    calBegin.add(Calendar.SECOND, 1);break;
            }
            if (calEnd.after(calBegin))
                listDate.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calBegin.getTime()));
            else
                listDate.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calEnd.getTime()));
        }
        return listDate;
    }

    /** 标准日期（不含时间）格式化器 */
    private final static SimpleDateFormat NORM_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /** 标准日期时间格式化器 */
    private final static SimpleDateFormat NORM_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /** HTTP日期时间格式化器 */
    private final static SimpleDateFormat HTTP_DATETIME_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
            Locale.US);

    /**
     *
     * 当前时间，格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串
     *
     */
    public static String now() {
        return formatDateTime(new Date());
    }

    /**
     *
     * 当前日期，格式 yyyy-MM-dd
     *
     * @return 当前日期的标准形式字符串
     *
     */
    public static String today() {
        return formatDate(new Date());
    }

    // ------------------------------------ Format start
    // ----------------------------------------------



    /**
     *
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     *            被格式化的日期
     *
     * @return 格式化后的日期
     *
     */
    public static String formatDateTime(Date date) {
        // return format(d, "yyyy-MM-dd HH:mm:ss");

        return NORM_DATETIME_FORMAT.format(date);
    }

    /**
     *
     * 格式化为Http的标准日期格式
     *
     * @param date
     *            被格式化的日期
     *
     * @return HTTP标准形式日期字符串
     *
     */
    public static String formatHttpDate(Date date) {
        // return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
        // Locale.US).format(date);

        return HTTP_DATETIME_FORMAT.format(date);
    }

    /**
     *
     * 格式 yyyy-MM-dd
     *
     * @param date
     *            被格式化的日期
     *
     * @return 格式化后的字符串
     *
     */
    public static String formatDate(Date date) {
        // return format(d, "yyyy-MM-dd");

        return NORM_DATE_FORMAT.format(date);
    }
    // ------------------------------------ Format end
    // ----------------------------------------------

    // ------------------------------------ Parse start
    // ----------------------------------------------

    /**
     *
     * 将特定格式的日期转换为Date对象
     *
     * @param dateString
     *            特定格式的日期
     *
     * @param format
     *            格式，例如yyyy-MM-dd
     *
     * @return 日期对象
     *
     */


    /**
     *
     * 格式yyyy-MM-dd HH:mm:ss
     *
     * @param dateString
     *            标准形式的时间字符串
     *
     * @return 日期对象
     *
     */


    /**
     *
     * 格式yyyy-MM-dd
     *
     * @param dateString
     *            标准形式的日期字符串
     *
     * @return 日期对象
     *
     */

    // ------------------------------------ Parse end
    // ----------------------------------------------

    /**
     *
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date
     *            基准日期
     *
     * @param calendarField
     *            偏移的粒度大小（小时、天、月等）使用Calendar中的常数
     *
     * @param offsite
     *            偏移量，正数为向后偏移，负数为向前偏移
     *
     * @return 偏移后的日期
     *
     */
    public static Date getOffsiteDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }

    /**
     *
     * 判断两个日期相差的时长<br/>
     *
     * 返回 minuend - subtrahend 的差
     *
     * @param subtrahend
     *            减数日期
     *
     * @param minuend
     *            被减数日期
     *
     * @param diffField
     *            相差的选项：相差的天、小时
     *
     * @return 日期差
     *
     */
    public static long dateDiff(Date subtrahend, Date minuend, long diffField) {
        long diff = minuend.getTime() - subtrahend.getTime();
        return diff / diffField;
    }

}
