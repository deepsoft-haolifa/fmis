package com.ruoyi.fmis.product.controller;

import java.util.List;

import com.ruoyi.common.json.JSONObject;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.dict.domain.BizDict;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
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
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业务产品Controller
 *
 * @author Xianlu Tech
 * @date 2020-02-26
 */
@Controller
@RequestMapping("/fmis/product")
public class BizProductController extends BaseController {
    private String prefix = "fmis/product";

    @Autowired
    private IBizProductService bizProductService;

    @Autowired
    private IBizDictService bizDictService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @RequiresPermissions("fmis:product:view")
    @GetMapping()
    public String product() {
        return prefix + "/product";
    }

    /**
     * 查询业务产品列表
     */
    @RequiresPermissions("fmis:product:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProduct bizProduct) {
        startPage();
        List<BizProduct> list = bizProductService.selectBizProductList(bizProduct);
        return getDataTable(list);
    }

    /**
     * 导出业务产品列表
     */
    @RequiresPermissions("fmis:product:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProduct bizProduct) {
        List<BizProduct> list = bizProductService.selectBizProductList(bizProduct);
        ExcelUtil<BizProduct> util = new ExcelUtil<BizProduct>(BizProduct.class);
        return util.exportExcel(list, "product");
    }

    /**
     * 新增业务产品
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {

        bizDictService.selectBizDictByProductType(BizConstants.specificationCode);

        mmap.put("seriesSelect",bizDictService.selectBizDictByProductType(BizConstants.specificationCode));
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        return prefix + "/add";
    }



    /**
     * 新增保存业务产品
     */
    @RequiresPermissions("fmis:product:add")
    @Log(title = "业务产品", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProduct bizProduct) {
        return toAjax(bizProductService.insertBizProduct(bizProduct));
    }

    @PostMapping("/findProductTypeDict")
    @ResponseBody
    public JSONObject findProductTypeDict(Long dictId) {
        return bizDictService.selectProductTypeDict(dictId);
    }


    /**
     * 修改业务产品
     */
    @GetMapping("/edit/{productId}")
    public String edit(@PathVariable("productId") Long productId, ModelMap mmap) {
        BizProduct bizProduct = bizProductService.selectBizProductById(productId);
        mmap.put("bizProduct", bizProduct);

        List<BizDict> bizDictList = bizDictService.selectBizDictByProductType(BizConstants.productTypeCode);

        for (BizDict bizDict : bizDictList) {
            String dictId = bizDict.getDictId().toString();
            if (dictId.equals(bizProduct.getSeries())) {
                bizDict.setFlag(true);
            }
        }

        mmap.put("seriesSelect",bizDictList);

        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers suppliers : suppliersList) {
            String supplierId = bizProduct.getSupplier();
            if (supplierId.equals(suppliers.getSuppliersId().toString())) {
                suppliers.setFlag(true);
            }
        }
        mmap.put("suppliers",suppliersList);
        return prefix + "/edit";
    }

    /**
     * 修改保存业务产品
     */
    @RequiresPermissions("fmis:product:edit")
    @Log(title = "业务产品", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProduct bizProduct) {
        return toAjax(bizProductService.updateBizProduct(bizProduct));
    }

    /**
     * 删除业务产品
     */
    @RequiresPermissions("fmis:product:remove")
    @Log(title = "业务产品", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizProductService.deleteBizProductByIds(ids));
    }
}
