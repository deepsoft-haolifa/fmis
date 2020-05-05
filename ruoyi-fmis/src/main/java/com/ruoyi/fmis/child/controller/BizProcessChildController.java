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
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程数据字Controller
 *
 * @author Xianlu Tech
 * @date 2020-05-05
 */
@Controller
@RequestMapping("/fmis/child")
public class BizProcessChildController extends BaseController {
    private String prefix = "fmis/child";

    @Autowired
    private IBizProcessChildService bizProcessChildService;

    @RequiresPermissions("fmis:child:view")
    @GetMapping()
    public String child() {
        return prefix + "/child";
    }

    /**
     * 查询流程数据字列表
     */
    @RequiresPermissions("fmis:child:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessChild bizProcessChild) {
        startPage();
        List<BizProcessChild> list = bizProcessChildService.selectBizProcessChildList(bizProcessChild);
        return getDataTable(list);
    }

    /**
     * 导出流程数据字列表
     */
    @RequiresPermissions("fmis:child:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProcessChild bizProcessChild) {
        List<BizProcessChild> list = bizProcessChildService.selectBizProcessChildList(bizProcessChild);
        ExcelUtil<BizProcessChild> util = new ExcelUtil<BizProcessChild>(BizProcessChild.class);
        return util.exportExcel(list, "child");
    }

    /**
     * 新增流程数据字
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存流程数据字
     */
    @RequiresPermissions("fmis:child:add")
    @Log(title = "流程数据字", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProcessChild bizProcessChild) {
        return toAjax(bizProcessChildService.insertBizProcessChild(bizProcessChild));
    }

    /**
     * 修改流程数据字
     */
    @GetMapping("/edit/{childId}")
    public String edit(@PathVariable("childId") Long childId, ModelMap mmap) {
        BizProcessChild bizProcessChild = bizProcessChildService.selectBizProcessChildById(childId);
        mmap.put("bizProcessChild", bizProcessChild);
        return prefix + "/edit";
    }

    /**
     * 修改保存流程数据字
     */
    @RequiresPermissions("fmis:child:edit")
    @Log(title = "流程数据字", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProcessChild bizProcessChild) {
        return toAjax(bizProcessChildService.updateBizProcessChild(bizProcessChild));
    }

    /**
     * 删除流程数据字
     */
    @RequiresPermissions("fmis:child:remove")
    @Log(title = "流程数据字", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizProcessChildService.deleteBizProcessChildByIds(ids));
    }
}
