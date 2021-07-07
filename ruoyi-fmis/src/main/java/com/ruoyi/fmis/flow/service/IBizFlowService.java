package com.ruoyi.fmis.flow.service;

import com.ruoyi.fmis.flow.domain.BizFlow;
import java.util.List;

/**
 * 流程记录Service接口
 *
 * @author frank
 * @date 2020-03-18
 */
public interface IBizFlowService {
    /**
     * 查询流程记录
     *
     * @param flowId 流程记录ID
     * @return 流程记录
     */
    public BizFlow selectBizFlowById(Long flowId);

    /**
     * 查询流程记录列表
     *
     * @param bizFlow 流程记录
     * @return 流程记录集合
     */
    public List<BizFlow> selectBizFlowList(BizFlow bizFlow);

    public List<BizFlow> selectBizFlowViewList(BizFlow bizFlow);
    /**
     * 新增流程记录
     *
     * @param bizFlow 流程记录
     * @return 结果
     */
    public int insertBizFlow(BizFlow bizFlow);

    /**
     * 修改流程记录
     *
     * @param bizFlow 流程记录
     * @return 结果
     */
    public int updateBizFlow(BizFlow bizFlow);

    /**
     * 批量删除流程记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizFlowByIds(String ids);

    /**
     * 删除流程记录信息
     *
     * @param flowId 流程记录ID
     * @return 结果
     */
    public int deleteBizFlowById(Long flowId);

    int deleteBizFlowByBizId(Long dataId);
}
