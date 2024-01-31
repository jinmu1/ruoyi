package com.ruoyi.web.controller.utils;

import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/***
 * 对导入的Excel表转换为List对象
 */
public   class ImportExcelUtils {

    /***
     * 将导入的Excel文件流转换为List对象
     * @param inputStream
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> importExcel(InputStream inputStream, Class<T> clazz) throws Exception {
        ExcelUtil<T> util = new ExcelUtil<>(clazz);
        return util.importExcel(inputStream);
    }


}
