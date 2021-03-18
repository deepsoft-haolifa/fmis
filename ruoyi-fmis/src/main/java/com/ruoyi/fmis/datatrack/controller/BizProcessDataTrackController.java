package com.ruoyi.fmis.datatrack.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customertrack.domain.BizCustomerTrack;
import com.ruoyi.fmis.customertrack.service.IBizCustomerTrackService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.datatrack.domain.BizProcessDataTrack;
import com.ruoyi.fmis.datatrack.service.IBizProcessDataTrackService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 合同追踪Controller
 *
 * @author frank
 * @date 2020-04-29
 */
@Controller
@RequestMapping("/fmis/processDataTrack")
public class BizProcessDataTrackController extends BaseController {
    private String prefix = "fmis/processDataTrack";

    @Autowired
    private IBizProcessDataTrackService bizProcessDataTrackService;
    @Autowired
    private IBizSuppliersService bizSuppliersService;
    @Autowired
    private IBizProcessDataService bizProcessDataService;
    @Autowired
    private IBizProcessDefineService bizProcessDefineService;


    @RequiresPermissions("fmis:processDataTrack:view")
    @GetMapping("/process-data-list")
    public String dataList() {
        return prefix + "/dataList";
    }


    @RequiresPermissions("fmis:processDataTrack:view")
    @PostMapping("/process-data-list")
    @ResponseBody
    public TableDataInfo list(BizProcessData bizProcessData) {

        String bizId = bizProcessData.getBizId();
        if (!StringUtils.isEmpty(bizProcessData.getString6())) {
            BizSuppliers bizSuppliers = new BizSuppliers();
            bizSuppliers.setName(bizProcessData.getString6());
            List<BizSuppliers> suppliers =  bizSuppliersService.selectBizSuppliersList(bizSuppliers);
            if (suppliers != null && suppliers.size() > 0) {
                bizSuppliers = suppliers.get(0);
                bizProcessData.setString6(bizSuppliers.getSuppliersId() + "");
            } else {
                bizProcessData.setString6("");
            }

        }

        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        String userFlowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            userFlowStatus = flowMap.keySet().iterator().next();
            bizProcessData.setRoleType(userFlowStatus);
        }

        startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefProcurement(bizProcessData);

        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                String flowStatus = data.getFlowStatus();
                //结束标识
                String normalFlag = data.getNormalFlag();
                String flowStatusRemark = "待上报";
                data.setLoginUserId(ShiroUtils.getUserId().toString());
                if ("-2".equals(flowStatus)) {
                    flowStatusRemark = "待上报";
                } else if ("1".equals(flowStatus)) {
                    flowStatusRemark = "已上报";
                } else {
                    SysRole currentSysRole =  CommonUtils.getLikeByMap(flowAllMap,flowStatus.replaceAll("-",""));
                    if (currentSysRole == null) {
                        continue;
                    }
                    if (flowStatus.equals(normalFlag)) {
                        flowStatusRemark = currentSysRole.getRoleName() + "已完成";
                    } else if (flowStatus.startsWith("-")) {
                        //不同意标识
                        flowStatusRemark = currentSysRole.getRoleName() + "不同意";
                    } else {
                        flowStatusRemark = currentSysRole.getRoleName() + "同意";
                    }
                }
                data.setFlowStatusRemark(flowStatusRemark);
                //计算是否可以审批
                int flowStatusInt = Integer.parseInt(flowStatus);
                data.setOperationExamineStatus(false);

                if (flowStatusInt > 0) {
                    if (!flowStatus.equals(normalFlag)) {
                        userFlowStatus = flowMap.keySet().iterator().next();
                        int userFlowStatusInt = Integer.parseInt(userFlowStatus);
                        if (userFlowStatusInt == flowStatusInt + 1) {
                            data.setOperationExamineStatus(true);
                        }

                    }
                }
            }
        }
        return getDataTable(list);
    }


    /**
     * 新增采购合同追踪
     */
    @RequiresPermissions("fmis:processDataTrack:add")
    @GetMapping("/addTrack/{dataId}")
    public String addTrack(@PathVariable("dataId") Long dataId,ModelMap mmap) {
        mmap.put("dataId", dataId);
        return prefix + "/addTrack";
    }

    /**
     * 新增保存采购合同追踪
     */
    @RequiresPermissions("fmis:processDataTrack:add")
    @Log(title = "采购合同追踪", businessType = BusinessType.INSERT)
    @PostMapping("/addTrack")
    @ResponseBody
    public AjaxResult addTrack(BizProcessDataTrack bizProcessDataTrack) {
        return toAjax(bizProcessDataTrackService.insertBizProcessDataTrack(bizProcessDataTrack));
    }

    @RequiresPermissions("fmis:processDataTrack:trackList")
    @GetMapping("/trackList/{dataId}")
    public String trackList(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        mmap.put("dataId", dataId);
        return prefix + "/trackList";
    }

    /**
     * 查询采购合同追踪列表
     */
    @RequiresPermissions("fmis:processDataTrack:trackList")
    @PostMapping("/trackList/{dataId}")
    @ResponseBody
    public TableDataInfo trackList(@PathVariable("dataId") Long dataId) {
        startPage();
        BizProcessDataTrack bizProcessDataTrack=new BizProcessDataTrack();
        bizProcessDataTrack.setDataId(dataId);
        List<BizProcessDataTrack> list = bizProcessDataTrackService.selectBizProcessDataTrackList(bizProcessDataTrack);
        return getDataTable(list);
    }
//    /**
//     * 修改合同追踪
//     */
//    @GetMapping("/edit/{trackId}")
//    public String edit(@PathVariable("trackId") Long trackId, ModelMap mmap) {
//        BizCustomerTrack queryBizCustomerTrack = new BizCustomerTrack();
//        queryBizCustomerTrack.setTrackId(trackId);
//        List<BizCustomerTrack> list = bizCustomerTrackService.selectBizCustomerTrackListAs(queryBizCustomerTrack);
//        mmap.put("bizCustomerTrack", list.get(0));
//        return prefix + "/edit";
//    }
//
//    /**
//     * 修改保存合同追踪
//     */
//    @RequiresPermissions("fmis:customertrack:edit")
//    @Log(title = "合同追踪", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(BizCustomerTrack bizCustomerTrack) {
//        return toAjax(bizCustomerTrackService.updateBizCustomerTrack(bizCustomerTrack));
//    }
//
//    /**
//     * 删除合同追踪
//     */
//    @RequiresPermissions("fmis:customertrack:remove")
//    @Log(title = "合同追踪", businessType = BusinessType.DELETE)
//    @PostMapping("/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids) {
//        return toAjax(bizCustomerTrackService.deleteBizCustomerTrackByIds(ids));
//    }
}
