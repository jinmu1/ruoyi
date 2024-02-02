package com.ruoyi.web.controller.utils;

import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
}