package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Netsend;
import com.ruoyi.system.service.INetsendService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2023-01-10
 */
@Controller
@RequestMapping("/system/netsend")
public class NetsendController extends BaseController
{
    private String prefix = "system/netsend";

    @Autowired
    private INetsendService netsendService;


    @GetMapping()
    public String netsend()
    {
        return prefix + "/netsend";
    }

    /**
     * 查询【请填写功能名称】列表
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Netsend netsend)
    {
        startPage();
        List<Netsend> list = netsendService.selectNetsendList(netsend);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Netsend netsend)
    {
        List<Netsend> list = netsendService.selectNetsendList(netsend);
        ExcelUtil<Netsend> util = new ExcelUtil<Netsend>(Netsend.class);
        return util.exportExcel(list, "【请填写功能名称】数据");
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */

    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Netsend netsend)
    {
        return toAjax(netsendService.insertNetsend(netsend));
    }

    /**
     * 修改【请填写功能名称】
     */

    @GetMapping("/edit")
    public String edit( )
    {

        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @RequiresPermissions("system:netsend:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Netsend netsend)
    {
        return toAjax(netsendService.updateNetsend(netsend));
    }

    /**
     * 删除【请填写功能名称】
     */
    @RequiresPermissions("system:netsend:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(netsendService.deleteNetsendBy订单编号s(ids));
    }

    @Log(title = "出库导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request) throws Exception
    {
        ExcelUtil<Netsend> util = new ExcelUtil<>(Netsend.class);
        Long userId = 345L;

        List<Netsend> userList = util.importExcel(file.getInputStream());
        String message = netsendService.importExcel(userList, updateSupport, userId);
        return AjaxResult.success(message);
    }
    @RequiresPermissions("system:glcDelivery:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<Netsend> util = new ExcelUtil<Netsend>(Netsend.class);
        return util.importTemplateExcel("原始出库数据");
    }
}
