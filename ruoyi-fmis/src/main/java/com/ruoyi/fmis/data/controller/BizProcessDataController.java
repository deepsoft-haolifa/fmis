package com.ruoyi.fmis.data.controller;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.service.ISysRoleService;
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
 * 合同管理Controller
 *
 * @author frank
 * @date 2020-05-05
 */
@Controller
@RequestMapping("/fmis/data")
public class BizProcessDataController extends BaseController {
    private String prefix = "fmis/data";

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @Autowired
    private IBizDictService bizDictService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @Autowired
    private IBizProcessChildService bizProcessChildService;

    @Autowired
    private IBizProductService bizProductService;

    @Autowired
    private IBizCustomerService bizCustomerService;

    @RequiresPermissions("fmis:data:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }

    /**
     * 查询合同管理列表
     */
    @RequiresPermissions("fmis:data:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessData bizProcessData) {

        String bizId = bizProcessData.getBizId();



        startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRef(bizProcessData);

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
    @GetMapping("/examineEdit")
    public String examineEdit(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/examineEdit";
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
    @RequiresPermissions("fmis:data:export")
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
    @RequiresPermissions("fmis:data:add")
    @Log(title = "合同管理", businessType = BusinessType.INSERT)
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
        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);
        Long dataId = bizProcessData.getDataId();
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
        return toAjax(insertReturn);
    }

    /**
     * 修改合同管理
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
                String productId = bizProcessChild.getString1();
                BizProduct bizProduct = bizProductService.selectBizProductById(Long.parseLong(productId));
                productNames += bizProduct.getName() + ",";
                productIds += bizProduct.getProductId() + ",";
                bizProcessChild.setBizProduct(bizProduct);
            }
            bizProcessData.setBizProcessChildList(bizProcessChildList);
        }
        String customerId = bizProcessData.getString2();
        if (StringUtils.isNotEmpty(customerId)) {
            bizProcessData.setBizCustomer(bizCustomerService.selectBizCustomerById(Long.parseLong(customerId)));
        }

        mmap.put("productNames", productNames);
        mmap.put("productIds", productIds);
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/edit";
    }

    /**
     * 修改保存合同管理
     */
    @RequiresPermissions("fmis:data:edit")
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
    @RequiresPermissions("fmis:data:remove")
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
        return toAjax(bizProcessDataService.subReportBizQuotation(bizQuotation));
    }

    /**
     * 选择系统用户
     */
    @GetMapping("/selectProduct")
    public String selectProduct(ModelMap mmap) {

        mmap.put("seriesSelect",bizDictService.selectBizDictByProductType(BizConstants.productTypeCode));
        mmap.put("suppliers",bizSuppliersService.selectAllList());

        return prefix + "/selectProduct";
    }
    /**
     * 选择客户
     */
    @GetMapping("/selectCustomer")
    public String selectCustomer() {
        return prefix + "/selectCustomer";
    }
}
