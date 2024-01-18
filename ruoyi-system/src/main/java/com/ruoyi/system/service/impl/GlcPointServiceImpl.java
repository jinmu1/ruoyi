package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.form.GlcPoint;
import com.ruoyi.system.mapper.GlcPointMapper;
import com.ruoyi.system.service.IGlcPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-06-24
 */
@Service
public class GlcPointServiceImpl implements IGlcPointService
{
    @Autowired
    private GlcPointMapper glcPointMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param provinces 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public GlcPoint selectGlcPointById(String provinces)
    {
        return glcPointMapper.selectGlcPointById(provinces);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param glcPoint 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<GlcPoint> selectGlcPointList(GlcPoint glcPoint)
    {
        return glcPointMapper.selectGlcPointList(glcPoint);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param glcPoint 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertGlcPoint(GlcPoint glcPoint)
    {
        return glcPointMapper.insertGlcPoint(glcPoint);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param glcPoint 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateGlcPoint(GlcPoint glcPoint)
    {
        return glcPointMapper.updateGlcPoint(glcPoint);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGlcPointByIds(String ids)
    {
        return glcPointMapper.deleteGlcPointByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param provinces 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteGlcPointById(String provinces)
    {
        return glcPointMapper.deleteGlcPointById(provinces);
    }
}
