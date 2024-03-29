package com.ruoyi.fmis.procurement.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.itextpdf.PdfUtil;
import com.ruoyi.common.utils.itextpdf.TextWaterMarkPdfPageEvent;
import com.ruoyi.common.utils.poi.ExcelProcessDataUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.Constant;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.datatrack.service.IBizProcessDataTrackService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.finance.domain.BizPayPlan;
import com.ruoyi.fmis.finance.service.IBizPayPlanService;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.itextpdf.PdfUtil.getAbsoluteFile;

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
    private IBizCustomerService bizCustomerService;

    @Autowired
    private IBizDataStatusService bizDataStatusService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;
    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private IBizPayPlanService bizPayPlanService;
    @Value("${pdf.ownerPassword}")
    private String ownerPassword;

    @RequiresPermissions("fmis:procurement:view")
    @GetMapping()
    public String data(ModelMap mmap) {
        String supplierId = getRequest().getParameter("supplierId");
        if (StringUtils.isNotEmpty(supplierId)) {
            mmap.put("supplierId", supplierId);
        }
        return prefix + "/data";
    }

    @RequiresPermissions("fmis:procurement:view")
    @GetMapping("kg")
    public String kg(ModelMap mmap) {
        String supplierId = getRequest().getParameter("supplierId");
        if (StringUtils.isNotEmpty(supplierId)) {
            mmap.put("supplierId", supplierId);
        }
        return prefix + "/kg";
    }

    /**
     * 查询合同管理列表
     */
    @RequiresPermissions("fmis:procurement:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessData bizProcessData) {

        String bizId = bizProcessData.getBizId();
        if (!StringUtils.isEmpty(bizProcessData.getSupplierName())) {
            BizSuppliers bizSuppliers = new BizSuppliers();
            // 供应商查询
            bizSuppliers.setName(bizProcessData.getSupplierName());
            List<BizSuppliers> suppliers = bizSuppliersService.selectBizSuppliersList(bizSuppliers);
            if (suppliers != null && suppliers.size() > 0) {
                Set<String> supplierIds = new HashSet<>();
                for (BizSuppliers supplier : suppliers) {
                    supplierIds.add(String.valueOf(supplier.getSuppliersId()));
                }
                bizProcessData.setSupplierIds(supplierIds);
            }
            // 判断是否有供应商数据，若没有直接返回
            if (CollectionUtils.isEmpty(bizProcessData.getSupplierIds())) {
                return getDataTable(new ArrayList<>());
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
        boolean flowMapIsEmpty = !CollectionUtils.isEmpty(flowMap);

        //计算流程描述
        for (BizProcessData data : list) {
            if (flowMapIsEmpty) {
                // 发起质检按钮的判断条件loginUserId
                data.setLoginUserId(String.valueOf(ShiroUtils.getUserId()));
                String flowStatus = data.getFlowStatus();
                //结束标识
                String normalFlag = data.getNormalFlag();
                String flowStatusRemark = "待上报";
                data.setLoginUserId(ShiroUtils.getUserId().toString());
//                if ("-2".equals(flowStatus)) {
//                    flowStatusRemark = "待上报";
//                } else

                if ("1".equals(flowStatus)) {
                    flowStatusRemark = "已上报";
                } else {
                    SysRole currentSysRole = CommonUtils.getLikeByMap(flowAllMap, flowStatus.replaceAll("-", ""));
                    if (currentSysRole == null) {
                        continue;
                    }
                    if (flowStatus.equals(normalFlag)) {
                        flowStatusRemark = currentSysRole.getRoleName() + "已完成";
                    } else if (flowStatus.startsWith("-2")) {
                        //不同意标识
                        flowStatusRemark = currentSysRole.getRoleName() + "不同意";
                    } else if (flowStatus.startsWith("-1")) {
                        //不同意标识
                        flowStatusRemark = currentSysRole.getRoleName() + "未上报";
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
            // 判断是否可以发起质检 1 可以 0 不可以
            try {
                if (canTest(data.getDataId())) {
                    data.setCanTest(1);
                }
            } catch (Exception e) {
                data.setCanTest(1);
                logger.error("dataId:{},calculate canTest error.", data.getDataId(), e);
            }
            // 补全数据：采购总数量，已付款金额，已入库数量（质检合格数）
            repairData(data);
        }
        return getDataTable(list);
    }

    /**
     * @param data
     */
    private void repairData(BizProcessData data) {
        // 获取采购总数量
        int purchaseNum = calculatePurchaseNum(data.getDataId());
        // 获取已付款金额
        String havePayTotal = calculatePayTotal(data.getString12());
        // 获取已入库数量
        int putStorageNum = calculateStorageNum(data.getString12());

        data.setPurchaseNum(purchaseNum);
        data.setPayTotal(havePayTotal);
        data.setPutStorageNum(putStorageNum);

    }

    /**
     * 已入库总数量：按照质检合格数统计
     *
     * @param purchaseNo
     * @return
     */
    private int calculateStorageNum(String purchaseNo) {
        BizProcessChild bizProcessChild = new BizProcessChild();
        bizProcessChild.setProcurementNo(purchaseNo);
        List<BizProcessChild> bizProcessChildren = bizProcessChildService.selectBizTestChildHistoryList(bizProcessChild);
        int count = bizProcessChildren.stream().map(BizProcessChild::getYesNum).mapToInt(Integer::parseInt).reduce(0, (a, b) -> a + b);
        return count;
    }

    /**
     * 已付款总额
     *
     * @param purchaseNo
     * @return
     */
    private String calculatePayTotal(String purchaseNo) {
        BizPayPlan bizPayPlan = new BizPayPlan();
        bizPayPlan.setStatus("1");// 1 已付款 0 未付款
        bizPayPlan.setContractNo(purchaseNo);
        List<BizPayPlan> bizPayPlans = bizPayPlanService.selectBizPayPlanList(bizPayPlan);
        Double count = bizPayPlans.stream().map(BizPayPlan::getApplyAmount).reduce(0.0, (a, b) -> a + b);
        return String.valueOf(count);
    }

    /**
     * 采购总数量：从采购状态池获取
     *
     * @param dataId
     * @return
     */
    private int calculatePurchaseNum(Long dataId) {
        BizDataStatus bizDataStatus = new BizDataStatus();
        bizDataStatus.setString4(String.valueOf(dataId));
        List<BizDataStatus> bizDataStatuses = bizDataStatusService.selectBizDataStatusList(bizDataStatus);
        int count = bizDataStatuses.stream().map(BizDataStatus::getString1).mapToInt(Integer::parseInt).reduce(0, (a, b) -> a + b);
        return count;
    }


    /**
     * @param dataId
     * @return
     */
    // 判断是否还需要发起质检
    private boolean canTest(Long dataId) {
        BizProcessChild bizProcessChild = new BizProcessChild();
        bizProcessChild.setDataId(dataId);
        List<BizProcessChild> productList = bizProcessChildService.selectBizTestProductList(bizProcessChild);
        for (BizProcessChild child : productList) {
            if (judgeCanTest(child.getYesNum(), child.getProductNum())) return true;
        }
        List<BizProcessChild> actuatorList = bizProcessChildService.selectBizTestActuatorList(bizProcessChild);
        for (BizProcessChild child : actuatorList) {
            if (judgeCanTest(child.getYesNum(), child.getActuatorNum())) return true;
        }
        List<BizProcessChild> pAList = bizProcessChildService.selectBizTestPAList(bizProcessChild);
        for (BizProcessChild child : pAList) {
            if (judgeCanTest(child.getYesNum(), String.valueOf(child.getPattachmentCount()))) return true;
        }
        List<BizProcessChild> ref1List = bizProcessChildService.selectBizTestRef1List(bizProcessChild);
        for (BizProcessChild child : ref1List) {
            if (judgeCanTest(child.getYesNum(), String.valueOf(child.getProductRef1Num()))) return true;
        }
        List<BizProcessChild> ref2List = bizProcessChildService.selectBizTestRef2List(bizProcessChild);
        for (BizProcessChild child : ref2List) {
            if (judgeCanTest(child.getYesNum(), String.valueOf(child.getProductRef2Num()))) return true;
        }
        return false;
    }

    /**
     * @param yesNum
     * @param countNum
     * @return
     */
    private boolean judgeCanTest(String yesNum, String countNum) {
        double yes = StringUtils.isNotEmpty(yesNum) ? Double.parseDouble(yesNum) : 0;
        double count = StringUtils.isNotEmpty(countNum) ? Double.parseDouble(countNum) : 0;
        if (yes < count) {
            return true;
        }
        return false;
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
                Integer parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id", k);
                jsonObject.put("num", num);
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
        mmap.put("suppliers", suppliersList);

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
     * 新增合同管理
     */
    @GetMapping("/addpool/{dataId}")
    public String addpool(@PathVariable("dataId") String dataId, ModelMap mmap) {
        mmap.put("suppliers", bizSuppliersService.selectAllList());
        String[] split = dataId.split(",");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.valueOf(split[0]));
        Long tempTime = 0l;
//        for (String id : split) {
//            BizProcessData tempBizProcessData = bizProcessDataService.selectBizProcessDataById(Long.valueOf(id));
//            //  比较销售合同交付日期:取日期最早的
//            String time = tempBizProcessData.getString6();
//            if (StringUtils.isNotEmpty(time)) {
//                Date date = DateUtils.dateTime("yyyy-MM-dd", time);
//                if (tempTime == 0) {
//                    tempTime = date.getTime();
//                    bizProcessData = tempBizProcessData;
//                } else if (tempTime > date.getTime()) {
//                    tempTime = date.getTime();
//                    bizProcessData = tempBizProcessData;
//                }
//            } else if (Objects.isNull(bizProcessData)) {
//                bizProcessData = tempBizProcessData;
//            }
//
//        }
        mmap.put("bizProcessData", bizProcessData);
        String string1 = dictDataService.selectDictLabel("supplier_type", bizProcessData.getString3());
        SysDept sysDept = sysDeptService.selectDeptById(bizProcessData.getString22() == null ? 1 : Long.parseLong(bizProcessData.getString22()));
        bizProcessData.setString1(string1);
        bizProcessData.setString2(sysDept.getDeptName());
        return prefix + "/add";
    }

    /**
     * 新增合同管理
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("suppliers", bizSuppliersService.selectAllList());
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
        bizProcessData.setFlowStatus("-1");
        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizProcessData.getBizId());
        if (bizProcessData.getPrice20() != null && !bizProcessData.getPrice20().equals("")) {
            bizProcessData.setPrice1(bizProcessData.getPrice1() - bizProcessData.getPrice20());
        }
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
        } catch (Exception ex) {
            bizProcessData.setPrice1(0D);
            logger.info(ex.getMessage());
        }
        bizProcessData.setStatus(Constant.procurementStatus.NEW);
        setNormalFlag(bizProcessData, productArrayStr);
        //添加采购合同
        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);
        Long dataId = bizProcessData.getDataId();
        List<String> dataIds = new ArrayList<>();
        //合并的是同一个合同还是不同合同的
        Map<String, Integer> dataIdCount = new HashMap<>();
        //此次合并数量
        int count = 0;
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizDataStatus bizDataStatus = JSONObject.parseObject(json.toJSONString(), BizDataStatus.class);
                /*if (bizDataStatus.getType().equals("1")) {
                    //产品
                    bizDataStatus.setProductProPrice(bizDataStatus.getCaigoujia());
                }
                if (bizDataStatus.getType().equals("2")) {
                    bizDataStatus.setActProPrice(bizDataStatus.getCaigoujia());

                }
                if (bizDataStatus.getType().equals("3")) {
                    bizDataStatus.setFalanProPrice(bizDataStatus.getCaigoujia());
                }
                if (bizDataStatus.getType().equals("4")) {
                    bizDataStatus.setLuoshuanProPrice(bizDataStatus.getCaigoujia());
                }
                if (bizDataStatus.getType().equals("5")) {
                    bizDataStatus.setpProPrice(bizDataStatus.getCaigoujia());
                }
                if (bizDataStatus.getType().equals("6")) {
                    bizDataStatus.setP1ProPrice(bizDataStatus.getCaigoujia());
                }
                if (bizDataStatus.getType().equals("7")) {
                    bizDataStatus.setP2ProPrice(bizDataStatus.getCaigoujia());
                }
                if (bizDataStatus.getType().equals("8")) {
                    bizDataStatus.setP3ProPrice(bizDataStatus.getCaigoujia());
                }
                if (bizDataStatus.getType().equals("9")) {
                    bizDataStatus.setP4ProPrice(bizDataStatus.getCaigoujia());
                }*/
                dataIds.add(bizDataStatus.getString3() + "");//销售id
                bizDataStatus.setString4(bizProcessData.getDataId().toString());//采购id
                bizDataStatusService.insertBizDataStatus(bizDataStatus);
                if (dataIdCount.containsKey(bizDataStatus.getString3())) {
                    int idCount = dataIdCount.get(bizDataStatus.getString3()) + 1;
                    dataIdCount.put(bizDataStatus.getString3() + "", idCount);
                } else {
                    dataIdCount.put(bizDataStatus.getString3() + "", 1);
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
            bizProcessData1.setBizId("contract");
            bizProcessData1.setString1(bizProcessData2.getString1());
            List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListCg(bizProcessData1);
            setXSStatus(string, list.size() - dataIdCount.get(string), dataIdCount.get(string));
        }

        setContractNo(bizProcessData, productArrayStr);
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
            if (!StringUtils.isEmpty(bizProcessChild.getString2()) && !bizProcessChild.getString2().equals("0")) {
                count++;
            }
            //法兰
            if (!StringUtils.isEmpty(bizProcessChild.getString5()) && !bizProcessChild.getString5().equals("0")) {
                count++;
            }
            //螺栓
            if (!StringUtils.isEmpty(bizProcessChild.getString8()) && !bizProcessChild.getString8().equals("0")) {
                count++;
            }
            //执行器
            if (!StringUtils.isEmpty(bizProcessChild.getString11()) && !bizProcessChild.getString11().equals("0")) {
                count++;
            }
            //定位器
            if (bizProcessChild.getPattachmentId() != null) {
                count++;
            }
            //附件1 2 3 4
            if (bizProcessChild.getPattachment1Id() != null) {
                count++;
            }
            if (bizProcessChild.getPattachment2Id() != null) {
                count++;
            }
            if (bizProcessChild.getPattachment3Id() != null) {
                count++;
            }
            if (bizProcessChild.getPattachment4Id() != null) {
                count++;
            }
        }
        bizProcessData.setStatus(Constant.contractStatus.PURCHASE_ING);
        if (size + num < count) {
            bizProcessData.setString30("1");
            bizProcessDataService.updateBizProcessData(bizProcessData);
        }
        if (size + num == count) {
            bizProcessData.setString30("2");
            bizProcessDataService.updateBizProcessData(bizProcessData);
        }


    }

    public String setNormalFlag(BizProcessData bizProcessData, String productArrayStr) {
        String normalFlag = "5";
        Double totalPrice = bizProcessData.getPrice1();
        if (totalPrice >= 300000) {
            normalFlag = "5";
        } else if (totalPrice > 100000) {
            normalFlag = "4";
        } else if (totalPrice <= 100000) {
            normalFlag = "3";
        }
        bizProcessData.setNormalFlag(normalFlag);
        return normalFlag;
    }

    /**
     * 设置合同号
     *
     * @param bizProcessData
     * @param productArrayStr
     */
    public void setContractNo(BizProcessData bizProcessData, String productArrayStr) {

        List<String> contractNoList = new ArrayList<>();
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizDataStatus bizDataStatus = JSONObject.parseObject(json.toJSONString(), BizDataStatus.class);
                Integer contractNo = bizDataStatus.getString3();
                if (StringUtils.isNotEmpty(contractNo + "")) {
                    if (!contractNoList.contains(contractNo)) {
                        contractNoList.add(contractNo + "");
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
            if (StringUtils.isNotEmpty(contractNos)) {
                contractNos = contractNos.substring(0, contractNos.length() - 1);
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
    public void setPurchasingStatus(BizProcessData bizProcessData, String productArrayStr) {

        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
//        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);

        JSONArray productArray = JSONArray.parseArray(productArrayStr);

        //修改 合同的采购状态
        Map<String, List<BizDataStatus>> contractMap = new HashMap<>();
        for (int i = 0; i < productArray.size(); i++) {
            JSONObject json = productArray.getJSONObject(i);
            BizDataStatus bizDataStatus = JSONObject.parseObject(json.toJSONString(), BizDataStatus.class);
            /**
             * 查询 此合同下边的所有产品数据
             * 查询 质检这个合同下的所有产品数据
             * 对比数量
             */
            Integer contractId = bizDataStatus.getString3();
            if (!contractMap.containsKey(contractId)) {
                List<BizDataStatus> bizDataStatusList = new ArrayList<>();
                bizDataStatusList.add(bizDataStatus);
                contractMap.put(contractId + "", bizDataStatusList);
            } else {
                List<BizDataStatus> bizDataStatusList = contractMap.get(contractId);
                bizDataStatusList.add(bizDataStatus);
                contractMap.put(contractId + "", bizDataStatusList);
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
            bizDataStatus.setString3(Integer.parseInt(contractId));
            List<BizDataStatus> bizDataStatusList = bizDataStatusService.selectBizDataStatusList(bizDataStatus);
            String string23 = "1";

            //计算质检里面合同数
            Map<String, BizDataStatus> bizDataStatusContractMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(bizDataStatusList)) {
                for (BizDataStatus bizDataStatus1 : bizDataStatusList) {
                    bizDataStatusContractMap.put(bizDataStatus1.getString4(), bizDataStatus1);
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
                Integer parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id", k);
                jsonObject.put("num", num);
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
        mmap.put("suppliers", suppliersList);

        return prefix + "/viewDetail";
    }

    @GetMapping("/kgviewDetail")
    public String kgviewDetail(ModelMap mmap) {
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
                Integer parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id", k);
                jsonObject.put("num", num);
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
        mmap.put("suppliers", suppliersList);

        return prefix + "/kgviewDetail";
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
                Integer parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id", k);
                jsonObject.put("num", num);
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
        mmap.put("suppliers", suppliersList);

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
                Integer parentContractId = bizDataStatus.getString3();
                String levelValue = bizDataStatus.getString5();
                JSONObject jsonObject = new JSONObject();
                String k = type + "_" + childId + "_" + bizDataId + "_" + parentContractId + "_" + levelValue;
                jsonObject.put("id", k);
                jsonObject.put("num", num);
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
        mmap.put("suppliers", suppliersList);
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
        setNormalFlag(bizProcessData, productArrayStr);
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
        setPurchasingStatus(bizProcessData, productArrayStr);
        setContractNo(bizProcessData, productArrayStr);
        return toAjax(updateReturn);
    }

    /**
     * 删除合同管理
     */
    @RequiresPermissions("fmis:procurement:remove")
    @Log(title = "合同管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
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
        bizQuotation.setStatus(Constant.procurementStatus.AUDIT);
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
        createPdf(request, response, null);
    }

    /**
     * 导出合同
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProcessData bizProcessData) {
        return createPdf(null, null, bizProcessData);
    }

    public AjaxResult createPdf(HttpServletRequest request, HttpServletResponse response, BizProcessData bizProcessDataParamter) {
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

        try {
//            bizProcessData
            String filename = bizProcessData.getString12() + "_" + System.currentTimeMillis() + ".pdf";
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
            writer.setEncryption(null, ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);
            //设置字体样式
            //正常
            Font textFont = PdfUtil.getPdfChineseFont(10, Font.NORMAL);
            //加粗
            Font boldFont = PdfUtil.getPdfChineseFont(11, Font.BOLD);
            //二级标题
            Font titleFont = PdfUtil.getPdfChineseFont(15, Font.BOLD);
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
                if (remarks.length > 1) {
                    remark1 = remarks[0];
                }
                if (remarks.length > 2) {
                    remark2 = remarks[1];
                }
                if (remarks.length > 3) {
                    remark3 = remarks[2];
                }
                if (remarks.length > 4) {
                    remark4 = remarks[3];
                }
                if (remarks.length > 5) {
                    remark5 = remarks[4];
                }
                if (remarks.length > 6) {
                    remark6 = remarks[5];
                }
                if (remarks.length > 7) {
                    remark7 = remarks[6];
                }
                if (remarks.length > 8) {
                    remark8 = remarks[7];
                }
                if (remarks.length > 9) {
                    remark9 = remarks[8];
                }
                if (remarks.length == 10) {
                    remark10 = remarks[9];
                }
            }

            if (companyName.equals("北京好利阀业集团有限公司")) {
                remark10 = "010-89218833";
            }

            // 第一行
            table.addCell(PdfUtil.mergeCol("合同编号：", 2, textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString12(), 13, textFont));

            // 第一行
            table.addCell(PdfUtil.mergeCol("甲方：", 2, textFont));
            table.addCell(PdfUtil.mergeCol(companyName, 5, textFont));
            table.addCell(PdfUtil.mergeCol("签订地点：", 2, textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString24(), 6, textFont));

//            table.addCell(PdfUtil.mergeCol("", 2,textFont));
//            table.addCell(PdfUtil.mergeCol("", 5,textFont));

            //第二行
            table.addCell(PdfUtil.mergeCol("乙方：", 2, textFont));
            String string6 = bizProcessData.getString6();
            String string6Name = "";
            BizSuppliers bizSuppliers = null;
            if (StringUtils.isNotEmpty(string6)) {
                bizSuppliers = bizSuppliersService.selectBizSuppliersById(Long.parseLong(string6));
                if (bizSuppliers != null) {
                    string6Name = bizSuppliers.getName();
                }
            }
            table.addCell(PdfUtil.mergeCol(string6Name, 5, textFont));
            table.addCell(PdfUtil.mergeCol("签订日期：", 2, textFont));
            table.addCell(PdfUtil.mergeCol(DateUtils.dateTime(bizProcessData.getCreateTime()), 6, textFont));
//            table.addCell(PdfUtil.mergeCol("", 2,textFont));
//            table.addCell(PdfUtil.mergeCol("", 5,textFont));
            //第三行
//            table.addCell(PdfUtil.mergeCol("采购合同编号", 2, textFont));
//            table.addCell(PdfUtil.mergeCol(bizProcessData.getString12(), 6, textFont));
//            table.addCell(PdfUtil.mergeCol("内销合同编号", 2, textFont));
//            table.addCell(PdfUtil.mergeCol(bizProcessData.getString10(), 5, textFont));
//            //第四行
//            table.addCell(PdfUtil.mergeCol("签订日期：", 2, textFont));
//            table.addCell(PdfUtil.mergeCol(DateUtils.dateTime(bizProcessData.getCreateTime()), 2, textFont));
//            table.addCell(PdfUtil.mergeCol("", totalColumn - 4, textFont));

            //第五行
            table.addCell(PdfUtil.mergeCol("为保障买卖双方的合法权益，根据《合同法》及有关法律规定，买卖双方经友好协商，一致同意按下列条款签订本合同。", totalColumn, textFont));

            table.addCell(PdfUtil.mergeCol("一、", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("供货内容：", 14, textFont));


            //产品信息
            BizProcessChild queryBizProcessChild22 = new BizProcessChild();
            queryBizProcessChild22.setProcurementId(String.valueOf(bizProcessData.getDataId()));
            queryBizProcessChild22.setBizEditFlag("2");
            queryBizProcessChild22.setString2("1");
            queryBizProcessChild22.setLevelValue("1");
            List<BizProcessChild> bizProductChildList = bizProcessChildService.selectExportBizChildProductList(queryBizProcessChild22);

            //第七行 产品数据开始 bizQuotationProducts
            table.addCell(PdfUtil.mergeCol("序号", 1, textFont));
            table.addCell(PdfUtil.mergeCol("产品ID", 1, textFont));
            table.addCell(PdfUtil.mergeCol("名称", 1, textFont));
            table.addCell(PdfUtil.mergeCol("系列", 1, textFont));
            table.addCell(PdfUtil.mergeCol("压力", 1, textFont));
            table.addCell(PdfUtil.mergeCol("规格", 1, textFont));
            table.addCell(PdfUtil.mergeCol("颜色", 1, textFont));
            table.addCell(PdfUtil.mergeCol("数量", 1, textFont));
            table.addCell(PdfUtil.mergeCol("单价", 1, textFont));
            table.addCell(PdfUtil.mergeCol("合计", 1, textFont));
            table.addCell(PdfUtil.mergeCol("材质", 3, textFont));
            table.addCell(PdfUtil.mergeCol("备注", 3, textFont));

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
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getModel(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductName(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getSeries(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getNominalPressure(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getSpecifications(), 1, textFont));
                    //颜色
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getColor(), 1, textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductNum(), 1, textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductProcurementPrice().toString(), 1, textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((Integer.parseInt(bizProcessChild.getProductNum()) * bizProcessChild.getProductProcurementPrice()) + "", 1, textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol("阀体:" + bizProcessChild.getValvebodyMaterial() + ",阀芯:" + bizProcessChild.getValveElement()
                            + ",密封材质:" + bizProcessChild.getSealingMaterial() + ",驱动形式:" + bizProcessChild.getDriveForm()
                            + ",连接方式:" + bizProcessChild.getConnectionType(), 3, textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRemark() == null ? "" : bizProcessChild.getRemark(), 3, textFont));
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
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorString1(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorName(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorString3(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getPressure(), 1, textFont));//压力
                    table.addCell(PdfUtil.mergeCol("", 1, textFont));//规格
                    //颜色
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getColor(), 1, textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorNum(), 1, textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getActuatorString6(), 1, textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((Integer.parseInt(bizProcessChild.getActuatorNum()) * Double.valueOf(bizProcessChild.getActuatorString6())) + "", 1, textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getString5(), 3, textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRemark(), 3, textFont));
                    sumTotalNum = sumTotalNum + Integer.parseInt(bizProcessChild.getActuatorNum());
                    sumTotalAmount = sumTotalAmount + Integer.parseInt(bizProcessChild.getActuatorNum()) * Double.valueOf(bizProcessChild.getActuatorString6());
                }
            }

            //List<BizProcessChild> ref1List = bizProcessChildService.selectBizChildRef1List(queryBizProcessChild);
            List<BizProcessChild> ref1List = bizProcessChildService.selectBizTestRef1List(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(ref1List)) {
                for (int i = 0; i < ref1List.size(); i++) {
                    BizProcessChild bizProcessChild = ref1List.get(i);
                    rowNum = rowNum + i + 1;
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getModel(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1Name(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol("", 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1Specifications(), 1, textFont));
                    //颜色
                    table.addCell(PdfUtil.mergeCol("", 1, textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductRef1Num() + "", 1, textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1Price().toString(), 1, textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((bizProcessChild.getProductRef1Num() * bizProcessChild.getRef1Price()) + "", 1, textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1MaterialRequire(), 3, textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRemark() == null ? "" : bizProcessChild.getRemark(), 3, textFont));
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
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getModel(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef2Name(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol("", 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef1Specifications(), 1, textFont));
                    //颜色
                    table.addCell(PdfUtil.mergeCol("", 1, textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductRef2Num() + "", 1, textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRef2Price().toString(), 1, textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((bizProcessChild.getProductRef2Num() * bizProcessChild.getRef2Price()) + "", 1, textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol("", 3, textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getRemark() == null ? "" : bizProcessChild.getRemark(), 3, textFont));
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
                    table.addCell(PdfUtil.mergeCol("" + rowNum, 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getProductId(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getChineseName(), 1, textFont));
                    table.addCell(PdfUtil.mergeCol("", 1, textFont));
                    table.addCell(PdfUtil.mergeCol("", 1, textFont));
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getChineseSpecifications(), 1, textFont));
                    //颜色
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getColor(), 1, textFont));
                    //数量
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getPattachmentCount() + "", 1, textFont));
                    //单价
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getCostPrice().toString(), 1, textFont));
                    //合计
                    table.addCell(PdfUtil.mergeCol((bizProcessChild.getPattachmentCount() * Double.parseDouble(bizProcessChild.getCostPrice())) + "", 1, textFont));
                    //材质
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getMaterial(), 3, textFont));
                    //备注
                    table.addCell(PdfUtil.mergeCol(bizProcessChild.getContractSpecial() == null ? "" : bizProcessChild.getContractSpecial(), 3, textFont));
                    sumTotalNum = sumTotalNum + bizProcessChild.getPattachmentCount();
                    sumTotalAmount = sumTotalAmount + bizProcessChild.getPattachmentCount() * bizProcessChild.getProcurementPrice();
                }
            }


            //金额合计
            table.addCell(PdfUtil.mergeColRight("合计", 7, textFont));//4
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalNum), 1, textFont));//总数量
            table.addCell(PdfUtil.mergeCol("", 1, textFont));//单价
            if (bizProcessData.getPrice20() != null && !bizProcessData.getPrice20().equals("") && !bizProcessData.getPrice20().equals("0") && !bizProcessData.getPrice20().equals("0.00")) {
                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalAmount - bizProcessData.getPrice20()), 1, textFont));//合计
            } else {
                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalAmount), 1, textFont));//合计
            }
            if (bizProcessData.getPrice20() != null && !bizProcessData.getPrice20().equals("") && !bizProcessData.getPrice20().equals("0") && !bizProcessData.getPrice20().equals("0.00")) {
                table.addCell(PdfUtil.mergeCol("优惠金额：" + bizProcessData.getPrice20(), 6
                        , textFont));//备注
            } else {
                table.addCell(PdfUtil.mergeCol("", 6
                        , textFont));//备注
            }


            table.addCell(PdfUtil.mergeColRight("以上价格均为含13%增值税价格", 5, textFont));
            table.addCell(PdfUtil.mergeCol(StringUtils.convert(sumTotalAmount), 3, textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 7, textFont));//备注

            //特殊要求
            table.addCell(PdfUtil.mergeCol("二、", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("特殊要求：" + StringUtils.trim(bizProcessData.getPurchaseSpecificRequests()), 14, textFont));


            table.addCell(PdfUtil.mergeCol("三、", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("产品执行标准；好利阀业有限公司生产标准，符合国家及行业标准；产品提供安装使用说明书，产品合格证；产品标识：好利标牌", 14, textFont));


            table.addCell(PdfUtil.mergeCol("四、", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("产品验收：按国家标准验收，甲方若有要求按甲方要求验收，验收合格以甲方签字为准；在验收期限内，需方对瑕疵产品有权无条件拒收，或要求供方换货，如更换之后的产品仍未达到验收标准的，需方有权要求退回。对于需方拒收或退回的产品，供方应在指定的期限内退还相应的货款。", 14, textFont));
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

            table.addCell(PdfUtil.mergeCol("五、", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("质量保证按国家标准执行：质保期18个月（自出厂日算起）；质保期内如因产品本身质量问题，卖方予以免费更换。", 14, textFont));
            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeCol("需方有权在质保期内对货物质量表示异议，供方在接到需方异议后应在3日内处理，否决即视为默认接收异议和处理意见，质保期内如出现产品本身质量问题，供方免费进行维修，不能维修予以更换，甲方若有因产品售后罚款及处理费用等损失的由乙方负责承担，供方未履行质量保证义务的，需方有权拒绝退还质保金，并要求供方另行支付合同总价款20%的违约金。", 14, textFont));


            table.addCell(PdfUtil.mergeCol("六、", 1, textFont));
            table.addCell(PdfUtil.mergeCol("安装与调试：电话指导", 14, textFont));

            table.addCell(PdfUtil.mergeCol("七、", 1, textFont));
            table.addCell(PdfUtil.mergeCol("包装方式：", 2, textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString27(), 5, textFont));
            table.addCell(PdfUtil.mergeCol("包装物回收：", 2, textFont));
            table.addCell(PdfUtil.mergeCol("不回收，由买方自行处理。", 5, textFont));

            table.addCell(PdfUtil.mergeCol("八、", 1, textFont));
            table.addCell(PdfUtil.mergeCol("交货周期：", 2, textFont));
            table.addCell(PdfUtil.mergeCol("合同生效日起   天发货，若有推迟发货超过5天，每天收取1%合同额违约金，并有权取消合同。", 12, textFont));

            table.addCell(PdfUtil.mergeCol("九", 1, textFont));
            String contractPaytype = dictDataService.selectDictLabel("contract_paytype", bizProcessData.getString20());
            table.addCell(PdfUtil.mergeCol("付款方式：", 2, textFont));
            table.addCell(PdfUtil.mergeCol(contractPaytype, 12, textFont));
//            table.addCell(PdfUtil.mergeCol("付款方式：电汇或承兑", 14, textFont));

            table.addCell(PdfUtil.mergeCol("十", 1, textFont));
            table.addCell(PdfUtil.mergeCol("运输：", 14, textFont));

           /* table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeCol("1、运输方式：", 2, textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString9() + " 联系人:" + bizProcessData.getString11() + " 电话:" + bizProcessData.getString12(), 3, textFont));
            table.addCell(PdfUtil.mergeCol("运费:", 2, textFont));
            table.addCell(PdfUtil.mergeCol(StringUtils.trim(bizProcessData.getString26()), 2, textFont));
            table.addCell(PdfUtil.mergeCol("是否需要送：", 2, textFont));
            table.addCell(PdfUtil.mergeCol("送货", 3, textFont));*/

            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeCol("1、运输方式：", 3, textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString9(), 2, textFont));
            table.addCell(PdfUtil.mergeCol("运费:", 2, textFont));
            table.addCell(PdfUtil.mergeCol(StringUtils.trim(bizProcessData.getString26()), 2, textFont));
            table.addCell(PdfUtil.mergeCol("是否需要送：", 3, textFont));
            table.addCell(PdfUtil.mergeCol("送货", 2, textFont));


            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeCol("2、收货地址信息：", 3, textFont));
            table.addCell(PdfUtil.mergeCol(StringUtils.trim(bizProcessData.getString28()), 12, textFont));
            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("3、产品运送至需方指定地点并由指定收货人签收后，视为供方完成交付义务，在供方完成交付义务之前，产品在运输途中毁损、灭失的风险均由供方承担，需方不承担任何责任。", 14, textFont));
            table.addCell(PdfUtil.mergeColLeft("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("4、其他约定事项：", 14, textFont));

            table.addCell(PdfUtil.mergeCol("十一", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("产品所有权自交接时起转移，但买方未按合同约定阶段付款，产品所有权仍属于出卖人所有。", 14, textFont));
//
//            table.addCell(PdfUtil.mergeCol("六、", 1, textFont));
//            table.addCell(PdfUtil.mergeCol("包装规范：", 2, textFont));
//            table.addCell(PdfUtil.mergeCol(bizProcessData.getString27(), 6, textFont));
//            table.addCell(PdfUtil.mergeCol("运输方式：", 2, textFont));
//            table.addCell(PdfUtil.mergeCol(bizProcessData.getString9(), 5, textFont));
//
//            table.addCell(PdfUtil.mergeCol("七、", 1, textFont));
//            table.addCell(PdfUtil.mergeCol("发货日期：", 2, textFont));
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
//            String date = "";
//            if (bizProcessData.getDatetime3() != null) {
//                date = sdf.format(bizProcessData.getDatetime3());
//            }
//            table.addCell(PdfUtil.mergeCol(date, 6, textFont));
//            table.addCell(PdfUtil.mergeCol("收货信息：", 2, textFont));
//            table.addCell(PdfUtil.mergeCol(bizProcessData.getString28(), 5, textFont));
//            table.addCell(PdfUtil.mergeCol("八、", 1, textFont));
//            table.addCell(PdfUtil.mergeCol("付款方式：", 2, textFont));
//            String contractPaytype = dictDataService.selectDictLabel("contract_paytype", bizProcessData.getString20());
//            table.addCell(PdfUtil.mergeCol(StringUtils.isNotEmpty(contractPaytype) ? contractPaytype : "", 6, textFont));
//            table.addCell(PdfUtil.mergeCol("运费承担：", 2, textFont));
//            table.addCell(PdfUtil.mergeCol(bizProcessData.getString26(), 5, textFont));
//
            table.addCell(PdfUtil.mergeCol("十二、", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("违约责任：供需双方在合同交往中供方应遵守商业道德廉洁从业，供方违反商业道德和廉洁从业尊则给与甲方人员回扣、礼金等即为违约，给需方造成损失的应双倍赔偿损失。合同签订后，买卖双方严格执行双方所签订合同的条款，其中一方不履行或不完全履行合同者应承担相应的法律责任；\n" +
                    "促裁委员会：因本合同产生的争议，双方应友好协商解决，协商不成的，任何一方均有权向需方所在地人民法院提起诉讼。", 14, textFont));

            table.addCell(PdfUtil.mergeCol("十三、", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("本合同一式贰份。双方各执一份，双方签字盖章后生效（传真件有效）。", 14, textFont));


            table.addCell(PdfUtil.mergeCol("", 1, textFont));
//            table.addCell(PdfUtil.mergeColLeft("需方：" , 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("单位名称：" + companyName, 7, textFont));

//            table.addCell(PdfUtil.mergeCol("供方", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("单位名称：" + string6Name, 7, textFont));
            //table.addCell(PdfUtil.mergeColLeft("单位地址："  + StringUtils.trim(bizCustomer.getCompanyAddress()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("委托代理人：", 7, textFont));
            table.addCell(PdfUtil.mergeColLeft("委托代理人：" + bizSuppliers.getHumanCapitalMeasure(), 7, textFont));

            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("电    话：" + Util.jsonObject.getJSONObject(companyName).getString("phone"), 7, textFont));
            table.addCell(PdfUtil.mergeColLeft("电    话：" + bizSuppliers.getTelphone(), 7, textFont));
            //table.addCell(PdfUtil.mergeColLeft("传    真：" + StringUtils.trim(bizCustomer.getFax()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("传    真：" + "", 7, textFont));
            table.addCell(PdfUtil.mergeColLeft("传    真：" + bizSuppliers.getFax(), 7, textFont));

            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("开户银行：" + Util.jsonObject.getJSONObject(companyName).getString("bank"), 7, textFont));
            table.addCell(PdfUtil.mergeColLeft("开户银行：" + bizSuppliers.getBank() == null ? "" : bizSuppliers.getBank(), 7, textFont));
            //table.addCell(PdfUtil.mergeColLeft("开户银行：" + StringUtils.trim(bizCustomer.getString11()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("帐    号：" + Util.jsonObject.getJSONObject(companyName).getString("bankNo"), 7, textFont));
//            table.addCell(PdfUtil.mergeColLeft("帐    号：" + StringUtils.trim(remark8), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("帐    号：" + bizSuppliers.getBankNo() == null ? "" : bizSuppliers.getBankNo(), 7, textFont));

            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("税    号：" + Util.jsonObject.getJSONObject(companyName).getString("faxNo"), 7, textFont));
            table.addCell(PdfUtil.mergeColLeft("税    号：" + bizSuppliers.getTaxNumber() == null ? "" : bizSuppliers.getTaxNumber(), 7, textFont));

            table.addCell(PdfUtil.mergeCol("", 1, textFont));
            table.addCell(PdfUtil.mergeColLeft("地    址：" + Util.jsonObject.getJSONObject(companyName).getString("address"), 7, textFont));
            table.addCell(PdfUtil.mergeColLeft("地    址：" + bizSuppliers.getAddress() == null ? "" : bizSuppliers.getAddress(), 7, textFont));

//            Paragraph paragraphRemark1 = new Paragraph();
//            Font remarkFont1 = PdfUtil.getPdfChineseFont(7, Font.NORMAL);
//            paragraphRemark1.add(new Chunk("总经理销售及投诉电话：" + StringUtils.trim(remark10), remarkFont1));
//            paragraphRemark1.setAlignment(Paragraph.ALIGN_RIGHT);

            /*paragraphRemark1.add(new Chunk(remark1, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark2, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark3, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark4, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.setAlignment(Paragraph.ALIGN_RIGHT);*/

//            Paragraph datePar = new Paragraph("打印日期：" + sdf.format(new Date()), PdfUtil.getPdfChineseFont());
//            datePar.setAlignment(Element.ALIGN_RIGHT);
//            datePar.setSpacingBefore(20);


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
//            document.add(paragraphRemark1);
//            document.add(datePar);

            // step 5
            document.close();
            writer.close();
            return AjaxResult.success(filename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("导出失败，请联系网站管理员！");
        }
    }

    /**
     * 导出验收单
     *
     * @param dataId
     * @return
     */
    @GetMapping("/export/receipt/{dataId}")
    public void exportReceipt(@PathVariable("dataId") Long dataId, HttpServletResponse response) throws IOException {

        BizProcessData bizProcessData1 = bizProcessDataService.selectBizProcessDataById(dataId);

        // 导出excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet receiptSheet = workbook.createSheet("验收单");

        // 第一行标题
        int rowIdx = 0;
        h_1(workbook, receiptSheet, rowIdx);

        // 第二行
        rowIdx++;
        h_2(bizProcessData1, workbook, receiptSheet, rowIdx);

        // 第三行 空白行
        rowIdx++;
        h_3(receiptSheet, rowIdx);

        // 第四行
        rowIdx++;
        XSSFCellStyle borderCellStyle = buildXssfCellStyle(workbook);
        h_4(receiptSheet, rowIdx, borderCellStyle);

        // 循环遍历产品
        // 查询报检列表
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(dataId);
        List<BizProcessChild> bizProcessChildrenProduct = bizProcessChildService.selectBizTestProductList(queryBizProcessChild);
        // 总的订货数量和实际到货数量
        int orderSize = 0;
        int arrivalSize = 0;
        if (!CollectionUtils.isEmpty(bizProcessChildrenProduct)) {
            for (int i = 0; i < bizProcessChildrenProduct.size(); i++) {
                BizProcessChild bizProcessChild = bizProcessChildrenProduct.get(i);
                rowIdx++;
                // 第 n 行
                h_n(receiptSheet, rowIdx, borderCellStyle, i, bizProcessChild);
                orderSize += Integer.parseInt(bizProcessChild.getProductNum());
                arrivalSize += Objects.isNull(bizProcessChild.getStayNum()) ? 0 : bizProcessChild.getStayNum();
            }
        }
        List<BizProcessChild> actuatorList = bizProcessChildService.selectBizTestActuatorList(queryBizProcessChild);
        if (!CollectionUtils.isEmpty(actuatorList)) {
            for (int i = 0; i < actuatorList.size(); i++) {

                BizProcessChild bizProcessChild = actuatorList.get(i);
                rowIdx++;
                // 材质要求：
                // 阀体
                String valvebodyMaterial = Objects.isNull(bizProcessChild.getValvebodyMaterial()) ? "无" : bizProcessChild.getValvebodyMaterial();
                // 阀芯
                String valveElement = Objects.isNull(bizProcessChild.getValveElement()) ? "无" : bizProcessChild.getValveElement();
                // 密封
                String valveMaterial = Objects.isNull(bizProcessChild.getValveMaterial()) ? "无" : bizProcessChild.getValveMaterial();
                // 驱动
                String driveForm = Objects.isNull(bizProcessChild.getDriveForm()) ? "无" : bizProcessChild.getDriveForm();
                // 连接方式
                String connectionType = Objects.isNull(bizProcessChild.getConnectionType()) ? "无" : bizProcessChild.getConnectionType();

                String format = String.format("阀体：%s;阀芯：%s；密封：%s；驱动：%s；连接方式：%s", valvebodyMaterial, valveElement, valveMaterial, driveForm, connectionType);
                XSSFRow rowList = receiptSheet.createRow(rowIdx);
                XSSFCell cellValue1 = rowList.createCell(0);// 序号
                cellValue1.setCellValue(i + 1);
                XSSFCell cellValue2 = rowList.createCell(1);// 产品名称
                cellValue2.setCellValue(bizProcessChild.getActuatorName());
                XSSFCell cellValue3 = rowList.createCell(2);// 型号
                cellValue3.setCellValue(bizProcessChild.getActuatorString1());
                XSSFCell cellValue4 = rowList.createCell(3);// 规格
                cellValue4.setCellValue("");
                XSSFCell cellValue5 = rowList.createCell(4);// 订货数
                cellValue5.setCellValue(bizProcessChild.getActuatorNum());
                XSSFCell cellValue6 = rowList.createCell(5);// 材质要求
                cellValue6.setCellValue(format);
                XSSFCell cellValue7 = rowList.createCell(6);// 内销合同号
                cellValue7.setCellValue(bizProcessChild.getContractNo());
                XSSFCell cellValue8 = rowList.createCell(7);// 实际到货数
                cellValue8.setCellValue(bizProcessChild.getStayNum());
                XSSFCell cellValue9 = rowList.createCell(8);// 合格数量
                cellValue9.setCellValue(Objects.isNull(bizProcessChild.getYesNum()) ? "" : bizProcessChild.getYesNum());
                XSSFCell cellValue10 = rowList.createCell(9);// 不合格数量
                cellValue10.setCellValue(Objects.isNull(bizProcessChild.getNoNum()) ? "" : bizProcessChild.getNoNum());
                XSSFCell cellValue11 = rowList.createCell(10);// 不合格原因
                cellValue11.setCellValue(Objects.isNull(bizProcessChild.getSnRemark()) ? "" : bizProcessChild.getSnRemark());

                XSSFCell cellValue12 = rowList.createCell(11);// 备注
                cellValue12.setCellValue(Objects.isNull(bizProcessChild.getRemark()) ? "" : bizProcessChild.getRemark());
// 设置单元格样式
                cellValue1.setCellStyle(borderCellStyle);
                cellValue2.setCellStyle(borderCellStyle);
                cellValue3.setCellStyle(borderCellStyle);
                cellValue4.setCellStyle(borderCellStyle);
                cellValue5.setCellStyle(borderCellStyle);
                cellValue6.setCellStyle(borderCellStyle);
                cellValue7.setCellStyle(borderCellStyle);
                cellValue8.setCellStyle(borderCellStyle);
                cellValue9.setCellStyle(borderCellStyle);
                cellValue10.setCellStyle(borderCellStyle);
                cellValue11.setCellStyle(borderCellStyle);
                cellValue12.setCellStyle(borderCellStyle);
                orderSize += Integer.parseInt(bizProcessChild.getActuatorNum());
                arrivalSize += bizProcessChild.getStayNum();
            }
        }

        List<BizProcessChild> ref1List = bizProcessChildService.selectBizTestRef1List(queryBizProcessChild);

        if (!CollectionUtils.isEmpty(ref1List)) {
            for (int i = 0; i < ref1List.size(); i++) {

                BizProcessChild bizProcessChild = ref1List.get(i);
                rowIdx++;
                // 材质要求：
                // 阀体
                String valvebodyMaterial = Objects.isNull(bizProcessChild.getValvebodyMaterial()) ? "无" : bizProcessChild.getValvebodyMaterial();
                // 阀芯
                String valveElement = Objects.isNull(bizProcessChild.getValveElement()) ? "无" : bizProcessChild.getValveElement();
                // 密封
                String valveMaterial = Objects.isNull(bizProcessChild.getValveMaterial()) ? "无" : bizProcessChild.getValveMaterial();
                // 驱动
                String driveForm = Objects.isNull(bizProcessChild.getDriveForm()) ? "无" : bizProcessChild.getDriveForm();
                // 连接方式
                String connectionType = Objects.isNull(bizProcessChild.getConnectionType()) ? "无" : bizProcessChild.getConnectionType();

                String format = String.format("阀体：%s;阀芯：%s；密封：%s；驱动：%s；连接方式：%s", valvebodyMaterial, valveElement, valveMaterial, driveForm, connectionType);
                XSSFRow rowList = receiptSheet.createRow(rowIdx);
                XSSFCell cellValue1 = rowList.createCell(0);// 序号
                cellValue1.setCellValue(i + 1);
                XSSFCell cellValue2 = rowList.createCell(1);// 产品名称
                cellValue2.setCellValue(bizProcessChild.getRef1Name());
                XSSFCell cellValue3 = rowList.createCell(2);// 型号
                cellValue3.setCellValue("");
                XSSFCell cellValue4 = rowList.createCell(3);// 规格
                cellValue4.setCellValue(bizProcessChild.getRef1Specifications());
                XSSFCell cellValue5 = rowList.createCell(4);// 订货数
                cellValue5.setCellValue(bizProcessChild.getProductRef1Num());
                XSSFCell cellValue6 = rowList.createCell(5);// 材质要求
                cellValue6.setCellValue(format);
                XSSFCell cellValue7 = rowList.createCell(6);// 内销合同号
                cellValue7.setCellValue(bizProcessChild.getContractNo());
                XSSFCell cellValue8 = rowList.createCell(7);// 实际到货数
                cellValue8.setCellValue(bizProcessChild.getStayNum());
                XSSFCell cellValue9 = rowList.createCell(8);// 合格数量
                cellValue9.setCellValue(Objects.isNull(bizProcessChild.getYesNum()) ? "" : bizProcessChild.getYesNum());
                XSSFCell cellValue10 = rowList.createCell(9);// 不合格数量
                cellValue10.setCellValue(Objects.isNull(bizProcessChild.getNoNum()) ? "" : bizProcessChild.getNoNum());
                XSSFCell cellValue11 = rowList.createCell(10);// 不合格原因
                cellValue11.setCellValue(Objects.isNull(bizProcessChild.getSnRemark()) ? "" : bizProcessChild.getSnRemark());

                XSSFCell cellValue12 = rowList.createCell(11);// 备注
                cellValue12.setCellValue(Objects.isNull(bizProcessChild.getRemark()) ? "" : bizProcessChild.getRemark());
// 设置单元格样式
                cellValue1.setCellStyle(borderCellStyle);
                cellValue2.setCellStyle(borderCellStyle);
                cellValue3.setCellStyle(borderCellStyle);
                cellValue4.setCellStyle(borderCellStyle);
                cellValue5.setCellStyle(borderCellStyle);
                cellValue6.setCellStyle(borderCellStyle);
                cellValue7.setCellStyle(borderCellStyle);
                cellValue8.setCellStyle(borderCellStyle);
                cellValue9.setCellStyle(borderCellStyle);
                cellValue10.setCellStyle(borderCellStyle);
                cellValue11.setCellStyle(borderCellStyle);
                cellValue12.setCellStyle(borderCellStyle);
                orderSize += bizProcessChild.getProductRef1Num().intValue();
                arrivalSize += bizProcessChild.getStayNum();
            }
        }

        List<BizProcessChild> ref2List = bizProcessChildService.selectBizTestRef2List(queryBizProcessChild);

        if (!CollectionUtils.isEmpty(ref2List)) {
            for (int i = 0; i < ref2List.size(); i++) {

                BizProcessChild bizProcessChild = ref2List.get(i);
                rowIdx++;
                // 材质要求：
                // 阀体
                String valvebodyMaterial = Objects.isNull(bizProcessChild.getValvebodyMaterial()) ? "无" : bizProcessChild.getValvebodyMaterial();
                // 阀芯
                String valveElement = Objects.isNull(bizProcessChild.getValveElement()) ? "无" : bizProcessChild.getValveElement();
                // 密封
                String valveMaterial = Objects.isNull(bizProcessChild.getValveMaterial()) ? "无" : bizProcessChild.getValveMaterial();
                // 驱动
                String driveForm = Objects.isNull(bizProcessChild.getDriveForm()) ? "无" : bizProcessChild.getDriveForm();
                // 连接方式
                String connectionType = Objects.isNull(bizProcessChild.getConnectionType()) ? "无" : bizProcessChild.getConnectionType();

                String format = String.format("阀体：%s;阀芯：%s；密封：%s；驱动：%s；连接方式：%s", valvebodyMaterial, valveElement, valveMaterial, driveForm, connectionType);
                XSSFRow rowList = receiptSheet.createRow(rowIdx);
                XSSFCell cellValue1 = rowList.createCell(0);// 序号
                cellValue1.setCellValue(i + 1);
                XSSFCell cellValue2 = rowList.createCell(1);// 产品名称
                cellValue2.setCellValue(bizProcessChild.getRef2Name());
                XSSFCell cellValue3 = rowList.createCell(2);// 型号
                cellValue3.setCellValue("");
                XSSFCell cellValue4 = rowList.createCell(3);// 规格
                cellValue4.setCellValue("");
                XSSFCell cellValue5 = rowList.createCell(4);// 订货数
                cellValue5.setCellValue(bizProcessChild.getProductRef2Num());
                XSSFCell cellValue6 = rowList.createCell(5);// 材质要求
                cellValue6.setCellValue(format);
                XSSFCell cellValue7 = rowList.createCell(6);// 内销合同号
                cellValue7.setCellValue(bizProcessChild.getContractNo());
                XSSFCell cellValue8 = rowList.createCell(7);// 实际到货数
                cellValue8.setCellValue(bizProcessChild.getStayNum());
                XSSFCell cellValue9 = rowList.createCell(8);// 合格数量
                cellValue9.setCellValue(Objects.isNull(bizProcessChild.getYesNum()) ? "" : bizProcessChild.getYesNum());
                XSSFCell cellValue10 = rowList.createCell(9);// 不合格数量
                cellValue10.setCellValue(Objects.isNull(bizProcessChild.getNoNum()) ? "" : bizProcessChild.getNoNum());
                XSSFCell cellValue11 = rowList.createCell(10);// 不合格原因
                cellValue11.setCellValue(Objects.isNull(bizProcessChild.getSnRemark()) ? "" : bizProcessChild.getSnRemark());

                XSSFCell cellValue12 = rowList.createCell(11);// 备注
                cellValue12.setCellValue(Objects.isNull(bizProcessChild.getRemark()) ? "" : bizProcessChild.getRemark());
// 设置
                cellValue1.setCellStyle(borderCellStyle);
                cellValue2.setCellStyle(borderCellStyle);
                cellValue3.setCellStyle(borderCellStyle);
                cellValue4.setCellStyle(borderCellStyle);
                cellValue5.setCellStyle(borderCellStyle);
                cellValue6.setCellStyle(borderCellStyle);
                cellValue7.setCellStyle(borderCellStyle);
                cellValue8.setCellStyle(borderCellStyle);
                cellValue9.setCellStyle(borderCellStyle);
                cellValue10.setCellStyle(borderCellStyle);
                cellValue11.setCellStyle(borderCellStyle);
                cellValue12.setCellStyle(borderCellStyle);
                orderSize += bizProcessChild.getProductRef2Num().intValue();
                arrivalSize += bizProcessChild.getStayNum();
            }
        }

        List<BizProcessChild> paList = bizProcessChildService.selectBizTestPAList(queryBizProcessChild);

        if (!CollectionUtils.isEmpty(paList)) {
            for (int i = 0; i < paList.size(); i++) {

                BizProcessChild bizProcessChild = paList.get(i);
                rowIdx++;
                // 材质要求：
                // 阀体
                String valvebodyMaterial = Objects.isNull(bizProcessChild.getValvebodyMaterial()) ? "无" : bizProcessChild.getValvebodyMaterial();
                // 阀芯
                String valveElement = Objects.isNull(bizProcessChild.getValveElement()) ? "无" : bizProcessChild.getValveElement();
                // 密封
                String valveMaterial = Objects.isNull(bizProcessChild.getValveMaterial()) ? "无" : bizProcessChild.getValveMaterial();
                // 驱动
                String driveForm = Objects.isNull(bizProcessChild.getDriveForm()) ? "无" : bizProcessChild.getDriveForm();
                // 连接方式
                String connectionType = Objects.isNull(bizProcessChild.getConnectionType()) ? "无" : bizProcessChild.getConnectionType();

                String format = String.format("阀体：%s;阀芯：%s；密封：%s；驱动：%s；连接方式：%s", valvebodyMaterial, valveElement, valveMaterial, driveForm, connectionType);
                XSSFRow rowList = receiptSheet.createRow(rowIdx);
                XSSFCell cellValue1 = rowList.createCell(0);// 序号
                cellValue1.setCellValue(i + 1);
                XSSFCell cellValue2 = rowList.createCell(1);// 产品名称
                cellValue2.setCellValue(bizProcessChild.getPattachmentName());
                XSSFCell cellValue3 = rowList.createCell(2);// 型号
                cellValue3.setCellValue("");
                XSSFCell cellValue4 = rowList.createCell(3);// 规格
                cellValue4.setCellValue("");
                XSSFCell cellValue5 = rowList.createCell(4);// 订货数
                cellValue5.setCellValue(bizProcessChild.getPattachmentCount());
                XSSFCell cellValue6 = rowList.createCell(5);// 材质要求
                cellValue6.setCellValue(format);
                XSSFCell cellValue7 = rowList.createCell(6);// 内销合同号
                cellValue7.setCellValue(bizProcessChild.getContractNo());
                XSSFCell cellValue8 = rowList.createCell(7);// 实际到货数
                cellValue8.setCellValue(bizProcessChild.getStayNum());
                XSSFCell cellValue9 = rowList.createCell(8);// 合格数量
                cellValue9.setCellValue(Objects.isNull(bizProcessChild.getYesNum()) ? "" : bizProcessChild.getYesNum());
                XSSFCell cellValue10 = rowList.createCell(9);// 不合格数量
                cellValue10.setCellValue(Objects.isNull(bizProcessChild.getNoNum()) ? "" : bizProcessChild.getNoNum());
                XSSFCell cellValue11 = rowList.createCell(10);// 不合格原因
                cellValue11.setCellValue(Objects.isNull(bizProcessChild.getSnRemark()) ? "" : bizProcessChild.getSnRemark());

                XSSFCell cellValue12 = rowList.createCell(11);// 备注
                cellValue12.setCellValue(Objects.isNull(bizProcessChild.getRemark()) ? "" : bizProcessChild.getRemark());
// 设置
                cellValue1.setCellStyle(borderCellStyle);
                cellValue2.setCellStyle(borderCellStyle);
                cellValue3.setCellStyle(borderCellStyle);
                cellValue4.setCellStyle(borderCellStyle);
                cellValue5.setCellStyle(borderCellStyle);
                cellValue6.setCellStyle(borderCellStyle);
                cellValue7.setCellStyle(borderCellStyle);
                cellValue8.setCellStyle(borderCellStyle);
                cellValue9.setCellStyle(borderCellStyle);
                cellValue10.setCellStyle(borderCellStyle);
                cellValue11.setCellStyle(borderCellStyle);
                cellValue12.setCellStyle(borderCellStyle);
                orderSize +=  bizProcessChild.getPattachmentCount().intValue(); //Integer.parseInt(bizProcessChild.getPattachmentCount() + "");
                arrivalSize += bizProcessChild.getStayNum();
            }
        }
        // todo

        // 合计栏

        rowIdx++;
        XSSFRow row4 = receiptSheet.createRow(rowIdx);
        CellRangeAddress region3 = new CellRangeAddress(rowIdx, rowIdx, 0, 3);
        receiptSheet.addMergedRegion(region3);
        XSSFCell cellCount = row4.createCell(0);
        XSSFCellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setWrapText(true);
        centerStyle.setBorderTop(BorderStyle.THIN);
        centerStyle.setBorderBottom(BorderStyle.THIN);
        centerStyle.setBorderLeft(BorderStyle.THIN);
        centerStyle.setBorderRight(BorderStyle.THIN);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        cellCount.setCellValue("合计");
        cellCount.setCellStyle(centerStyle);
        XSSFCell cell1 = row4.createCell(1);
        XSSFCell cell2 = row4.createCell(2);
        XSSFCell cell3 = row4.createCell(3);
        XSSFCell cell4 = row4.createCell(4);
        cell4.setCellValue(orderSize);
        XSSFCell cell5 = row4.createCell(5);
        XSSFCell cell6 = row4.createCell(6);
        XSSFCell cell7 = row4.createCell(7);
        cell7.setCellValue(arrivalSize);
        XSSFCell cell8 = row4.createCell(8);
        XSSFCell cell9 = row4.createCell(9);
        XSSFCell cell10 = row4.createCell(10);
        XSSFCell cell11 = row4.createCell(11);
        cell1.setCellStyle(borderCellStyle);
        cell2.setCellStyle(borderCellStyle);
        cell3.setCellStyle(borderCellStyle);
        cell4.setCellStyle(borderCellStyle);
        cell5.setCellStyle(borderCellStyle);
        cell6.setCellStyle(borderCellStyle);
        cell7.setCellStyle(borderCellStyle);
        cell8.setCellStyle(borderCellStyle);
        cell9.setCellStyle(borderCellStyle);
        cell10.setCellStyle(borderCellStyle);
        cell11.setCellStyle(borderCellStyle);

        // 备注栏
        //
        rowIdx++;
        XSSFRow row5 = receiptSheet.createRow(rowIdx);
        CellRangeAddress region4 = new CellRangeAddress(rowIdx, rowIdx, 1, 11);
        receiptSheet.addMergedRegion(region4);
        XSSFCell remark = row5.createCell(0);
        remark.setCellValue("备注");
        remark.setCellStyle(borderCellStyle);
        XSSFCell remarkValue = row5.createCell(1);
        remarkValue.setCellStyle(borderCellStyle);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region4, receiptSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region4, receiptSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region4, receiptSheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region4, receiptSheet);
        // 签字栏
        XSSFCellStyle topCellStyle = workbook.createCellStyle();
        topCellStyle.setBorderTop(BorderStyle.THIN);
        XSSFCellStyle rightCellStyle = workbook.createCellStyle();
        rightCellStyle.setBorderTop(BorderStyle.THIN);
        rightCellStyle.setBorderRight(BorderStyle.THIN);
        XSSFCellStyle leftCellStyle = workbook.createCellStyle();
        leftCellStyle.setBorderTop(BorderStyle.THIN);
        leftCellStyle.setBorderLeft(BorderStyle.THIN);
        rowIdx++;
        XSSFRow row6 = receiptSheet.createRow(rowIdx);
        CellRangeAddress region7 = new CellRangeAddress(rowIdx, rowIdx, 1, 2);
        receiptSheet.addMergedRegion(region7);
        CellRangeAddress region5 = new CellRangeAddress(rowIdx, rowIdx, 4, 5);
        receiptSheet.addMergedRegion(region5);
        CellRangeAddress region6 = new CellRangeAddress(rowIdx, rowIdx, 7, 11);
        receiptSheet.addMergedRegion(region6);
        RegionUtil.setBorderRight(BorderStyle.THIN, region7, receiptSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region7, receiptSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region5, receiptSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region5, receiptSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region6, receiptSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region6, receiptSheet);
        XSSFCell purchaseNameKey = row6.createCell(0);
        purchaseNameKey.setCellValue("采购：");
        purchaseNameKey.setCellStyle(leftCellStyle);
        XSSFCell purchaseNameValue = row6.createCell(1);
        purchaseNameValue.setCellValue("");
        purchaseNameValue.setCellStyle(topCellStyle);
        XSSFCell testResultKey = row6.createCell(3);
        testResultKey.setCellValue("检验结论：");
        testResultKey.setCellStyle(topCellStyle);
        XSSFCell testResultValue = row6.createCell(4);
        testResultValue.setCellValue("");
        testResultValue.setCellStyle(topCellStyle);
        XSSFCell factoryCheckKey = row6.createCell(6);
        factoryCheckKey.setCellValue("库房确认：");
        factoryCheckKey.setCellStyle(topCellStyle);
        XSSFCell factoryCheckValue = row6.createCell(11);
        factoryCheckValue.setCellValue("");
        factoryCheckValue.setCellStyle(rightCellStyle);


        rowIdx++;
        XSSFRow row7 = receiptSheet.createRow(rowIdx);
        XSSFCell cell90 = row7.createCell(0);
        XSSFCell cell101 = row7.createCell(1);
        XSSFCell cell121 = row7.createCell(2);
        XSSFCell cell12 = row7.createCell(3);
        XSSFCell cell13 = row7.createCell(4);
        XSSFCell cell14 = row7.createCell(5);
        XSSFCell cell15 = row7.createCell(6);
        XSSFCell cell16 = row7.createCell(7);
        XSSFCell cell17 = row7.createCell(8);
        XSSFCell cell181 = row7.createCell(9);
        XSSFCell cell191 = row7.createCell(10);
        XSSFCell cell201 = row7.createCell(11);
        XSSFCellStyle leftCellStyle2 = workbook.createCellStyle();
        leftCellStyle2.setBorderLeft(BorderStyle.THIN);
        cell90.setCellStyle(leftCellStyle2);
        XSSFCellStyle rightCellStyle2 = workbook.createCellStyle();
        rightCellStyle2.setBorderRight(BorderStyle.THIN);
        cell121.setCellStyle(rightCellStyle2);
        cell4.setCellStyle(rightCellStyle2);
        cell201.setCellStyle(rightCellStyle2);
        cell14.setCellStyle(rightCellStyle2);

        rowIdx++;
        XSSFCellStyle lastLeftStyle = workbook.createCellStyle();
        lastLeftStyle.setBorderLeft(BorderStyle.THIN);
        lastLeftStyle.setBorderBottom(BorderStyle.THIN);

        XSSFCellStyle lastRightStyle = workbook.createCellStyle();
        lastRightStyle.setBorderRight(BorderStyle.THIN);
        lastRightStyle.setBorderBottom(BorderStyle.THIN);

        XSSFCellStyle lastBottomStyle = workbook.createCellStyle();
        lastBottomStyle.setBorderBottom(BorderStyle.THIN);
        XSSFRow row8 = receiptSheet.createRow(rowIdx);

        XSSFCell cell18 = row8.createCell(0);
        cell18.setCellStyle(lastLeftStyle);
        XSSFCell cell_1 = row8.createCell(1);
        cell_1.setCellValue("经办人：");
        cell_1.setCellStyle(lastBottomStyle);
        XSSFCell cell = row8.createCell(2);
        cell.setCellStyle(lastRightStyle);
        XSSFCell cell19 = row8.createCell(3);
        cell19.setCellStyle(lastBottomStyle);
        XSSFCell cell_2 = row8.createCell(4);
        cell_2.setCellValue("质检员：");
        cell_2.setCellStyle(lastBottomStyle);
        XSSFCell cell20 = row8.createCell(5);
        cell20.setCellStyle(lastRightStyle);
        XSSFCell cell_3 = row8.createCell(6);
        cell_3.setCellValue("库管：");
        cell_3.setCellStyle(lastBottomStyle);
        XSSFCell cell_4 = row8.createCell(7);
        cell_4.setCellValue("经办人：");
        cell_4.setCellStyle(lastBottomStyle);
        XSSFCell cell21 = row8.createCell(8);
        cell21.setCellStyle(lastBottomStyle);
        XSSFCell cell21111 = row8.createCell(9);
        cell21111.setCellStyle(lastBottomStyle);
        XSSFCell cell2111 = row8.createCell(10);
        cell2111.setCellStyle(lastBottomStyle);
        XSSFCell cell211 = row8.createCell(11);
        cell211.setCellStyle(lastRightStyle);

        // 设置默认列宽
        receiptSheet.setDefaultColumnWidth(15);

        String filePath = Global.getFilePath();
        long l = System.currentTimeMillis();
        String fileName = "验收单_" + bizProcessData1.getString12() + "_" + l + ".xlsx";
//        FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + fileName);
//
//        workbook.write(fileOutputStream);
//        fileOutputStream.close();

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName));


        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    private void h_n(XSSFSheet receiptSheet, int rowIdx, XSSFCellStyle borderCellStyle, int i, BizProcessChild bizProcessChild) {
        // 材质要求：
        // 阀体
        String valvebodyMaterial = Objects.isNull(bizProcessChild.getValvebodyMaterial()) ? "无" : bizProcessChild.getValvebodyMaterial();
        // 阀芯
        String valveElement = Objects.isNull(bizProcessChild.getValveElement()) ? "无" : bizProcessChild.getValveElement();
        // 密封
        String valveMaterial = Objects.isNull(bizProcessChild.getValveMaterial()) ? "无" : bizProcessChild.getValveMaterial();
        // 驱动
        String driveForm = Objects.isNull(bizProcessChild.getDriveForm()) ? "无" : bizProcessChild.getDriveForm();
        // 连接方式
        String connectionType = Objects.isNull(bizProcessChild.getConnectionType()) ? "无" : bizProcessChild.getConnectionType();

        String format = String.format("阀体：%s;阀芯：%s；密封：%s；驱动：%s；连接方式：%s", valvebodyMaterial, valveElement, valveMaterial, driveForm, connectionType);
        XSSFRow rowList = receiptSheet.createRow(rowIdx);
        XSSFCell cellValue1 = rowList.createCell(0);// 序号
        cellValue1.setCellValue(i + 1);
        XSSFCell cellValue2 = rowList.createCell(1);// 产品名称
        cellValue2.setCellValue(bizProcessChild.getProductName());
        XSSFCell cellValue3 = rowList.createCell(2);// 型号
        cellValue3.setCellValue(bizProcessChild.getModel());
        XSSFCell cellValue4 = rowList.createCell(3);// 规格
        cellValue4.setCellValue(bizProcessChild.getSpecifications());
        XSSFCell cellValue5 = rowList.createCell(4);// 订货数
        cellValue5.setCellValue(bizProcessChild.getProductNum());
        XSSFCell cellValue6 = rowList.createCell(5);// 材质要求
        cellValue6.setCellValue(format);
        XSSFCell cellValue7 = rowList.createCell(6);// 内销合同号
        cellValue7.setCellValue(bizProcessChild.getContractNo());
        XSSFCell cellValue8 = rowList.createCell(7);// 实际到货数
        cellValue8.setCellValue(Objects.isNull(bizProcessChild.getStayNum()) ? 0 : bizProcessChild.getStayNum());
        XSSFCell cellValue9 = rowList.createCell(8);// 合格数量
        cellValue9.setCellValue(Objects.isNull(bizProcessChild.getYesNum()) ? "" : bizProcessChild.getYesNum());
        XSSFCell cellValue10 = rowList.createCell(9);// 不合格数量
        cellValue10.setCellValue(Objects.isNull(bizProcessChild.getNoNum()) ? "" : bizProcessChild.getNoNum());
        XSSFCell cellValue11 = rowList.createCell(10);// 不合格原因
        cellValue11.setCellValue(Objects.isNull(bizProcessChild.getSnRemark()) ? "" : bizProcessChild.getSnRemark());

        XSSFCell cellValue12 = rowList.createCell(11);// 备注
        cellValue12.setCellValue(Objects.isNull(bizProcessChild.getRemark()) ? "" : bizProcessChild.getRemark());
// 设置单元格样式
        cellValue1.setCellStyle(borderCellStyle);
        cellValue2.setCellStyle(borderCellStyle);
        cellValue3.setCellStyle(borderCellStyle);
        cellValue4.setCellStyle(borderCellStyle);
        cellValue5.setCellStyle(borderCellStyle);
        cellValue6.setCellStyle(borderCellStyle);
        cellValue7.setCellStyle(borderCellStyle);
        cellValue8.setCellStyle(borderCellStyle);
        cellValue9.setCellStyle(borderCellStyle);
        cellValue10.setCellStyle(borderCellStyle);
        cellValue11.setCellStyle(borderCellStyle);
        cellValue12.setCellStyle(borderCellStyle);
    }

    private XSSFCellStyle buildXssfCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle borderCellStyle = workbook.createCellStyle();
        borderCellStyle.setBorderTop(BorderStyle.THIN);
        borderCellStyle.setBorderBottom(BorderStyle.THIN);
        borderCellStyle.setBorderLeft(BorderStyle.THIN);
        borderCellStyle.setBorderRight(BorderStyle.THIN);
        borderCellStyle.setAlignment(HorizontalAlignment.LEFT);
        borderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        borderCellStyle.setWrapText(true);
        return borderCellStyle;
    }

    private void h_4(XSSFSheet receiptSheet, int rowIdx, XSSFCellStyle borderCellStyle) {
        XSSFRow row3 = receiptSheet.createRow(rowIdx);


        XSSFCell cellKey1 = row3.createCell(0);// 序号
        cellKey1.setCellValue("序号");
        XSSFCell cellKey2 = row3.createCell(1);// 产品名称
        cellKey2.setCellValue("产品名称");
        XSSFCell cellKey3 = row3.createCell(2);// 型号
        cellKey3.setCellValue("型号");
        XSSFCell cellKey4 = row3.createCell(3);// 规格
        cellKey4.setCellValue("规格");
        XSSFCell cellKey5 = row3.createCell(4);// 订货数
        cellKey5.setCellValue("订货数");
        XSSFCell cellKey6 = row3.createCell(5);// 材质要求
        cellKey6.setCellValue("材质要求");
        XSSFCell cellKey7 = row3.createCell(6);// 内销合同号
        cellKey7.setCellValue("内销合同号");
        XSSFCell cellKey8 = row3.createCell(7);// 实际到货数
        cellKey8.setCellValue("实际到货数");
        XSSFCell cellKey9 = row3.createCell(8);// 实际到货数
        cellKey9.setCellValue("合格数");
        XSSFCell cellKey10 = row3.createCell(9);// 实际到货数
        cellKey10.setCellValue("不合格数");
        XSSFCell cellKey11 = row3.createCell(10);// 实际到货数
        cellKey11.setCellValue("不合格原因");
        XSSFCell cellKey12 = row3.createCell(11);// 备注
        cellKey12.setCellValue("备注");

        // 设置单元格样式
        cellKey1.setCellStyle(borderCellStyle);
        cellKey2.setCellStyle(borderCellStyle);
        cellKey3.setCellStyle(borderCellStyle);
        cellKey4.setCellStyle(borderCellStyle);
        cellKey5.setCellStyle(borderCellStyle);
        cellKey6.setCellStyle(borderCellStyle);
        cellKey7.setCellStyle(borderCellStyle);
        cellKey8.setCellStyle(borderCellStyle);
        cellKey9.setCellStyle(borderCellStyle);
    }

    private void h_3(XSSFSheet receiptSheet, int rowIdx) {
        receiptSheet.createRow(rowIdx);
    }

    private void h_2(BizProcessData bizProcessData1, XSSFWorkbook workbook, XSSFSheet receiptSheet, int rowIdx) {
        // 查询供应商
        String string6 = bizProcessData1.getString6();

        BizSuppliers bizSuppliers = bizSuppliersService.selectBizSuppliersById(Long.valueOf(string6));

        // 发货日期，最后一个报检日期
        String string12 = bizProcessData1.getString12();
        BizProcessChild bizProcessChild_query = new BizProcessChild();
        bizProcessChild_query.setProcurementNo(string12);

        List<BizProcessChild> bizProcessChildren = bizProcessChildService.selectBizTestChildHistoryList(bizProcessChild_query);

        String dateTime = "";
        if (!CollectionUtils.isEmpty(bizProcessChildren)) {
            Date createTime = bizProcessChildren.get(0).getCreateTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateTime = simpleDateFormat.format(createTime);
        }

        // 加粗字体

        XSSFFont normalBold = workbook.createFont();
        normalBold.setFontName("宋体");
        normalBold.setBold(true);// 加粗
        normalBold.setFontHeightInPoints((short) 10);// 字体大小
        // 第二行
        XSSFRow row2 = receiptSheet.createRow(rowIdx);
        CellRangeAddress region = new CellRangeAddress(rowIdx, rowIdx, 1, 2);
        receiptSheet.addMergedRegion(region);
        CellRangeAddress region1 = new CellRangeAddress(rowIdx, rowIdx, 4, 5);
        receiptSheet.addMergedRegion(region1);
        CellRangeAddress region2 = new CellRangeAddress(rowIdx, rowIdx, 7, 11);
        receiptSheet.addMergedRegion(region2);

        // 加粗
        XSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(normalBold);
        boldStyle.setBorderBottom(BorderStyle.DOUBLE);
        boldStyle.setBorderTop(BorderStyle.DOUBLE);
        // 不加粗字体
        XSSFCellStyle thinStyle = workbook.createCellStyle();
        thinStyle.setBorderBottom(BorderStyle.DOUBLE);
        thinStyle.setBorderTop(BorderStyle.DOUBLE);
        XSSFCell supplierKeyCell = row2.createCell(0);
        supplierKeyCell.setCellValue("供方：");
        supplierKeyCell.setCellStyle(boldStyle);
        XSSFCell supplierValueCell = row2.createCell(1);
        supplierValueCell.setCellValue(bizSuppliers.getNickName());
        supplierValueCell.setCellStyle(thinStyle);
        RegionUtil.setBorderBottom(BorderStyle.DOUBLE, region, receiptSheet);
        RegionUtil.setBorderTop(BorderStyle.DOUBLE, region, receiptSheet);
        XSSFCell procurementKeyCell = row2.createCell(3);
        procurementKeyCell.setCellValue("采购合同号：");
        procurementKeyCell.setCellStyle(boldStyle);
        XSSFCell procurementValueCell = row2.createCell(4);
        procurementValueCell.setCellValue(bizProcessData1.getString12());
        procurementValueCell.setCellStyle(thinStyle);
        RegionUtil.setBorderTop(BorderStyle.DOUBLE, region1, receiptSheet);
        RegionUtil.setBorderBottom(BorderStyle.DOUBLE, region1, receiptSheet);
        XSSFCell deliveryDateKeyCell = row2.createCell(6);
        deliveryDateKeyCell.setCellValue("发货日期：");
        deliveryDateKeyCell.setCellStyle(boldStyle);
        XSSFCell deliveryDateValueCell = row2.createCell(7);
        deliveryDateValueCell.setCellValue(dateTime);
        RegionUtil.setBorderTop(BorderStyle.DOUBLE, region2, receiptSheet);
        RegionUtil.setBorderBottom(BorderStyle.DOUBLE, region2, receiptSheet);
    }

    private void h_1(XSSFWorkbook workbook, XSSFSheet receiptSheet, int rowIdx) {
        XSSFRow row = receiptSheet.createRow(rowIdx);
        receiptSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));
        XSSFCell title = row.createCell(0);

        XSSFFont headfont = workbook.createFont();
        headfont.setFontName("宋体");
        headfont.setBold(true);// 加粗
        headfont.setFontHeightInPoints((short) 20);// 字体大小
        XSSFCellStyle headstyle = workbook.createCellStyle();
        headstyle.setFont(headfont);
        headstyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        headstyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        headstyle.setLocked(true);
        title.setCellValue("验收入库单");
        title.setCellStyle(headstyle);
    }

    /**
     * 修改完成采购
     */
    @PostMapping("/caigouwancheng/{dataId}")
    @ResponseBody
    public AjaxResult caigouwancheng(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(dataId);

        bizProcessData.setString30("2");

        return toAjax(bizProcessDataService.updateBizProcessData(bizProcessData));
    }

    /**
     * 修改完成采购
     */
    @PostMapping("/jixuchuli/{dataId}")
    @ResponseBody
    public AjaxResult jixuchuli(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(dataId);

        bizProcessData.setString30("1");

        return toAjax(bizProcessDataService.updateBizProcessData(bizProcessData));
    }

    /**
     * 导出采购合同
     */
    @PostMapping("/exportPurchaseContract")
    @ResponseBody
    public AjaxResult exportPurchaseContract(BizProcessData bizProcessDataParamter) {
        try {
            String id = bizProcessDataParamter.getDataId().toString();
            BizProcessData bizProcess = bizProcessDataService.selectBizProcessDataById(Long.parseLong(id));
            String contractNoStr = "";
            for (String contractNo : bizProcess.getString10().split(",")) {
                BizProcessData bizProcessData = new BizProcessData();
                bizProcessData.setString1(contractNo);
                bizProcessData.setBizId("contract");
                bizProcessData.setQueryStatus("0");
                List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRef(bizProcessData);
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
                    String codeName = list.get(0).getCodeName();
                    if (StringUtils.isNotEmpty(codeName)) {
                        contractNoStr += contractNo + "-" + codeName + ",";
                    } else {
                        contractNoStr += contractNo + ",";
                    }
                }
            }
            if (contractNoStr.contains(",")) {
                contractNoStr = contractNoStr.substring(0, contractNoStr.length()-1);
            }

            Workbook workbook = new HSSFWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            Sheet sheet = workbook.createSheet("产品采购合同");
            // 单元格样式
            CellStyle cellLeft = ExcelProcessDataUtils.cellLeft(workbook);
            Row row1 = sheet.createRow(0);
            row1.setHeight((short) 600);

            CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 10);
            sheet.addMergedRegion(cra1);
            Cell cell_title_1 = row1.createCell(0);
            cell_title_1.setCellValue("产品采购合同");
            CellStyle cellTitle = ExcelProcessDataUtils.titleCell(workbook);
            cell_title_1.setCellStyle(cellTitle);

            Row row2 = sheet.createRow(1);
            row2.setHeight((short) 600);
            Cell cell_26 = row2.createCell(5);
            cell_26.setCellValue("合同编号：");
            cell_26.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 7));
            Cell cell_27 = row2.createCell(8);
            cell_27.setCellValue(bizProcess.getString12());
            cell_27.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 10));

            Row row3 = sheet.createRow(2);
            row3.setHeight((short) 600);

            Cell cell_30 = row3.createCell(0);
            cell_30.setCellValue("甲方（需方）：" + bizProcess.getString1());
            cell_30.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 4));
            Cell cell_34 = row3.createCell(5);
            cell_34.setCellValue("签订地点：");
            cell_34.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 7));
            Cell cell_35 = row3.createCell(8);
            cell_35.setCellValue(bizProcess.getString24());
            cell_35.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 8, 10));

            Row row4 = sheet.createRow(3);
            row4.setHeight((short) 600);

            String string6 = bizProcess.getString6();
            String string6Name = "";
            BizSuppliers bizSuppliers = null;
            if (StringUtils.isNotEmpty(string6)) {
                bizSuppliers = bizSuppliersService.selectBizSuppliersById(Long.parseLong(string6));
                if (bizSuppliers != null) {
                    string6Name = bizSuppliers.getName();
                }
            }
            Cell cell_40 = row4.createCell(0);
            cell_40.setCellValue("乙方（供方）：" + string6Name);
            cell_40.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 4));
            Cell cell_44 = row4.createCell(5);
            cell_44.setCellValue("签订日期：");
            cell_44.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 7));
            Cell cell_46 = row4.createCell(8);
            cell_46.setCellValue(DateUtils.dateTime(bizProcess.getCreateTime()));
            cell_46.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 8, 10));

            Row row5 = sheet.createRow(5);
            row5.setHeight((short) 600);
            Cell cell_50 = row5.createCell(0);
            row5.setRowStyle(cellLeft);
            cell_50.setCellValue("为保障买卖双方的合法权益，根据现行《民法典》及有关法律规定，经友好协商，一致同意按下列条款签订本合同。");
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 10));
            cell_50.setCellStyle(cellLeft);

            Row row6 = sheet.createRow(6);
            Cell cell_60 = row6.createCell(0);
            cell_60.setCellValue("一、");
            cell_60.setCellStyle(cellLeft);
            Cell cell_61 = row6.createCell(1);
            cell_61.setCellValue("供货内容：");
            cell_61.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 10));

            CellStyle cellBottomStyle = ExcelProcessDataUtils.createBottomStyle(workbook);
            BizProcessChild queryBizProcessChild = new BizProcessChild();
            queryBizProcessChild.setDataId(bizProcessDataParamter.getDataId());
            List<BizProcessChild> bizProductChildList = bizProcessChildService.selectBizTestProductList(queryBizProcessChild);
//            BizProcessChild queryBizProcessChild = new BizProcessChild();
//            queryBizProcessChild.setProcurementId(String.valueOf(bizProcessDataParamter.getDataId()));
//            queryBizProcessChild.setBizEditFlag("2");
//            queryBizProcessChild.setString2("1");
//            queryBizProcessChild.setLevelValue("1");
//            List<BizProcessChild> bizProductChildList = bizProcessChildService.selectExportBizChildProductList(queryBizProcessChild);

            CellStyle cellTableStyle = ExcelProcessDataUtils.cellTableStyle(workbook);

            Row row7 = sheet.createRow(7);
            Cell cell_80 = row7.createCell(0);
            cell_80.setCellValue("序号");
            cell_80.setCellStyle(cellTableStyle);
            Cell cell_81 = row7.createCell(1);
            cell_81.setCellValue("名称");
            cell_81.setCellStyle(cellTableStyle);
            Cell cell_811 = row7.createCell(2);
            cell_811.setCellValue("系列");
            cell_811.setCellStyle(cellTableStyle);
            Cell cell_82 = row7.createCell(3);
            cell_82.setCellValue("型号");
            cell_82.setCellStyle(cellTableStyle);
            Cell cell_83 = row7.createCell(4);
            cell_83.setCellValue("规格");
            cell_83.setCellStyle(cellTableStyle);
            Cell cell_84 = row7.createCell(5);
            cell_84.setCellValue("数量");
            cell_84.setCellStyle(cellTableStyle);
            Cell cell_85 = row7.createCell(6);
            cell_85.setCellValue("单价");
            cell_85.setCellStyle(cellTableStyle);
            Cell cell_86 = row7.createCell(7);
            cell_86.setCellValue("金额");
            cell_86.setCellStyle(cellTableStyle);
            Cell cell_87 = row7.createCell(8);
            cell_87.setCellValue("材质说明");
            cell_87.setCellStyle(cellTableStyle);
            Cell cell_88 = row7.createCell(9);
            cell_88.setCellValue("流转单号");
            cell_88.setCellStyle(cellTableStyle);
            Cell cell_89 = row7.createCell(10);
            cell_89.setCellValue("备注");
            cell_89.setCellStyle(cellTableStyle);

            int rowCount = 7;
            int num = 0;
            BigDecimal price = new BigDecimal("0");
            BigDecimal totalPrice = new BigDecimal("0");
            Double string14D = new Double(0);

            if (!CollectionUtils.isEmpty(bizProductChildList)) {
                for (int i = 0; i < bizProductChildList.size(); i++) {
                    BizProcessChild bizProcessChild = bizProductChildList.get(i);
                    num += StringUtils.isEmpty(bizProcessChild.getProductNum()) ? 0 : Integer.valueOf(bizProcessChild.getProductNum());
                    price = price.add(new BigDecimal(bizProcessChild.getProductProcurementPrice()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getProductProcurementPrice());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getProductNum());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getProductName());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(bizProcessChild.getModel());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(bizProcessChild.getSpecifications());
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getProductNum());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getProductProcurementPrice());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    String caizhi = "阀体:" + bizProcessChild.getValvebodyMaterial() + ",阀芯:" + bizProcessChild.getValveElement()
                            + ",密封材质:" + bizProcessChild.getSealingMaterial() + ",驱动形式:" + bizProcessChild.getDriveForm()
                            + ",连接方式:" + bizProcessChild.getConnectionType() + ",阀轴材质：" + bizProcessChild.getValveShaft();
                    cell8.setCellValue(caizhi);
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(contractNoStr);
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }

            List<BizProcessChild> actuatorList = bizProcessChildService.selectBizTestActuatorList(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(actuatorList)) {
                for (int i = 0; i < actuatorList.size(); i++) {
                    BizProcessChild bizProcessChild = actuatorList.get(i);
                    num += StringUtils.isEmpty(bizProcessChild.getActuatorNum()) ? 0 : Integer.valueOf(bizProcessChild.getActuatorNum());
                    price = price.add(new BigDecimal(bizProcessChild.getActuatorString6()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getActuatorString6());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getActuatorNum());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getActuatorName());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(bizProcessChild.getActuatorString1());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue("");
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getActuatorNum());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getActuatorString6());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(bizProcessChild.getString5());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(contractNoStr);
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }

            List<BizProcessChild> ref1List = bizProcessChildService.selectBizTestRef1List(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(ref1List)) {
                for (int i = 0; i < ref1List.size(); i++) {
                    BizProcessChild bizProcessChild = ref1List.get(i);
                    num += Double.isNaN(bizProcessChild.getProductRef1Num()) ? 0 : bizProcessChild.getProductRef1Num().intValue();
                    price = price.add(new BigDecimal(bizProcessChild.getRef1Price()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getRef1Price());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getProductRef1Num());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getRef1Name());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(bizProcessChild.getModel());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(bizProcessChild.getRef1Specifications());
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getProductRef1Num());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getRef1Price());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(bizProcessChild.getRef1MaterialRequire());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(contractNoStr);
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }
            List<BizProcessChild> ref2List = bizProcessChildService.selectBizTestRef2List(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(ref2List)) {
                for (int i = 0; i < ref2List.size(); i++) {
                    BizProcessChild bizProcessChild = ref2List.get(i);
                    num += Double.isNaN(bizProcessChild.getProductRef2Num()) ? 0 : bizProcessChild.getProductRef2Num().intValue();
                    price = price.add(new BigDecimal(bizProcessChild.getRef2Price()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getRef2Price());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getProductRef2Num());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getRef2Name());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(bizProcessChild.getModel());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue("");
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getProductRef2Num());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getRef2Price());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue("");
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(contractNoStr);
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }
            List<BizProcessChild> paList = bizProcessChildService.selectBizTestPAList(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(paList)) {
                for (int i = 0; i < paList.size(); i++) {
                    BizProcessChild bizProcessChild = paList.get(i);
                    num += Double.isNaN(bizProcessChild.getPattachmentCount()) ? 0 : bizProcessChild.getPattachmentCount().intValue();
                    price = price.add(new BigDecimal(bizProcessChild.getCostPrice()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getCostPrice());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getPattachmentCount());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getChineseName());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue("");
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(bizProcessChild.getChineseSpecifications());
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getPattachmentCount());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getCostPrice());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(bizProcessChild.getMaterial());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(contractNoStr);
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }
            rowCount++;
            Row row9 = sheet.createRow(rowCount);
            Cell cell_90 = row9.createCell(0);
            cell_90.setCellValue("合计");
            cell_90.setCellStyle(cellTableStyle);
            Cell cell_91 = row9.createCell(1);
            cell_91.setCellValue("");
            cell_91.setCellStyle(cellTableStyle);
            Cell cell_92 = row9.createCell(2);
            cell_92.setCellValue("");
            cell_92.setCellStyle(cellTableStyle);
            Cell cell_93 = row9.createCell(3);
            cell_93.setCellValue("");
            cell_93.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 3));
            Cell cell_94 = row9.createCell(4);
            cell_94.setCellValue("");
            Cell cell_95 = row9.createCell(5);
            cell_95.setCellValue(num);
            Cell cell_96 = row9.createCell(6);
            cell_96.setCellValue("");
            Cell cell_97 = row9.createCell(7);
            cell_97.setCellStyle(cellTableStyle);
            cell_97.setCellValue(totalPrice.doubleValue());
            Cell cell_98 = row9.createCell(8);
            // 优惠金额
            Double preferentialPrice = bizProcess.getPrice20();
            if (Objects.nonNull(preferentialPrice) && !new Double(0).equals(preferentialPrice)) {
                cell_98.setCellValue("优惠价：");
            }
            cell_98.setCellStyle(cellTableStyle);
            Cell cell_99 = row9.createCell(9);
            if (Objects.nonNull(preferentialPrice) &&  !new Double(0).equals(preferentialPrice)) {
                cell_99.setCellValue(preferentialPrice);
            }
            cell_99.setCellStyle(cellTableStyle);
            Cell cell_100 = row9.createCell(10);
            cell_100.setCellStyle(cellTableStyle);


            rowCount++;
            Row row10 = sheet.createRow(rowCount);
            Cell cell_10 = row10.createCell(0);
            cell_10.setCellStyle(cellTableStyle);
            double totalPricePrefer = 0;
            if (Objects.nonNull(preferentialPrice) &&  !new Double(0).equals(preferentialPrice)) {
                cell_98.setCellValue("优惠价：");
                totalPricePrefer = totalPrice.doubleValue() - preferentialPrice;
            } else {
                totalPricePrefer = totalPrice.doubleValue();
            }
            cell_10.setCellValue("合计人民币金额（大写）：" + StringUtils.convert(totalPricePrefer) + "（以上价格为含13%税价格）");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 10));
            Cell cell_11 = row10.createCell(1);
            cell_11.setCellStyle(cellTableStyle);
            Cell cell_12 = row10.createCell(2);
            cell_12.setCellStyle(cellTableStyle);
            Cell cell_13 = row10.createCell(3);
            cell_13.setCellStyle(cellTableStyle);
            Cell cell_14 = row10.createCell(4);
            cell_14.setCellStyle(cellTableStyle);
            Cell cell_15 = row10.createCell(5);
            cell_15.setCellStyle(cellTableStyle);
            Cell cell_16 = row10.createCell(6);
            cell_16.setCellStyle(cellTableStyle);
            Cell cell_17 = row10.createCell(7);
            cell_17.setCellStyle(cellTableStyle);
            Cell cell_18 = row10.createCell(8);
            cell_18.setCellStyle(cellTableStyle);
            Cell cell_19 = row10.createCell(9);
            cell_19.setCellStyle(cellTableStyle);
            Cell cell_119 = row10.createCell(10);
            cell_119.setCellStyle(cellTableStyle);


            rowCount++;
            Row row11 = sheet.createRow(rowCount);
            Cell cell_11_0 = row11.createCell(0);
            cell_11_0.setCellValue("二、");
            Cell cell_11_1 = row11.createCell(1);
            cell_11_1.setCellValue("技术及其他要求：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 3, 10));
            Cell cell_11_2 = row11.createCell(2);
            cell_11_2.setCellStyle(cellBottomStyle);
            Cell cell_11_3 = row11.createCell(3);
            cell_11_3.setCellValue(bizProcess.getPurchaseSpecificRequests());
            cell_11_3.setCellStyle(cellBottomStyle);
            Cell cell_11_4 = row11.createCell(4);
            cell_11_4.setCellStyle(cellBottomStyle);
            Cell cell_11_5 = row11.createCell(5);
            cell_11_5.setCellStyle(cellBottomStyle);
            Cell cell_11_6 = row11.createCell(6);
            cell_11_6.setCellStyle(cellBottomStyle);
            Cell cell_11_7 = row11.createCell(7);
            cell_11_7.setCellStyle(cellBottomStyle);
            Cell cell_11_8 = row11.createCell(8);
            cell_11_8.setCellStyle(cellBottomStyle);
            Cell cell_11_9 = row11.createCell(9);
            cell_11_9.setCellStyle(cellBottomStyle);
            Cell cell_11_10 = row11.createCell(10);
            cell_11_10.setCellStyle(cellBottomStyle);


            rowCount++;
            Row row12 = sheet.createRow(rowCount);
            row12.setRowStyle(cellLeft);
            Cell cell_12_0 = row12.createCell(0);
            cell_12_0.setCellValue("三、");
            Cell cell_12_1 = row12.createCell(1);
            cell_12_1.setCellValue("质量要求：符合国家及行业标准或卖方企业标准；产品合格证、标牌：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 7));
            Cell cell_12_6 = row12.createCell(8);
            String string19 = bizProcess.getString19();
            String biaopai = "好利";
            if (StringUtils.isNotEmpty(string19)) {
                List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType("product_logo");
                Map<String, String> collect = dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel, (a, b) -> a));
                biaopai = collect.get(string19);
            }
            cell_12_6.setCellValue(biaopai);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 8, 10));

            rowCount++;
            Row row13 = sheet.createRow(rowCount);
            row13.setHeight((short) 1300);

            row13.setRowStyle(cellLeft);
            Cell cell_13_0 = row13.createCell(0);
            cell_13_0.setCellValue("四、");
            Cell cell_13_1 = row13.createCell(1);
            cell_13_1.setCellStyle(cellLeft);
            cell_13_1.setCellValue("产品验收：按国家标准验收，甲方若有要求按甲方要求验收，验收合格以甲方签字为准；在验收期限内，需方对瑕疵产品有权无条件拒收，或要求供方换货，如更换之后的产品仍未达到验收标准的，需方有权要求退回。对于需方拒收或退回的产品，供方应在指定的期限内退还相应的货款。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));

            rowCount++;
            Row row14 = sheet.createRow(rowCount);
            Cell cell_14_0 = row14.createCell(0);
            cell_14_0.setCellValue("五、");
            Cell cell_14_1 = row14.createCell(1);
            cell_14_1.setCellValue("产品质保：");
            Cell cell_14_2 = row14.createCell(3);
            cell_14_2.setCellValue("自出厂之日算起，");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 3, 5));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 6, 7));
            ExcelProcessDataUtils.addValidationData(sheet, rowCount, rowCount, 6, 7, ExcelProcessDataUtils.baozhiqi);

            rowCount++;
            Row row15 = sheet.createRow(rowCount);
            row15.setHeight((short) 1500);

            row15.setRowStyle(cellLeft);
            Cell cell_15_0 = row15.createCell(2);
            cell_15_0.setCellStyle(cellLeft);
            cell_15_0.setCellValue("需方有权在质保期内对货物质量表示异议，供方在接到需方异议后应在3日内处理，否决即视为默认接收异议和处理意见，质保期内如出现产品本身质量问题，供方免费进行维修，不能维修予以更换，甲方若有因产品售后罚款及处理费用等损失的由乙方负责承担，供方未履行质量保证义务的，需方有权拒绝退还质保金，并要求供方另行支付合同总价款20%的违约金。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 10));

            rowCount++;
            Row row16 = sheet.createRow(rowCount);
            Cell cell_16_0 = row16.createCell(0);
            cell_16_0.setCellValue("六、");
            Cell cell_16_1 = row16.createCell(1);
            cell_16_1.setCellValue("安装与调试：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
            Cell cell_16_2 = row16.createCell(3);
            cell_16_2.setCellValue("电话指导");

            rowCount++;
            Row row17 = sheet.createRow(rowCount);
            Cell cell_17_0 = row17.createCell(0);
            cell_17_0.setCellValue("七、");
            Cell cell_17_1 = row17.createCell(1);
            cell_17_1.setCellValue("包装方式：");
            Cell cell_17_2 = row17.createCell(2);
            cell_17_2.setCellValue(bizProcess.getString27());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 4));
            Cell cell_17_4 = row17.createCell(5);
            cell_17_4.setCellValue("包装物回收：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 6));
            Cell cell_17_5 = row17.createCell(7);
            cell_17_5.setCellValue("不回收，由甲方自行处理。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 10));

            rowCount++;
            Row row18 = sheet.createRow(rowCount);
            Cell cell_18_0 = row18.createCell(0);
            cell_18_0.setCellValue("八、");
            Cell cell_18_1 = row18.createCell(1);
            cell_18_1.setCellValue("交货周期:");
            Cell cell_18_2 = row18.createCell(2);
            cell_18_2.setCellValue("合同生效日起   天发货，若有推迟发货超过5天，每天收取1%合同额违约金，并有权取消合同。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 10));

//            rowCount++;
//            Row row19 = sheet.createRow(rowCount);
//            row19.setHeight((short) 600);
//            Cell cell_19_0 = row19.createCell(2);
//            cell_19_0.setCellValue("合同生效日起   天发货，若有推迟发货超过5天，每天收取1%合同额违约金，并有权取消合同。");
//            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 7));
//            cell_19_0.setCellStyle(cellLeft);

            rowCount++;
            Row row20 = sheet.createRow(rowCount);
            Cell cell_20_0 = row20.createCell(0);
            cell_20_0.setCellValue("九、");
            Cell cell_20_10 = row20.createCell(1);
            cell_20_10.setCellValue("付款方式：");
            String contractPaytype = dictDataService.selectDictLabel("contract_paytype", bizProcess.getString20());
            Cell cell_20_12 = row20.createCell(2);
            cell_20_12.setCellValue(contractPaytype);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 10));

            rowCount++;
            Row row210 = sheet.createRow(rowCount);
            Cell cell_211_0 = row210.createCell(0);
            cell_211_0.setCellValue("十、");
            Cell cell_212_0 = row210.createCell(1);
            cell_212_0.setCellValue("运输：");

            rowCount++;
            Row row2101 = sheet.createRow(rowCount);
            Cell cell_211_01 = row2101.createCell(1);
            cell_211_01.setCellValue("1、运输方式：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
            Cell cell_212_02 = row2101.createCell(3);
            cell_212_02.setCellValue(bizProcess.getString9());
            Cell cell_212_04 = row2101.createCell(4);
            cell_212_04.setCellValue("运费：");
            Cell cell_212_05 = row2101.createCell(5);
            cell_212_05.setCellValue(bizProcess.getString26());

            rowCount++;
            Row row22 = sheet.createRow(rowCount);
            Cell cell_22_1 = row22.createCell(1);
            cell_22_1.setCellValue("2、收货人及收货地点：");
            Cell cell_22_3 = row22.createCell(3);
            cell_22_3.setCellValue(bizProcess.getString28());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 3, 10));

            rowCount++;
            Row row23 = sheet.createRow(rowCount);
            row23.setHeight((short) 700);
            Cell cell_23_1 = row23.createCell(1);
            cell_23_1.setCellValue("3、产品运送至需方指定地点并由指定收货人签收后，视为供方完成交付义务，在供方完成交付义务之前，产品在运输途中毁损、灭失的风险均由供方承担，需方不承担任何责任。");
            cell_23_1.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));

            rowCount++;
            Row row231 = sheet.createRow(rowCount);
            Cell cell_231_1 = row231.createCell(1);
            cell_231_1.setCellValue("4、其他约定事项：                                                                   。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));

            rowCount++;
            Row row24 = sheet.createRow(rowCount);
            Cell cell_24_1 = row24.createCell(0);
            cell_24_1.setCellValue("十一、");
            Cell cell_24_2 = row24.createCell(1);
            cell_24_2.setCellValue("产品所有权自交接时起转移，月结供应商以甲方收货入库上账开始执行相关付款手续。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));

            rowCount++;
            Row row25 = sheet.createRow(rowCount);
            row25.setHeight((short) 1300);
            Cell cell_25_1 = row25.createCell(0);
            cell_25_1.setCellValue("十二、");
            Cell cell_25_2 = row25.createCell(1);
            cell_25_2.setCellValue("违约责任：供需双方在合同交往中供方应遵守商业道德廉洁从业，供方违反商业道德和廉洁从业尊则给与甲方人员回扣、礼金等即为违约，给需方造成损失的应双倍赔偿损失。合同签订后，买卖双方严格执行双方所签订合同的条款，其中一方不履行或不完全履行合同者应承担相应的法律责任；促裁委员会：因本合同产生的争议，双方应友好协商解决，协商不成的，任何一方均有权向需方住所地仲裁委员会仲裁。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));
            cell_25_2.setCellStyle(cellLeft);

            rowCount++;
            Row row26 = sheet.createRow(rowCount);
            row26.setHeight((short) 300);
            Cell cell_26_1 = row26.createCell(0);
            cell_26_1.setCellValue("十三、");
            Cell cell_26_2 = row26.createCell(1);
            cell_26_2.setCellValue("本合同双方盖章生效，复印件同等有效，一式两份，双方各持一份，具有同等法律效力。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));
            cell_26_2.setCellStyle(cellLeft);

            rowCount++;
            rowCount++;
            Row row27 = sheet.createRow(rowCount);
            Cell cell_27_1 = row27.createCell(0);
            cell_27_1.setCellValue("单位名称：" + bizProcess.getString1() + "（章）");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            Cell cell_27_4 = row27.createCell(5);
            cell_27_4.setCellValue("单位名称：" + string6Name + "（章）");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 10));

            rowCount++;
            Row row28 = sheet.createRow(rowCount);
            Cell cell_28_1 = row28.createCell(0);
            cell_28_1.setCellValue("单位地址：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("address"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            Cell cell_28_4 = row28.createCell(5);
            cell_28_4.setCellValue("单位地址：" + bizSuppliers.getAddress());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 10));

            rowCount++;
            Row row29 = sheet.createRow(rowCount);
            Cell cell_29_1 = row29.createCell(0);
            cell_29_1.setCellValue("法定代表人：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            Cell cell_29_4 = row29.createCell(5);
            cell_29_4.setCellValue("法定代表人：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 10));

            rowCount++;
            Row row30 = sheet.createRow(rowCount);
            Cell cell_30_1 = row30.createCell(0);
            cell_30_1.setCellValue("委托代理人：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            Cell cell_30_4 = row30.createCell(5);
            cell_30_4.setCellValue("委托代理人：" + bizSuppliers.getHumanCapitalMeasure());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 10));

            rowCount++;
            Row row31 = sheet.createRow(rowCount);
            Cell cell_31_1 = row31.createCell(0);
            cell_31_1.setCellValue("电     话：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("phone"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            Cell cell_31_4 = row31.createCell(5);
            cell_31_4.setCellValue("电     话：" + bizSuppliers.getTelphone());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 10));

            rowCount++;
            Row row32 = sheet.createRow(rowCount);
            Cell cell_32_1 = row32.createCell(0);
            cell_32_1.setCellValue("开户银行：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("bank"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            Cell cell_32_4 = row32.createCell(5);
            cell_32_4.setCellValue("开户银行：" + bizSuppliers.getBank() == null ? "" : bizSuppliers.getBank());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 10));

            rowCount++;
            Row row33 = sheet.createRow(rowCount);
            Cell cell_33_1 = row33.createCell(0);
            cell_33_1.setCellValue("帐    号：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("bankNo"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            Cell cell_33_4 = row33.createCell(5);
            cell_33_4.setCellValue("帐    号：" + bizSuppliers.getBankNo());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 10));

            rowCount++;
            Row row34 = sheet.createRow(rowCount);
            Cell cell_34_1 = row34.createCell(0);
            cell_34_1.setCellValue("税    号：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("faxNo"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            Cell cell_34_4 = row34.createCell(5);
            cell_34_4.setCellValue("税    号：" + bizSuppliers.getTaxNumber());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 10));

            String filename = ExcelUtil.encodingFilenameByXls("采购合同");
            OutputStream out = new FileOutputStream(getAbsoluteFile(filename));
            workbook.write(out);
            out.flush();
            out.close();
            return AjaxResult.success(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    /**
     * 导出采购合同北京系统给山西系统下单导出
     */
    @PostMapping("/exportPurchaseContractToShanXi")
    @ResponseBody
    public AjaxResult exportPurchaseContractToShanXi(BizProcessData bizProcessDataParamter) {
        try {
            String id = bizProcessDataParamter.getDataId().toString();
            BizProcessData bizProcess = bizProcessDataService.selectBizProcessDataById(Long.parseLong(id));
            String contractNoStr = "";
            for (String contractNo : bizProcess.getString10().split(",")) {
                BizProcessData bizProcessData = new BizProcessData();
                bizProcessData.setString1(contractNo);
                bizProcessData.setBizId("contract");
                bizProcessData.setQueryStatus("0");
                List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRef(bizProcessData);
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
                    String codeName = list.get(0).getCodeName();
                    if (StringUtils.isNotEmpty(codeName)) {
                        contractNoStr += contractNo + "-" + codeName + ",";
                    } else {
                        contractNoStr += contractNo + ",";
                    }
                }
            }
            if (contractNoStr.contains(",")) {
                contractNoStr = contractNoStr.substring(0, contractNoStr.length()-1);
            }
            Workbook workbook = new HSSFWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            Sheet sheet = workbook.createSheet("候马订单");
            Row row1 = sheet.createRow(0);
            Cell cell_title_1 = row1.createCell(0);
            cell_title_1.setCellValue("    如无问题，请尽快回传及付款，以免影响交货期！回传电话：010-67171220。");
            CellStyle cellCenterss = ExcelProcessDataUtils.cellCenter(workbook);
            cell_title_1.setCellStyle(cellCenterss);
            CellRangeAddress cra0 = new CellRangeAddress(0, 0, 0, 10);
            sheet.addMergedRegion(cra0);

            // 单元格样式
            CellStyle cellLeft = ExcelProcessDataUtils.cellLeft(workbook);
            Row row2 = sheet.createRow(1);
            row2.setHeight((short) 500);

            CellRangeAddress cra1 = new CellRangeAddress(1, 1, 0, 10);
            sheet.addMergedRegion(cra1);
            Cell cell_title_2 = row2.createCell(0);
            cell_title_2.setCellValue("产品销售合同");
            CellStyle cellTitle = ExcelProcessDataUtils.titleCell(workbook);
            cell_title_2.setCellStyle(cellTitle);


            Row row3 = sheet.createRow(2);
            Cell cell_30 = row3.createCell(1);
            cell_30.setCellValue("需方：" + bizProcess.getString1());
            cell_30.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 4));
//            Cell cell_34 = row3.createCell(4);
//            cell_34.setCellValue("签订地点：");
//            cell_34.setCellStyle(cellLeft);
//            Cell cell_35 = row3.createCell(5);
//            cell_35.setCellValue(bizProcess.getString24());
//            cell_35.setCellStyle(cellLeft);
//            Cell cell_36 = row3.createCell(6);
//            cell_36.setCellValue("合同编号：");
//            cell_36.setCellStyle(cellLeft);
//            Cell cell_37 = row3.createCell(7);
//            cell_37.setCellValue(bizProcess.getString12());
//            cell_37.setCellStyle(cellLeft);

            Row row4 = sheet.createRow(3);
            String string6 = bizProcess.getString6();
            String string6Name = "";
            BizSuppliers bizSuppliers = null;
            if (StringUtils.isNotEmpty(string6)) {
                bizSuppliers = bizSuppliersService.selectBizSuppliersById(Long.parseLong(string6));
                if (bizSuppliers != null) {
                    string6Name = bizSuppliers.getName();
                }
            }
            Cell cell_40 = row4.createCell(1);
            cell_40.setCellValue("供方：" + string6Name);
            cell_40.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 4));

            Row row5 = sheet.createRow(4);
            Cell cell_50 = row5.createCell(1);
            cell_50.setCellValue("合同编号：" + contractNoStr);
            cell_50.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));
            Row row6 = sheet.createRow(5);
            Cell cell_60 = row6.createCell(1);
            cell_60.setCellValue("签订日期："+ DateUtils.dateTime(bizProcess.getCreateTime()));
            cell_60.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 4));

            Row row7 = sheet.createRow(6);
            Cell cell_70 = row7.createCell(1);
            cell_70.setCellValue("为保障买卖双方的合法权益，根据现行《民法典》及有关法律规定，经友好协商，一致同意按下列条款签订本合同。");
            cell_70.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 10));


            Row row88 = sheet.createRow(7);
            Cell cell_800 = row88.createCell(0);
            cell_800.setCellValue("一、");
            cell_800.setCellStyle(cellCenterss);
            Cell cell_811 = row88.createCell(1);
            cell_811.setCellValue("供货内容：");
            cell_811.setCellStyle(cellCenterss);
            sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 10));

            CellStyle cellBottomStyle = ExcelProcessDataUtils.createBottomStyle(workbook);
            BizProcessChild queryBizProcessChild = new BizProcessChild();
            queryBizProcessChild.setDataId(bizProcessDataParamter.getDataId());
            List<BizProcessChild> bizProductChildList = bizProcessChildService.selectBizTestProductList(queryBizProcessChild);
            CellStyle cellTableStyle = ExcelProcessDataUtils.cellTableStyle(workbook);

            Row row77 = sheet.createRow(8);
            Cell cell_80 = row77.createCell(0);
            cell_80.setCellValue("序号");
            cell_80.setCellStyle(cellTableStyle);
            Cell cell_81 = row77.createCell(1);
            cell_81.setCellValue("产品ID");
            cell_81.setCellStyle(cellTableStyle);
            Cell cell_82 = row77.createCell(2);
            cell_82.setCellValue("名称");
            cell_82.setCellStyle(cellTableStyle);
            Cell cell_83 = row77.createCell(3);
            cell_83.setCellValue("标签属性");
            cell_83.setCellStyle(cellTableStyle);
            Cell cell_84 = row77.createCell(4);
            cell_84.setCellValue("规格");
            cell_84.setCellStyle(cellTableStyle);
            Cell cell_85 = row77.createCell(5);
            cell_85.setCellValue("颜色");
            cell_85.setCellStyle(cellTableStyle);
            Cell cell_86 = row77.createCell(6);
            cell_86.setCellValue("数量");
            cell_86.setCellStyle(cellTableStyle);
            Cell cell_87 = row77.createCell(7);
            cell_87.setCellValue("单价");
            cell_87.setCellStyle(cellTableStyle);
            Cell cell_88 = row77.createCell(8);
            cell_88.setCellValue("合计");
            cell_88.setCellStyle(cellTableStyle);
            Cell cell_89 = row77.createCell(9);
            cell_89.setCellValue("材质");
            cell_89.setCellStyle(cellTableStyle);
            Cell cell_810 = row77.createCell(10);
            cell_810.setCellValue("备注");
            cell_810.setCellStyle(cellTableStyle);

            int rowCount = 8;
            int num = 0;
            BigDecimal price = new BigDecimal("0");
            BigDecimal totalPrice = new BigDecimal("0");

            String label = "等级:A;系列:%sA";
            if (!CollectionUtils.isEmpty(bizProductChildList)) {
                for (int i = 0; i < bizProductChildList.size(); i++) {
                    BizProcessChild bizProcessChild = bizProductChildList.get(i);
                    num += StringUtils.isEmpty(bizProcessChild.getProductNum()) ? 0 : Integer.valueOf(bizProcessChild.getProductNum());
                    price = price.add(new BigDecimal(bizProcessChild.getProductProcurementPrice()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getProductProcurementPrice());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getProductNum());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getModel());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(bizProcessChild.getProductName());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue(String.format(label, bizProcessChild.getSeries()));
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(4);
                    cell5.setCellValue(bizProcessChild.getSpecifications());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(5);
                    cell6.setCellValue(bizProcessChild.getColor());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue(bizProcessChild.getProductNum());
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(7);
                    cell8.setCellValue(bizProcessChild.getProductProcurementPrice());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(8);
                    cell9.setCellValue(amount);
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(9);
                    String caizhi = "阀体:" + bizProcessChild.getValvebodyMaterial() + ",阀芯:" + bizProcessChild.getValveElement()
                            + ",密封材质:" + bizProcessChild.getSealingMaterial() + ",驱动形式:" + bizProcessChild.getDriveForm()
                            + ",连接方式:" + bizProcessChild.getConnectionType() + ",阀轴材质：" + bizProcessChild.getValveShaft();
                    cell10.setCellValue(caizhi);
                    cell10.setCellStyle(cellTableStyle);
                    Cell cell111 = row.createCell(10);
                    cell111.setCellValue(bizProcessChild.getRemark());
                    cell111.setCellStyle(cellTableStyle);
                }
            }

            List<BizProcessChild> actuatorList = bizProcessChildService.selectBizTestActuatorList(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(actuatorList)) {
                for (int i = 0; i < actuatorList.size(); i++) {
                    BizProcessChild bizProcessChild = actuatorList.get(i);
                    num += StringUtils.isEmpty(bizProcessChild.getActuatorNum()) ? 0 : Integer.valueOf(bizProcessChild.getActuatorNum());
                    price = price.add(new BigDecimal(bizProcessChild.getActuatorString6()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getActuatorString6());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getActuatorNum());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getActuatorString1());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(bizProcessChild.getActuatorName());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue(bizProcessChild.getActuatorString3());
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(4);
                    cell5.setCellValue("");
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(5);
                    cell6.setCellValue(bizProcessChild.getColor());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue(bizProcessChild.getActuatorNum());
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(7);
                    cell8.setCellValue(bizProcessChild.getActuatorString6());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(8);
                    cell9.setCellValue(amount);
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(9);
                    cell10.setCellValue(bizProcessChild.getString5());
                    cell10.setCellStyle(cellTableStyle);
                    Cell cell111 = row.createCell(10);
                    cell111.setCellValue(bizProcessChild.getRemark());
                    cell111.setCellStyle(cellTableStyle);
                }
            }

            List<BizProcessChild> ref1List = bizProcessChildService.selectBizTestRef1List(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(ref1List)) {
                for (int i = 0; i < ref1List.size(); i++) {
                    BizProcessChild bizProcessChild = ref1List.get(i);
                    num += Double.isNaN(bizProcessChild.getProductRef1Num()) ? 0 : bizProcessChild.getProductRef1Num().intValue();
                    price = price.add(new BigDecimal(bizProcessChild.getRef1Price()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getRef1Price());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getProductRef1Num());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getModel());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(bizProcessChild.getRef1Name());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue(String.format(label, bizProcessChild.getSeries()));
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(4);
                    cell5.setCellValue(bizProcessChild.getRef1Specifications());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(5);
                    cell6.setCellValue("");
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue(bizProcessChild.getProductRef1Num());
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(7);
                    cell8.setCellValue(bizProcessChild.getRef1Price());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(8);
                    cell9.setCellValue(amount);
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(9);
                    cell10.setCellValue(bizProcessChild.getRef1MaterialRequire());
                    cell10.setCellStyle(cellTableStyle);
                    Cell cell111 = row.createCell(10);
                    cell111.setCellValue(bizProcessChild.getRemark());
                    cell111.setCellStyle(cellTableStyle);
                }
            }
            rowCount++;
            Row row9 = sheet.createRow(rowCount);
            Cell cell_90 = row9.createCell(0);
            cell_90.setCellValue("");
            cell_90.setCellStyle(cellTableStyle);
            Cell cell_91 = row9.createCell(1);
            cell_91.setCellValue("合计");
            cell_91.setCellStyle(cellTableStyle);
            Cell cell_92 = row9.createCell(2);
            cell_92.setCellValue("");
            cell_92.setCellStyle(cellTableStyle);
            Cell cell_93 = row9.createCell(3);
            cell_93.setCellValue("");
            cell_93.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_94 = row9.createCell(4);
            cell_94.setCellStyle(cellTableStyle);
            Cell cell_95 = row9.createCell(5);
            cell_95.setCellStyle(cellTableStyle);
            Cell cell_96 = row9.createCell(6);
            cell_96.setCellValue(num);
            Cell cell_97 = row9.createCell(7);
            cell_97.setCellStyle(cellTableStyle);
            Cell cell_98 = row9.createCell(8);
            cell_98.setCellValue(totalPrice.doubleValue());
            cell_98.setCellStyle(cellTableStyle);
            Cell cell_99 = row9.createCell(9);
            cell_99.setCellStyle(cellTableStyle);
            Cell cell_910 = row9.createCell(10);
            cell_910.setCellStyle(cellTableStyle);

            rowCount++;
            Row row10 = sheet.createRow(rowCount);
            Cell cell_10 = row10.createCell(0);
            cell_10.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_11 = row10.createCell(1);
            cell_11.setCellValue("优惠后价格");
            cell_11.setCellStyle(cellTableStyle);
            Cell cell_12 = row10.createCell(2);
            cell_12.setCellStyle(cellTableStyle);
            Cell cell_13 = row10.createCell(3);
            cell_13.setCellStyle(cellTableStyle);
            Cell cell_14 = row10.createCell(4);
            cell_14.setCellStyle(cellTableStyle);
            Cell cell_15 = row10.createCell(5);
            cell_15.setCellStyle(cellTableStyle);
            Cell cell_16 = row10.createCell(6);
            cell_16.setCellValue(num);
            cell_16.setCellStyle(cellTableStyle);
            Cell cell_17 = row10.createCell(7);
            cell_17.setCellStyle(cellTableStyle);
            Cell cell_18 = row10.createCell(8);
            cell_18.setCellValue(totalPrice.doubleValue());
            cell_18.setCellStyle(cellTableStyle);
            Cell cell_19 = row10.createCell(9);
            cell_19.setCellStyle(cellTableStyle);
            Cell cell_110 = row10.createCell(10);
            cell_110.setCellStyle(cellTableStyle);

            rowCount++;
            Row row101 = sheet.createRow(rowCount);
            Cell cell_101 = row101.createCell(0);
            cell_101.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_111 = row101.createCell(1);
            cell_111.setCellValue("以上价格均为含税价格");
            cell_111.setCellStyle(cellTableStyle);
            Cell cell_122 = row101.createCell(2);
            cell_122.setCellStyle(cellTableStyle);
            Cell cell_133 = row101.createCell(3);
            cell_133.setCellStyle(cellTableStyle);
            Cell cell_144 = row101.createCell(4);
            cell_144.setCellStyle(cellTableStyle);
            cell_144.setCellValue(StringUtils.convert(totalPrice.doubleValue()));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 10));
            Cell cell_155 = row101.createCell(5);
            cell_155.setCellStyle(cellTableStyle);
            Cell cell_166 = row101.createCell(6);
            cell_166.setCellStyle(cellTableStyle);
            Cell cell_177 = row101.createCell(7);
            cell_177.setCellStyle(cellTableStyle);
            Cell cell_188 = row101.createCell(8);
            cell_188.setCellStyle(cellTableStyle);
            Cell cell_199 = row101.createCell(9);
            cell_199.setCellStyle(cellTableStyle);
            Cell cell_1101 = row101.createCell(10);
            cell_1101.setCellStyle(cellTableStyle);

            rowCount++;
            Row row11 = sheet.createRow(rowCount);
            Cell cell_11_0 = row11.createCell(0);
            cell_11_0.setCellValue("二、");
            cell_11_0.setCellStyle(cellCenterss);
            Cell cell_11_1 = row11.createCell(1);
            cell_11_1.setCellValue("特殊要求：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 10));
            Cell cell_11_2 = row11.createCell(2);
            cell_11_2.setCellValue(StringUtils.trim(bizProcess.getPurchaseSpecificRequests()));

            rowCount++;
            Row row12 = sheet.createRow(rowCount);
            row12.setRowStyle(cellLeft);
            Cell cell_12_0 = row12.createCell(0);
            cell_12_0.setCellValue("三、");
            cell_12_0.setCellStyle(cellCenterss);
            Cell cell_12_1 = row12.createCell(1);
            cell_12_1.setCellValue("随货资料：");
            Cell cell_12_2 = row12.createCell(2);
            cell_12_2.setCellValue("安装使用说明书/产品合格证");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_12_6 = row12.createCell(6);
            cell_12_6.setCellValue("标    牌：");
            Cell cell_12_7 = row12.createCell(7);
            cell_12_7.setCellValue("好利标牌");

            rowCount++;
            Row row13 = sheet.createRow(rowCount);
            row13.setRowStyle(cellLeft);
            Cell cell_13_0 = row13.createCell(0);
            cell_13_0.setCellValue("四、");
            cell_13_0.setCellStyle(cellCenterss);

            Cell cell_13_1 = row13.createCell(1);
            cell_13_1.setCellValue("验收标准：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 10));
            Cell cell_13_2 = row13.createCell(2);
            cell_13_2.setCellValue("国家标准 验收");

            rowCount++;
            Row row14 = sheet.createRow(rowCount);
            Cell cell_14_0 = row14.createCell(0);
            cell_14_0.setCellValue("五、");
            cell_14_0.setCellStyle(cellCenterss);

            Cell cell_14_1 = row14.createCell(1);
            cell_14_1.setCellValue("质量保证：");
            Cell cell_14_2 = row14.createCell(2);
            cell_14_2.setCellValue("质保期 18个月 (自客户签收日期)");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 10));

            rowCount++;
            Row row16 = sheet.createRow(rowCount);
            Cell cell_16_0 = row16.createCell(0);
            cell_16_0.setCellValue("六、");
            cell_16_0.setCellStyle(cellCenterss);

            Cell cell_16_1 = row16.createCell(1);
            cell_16_1.setCellValue("包装规范：");
            Cell cell_16_2 = row16.createCell(2);
            cell_16_2.setCellValue(bizProcess.getString27());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_16_6 = row16.createCell(6);
            cell_16_6.setCellValue("运输方式：");
            Cell cell_16_7 = row16.createCell(7);
            cell_16_7.setCellValue(bizProcess.getString9());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 10));

            rowCount++;
            Row row17 = sheet.createRow(rowCount);
            Cell cell_17_0 = row17.createCell(0);
            cell_17_0.setCellValue("七、");
            cell_17_0.setCellStyle(cellCenterss);

            Cell cell_17_1 = row17.createCell(1);
            cell_17_1.setCellValue("发货日期：");
            Cell cell_17_2 = row17.createCell(2);
            cell_17_2.setCellValue(DateUtils.dateTime(bizProcess.getDatetime3()));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_17_3 = row17.createCell(6);
            cell_17_3.setCellValue("运费承担：");
            Cell cell_17_4 = row17.createCell(7);
            cell_17_4.setCellValue(bizProcess.getString26());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 10));

            rowCount++;
            Row row18 = sheet.createRow(rowCount);
            Cell cell_18_0 = row18.createCell(0);
            cell_18_0.setCellValue("八、");
            cell_18_0.setCellStyle(cellCenterss);

            Cell cell_18_1 = row18.createCell(1);
            cell_18_1.setCellValue("付款方式:");
            Cell cell_18_2 = row18.createCell(2);
            String contractPaytype = dictDataService.selectDictLabel("contract_paytype", bizProcess.getString20());
            cell_18_2.setCellValue(contractPaytype);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_188_3 = row18.createCell(6);
            cell_188_3.setCellValue("收货信息：");
            Cell cell_188_4 = row18.createCell(7);
            cell_188_4.setCellValue(bizProcess.getString28());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 12));

            rowCount++;
            Row row20 = sheet.createRow(rowCount);
            row20.setHeight((short) 900);
            Cell cell_20_0 = row20.createCell(0);
            cell_20_0.setCellValue("九、");
            cell_20_0.setCellStyle(cellCenterss);
            Cell cell_20_10 = row20.createCell(1);
            cell_20_10.setCellValue("违约责任：合同签订后，买卖双方严格执行双方所签订合同的条款，其中一方不履行或不完全履行合同者应承担相应的法律责任。促裁委员会：双方协商解决，解决不成由买方所在工商局经济合同仲裁委员会仲裁或法院起诉。本合同一式贰份，双方各执一份，双方盖章后生效（传真件有效）。");
            cell_20_10.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));

            rowCount++;
            Row row27 = sheet.createRow(rowCount);
            Cell cell_27_1 = row27.createCell(1);
            cell_27_1.setCellValue("需方：");
            Cell cell_27_2 = row27.createCell(2);
            cell_27_2.setCellValue(bizProcess.getString1());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_27_4 = row27.createCell(6);
            cell_27_4.setCellValue("供方：");
            Cell cell_27_7 = row27.createCell(7);
            cell_27_7.setCellValue(string6Name);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 10));

            rowCount++;
            Row row30 = sheet.createRow(rowCount);
            Cell cell_30_1 = row30.createCell(1);
            cell_30_1.setCellValue("委托代理人：");
            Cell cell_30_2 = row30.createCell(2);
            cell_30_2.setCellValue("");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_30_4 = row30.createCell(6);
            cell_30_4.setCellValue("委托代理人：");
            Cell cell_30_7 = row30.createCell(7);
            cell_30_7.setCellValue(bizSuppliers.getHumanCapitalMeasure());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 10));

            rowCount++;
            Row row31 = sheet.createRow(rowCount);
            Cell cell_31_1 = row31.createCell(1);
            cell_31_1.setCellValue("电     话：");
            Cell cell_31_2 = row31.createCell(2);
            cell_31_2.setCellValue(Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("phone"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_31_4 = row31.createCell(6);
            cell_31_4.setCellValue("电     话：");
            Cell cell_31_7 = row31.createCell(7);
            cell_31_7.setCellValue(bizSuppliers.getTelphone());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 10));

            rowCount++;
            Row row32 = sheet.createRow(rowCount);
            Cell cell_32_1 = row32.createCell(1);
            cell_32_1.setCellValue("传     真：");
            Cell cell_32_2 = row32.createCell(2);
            cell_32_2.setCellValue(Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("faxNo"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_32_4 = row32.createCell(6);
            cell_32_4.setCellValue("传     真：");
            Cell cell_32_7 = row32.createCell(7);
            cell_32_7.setCellValue(bizSuppliers.getFax());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 10));

            rowCount++;
            Row row33 = sheet.createRow(rowCount);
            Cell cell_33_1 = row33.createCell(1);
            cell_33_1.setCellValue("地     址：");
            Cell cell_33_2 = row33.createCell(2);
            cell_33_2.setCellValue(Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("address"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 5));
            Cell cell_33_4 = row33.createCell(6);
            cell_33_4.setCellValue("地     址");
            Cell cell_33_7 = row33.createCell(7);
            cell_33_7.setCellValue(bizSuppliers.getAddress());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 7, 10));

            String filename = ExcelUtil.encodingFilenameByXls("侯马合同");
            OutputStream out = new FileOutputStream(getAbsoluteFile(filename));
            workbook.write(out);
            out.flush();
            out.close();
            return AjaxResult.success(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    /**
     * 导出采购合同
     */
    @PostMapping("/exportPurchaseContractForOther")
    @ResponseBody
    public AjaxResult exportPurchaseContractForOther(BizProcessData bizProcessDataParamter) {
        try {
            String id = bizProcessDataParamter.getDataId().toString();
            BizProcessData bizProcess = bizProcessDataService.selectBizProcessDataById(Long.parseLong(id));

            Workbook workbook = new HSSFWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            Sheet sheet = workbook.createSheet("产品采购合同");
            // 单元格样式
            CellStyle cellLeft = ExcelProcessDataUtils.cellLeft(workbook);
            Row row1 = sheet.createRow(0);
            row1.setHeight((short) 600);

            CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 7);
            sheet.addMergedRegion(cra1);
            Cell cell_title_1 = row1.createCell(0);
            cell_title_1.setCellValue("产品采购合同");
            CellStyle cellTitle = ExcelProcessDataUtils.titleCell(workbook);
            cell_title_1.setCellStyle(cellTitle);

            Row row2 = sheet.createRow(1);
            row2.setHeight((short) 600);
            Cell cell_26 = row2.createCell(5);
            cell_26.setCellValue("合同编号：");
            cell_26.setCellStyle(cellLeft);
            Cell cell_27 = row2.createCell(6);
            cell_27.setCellValue(bizProcess.getString12());
            cell_27.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));

            Row row3 = sheet.createRow(2);
            row3.setHeight((short) 600);

            Cell cell_30 = row3.createCell(0);
            cell_30.setCellValue("甲方（需方）：" + bizProcess.getString1());
            cell_30.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 4));
            Cell cell_34 = row3.createCell(5);
            cell_34.setCellValue("签订地点：");
            cell_34.setCellStyle(cellLeft);
            Cell cell_35 = row3.createCell(6);
            cell_35.setCellValue(bizProcess.getString24());
            cell_35.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));

            Row row4 = sheet.createRow(3);
            row4.setHeight((short) 600);

            String string6 = bizProcess.getString6();
            String string6Name = "";
            BizSuppliers bizSuppliers = null;
            if (StringUtils.isNotEmpty(string6)) {
                bizSuppliers = bizSuppliersService.selectBizSuppliersById(Long.parseLong(string6));
                if (bizSuppliers != null) {
                    string6Name = bizSuppliers.getName();
                }
            }
            Cell cell_40 = row4.createCell(0);
            cell_40.setCellValue("乙方（供方）：" + "山西好利阀机械制造有限公司");
            cell_40.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 4));
            Cell cell_44 = row4.createCell(5);
            cell_44.setCellValue("签订日期：");
            cell_44.setCellStyle(cellLeft);
            Cell cell_46 = row4.createCell(6);
            cell_46.setCellValue(DateUtils.dateTime(bizProcess.getCreateTime()));
            cell_46.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 7));

            Row row5 = sheet.createRow(5);
            row5.setHeight((short) 600);
            Cell cell_50 = row5.createCell(0);
            row5.setRowStyle(cellLeft);
            cell_50.setCellValue("为保障买卖双方的合法权益，根据现行《民法典》及有关法律规定，经友好协商，一致同意按下列条款签订本合同。");
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));
            cell_50.setCellStyle(cellLeft);

            Row row6 = sheet.createRow(6);
            Cell cell_60 = row6.createCell(0);
            cell_60.setCellValue("一、");
            cell_60.setCellStyle(cellLeft);
            Cell cell_61 = row6.createCell(1);
            cell_61.setCellValue("供货内容：");
            cell_61.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));

            CellStyle cellBottomStyle = ExcelProcessDataUtils.createBottomStyle(workbook);
            BizProcessChild queryBizProcessChild = new BizProcessChild();
            queryBizProcessChild.setDataId(bizProcessDataParamter.getDataId());
            List<BizProcessChild> bizProductChildList = bizProcessChildService.selectBizTestProductList(queryBizProcessChild);
            CellStyle cellTableStyle = ExcelProcessDataUtils.cellTableStyle(workbook);

            Row row7 = sheet.createRow(7);
            Cell cell_80 = row7.createCell(0);
            cell_80.setCellValue("序号");
            cell_80.setCellStyle(cellTableStyle);
            Cell cell_81 = row7.createCell(1);
            cell_81.setCellValue("名称");
            cell_81.setCellStyle(cellTableStyle);
            Cell cell_811 = row7.createCell(2);
            cell_811.setCellValue("系列");
            cell_811.setCellStyle(cellTableStyle);
            Cell cell_82 = row7.createCell(3);
            cell_82.setCellValue("型号");
            cell_82.setCellStyle(cellTableStyle);
            Cell cell_83 = row7.createCell(4);
            cell_83.setCellValue("规格");
            cell_83.setCellStyle(cellTableStyle);
            Cell cell_84 = row7.createCell(5);
            cell_84.setCellValue("数量");
            cell_84.setCellStyle(cellTableStyle);
            Cell cell_85 = row7.createCell(6);
            cell_85.setCellValue("单价");
            cell_85.setCellStyle(cellTableStyle);
            Cell cell_86 = row7.createCell(7);
            cell_86.setCellValue("金额");
            cell_86.setCellStyle(cellTableStyle);
            Cell cell_87 = row7.createCell(8);
            cell_87.setCellValue("材质说明");
            cell_87.setCellStyle(cellTableStyle);
            Cell cell_88 = row7.createCell(9);
            cell_88.setCellValue("流转单号");
            cell_88.setCellStyle(cellTableStyle);
            Cell cell_89 = row7.createCell(10);
            cell_89.setCellValue("备注");
            cell_89.setCellStyle(cellTableStyle);

            String customerId = bizProcess.getString4();
            BizCustomer bizCustomer = new BizCustomer();
            if (StringUtils.isNotEmpty(customerId)) {
                bizCustomer = bizCustomerService.selectBizCustomerById(Long.parseLong(customerId));
            }
            int rowCount = 7;
            int num = 0;
            BigDecimal price = new BigDecimal("0");
            BigDecimal totalPrice = new BigDecimal("0");
            Double string14D = new Double(0);

            if (!CollectionUtils.isEmpty(bizProductChildList)) {
                for (int i = 0; i < bizProductChildList.size(); i++) {
                    BizProcessChild bizProcessChild = bizProductChildList.get(i);
                    num += StringUtils.isEmpty(bizProcessChild.getProductNum()) ? 0 : Integer.valueOf(bizProcessChild.getProductNum());
                    price = price.add(new BigDecimal(bizProcessChild.getProductProcurementPrice()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getProductProcurementPrice());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getProductNum());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getProductName());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(bizProcessChild.getModel());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(bizProcessChild.getSpecifications());
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getProductNum());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getProductProcurementPrice());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    String caizhi = "阀体:" + bizProcessChild.getValvebodyMaterial() + ",阀芯:" + bizProcessChild.getValveElement()
                            + ",密封材质:" + bizProcessChild.getSealingMaterial() + ",驱动形式:" + bizProcessChild.getDriveForm()
                            + ",连接方式:" + bizProcessChild.getConnectionType() + ",阀轴材质：" + bizProcessChild.getValveShaft();
                    cell8.setCellValue(caizhi);
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    if (StringUtils.isBlank(bizCustomer.getCodeName())) {
                        cell9.setCellValue(bizProcess.getString10());
                    } else {
                        cell9.setCellValue(bizProcess.getString10() + " " + bizCustomer.getCodeName());
                    }
                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }

            List<BizProcessChild> actuatorList = bizProcessChildService.selectBizTestActuatorList(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(actuatorList)) {
                for (int i = 0; i < actuatorList.size(); i++) {
                    BizProcessChild bizProcessChild = actuatorList.get(i);
                    num += StringUtils.isEmpty(bizProcessChild.getActuatorNum()) ? 0 : Integer.valueOf(bizProcessChild.getActuatorNum());
                    price = price.add(new BigDecimal(bizProcessChild.getActuatorString6()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getActuatorString6());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getActuatorNum());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getActuatorName());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(bizProcessChild.getActuatorString1());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue("");
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getActuatorNum());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getActuatorString6());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(bizProcessChild.getString5());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    if (StringUtils.isBlank(bizCustomer.getCodeName())) {
                        cell9.setCellValue(bizProcess.getString10());
                    } else {
                        cell9.setCellValue(bizProcess.getString10() + " " + bizCustomer.getCodeName());
                    }                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }

            List<BizProcessChild> ref1List = bizProcessChildService.selectBizTestRef1List(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(ref1List)) {
                for (int i = 0; i < ref1List.size(); i++) {
                    BizProcessChild bizProcessChild = ref1List.get(i);
                    num += Double.isNaN(bizProcessChild.getProductRef1Num()) ? 0 : bizProcessChild.getProductRef1Num().intValue();
                    price = price.add(new BigDecimal(bizProcessChild.getRef1Price()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getRef1Price());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getProductRef1Num());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getRef1Name());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(bizProcessChild.getModel());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(bizProcessChild.getRef1Specifications());
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getProductRef1Num());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getRef1Price());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(bizProcessChild.getRef1MaterialRequire());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    if (StringUtils.isBlank(bizCustomer.getCodeName())) {
                        cell9.setCellValue(bizProcess.getString10());
                    } else {
                        cell9.setCellValue(bizProcess.getString10() + " " + bizCustomer.getCodeName());
                    }                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }
            List<BizProcessChild> ref2List = bizProcessChildService.selectBizTestRef2List(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(ref2List)) {
                for (int i = 0; i < ref2List.size(); i++) {
                    BizProcessChild bizProcessChild = ref2List.get(i);
                    num += Double.isNaN(bizProcessChild.getProductRef2Num()) ? 0 : bizProcessChild.getProductRef2Num().intValue();
                    price = price.add(new BigDecimal(bizProcessChild.getRef2Price()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getRef2Price());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getProductRef2Num());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getRef2Name());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(bizProcessChild.getModel());
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue("");
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getProductRef2Num());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getRef2Price());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue("");
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    if (StringUtils.isBlank(bizCustomer.getCodeName())) {
                        cell9.setCellValue(bizProcess.getString10());
                    } else {
                        cell9.setCellValue(bizProcess.getString10() + " " + bizCustomer.getCodeName());
                    }                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }
            List<BizProcessChild> paList = bizProcessChildService.selectBizTestPAList(queryBizProcessChild);
            if (!CollectionUtils.isEmpty(paList)) {
                for (int i = 0; i < paList.size(); i++) {
                    BizProcessChild bizProcessChild = paList.get(i);
                    num += Double.isNaN(bizProcessChild.getPattachmentCount()) ? 0 : bizProcessChild.getPattachmentCount().intValue();
                    price = price.add(new BigDecimal(bizProcessChild.getCostPrice()));

                    BigDecimal a1 = new BigDecimal(bizProcessChild.getCostPrice());
                    BigDecimal aa = new BigDecimal(bizProcessChild.getPattachmentCount());
                    Double amount = a1.multiply(aa).doubleValue();
                    totalPrice = totalPrice.add(new BigDecimal(amount));
                    string14D += StringUtils.toDouble(bizProcessChild.getString14());

                    rowCount++;
                    Row row = sheet.createRow(rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(i+1);
                    cell1.setCellStyle(cellTableStyle);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bizProcessChild.getChineseName());
                    cell2.setCellStyle(cellTableStyle);
                    Cell cell22 = row.createCell(2);
                    cell22.setCellValue(bizProcessChild.getSeries());
                    cell22.setCellStyle(cellTableStyle);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue("");
                    cell3.setCellStyle(cellTableStyle);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(bizProcessChild.getChineseSpecifications());
                    cell4.setCellStyle(cellTableStyle);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(bizProcessChild.getPattachmentCount());
                    cell5.setCellStyle(cellTableStyle);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(bizProcessChild.getCostPrice());
                    cell6.setCellStyle(cellTableStyle);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(amount);
                    cell7.setCellStyle(cellTableStyle);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(bizProcessChild.getMaterial());
                    cell8.setCellStyle(cellTableStyle);
                    Cell cell9 = row.createCell(9);
                    if (StringUtils.isBlank(bizCustomer.getCodeName())) {
                        cell9.setCellValue(bizProcess.getString10());
                    } else {
                        cell9.setCellValue(bizProcess.getString10() + " " + bizCustomer.getCodeName());
                    }                    cell9.setCellStyle(cellTableStyle);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(bizProcessChild.getRemark());
                    cell10.setCellStyle(cellTableStyle);
                }
            }
            rowCount++;
            Row row9 = sheet.createRow(rowCount);
            Cell cell_90 = row9.createCell(0);
            cell_90.setCellValue("合计");
            cell_90.setCellStyle(cellTableStyle);
            Cell cell_91 = row9.createCell(1);
            cell_91.setCellValue("");
            cell_91.setCellStyle(cellTableStyle);
            Cell cell_92 = row9.createCell(2);
            cell_92.setCellValue("");
            cell_92.setCellStyle(cellTableStyle);
            Cell cell_93 = row9.createCell(3);
            cell_93.setCellValue("");
            cell_93.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 3));
            Cell cell_94 = row9.createCell(4);
            cell_94.setCellValue("");
            Cell cell_95 = row9.createCell(5);
            cell_95.setCellValue(num);
            Cell cell_96 = row9.createCell(6);
            cell_96.setCellValue("");
            Cell cell_97 = row9.createCell(7);
            cell_97.setCellStyle(cellTableStyle);
            cell_97.setCellValue(totalPrice.doubleValue());
            Cell cell_98 = row9.createCell(8);
            if (!string14D.equals(new Double(0))) {
                cell_98.setCellValue("优惠价：");
            }
            cell_98.setCellStyle(cellTableStyle);
            Cell cell_99 = row9.createCell(9);
            if (!string14D.equals(new Double(0))) {
                cell_99.setCellValue(string14D);
            }
            cell_99.setCellStyle(cellTableStyle);
            Cell cell_100 = row9.createCell(10);
            cell_100.setCellStyle(cellTableStyle);


            rowCount++;
            Row row10 = sheet.createRow(rowCount);
            Cell cell_10 = row10.createCell(0);
            cell_10.setCellStyle(cellTableStyle);
            cell_10.setCellValue("合计人民币金额（大写）：" + StringUtils.convert(totalPrice.doubleValue()) + "（以上价格为含13%税价格）");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 10));
            Cell cell_11 = row10.createCell(1);
            cell_11.setCellStyle(cellTableStyle);
            Cell cell_12 = row10.createCell(2);
            cell_12.setCellStyle(cellTableStyle);
            Cell cell_13 = row10.createCell(3);
            cell_13.setCellStyle(cellTableStyle);
            Cell cell_14 = row10.createCell(4);
            cell_14.setCellStyle(cellTableStyle);
            Cell cell_15 = row10.createCell(5);
            cell_15.setCellStyle(cellTableStyle);
            Cell cell_16 = row10.createCell(6);
            cell_16.setCellStyle(cellTableStyle);
            Cell cell_17 = row10.createCell(7);
            cell_17.setCellStyle(cellTableStyle);
            Cell cell_18 = row10.createCell(8);
            cell_18.setCellStyle(cellTableStyle);
            Cell cell_19 = row10.createCell(9);
            cell_19.setCellStyle(cellTableStyle);
            Cell cell_119 = row10.createCell(10);
            cell_119.setCellStyle(cellTableStyle);


            rowCount++;
            Row row11 = sheet.createRow(rowCount);
            Cell cell_11_0 = row11.createCell(0);
            cell_11_0.setCellValue("二、");
            Cell cell_11_1 = row11.createCell(1);
            cell_11_1.setCellValue("技术及其他要求：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 3, 7));
            Cell cell_11_2 = row11.createCell(2);
            cell_11_2.setCellStyle(cellBottomStyle);
            Cell cell_11_3 = row11.createCell(3);
            cell_11_3.setCellValue(bizProcess.getPurchaseSpecificRequests());
            cell_11_3.setCellStyle(cellBottomStyle);
            Cell cell_11_4 = row11.createCell(4);
            cell_11_4.setCellStyle(cellBottomStyle);
            Cell cell_11_5 = row11.createCell(5);
            cell_11_5.setCellStyle(cellBottomStyle);

            rowCount++;
            Row row12 = sheet.createRow(rowCount);
            row12.setRowStyle(cellLeft);
            Cell cell_12_0 = row12.createCell(0);
            cell_12_0.setCellValue("三、");
            Cell cell_12_1 = row12.createCell(1);
            cell_12_1.setCellValue("质量要求：符合国家及行业标准或卖方企业标准；产品合格证、标牌：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 5));
            Cell cell_12_6 = row12.createCell(6);
            String string19 = bizProcess.getString19();
            String biaopai = "好利";
            if (StringUtils.isNotEmpty(string19)) {
                List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType("product_logo");
                Map<String, String> collect = dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel, (a, b) -> a));
                biaopai = collect.get(string19);
            }
            cell_12_6.setCellValue(biaopai);

            rowCount++;
            Row row13 = sheet.createRow(rowCount);
            row13.setHeight((short) 1500);

            row13.setRowStyle(cellLeft);
            Cell cell_13_0 = row13.createCell(0);
            cell_13_0.setCellValue("四、");
            Cell cell_13_1 = row13.createCell(1);
            cell_13_1.setCellStyle(cellLeft);
            cell_13_1.setCellValue("产品验收：按国家标准验收，甲方若有要求按甲方要求验收，验收合格以甲方签字为准；在验收期限内，需方对瑕疵产品有权无条件拒收，或要求供方换货，如更换之后的产品仍未达到验收标准的，需方有权要求退回。对于需方拒收或退回的产品，供方应在指定的期限内退还相应的货款。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 7));

            rowCount++;
            Row row14 = sheet.createRow(rowCount);
            Cell cell_14_0 = row14.createCell(0);
            cell_14_0.setCellValue("五、");
            Cell cell_14_1 = row14.createCell(1);
            cell_14_1.setCellValue("产品质保：");
            Cell cell_14_2 = row14.createCell(2);
            cell_14_2.setCellValue("自出厂之日算起，");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 5));
            ExcelProcessDataUtils.addValidationData(sheet, rowCount, rowCount, 4, 4, ExcelProcessDataUtils.baozhiqi);

            rowCount++;
            Row row15 = sheet.createRow(rowCount);
            row15.setHeight((short) 1800);

            row15.setRowStyle(cellLeft);
            Cell cell_15_0 = row15.createCell(2);
            cell_15_0.setCellStyle(cellLeft);
            cell_15_0.setCellValue("需方有权在质保期内对货物质量表示异议，供方在接到需方异议后应在3日内处理，否决即视为默认接收异议和处理意见，质保期内如出现产品本身质量问题，供方免费进行维修，不能维修予以更换，甲方若有因产品售后罚款及处理费用等损失的由乙方负责承担，供方未履行质量保证义务的，需方有权拒绝退还质保金，并要求供方另行支付合同总价款20%的违约金。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 7));

            rowCount++;
            Row row16 = sheet.createRow(rowCount);
            Cell cell_16_0 = row16.createCell(0);
            cell_16_0.setCellValue("六、");
            Cell cell_16_1 = row16.createCell(1);
            cell_16_1.setCellValue("安装与调试：");

            rowCount++;
            Row row17 = sheet.createRow(rowCount);
            Cell cell_17_0 = row17.createCell(0);
            cell_17_0.setCellValue("七、");
            Cell cell_17_1 = row17.createCell(1);
            cell_17_1.setCellValue("包装方式：");
            Cell cell_17_2 = row17.createCell(2);
            cell_17_2.setCellValue(bizProcess.getString27());
            Cell cell_17_3 = row17.createCell(3);
            cell_17_3.setCellValue("但确保产品能被保护。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 3, 4));
            Cell cell_17_4 = row17.createCell(5);
            cell_17_4.setCellValue("包装物回收：");
            Cell cell_17_5 = row17.createCell(6);
            cell_17_5.setCellValue("不回收，由甲方自行处理。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 6, 7));

            rowCount++;
            Row row18 = sheet.createRow(rowCount);
            Cell cell_18_0 = row18.createCell(0);
            cell_18_0.setCellValue("八、");
            Cell cell_18_1 = row18.createCell(1);
            cell_18_1.setCellValue("交货周期:");
            Cell cell_18_2 = row18.createCell(2);
//            cell_18_2.setCellValue(DateUtils.dateTime(bizProcess.getDatetime2()));
            cell_18_2.setCellValue("合同生效日起   天发货，若有推迟发货超过5天，每天收取1%合同额违约金，并有权取消合同。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 7));

//            rowCount++;
//            Row row19 = sheet.createRow(rowCount);
//            row19.setHeight((short) 600);
//            Cell cell_19_0 = row19.createCell(2);
//            cell_19_0.setCellValue("合同生效日起   天发货，若有推迟发货超过5天，每天收取1%合同额违约金，并有权取消合同。");
//            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 7));
//            cell_19_0.setCellStyle(cellLeft);

            rowCount++;
            Row row20 = sheet.createRow(rowCount);
            Cell cell_20_0 = row20.createCell(0);
            cell_20_0.setCellValue("九、");
            Cell cell_20_10 = row20.createCell(1);
            cell_20_10.setCellValue("付款方式：");
            String contractPaytype = dictDataService.selectDictLabel("contract_paytype", bizProcess.getString20());
            Cell cell_20_12 = row20.createCell(2);
            cell_20_12.setCellValue(contractPaytype);

            rowCount++;
            Row row210 = sheet.createRow(rowCount);
            Cell cell_211_0 = row210.createCell(0);
            cell_211_0.setCellValue("十、");
            Cell cell_212_0 = row210.createCell(1);
            cell_212_0.setCellValue("运输：");

            rowCount++;
            Row row2101 = sheet.createRow(rowCount);
            Cell cell_211_01 = row2101.createCell(1);
            cell_211_01.setCellValue("1、运输方式：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
            Cell cell_212_02 = row2101.createCell(3);
            cell_212_02.setCellValue(bizProcess.getString9());
            Cell cell_212_04 = row2101.createCell(4);
            cell_212_04.setCellValue("运费：");
            Cell cell_212_05 = row2101.createCell(5);
            cell_212_05.setCellValue(bizProcess.getString26());

            rowCount++;
            Row row22 = sheet.createRow(rowCount);
            Cell cell_22_1 = row22.createCell(1);
            cell_22_1.setCellValue("2、收货人及收货地点：");
            Cell cell_22_3 = row22.createCell(3);
            cell_22_3.setCellValue(bizProcess.getString28());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 3, 7));

            rowCount++;
            Row row23 = sheet.createRow(rowCount);
            row23.setHeight((short) 900);
            Cell cell_23_1 = row23.createCell(1);
            cell_23_1.setCellValue("3、产品运送至需方指定地点并由指定收货人签收后，视为供方完成交付义务，在供方完成交付义务之前，产品在运输途中毁损、灭失的风险均由供方承担，需方不承担任何责任。");
            cell_23_1.setCellStyle(cellLeft);
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 7));

            rowCount++;
            Row row231 = sheet.createRow(rowCount);
            Cell cell_231_1 = row231.createCell(1);
            cell_231_1.setCellValue("4、其他约定事项：                                                                   。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 7));

            rowCount++;
            Row row24 = sheet.createRow(rowCount);
            Cell cell_24_1 = row24.createCell(0);
            cell_24_1.setCellValue("十一、");
            Cell cell_24_2 = row24.createCell(1);
            cell_24_2.setCellValue("产品所有权自交接时起转移，月结供应商以甲方收货入库上账开始执行相关付款手续。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 7));

            rowCount++;
            Row row25 = sheet.createRow(rowCount);
            row25.setHeight((short) 1800);
            Cell cell_25_1 = row25.createCell(0);
            cell_25_1.setCellValue("十二、");
            Cell cell_25_2 = row25.createCell(1);
            cell_25_2.setCellValue("违约责任：供需双方在合同交往中供方应遵守商业道德廉洁从业，供方违反商业道德和廉洁从业尊则给与甲方人员回扣、礼金等即为违约，给需方造成损失的应双倍赔偿损失。合同签订后，买卖双方严格执行双方所签订合同的条款，其中一方不履行或不完全履行合同者应承担相应的法律责任；促裁委员会：因本合同产生的争议，双方应友好协商解决，协商不成的，任何一方均有权向需方住所地仲裁委员会仲裁。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 7));
            cell_25_2.setCellStyle(cellLeft);

            rowCount++;
            Row row26 = sheet.createRow(rowCount);
            row26.setHeight((short) 600);
            Cell cell_26_1 = row26.createCell(0);
            cell_26_1.setCellValue("十三、");
            Cell cell_26_2 = row26.createCell(1);
            cell_26_2.setCellValue("本合同双方盖章生效，复印件同等有效，一式两份，双方各持一份，具有同等法律效力。");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 7));
            cell_26_2.setCellStyle(cellLeft);

            rowCount++;
            rowCount++;
            Row row27 = sheet.createRow(rowCount);
            Cell cell_27_1 = row27.createCell(1);
            cell_27_1.setCellValue("单位名称：" + bizProcess.getString1() + "（章）");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_27_4 = row27.createCell(4);
            cell_27_4.setCellValue("单位名称：" + string6Name + "（章）");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

            rowCount++;
            Row row28 = sheet.createRow(rowCount);
            Cell cell_28_1 = row28.createCell(1);
            cell_28_1.setCellValue("单位地址：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("address"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_28_4 = row28.createCell(4);
            cell_28_4.setCellValue("单位地址：" + bizSuppliers.getAddress());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

            rowCount++;
            Row row29 = sheet.createRow(rowCount);
            Cell cell_29_1 = row29.createCell(1);
            cell_29_1.setCellValue("法定代表人：");
            Cell cell_29_4 = row29.createCell(4);
            cell_29_4.setCellValue("法定代表人：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

            rowCount++;
            Row row30 = sheet.createRow(rowCount);
            Cell cell_30_1 = row30.createCell(1);
            cell_30_1.setCellValue("委托代理人：");
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_30_4 = row30.createCell(4);
            cell_30_4.setCellValue("委托代理人：" + bizSuppliers.getHumanCapitalMeasure());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

            rowCount++;
            Row row31 = sheet.createRow(rowCount);
            Cell cell_31_1 = row31.createCell(1);
            cell_31_1.setCellValue("电     话：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("phone"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_31_4 = row31.createCell(4);
            cell_31_4.setCellValue("电     话：" + bizSuppliers.getTelphone());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

            rowCount++;
            Row row32 = sheet.createRow(rowCount);
            Cell cell_32_1 = row32.createCell(1);
            cell_32_1.setCellValue("开户银行：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("bank"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_32_4 = row32.createCell(4);
            cell_32_4.setCellValue("开户银行：" + bizSuppliers.getBank() == null ? "" : bizSuppliers.getBank());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

            rowCount++;
            Row row33 = sheet.createRow(rowCount);
            Cell cell_33_1 = row33.createCell(1);
            cell_33_1.setCellValue("帐    号：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("bankNo"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_33_4 = row33.createCell(4);
            cell_33_4.setCellValue("帐    号：" + bizSuppliers.getBankNo());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

            rowCount++;
            Row row34 = sheet.createRow(rowCount);
            Cell cell_34_1 = row34.createCell(1);
            cell_34_1.setCellValue("税    号：" + Util.jsonObject.getJSONObject(bizProcess.getString1()).getString("faxNo"));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
            Cell cell_34_4 = row34.createCell(4);
            cell_34_4.setCellValue("税    号：" + bizSuppliers.getTaxNumber());
            sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

            String filename = ExcelUtil.encodingFilenameByXls("采购合同");
            OutputStream out = new FileOutputStream(getAbsoluteFile(filename));
            workbook.write(out);
            out.flush();
            out.close();
            return AjaxResult.success(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
}
