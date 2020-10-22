package com.ruoyi.fmis.finance.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.finance.domain.BizBill;
import com.ruoyi.fmis.finance.domain.BizBillContract;
import com.ruoyi.fmis.finance.service.IBizBillContractService;
import com.ruoyi.fmis.finance.service.IBizBillService;
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
 * 合同收款(合同分解)Controller
 *
 * @author hedong
 * @date 2020-10-22
 */
@Controller
@RequestMapping("/finance/billContract")
public class BizBillContractController extends BaseController {
    private String prefix = "finance/billContract";

    @Autowired
    private IBizBillContractService bizBillContractService;
    @Autowired
    private IBizBillService bizBillService;
    @Autowired
    private IBizProcessDataService bizProcessDataService;
    @Autowired
    private IBizProcessDefineService bizProcessDefineService;


    @RequiresPermissions("finance:billContract:view")
    @GetMapping()
    public String billContract() {
        return prefix + "/billContract";
    }

    /**
     * 查询合同收款列表(银行记账-收款，收款类别是货款)
     */
    @RequiresPermissions("finance:billContract:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizBill bizBill) {
        startPage();
        bizBill.setType("1");
        bizBill.setCollectionType("2");
        List<BizBill> list = bizBillService.selectBizBillList(bizBill);
        return getDataTable(list);
    }

    /**
     * 合同分解编辑
     */
    @GetMapping("/edit/{billId}")
    public String edit(@PathVariable("billId") Long billId, ModelMap mmap) {
        BizBill bizBill = bizBillService.selectBizBillById(billId);
        mmap.put("bizBill", bizBill);
        return prefix + "/edit";
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
                    SysRole currentSysRole = CommonUtils.getLikeByMap(flowAllMap, flowStatus.replaceAll("-", ""));
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


    @PostMapping("/addContract")
    @ResponseBody
    public AjaxResult addContract() {
        String bcId = getRequest().getParameter("bcId");
        String dataId = getRequest().getParameter("dataId");//合同id
        String billId = getRequest().getParameter("billId");//业务数据id
        String remark = getRequest().getParameter("remark");
        String amount = getRequest().getParameter("amount");

        //TODO 判断合同的金额是否够分

        BizBillContract bizBillContract = new BizBillContract();
        if (!"0".equals(bcId) && StringUtils.isNotEmpty(bcId)) {
            bizBillContract = bizBillContractService.selectBizBillContractById(Long.parseLong(bcId));
        }
        bizBillContract.setBillId(Long.parseLong(billId));
        bizBillContract.setDataId(Long.parseLong(dataId));
        bizBillContract.setAmount(Double.parseDouble(amount));
        bizBillContract.setBookKeeper(ShiroUtils.getUserId().toString());
        bizBillContract.setRemark(remark);
        if ("0".equals(bcId) || StringUtils.isEmpty(bcId)) {
            bizBillContract.setCreateTime(new Date());
            bizBillContract.setCreateBy(ShiroUtils.getUserId().toString());
            bizBillContractService.insertBizBillContract(bizBillContract);
        } else {
            bizBillContractService.updateBizBillContract(bizBillContract);
        }

        //判断合同分解是否完成
        BizBillContract query1 = new BizBillContract();
        query1.setBillId(Long.parseLong(billId));
        List<BizBillContract> bizBillContracts = bizBillContractService.selectBizBillContractList(query1);
        double alreadyAmount = bizBillContracts.stream().mapToDouble(BizBillContract::getAmount).sum();
        BizBill bizBill = bizBillService.selectBizBillById(Long.parseLong(billId));
        BizBill updateBill = new BizBill();
        if (bizBill.getCollectionMoney() <= alreadyAmount) {
            // 将分解状态更改为分解完成
            updateBill.setContractStatus("1");
        } else {
            updateBill.setContractStatus("0");
        }
        updateBill.setBillId(bizBill.getBillId());
        bizBillService.updateBizBill(updateBill);
        return toAjax(1);
    }


    @PostMapping("/removeContract")
    @ResponseBody
    public AjaxResult removeTest() {
        String bcId = getRequest().getParameter("bcId");
        String billId = getRequest().getParameter("billId");//业务数据id
        if ("0".equals(bcId)) {
            return toAjax(1);
        }
        bizBillContractService.deleteBizBillContractById(Long.parseLong(bcId));
        BizBill updateBill = new BizBill();
        updateBill.setBillId(Long.parseLong(billId));
        bizBillService.updateBizBill(updateBill);
        updateBill.setContractStatus("0");
        return toAjax(1);
    }

//    /**
//     * 新增合同收款(合同分解)
//     */
//    @GetMapping("/add")
//    public String add() {
//        return prefix + "/add";
//    }
//
//    /**
//     * 新增保存合同收款(合同分解)
//     */
//    @RequiresPermissions("finance:billContract:add")
//    @Log(title = "合同收款(合同分解)", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(BizBillContract bizBillContract) {
//        return toAjax(bizBillContractService.insertBizBillContract(bizBillContract));
//    }
//
//    /**
//     * 修改合同收款(合同分解)
//     */
//    @GetMapping("/edit/{bcId}")
//    public String edit(@PathVariable("bcId") Long bcId, ModelMap mmap) {
//        BizBillContract bizBillContract = bizBillContractService.selectBizBillContractById(bcId);
//        mmap.put("bizBillContract", bizBillContract);
//        return prefix + "/edit";
//    }
//
//    /**
//     * 修改保存合同收款(合同分解)
//     */
//    @RequiresPermissions("finance:billContract:edit")
//    @Log(title = "合同收款(合同分解)", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(BizBillContract bizBillContract) {
//        return toAjax(bizBillContractService.updateBizBillContract(bizBillContract));
//    }
//
//    /**
//     * 删除合同收款(合同分解)
//     */
//    @RequiresPermissions("finance:billContract:remove")
//    @Log(title = "合同收款(合同分解)", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids) {
//        return toAjax(bizBillContractService.deleteBizBillContractByIds(ids));
//    }
}
