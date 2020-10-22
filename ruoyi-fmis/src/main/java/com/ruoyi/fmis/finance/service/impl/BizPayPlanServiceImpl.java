package com.ruoyi.fmis.finance.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.finance.mapper.BizPayPlanMapper;
import com.ruoyi.fmis.finance.domain.BizPayPlan;
import com.ruoyi.fmis.finance.service.IBizPayPlanService;
import com.ruoyi.common.core.text.Convert;

/**
 * 付款计划（基于付款申请记录）Service业务层处理
 *
 * @author hedong
 * @date 2020-10-22
 */
@Service
public class BizPayPlanServiceImpl implements IBizPayPlanService {
    @Autowired
    private BizPayPlanMapper bizPayPlanMapper;

    /**
     * 查询付款计划（基于付款申请记录）
     *
     * @param payPlanId 付款计划（基于付款申请记录）ID
     * @return 付款计划（基于付款申请记录）
     */
    @Override
    public BizPayPlan selectBizPayPlanById(Long payPlanId) {
        return bizPayPlanMapper.selectBizPayPlanById(payPlanId);
    }

    /**
     * 查询付款计划（基于付款申请记录）列表
     *
     * @param bizPayPlan 付款计划（基于付款申请记录）
     * @return 付款计划（基于付款申请记录）
     */
    @Override
    public List<BizPayPlan> selectBizPayPlanList(BizPayPlan bizPayPlan) {
        return bizPayPlanMapper.selectBizPayPlanList(bizPayPlan);
    }

    /**
     * 新增付款计划（基于付款申请记录）
     *
     * @param bizPayPlan 付款计划（基于付款申请记录）
     * @return 结果
     */
    @Override
    public int insertBizPayPlan(BizPayPlan bizPayPlan) {
        bizPayPlan.setCreateTime(DateUtils.getNowDate());
        return bizPayPlanMapper.insertBizPayPlan(bizPayPlan);
    }

    /**
     * 修改付款计划（基于付款申请记录）
     *
     * @param bizPayPlan 付款计划（基于付款申请记录）
     * @return 结果
     */
    @Override
    public int updateBizPayPlan(BizPayPlan bizPayPlan) {
        bizPayPlan.setUpdateTime(DateUtils.getNowDate());
        return bizPayPlanMapper.updateBizPayPlan(bizPayPlan);
    }

    /**
     * 删除付款计划（基于付款申请记录）对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizPayPlanByIds(String ids) {
        return bizPayPlanMapper.deleteBizPayPlanByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除付款计划（基于付款申请记录）信息
     *
     * @param payPlanId 付款计划（基于付款申请记录）ID
     * @return 结果
     */
    @Override
    public int deleteBizPayPlanById(Long payPlanId) {
        return bizPayPlanMapper.deleteBizPayPlanById(payPlanId);
    }
}
