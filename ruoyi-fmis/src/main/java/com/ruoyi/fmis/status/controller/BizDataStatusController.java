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
import com.ruoyi.fmis.status.domain.BizDataStatus;
import com.ruoyi.fmis.status.service.IBizDataStatusService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据状态Controller
 *
 * @author frank
 * @date 2020-06-16
 */
@Controller
@RequestMapping("/fmis/status")
public class BizDataStatusController extends BaseController {
    private String prefix = "fmis/status";

    @Autowired
    private IBizDataStatusService bizDataStatusService;

    @RequiresPermissions("fmis:status:view")
    @GetMapping()
    public String status() {
        return prefix + "/status";
    }

    /**
     * 查询数据状态列表
     */
    @RequiresPermissions("fmis:status:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizDataStatus bizDataStatus) {
        startPage();
        List<BizDataStatus> list = bizDataStatusService.selectBizDataStatusList(bizDataStatus);
        return getDataTable(list);
    }

    /**
     * 导出数据状态列表
     */
    @RequiresPermissions("fmis:status:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizDataStatus bizDataStatus) {
        List<BizDataStatus> list = bizDataStatusService.selectBizDataStatusList(bizDataStatus);
        ExcelUtil<BizDataStatus> util = new ExcelUtil<BizDataStatus>(BizDataStatus.class);
        return util.exportExcel(list, "status");
    }

    /**
     * 新增数据状态
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存数据状态
     */
    @RequiresPermissions("fmis:status:add")
    @Log(title = "数据状态", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizDataStatus bizDataStatus) {
        return toAjax(bizDataStatusService.insertBizDataStatus(bizDataStatus));
    }

    /**
     * 修改数据状态
     */
    @GetMapping("/edit/{statusId}")
    public String edit(@PathVariable("statusId") Long statusId, ModelMap mmap) {
        BizDataStatus bizDataStatus = bizDataStatusService.selectBizDataStatusById(statusId);
        mmap.put("bizDataStatus", bizDataStatus);
        return prefix + "/edit";
    }

    /**
     * 修改保存数据状态
     */
    @RequiresPermissions("fmis:status:edit")
    @Log(title = "数据状态", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizDataStatus bizDataStatus) {
        return toAjax(bizDataStatusService.updateBizDataStatus(bizDataStatus));
    }

    /**
     * 删除数据状态
     */
    @RequiresPermissions("fmis:status:remove")
    @Log(title = "数据状态", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizDataStatusService.deleteBizDataStatusByIds(ids));
    }
}
