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
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.service.IBizFlowService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程记录Controller
 *
 * @author frank
 * @date 2020-03-18
 */
@Controller
@RequestMapping("/fmis/flow")
public class BizFlowController extends BaseController {
    private String prefix = "fmis/flow";

    @Autowired
    private IBizFlowService bizFlowService;

    @RequiresPermissions("fmis:flow:view")
    @GetMapping()
    public String flow() {
        return prefix + "/flow";
    }

    /**
     * 查询流程记录列表
     */
    @RequiresPermissions("fmis:flow:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizFlow bizFlow) {
        startPage();
        List<BizFlow> list = bizFlowService.selectBizFlowList(bizFlow);
        return getDataTable(list);
    }

    /**
     * 导出流程记录列表
     */
    @RequiresPermissions("fmis:flow:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizFlow bizFlow) {
        List<BizFlow> list = bizFlowService.selectBizFlowList(bizFlow);
        ExcelUtil<BizFlow> util = new ExcelUtil<BizFlow>(BizFlow.class);
        return util.exportExcel(list, "flow");
    }

    /**
     * 新增流程记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存流程记录
     */
    @RequiresPermissions("fmis:flow:add")
    @Log(title = "流程记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizFlow bizFlow) {
        return toAjax(bizFlowService.insertBizFlow(bizFlow));
    }

    /**
     * 修改流程记录
     */
    @GetMapping("/edit/{flowId}")
    public String edit(@PathVariable("flowId") Long flowId, ModelMap mmap) {
        BizFlow bizFlow = bizFlowService.selectBizFlowById(flowId);
        mmap.put("bizFlow", bizFlow);
        return prefix + "/edit";
    }

    /**
     * 修改保存流程记录
     */
    @RequiresPermissions("fmis:flow:edit")
    @Log(title = "流程记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizFlow bizFlow) {
        return toAjax(bizFlowService.updateBizFlow(bizFlow));
    }

    /**
     * 删除流程记录
     */
    @RequiresPermissions("fmis:flow:remove")
    @Log(title = "流程记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizFlowService.deleteBizFlowByIds(ids));
    }
}
