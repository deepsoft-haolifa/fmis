package com.ruoyi.fmis.budget.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.subjects.domain.BizSubjects;
import com.ruoyi.fmis.subjects.service.IBizSubjectsService;
import com.ruoyi.fmis.subjects.service.impl.BizSubjectsServiceImpl;
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
    @Autowired
    private IBizSubjectsService bizSubjectsService;

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
    @DataScope(deptAlias = "b", userAlias = "u")
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

    @Override
    public List<BizSubjects> selectByDeptId(Long deptId) {
        BizCostBudget bizCostBudget = new BizCostBudget();
        if (deptId != null && deptId > 0L) {
            bizCostBudget.setDeptId(deptId);
        }
        List<BizCostBudget> budgets = bizCostBudgetMapper.selectBizCostBudgetList(bizCostBudget);
        Set<Long> subjectsIdSet = budgets.stream().map(BizCostBudget::getSubjectsId).collect(Collectors.toSet());
        List<BizSubjects> bizSubjects = bizSubjectsService.selectBizSubjectsListNoParent(new BizSubjects());
        return bizSubjects.stream().filter(e -> subjectsIdSet.contains(e.getSubjectsId())).collect(Collectors.toList());
    }


    /**
     * 查询费用预算列表没有权限
     *
     * @param bizCostBudget 费用预算
     * @return 费用预算
     */
    @Override
    public List<BizCostBudget> selectBizCostBudgetList2(BizCostBudget bizCostBudget) {
        return bizCostBudgetMapper.selectBizCostBudgetList2(bizCostBudget);
    }
}
