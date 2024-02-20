package com.ruoyi.web.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.data.common.ObjectMap;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class with public static methods shared by Baidie's codebase.
 */
public class BaidieUtils {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * Parses content from file into a List of data, where data is of class T
     *
     * @param file  - given file
     * @param clazz - the type of data being returned.
     * @param <T>
     * @return - a List of data of type T
     * @throws Exception when input file is not excel format.
     */
    public static <T> List<T> parseFromExcelFile(MultipartFile file, Class<T> clazz) throws Exception {
        ExcelUtil<T> util = new ExcelUtil<>(clazz);
        return util.importExcel(file.getInputStream());
    }

    /***
     * 将Map转寒为JSONObject
     * @param keyToArray   a Map from json key to its corresponding data array.
     * @return JSONObject with all the keys and values.
     */
    public static JSONObject generateResponseJson(Map<String, List<?>> keyToArray) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        // 将 HashMap 中的键值对添加到 JsonObject 中
        for (Map.Entry<String, List<?>> entry : keyToArray.entrySet()) {
            jsonObject.put(entry.getKey(), convertListObjectsToJsonString(entry.getValue()));
        }
        return jsonObject;
    }

    /***
     * 该方法为Json 转换方法，将List对象转换为json的一个对象
     * @param object value 对象
     * @return object的json字符串
     * @throws JsonProcessingException 返回的转换异常，
     */
    private static String convertListObjectsToJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    /**
     * 返回value占total的百分比的string，小数点后两位精确度。
     *
     * @param value
     * @param total
     * @return
     */
    public static String toPercentageOfString(double value, double total) {
        return BigDecimal.valueOf(value).multiply(ONE_HUNDRED)
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_DOWN)
                + "%";
    }

    /**
     * 将一个数组转换为区间和区间值数量的方法
     *
     * @param data 需要统计的数组
     * @return 返回一个key是区间，value是该区间值的数量的一个对象
     */
    public static List<ObjectMap> generateIntervalData(double[] data,
                                                       int intervalNumber) {
        // 找到数据中的最小值和最大值
        double minValue = Arrays.stream(data).min().orElse(0);
        double maxValue = Arrays.stream(data).max().orElse(0);

        // 确定区间范围
        int intervalWidth = (int) Math.ceil((maxValue - minValue) / intervalNumber);

        // 创建区间和值的统计 List
        List<ObjectMap> intervalDataList = new ArrayList<>();
        for (int i = 0; i < intervalNumber; i++) {
            int intervalStart = (int) (minValue + i * intervalWidth);
            int intervalEnd = (int) (intervalStart + intervalWidth);
            String intervalKey = "[" + intervalStart + ", " + intervalEnd + ")";
            intervalDataList.add(new ObjectMap(intervalKey, 0.0));
        }

        // 统计每个区间的值的百分比
        int totalValues = data.length;
        for (double value : data) {
            String intervalKey = findInterval(value, intervalWidth, minValue);
            for (ObjectMap intervalData : intervalDataList) {
                if (intervalData.getKey().equals(intervalKey)) {
                    intervalData.setValue(intervalData.getValue() + 1);
                    break;
                }
            }
        }

        // 将数量转换为百分比
        DecimalFormat df = new DecimalFormat("#.##");
        for (ObjectMap intervalData : intervalDataList) {
            double percentage = (intervalData.getValue() / totalValues) * 100;
            // 将百分比保留小数点后两位
            String formattedPercentage = df.format(percentage);
            intervalData.setValue(Double.parseDouble(formattedPercentage));
        }

        return intervalDataList;
    }

    /**
     * 计算合适区间值的方式
     *
     * @param value         值
     * @param intervalWidth 区间宽度
     * @param minValue      最小值
     * @return 一个区间
     */
    private static String findInterval(double value, int intervalWidth, double minValue) {
        int intervalStart = ((int) ((value - minValue) / intervalWidth)) * intervalWidth + (int) minValue;
        int intervalEnd = intervalStart + intervalWidth;
        return "[" + intervalStart + ", " + intervalEnd + ")";
    }

    /**
     * 截取字段中的数字
     * @param input
     * @return
     */
    public static List<Integer> extractNumbers(String input) {
        List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            numbers.add(Integer.parseInt(matcher.group()));
        }
        return numbers;
    }
}