package com.ruoyi.fmis.actuatorref.controller;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.dict.service.IBizDictService;
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
import com.ruoyi.fmis.actuatorref.domain.BizActuatorRef;
import com.ruoyi.fmis.actuatorref.service.IBizActuatorRefService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 执行器关系管理Controller
 *
 * @author frank
 * @date 2020-03-17
 */
@Controller
@RequestMapping("/fmis/actuatorRef")
public class BizActuatorRefController extends BaseController {
    private String prefix = "fmis/actuatorRef";

    @Autowired
    private IBizActuatorRefService bizActuatorRefService;

    @Autowired
    private IBizDictService bizDictService;

    @RequiresPermissions("fmis:actuatorRef:view")
    @GetMapping()
    public String actuatorRef(ModelMap mmap) {

        mmap.put("specifications",bizDictService.selectProductDictForParentType(BizConstants.specificationCode,0L));
        mmap.put("nominalPressures",bizDictService.selectProductDictForParentType(BizConstants.nominalPressure,0L));


        return prefix + "/actuatorRef";
    }

    /**
     * 查询执行器关系管理列表
     */
    @RequiresPermissions("fmis:actuatorRef:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizActuatorRef bizActuatorRef) {
        startPage();
        List<BizActuatorRef> list = bizActuatorRefService.selectBizActuatorRefList(bizActuatorRef);
        return getDataTable(list);
    }

    /**
     * 导出执行器关系管理列表
     */
    @RequiresPermissions("fmis:actuatorRef:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizActuatorRef bizActuatorRef) {
        List<BizActuatorRef> list = bizActuatorRefService.selectBizActuatorRefList(bizActuatorRef);
        ExcelUtil<BizActuatorRef> util = new ExcelUtil<BizActuatorRef>(BizActuatorRef.class);
        return util.exportExcel(list, "actuatorRef");
    }

    /**
     * 新增执行器关系管理
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("specifications",bizDictService.selectProductDictForParentType(BizConstants.specificationCode,0L));
        mmap.put("nominalPressures",bizDictService.selectProductDictForParentType(BizConstants.nominalPressure,0L));

        return prefix + "/add";
    }

    /**
     * 新增保存执行器关系管理
     */
    @RequiresPermissions("fmis:actuatorRef:add")
    @Log(title = "执行器关系管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizActuatorRef bizActuatorRef) {
        return toAjax(bizActuatorRefService.insertBizActuatorRef(bizActuatorRef));
    }

    /**
     * 修改执行器关系管理
     */
    @GetMapping("/edit/{arId}")
    public String edit(@PathVariable("arId") Long arId, ModelMap mmap) {
        BizActuatorRef bizActuatorRef = bizActuatorRefService.selectBizActuatorRefById(arId);

        Long valveType = 0L;
        Long pressure = 0L;
        if (StringUtils.isNotEmpty(bizActuatorRef.getValveType())) {
            valveType = Long.parseLong(bizActuatorRef.getValveType());
        }

        if (StringUtils.isNotEmpty(bizActuatorRef.getPressure())) {
            pressure = Long.parseLong(bizActuatorRef.getPressure());
        }

        mmap.put("specifications",bizDictService.selectProductDictForParentType(BizConstants.specificationCode,valveType));
        mmap.put("nominalPressures",bizDictService.selectProductDictForParentType(BizConstants.nominalPressure,pressure));


        mmap.put("bizActuatorRef", bizActuatorRef);
        return prefix + "/edit";
    }

    /**
     * 修改保存执行器关系管理
     */
    @RequiresPermissions("fmis:actuatorRef:edit")
    @Log(title = "执行器关系管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizActuatorRef bizActuatorRef) {
        return toAjax(bizActuatorRefService.updateBizActuatorRef(bizActuatorRef));
    }

    /**
     * 删除执行器关系管理
     */
    @RequiresPermissions("fmis:actuatorRef:remove")
    @Log(title = "执行器关系管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizActuatorRefService.deleteBizActuatorRefByIds(ids));
    }
}
