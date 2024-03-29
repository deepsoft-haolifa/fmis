package com.ruoyi.fmis.cpayment.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.service.ISysRoleService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 付款申请Controller
 *
 * @author frank
 * @date 2020-05-05
 */
@Controller
@RequestMapping("/fmis/cpayment")
public class BizProcessDataCpaymentController extends BaseController {
    private String prefix = "fmis/cpayment";

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @Autowired
    private IBizProcessChildService bizProcessChildService;

    @Autowired
    private IBizCustomerService bizCustomerService;

    @RequiresPermissions("fmis:cpayment:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }

    /**
     * 查询付款申请列表
     */
    @RequiresPermissions("fmis:cpayment:list")
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
            bizProcessData.setRoleType(userFlowStatus);
        }
        startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefCPayment(bizProcessData);

        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                String flowStatus = data.getFlowStatus();
                data.setLoginUserId(ShiroUtils.getUserId().toString());
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
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildList(queryBizProcessChild);
        String productNames = "";
        String productIds = "";
        if (!CollectionUtils.isEmpty(bizProcessChildList)) {
            for (BizProcessChild bizProcessChild : bizProcessChildList) {
                String productId = bizProcessChild.getString1();
                BizProcessData bizProduct = bizProcessDataService.selectBizProcessDataById(Long.parseLong(productId));
                if (bizProduct != null) {
                    productNames += bizProcessChild.getString2() + ",";
                    productIds += bizProduct.getDataId() + ",";
                    bizProcessChild.setBizProcessData(bizProduct);
                }
            }
            bizProcessData.setBizProcessChildList(bizProcessChildList);
        }
        String customerId = bizProcessData.getString2();
        if (StringUtils.isNotEmpty(customerId)) {
            BizCustomer customer = bizCustomerService.selectBizCustomerById(Long.parseLong(customerId));
            bizProcessData.setBizCustomer(customer);
            bizProcessData.setCustomerName(customer.getName());
        }

        mmap.put("contractNames", productNames);
        mmap.put("contractIds", productIds);

        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/viewDetail";
    }

    @GetMapping("/examineEdit")
    public String examineEdit(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));

        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildList(queryBizProcessChild);
        String productNames = "";
        String productIds = "";
        if (!CollectionUtils.isEmpty(bizProcessChildList)) {
            for (BizProcessChild bizProcessChild : bizProcessChildList) {
                String productId = bizProcessChild.getString1();
                BizProcessData bizProduct = bizProcessDataService.selectBizProcessDataById(Long.parseLong(productId));
                productNames += bizProcessChild.getString2() + ",";
                productIds += bizProduct.getDataId() + ",";
                bizProcessChild.setBizProcessData(bizProduct);
            }
            bizProcessData.setBizProcessChildList(bizProcessChildList);
        }
        String customerId = bizProcessData.getString2();
        if (StringUtils.isNotEmpty(customerId)) {
            BizCustomer customer = bizCustomerService.selectBizCustomerById(Long.parseLong(customerId));
            bizProcessData.setBizCustomer(customer);
            bizProcessData.setCustomerName(customer.getName());
        }
        mmap.put("contractNames", productNames);
        mmap.put("contractIds", productIds);
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/examineEdit";
    }

    @PostMapping("/doExamine")
    @ResponseBody
    public AjaxResult doExamine(BizProcessData bizProcessData) {
        String examineStatus = bizProcessData.getExamineStatus();
        String examineRemark = bizProcessData.getExamineRemark();
        String dataId = bizProcessData.getDataId().toString();
        return toAjax(bizProcessDataService.doExamine(dataId, examineStatus, examineRemark, bizProcessData.getBizId()));
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
     * 导出付款申请列表
     */
    @RequiresPermissions("fmis:cpayment:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProcessData bizProcessData) {
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataList(bizProcessData);
        ExcelUtil<BizProcessData> util = new ExcelUtil<BizProcessData>(BizProcessData.class);
        return util.exportExcel(list, "data");
    }

    /**
     * 新增付款申请
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存付款申请
     */
    @RequiresPermissions("fmis:cpayment:add")
    @Log(title = "付款申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProcessData bizProcessData) {
        bizProcessData.setFlowStatus("-2");

        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizProcessData.getBizId());

        if (!CollectionUtils.isEmpty(flowAllMap)) {
            for (String key : flowAllMap.keySet()) {
                bizProcessData.setNormalFlag(key);
            }
        }
        String productArrayStr = bizProcessData.getProductParmters();
        if (StringUtils.isEmpty(productArrayStr)) {
            return error("付款申请详情为空，不能申请");
        }
        String payCompanyStr = "";
        JSONArray productArray = JSONArray.parseArray(productArrayStr);
        for (int i = 0; i < productArray.size(); i++) {
            JSONObject json = productArray.getJSONObject(i);
            // 结算单位，结算单位一直才能提交
            String string3 = json.getString("string3");
            if (StringUtils.isNotEmpty(string3)) {
                if ("" != payCompanyStr && !payCompanyStr.equals(string3)) {
                    return error("只能是同一个付款单位才能申请");
                }
                payCompanyStr = string3;
            }
        }
        bizProcessData.setString1(payCompanyStr);
        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);
        Long dataId = bizProcessData.getDataId();
        for (int i = 0; i < productArray.size(); i++) {
            JSONObject json = productArray.getJSONObject(i);
            BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
            if (StringUtils.isNotEmpty(bizProcessChild.getString1())) {
                bizProcessChild.setDataId(dataId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild);
            }
        }
        return toAjax(insertReturn);
    }

    /**
     * 修改付款申请
     */
    @GetMapping("/edit/{dataId}")
    public String edit(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(dataId);

        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildList(queryBizProcessChild);
        String productNames = "";
        String productIds = "";
        if (!CollectionUtils.isEmpty(bizProcessChildList)) {
            for (BizProcessChild bizProcessChild : bizProcessChildList) {
                productNames += bizProcessChild.getString2() + ",";
                productIds += bizProcessChild.getString1() + ",";
            }
            bizProcessData.setBizProcessChildList(bizProcessChildList);
        }
//        String customerId = bizProcessData.getString2();
//        if (StringUtils.isNotEmpty(customerId)) {
//            BizCustomer customer = bizCustomerService.selectBizCustomerById(Long.parseLong(customerId));
//            bizProcessData.setBizCustomer(customer);
//            bizProcessData.setCustomerName(customer.getName());
//        }

        mmap.put("contractNames", productNames);
        mmap.put("contractIds", productIds);

        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/edit";
    }

    /**
     * 修改保存付款申请
     */
    @RequiresPermissions("fmis:cpayment:edit")
    @Log(title = "付款申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProcessData bizProcessData) {
        String productArrayStr = bizProcessData.getProductParmters();
        int updateReturn = bizProcessDataService.updateBizProcessData(bizProcessData);

        Long dataId = bizProcessData.getDataId();

        BizProcessChild removeBizProcuessChild = new BizProcessChild();
        removeBizProcuessChild.setDataId(dataId);
        List<BizProcessChild> removeBizProcessChildList = bizProcessChildService.selectBizProcessChildList(removeBizProcuessChild);
        if (!CollectionUtils.isEmpty(removeBizProcessChildList)) {
            for (BizProcessChild bizProcessChild : removeBizProcessChildList) {
                bizProcessChildService.deleteBizProcessChildById(bizProcessChild.getChildId());
            }
        }

        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                if (StringUtils.isNotEmpty(bizProcessChild.getString1())) {
                    bizProcessChild.setDataId(dataId);
                    bizProcessChildService.insertBizProcessChild(bizProcessChild);
                }

            }
        }
        return toAjax(updateReturn);
    }

    /**
     * 删除付款申请
     */
    @RequiresPermissions("fmis:cpayment:remove")
    @Log(title = "合同管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        // 删除主表流程
        bizProcessDataService.deleteBizProcessDataByIds(ids);
        // 删除子表流程
        return toAjax(bizProcessChildService.deleteBizProcessChildByDataIds(ids));
    }

    @PostMapping("/report")
    @ResponseBody
    public AjaxResult report() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        return toAjax(bizProcessDataService.subReportBizQuotation(bizQuotation));
    }

    /**
     * 选择客户
     */
    @GetMapping("/selectCustomer")
    public String selectCustomer() {
        return prefix + "/selectCustomer";
    }

    /**
     * 选择合同
     */
    @GetMapping("/selectContract")
    public String selectContract(ModelMap mmap) {
        return prefix + "/selectContract";
    }
}
