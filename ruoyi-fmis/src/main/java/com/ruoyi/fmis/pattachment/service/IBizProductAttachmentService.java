package com.ruoyi.fmis.pattachment.service;

import com.ruoyi.fmis.pattachment.domain.BizProductAttachment;
import java.util.List;

/**
 * 定位器Service接口
 *
 * @author frank
 * @date 2020-06-08
 */
public interface IBizProductAttachmentService {
    /**
     * 查询定位器
     *
     * @param attachmentId 定位器ID
     * @return 定位器
     */
    public BizProductAttachment selectBizProductAttachmentById(Long attachmentId);

    /**
     * 查询定位器列表
     *
     * @param bizProductAttachment 定位器
     * @return 定位器集合
     */
    public List<BizProductAttachment> selectBizProductAttachmentList(BizProductAttachment bizProductAttachment);

    /**
     * 新增定位器
     *
     * @param bizProductAttachment 定位器
     * @return 结果
     */
    public int insertBizProductAttachment(BizProductAttachment bizProductAttachment);

    /**
     * 修改定位器
     *
     * @param bizProductAttachment 定位器
     * @return 结果
     */
    public int updateBizProductAttachment(BizProductAttachment bizProductAttachment);

    /**
     * 批量删除定位器
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProductAttachmentByIds(String ids);

    /**
     * 删除定位器信息
     *
     * @param attachmentId 定位器ID
     * @return 结果
     */
    public int deleteBizProductAttachmentById(Long attachmentId);
}
