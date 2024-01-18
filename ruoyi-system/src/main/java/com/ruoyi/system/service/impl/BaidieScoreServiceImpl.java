package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BaidieScoreMapper;
import com.ruoyi.system.domain.BaidieScore;
import com.ruoyi.system.service.IBaidieScoreService;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-03-15
 */
@Service
public class BaidieScoreServiceImpl implements IBaidieScoreService 
{
    @Autowired
    private BaidieScoreMapper baidieScoreMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public BaidieScore selectBaidieScoreById(Long id)
    {
        return baidieScoreMapper.selectBaidieScoreById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param baidieScore 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<BaidieScore> selectBaidieScoreList(BaidieScore baidieScore)
    {
        return baidieScoreMapper.selectBaidieScoreList(baidieScore);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param baidieScore 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertBaidieScore(BaidieScore baidieScore)
    {
        return baidieScoreMapper.insertBaidieScore(baidieScore);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param baidieScore 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateBaidieScore(BaidieScore baidieScore)
    {
        return baidieScoreMapper.updateBaidieScore(baidieScore);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteBaidieScoreByIds(String ids)
    {
        return baidieScoreMapper.deleteBaidieScoreByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteBaidieScoreById(Long id)
    {
        return baidieScoreMapper.deleteBaidieScoreById(id);
    }
}
