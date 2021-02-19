package com.ruoyi.fmis.finance.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.domain.BizBill;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.fmis.finance.service.IBizBillService;
import com.ruoyi.fmis.util.RoleEnum;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fmis.finance.domain.BizPayPlan;
import com.ruoyi.fmis.finance.service.IBizPayPlanService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @GetMapping("view")
    public String viewPayPlan(@RequestParam String needCompany,@RequestParam String supplierName, ModelMap mmap) {
        mmap.put("needCompany",needCompany);
        mmap.put("supplierName",supplierName);
        mmap.put("payStatus",0);
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
        List<SysRole> roles = ShiroUtils.getSysUser().getRoles();
        Set<String> roleKeySet = roles.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());

        // 老总角色，看所有
        // 财务总管，看老总确认的数据
        // 出纳，看老总和财务总管确认的数据
        if (roleKeySet.contains(RoleEnum.PROCESS_FK_ZJL.getRoleKey())) {
        } else if (roleKeySet.contains(RoleEnum.ZJJL.getRoleKey())) {
            bizPayPlan.setDataStatusList(Stream.of("2", "3").collect(Collectors.toList()));
        } else if (roleKeySet.contains(RoleEnum.CNY.getRoleKey())) {
            bizPayPlan.setDataStatusList(Stream.of("3").collect(Collectors.toList()));
        }
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
        bizPayPlan.setDataStatus("1");
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
                    bizBill.setString1(bizPayPlan.getPayCompany());
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
                    bizBankBill.setPayCompany(bizPayPlan.getPayCompany());
                    bizBankBill.setPayAccount(bizPayPlan.getPayAccount());
                    bizBankBill.setCollectCompany(bizPayPlan.getApplyCollectionCompany());
                    bizBankBillService.insertBizBankBill(bizBankBill);
                }

            }
        }
        return toAjax(payPlan);
    }

    /**
     * 付款计划（确认）
     */
    @RequiresPermissions("finance:payPlan:confirm")
    @Log(title = "付款计划（确认）", businessType = BusinessType.UPDATE)
    @PostMapping("/confirm")
    @ResponseBody
    public AjaxResult updateDateStatus(String ids, String dataStatus) {
        List<BizPayPlan> bizPayPlans = bizPayPlanService.selectBizPayPlanByIds(Convert.toStrArray(ids));
        Set<String> dataStatusSet = bizPayPlans.stream().map(BizPayPlan::getDataStatus).collect(Collectors.toSet());
        if (dataStatusSet.size() > 1) {
            return error("不同数据状态的数据不能同时确认");
        }
        String next = dataStatusSet.iterator().next();
        if (StringUtils.isEmpty(next)) {
            next = "1";
        }
        int nextInt = Integer.parseInt(next);
        if (nextInt >= 3) {
            return error("此状态不能确认");
        }
        dataStatus = String.valueOf(nextInt + 1);
        return toAjax(bizPayPlanService.updateDateStatus(ids, dataStatus));
    }
}
