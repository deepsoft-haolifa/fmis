package com.ruoyi.fmis.flow.mapper;

import com.ruoyi.fmis.flow.domain.BizFlow;
import java.util.List;

/**
 * 流程记录Mapper接口
 *
 * @author frank
 * @date 2020-03-18
 */
public interface BizFlowMapper {
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
     * 删除流程记录
     *
     * @param flowId 流程记录ID
     * @return 结果
     */
    public int deleteBizFlowById(Long flowId);

    /**
     * 批量删除流程记录
     *
     * @param flowIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizFlowByIds(String[] flowIds);
}
