package com.ruoyi.fmis.finance.controller;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.framework.util.ShiroUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
import com.ruoyi.fmis.finance.domain.BizPayPlan;
import com.ruoyi.fmis.finance.service.IBizPayPlanService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 付款计划（基于付款申请记录）Controller
 *
 * @author hedong
 * @date 2020-10-22
 */
@Controller
@RequestMapping("/finance/payPlan")
public class BizPayPlanController extends BaseController {
    private String prefix = "finance/payPlan";

    @Autowired
    private IBizPayPlanService bizPayPlanService;
    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @RequiresPermissions("finance:payPlan:view")
    @GetMapping()
    public String payPlan() {
        return prefix + "/payPlan";
    }

    /**
     * 查询付款计划（基于付款申请记录）列表
     */
    @RequiresPermissions("finance:payPlan:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizPayPlan bizPayPlan) {
        startPage();
        List<BizPayPlan> list = bizPayPlanService.selectBizPayPlanList(bizPayPlan);
        return getDataTable(list);
    }

    /**
     * 导出付款计划（基于付款申请记录）列表
     */
    @RequiresPermissions("finance:payPlan:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizPayPlan bizPayPlan) {
        List<BizPayPlan> list = bizPayPlanService.selectBizPayPlanList(bizPayPlan);
        ExcelUtil<BizPayPlan> util = new ExcelUtil<BizPayPlan>(BizPayPlan.class);
        return util.exportExcel(list, "payPlan");
    }

    /**
     * 新增付款计划（基于付款申请记录）
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存付款计划（基于付款申请记录）
     */
    @RequiresPermissions("finance:payPlan:add")
    @Log(title = "付款计划（基于付款申请记录）", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizPayPlan bizPayPlan) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(bizPayPlan.getPayDataId());
        bizPayPlan = new BizPayPlan();
        bizPayPlan.setPayDataId(bizProcessData.getDataId());
        bizPayPlan.setApplyPayCompany(bizProcessData.getString6());
        bizPayPlan.setApplyCollectionCompany(bizProcessData.getString1());
        bizPayPlan.setApplyRemark(bizProcessData.getRemark());
        bizPayPlan.setApplyAmount(bizProcessData.getPrice2());
        bizPayPlan.setApplyDate(bizProcessData.getDatetime1());
        bizPayPlan.setContractNo(bizProcessData.getString5());
        bizPayPlan.setApplyNo("PP" + DateUtils.dateTimeNow() + RandomStringUtils.randomNumeric(3));
        bizPayPlan.setCreateTime(DateUtils.getNowDate());
        bizPayPlan.setCreateBy(ShiroUtils.getUserId().toString());
        return toAjax(bizPayPlanService.insertBizPayPlan(bizPayPlan));
    }

    /**
     * 修改付款计划（基于付款申请记录）
     */
    @GetMapping("/edit/{payPlanId}")
    public String edit(@PathVariable("payPlanId") Long payPlanId, ModelMap mmap) {
        BizPayPlan bizPayPlan = bizPayPlanService.selectBizPayPlanById(payPlanId);
        mmap.put("bizPayPlan", bizPayPlan);
        return prefix + "/edit";
    }

    /**
     * 修改保存付款计划（基于付款申请记录）
     */
    @RequiresPermissions("finance:payPlan:edit")
    @Log(title = "付款计划（基于付款申请记录）", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizPayPlan bizPayPlan) {
        return toAjax(bizPayPlanService.updateBizPayPlan(bizPayPlan));
    }

//    /**
//     * 删除付款计划（基于付款申请记录）
//     */
//    @RequiresPermissions("finance:payPlan:remove")
//    @Log(title = "付款计划（基于付款申请记录）", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids) {
//        return toAjax(bizPayPlanService.deleteBizPayPlanByIds(ids));
//    }
}
