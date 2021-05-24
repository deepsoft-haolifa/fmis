package com.ruoyi.fmis.datatrack.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.Constant;
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
import com.ruoyi.fmis.util.RoleEnum;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            List<BizSuppliers> suppliers = bizSuppliersService.selectBizSuppliersList(bizSuppliers);
            if (suppliers != null && suppliers.size() > 0) {
                bizSuppliers = suppliers.get(0);
                bizProcessData.setString6(bizSuppliers.getSuppliersId() + "");
            } else {
                bizProcessData.setString6("");
            }

        }

        // 销售业务员，只能看到自己的合同
        List<SysRole> roles = ShiroUtils.getSysUser().getRoles();
        Set<String> roleKeySet = roles.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
        if (roleKeySet.contains(RoleEnum.XS_YWY.getRoleKey())) {
            // 获取此业务员的合同号；
            BizProcessData data=new BizProcessData();
            data.setBizId(Constant.processDataBizId.CONTRACT);
            data.setCreateBy(String.valueOf(ShiroUtils.getSysUser().getUserId()));
            List<BizProcessData> dataList = bizProcessDataService.selectBizProcessDataList(data);
            if(CollectionUtils.isEmpty(dataList)){
                return getDataTable(null);
            } else {
                List<String> contractNoList = dataList.stream().map(BizProcessData::getString1).collect(Collectors.toList());
                if(StringUtils.isNotEmpty(bizProcessData.getString10())){
                    if(!contractNoList.contains(bizProcessData.getString10())){
                        return getDataTable(null);
                    }
                }else{
                    bizProcessData.setContractNoList(contractNoList);
                }
            }
        }
        startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefTrack(bizProcessData);
        for (BizProcessData data : list) {
            data.setLoginUserId(ShiroUtils.getUserId().toString());
        }
        return getDataTable(list);
    }


    /**
     * 新增采购合同追踪
     */
    @RequiresPermissions("fmis:processDataTrack:add")
    @GetMapping("/addTrack/{dataId}")
    public String addTrack(@PathVariable("dataId") Long dataId, ModelMap mmap) {
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
        BizProcessDataTrack bizProcessDataTrack = new BizProcessDataTrack();
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
