package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.GlcPublic;

/**
 * 百碟项目Service接口
 * 
 * @author ruoyi
 * @date 2023-03-15
 */
public interface IGlcPublicService 
{
    /**
     * 查询百碟项目
     * 
     * @param id 百碟项目主键
     * @return 百碟项目
     */
    public GlcPublic selectGlcPublicById(Long id);

    /**
     * 查询百碟项目列表
     * 
     * @param glcPublic 百碟项目
     * @return 百碟项目集合
     */
    public List<GlcPublic> selectGlcPublicList(GlcPublic glcPublic);

    /**
     * 新增百碟项目
     * 
     * @param glcPublic 百碟项目
     * @return 结果
     */
    public int insertGlcPublic(GlcPublic glcPublic);

    /**
     * 修改百碟项目
     * 
     * @param glcPublic 百碟项目
     * @return 结果
     */
    public int updateGlcPublic(GlcPublic glcPublic);

    /**
     * 批量删除百碟项目
     * 
     * @param ids 需要删除的百碟项目主键集合
     * @return 结果
     */
    public int deleteGlcPublicByIds(String ids);

    /**
     * 删除百碟项目信息
     * 
     * @param id 百碟项目主键
     * @return 结果
     */
    public int deleteGlcPublicById(Long id);
}
