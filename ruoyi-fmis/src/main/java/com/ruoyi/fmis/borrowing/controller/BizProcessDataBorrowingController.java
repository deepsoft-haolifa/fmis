package com.ruoyi.fmis.borrowing.controller;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.product.domain.BizProduct;
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
@RequestMapping("/fmis/borrowing")
public class BizProcessDataBorrowingController extends BaseController {
    private String prefix = "fmis/borrowing";

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;


    @Autowired
    private IBizProcessChildService bizProcessChildService;


    @Autowired
    private IBizProductService bizProductService;

    @Autowired
    private IBizCustomerService bizCustomerService;

    @RequiresPermissions("fmis:borrowing:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }

    /**
     * 查询合同管理列表
     */
    @RequiresPermissions("fmis:borrowing:list")
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
                if ("0".equals(flowStatus)) {
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
    @GetMapping("/examineEdit")
    public String examineEdit(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataBorrowingById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/examineEdit";
    }

    @GetMapping("/viewDetail")
    public String viewDetail(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataBorrowingById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/viewDetail";
    }

    @PostMapping("/doExamine")
    @ResponseBody
    public AjaxResult doExamine(BizProcessData bizProcessData) {
        String examineStatus = bizProcessData.getExamineStatus();
        String examineRemark = bizProcessData.getExamineRemark();
        String dataId = bizProcessData.getDataId().toString();
        return toAjax(bizProcessDataService.doExamine(dataId,examineStatus,examineRemark,bizProcessData.getBizId()));
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
     * 导出合同管理列表
     */
    @RequiresPermissions("fmis:borrowing:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProcessData bizProcessData) {
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataList(bizProcessData);
        ExcelUtil<BizProcessData> util = new ExcelUtil<BizProcessData>(BizProcessData.class);
        return util.exportExcel(list, "data");
    }

    /**
     * 新增合同管理
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存合同管理
     */
    @RequiresPermissions("fmis:borrowing:add")
    @Log(title = "合同管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProcessData bizProcessData) {
        bizProcessData.setFlowStatus("0");


        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizProcessData.getBizId());
        String lastRoleKey = "";
        for (String key : flowMap.keySet()) {
            lastRoleKey = key;
        }

        if (!"1".equals(lastRoleKey)) {
            bizProcessData.setFlowStatus(lastRoleKey + "0");
        }


        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizProcessData.getBizId());
        if (!CollectionUtils.isEmpty(flowAllMap)) {
            for (String key : flowAllMap.keySet()) {
                bizProcessData.setNormalFlag(key);
            }
        }
        bizProcessData.setString2("BP" + DateUtils.dateTimeNow() + RandomStringUtils.randomNumeric(3));
        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);

        return toAjax(insertReturn);
    }

    /**
     * 修改合同管理
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
    @RequiresPermissions("fmis:borrowing:edit")
    @Log(title = "合同管理", businessType = BusinessType.UPDATE)
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
     * 删除合同管理
     */
    @RequiresPermissions("fmis:borrowing:remove")
    @Log(title = "合同管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
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

    /**
     * 选择客户
     */
    @GetMapping("/selectCustomer")
    public String selectCustomer() {
        return prefix + "/selectCustomer";
    }

    /**
     * 选择系统用户
     */
    @GetMapping("/selectContract")
    public String selectContract(ModelMap mmap) {


        return prefix + "/selectContract";
    }
}
