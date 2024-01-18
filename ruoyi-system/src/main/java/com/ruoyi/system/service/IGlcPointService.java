package com.ruoyi.system.service;





import com.ruoyi.system.form.GlcPoint;

import java.util.List;


/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-06-24
 */
public interface IGlcPointService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param provinces 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public GlcPoint selectGlcPointById(String provinces);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param glcPoint 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<GlcPoint> selectGlcPointList(GlcPoint glcPoint);

    /**
     * 新增【请填写功能名称】
     * 
     * @param glcPoint 【请填写功能名称】
     * @return 结果
     */
    public int insertGlcPoint(GlcPoint glcPoint);

    /**
     * 修改【请填写功能名称】
     * 
     * @param glcPoint 【请填写功能名称】
     * @return 结果
     */
    public int updateGlcPoint(GlcPoint glcPoint);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlcPointByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param provinces 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteGlcPointById(String provinces);
}
