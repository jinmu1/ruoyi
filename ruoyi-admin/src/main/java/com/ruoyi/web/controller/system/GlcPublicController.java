package com.ruoyi.web.controller.system;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.GlcPublic;
import com.ruoyi.system.service.IGlcPublicService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 百碟项目Controller
 * 
 * @author ruoyi
 * @date 2023-03-15
 */
@Controller
@RequestMapping("/system/public")
public class GlcPublicController extends BaseController
{
    private final String prefix = "system/public";

    @Autowired
    private IGlcPublicService glcPublicService;

    @RequiresPermissions("system:public:view")
    @GetMapping()
    public String public1()
    {
        return prefix + "/public";
    }

    /**
     * 查询百碟项目列表
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(GlcPublic glcPublic)
    {
        startPage();
        List<GlcPublic> list = glcPublicService.selectGlcPublicList(glcPublic);
        return getDataTable(list);
    }

    /**
     * 导出百碟项目列表
     */

    @Log(title = "百碟项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GlcPublic glcPublic)
    {
        List<GlcPublic> list = glcPublicService.selectGlcPublicList(glcPublic);
        ExcelUtil<GlcPublic> util = new ExcelUtil<GlcPublic>(GlcPublic.class);
        return util.exportExcel(list, "百碟项目数据");
    }

    /**
     * 新增百碟项目
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存百碟项目
     */

    @Log(title = "百碟项目", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(GlcPublic glcPublic)
    {
        return toAjax(glcPublicService.insertGlcPublic(glcPublic));
    }

    /**
     * 修改百碟项目
     */

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        GlcPublic glcPublic = glcPublicService.selectGlcPublicById(id);
        mmap.put("glcPublic", glcPublic);
        return prefix + "/edit";
    }

    /**
     * 修改保存百碟项目
     */

    @Log(title = "百碟项目", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(GlcPublic glcPublic)
    {
        return toAjax(glcPublicService.updateGlcPublic(glcPublic));
    }

    /**
     * 删除百碟项目
     */

    @Log(title = "百碟项目", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(glcPublicService.deleteGlcPublicByIds(ids));
    }
}
