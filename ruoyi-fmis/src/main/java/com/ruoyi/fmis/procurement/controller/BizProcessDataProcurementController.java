package com.ruoyi.fmis.procurement.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.itextpdf.PdfUtil;
import com.ruoyi.common.utils.itextpdf.TextWaterMarkPdfPageEvent;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.status.domain.BizDataStatus;
import com.ruoyi.fmis.status.service.IBizDataStatusService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.fmis.util.Util;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
    private ISysUserService sysUserService;
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDeptService sysDeptService;

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
     * 新增合同管理
     */
    @GetMapping("/addpool/{dataId}")
    public String addpool(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(dataId);
        mmap.put("bizProcessData", bizProcessData);
        String string1 = dictDataService.selectDictLabel("supplier_type", bizProcessData.getString3());
        SysDept sysDept =  sysDeptService.selectDeptById(bizProcessData.getString22() == null ? 1 : Long.parseLong(bizProcessData.getString22()));
        bizProcessData.setString1(string1);
        bizProcessData.setString2(sysDept.getDeptName());
        return prefix + "/add";
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
    @Transactional
    public AjaxResult addSave(BizProcessData bizProcessData) {
        bizProcessData.setFlowStatus("-2");
        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizProcessData.getBizId());
        if (!CollectionUtils.isEmpty(flowAllMap)) {
            for (String key : flowAllMap.keySet()) {
                bizProcessData.setNormalFlag(key);
            }
        }

        String productArrayStr = bizProcessData.getProductParmters();


        //string11 采购合同号
        String noStart = "PO-";
        noStart += DateUtils.dateTime1();
        Long no = bizProcessDataService.selectProcurementMaxNo();
        if (no == null) {
            no = 1L;
        }
        String orderNo = noStart + new DecimalFormat("000").format(no);
        bizProcessData.setString12(orderNo);
        try {
            Double price1 = bizProcessData.getPrice1();
            if (price1.isNaN()) {
                bizProcessData.setPrice1(0D);
            }
        } catch(Exception ex) {
            bizProcessData.setPrice1(0D);
            logger.info(ex.getMessage());
        }
        setNormalFlag(bizProcessData,productArrayStr);
        //添加采购合同
        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);
        Long dataId = bizProcessData.getDataId();
        List<String> dataIds = new ArrayList<>();
        //合并的是同一个合同还是不同合同的
        Map<String,Integer> dataIdCount = new HashMap<>();
        //此次合并数量
        int count = 0;
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizDataStatus bizDataStatus = JSONObject.parseObject(json.toJSONString(), BizDataStatus.class);
                dataIds.add(bizDataStatus.getString3());//销售id
                bizDataStatus.setString4(bizProcessData.getDataId().toString());//采购id
                bizDataStatusService.insertBizDataStatus(bizDataStatus);
                if (dataIdCount.containsKey(bizDataStatus.getString3())) {
                    int idCount =  dataIdCount.get(bizDataStatus.getString3()) + 1;
                    dataIdCount.put(bizDataStatus.getString3(), idCount);
                } else {
                    dataIdCount.put(bizDataStatus.getString3(), 1);
                }
            }
        }

        setPurchasingStatus(bizProcessData, productArrayStr);
        /**
         * 更新销售合同是否已经采购
         */
        for (String string : dataIds) {
            BizProcessData bizProcessData2 = bizProcessDataService.selectBizProcessDataById(Long.parseLong(string));
            BizProcessData bizProcessData1 = new BizProcessData();
            bizProcessData1.setBizId("procurement");
            bizProcessData1.setString3(bizProcessData2.getString1());
            List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListCg(bizProcessData1);
            setXSStatus(string, list.size(), dataIdCount.get(string));
        }

        setContractNo(bizProcessData,productArrayStr);
        return toAjax(insertReturn);
    }
    public void setXSStatus(String string, int size, int num) {
        //合并采购合同的处理listLevelS
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(string));
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(string));
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildList(queryBizProcessChild);
        int count = 0;
        for (BizProcessChild bizProcessChild : bizProcessChildList) {
            //产品
            if(!StringUtils.isEmpty(bizProcessChild.getString2()) && !bizProcessChild.getString2().equals("0")) {
                count ++;
            }
            //法兰
            if(!StringUtils.isEmpty(bizProcessChild.getString5()) && !bizProcessChild.getString5().equals("0")) {
                count ++;
            }
            //螺栓
            if(!StringUtils.isEmpty(bizProcessChild.getString8()) && !bizProcessChild.getString8().equals("0")) {
                count ++;
            }
            //执行器
            if(!StringUtils.isEmpty(bizProcessChild.getString11()) && !bizProcessChild.getString11().equals("0")) {
                count ++;
            }
            //附件1 2 3 4
            if(bizProcessChild.getPattachment1Id() != null) {
                count ++;
            }
            if(bizProcessChild.getPattachment2Id() != null) {
                count ++;
            }
            if(bizProcessChild.getPattachment3Id() != null) {
                count ++;
            }
            if(bizProcessChild.getPattachment4Id() != null) {
                count ++;
            }
        }
        if (size + num < count) {
            bizProcessData.setString30("1");
            bizProcessDataService.updateBizProcessData(bizProcessData);
        }
        if (size + num == count) {
            bizProcessData.setString30("2");
            bizProcessDataService.updateBizProcessData(bizProcessData);
        }


    }
    public String setNormalFlag (BizProcessData bizProcessData,String productArrayStr) {
        String normalFlag = "4";
        Double totalPrice = bizProcessData.getPrice1();
        if (totalPrice >= 300000) {
            normalFlag = "4";
        } else if (totalPrice > 100000) {
            normalFlag = "3";
        } else if (totalPrice <= 100000) {
            normalFlag = "2";
        }
        bizProcessData.setNormalFlag(normalFlag);
        return normalFlag;
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
        bizProcessData.setString3(contractNos);
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
//        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);

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

            //计算质检里面合同数
            Map<String,BizDataStatus> bizDataStatusContractMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(bizDataStatusList)) {
                for (BizDataStatus bizDataStatus1 : bizDataStatusList) {
                    bizDataStatusContractMap.put(bizDataStatus1.getString4(),bizDataStatus1);
                }
            }

            int contractSize = contractChildList.size();
            int contractCount = 0;

            for (BizProcessChild bizProcessChild1 : contractChildList) {
                String string5 = bizProcessChild1.getProductRef1Id();
                String string8 = bizProcessChild1.getProductRef2Id();
                String string11 = bizProcessChild1.getActuatorId();
                Long pattachmentId = bizProcessChild1.getPattachmentId();
                Long pattachment1Id = bizProcessChild1.getPattachment1Id();
                Long pattachment2Id = bizProcessChild1.getPattachment2Id();
                Long pattachment3Id = bizProcessChild1.getPattachment3Id();
                Long pattachment4Id = bizProcessChild1.getPattachment4Id();
                String productId = bizProcessChild1.getProductId();
                if (StringUtils.isNotEmpty(productId)) {
                    contractCount++;
                }
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


            if (contractCount == bizDataStatusContractMap.size()) {
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
        setNormalFlag(bizProcessData,productArrayStr);
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

    @GetMapping("/viewPdf")
    public void viewPdf(HttpServletRequest request, HttpServletResponse response) {
        createPdf(request,response,null);
    }

    /**
     * 导出合同
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProcessData bizProcessData) {
        return createPdf(null,null,bizProcessData);
    }

    public AjaxResult createPdf (HttpServletRequest request, HttpServletResponse response, BizProcessData bizProcessDataParamter) {
        String id = "";
        if (bizProcessDataParamter == null) {
            id = request.getParameter("id");
        } else {
            id = bizProcessDataParamter.getDataId().toString();
        }


        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(id));
        //产品信息
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        //List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizQuotationProductList(queryBizProcessChild);

        try
        {
            String filename = PdfUtil.encodingFilename("合同");
            String filePath = PdfUtil.getAbsoluteFile(filename);
            // step 1 横向
            Document document = new Document(PageSize.A4_LANDSCAPE);
            // step 2
            //new FileOutputStream(filePath)
            PdfWriter writer = null;
            if (response != null) {
                writer = PdfWriter.getInstance(document, response.getOutputStream());
            } else {
                writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            }
            // 只读
            writer.setEncryption(null, null, PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);
            //设置字体样式
            //正常
            Font textFont = PdfUtil.getPdfChineseFont(6,Font.NORMAL);
            //加粗
            Font boldFont = PdfUtil.getPdfChineseFont(11,Font.BOLD);
            //二级标题
            Font titleFont = PdfUtil.getPdfChineseFont(15,Font.BOLD);
            String companyName = bizProcessData.getString1();
            Paragraph paragraph0 = new Paragraph("    " + companyName + "    如无问题，请尽快回传及付款，以免影响交货期！回传电话：010-67171220。", textFont);
            paragraph0.setAlignment(Paragraph.ALIGN_CENTER);

            Paragraph paragraph = new Paragraph("产品购销合同", titleFont);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            if (bizProcessDataParamter != null) {
                writer.setPageEvent(new TextWaterMarkPdfPageEvent("北京好利"));
            }
            //Paragraph paragraph1 = new Paragraph("报价单", textFont);
            //paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
            //空行
            Paragraph blankRow = new Paragraph(18f, " ");


            //添加副标题
            Font simheiMiddle = textFont;
            PdfPTable tbSubtitle = new PdfPTable(1);
            PdfPCell cellSubtitle = new PdfPCell();
            Paragraph pSubtitle = new Paragraph("Beijing HAOLIFA Valve Group Co.,LTD", simheiMiddle);
            pSubtitle.setAlignment(Paragraph.ALIGN_CENTER);
            cellSubtitle.addElement(pSubtitle);
            cellSubtitle.setPaddingBottom(5);
            cellSubtitle.setBorderWidthTop(0);
            cellSubtitle.setBorderWidthLeft(0);
            cellSubtitle.setBorderWidthRight(0);
            cellSubtitle.setBorderWidthBottom(2);
            cellSubtitle.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tbSubtitle.addCell(cellSubtitle);
            tbSubtitle.setHorizontalAlignment(Paragraph.ALIGN_CENTER);


            //总列数
            int totalColumn = 15;
            PdfPTable table = new PdfPTable(totalColumn);

            //table前间距
            table.setSpacingBefore(10f);
            // 设置各列列宽
            table.setTotalWidth(500);

            //锁定列宽
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);

            String customerId = bizProcessData.getString2();
            BizCustomer bizCustomer = new BizCustomer();
            if (StringUtils.isNotEmpty(customerId)) {
                //bizCustomer = bizCustomerService.selectBizCustomerById(Long.parseLong(customerId));
            }
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(bizProcessData.getCreateBy()));

            //sysUser 递归查询上级部门查找地址
            String remark = sysDeptService.getDeptRemarkForUserPdf(sysUser);
            String remark1 = "Add：北京市 东城区 广渠门内大街90号 新裕商务大厦506#  邮编：100062";
            String remark2 = "北京好利集团商务管理中心";
            String remark3 = "010-67110192";
            String remark4 = "010-67171220";
            //合同 收货人电话
            String remark5 = "";
            //合同 单位电话
            String remark6 = "";
            //合同 开户行
            String remark7 = "";

            //合同 开户账号
            String remark8 = "";
            //合同 开户税号
            String remark9 = "";
            //合同 投诉电话
            String remark10 = "";

            if (StringUtils.isNotEmpty(remark)) {
                String[] remarks = remark.split("###");
                if (remarks.length > 1) {remark1 = remarks[0];}
                if (remarks.length > 2) {remark2 = remarks[1];}
                if (remarks.length > 3) {remark3 = remarks[2];}
                if (remarks.length > 4) {remark4 = remarks[3];}
                if (remarks.length > 5) {remark5 = remarks[4];}
                if (remarks.length > 6) {remark6 = remarks[5];}
                if (remarks.length > 7) {remark7 = remarks[6];}
                if (remarks.length > 8) {remark8 = remarks[7];}
                if (remarks.length > 9) {remark9 = remarks[8];}
                if (remarks.length == 10) {remark10 = remarks[9];}
            }

            // 第一行
            table.addCell(PdfUtil.mergeCol("需方：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(companyName, 13,textFont));


//            table.addCell(PdfUtil.mergeCol("", 2,textFont));
//            table.addCell(PdfUtil.mergeCol("", 5,textFont));

            //第二行
            table.addCell(PdfUtil.mergeCol("供方：", 2,textFont));
            String string6 = bizProcessData.getString6();
            String string6Name = "";
            BizSuppliers bizSuppliers = null;
            if (StringUtils.isNotEmpty(string6)) {
                bizSuppliers = bizSuppliersService.selectBizSuppliersById(Long.parseLong(string6));
                if (bizSuppliers != null) {
                    string6Name = bizSuppliers.getName();
                }
            }
            table.addCell(PdfUtil.mergeCol(string6Name, 13,textFont));
//            table.addCell(PdfUtil.mergeCol("", 2,textFont));
//            table.addCell(PdfUtil.mergeCol("", 5,textFont));
            //第三行
            table.addCell(PdfUtil.mergeCol("采购合同编号：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString12(), 6,textFont));
            table.addCell(PdfUtil.mergeCol("内销合同编号：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString10(), 5,textFont));
            //第四行
            table.addCell(PdfUtil.mergeCol("签订日期：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(DateUtils.dateTime(bizProcessData.getCreateTime()), 2,textFont));
            table.addCell(PdfUtil.mergeCol("", totalColumn - 4,textFont));

            //第五行
            table.addCell(PdfUtil.mergeCol("为保障买卖双方的合法权益，根据《合同法》及有关法律规定，买卖双方经友好协商，一致同意按下列条款签订本合同。", totalColumn,textFont));

            table.addCell(PdfUtil.mergeCol("一、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("供货内容：", 14,textFont));


            //产品信息
            BizProcessChild queryProductChild = new BizProcessChild();
            queryProductChild.setDataId(bizProcessData.getDataId());
            //List<BizProcessChild> bizProductChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
            List<BizProcessChild> bizProductChildList = bizProcessChildService.selectBizTestProductList(queryBizProcessChild);

            //第七行 产品数据开始 bizQuotationProducts
            table.addCell(PdfUtil.mergeCol("序号", 1,textFont));
            table.addCell(PdfUtil.mergeCol("产品ID", 1,textFont));
            table.addCell(PdfUtil.mergeCol("名称", 1,textFont));
            table.addCell(PdfUtil.mergeCol("压力", 1,textFont));
            table.addCell(PdfUtil.mergeCol("规格", 1,textFont));
            table.addCell(PdfUtil.mergeCol("颜色", 1,textFont));
            table.addCell(PdfUtil.mergeCol("数量", 1,textFont));
            table.addCell(PdfUtil.mergeCol("单价", 1,textFont));
            table.addCell(PdfUtil.mergeCol("合计", 1,textFont));
            table.addCell(PdfUtil.mergeCol("材质", 3,textFont));
            table.addCell(PdfUtil.mergeCol("备注", 3,textFont));

            Double sumTotalNum = new Double(0);
            Double sumTotalPrice = new Double(0);
            Double sumTotalAmount = new Double(0);
            Double sumTotalNumRef1 = new Double(0);
            Double sumTotalNumRef2 = new Double(0);

            DecimalFormat data = new DecimalFormat("#");
            //优惠 string14
            Double string14D = new Double(0);


            int rowNum = 0;
            if (!CollectionUtils.isEmpty(bizProductChildList)) {
                for (int i = 0; i < bizProductChildList.size(); i++) {
                    rowNum = i + 1;
                    BizProcessChild bizProcessChild = bizProductChildList.get(i);
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getModel(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductName(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getNominalPressure(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getSpecifications(), 1,textFont));
                    //颜色
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getColor(), 1,textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductNum(), 1,textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductProcurementPrice().toString(), 1,textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((Integer.parseInt(bizProcessChild.getProductNum()) * bizProcessChild.getProductProcurementPrice()) + "", 1,textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol("阀体:" + bizProcessChild.getValvebodyMaterial() + ",阀芯:" + bizProcessChild.getValvebodyMaterial()
                            + ",密封材质:" + bizProcessChild.getSealingMaterial() + ",驱动形式:" + bizProcessChild.getDriveForm()
                            + ",连接方式:" + bizProcessChild.getConnectionType(), 3,textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRemark() == null ? "" : bizProcessChild.getRemark(), 3,textFont));
                    sumTotalNum = sumTotalNum + Integer.parseInt(bizProcessChild.getProductNum());
                    sumTotalAmount = sumTotalAmount + Integer.parseInt(bizProcessChild.getProductNum()) * bizProcessChild.getProductProcurementPrice();
                }
            }

            //List<BizProcessChild> actuatorList = bizProcessChildService.selectBizChildActuatorList(queryBizProcessChild);
            List<BizProcessChild> actuatorList = bizProcessChildService.selectBizTestActuatorList(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(actuatorList)) {
                for (int i = 0; i < actuatorList.size(); i++) {
                    BizProcessChild bizProcessChild = actuatorList.get(i);
                    rowNum = rowNum + i + 1;
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorString1(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorName(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getPressure(), 1,textFont));//压力
                    table.addCell(PdfUtil.mergeCol("", 1,textFont));//规格
                    //颜色
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getColor(), 1,textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorNum(), 1,textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorPrice().toString(), 1,textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((Integer.parseInt(bizProcessChild.getActuatorNum()) * bizProcessChild.getActuatorPrice()) + "", 1,textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getString5(), 3,textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRemark(), 3,textFont));
                    sumTotalNum = sumTotalNum + Integer.parseInt(bizProcessChild.getActuatorNum());
                    sumTotalAmount = sumTotalAmount + Integer.parseInt(bizProcessChild.getActuatorNum()) * bizProcessChild.getActuatorPrice();
                }
            }

            //List<BizProcessChild> ref1List = bizProcessChildService.selectBizChildRef1List(queryBizProcessChild);
            List<BizProcessChild> ref1List = bizProcessChildService.selectBizTestRef1List(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(ref1List)) {
                for (int i = 0; i < ref1List.size(); i++) {
                    BizProcessChild bizProcessChild = ref1List.get(i);
                    rowNum = rowNum + i + 1;
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getModel(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1Name(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol("", 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1Specifications(), 1,textFont));
                    //颜色
                    table.addCell(PdfUtil.mergeCol( "", 1,textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductRef1Num() + "", 1,textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1Price().toString(), 1,textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((bizProcessChild.getProductRef1Num() * bizProcessChild.getRef1Price()) + "", 1,textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1MaterialRequire(), 3,textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRemark() == null ? "" : bizProcessChild.getRemark(), 3,textFont));
                    sumTotalNum = sumTotalNum + bizProcessChild.getProductRef1Num();
                    sumTotalAmount = sumTotalAmount + bizProcessChild.getProductRef1Num() * bizProcessChild.getRef1Price();
                }
            }
            //List<BizProcessChild> ref2List = bizProcessChildService.selectBizChildRef2List(queryBizProcessChild);
            List<BizProcessChild> ref2List = bizProcessChildService.selectBizTestRef2List(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(ref2List)) {
                for (int i = 0; i < ref2List.size(); i++) {
                    BizProcessChild bizProcessChild = ref2List.get(i);
                    rowNum = rowNum + i + 1;
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getModel(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef2Name(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol("", 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1Specifications(), 1,textFont));
                    //颜色
                    table.addCell(PdfUtil.mergeCol("", 1,textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductRef2Num() + "", 1,textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef2Price().toString(), 1,textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((bizProcessChild.getProductRef2Num() * bizProcessChild.getRef2Price()) + "", 1,textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol("", 3,textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRemark() == null ? "" : bizProcessChild.getRemark(), 3,textFont));
                    sumTotalNum = sumTotalNum + bizProcessChild.getProductRef2Num();
                    sumTotalAmount = sumTotalAmount + bizProcessChild.getProductRef2Num() * bizProcessChild.getRef2Price();
                }
            }
            /*List<BizProcessChild> paList = bizProcessChildService.selectBizChildPAList(queryBizProcessChild);
            List<BizProcessChild> pa1List = bizProcessChildService.selectBizChildPA1List(queryBizProcessChild);
            List<BizProcessChild> pa2List = bizProcessChildService.selectBizChildPA2List(queryBizProcessChild);
            List<BizProcessChild> pa3List = bizProcessChildService.selectBizChildPA3List(queryBizProcessChild);
            List<BizProcessChild> pa4List = bizProcessChildService.selectBizChildPA4List(queryBizProcessChild);*/
            List<BizProcessChild> paList = bizProcessChildService.selectBizTestPAList(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(paList)) {
                for (int i = 0; i < paList.size(); i++) {
                    BizProcessChild bizProcessChild = paList.get(i);
                    rowNum = rowNum + i + 1;
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1,textFont));
                    table.addCell(PdfUtil.mergeCol("", 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getChineseName(), 1,textFont));
                    table.addCell(PdfUtil.mergeCol("", 1,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getChineseSpecifications(), 1,textFont));
                    //颜色
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getColor(), 1,textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getPattachmentCount() + "", 1,textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProcurementPrice().toString(), 1,textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((bizProcessChild.getPattachmentCount() * bizProcessChild.getProcurementPrice()) + "", 1,textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getMaterial(), 3,textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getContractSpecial() == null ? "" : bizProcessChild.getContractSpecial(), 3,textFont));
                    sumTotalNum = sumTotalNum + bizProcessChild.getPattachmentCount();
                    sumTotalAmount = sumTotalAmount + bizProcessChild.getPattachmentCount() * bizProcessChild.getProcurementPrice();
                }
            }


            //金额合计
            table.addCell(PdfUtil.mergeColRight("合计", 6,textFont));//4
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalNum), 1,textFont));//总数量
            table.addCell(PdfUtil.mergeCol("", 1,textFont));//单价
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalAmount), 1,textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 7,textFont));//备注

            table.addCell(PdfUtil.mergeColRight("以上价格均为含13%增值税价格", 5,textFont));
            table.addCell(PdfUtil.mergeCol(StringUtils.convert(sumTotalAmount), 3,textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 7,textFont));//备注

            //特殊要求
            table.addCell(PdfUtil.mergeCol("二、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("特殊要求：" + StringUtils.trim(bizProcessData.getString29()), 14,textFont));


            table.addCell(PdfUtil.mergeCol("三、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("产品执行标准；好利阀业有限公司生产标准，符合国家及行业标准；产品提供安装使用说明书，产品合格证；产品标识：好利标牌", 14,textFont));


            table.addCell(PdfUtil.mergeCol("四、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("产品验收标准：按国家标准验收。", 14,textFont));
            /*table.addCell(PdfUtil.mergeCol("", 1,textFont));
            //电汇结算，款到发货；货物采用纸箱包装，采用市内送货运输，运输费用卖方承担

            String payRemark = "";
            String string18 = StringUtils.trim(bizProcessData.getString18());
            String string7 = StringUtils.trim(bizProcessData.getString7());

            String paymentType = dictDataService.selectDictLabel("payment_type",string18);
            String transportType = dictDataService.selectDictLabel("transport_type",string7);

            if (StringUtils.isNotEmpty(paymentType)) {
                payRemark += paymentType;
            }

            if (StringUtils.isNotEmpty(transportType)) {
                payRemark = payRemark + "," + transportType;
            }

            table.addCell(PdfUtil.mergeColLeft("付款及运输：" + payRemark, 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            //合同签定后5个工作日发货（若未当日回传，发货期则从收到回传之日延后）

            Date dateTime2 = bizProcessData.getDatetime2();
            String dateTime2S = "";
            if (dateTime2 != null) {
                dateTime2S = DateConvert.formatDate(bizProcessData.getDatetime2());
            }
            table.addCell(PdfUtil.mergeColLeft("1、交货周期：" + dateTime2S, 14,textFont));

            //table.addCell(PdfUtil.mergeColLeft("1、交货周期：" + DateConvert.formatDate(bizProcessData.getDatetime2()), 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("2、收  货  人：" + StringUtils.trim(bizProcessData.getString11()), 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("3、交货地点：" + StringUtils.trim(bizProcessData.getString9()), 14,textFont));*/

            table.addCell(PdfUtil.mergeCol("五、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("质量保证按国家标准执行：质保期18个月（自出厂日算起）；质保期内如因产品本身质量问题，卖方予以免费更换。", 14,textFont));

            table.addCell(PdfUtil.mergeCol("六、", 1,textFont));
            table.addCell(PdfUtil.mergeCol("包装规范：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString27(), 6,textFont));
            table.addCell(PdfUtil.mergeCol("运输方式：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString9(), 5,textFont));

            table.addCell(PdfUtil.mergeCol("七、", 1,textFont));
            table.addCell(PdfUtil.mergeCol("发货日期：", 2,textFont));
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String date = "";
            if (bizProcessData.getDatetime3() != null) {
                date = sdf.format(bizProcessData.getDatetime3());
            }
            table.addCell(PdfUtil.mergeCol(date, 6,textFont));
            table.addCell(PdfUtil.mergeCol("收货信息：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString28(), 5,textFont));
            table.addCell(PdfUtil.mergeCol("八、", 1,textFont));
            table.addCell(PdfUtil.mergeCol("付款方式：", 2,textFont));
            SysDictData  dictData = new SysDictData();
            dictData.setDictType("contract_paytype");
            dictData.setDictValue(bizProcessData.getString20());
            List<SysDictData> list = dictDataService.selectDictDataList(dictData);
            SysDictData sysDictData = list.get(0);
            table.addCell(PdfUtil.mergeCol(sysDictData == null ? "" : sysDictData.getDictLabel(), 6,textFont));
            table.addCell(PdfUtil.mergeCol("运费承担：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString26(), 5,textFont));

            table.addCell(PdfUtil.mergeCol("九、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("违约责任：合同签订后，买卖双方严格执行双方所签订合同的条款，其中一方不履行或不完全履行合同者应承担相应的法律责任；解决合同纠纷方式：双方协商解决，解决不成由卖方所在北京仲裁委员会仲裁。", 14,textFont));

            table.addCell(PdfUtil.mergeCol("十、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("本合同一式贰份。双方各执一份，双方签字盖章后生效（传真件有效）。", 14,textFont));




            table.addCell(PdfUtil.mergeCol("", 1,textFont));
//            table.addCell(PdfUtil.mergeColLeft("需方：" , 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("需方：" + companyName, 7,textFont));

//            table.addCell(PdfUtil.mergeCol("供方", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft( "供方：" + string6Name, 7,textFont));
            //table.addCell(PdfUtil.mergeColLeft("单位地址："  + StringUtils.trim(bizCustomer.getCompanyAddress()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("委托代理人：", 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("委托代理人：" + bizSuppliers.getHumanCapitalMeasure(), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("电    话：" + Util.jsonObject.getJSONObject(companyName).getString("phone"), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("电    话：" + bizSuppliers.getTelphone(), 7,textFont));
            //table.addCell(PdfUtil.mergeColLeft("传    真：" + StringUtils.trim(bizCustomer.getFax()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("传    真：" + "", 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("传    真：" + bizSuppliers.getFax(), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("开户银行：" + Util.jsonObject.getJSONObject(companyName).getString("bank"), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("开户银行：" + bizSuppliers.getBank() == null ? "": bizSuppliers.getBank(), 7,textFont));
            //table.addCell(PdfUtil.mergeColLeft("开户银行：" + StringUtils.trim(bizCustomer.getString11()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("帐    号：" + Util.jsonObject.getJSONObject(companyName).getString("bankNo"), 7,textFont));
//            table.addCell(PdfUtil.mergeColLeft("帐    号：" + StringUtils.trim(remark8), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("帐    号：" +  bizSuppliers.getBankNo() == null ? "" : bizSuppliers.getBankNo(), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("税    号："  + Util.jsonObject.getJSONObject(companyName).getString("faxNo"), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("税    号：" + bizSuppliers.getTaxNumber() == null ? "":bizSuppliers.getTaxNumber(), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("地    址："  + Util.jsonObject.getJSONObject(companyName).getString("address"), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("地    址：" + bizSuppliers.getAddress() == null ? "":bizSuppliers.getAddress(), 7,textFont));

            Paragraph paragraphRemark1 = new Paragraph();
            Font remarkFont1 = PdfUtil.getPdfChineseFont(7, Font.NORMAL);
            paragraphRemark1.add(new Chunk("总经理销售及投诉电话：" + StringUtils.trim(remark10), remarkFont1));
            paragraphRemark1.setAlignment(Paragraph.ALIGN_RIGHT);

            /*paragraphRemark1.add(new Chunk(remark1, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark2, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark3, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark4, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.setAlignment(Paragraph.ALIGN_RIGHT);*/

            Paragraph datePar = new Paragraph("打印日期：" + sdf.format(new Date()), PdfUtil.getPdfChineseFont());
            datePar.setAlignment(Element.ALIGN_RIGHT);
            datePar.setSpacingBefore(20);



            document.open();
            //document.add(paragraph0);
            document.add(paragraph);
            //document.add(blankRow);
            //document.add(paragraph1);
            //document.add(blankRow);
            //document.add(tbSubtitle);

            // step 4 写入内容
            document.add(table);
            //document.add(paragraphRemark);
            document.add(paragraphRemark1);
            document.add(datePar);

            // step 5
            document.close();
            writer.close();
            return AjaxResult.success(filename);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("导出失败，请联系网站管理员！");
        }
    }


}
