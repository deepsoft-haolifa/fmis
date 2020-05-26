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
import com.ruoyi.fmis.chistory.domain.BizCustomerHistory;
import com.ruoyi.fmis.chistory.service.IBizCustomerHistoryService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户分配记录Controller
 *
 * @author frank
 * @date 2020-05-26
 */
@Controller
@RequestMapping("/fmis/chistory")
public class BizCustomerHistoryController extends BaseController {
    private String prefix = "fmis/chistory";

    @Autowired
    private IBizCustomerHistoryService bizCustomerHistoryService;

    @RequiresPermissions("fmis:chistory:view")
    @GetMapping()
    public String chistory() {
        return prefix + "/chistory";
    }

    /**
     * 查询客户分配记录列表
     */
    @RequiresPermissions("fmis:chistory:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizCustomerHistory bizCustomerHistory) {
        startPage();
        List<BizCustomerHistory> list = bizCustomerHistoryService.selectBizCustomerHistoryList(bizCustomerHistory);
        return getDataTable(list);
    }

    /**
     * 导出客户分配记录列表
     */
    @RequiresPermissions("fmis:chistory:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizCustomerHistory bizCustomerHistory) {
        List<BizCustomerHistory> list = bizCustomerHistoryService.selectBizCustomerHistoryList(bizCustomerHistory);
        ExcelUtil<BizCustomerHistory> util = new ExcelUtil<BizCustomerHistory>(BizCustomerHistory.class);
        return util.exportExcel(list, "chistory");
    }

    /**
     * 新增客户分配记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存客户分配记录
     */
    @RequiresPermissions("fmis:chistory:add")
    @Log(title = "客户分配记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizCustomerHistory bizCustomerHistory) {
        return toAjax(bizCustomerHistoryService.insertBizCustomerHistory(bizCustomerHistory));
    }

    /**
     * 修改客户分配记录
     */
    @GetMapping("/edit/{historyId}")
    public String edit(@PathVariable("historyId") Long historyId, ModelMap mmap) {
        BizCustomerHistory bizCustomerHistory = bizCustomerHistoryService.selectBizCustomerHistoryById(historyId);
        mmap.put("bizCustomerHistory", bizCustomerHistory);
        return prefix + "/edit";
    }

    /**
     * 修改保存客户分配记录
     */
    @RequiresPermissions("fmis:chistory:edit")
    @Log(title = "客户分配记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizCustomerHistory bizCustomerHistory) {
        return toAjax(bizCustomerHistoryService.updateBizCustomerHistory(bizCustomerHistory));
    }

    /**
     * 删除客户分配记录
     */
    @RequiresPermissions("fmis:chistory:remove")
    @Log(title = "客户分配记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizCustomerHistoryService.deleteBizCustomerHistoryByIds(ids));
    }
}
