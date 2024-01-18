package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.GlcPublicMapper;
import com.ruoyi.system.domain.GlcPublic;
import com.ruoyi.system.service.IGlcPublicService;
import com.ruoyi.common.core.text.Convert;

/**
 * 百碟项目Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-03-15
 */
@Service
public class GlcPublicServiceImpl implements IGlcPublicService 
{
    @Autowired
    private GlcPublicMapper glcPublicMapper;

    /**
     * 查询百碟项目
     * 
     * @param id 百碟项目主键
     * @return 百碟项目
     */
    @Override
    public GlcPublic selectGlcPublicById(Long id)
    {
        return glcPublicMapper.selectGlcPublicById(id);
    }

    /**
     * 查询百碟项目列表
     * 
     * @param glcPublic 百碟项目
     * @return 百碟项目
     */
    @Override
    public List<GlcPublic> selectGlcPublicList(GlcPublic glcPublic)
    {
        return glcPublicMapper.selectGlcPublicList(glcPublic);
    }

    /**
     * 新增百碟项目
     * 
     * @param glcPublic 百碟项目
     * @return 结果
     */
    @Override
    public int insertGlcPublic(GlcPublic glcPublic)
    {
        return glcPublicMapper.insertGlcPublic(glcPublic);
    }

    /**
     * 修改百碟项目
     * 
     * @param glcPublic 百碟项目
     * @return 结果
     */
    @Override
    public int updateGlcPublic(GlcPublic glcPublic)
    {
        return glcPublicMapper.updateGlcPublic(glcPublic);
    }

    /**
     * 批量删除百碟项目
     * 
     * @param ids 需要删除的百碟项目主键
     * @return 结果
     */
    @Override
    public int deleteGlcPublicByIds(String ids)
    {
        return glcPublicMapper.deleteGlcPublicByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除百碟项目信息
     * 
     * @param id 百碟项目主键
     * @return 结果
     */
    @Override
    public int deleteGlcPublicById(Long id)
    {
        return glcPublicMapper.deleteGlcPublicById(id);
    }
}
