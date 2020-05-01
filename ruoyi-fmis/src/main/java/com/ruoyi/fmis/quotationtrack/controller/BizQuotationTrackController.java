package com.ruoyi.fmis.quotationtrack.controller;

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
import com.ruoyi.fmis.quotationtrack.domain.BizQuotationTrack;
import com.ruoyi.fmis.quotationtrack.service.IBizQuotationTrackService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报价追踪Controller
 *
 * @author Xianlu Tech
 * @date 2020-04-30
 */
@Controller
@RequestMapping("/fmis/quotationtrack")
public class BizQuotationTrackController extends BaseController {
    private String prefix = "fmis/quotationtrack";

    @Autowired
    private IBizQuotationTrackService bizQuotationTrackService;

    @RequiresPermissions("fmis:quotationtrack:view")
    @GetMapping()
    public String quotationtrack() {
        return prefix + "/quotationtrack";
    }

    @GetMapping("/selectQuotation")
    public String selectQuotation() {
        return prefix + "/selectQuotation";
    }

    /**
     * 查询报价追踪列表
     */
    @RequiresPermissions("fmis:quotationtrack:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizQuotationTrack bizQuotationTrack) {
        startPage();
        List<BizQuotationTrack> list = bizQuotationTrackService.selectBizQuotationTrackList(bizQuotationTrack);
        return getDataTable(list);
    }

    /**
     * 导出报价追踪列表
     */
    @RequiresPermissions("fmis:quotationtrack:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizQuotationTrack bizQuotationTrack) {
        List<BizQuotationTrack> list = bizQuotationTrackService.selectBizQuotationTrackList(bizQuotationTrack);
        ExcelUtil<BizQuotationTrack> util = new ExcelUtil<BizQuotationTrack>(BizQuotationTrack.class);
        return util.exportExcel(list, "quotationtrack");
    }

    /**
     * 新增报价追踪
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存报价追踪
     */
    @RequiresPermissions("fmis:quotationtrack:add")
    @Log(title = "报价追踪", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizQuotationTrack bizQuotationTrack) {
        return toAjax(bizQuotationTrackService.insertBizQuotationTrack(bizQuotationTrack));
    }

    /**
     * 修改报价追踪
     */
    @GetMapping("/edit/{trackId}")
    public String edit(@PathVariable("trackId") Long trackId, ModelMap mmap) {

        BizQuotationTrack bizQuotationTrack = new BizQuotationTrack();
        bizQuotationTrack.setTrackId(trackId);
        List<BizQuotationTrack> list = bizQuotationTrackService.selectBizQuotationTrackList(bizQuotationTrack);

        mmap.put("bizQuotationTrack", list.get(0));
        return prefix + "/edit";
    }

    /**
     * 修改保存报价追踪
     */
    @RequiresPermissions("fmis:quotationtrack:edit")
    @Log(title = "报价追踪", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizQuotationTrack bizQuotationTrack) {
        return toAjax(bizQuotationTrackService.updateBizQuotationTrack(bizQuotationTrack));
    }

    /**
     * 删除报价追踪
     */
    @RequiresPermissions("fmis:quotationtrack:remove")
    @Log(title = "报价追踪", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizQuotationTrackService.deleteBizQuotationTrackByIds(ids));
    }
}
