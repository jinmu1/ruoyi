package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Netsend;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2023-01-10
 */
public interface NetsendMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param 订单编号 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Netsend selectNetsendBy订单编号(String 订单编号);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param netsend 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Netsend> selectNetsendList(Netsend netsend);

    /**
     * 新增【请填写功能名称】
     * 
     * @param netsend 【请填写功能名称】
     * @return 结果
     */
    public int insertNetsend(Netsend netsend);

    /**
     * 修改【请填写功能名称】
     * 
     * @param netsend 【请填写功能名称】
     * @return 结果
     */
    public int updateNetsend(Netsend netsend);

    /**
     * 删除【请填写功能名称】
     * 
     * @param 订单编号 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteNetsendBy订单编号(String 订单编号);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param 订单编号s 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNetsendBy订单编号s(String[] 订单编号s);

    void insertNetsendList(List<Netsend> list);
}
