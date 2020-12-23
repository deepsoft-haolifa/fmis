package com.ruoyi.fmis.budget.controller;

import java.util.List;

import com.ruoyi.fmis.subjects.domain.BizSubjects;
import com.ruoyi.fmis.subjects.service.IBizSubjectsService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fmis.budget.domain.BizCostBudget;
import com.ruoyi.fmis.budget.service.IBizCostBudgetService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 费用预算Controller
 *
 * @author frank
 * @date 2020-06-28
 */
@Controller
@RequestMapping("/fmis/budget")
public class BizCostBudgetController extends BaseController {
    private String prefix = "fmis/budget";

    @Autowired
    private IBizCostBudgetService bizCostBudgetService;

    @Autowired
    private IBizSubjectsService bizSubjectsService;

    @RequiresPermissions("fmis:budget:view")
    @GetMapping()
    public String budget() {
        return prefix + "/budget";
    }

    /**
     * 查询费用预算列表
     */
    @RequiresPermissions("fmis:budget:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizCostBudget bizCostBudget) {
        startPage();
        List<BizCostBudget> list = bizCostBudgetService.selectBizCostBudgetList(bizCostBudget);
        return getDataTable(list);
    }

    /**
     * 导出费用预算列表
     */
    @RequiresPermissions("fmis:budget:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizCostBudget bizCostBudget) {
        List<BizCostBudget> list = bizCostBudgetService.selectBizCostBudgetList(bizCostBudget);
        ExcelUtil<BizCostBudget> util = new ExcelUtil<BizCostBudget>(BizCostBudget.class);
        return util.exportExcel(list, "budget");
    }

    /**
     * 新增费用预算
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("subjects",bizSubjectsService.selectBizSubjectsList(new BizSubjects()));
        return prefix + "/add";
    }

    /**
     * 新增保存费用预算
     */
    @RequiresPermissions("fmis:budget:add")
    @Log(title = "费用预算", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizCostBudget bizCostBudget) {
        return toAjax(bizCostBudgetService.insertBizCostBudget(bizCostBudget));
    }

    /**
     * 修改费用预算
     */
    @GetMapping("/edit/{budgetId}")
    public String edit(@PathVariable("budgetId") Long budgetId, ModelMap mmap) {
        BizCostBudget bizCostBudget = bizCostBudgetService.selectBizCostBudgetById(budgetId);
        mmap.put("bizCostBudget", bizCostBudget);

        List<BizSubjects> suppliersList = bizSubjectsService.selectBizSubjectsList(new BizSubjects());
        for (BizSubjects subjects : suppliersList) {
            Long supplierId = subjects.getSubjectsId();
            if (Long.compare(supplierId,bizCostBudget.getSubjectsId()) == 0) {
                subjects.setFlag(true);
            }
        }
        mmap.put("subjects",suppliersList);

        return prefix + "/edit";
    }

    /**
     * 修改保存费用预算
     */
    @RequiresPermissions("fmis:budget:edit")
    @Log(title = "费用预算", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizCostBudget bizCostBudget) {
        return toAjax(bizCostBudgetService.updateBizCostBudget(bizCostBudget));
    }

    /**
     * 删除费用预算
     */
    @RequiresPermissions("fmis:budget:remove")
    @Log(title = "费用预算", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizCostBudgetService.deleteBizCostBudgetByIds(ids));
    }
}
