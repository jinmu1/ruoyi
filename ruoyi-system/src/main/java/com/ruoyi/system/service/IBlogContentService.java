package com.ruoyi.system.service;

import com.ruoyi.system.domain.BlogContent;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-01-16
 */
public interface IBlogContentService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public BlogContent selectBlogContentById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param blogContent 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<BlogContent> selectBlogContentList(BlogContent blogContent);

    /**
     * 新增【请填写功能名称】
     * 
     * @param blogContent 【请填写功能名称】
     * @return 结果
     */
    public int insertBlogContent(BlogContent blogContent);

    /**
     * 修改【请填写功能名称】
     * 
     * @param blogContent 【请填写功能名称】
     * @return 结果
     */
    public int updateBlogContent(BlogContent blogContent);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBlogContentByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteBlogContentById(Long id);

    List<BlogContent> selectList(BlogContent blogContent);
}
