package com.ruoyi.fmis.flow.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.flow.mapper.BizFlowMapper;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.service.IBizFlowService;
import com.ruoyi.common.core.text.Convert;

/**
 * 流程记录Service业务层处理
 *
 * @author frank
 * @date 2020-03-18
 */
@Service
public class BizFlowServiceImpl implements IBizFlowService {
    @Autowired
    private BizFlowMapper bizFlowMapper;

    /**
     * 查询流程记录
     *
     * @param flowId 流程记录ID
     * @return 流程记录
     */
    @Override
    public BizFlow selectBizFlowById(Long flowId) {
        return bizFlowMapper.selectBizFlowById(flowId);
    }

    /**
     * 查询流程记录列表
     *
     * @param bizFlow 流程记录
     * @return 流程记录
     */
    @Override
    public List<BizFlow> selectBizFlowList(BizFlow bizFlow) {
        return bizFlowMapper.selectBizFlowList(bizFlow);
    }

    /**
     * 新增流程记录
     *
     * @param bizFlow 流程记录
     * @return 结果
     */
    @Override
    public int insertBizFlow(BizFlow bizFlow) {
        bizFlow.setCreateTime(DateUtils.getNowDate());
        return bizFlowMapper.insertBizFlow(bizFlow);
    }

    /**
     * 修改流程记录
     *
     * @param bizFlow 流程记录
     * @return 结果
     */
    @Override
    public int updateBizFlow(BizFlow bizFlow) {
        bizFlow.setUpdateTime(DateUtils.getNowDate());
        return bizFlowMapper.updateBizFlow(bizFlow);
    }

    /**
     * 删除流程记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizFlowByIds(String ids) {
        return bizFlowMapper.deleteBizFlowByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程记录信息
     *
     * @param flowId 流程记录ID
     * @return 结果
     */
    @Override
    public int deleteBizFlowById(Long flowId) {
        return bizFlowMapper.deleteBizFlowById(flowId);
    }
}
