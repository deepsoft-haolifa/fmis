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
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 供应商Controller
 *
 * @author Xianlu Tech
 * @date 2020-03-06
 */
@Controller
@RequestMapping("/fmis/suppliers")
public class BizSuppliersController extends BaseController {
    private String prefix = "fmis/suppliers";

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @RequiresPermissions("fmis:suppliers:view")
    @GetMapping()
    public String suppliers() {
        return prefix + "/suppliers";
    }

    /**
     * 查询供应商列表
     */
    @RequiresPermissions("fmis:suppliers:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizSuppliers bizSuppliers) {
        startPage();
        List<BizSuppliers> list = bizSuppliersService.selectBizSuppliersList(bizSuppliers);
        return getDataTable(list);
    }

    /**
     * 导出供应商列表
     */
    @RequiresPermissions("fmis:suppliers:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizSuppliers bizSuppliers) {
        List<BizSuppliers> list = bizSuppliersService.selectBizSuppliersList(bizSuppliers);
        ExcelUtil<BizSuppliers> util = new ExcelUtil<BizSuppliers>(BizSuppliers.class);
        return util.exportExcel(list, "suppliers");
    }

    /**
     * 新增供应商
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存供应商
     */
    @RequiresPermissions("fmis:suppliers:add")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizSuppliers bizSuppliers) {
        return toAjax(bizSuppliersService.insertBizSuppliers(bizSuppliers));
    }

    /**
     * 修改供应商
     */
    @GetMapping("/edit/{suppliersId}")
    public String edit(@PathVariable("suppliersId") Long suppliersId, ModelMap mmap) {
        BizSuppliers bizSuppliers = bizSuppliersService.selectBizSuppliersById(suppliersId);
        mmap.put("bizSuppliers", bizSuppliers);
        return prefix + "/edit";
    }

    /**
     * 修改保存供应商
     */
    @RequiresPermissions("fmis:suppliers:edit")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizSuppliers bizSuppliers) {
        return toAjax(bizSuppliersService.updateBizSuppliers(bizSuppliers));
    }

    /**
     * 删除供应商
     */
    @RequiresPermissions("fmis:suppliers:remove")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizSuppliersService.deleteBizSuppliersByIds(ids));
    }
}
