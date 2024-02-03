package com.ruoyi.web.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class with public static methods shared by Baidie's codebase.
 */
public class BaidieUtils {
    /**
     * Parses content from file into a List of data, where data is of class T
     * @param file - given file
     * @param clazz - the type of data being returned.
     * @return - a List of data of type T
     * @param <T>
     * @throws Exception when input file is not excel format.
     */
    public static <T> List<T> parseFromExcelFile(MultipartFile file, Class<T> clazz) throws Exception {
        ExcelUtil<T> util = new ExcelUtil<>(clazz);
        return util.importExcel(file.getInputStream());
    }

    /***
     * 该方法为Json 转换方法，将List对象转换为json的一个对象
     * @param list List对象
     * @return  String字符串
     * @throws JsonProcessingException 返回的转换异常，
     */
    private static String convertListObjectsToJsonString(List<?> list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(list);
    }

    /***
     * 将HashMap换行为JSONObject
     * @param jsonKeyToValue
     * @return
     */
    public static JSONObject generateResponseJson(HashMap<String, List<?>> jsonKeyToValue) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        // 将 HashMap 中的键值对添加到 JsonObject 中
        for (Map.Entry<String, List<?>> entry : jsonKeyToValue.entrySet()) {
            jsonObject.put(entry.getKey(), convertListObjectsToJsonString(entry.getValue()));
        }
        return jsonObject;
    }

}