package com.ruoyi.system.service;

import com.ruoyi.system.domain.Content;

import java.util.HashMap;
import java.util.List;

/**
 * 内容Service接口
 * 
 * @author ruoyi
 * @date 2021-01-15
 */
public interface IContentService 
{
    /**
     * 查询内容
     * 
     * @param id 内容ID
     * @return 内容
     */
    public Content selectContentById(Long id);

    /**
     * 查询内容列表
     * 
     * @param content 内容
     * @return 内容集合
     */
    public List<Content> selectContentList(Content content);

    /**
     * 新增内容
     * 
     * @param content 内容
     * @return 结果
     */
    public int insertContent(Content content);

    /**
     * 修改内容
     * 
     * @param content 内容
     * @return 结果
     */
    public int updateContent(Content content);

    /**
     * 批量删除内容
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteContentByIds(String ids);

    /**
     * 删除内容信息
     * 
     * @param id 内容ID
     * @return 结果
     */
    public int deleteContentById(Long id);

    int getContentNum(HashMap<String, Object> pm);

    List<Content> getContentList(HashMap<String, Object> pm);
}
