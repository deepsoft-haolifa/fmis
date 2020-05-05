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
import com.ruoyi.fmis.define.domain.BizProcessDefine;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业务流程定义Controller
 *
 * @author frank
 * @date 2020-05-05
 */
@Controller
@RequestMapping("/fmis/define")
public class BizProcessDefineController extends BaseController {
    private String prefix = "fmis/define";

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @RequiresPermissions("fmis:define:view")
    @GetMapping()
    public String define() {
        return prefix + "/define";
    }

    /**
     * 查询业务流程定义列表
     */
    @RequiresPermissions("fmis:define:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessDefine bizProcessDefine) {
        startPage();
        List<BizProcessDefine> list = bizProcessDefineService.selectBizProcessDefineList(bizProcessDefine);
        return getDataTable(list);
    }

    /**
     * 导出业务流程定义列表
     */
    @RequiresPermissions("fmis:define:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProcessDefine bizProcessDefine) {
        List<BizProcessDefine> list = bizProcessDefineService.selectBizProcessDefineList(bizProcessDefine);
        ExcelUtil<BizProcessDefine> util = new ExcelUtil<BizProcessDefine>(BizProcessDefine.class);
        return util.exportExcel(list, "define");
    }

    /**
     * 新增业务流程定义
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存业务流程定义
     */
    @RequiresPermissions("fmis:define:add")
    @Log(title = "业务流程定义", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProcessDefine bizProcessDefine) {
        return toAjax(bizProcessDefineService.insertBizProcessDefine(bizProcessDefine));
    }

    /**
     * 修改业务流程定义
     */
    @GetMapping("/edit/{defineId}")
    public String edit(@PathVariable("defineId") Long defineId, ModelMap mmap) {
        BizProcessDefine bizProcessDefine = bizProcessDefineService.selectBizProcessDefineById(defineId);
        mmap.put("bizProcessDefine", bizProcessDefine);
        return prefix + "/edit";
    }

    /**
     * 修改保存业务流程定义
     */
    @RequiresPermissions("fmis:define:edit")
    @Log(title = "业务流程定义", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProcessDefine bizProcessDefine) {
        return toAjax(bizProcessDefineService.updateBizProcessDefine(bizProcessDefine));
    }

    /**
     * 删除业务流程定义
     */
    @RequiresPermissions("fmis:define:remove")
    @Log(title = "业务流程定义", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizProcessDefineService.deleteBizProcessDefineByIds(ids));
    }
}
