package com.ruoyi.fmis.procurement.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.status.domain.BizDataStatus;
import com.ruoyi.fmis.status.service.IBizDataStatusService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
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
@RequestMapping("/fmis/procurement")
public class BizProcessDataProcurementController extends BaseController {
    private String prefix = "fmis/procurement";

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

    @Autowired
    private IBizDataStatusService bizDataStatusService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @RequiresPermissions("fmis:procurement:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }

    /**
     * 查询合同管理列表
     */
    @RequiresPermissions("fmis:procurement:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessData bizProcessData) {

        String bizId = bizProcessData.getBizId();

        startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefProcurement(bizProcessData);

        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
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

        String customerId = bizProcessData.getString4();
        if (StringUtils.isNotEmpty(customerId)) {
            bizProcessData.setBizCustomer(bizCustomerService.selectBizCustomerById(Long.parseLong(customerId)));
        }

        //查询已经选中的采购
        BizDataStatus queryBizDataStatus = new BizDataStatus();
        queryBizDataStatus.setString4(bizProcessData.getDataId().toString());
        List<BizDataStatus> bizDataStatuses = bizDataStatusService.selectBizDataStatusList(queryBizDataStatus);
        JSONArray numJsonValue = new JSONArray();
        if (!CollectionUtils.isEmpty(bizDataStatuses)) {
            for (BizDataStatus bizDataStatus : bizDataStatuses) {
                String type = bizDataStatus.getType();
                String childId = bizDataStatus.getChildId().toString();
                String num = bizDataStatus.getString1();
                String bizDataId = bizDataStatus.getString2();
                String parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id",k);
                jsonObject.put("num",num);
                numJsonValue.add(jsonObject);
            }
        }
        mmap.put("numJsonValue", numJsonValue);
        mmap.put("bizProcessData", bizProcessData);

        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers suppliers : suppliersList) {
            String supplierId = bizProcessData.getString6() == null ? "" : bizProcessData.getString6();
            if (supplierId.equals(suppliers.getSuppliersId().toString())) {
                suppliers.setFlag(true);
            }
        }
        mmap.put("suppliers",suppliersList);

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
    @RequiresPermissions("fmis:procurement:export")
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
    public String add(ModelMap mmap) {
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        return prefix + "/add";
    }

    /**
     * 新增保存合同管理
     */
    @RequiresPermissions("fmis:procurement:add")
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
                BizDataStatus bizDataStatus = JSONObject.parseObject(json.toJSONString(), BizDataStatus.class);
                bizDataStatus.setString4(bizProcessData.getDataId().toString());
                bizDataStatusService.insertBizDataStatus(bizDataStatus);
            }
        }
        setPurchasingStatus(bizProcessData,productArrayStr);
        setContractNo(bizProcessData,productArrayStr);
        return toAjax(insertReturn);
    }

    /**
     * 设置合同号
     * @param bizProcessData
     * @param productArrayStr
     */
    public void setContractNo (BizProcessData bizProcessData,String productArrayStr) {

        //设置采购状态  审批中
        bizProcessData.setString11("2");

        List<String> contractNoList = new ArrayList<>();
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizDataStatus bizDataStatus = JSONObject.parseObject(json.toJSONString(), BizDataStatus.class);
                String contractNo = bizDataStatus.getString3();
                if (StringUtils.isNotEmpty(contractNo)) {
                    if (!contractNoList.contains(contractNo)) {
                        contractNoList.add(contractNo);
                    }
                }
            }
        }
        String contractNos = "";
        if (!CollectionUtils.isEmpty(contractNoList)) {
            for (String noId : contractNoList) {
                BizProcessData bizProcessData1 = bizProcessDataService.selectBizProcessDataById(Long.parseLong(noId));
                if (bizProcessData1 != null) {
                    contractNos += bizProcessData1.getString1() + ",";
                }

            }
        }
        bizProcessData.setString10(contractNos);
        bizProcessDataService.updateBizProcessData(bizProcessData);
    }


    /**
     * 更新采购状态  采购状态string23 未采购=0 采购中=1 采购完成=2
     *
     * @param bizProcessData
     * @param productArrayStr
     */
    public void setPurchasingStatus (BizProcessData bizProcessData,String productArrayStr) {

        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);

        JSONArray productArray = JSONArray.parseArray(productArrayStr);

        //修改 合同的采购状态
        Map<String,List<BizDataStatus>> contractMap = new HashMap<>();
        for (int i = 0; i < productArray.size(); i++) {
            JSONObject json = productArray.getJSONObject(i);
            BizDataStatus bizDataStatus = JSONObject.parseObject(json.toJSONString(), BizDataStatus.class);
            /**
             * 查询 此合同下边的所有产品数据
             * 查询 质检这个合同下的所有产品数据
             * 对比数量
             */
            String contractId = bizDataStatus.getString3();
            if (!contractMap.containsKey(contractId)) {
                List<BizDataStatus> bizDataStatusList = new ArrayList<>();
                bizDataStatusList.add(bizDataStatus);
                contractMap.put(contractId,bizDataStatusList);
            } else {
                List<BizDataStatus> bizDataStatusList = contractMap.get(contractId);
                bizDataStatusList.add(bizDataStatus);
                contractMap.put(contractId,bizDataStatusList);
            }
        }
        //contractMap=质检这个合同下的所有产品数据
        for (String contractId : contractMap.keySet()) {
            BizProcessChild bizProcessChild = new BizProcessChild();
            bizProcessChild.setDataId(Long.parseLong(contractId));
            //合同下里面的所有数据
            List<BizProcessChild> contractChildList = bizProcessChildService.selectBizQuotationProductList(bizProcessChild);
            //已经质检的所有数据
            BizDataStatus bizDataStatus = new BizDataStatus();
            bizDataStatus.setString3(contractId);
            List<BizDataStatus> bizDataStatusList = bizDataStatusService.selectBizDataStatusList(bizDataStatus);
            String string23 = "1";

            int contractCount = contractChildList.size();

            for (BizProcessChild bizProcessChild1 : contractChildList) {
                String string5 = bizProcessChild1.getProductRef1Id();
                String string8 = bizProcessChild1.getProductRef2Id();
                String string11 = bizProcessChild1.getActuatorId();
                Long pattachmentId = bizProcessChild1.getPattachmentId();
                Long pattachment1Id = bizProcessChild1.getPattachment1Id();
                Long pattachment2Id = bizProcessChild1.getPattachment2Id();
                Long pattachment3Id = bizProcessChild1.getPattachment3Id();
                Long pattachment4Id = bizProcessChild1.getPattachment4Id();
                if (StringUtils.isNotEmpty(string5)) {
                    contractCount++;
                }
                if (StringUtils.isNotEmpty(string8)) {
                    contractCount++;
                }
                if (StringUtils.isNotEmpty(string11)) {
                    contractCount++;
                }
                if (pattachmentId != null && pattachmentId > 0L) {
                    contractCount++;
                }
                if (pattachment1Id != null && pattachment1Id > 0L) {
                    contractCount++;
                }
                if (pattachment2Id != null && pattachment2Id > 0L) {
                    contractCount++;
                }
                if (pattachment3Id != null && pattachment3Id > 0L) {
                    contractCount++;
                }
                if (pattachment4Id != null && pattachment4Id > 0L) {
                    contractCount++;
                }
            }


            if (contractCount == bizDataStatusList.size()) {
                string23 = "2";
            }
            BizProcessData updateBizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(contractId));
            updateBizProcessData.setString23(string23);
            bizProcessDataService.updateBizProcessData(updateBizProcessData);
        }
    }

    @GetMapping("/viewDetail")
    public String viewDetail(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));

        String customerId = bizProcessData.getString4();
        if (StringUtils.isNotEmpty(customerId)) {
            bizProcessData.setBizCustomer(bizCustomerService.selectBizCustomerById(Long.parseLong(customerId)));
        }

        //查询已经选中的采购
        BizDataStatus queryBizDataStatus = new BizDataStatus();
        queryBizDataStatus.setString4(bizProcessData.getDataId().toString());
        List<BizDataStatus> bizDataStatuses = bizDataStatusService.selectBizDataStatusList(queryBizDataStatus);
        JSONArray numJsonValue = new JSONArray();
        if (!CollectionUtils.isEmpty(bizDataStatuses)) {
            for (BizDataStatus bizDataStatus : bizDataStatuses) {
                String type = bizDataStatus.getType();
                String childId = bizDataStatus.getChildId().toString();
                String num = bizDataStatus.getString1();
                String bizDataId = bizDataStatus.getString2();
                String parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id",k);
                jsonObject.put("num",num);
                numJsonValue.add(jsonObject);
            }
        }
        mmap.put("numJsonValue", numJsonValue);
        mmap.put("bizProcessData", bizProcessData);

        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers suppliers : suppliersList) {
            String supplierId = bizProcessData.getString6() == null ? "" : bizProcessData.getString6();
            if (supplierId.equals(suppliers.getSuppliersId().toString())) {
                suppliers.setFlag(true);
            }
        }
        mmap.put("suppliers",suppliersList);

        return prefix + "/viewDetail";
    }

    @GetMapping("/startTest")
    public String startTest(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));

        String customerId = bizProcessData.getString4();
        if (StringUtils.isNotEmpty(customerId)) {
            bizProcessData.setBizCustomer(bizCustomerService.selectBizCustomerById(Long.parseLong(customerId)));
        }

        //查询已经选中的采购
        BizDataStatus queryBizDataStatus = new BizDataStatus();
        queryBizDataStatus.setString4(bizProcessData.getDataId().toString());
        List<BizDataStatus> bizDataStatuses = bizDataStatusService.selectBizDataStatusList(queryBizDataStatus);
        JSONArray numJsonValue = new JSONArray();
        if (!CollectionUtils.isEmpty(bizDataStatuses)) {
            for (BizDataStatus bizDataStatus : bizDataStatuses) {
                String type = bizDataStatus.getType();
                String childId = bizDataStatus.getChildId().toString();
                String num = bizDataStatus.getString1();
                String bizDataId = bizDataStatus.getString2();
                String parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id",k);
                jsonObject.put("num",num);
                numJsonValue.add(jsonObject);
            }
        }
        mmap.put("numJsonValue", numJsonValue);
        mmap.put("bizProcessData", bizProcessData);

        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers suppliers : suppliersList) {
            String supplierId = bizProcessData.getString6() == null ? "" : bizProcessData.getString6();
            if (supplierId.equals(suppliers.getSuppliersId().toString())) {
                suppliers.setFlag(true);
            }
        }
        mmap.put("suppliers",suppliersList);

        return prefix + "/startTest";
    }
    /**
     * 修改合同管理
     */
    @GetMapping("/edit/{dataId}")
    public String edit(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(dataId);

        String customerId = bizProcessData.getString4();
        if (StringUtils.isNotEmpty(customerId)) {
            bizProcessData.setBizCustomer(bizCustomerService.selectBizCustomerById(Long.parseLong(customerId)));
        }

        //查询已经选中的采购
        BizDataStatus queryBizDataStatus = new BizDataStatus();
        queryBizDataStatus.setString4(bizProcessData.getDataId().toString());
        List<BizDataStatus> bizDataStatuses = bizDataStatusService.selectBizDataStatusList(queryBizDataStatus);
        JSONArray numJsonValue = new JSONArray();
        if (!CollectionUtils.isEmpty(bizDataStatuses)) {
            for (BizDataStatus bizDataStatus : bizDataStatuses) {
                String type = bizDataStatus.getType();
                String childId = bizDataStatus.getChildId().toString();
                String num = bizDataStatus.getString1();
                String bizDataId = bizDataStatus.getString2();
                String parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id",k);
                jsonObject.put("num",num);
                numJsonValue.add(jsonObject);
            }
        }
        mmap.put("numJsonValue", numJsonValue);
        mmap.put("bizProcessData", bizProcessData);

        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers suppliers : suppliersList) {
            String supplierId = bizProcessData.getString6() == null ? "" : bizProcessData.getString6();
            if (supplierId.equals(suppliers.getSuppliersId().toString())) {
                suppliers.setFlag(true);
            }
        }
        mmap.put("suppliers",suppliersList);
        return prefix + "/edit";
    }

    /**
     * 修改保存合同管理
     */
    @RequiresPermissions("fmis:procurement:edit")
    @Log(title = "合同管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProcessData bizProcessData) {

        String productArrayStr = bizProcessData.getProductParmters();
        int updateReturn = bizProcessDataService.updateBizProcessData(bizProcessData);

        Long dataId = bizProcessData.getDataId();

        BizDataStatus queryBizDataStatus = new BizDataStatus();
        queryBizDataStatus.setString4(bizProcessData.getDataId().toString());
        List<BizDataStatus> bizDataStatuses = bizDataStatusService.selectBizDataStatusList(queryBizDataStatus);
        JSONArray numJsonValue = new JSONArray();
        if (!CollectionUtils.isEmpty(bizDataStatuses)) {
            for (BizDataStatus bizDataStatus : bizDataStatuses) {
                bizDataStatusService.deleteBizDataStatusById(bizDataStatus.getStatusId());
            }
        }

        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizDataStatus bizDataStatus = JSONObject.parseObject(json.toJSONString(), BizDataStatus.class);
                bizDataStatus.setString4(bizProcessData.getDataId().toString());
                bizDataStatusService.insertBizDataStatus(bizDataStatus);
            }
        }
        setPurchasingStatus(bizProcessData,productArrayStr);
        setContractNo(bizProcessData,productArrayStr);
        return toAjax(updateReturn);
    }

    /**
     * 删除合同管理
     */
    @RequiresPermissions("fmis:procurement:remove")
    @Log(title = "合同管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {

        String[] ids_ = ids.split(",");
        for (String id : ids_) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            BizDataStatus queryBizDataStatus = new BizDataStatus();
            queryBizDataStatus.setString4(id);
            List<BizDataStatus> bizDataStatuses = bizDataStatusService.selectBizDataStatusList(queryBizDataStatus);
            if (!CollectionUtils.isEmpty(bizDataStatuses)) {
                for (BizDataStatus bizDataStatus : bizDataStatuses) {
                    bizDataStatusService.deleteBizDataStatusById(bizDataStatus.getStatusId());
                }
            }
        }

        AjaxResult returnAjax = toAjax(bizProcessDataService.deleteBizProcessDataByIds(ids));
        return returnAjax;
    }

    @PostMapping("/report")
    @ResponseBody
    public AjaxResult report() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        return toAjax(bizProcessDataService.subReportBizQuotation(bizQuotation));
    }

    @PostMapping("/subTest")
    @ResponseBody
    public AjaxResult subTest() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        return toAjax(bizProcessDataService.subTestBizQuotation(bizQuotation));
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
