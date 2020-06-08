package com.ruoyi.fmis.pattachment.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.pattachment.domain.BizProductAttachment;
import com.ruoyi.fmis.pattachment.service.IBizProductAttachmentService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 可离合减速器Controller
 *
 * @author frank
 * @date 2020-06-08
 */
@Controller
@RequestMapping("/fmis/pattachment4")
public class BizProductAttachment4Controller extends BaseController {
    private String prefix = "fmis/pattachment4";

    @Autowired
    private IBizProductAttachmentService bizProductAttachmentService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @RequiresPermissions("fmis:pattachment4:view")
    @GetMapping()
    public String pattachment(ModelMap mmap) {
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        return prefix + "/pattachment";
    }

    /**
     * 查询定位器列表
     */
    @RequiresPermissions("fmis:pattachment4:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProductAttachment bizProductAttachment) {
        startPage();
        List<BizProductAttachment> list = bizProductAttachmentService.selectBizProductAttachmentList(bizProductAttachment);
        return getDataTable(list);
    }

    /**
     * 导出定位器列表
     */
    @RequiresPermissions("fmis:pattachment4:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProductAttachment bizProductAttachment) {
        List<BizProductAttachment> list = bizProductAttachmentService.selectBizProductAttachmentList(bizProductAttachment);
        ExcelUtil<BizProductAttachment> util = new ExcelUtil<BizProductAttachment>(BizProductAttachment.class);
        return util.exportExcel(list, "pattachment");
    }

    /**
     * 新增定位器
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        return prefix + "/add";
    }

    /**
     * 新增保存定位器
     */
    @RequiresPermissions("fmis:pattachment4:add")
    @Log(title = "回信器数", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProductAttachment bizProductAttachment) {
        return toAjax(bizProductAttachmentService.insertBizProductAttachment(bizProductAttachment));
    }

    /**
     * 修改定位器
     */
    @GetMapping("/edit/{attachmentId}")
    public String edit(@PathVariable("attachmentId") Long attachmentId, ModelMap mmap) {
        BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentById(attachmentId);
        mmap.put("bizProductAttachment", bizProductAttachment);
        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers suppliers : suppliersList) {
            String supplierId = bizProductAttachment.getSupplier();
            if (supplierId.equals(suppliers.getSuppliersId().toString())) {
                suppliers.setFlag(true);
            }
        }
        mmap.put("suppliers",suppliersList);
        return prefix + "/edit";
    }

    /**
     * 修改保存定位器
     */
    @RequiresPermissions("fmis:pattachment4:edit")
    @Log(title = "回信器数", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProductAttachment bizProductAttachment) {
        return toAjax(bizProductAttachmentService.updateBizProductAttachment(bizProductAttachment));
    }

    /**
     * 删除定位器
     */
    @RequiresPermissions("fmis:pattachment4:remove")
    @Log(title = "回信器数", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizProductAttachmentService.deleteBizProductAttachmentByIds(ids));
    }
}
