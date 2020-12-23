package com.ruoyi.fmis.finance.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 银行日记账Controller
 *
 * @author frank
 * @date 2020-06-25
 */
@Controller
@RequestMapping("/finance/bankBill")
public class BizBankBillController extends BaseController {
    private String prefix = "finance/bankBill";

    @Autowired
    private IBizBankBillService bizBankBillService;

    @Autowired
    private IBizCustomerService customerService;

    @RequiresPermissions("finance:bankBill:view")
    @GetMapping()
    public String bill() {
        return prefix + "/bankBill";
    }

    /**
     * 查询银行日记账列表
     */
    @RequiresPermissions("finance:bankBill:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizBankBill bizBankBill) {
        startPage();
        List<BizBankBill> list = bizBankBillService.selectBizBankBillList(bizBankBill);
        return getDataTable(list);
    }

    /**
     * 导出银行日记账列表
     */
    @RequiresPermissions("finance:bankBill:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizBankBill bizBankBill) {
        List<BizBankBill> list = bizBankBillService.selectBizBankBillList(bizBankBill);
        ExcelUtil<BizBankBill> util = new ExcelUtil<BizBankBill>(BizBankBill.class);
        return util.exportExcel(list, "bill");
    }

    /**
     * 新增银行日记账（收款）
     */
    @GetMapping("/addCollect")
    public String addCollect(ModelMap mmap) {
        // 收款单的付款单位--客户管理
        List<BizCustomer> customerList = customerService.selectCustomerAll();
        mmap.put("customerList", customerList);
        return prefix + "/addCollect";
    }

    /**
     * 新增保存银行日记账（收款）
     */
    @RequiresPermissions("finance:bankBill:addCollect")
    @Log(title = "银行日记账（收款）", businessType = BusinessType.INSERT)
    @PostMapping("/addCollect")
    @ResponseBody
    public AjaxResult addCollectSave(BizBankBill bizBankBill) {
        return toAjax(bizBankBillService.insertBizBankBill(bizBankBill));
    }

    /**
     * 修改银行日记账（收款）
     */
    @GetMapping("/editCollect/{billId}")
    public String editCollect(@PathVariable("billId") Long billId, ModelMap mmap) {
        BizBankBill bizBankBill = bizBankBillService.selectBizBankBillById(billId);
        mmap.put("bizBill", bizBankBill);
        // 收款单的付款单位--客户管理
        List<BizCustomer> customerList = customerService.selectCustomerAll();
        mmap.put("customerList", customerList);

        return prefix + "/editCollect";
    }

    /**
     * 修改保存银行日记账（收款）
     */
    @RequiresPermissions("finance:bankBill:edit")
    @Log(title = "银行日记账", businessType = BusinessType.UPDATE)
    @PostMapping("/editCollect")
    @ResponseBody
    public AjaxResult editCollectSave(BizBankBill bizBankBill) {
        return toAjax(bizBankBillService.updateBizBankBill(bizBankBill));
    }
    /**
     * 新增银行日记账（付款）
     */
    @GetMapping("/addPay")
    public String addPay(ModelMap mmap) {
        // 收款单的付款单位--客户管理
        List<BizCustomer> customerList = customerService.selectCustomerAll();
        mmap.put("customerList", customerList);
        return prefix + "/addPay";
    }

    /**
     * 新增保存银行日记账（付款）
     */
    @RequiresPermissions("finance:bankBill:addPay")
    @Log(title = "银行日记账（付款）", businessType = BusinessType.INSERT)
    @PostMapping("/addPay")
    @ResponseBody
    public AjaxResult addPaySave(BizBankBill bizBankBill) {
        return toAjax(bizBankBillService.insertBizBankBill(bizBankBill));
    }

    /**
     * 修改银行日记账（付款）
     */
    @GetMapping("/edit/{billId}")
    public String edit(@PathVariable("billId") Long billId, ModelMap mmap) {
        BizBankBill bizBankBill = bizBankBillService.selectBizBankBillById(billId);
        mmap.put("bizBill", bizBankBill);
        // 收款单的付款单位--客户管理
        List<BizCustomer> customerList = customerService.selectCustomerAll();
        mmap.put("customerList", customerList);

        return prefix + "/editPay";
    }

    /**
     * 修改保存银行日记账（付款）
     */
    @RequiresPermissions("finance:bankBill:editPay")
    @Log(title = "银行日记账", businessType = BusinessType.UPDATE)
    @PostMapping("/editPay")
    @ResponseBody
    public AjaxResult editPaySave(BizBankBill bizBankBill) {
        return toAjax(bizBankBillService.updateBizBankBill(bizBankBill));
    }

    /**
     * 删除银行日记账账
     */
    @RequiresPermissions("finance:bankBill:remove")
    @Log(title = "银行日记账", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizBankBillService.deleteBizBankBillByIds(ids));
    }


}
