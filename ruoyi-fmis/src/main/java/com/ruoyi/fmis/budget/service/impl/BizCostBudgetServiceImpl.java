package com.ruoyi.fmis.budget.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.budget.mapper.BizCostBudgetMapper;
import com.ruoyi.fmis.budget.domain.BizCostBudget;
import com.ruoyi.fmis.budget.service.IBizCostBudgetService;
import com.ruoyi.common.core.text.Convert;

/**
 * 费用预算Service业务层处理
 *
 * @author frank
 * @date 2020-06-28
 */
@Service
public class BizCostBudgetServiceImpl implements IBizCostBudgetService {
    @Autowired
    private BizCostBudgetMapper bizCostBudgetMapper;

    /**
     * 查询费用预算
     *
     * @param budgetId 费用预算ID
     * @return 费用预算
     */
    @Override
    public BizCostBudget selectBizCostBudgetById(Long budgetId) {
        return bizCostBudgetMapper.selectBizCostBudgetById(budgetId);
    }

    /**
     * 查询费用预算列表
     *
     * @param bizCostBudget 费用预算
     * @return 费用预算
     */
    @Override
    public List<BizCostBudget> selectBizCostBudgetList(BizCostBudget bizCostBudget) {
        return bizCostBudgetMapper.selectBizCostBudgetList(bizCostBudget);
    }

    /**
     * 新增费用预算
     *
     * @param bizCostBudget 费用预算
     * @return 结果
     */
    @Override
    public int insertBizCostBudget(BizCostBudget bizCostBudget) {
        bizCostBudget.setCreateTime(DateUtils.getNowDate());
        return bizCostBudgetMapper.insertBizCostBudget(bizCostBudget);
    }

    /**
     * 修改费用预算
     *
     * @param bizCostBudget 费用预算
     * @return 结果
     */
    @Override
    public int updateBizCostBudget(BizCostBudget bizCostBudget) {
        bizCostBudget.setUpdateTime(DateUtils.getNowDate());
        return bizCostBudgetMapper.updateBizCostBudget(bizCostBudget);
    }

    /**
     * 删除费用预算对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizCostBudgetByIds(String ids) {
        return bizCostBudgetMapper.deleteBizCostBudgetByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除费用预算信息
     *
     * @param budgetId 费用预算ID
     * @return 结果
     */
    @Override
    public int deleteBizCostBudgetById(Long budgetId) {
        return bizCostBudgetMapper.deleteBizCostBudgetById(budgetId);
    }
}
