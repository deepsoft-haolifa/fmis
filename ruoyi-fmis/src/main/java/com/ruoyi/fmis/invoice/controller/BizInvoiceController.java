package com.ruoyi.fmis.invoice.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
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
import com.ruoyi.fmis.invoice.bean.BizInvoice;
import com.ruoyi.fmis.invoice.service.IBizInvoiceService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 发票信息（进项发票）Controller
 *
 * @author hedong
 * @date 2021-05-22
 */
@Controller
@RequestMapping("/fmis/bizInvoice")
public class BizInvoiceController extends BaseController {
    private String prefix = "fmis/invoice/bizInvoice";

    @Autowired
    private IBizInvoiceService bizInvoiceService;
    @Autowired
    private IBizSuppliersService bizSuppliersService;
    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @RequiresPermissions("fmis:bizInvoice:view")
    @GetMapping()
    public String invoice() {
        return prefix + "/invoice";
    }

    /**
     * 查询发票信息（进项发票）列表
     */
    @RequiresPermissions("fmis:bizInvoice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizInvoice bizInvoice) {
        startPage();
        List<BizInvoice> list = bizInvoiceService.selectBizInvoiceList(bizInvoice);
        return getDataTable(list);
    }

    /**
     * 导出发票信息（进项发票）列表
     */
    @RequiresPermissions("fmis:bizInvoice:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizInvoice bizInvoice) {
        List<BizInvoice> list = bizInvoiceService.selectBizInvoiceList(bizInvoice);
        ExcelUtil<BizInvoice> util = new ExcelUtil<BizInvoice>(BizInvoice.class);
        return util.exportExcel(list, "invoice");
    }

    /**
     * 新增发票信息（进项发票）
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        // 供应商列表
        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        mmap.put("suppliersList", suppliersList);
        return prefix + "/add";
    }

    /**
     * 新增保存发票信息（进项发票）
     */
    @RequiresPermissions("fmis:bizInvoice:add")
    @Log(title = "发票信息（进项发票）", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizInvoice bizInvoice) {
        return toAjax(bizInvoiceService.insertBizInvoice(bizInvoice));
    }

    /**
     * 修改发票信息（进项发票）
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        BizInvoice bizInvoice = bizInvoiceService.selectBizInvoiceById(id);
        mmap.put("bizInvoice", bizInvoice);

        // 供应商列表
        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        mmap.put("suppliersList", suppliersList);
        return prefix + "/edit";
    }

    /**
     * 修改保存发票信息（进项发票）
     */
    @RequiresPermissions("fmis:bizInvoice:edit")
    @Log(title = "发票信息（进项发票）", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizInvoice bizInvoice) {
        return toAjax(bizInvoiceService.updateBizInvoice(bizInvoice));
    }

    /**
     * 删除发票信息（进项发票）
     */
    @RequiresPermissions("fmis:bizInvoice:remove")
    @Log(title = "发票信息（进项发票）", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizInvoiceService.deleteBizInvoiceByIds(ids));
    }

    @RequiresPermissions("fmis:bizInvoice:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<BizInvoice> util = new ExcelUtil<BizInvoice>(BizInvoice.class);
        return util.importTemplateExcel("申报抵扣统计表");
    }

    @RequiresPermissions("fmis:bizInvoice:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<BizInvoice> util = new ExcelUtil<BizInvoice>(BizInvoice.class);
        List<BizInvoice> list = util.importExcel(file.getInputStream());
        String message = bizInvoiceService.importList(list, updateSupport);
        return AjaxResult.success(message);
    }

    @PostMapping("/selectContract/{supplierId}")
    @ResponseBody
    public List<String> selectContract(@PathVariable String supplierId) {
        BizProcessData bizProcessData = new BizProcessData();
        bizProcessData.setBizId("procurement");
        bizProcessData.setString6(supplierId);
        bizProcessData.setStatus("4");// 采购完成
        List<BizProcessData> bizProcessDataList = bizProcessDataService.selectBizProcessDataList(bizProcessData);
        return bizProcessDataList.stream().map(BizProcessData::getString12).collect(Collectors.toList());
    }

}
