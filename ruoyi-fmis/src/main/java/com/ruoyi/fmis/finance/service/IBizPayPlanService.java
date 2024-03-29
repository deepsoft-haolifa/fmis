package com.ruoyi.fmis.finance.service;

import com.ruoyi.fmis.finance.domain.BizPayPlan;
import java.util.List;

/**
 * 付款计划（基于付款申请记录）Service接口
 *
 * @author hedong
 * @date 2020-10-22
 */
public interface IBizPayPlanService {
    /**
     * 查询付款计划（基于付款申请记录）
     *
     * @param payPlanId 付款计划（基于付款申请记录）ID
     * @return 付款计划（基于付款申请记录）
     */
    public BizPayPlan selectBizPayPlanById(Long payPlanId);

    public List<BizPayPlan> selectBizPayPlanByIds(String[] ids);

    /**
     * 查询付款计划（基于付款申请记录）列表
     *
     * @param bizPayPlan 付款计划（基于付款申请记录）
     * @return 付款计划（基于付款申请记录）集合
     */
    public List<BizPayPlan> selectBizPayPlanList(BizPayPlan bizPayPlan);

    /**
     * 新增付款计划（基于付款申请记录）
     *
     * @param bizPayPlan 付款计划（基于付款申请记录）
     * @return 结果
     */
    public int insertBizPayPlan(BizPayPlan bizPayPlan);

    /**
     * 修改付款计划（基于付款申请记录）
     *
     * @param bizPayPlan 付款计划（基于付款申请记录）
     * @return 结果
     */
    public int updateBizPayPlan(BizPayPlan bizPayPlan);

    /**
     * 批量删除付款计划（基于付款申请记录）
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizPayPlanByIds(String ids);

    /**
     * 删除付款计划（基于付款申请记录）信息
     *
     * @param payPlanId 付款计划（基于付款申请记录）ID
     * @return 结果
     */
    public int deleteBizPayPlanById(Long payPlanId);

    int updateDateStatus(String ids,String dataStatus);
}
