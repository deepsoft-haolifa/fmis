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
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import com.ruoyi.fmis.quotationproduct.service.IBizQuotationProductService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报价单产品Controller
 *
 * @author Xianlu Tech
 * @date 2020-03-21
 */
@Controller
@RequestMapping("/fmis/quotationproduct")
public class BizQuotationProductController extends BaseController {
    private String prefix = "fmis/quotationproduct";

    @Autowired
    private IBizQuotationProductService bizQuotationProductService;

    @RequiresPermissions("fmis:quotationproduct:view")
    @GetMapping()
    public String quotationproduct() {
        return prefix + "/quotationproduct";
    }

    /**
     * 查询报价单产品列表
     */
    @RequiresPermissions("fmis:quotationproduct:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizQuotationProduct bizQuotationProduct) {
        startPage();
        List<BizQuotationProduct> list = bizQuotationProductService.selectBizQuotationProductList(bizQuotationProduct);
        return getDataTable(list);
    }

    /**
     * 导出报价单产品列表
     */
    @RequiresPermissions("fmis:quotationproduct:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizQuotationProduct bizQuotationProduct) {
        List<BizQuotationProduct> list = bizQuotationProductService.selectBizQuotationProductList(bizQuotationProduct);
        ExcelUtil<BizQuotationProduct> util = new ExcelUtil<BizQuotationProduct>(BizQuotationProduct.class);
        return util.exportExcel(list, "quotationproduct");
    }

    /**
     * 新增报价单产品
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存报价单产品
     */
    @RequiresPermissions("fmis:quotationproduct:add")
    @Log(title = "报价单产品", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizQuotationProduct bizQuotationProduct) {
        return toAjax(bizQuotationProductService.insertBizQuotationProduct(bizQuotationProduct));
    }

    /**
     * 修改报价单产品
     */
    @GetMapping("/edit/{qpId}")
    public String edit(@PathVariable("qpId") Long qpId, ModelMap mmap) {
        BizQuotationProduct bizQuotationProduct = bizQuotationProductService.selectBizQuotationProductById(qpId);
        mmap.put("bizQuotationProduct", bizQuotationProduct);
        return prefix + "/edit";
    }

    /**
     * 修改保存报价单产品
     */
    @RequiresPermissions("fmis:quotationproduct:edit")
    @Log(title = "报价单产品", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizQuotationProduct bizQuotationProduct) {
        return toAjax(bizQuotationProductService.updateBizQuotationProduct(bizQuotationProduct));
    }

    /**
     * 删除报价单产品
     */
    @RequiresPermissions("fmis:quotationproduct:remove")
    @Log(title = "报价单产品", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizQuotationProductService.deleteBizQuotationProductByIds(ids));
    }
}
