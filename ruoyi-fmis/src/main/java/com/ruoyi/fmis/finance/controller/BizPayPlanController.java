package com.ruoyi.fmis.finance.controller;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.domain.BizBill;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.fmis.finance.service.IBizBillService;
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
    @Autowired
    private IBizBillService bizBillService;
    @Autowired
    private IBizBankBillService bizBankBillService;

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
        if (bizPayPlan.getPayDate() == null) {
            bizPayPlan.setPayDate(new Date());
        }
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
        int payPlan = bizPayPlanService.updateBizPayPlan(bizPayPlan);
        // 已付款状态之后；
        if (payPlan > 0 && "1".equals(bizPayPlan.getStatus())) {
            String bookingType = bizPayPlan.getBookingType();
            if ("1".equals(bookingType)) {
                // 添加现金日记账
                if (!bizBillService.existsByCertificateNumber(bizPayPlan.getApplyNo())) {
                    BizBill bizBill = new BizBill();
                    bizBill.setType("1");
                    bizBill.setCertificateNumber(bizPayPlan.getApplyNo());
                    bizBill.setD(bizPayPlan.getPayDate());
                    bizBill.setPaymentType(bizPayPlan.getPaymentType());
                    bizBill.setPayment(bizPayPlan.getApplyAmount());
                    bizBill.setRemark(bizPayPlan.getApplyRemark());
                    bizBill.setString1(bizPayPlan.getApplyPayCompany());
                    bizBill.setString2(bizPayPlan.getApplyCollectionCompany());
                    bizBillService.insertBizBill(bizBill);
                }
            } else if ("2".equals(bookingType)) {
                // 添加银行日记账
                if (!bizBankBillService.existsByCertificateNumber(bizPayPlan.getApplyNo())) {
                    BizBankBill bizBankBill = new BizBankBill();
                    bizBankBill.setType("2");
                    bizBankBill.setCertificateNumber(bizPayPlan.getApplyNo());
                    bizBankBill.setOperateDate(bizPayPlan.getPayDate());
                    bizBankBill.setPayWay(bizPayPlan.getPayWay());
                    bizBankBill.setPaymentType(bizPayPlan.getPaymentType());
                    bizBankBill.setPayment(bizPayPlan.getApplyAmount());
                    bizBankBill.setRemark(bizPayPlan.getApplyRemark());
                    bizBankBill.setPayCompany(bizPayPlan.getApplyPayCompany());
                    bizBankBill.setCollectCompany(bizPayPlan.getApplyCollectionCompany());
                    bizBankBillService.insertBizBankBill(bizBankBill);
                }

            }
        }
        return toAjax(payPlan);
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
