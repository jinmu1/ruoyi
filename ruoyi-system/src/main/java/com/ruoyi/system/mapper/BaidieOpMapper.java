package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BaidieOp;

/**
 * 操作Mapper接口
 * 
 * @author ruoyi
 * @date 2023-03-18
 */
public interface BaidieOpMapper 
{
    /**
     * 查询操作
     * 
     * @param id 操作主键
     * @return 操作
     */
    public BaidieOp selectBaidieOpById(Long id);

    /**
     * 查询操作列表
     * 
     * @param baidieOp 操作
     * @return 操作集合
     */
    public List<BaidieOp> selectBaidieOpList(BaidieOp baidieOp);

    /**
     * 新增操作
     * 
     * @param baidieOp 操作
     * @return 结果
     */
    public int insertBaidieOp(BaidieOp baidieOp);

    /**
     * 修改操作
     * 
     * @param baidieOp 操作
     * @return 结果
     */
    public int updateBaidieOp(BaidieOp baidieOp);

    /**
     * 删除操作
     * 
     * @param id 操作主键
     * @return 结果
     */
    public int deleteBaidieOpById(Long id);

    /**
     * 批量删除操作
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaidieOpByIds(String[] ids);
}
