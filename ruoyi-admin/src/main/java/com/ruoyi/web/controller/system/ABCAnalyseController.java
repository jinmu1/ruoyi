package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Controller
public class ABCAnalyseController  extends BaseController{


/**
 * ABC分析的处理方法
 */

@RequiresPermissions("system:point:list")
@PostMapping("/getABC")
@ResponseBody
public TableDataInfo list()
{
    //读取本地数据
    File jsonFile = new File("path/to/json/file.json");
    try (FileReader fileReader = new FileReader(jsonFile);
         BufferedReader bufferedReader = new BufferedReader(fileReader)) {

        String line;
        StringBuilder jsonContent = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            jsonContent.append(line);
        }
        // 处理读取到的JSON内容
        String jsonString = jsonContent.toString();
        // 在这里对jsonString进行相应的处理

    } catch (IOException e) {
        e.printStackTrace();
    }


    return getDataTable(null);
}


}
