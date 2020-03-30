package com.ruoyi.fmis.productref.controller;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.dict.domain.BizDict;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
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
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 产品关系Controller
 *
 * @author frank
 * @date 2020-03-17
 */
@Controller
@RequestMapping("/fmis/productRef")
public class BizProductRefController extends BaseController {
    private String prefix = "fmis/productRef";

    @Autowired
    private IBizProductRefService bizProductRefService;

    @Autowired
    private IBizDictService bizDictService;

    @Autowired
    private IBizProductService bizProductService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @RequiresPermissions("fmis:productRef:view")
    @GetMapping()
    public String productRef() {
        return prefix + "/productRef";
    }

    /**
     * 查询产品关系列表
     */
    @RequiresPermissions("fmis:productRef:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProductRef bizProductRef) {
        startPage();
        List<BizProductRef> list = bizProductRefService.selectBizProductRefList(bizProductRef);
        return getDataTable(list);
    }

    @PostMapping("/listForProductId")
    @ResponseBody
    public TableDataInfo listForProductId(BizProductRef bizProductRef) {

        String productId = getRequest().getParameter("productId");
        String refType = getRequest().getParameter("refType");

        BizProduct bizProduct = bizProductService.selectBizProductById(Long.parseLong(productId));
        if (bizProduct != null) {
            String specifications = bizProduct.getSpecifications();
            if (StringUtils.isNotEmpty(specifications)) {
                BizDict bizDict = bizDictService.selectBizDictById(Long.parseLong(specifications));
                if (bizDict != null) {
                    bizProductRef.setSpecifications(bizDict.getName());
                }
            }

            String valvebodyMaterial = bizProduct.getValvebodyMaterial();
            if (StringUtils.isNotEmpty(valvebodyMaterial)) {
                BizDict bizDict = bizDictService.selectBizDictById(Long.parseLong(valvebodyMaterial));
                if (bizDict != null) {
                    bizProductRef.setValvebodyMaterial(bizDict.getName());
                }
            }
        }
        bizProductRef.setType(refType);

        //如果是螺栓的话 搜索全部
        if ("2".equals(refType)) {
            bizProductRef = new BizProductRef();
            bizProductRef.setType(refType);
        }

        startPage();
        List<BizProductRef> list = bizProductRefService.selectBizProductRefList(bizProductRef);
        return getDataTable(list);
    }

    /**
     * 导出产品关系列表
     */
    @RequiresPermissions("fmis:productRef:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProductRef bizProductRef) {
        List<BizProductRef> list = bizProductRefService.selectBizProductRefList(bizProductRef);
        ExcelUtil<BizProductRef> util = new ExcelUtil<BizProductRef>(BizProductRef.class);
        return util.exportExcel(list, "productRef");
    }

    /**
     * 新增产品关系
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("specifications",bizDictService.selectProductDictForParentType(BizConstants.specificationCode,0L));
        mmap.put("valvebodyMaterials",bizDictService.selectProductDictForParentType(BizConstants.bodyMaterial,0L));
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        return prefix + "/add";
    }

    /**
     * 新增保存产品关系
     */
    @RequiresPermissions("fmis:productRef:add")
    @Log(title = "产品关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProductRef bizProductRef) {
        return toAjax(bizProductRefService.insertBizProductRef(bizProductRef));
    }

    /**
     * 修改产品关系
     */
    @GetMapping("/edit/{productRefId}")
    public String edit(@PathVariable("productRefId") Long productRefId, ModelMap mmap) {
        BizProductRef bizProductRef = bizProductRefService.selectBizProductRefById(productRefId);

        mmap.put("specifications",bizDictService.selectProductDictForParentType(BizConstants.specificationCode,Long.parseLong(bizProductRef.getSpecifications())));
        mmap.put("valvebodyMaterials",bizDictService.selectProductDictForParentType(BizConstants.bodyMaterial,Long.parseLong(bizProductRef.getValvebodyMaterial())));


        List<BizSuppliers> bizSuppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers bizSuppliers : bizSuppliersList) {
            if (bizSuppliers.getSuppliersId().toString().equals(bizProductRef.getSuppliersId().toString())) {
                bizSuppliers.setFlag(true);
            }
        }

        mmap.put("suppliers",bizSuppliersList);

        mmap.put("bizProductRef", bizProductRef);
        return prefix + "/edit";
    }

    /**
     * 修改保存产品关系
     */
    @RequiresPermissions("fmis:productRef:edit")
    @Log(title = "产品关系", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProductRef bizProductRef) {
        return toAjax(bizProductRefService.updateBizProductRef(bizProductRef));
    }

    /**
     * 删除产品关系
     */
    @RequiresPermissions("fmis:productRef:remove")
    @Log(title = "产品关系", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizProductRefService.deleteBizProductRefByIds(ids));
    }
}
