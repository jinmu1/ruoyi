package com.ruoyi.web.controller.system;

import com.ruoyi.baidie.BaidieProcessor;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.web.controller.utils.BaidieUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/pcb")
public class PCBAnalyseController {
    /**
     * EIQ分析的导入数据
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request)
    {
        try {
            final Map<String, List<?>> resultKeyToDataArrays = BaidieProcessor.importPCBQGroup(file);
            final JSONObject json = BaidieUtils.generateResponseJson(resultKeyToDataArrays);

            return AjaxResult.success(json);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }


}
