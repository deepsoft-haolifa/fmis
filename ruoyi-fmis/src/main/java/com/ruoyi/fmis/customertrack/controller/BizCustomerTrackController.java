package com.ruoyi.fmis.customertrack.controller;

import java.util.List;

import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customertrack.domain.BizCustomerTrackVo;
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
import com.ruoyi.fmis.customertrack.domain.BizCustomerTrack;
import com.ruoyi.fmis.customertrack.service.IBizCustomerTrackService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户追踪Controller
 *
 * @author frank
 * @date 2020-04-29
 */
@Controller
@RequestMapping("/fmis/customertrack")
public class BizCustomerTrackController extends BaseController {
    private String prefix = "fmis/customertrack";

    @Autowired
    private IBizCustomerTrackService bizCustomerTrackService;


    @RequiresPermissions("fmis:customerList:view")
    @GetMapping("/customer-list")
    public String customerlist() {
        return prefix + "/customer";
    }

    /**
     * 查询客户列表（关联了客户跟踪信息）
     */
    @RequiresPermissions("fmis:customerList:view")
    @PostMapping("/customer-list")
    @ResponseBody
    public TableDataInfo customerList(BizCustomer bizCustomer) {
        startPage();
        List<BizCustomer> list = bizCustomerTrackService.selectBizCustomerListAndTrack(bizCustomer);
        return getDataTable(list);
    }

    @RequiresPermissions("fmis:customertrack:view")
    @GetMapping("/track-list/{customerId}")
    public String customerTrack(@PathVariable("customerId") Long customerId, ModelMap mmap) {
        mmap.put("customerId", customerId);
        return prefix + "/customertrack";
    }

    /**
     * 查询客户追踪列表
     */
    @RequiresPermissions("fmis:customertrack:view")
    @PostMapping("/track-list/{customerId}")
    @ResponseBody
    public TableDataInfo trackList(@PathVariable("customerId") Long customerId) {
        startPage();
        BizCustomerTrack bizCustomerTrack=new BizCustomerTrack();
        bizCustomerTrack.setCustomerId(customerId);
        List<BizCustomerTrack> list = bizCustomerTrackService.selectBizCustomerTrackListAs(bizCustomerTrack);
        return getDataTable(list);
    }


    /**
     * 选择客户
     */
    @GetMapping("/selectCustomer")
    public String selectCustomer() {
        return prefix + "/selectCustomer";
    }


    /**
     * 导出客户追踪列表
     */
    @RequiresPermissions("fmis:customertrack:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizCustomerTrack bizCustomerTrack) {
        List<BizCustomerTrack> list = bizCustomerTrackService.selectBizCustomerTrackList(bizCustomerTrack);
        ExcelUtil<BizCustomerTrack> util = new ExcelUtil<BizCustomerTrack>(BizCustomerTrack.class);
        return util.exportExcel(list, "customertrack");
    }

    /**
     * 新增客户追踪
     */
    @GetMapping("/add/{customerId}/{customerName}")
    public String add(@PathVariable("customerId") Long customerId, @PathVariable("customerName") String customerName,ModelMap mmap) {
        mmap.put("customerId", customerId);
        mmap.put("customerName", customerName);
        return prefix + "/add";
    }

    /**
     * 新增保存客户追踪
     */
    @RequiresPermissions("fmis:customertrack:add")
    @Log(title = "客户追踪", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizCustomerTrack bizCustomerTrack) {
        return toAjax(bizCustomerTrackService.insertBizCustomerTrack(bizCustomerTrack));
    }

    /**
     * 修改客户追踪
     */
    @GetMapping("/edit/{trackId}")
    public String edit(@PathVariable("trackId") Long trackId, ModelMap mmap) {
        BizCustomerTrack queryBizCustomerTrack = new BizCustomerTrack();
        queryBizCustomerTrack.setTrackId(trackId);
        List<BizCustomerTrack> list = bizCustomerTrackService.selectBizCustomerTrackListAs(queryBizCustomerTrack);
        mmap.put("bizCustomerTrack", list.get(0));
        return prefix + "/edit";
    }

    /**
     * 修改保存客户追踪
     */
    @RequiresPermissions("fmis:customertrack:edit")
    @Log(title = "客户追踪", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizCustomerTrack bizCustomerTrack) {
        return toAjax(bizCustomerTrackService.updateBizCustomerTrack(bizCustomerTrack));
    }

    /**
     * 删除客户追踪
     */
    @RequiresPermissions("fmis:customertrack:remove")
    @Log(title = "客户追踪", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizCustomerTrackService.deleteBizCustomerTrackByIds(ids));
    }
}
