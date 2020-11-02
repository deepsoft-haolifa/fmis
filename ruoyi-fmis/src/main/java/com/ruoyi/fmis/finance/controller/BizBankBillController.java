package com.ruoyi.fmis.finance.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
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
     * 新增银行日记账
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存银行日记账
     */
    @RequiresPermissions("finance:bankBill:add")
    @Log(title = "银行日记账", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizBankBill bizBankBill) {
        return toAjax(bizBankBillService.insertBizBankBill(bizBankBill));
    }

    /**
     * 修改银行日记账
     */
    @GetMapping("/edit/{billId}")
    public String edit(@PathVariable("billId") Long billId, ModelMap mmap) {
        BizBankBill bizBankBill = bizBankBillService.selectBizBankBillById(billId);
        mmap.put("bizBill", bizBankBill);
        return prefix + "/edit";
    }

    /**
     * 修改保存银行日记账
     */
    @RequiresPermissions("finance:bankBill:edit")
    @Log(title = "银行日记账", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizBankBill bizBankBill) {
        return toAjax(bizBankBillService.updateBizBankBill(bizBankBill));
    }

    /**
     * 删除银行日记账账
     */
    @RequiresPermissions("finance:bankBill:remove")
    @Log(title = "银行日记账", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizBankBillService.deleteBizBankBillByIds(ids));
    }



}
