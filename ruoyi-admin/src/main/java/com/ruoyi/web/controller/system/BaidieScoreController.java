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
import com.ruoyi.system.domain.BaidieScore;
import com.ruoyi.system.service.IBaidieScoreService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2023-03-15
 */
@Controller
@RequestMapping("/system/score")
public class BaidieScoreController extends BaseController
{
    private String prefix = "system/score";

    @Autowired
    private IBaidieScoreService baidieScoreService;


    @GetMapping()
    public String score()
    {
        return prefix + "/score";
    }

    /**
     * 查询【请填写功能名称】列表
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BaidieScore baidieScore, HttpServletRequest request)
    {
        startPage();
        String issueID = request.getParameter("IssueId");
        if(issueID!=null){
            System.out.println("userID获取成功！");
        }
        baidieScore.setUserId(issueID);
        List<BaidieScore> list = baidieScoreService.selectBaidieScoreList(baidieScore);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
//    @RequiresPermissions("system:score:export")
//    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BaidieScore baidieScore, HttpServletRequest request)
    {
        String issueID = request.getParameter("IssueId");
        if(issueID!=null){
            System.out.println("userID获取成功！");
        }
        String userCode = request.getParameter("userCode");
        if(userCode!=null){
            System.out.println("userCode获取成功！");
        }
        if (userCode!="GLC"){
            return null;
        }else {
            List<BaidieScore> list = baidieScoreService.selectBaidieScoreList(baidieScore);
            ExcelUtil<BaidieScore> util = new ExcelUtil<BaidieScore>(BaidieScore.class);
            return util.exportExcel(list, "【请填写功能名称】数据");
        }

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
    @RequiresPermissions("system:score:add")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BaidieScore baidieScore)
    {
        return toAjax(baidieScoreService.insertBaidieScore(baidieScore));
    }

    /**
     * 修改【请填写功能名称】
     */
    @RequiresPermissions("system:score:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        BaidieScore baidieScore = baidieScoreService.selectBaidieScoreById(id);
        mmap.put("baidieScore", baidieScore);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @RequiresPermissions("system:score:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BaidieScore baidieScore)
    {
        return toAjax(baidieScoreService.updateBaidieScore(baidieScore));
    }

    /**
     * 删除【请填写功能名称】
     */
    @RequiresPermissions("system:score:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(baidieScoreService.deleteBaidieScoreByIds(ids));
    }
}
