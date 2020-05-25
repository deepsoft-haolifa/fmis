package com.ruoyi.fmis.actuator.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.fmis.actuatorref.service.IBizActuatorRefService;
import com.ruoyi.fmis.dict.domain.BizDict;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 执行器Controller
 *
 * @author frank
 * @date 2020-03-17
 */
@Controller
@RequestMapping("/fmis/airActuator")
public class BizAirActuatorController extends BaseController {
    private String prefix = "fmis/airActuator";

    @Autowired
    private IBizActuatorService bizActuatorService;

    @Autowired
    private IBizDictService bizDictService;

    @Autowired
    private IBizActuatorRefService bizActuatorRefService;

    @RequiresPermissions("fmis:airActuator:view")
    @GetMapping()
    public String actuator() {
        return prefix + "/airActuator";
    }


    @Autowired
    private IBizProductService bizProductService;
    /**
     * 查询执行器列表
     */
    @RequiresPermissions("fmis:airActuator:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizActuator bizActuator) {
        startPage();
        List<BizActuator> list = bizActuatorService.selectBizActuatorList(bizActuator);
        return getDataTable(list);
    }

    @PostMapping("/listForProductId")
    @ResponseBody
    public TableDataInfo listForProductId(BizActuator bizActuator) {


        String productId = getRequest().getParameter("productId");

        BizProduct bizProduct = bizProductService.selectBizProductById(Long.parseLong(productId));

        if (bizProduct != null) {
            String specifications = bizProduct.getSpecifications();

            if (StringUtils.isNotEmpty(specifications)) {
                BizDict bizDict = bizDictService.selectBizDictById(Long.parseLong(specifications));
                if (bizDict != null) {
                    bizActuator.setString1(bizDict.getName());
                }
            }

            String nominalPressure = bizProduct.getNominalPressure();
            if (StringUtils.isNotEmpty(nominalPressure)) {
                BizDict bizDict = bizDictService.selectBizDictById(Long.parseLong(nominalPressure));
                if (bizDict != null) {
                    bizActuator.setString2(bizDict.getName());
                }
            }
        }

        startPage();
        List<BizActuator> list = bizActuatorService.selectBizActuatorForRefList(bizActuator);
        return getDataTable(list);
    }

    /**
     * 导出执行器列表
     */
    @RequiresPermissions("fmis:airActuator:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizActuator bizActuator) {
        List<BizActuator> list = bizActuatorService.selectBizActuatorList(bizActuator);
        ExcelUtil<BizActuator> util = new ExcelUtil<BizActuator>(BizActuator.class);
        return util.exportExcel(list, "actuator");
    }

    /**
     * 新增执行器
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存执行器
     */
    @RequiresPermissions("fmis:airActuator:add")
    @Log(title = "执行器", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizActuator bizActuator) {
        return toAjax(bizActuatorService.insertBizActuator(bizActuator));
    }

    /**
     * 修改执行器
     */
    @GetMapping("/edit/{actuatorId}")
    public String edit(@PathVariable("actuatorId") Long actuatorId, ModelMap mmap) {
        BizActuator bizActuator = bizActuatorService.selectBizActuatorById(actuatorId);
        mmap.put("bizActuator", bizActuator);
        return prefix + "/edit";
    }
    @GetMapping("/upload")
    public String upload(ModelMap mmap) {
        return prefix + "/upload";
    }
    /**
     * 修改保存执行器
     */
    @RequiresPermissions("fmis:airActuator:edit")
    @Log(title = "执行器", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizActuator bizActuator) {
        return toAjax(bizActuatorService.updateBizActuator(bizActuator));
    }

    /**
     * 删除执行器
     */
    @RequiresPermissions("fmis:airActuator:remove")
    @Log(title = "执行器", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizActuatorService.deleteBizActuatorByIds(ids));
    }
}
