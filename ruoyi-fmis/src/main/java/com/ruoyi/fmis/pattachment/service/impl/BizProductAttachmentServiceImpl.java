package com.ruoyi.fmis.pattachment.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.pattachment.mapper.BizProductAttachmentMapper;
import com.ruoyi.fmis.pattachment.domain.BizProductAttachment;
import com.ruoyi.fmis.pattachment.service.IBizProductAttachmentService;
import com.ruoyi.common.core.text.Convert;

/**
 * 定位器Service业务层处理
 *
 * @author frank
 * @date 2020-06-08
 */
@Service
public class BizProductAttachmentServiceImpl implements IBizProductAttachmentService {
    @Autowired
    private BizProductAttachmentMapper bizProductAttachmentMapper;

    /**
     * 查询定位器
     *
     * @param attachmentId 定位器ID
     * @return 定位器
     */
    @Override
    public BizProductAttachment selectBizProductAttachmentById(Long attachmentId) {
        return bizProductAttachmentMapper.selectBizProductAttachmentById(attachmentId);
    }

    /**
     * 查询定位器列表
     *
     * @param bizProductAttachment 定位器
     * @return 定位器
     */
    @Override
    public List<BizProductAttachment> selectBizProductAttachmentList(BizProductAttachment bizProductAttachment) {
        return bizProductAttachmentMapper.selectBizProductAttachmentList(bizProductAttachment);
    }

    /**
     * 新增定位器
     *
     * @param bizProductAttachment 定位器
     * @return 结果
     */
    @Override
    public int insertBizProductAttachment(BizProductAttachment bizProductAttachment) {
        bizProductAttachment.setCreateTime(DateUtils.getNowDate());
        return bizProductAttachmentMapper.insertBizProductAttachment(bizProductAttachment);
    }

    /**
     * 修改定位器
     *
     * @param bizProductAttachment 定位器
     * @return 结果
     */
    @Override
    public int updateBizProductAttachment(BizProductAttachment bizProductAttachment) {
        bizProductAttachment.setUpdateTime(DateUtils.getNowDate());
        return bizProductAttachmentMapper.updateBizProductAttachment(bizProductAttachment);
    }

    /**
     * 删除定位器对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizProductAttachmentByIds(String ids) {
        return bizProductAttachmentMapper.deleteBizProductAttachmentByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除定位器信息
     *
     * @param attachmentId 定位器ID
     * @return 结果
     */
    @Override
    public int deleteBizProductAttachmentById(Long attachmentId) {
        return bizProductAttachmentMapper.deleteBizProductAttachmentById(attachmentId);
    }
}
