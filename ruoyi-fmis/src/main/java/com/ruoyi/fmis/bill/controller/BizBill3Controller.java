package com.ruoyi.fmis.bill.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.bcontract.domain.BizBillContract;
import com.ruoyi.fmis.bcontract.service.IBizBillContractService;
import com.ruoyi.fmis.bill.domain.BizBill;
import com.ruoyi.fmis.bill.service.IBizBillService;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.stestn.domain.BizDataStestn;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行日记账 收款类型=入账货款 需要分解
 *
 * @author frank
 * @date 2020-06-25
 */
@Controller
@RequestMapping("/fmis/bill3")
public class BizBill3Controller extends BaseController {
    private String prefix = "fmis/bill3";

    @Autowired
    private IBizBillService bizBillService;

    @Autowired
    private IBizProcessDataService bizProcessDataService;
    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @Autowired
    private IBizBillContractService bizBillContractService;

    @RequiresPermissions("fmis:bill3:view")
    @GetMapping()
    public String bill() {
        return prefix + "/bill";
    }

    /**
     * 查询现金日记账列表
     */
    @RequiresPermissions("fmis:bill3:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizBill bizBill) {
        startPage();
        List<BizBill> list = bizBillService.selectBizBillList(bizBill);
        return getDataTable(list);
    }

    /**
     * 导出现金日记账列表
     */
    @RequiresPermissions("fmis:bill3:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizBill bizBill) {
        List<BizBill> list = bizBillService.selectBizBillList(bizBill);
        ExcelUtil<BizBill> util = new ExcelUtil<BizBill>(BizBill.class);
        return util.exportExcel(list, "bill");
    }

    /**
     * 新增现金日记账
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存现金日记账
     */
    @RequiresPermissions("fmis:bill3:add")
    @Log(title = "现金日记账", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizBill bizBill) {
        return toAjax(bizBillService.insertBizBill(bizBill));
    }

    /**
     * 修改现金日记账
     */
    @GetMapping("/edit/{billId}")
    public String edit(@PathVariable("billId") Long billId, ModelMap mmap) {
        BizBill bizBill = bizBillService.selectBizBillById(billId);
        mmap.put("bizBill", bizBill);
        return prefix + "/edit";
    }

    /**
     * 修改保存现金日记账
     */
    @RequiresPermissions("fmis:bill3:edit")
    @Log(title = "现金日记账", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizBill bizBill) {
        return toAjax(bizBillService.updateBizBill(bizBill));
    }

    /**
     * 删除现金日记账
     */
    @RequiresPermissions("fmis:bill3:remove")
    @Log(title = "现金日记账", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizBillService.deleteBizBillByIds(ids));
    }

    @PostMapping("/contractList")
    @ResponseBody
    public TableDataInfo contractList(BizProcessData bizProcessData) {


        BizProcessData newBizProcessData = new BizProcessData();
        String bizId = bizProcessData.getBizId();
        //采购池
        newBizProcessData.setBizId(bizId);
        newBizProcessData.setString13("1");
        newBizProcessData.setBizId(BizConstants.BIZ_contract);
        if (StringUtils.isNotEmpty(bizProcessData.getBizEditFlag())) {
            newBizProcessData.setBizEditFlag(bizProcessData.getBizEditFlag());
            newBizProcessData.setDataId(bizProcessData.getDataId());
        }
        //startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefBill(newBizProcessData);

        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                String flowStatus = data.getFlowStatus();
                //结束标识
                String normalFlag = data.getNormalFlag();
                String flowStatusRemark = "待上报";
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
                        String userFlowStatus = flowMap.keySet().iterator().next();
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


    @PostMapping("/selectBizBillChildList")
    @ResponseBody
    public TableDataInfo selectBizBillChildList(BizBillContract billContract) {
        BizBillContract queryBizBill = new BizBillContract();
        queryBizBill.setDataId(billContract.getDataId());
        queryBizBill.setBillId(billContract.getBillId());
        List<BizBillContract> bizProcessChildList = bizBillContractService.selectBizBillContractList(queryBizBill);
        return getDataTable(bizProcessChildList);
    }


    @PostMapping("/saveTest")
    @ResponseBody
    public AjaxResult saveTest() {
        String bcId = getRequest().getParameter("bcId");
        String dataId = getRequest().getParameter("dataId");//合同id
        String billId = getRequest().getParameter("billId");//业务数据id
        String remark = getRequest().getParameter("remark");

        String amount = getRequest().getParameter("amount");

        BizBillContract bizBillContract = new BizBillContract();

        if (!"0".equals(bcId) && StringUtils.isNotEmpty(bcId)) {
            bizBillContract = bizBillContractService.selectBizBillContractById(Long.parseLong(bcId));
        }
        bizBillContract.setBillId(Long.parseLong(billId));
        bizBillContract.setDataId(Long.parseLong(dataId));
        bizBillContract.setAmount(Double.parseDouble(amount));
        bizBillContract.setRemark(remark);
        if ("0".equals(bcId) || StringUtils.isEmpty(bcId)) {
            bizBillContract.setCreateTime(new Date());
            bizBillContract.setCreateBy(ShiroUtils.getUserId().toString());
            bizBillContractService.insertBizBillContract(bizBillContract);
        } else {
            bizBillContractService.updateBizBillContract(bizBillContract);
        }
        return toAjax(1);
    }


    @PostMapping("/removeTest")
    @ResponseBody
    public AjaxResult removeTest() {
        String bcId = getRequest().getParameter("bcId");
        if ("0".equals(bcId)) {
            return toAjax(1);
        }
        bizBillContractService.deleteBizBillContractById(Long.parseLong(bcId));
        return toAjax(1);
    }
}
