package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BaidieScore;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2023-03-15
 */
public interface IBaidieScoreService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public BaidieScore selectBaidieScoreById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param baidieScore 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<BaidieScore> selectBaidieScoreList(BaidieScore baidieScore);

    /**
     * 新增【请填写功能名称】
     * 
     * @param baidieScore 【请填写功能名称】
     * @return 结果
     */
    public int insertBaidieScore(BaidieScore baidieScore);

    /**
     * 修改【请填写功能名称】
     * 
     * @param baidieScore 【请填写功能名称】
     * @return 结果
     */
    public int updateBaidieScore(BaidieScore baidieScore);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteBaidieScoreByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteBaidieScoreById(Long id);
}
