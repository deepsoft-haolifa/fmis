package com.ruoyi.fmis.borrowingpay.controller;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.domain.BizBill;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.fmis.finance.service.IBizBillService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.service.ISysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 借款
 *
 * @author frank
 * @date 2020-05-05
 */
@Controller
@RequestMapping("/fmis/borrowingpay")
public class BizProcessDataBorrowingPayController extends BaseController {
    private String prefix = "fmis/borrowingpay";

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;


    @Autowired
    private IBizProcessChildService bizProcessChildService;


    @Autowired
    private IBizBankBillService bizBankBillService;

    @Autowired
    private IBizBillService bizBillService;

    @RequiresPermissions("fmis:borrowingpay:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }


    @RequiresPermissions("fmis:borrowingpay:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessData bizProcessData) {

        String bizId = bizProcessData.getBizId();

        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        String userFlowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            for (String key : flowMap.keySet()) {
                userFlowStatus = key;
            }

        }
        bizProcessData.setRoleType("0");
        startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataVoRefBorrowing(bizProcessData);

        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                String flowStatus = data.getFlowStatus();
                //结束标识
                String normalFlag = data.getNormalFlag();
                data.setLoginUserId(ShiroUtils.getUserId().toString());
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
                        //String userFlowStatus = flowMap.keySet().iterator().next();
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

    @GetMapping("/viewDetail")
    public String viewDetail(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataBorrowingById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/viewDetail";
    }


    @GetMapping("/upload")
    public String upload(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        mmap.put("dataId", dataId);
        mmap.put("fileUrl", Global.getFileUrl());
        return prefix + "/upload";
    }

    @PostMapping("/uploadUrl")
    @ResponseBody
    public AjaxResult uploadUrl() {
        String dataId = getRequest().getParameter("dataId");
        String url = getRequest().getParameter("url");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        bizProcessData.setString12(url);
        return toAjax(bizProcessDataService.updateBizProcessData(bizProcessData));
    }

    @GetMapping("/viewExamineHistory")
    public String viewExamine(ModelMap mmap) {

        String dataId = getRequest().getParameter("dataId");
        String bizId = getRequest().getParameter("bizId");
        mmap.put("bizId", dataId);
        mmap.put("bizTable", bizId);
        return prefix + "/viewExamineHistory";
    }

    /**
     * 放款
     */
    @GetMapping("/edit/{dataId}")
    public String edit(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataBorrowingById(dataId);

        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/edit";
    }

    /**
     * 修改保存合同管理
     */
    @RequiresPermissions("fmis:borrowingpay:edit")
    @PostMapping("/edit")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult editSave(BizProcessData bizProcessData) {

        int updateReturn = bizProcessDataService.updateBizProcessData(bizProcessData);
        // 已付款状态之后；

        if (updateReturn > 0 && "1".equals(bizProcessData.getString11())) {
            String bookingType = bizProcessData.getString13();
            if ("1".equals(bookingType)) {
                // 添加现金日记账
                if (!bizBillService.existsByCertificateNumber(bizProcessData.getString2())) {
                    BizBill bizBill = new BizBill();
                    bizBill.setType("1");
                    bizBill.setDeptId(bizProcessData.getString1());
                    bizBill.setCertificateNumber(bizProcessData.getString2());
                    bizBill.setD(bizProcessData.getDatetime3());
                    bizBill.setPaymentType("3");
                    bizBill.setPayment(bizProcessData.getPrice1());
                    bizBill.setRemark(bizProcessData.getRemark());
                    bizBill.setString1(bizProcessData.getString10());
                    bizBill.setString2(bizProcessData.getString5());
                    bizBillService.insertBizBill(bizBill);
                }
            } else if ("2".equals(bookingType)) {
                // 添加银行日记账
                if (!bizBankBillService.existsByCertificateNumber(bizProcessData.getString2())) {
                    BizBankBill bizBankBill = new BizBankBill();
                    bizBankBill.setType("2");
                    bizBankBill.setDeptId(bizProcessData.getString1());
                    bizBankBill.setCertificateNumber(bizProcessData.getString2());
                    bizBankBill.setOperateDate(bizProcessData.getDatetime3());
                    bizBankBill.setPaymentType("3");
                    bizBankBill.setPayment(bizProcessData.getPrice1());
                    bizBankBill.setRemark(bizProcessData.getRemark());
                    bizBankBill.setPayCompany(bizProcessData.getString10());
                    bizBankBill.setPayAccount(bizProcessData.getString12());
                    bizBankBill.setCollectCompany(bizProcessData.getString5());
                    bizBankBillService.insertBizBankBill(bizBankBill);
                }
            }
        }

        return toAjax(updateReturn);
    }

    /**
     * 删除合同管理
     */
    @RequiresPermissions("fmis:borrowingpay:remove")
    @Log(title = "合同管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizProcessDataService.deleteBizProcessDataByIds(ids));
    }

    @PostMapping("/report")
    @ResponseBody
    public AjaxResult report() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        return toAjax(bizProcessDataService.subReportBizQuotationBorrowing(bizQuotation));
    }

    @PostMapping("/pay")
    @ResponseBody
    public AjaxResult pay() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        bizQuotation.setString11("1");
        int i = bizProcessDataService.updateBizProcessData(bizQuotation);
        if (i > 0) {

        }
        return toAjax(i);
    }

}
