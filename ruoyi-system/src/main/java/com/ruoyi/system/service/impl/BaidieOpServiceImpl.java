package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BaidieOpMapper;
import com.ruoyi.system.domain.BaidieOp;
import com.ruoyi.system.service.IBaidieOpService;
import com.ruoyi.common.core.text.Convert;

/**
 * 操作Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-03-18
 */
@Service
public class BaidieOpServiceImpl implements IBaidieOpService 
{
    @Autowired
    private BaidieOpMapper baidieOpMapper;

    /**
     * 查询操作
     * 
     * @param id 操作主键
     * @return 操作
     */
    @Override
    public BaidieOp selectBaidieOpById(Long id)
    {
        return baidieOpMapper.selectBaidieOpById(id);
    }

    /**
     * 查询操作列表
     * 
     * @param baidieOp 操作
     * @return 操作
     */
    @Override
    public List<BaidieOp> selectBaidieOpList(BaidieOp baidieOp)
    {
        return baidieOpMapper.selectBaidieOpList(baidieOp);
    }

    /**
     * 新增操作
     * 
     * @param baidieOp 操作
     * @return 结果
     */
    @Override
    public int insertBaidieOp(BaidieOp baidieOp)
    {
        return baidieOpMapper.insertBaidieOp(baidieOp);
    }

    /**
     * 修改操作
     * 
     * @param baidieOp 操作
     * @return 结果
     */
    @Override
    public int updateBaidieOp(BaidieOp baidieOp)
    {
        return baidieOpMapper.updateBaidieOp(baidieOp);
    }

    /**
     * 批量删除操作
     * 
     * @param ids 需要删除的操作主键
     * @return 结果
     */
    @Override
    public int deleteBaidieOpByIds(String ids)
    {
        return baidieOpMapper.deleteBaidieOpByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除操作信息
     * 
     * @param id 操作主键
     * @return 结果
     */
    @Override
    public int deleteBaidieOpById(Long id)
    {
        return baidieOpMapper.deleteBaidieOpById(id);
    }
}
