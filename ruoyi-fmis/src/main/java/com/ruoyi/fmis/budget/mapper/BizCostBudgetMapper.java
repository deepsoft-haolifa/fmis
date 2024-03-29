package com.ruoyi.fmis.budget.mapper;

import com.ruoyi.fmis.budget.domain.BizCostBudget;
import java.util.List;

/**
 * 费用预算Mapper接口
 *
 * @author frank
 * @date 2020-06-28
 */
public interface BizCostBudgetMapper {
    /**
     * 查询费用预算
     *
     * @param budgetId 费用预算ID
     * @return 费用预算
     */
    public BizCostBudget selectBizCostBudgetById(Long budgetId);

    /**
     * 查询费用预算列表
     *
     * @param bizCostBudget 费用预算
     * @return 费用预算集合
     */
    public List<BizCostBudget> selectBizCostBudgetList(BizCostBudget bizCostBudget);

    /**
     * 新增费用预算
     *
     * @param bizCostBudget 费用预算
     * @return 结果
     */
    public int insertBizCostBudget(BizCostBudget bizCostBudget);

    /**
     * 修改费用预算
     *
     * @param bizCostBudget 费用预算
     * @return 结果
     */
    public int updateBizCostBudget(BizCostBudget bizCostBudget);

    /**
     * 删除费用预算
     *
     * @param budgetId 费用预算ID
     * @return 结果
     */
    public int deleteBizCostBudgetById(Long budgetId);

    /**
     * 批量删除费用预算
     *
     * @param budgetIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizCostBudgetByIds(String[] budgetIds);

    /**
     * 查询费用预算列表没权限
     *
     * @param bizCostBudget 费用预算
     * @return 费用预算集合
     */
    public List<BizCostBudget> selectBizCostBudgetList2(BizCostBudget bizCostBudget);
}
