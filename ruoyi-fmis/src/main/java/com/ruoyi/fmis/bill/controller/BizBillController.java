package com.ruoyi.fmis.web.controller.fmis;

import java.util.List;
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
import com.ruoyi.fmis.bill.domain.BizBill;
import com.ruoyi.fmis.bill.service.IBizBillService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 现金日记账Controller
 *
 * @author frank
 * @date 2020-06-25
 */
@Controller
@RequestMapping("/fmis/bill")
public class BizBillController extends BaseController {
    private String prefix = "fmis/bill";

    @Autowired
    private IBizBillService bizBillService;

    @RequiresPermissions("fmis:bill:view")
    @GetMapping()
    public String bill() {
        return prefix + "/bill";
    }

    /**
     * 查询现金日记账列表
     */
    @RequiresPermissions("fmis:bill:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizBill bizBill) {
        startPage();
        List<BizBill> list = bizBillService.selectBizBillList(bizBill);
        return getDataTable(list);
    }

    /**
     * 导出现金日记账列表
     */
    @RequiresPermissions("fmis:bill:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizBill bizBill) {
        List<BizBill> list = bizBillService.selectBizBillList(bizBill);
        ExcelUtil<BizBill> util = new ExcelUtil<BizBill>(BizBill.class);
        return util.exportExcel(list, "bill");
    }

    /**
     * 新增现金日记账
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存现金日记账
     */
    @RequiresPermissions("fmis:bill:add")
    @Log(title = "现金日记账", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizBill bizBill) {
        return toAjax(bizBillService.insertBizBill(bizBill));
    }

    /**
     * 修改现金日记账
     */
    @GetMapping("/edit/{billId}")
    public String edit(@PathVariable("billId") Long billId, ModelMap mmap) {
        BizBill bizBill = bizBillService.selectBizBillById(billId);
        mmap.put("bizBill", bizBill);
        return prefix + "/edit";
    }

    /**
     * 修改保存现金日记账
     */
    @RequiresPermissions("fmis:bill:edit")
    @Log(title = "现金日记账", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizBill bizBill) {
        return toAjax(bizBillService.updateBizBill(bizBill));
    }

    /**
     * 删除现金日记账
     */
    @RequiresPermissions("fmis:bill:remove")
    @Log(title = "现金日记账", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizBillService.deleteBizBillByIds(ids));
    }
}
