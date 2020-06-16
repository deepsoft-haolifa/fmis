package com.ruoyi.fmis.data.controller;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.itextpdf.PdfUtil;
import com.ruoyi.common.utils.itextpdf.TextWaterMarkPdfPageEvent;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.BizContractLevel;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotation.service.IBizQuotationService;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private ISysUserService sysUserService;

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


    @Autowired
    private IBizProductRefService bizProductRefService;

    @Autowired
    private IBizActuatorService bizActuatorService;

    @Autowired
    private IBizQuotationService bizQuotationService;

    @RequiresPermissions("fmis:data:view")
    @GetMapping()
    public String data(ModelMap mmap) {
        String toDo = getRequest().getParameter("todo");
        if ("1".equals(toDo)) {
            mmap.put("todo","1");
        }
        return prefix + "/data";
    }

    /**
     * 查询合同管理列表
     */
    @RequiresPermissions("fmis:data:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessData bizProcessData) {

        String toDo = getRequest().getParameter("todo");
        if ("1".equals(toDo)) {
            bizProcessData.setQueryStatus("1");
        }



        String bizId = bizProcessData.getBizId();
        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        String userFlowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            userFlowStatus = flowMap.keySet().iterator().next();
            bizProcessData.setRoleType(userFlowStatus);
        }
        startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRef(bizProcessData);


        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                String flowStatus = data.getFlowStatus();
                //结束标识
                String normalFlag = data.getNormalFlag();
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
                data.setLoginUserId(ShiroUtils.getUserId().toString());
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

        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildList(queryBizProcessChild);
        String productNames = "";
        String productIds = "";
        if (!CollectionUtils.isEmpty(bizProcessChildList)) {
            for (BizProcessChild bizProcessChild : bizProcessChildList) {
                String productId = bizProcessChild.getString2();
                String otherId = bizProcessChild.getString1() + "-" + productId;

                bizProcessChild.setParamterId(otherId);

                productIds += otherId + ",";
                String quotationId = bizProcessChild.getString1();
                if (StringUtils.isNotEmpty(quotationId)) {
                    BizQuotation bizQuotation = bizQuotationService.selectBizQuotationById(Long.parseLong(quotationId));
                    productNames += bizQuotation.getString1() + ",";
                    bizProcessChild.setBizQuotation(bizQuotation);

                }
                BizProduct bizProduct = null;
                BizProduct queryBizProduct = new BizProduct();
                queryBizProduct.setProductId(Long.parseLong(productId));
                List<BizProduct> bizProductList = bizProductService.selectBizProductList(queryBizProduct);
                if (!CollectionUtils.isEmpty(bizProductList)) {
                    bizProduct = bizProductList.get(0);
                }

                bizProcessChild.setBizProduct(bizProduct);
                String ref1Id = bizProcessChild.getString5();
                if (StringUtils.isNotEmpty(ref1Id)) {
                    bizProcessChild.setRef1(bizProductRefService.selectBizProductRefById(Long.parseLong(ref1Id)));
                }
                String ref2Id = bizProcessChild.getString8();
                if (StringUtils.isNotEmpty(ref2Id)) {
                    bizProcessChild.setRef2(bizProductRefService.selectBizProductRefById(Long.parseLong(ref2Id)));
                }
                String actuatorId = bizProcessChild.getString11();
                if (StringUtils.isNotEmpty(actuatorId)) {
                    bizProcessChild.setBizActuator(bizActuatorService.selectBizActuatorById(Long.parseLong(actuatorId)));
                }
            }
            bizProcessData.setBizProcessChildList(bizProcessChildList);
        }
        String customerId = bizProcessData.getString2();
        if (StringUtils.isNotEmpty(customerId)) {
            bizProcessData.setBizCustomer(bizCustomerService.selectBizCustomerById(Long.parseLong(customerId)));
        }
        mmap.put("quotationNames", productNames);
        mmap.put("quotationIds", productIds);
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/examineEdit";
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
                String productId = bizProcessChild.getString2();
                String otherId = bizProcessChild.getString1() + "-" + productId;

                bizProcessChild.setParamterId(otherId);

                productIds += otherId + ",";
                String quotationId = bizProcessChild.getString1();
                if (StringUtils.isNotEmpty(quotationId)) {
                    BizQuotation bizQuotation = bizQuotationService.selectBizQuotationById(Long.parseLong(quotationId));
                    productNames += bizQuotation.getString1() + ",";
                    bizProcessChild.setBizQuotation(bizQuotation);

                }

                BizProduct bizProduct = null;
                BizProduct queryBizProduct = new BizProduct();
                queryBizProduct.setProductId(Long.parseLong(productId));
                List<BizProduct> bizProductList = bizProductService.selectBizProductList(queryBizProduct);
                if (!CollectionUtils.isEmpty(bizProductList)) {
                    bizProduct = bizProductList.get(0);
                }

                bizProcessChild.setBizProduct(bizProduct);
                String ref1Id = bizProcessChild.getString5();
                if (StringUtils.isNotEmpty(ref1Id)) {
                    bizProcessChild.setRef1(bizProductRefService.selectBizProductRefById(Long.parseLong(ref1Id)));
                }
                String ref2Id = bizProcessChild.getString8();
                if (StringUtils.isNotEmpty(ref2Id)) {
                    bizProcessChild.setRef2(bizProductRefService.selectBizProductRefById(Long.parseLong(ref2Id)));
                }
                String actuatorId = bizProcessChild.getString11();
                if (StringUtils.isNotEmpty(actuatorId)) {
                    bizProcessChild.setBizActuator(bizActuatorService.selectBizActuatorById(Long.parseLong(actuatorId)));
                }
            }
            bizProcessData.setBizProcessChildList(bizProcessChildList);
        }
        String customerId = bizProcessData.getString2();
        if (StringUtils.isNotEmpty(customerId)) {
            bizProcessData.setBizCustomer(bizCustomerService.selectBizCustomerById(Long.parseLong(customerId)));
        }
        mmap.put("quotationNames", productNames);
        mmap.put("quotationIds", productIds);
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

    @GetMapping("/upload")
    public String upload(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        mmap.put("dataId", dataId);
        mmap.put("fileUrl", Global.getFileUrl());
        return prefix + "/upload";
    }

    @GetMapping("/selectQuotation")
    public String selectQuotation(ModelMap mmap) {
        String customerId = getRequest().getParameter("customerId");
        mmap.put("customerId",customerId);
        return prefix + "/selectQuotation";
    }

    //selectNewQuotation
    @GetMapping("/selectNewQuotation")
    public String selectNewQuotation(ModelMap mmap) {
        String customerId = getRequest().getParameter("customerId");
        mmap.put("customerId",customerId);
        return prefix + "/selectNewQuotation";
    }

    @GetMapping("/selectNewProduct")
    public String selectNewProduct(ModelMap mmap) {
        String customerId = getRequest().getParameter("customerId");
        mmap.put("customerId",customerId);
        return prefix + "/selectNewProduct";
    }
    @GetMapping("/selectPattachment")
    public String selectPattachment(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        String type = getRequest().getParameter("type");
        mmap.put("productId", productId);
        mmap.put("type", type);
        return prefix + "/selectPattachment";
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
        bizProcessData.setFlowStatus("0");
        String productArrayStr = bizProcessData.getProductParmters();
        setNormalFlag(bizProcessData,productArrayStr);
        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizProcessData.getBizId());
        if (!CollectionUtils.isEmpty(flowAllMap)) {
            for (String key : flowAllMap.keySet()) {
                bizProcessData.setNormalFlag(key);
            }
        }
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
     * 查询报价单产品
     * @return
     */
    @PostMapping("/listProductNoPage")
    @ResponseBody
    public TableDataInfo listProductNoPage(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizQuotationProductList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }


    @PostMapping("/listProductChild")
    @ResponseBody
    public TableDataInfo listProductChild(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setString2(level);
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }

    @PostMapping("/listLevelProduct")
    @ResponseBody
    public TableDataInfo listLevelProduct(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setString2(level);
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }

    @PostMapping("/listLevelActuator")
    @ResponseBody
    public TableDataInfo listLevelActuator(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setLevelLabel(level);
        List<BizProcessChild> bizProcessChildListActuatorA = bizProcessChildService.selectBizChildActuatorList(queryBizProcessChild);
        return getDataTable(bizProcessChildListActuatorA);
    }

    @PostMapping("/listLevelRef1")
    @ResponseBody
    public TableDataInfo listLevelRef1(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setLevelLabel(level);
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildRef1List(queryBizProcessChild);
        return getDataTable(bizProcessChildListRefA);
    }

    @PostMapping("/listLevelRef2")
    @ResponseBody
    public TableDataInfo listLevelRef2(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setLevelLabel(level);
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildRef2List(queryBizProcessChild);
        return getDataTable(bizProcessChildListRefA);
    }

    @PostMapping("/listLevelPA")
    @ResponseBody
    public TableDataInfo listLevelPA(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setLevelLabel(level);
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPAList(queryBizProcessChild);
        return getDataTable(bizProcessChildListRefA);
    }

    @PostMapping("/listLevelPA1")
    @ResponseBody
    public TableDataInfo listLevelPA1(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setLevelLabel(level);
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPA1List(queryBizProcessChild);
        return getDataTable(bizProcessChildListRefA);
    }
    @PostMapping("/listLevelPA2")
    @ResponseBody
    public TableDataInfo listLevelPA2(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setLevelLabel(level);
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPA2List(queryBizProcessChild);
        return getDataTable(bizProcessChildListRefA);
    }
    @PostMapping("/listLevelPA3")
    @ResponseBody
    public TableDataInfo listLevelPA3(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setLevelLabel(level);
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPA3List(queryBizProcessChild);
        return getDataTable(bizProcessChildListRefA);
    }
    @PostMapping("/listLevelPA4")
    @ResponseBody
    public TableDataInfo listLevelPA4(@RequestParam String dataId,@RequestParam String level) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(Long.parseLong(dataId));
        queryBizProcessChild.setLevelLabel(level);
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPA4List(queryBizProcessChild);
        return getDataTable(bizProcessChildListRefA);
    }
    @PostMapping("/listLevel")
    @ResponseBody
    public TableDataInfo listLevel(@RequestParam String dataId) {

        List<BizContractLevel> bizContractLevels = new ArrayList<>();

        if (!CollectionUtils.isEmpty(listLevelProduct(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("产品信息A");
            bizContractLevel.setLevelType("11");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelProduct(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("产品信息B");
            bizContractLevel.setLevelType("12");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelProduct(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("产品信息C");
            bizContractLevel.setLevelType("13");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        //执行器
        if (!CollectionUtils.isEmpty(listLevelActuator(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("执行器信息A");
            bizContractLevel.setLevelType("21");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelActuator(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("执行器信息B");
            bizContractLevel.setLevelType("22");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelActuator(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("执行器信息C");
            bizContractLevel.setLevelType("23");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        if (!CollectionUtils.isEmpty(listLevelRef1(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("法兰信息A");
            bizContractLevel.setLevelType("31");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelRef1(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("法兰信息B");
            bizContractLevel.setLevelType("32");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelRef1(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("法兰信息C");
            bizContractLevel.setLevelType("33");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        if (!CollectionUtils.isEmpty(listLevelRef2(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("螺栓信息A");
            bizContractLevel.setLevelType("41");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelRef2(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("螺栓信息B");
            bizContractLevel.setLevelType("42");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelRef2(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("螺栓信息C");
            bizContractLevel.setLevelType("43");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        //定位器
        if (!CollectionUtils.isEmpty(listLevelPA(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("定位器信息A");
            bizContractLevel.setLevelType("51");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("定位器信息B");
            bizContractLevel.setLevelType("52");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("定位器信息C");
            bizContractLevel.setLevelType("53");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        //电磁阀
        if (!CollectionUtils.isEmpty(listLevelPA1(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("电磁阀信息A");
            bizContractLevel.setLevelType("61");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA1(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("电磁阀信息B");
            bizContractLevel.setLevelType("62");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA1(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("电磁阀信息C");
            bizContractLevel.setLevelType("63");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        //回信器数
        if (!CollectionUtils.isEmpty(listLevelPA2(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("回信器数信息A");
            bizContractLevel.setLevelType("71");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA2(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("回信器数信息B");
            bizContractLevel.setLevelType("72");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA2(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("回信器数信息C");
            bizContractLevel.setLevelType("73");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        //气源三连件
        if (!CollectionUtils.isEmpty(listLevelPA3(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("气源三连件信息A");
            bizContractLevel.setLevelType("81");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA3(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("气源三连件信息B");
            bizContractLevel.setLevelType("82");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA3(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("气源三连件信息C");
            bizContractLevel.setLevelType("83");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        //可离合减速器
        if (!CollectionUtils.isEmpty(listLevelPA4(dataId,"A").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("可离合减速器信息A");
            bizContractLevel.setLevelType("91");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA4(dataId,"B").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("可离合减速器信息B");
            bizContractLevel.setLevelType("92");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }
        if (!CollectionUtils.isEmpty(listLevelPA4(dataId,"C").getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("可离合减速器信息C");
            bizContractLevel.setLevelType("93");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(dataId);
            bizContractLevels.add(bizContractLevel);
        }

        return getDataTable(bizContractLevels);
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
                String productId = bizProcessChild.getString2();
                String otherId = bizProcessChild.getString1() + "-" + productId;

                bizProcessChild.setParamterId(otherId);

                productIds += otherId + ",";
                String quotationId = bizProcessChild.getString1();
                if (StringUtils.isNotEmpty(quotationId)) {
                    BizQuotation bizQuotation = bizQuotationService.selectBizQuotationById(Long.parseLong(quotationId));
                    productNames += bizQuotation.getString1() + ",";
                    bizProcessChild.setBizQuotation(bizQuotation);

                }
                BizProduct queryBizProduct = new BizProduct();
                queryBizProduct.setProductId(Long.parseLong(productId));
                BizProduct bizProduct = null;
                List<BizProduct> bizProductList = bizProductService.selectBizProductList(queryBizProduct);
                if (!CollectionUtils.isEmpty(bizProductList)) {
                    bizProduct = bizProductList.get(0);
                }
                bizProcessChild.setBizProduct(bizProduct);
                String ref1Id = bizProcessChild.getString5();
                if (StringUtils.isNotEmpty(ref1Id)) {
                    bizProcessChild.setRef1(bizProductRefService.selectBizProductRefById(Long.parseLong(ref1Id)));
                }
                String ref2Id = bizProcessChild.getString8();
                if (StringUtils.isNotEmpty(ref2Id)) {
                    bizProcessChild.setRef2(bizProductRefService.selectBizProductRefById(Long.parseLong(ref2Id)));
                }
                String actuatorId = bizProcessChild.getString11();
                if (StringUtils.isNotEmpty(actuatorId)) {
                    bizProcessChild.setBizActuator(bizActuatorService.selectBizActuatorById(Long.parseLong(actuatorId)));
                }
            }
            bizProcessData.setBizProcessChildList(bizProcessChildList);
        }
        String customerId = bizProcessData.getString2();
        if (StringUtils.isNotEmpty(customerId)) {
            bizProcessData.setBizCustomer(bizCustomerService.selectBizCustomerById(Long.parseLong(customerId)));
        }
        String suppliersId = bizProcessData.getString3();
        if (StringUtils.isNotEmpty(suppliersId)) {
            bizProcessData.setBizSuppliers(bizSuppliersService.selectBizSuppliersById(Long.parseLong(suppliersId)));
        }


        mmap.put("quotationNames", productNames);
        mmap.put("quotationIds", productIds);
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
        setNormalFlag(bizProcessData,productArrayStr);
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
     *
     * 2销售经理审批结束 3 区域经理审批结束 4副总审批结束 5 老总审批结束
     * @param bizProcessData
     * @return
     */
    public String setNormalFlag (BizProcessData bizProcessData,String productArrayStr) {
        String normalFlag = "5";
        String totalPrice = bizProcessData.getPrice1().toString();
        String specialExpenses = bizProcessData.getString14();
        if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 500000) {
            normalFlag = "5";
        } else if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 300000) {
            normalFlag = "4";
        } else if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 100000) {
            normalFlag = "3";
        }  else {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            Double minCoefficient = new Double(10000000);
            Double minOtherCoefficient = new Double(10000000);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                if (bizProcessChild.getString2() != null) {

                    String productCoefficient = bizProcessChild.getString4();
                    if (StringUtils.isNotEmpty(productCoefficient) && Double.parseDouble(productCoefficient) < minCoefficient) {
                        minCoefficient = Double.parseDouble(productCoefficient);
                    }
                    String actuatorCoefficient = bizProcessChild.getString13();
                    if (StringUtils.isNotEmpty(actuatorCoefficient) && Double.parseDouble(actuatorCoefficient) < minOtherCoefficient) {
                        minOtherCoefficient = Double.parseDouble(actuatorCoefficient);
                    }
                    String productRef1Coefficient = bizProcessChild.getString7();
                    if (StringUtils.isNotEmpty(productRef1Coefficient) && Double.parseDouble(productRef1Coefficient) < minOtherCoefficient) {
                        minOtherCoefficient = Double.parseDouble(productRef1Coefficient);
                    }
                    String productRef2Coefficient = bizProcessChild.getString10();
                    if (StringUtils.isNotEmpty(productRef2Coefficient) && Double.parseDouble(productRef2Coefficient) < minOtherCoefficient) {
                        minOtherCoefficient = Double.parseDouble(productRef2Coefficient);
                    }
                }
            }
            /**
             * 计算最低系数
             * 如果系数（报价员录入的系数）底于0.9，由必须由销售副总审核、总经理审批，流程结束；
             * 如果系数0.9--1.0，必须由销售副总审核，流程结束，
             * 如果报价系数大于1.0--1.1则由区域经理审批完成后流程结束；
             * 如果系数大于1.1，则由部门销售经理审核完成后流程结束
             */
            if (minCoefficient < 0.88 || minOtherCoefficient < 0.92) {
                normalFlag = "5";
            }else if ((minCoefficient >= 0.88 && minCoefficient < 0.95) || (minOtherCoefficient >= 0.92 && minOtherCoefficient < 0.97)) {
                normalFlag = "4";
            } else if ((minCoefficient >= 0.95 && minCoefficient < 1) || (minOtherCoefficient >= 0.97 && minOtherCoefficient <= 1)) {
                normalFlag = "3";
            }
        }
        bizProcessData.setNormalFlag(normalFlag);
        return normalFlag;
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

    @PostMapping("/goPool")
    @ResponseBody
    public AjaxResult goPool() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        bizQuotation.setString13("1");
        bizQuotation.setUpdateBy(ShiroUtils.getUserId().toString());
        return toAjax(bizProcessDataService.updateBizProcessData(bizQuotation));
    }

    @PostMapping("/uploadUrl")
    @ResponseBody
    public AjaxResult uploadUrl() {
        String dataId = getRequest().getParameter("dataId");
        String url = getRequest().getParameter("url");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        bizProcessData.setString17(url);
        return toAjax(bizProcessDataService.updateBizProcessData(bizProcessData));
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

    /**
     * 选择客户
     */
    @GetMapping("/selectSuppliers")
    public String selectSuppliers() {
        return prefix + "/selectSuppliers";
    }

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 导出合同
     */
    @RequiresPermissions("fmis:data:export")
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
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizQuotationProductList(queryBizProcessChild);

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
            String companyName = "北京好利阀业集团有限公司";
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
                bizCustomer = bizCustomerService.selectBizCustomerById(Long.parseLong(customerId));
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
            table.addCell(PdfUtil.mergeCol("卖方：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(companyName, 6,textFont));


            table.addCell(PdfUtil.mergeCol("合同编号：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizProcessData.getString1() + "-" + bizCustomer.getCodeName() + "-" + StringUtils.trim(bizProcessData.getString4()), 5,textFont));

            //第二行
            table.addCell(PdfUtil.mergeCol("买方：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizCustomer.getName(), 6,textFont));

            table.addCell(PdfUtil.mergeCol("签订日期：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(DateUtils.dateTime(bizProcessData.getCreateTime()), 5,textFont));

            table.addCell(PdfUtil.mergeCol("为保障买卖双方的合法权益，根据《合同法》及有关法律规定，买卖双方经友好协商，一致同意按下列条款签订本合同。", 15,textFont));

            table.addCell(PdfUtil.mergeCol("一、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("供货内容：", 14,textFont));

            //第七行 产品数据开始 bizQuotationProducts
            table.addCell(PdfUtil.mergeCol("序号", 1,textFont));
            table.addCell(PdfUtil.mergeCol("名称", 1,textFont));
            table.addCell(PdfUtil.mergeCol("型号", 2,textFont));
            table.addCell(PdfUtil.mergeCol("规格", 1,textFont));
            table.addCell(PdfUtil.mergeCol("数量", 1,textFont));
            table.addCell(PdfUtil.mergeCol("单价", 1,textFont));
            table.addCell(PdfUtil.mergeCol("合计", 1,textFont));
            table.addCell(PdfUtil.mergeCol("材质说明", 7,textFont));

            Double sumTotalNum = new Double(0);
            Double sumTotalPrice = new Double(0);
            Double sumTotalAmount = new Double(0);
            Double sumTotalNumRef1 = new Double(0);
            Double sumTotalNumRef2 = new Double(0);

            DecimalFormat data = new DecimalFormat("#");

            //优惠 string14
            Double string14D = new Double(0);

            for (int i = 0; i < bizProcessChildList.size(); i++) {

                String endRemark = "";

                BizProcessChild bizProduct = bizProcessChildList.get(i);

                string14D += StringUtils.toDouble(bizProduct.getString14());
                table.addCell(PdfUtil.mergeCol("" + (i + 1), 1,textFont));


                String productName = bizProduct.getProductName();
                String model = bizProduct.getModel();
                //执行器计算
                BizActuator bizActuator = bizProduct.getBizActuator();
                Double actuatorTotal = new Double(0);
                if (bizActuator != null) {
                    Double actuatorPrice = bizActuator.getPrice();
                    String actuatorNum = bizProduct.getActuatorNum();
                    String actuatorCoefficient = bizProduct.getActuatorCoefficient();
                    if (StringUtils.isNotEmpty(actuatorNum) && actuatorPrice > 0 && StringUtils.isNotEmpty(actuatorCoefficient)) {
                        actuatorTotal = Double.parseDouble(actuatorNum) * actuatorPrice * Double.parseDouble(actuatorCoefficient);

                        String type = bizActuator.getString2();
                        String repStr = "气动";
                        String appendStr = "6";
                        if ("1".equals(type)) {
                            repStr = "电动";
                            appendStr = "9";
                        }
                        productName = productName.replaceAll("无头",repStr);

                        if (model.startsWith("D")) {
                            model = model.substring(1,model.length());
                            model = "D" + appendStr + model;
                        }
                        endRemark += "执行器";

                    }
                }
                table.addCell(PdfUtil.mergeCol(productName, 1,textFont));


                table.addCell(PdfUtil.mergeCol(model, 2,textFont));
                table.addCell(PdfUtil.mergeCol(bizProduct.getSpecifications(), 1,textFont));//规格


                table.addCell(PdfUtil.mergeCol(bizProduct.getProductNum(), 1,textFont));//数量

                //总价计算
                Double productPrice = bizProduct.getProductPrice();
                String productNum = bizProduct.getProductNum();

                sumTotalNum = sumTotalNum + Double.parseDouble(productNum);

                Double productTotal = new Double(0);
                String productCoefficient = bizProduct.getProductCoefficient();
                if (StringUtils.isNotEmpty(productNum) && productPrice > 0 && StringUtils.isNotEmpty(productCoefficient)) {
                    productTotal = Double.parseDouble(productNum) * productPrice * Double.parseDouble(productCoefficient);
                }
                //法兰计算

                Double ref1Total = new Double(0);
                String ref1Id = bizProduct.getString5();
                if (StringUtils.isNotEmpty(ref1Id)) {
                    Double ref1Price = bizProduct.getPrice2();
                    String ref1Num = bizProduct.getString6();
                    String ref1Coefficient = bizProduct.getProductRef1Coefficient();
                    if (StringUtils.isNotEmpty(ref1Num) && ref1Price > 0 && StringUtils.isNotEmpty(ref1Coefficient)) {
                        ref1Total = Double.parseDouble(ref1Num) * ref1Price * Double.parseDouble(ref1Coefficient);
                        sumTotalNumRef1 = sumTotalNumRef1 + Double.parseDouble(ref1Num);
                        endRemark += "法兰";
                    }
                }
                //螺栓计算
                Double ref2Tota = new Double(0);
                String ref2Id = bizProduct.getString5();
                if (StringUtils.isNotEmpty(ref2Id)) {
                    Double ref2Price = StringUtils.toDouble(bizProduct.getString8());
                    String ref2Num = bizProduct.getString9();
                    String ref2Coefficient = bizProduct.getProductRef2Coefficient();
                    if (StringUtils.isNotEmpty(ref2Num) && ref2Price > 0 && StringUtils.isNotEmpty(ref2Coefficient)) {
                        ref2Tota = Double.parseDouble(ref2Num) * ref2Price * Double.parseDouble(ref2Coefficient);
                        sumTotalNumRef2 = sumTotalNumRef2 + Double.parseDouble(ref2Num);
                        endRemark += "螺栓";
                    }
                }

                //定位器
                Double pattachmentIdTotal = new Double(0);
                Long pattachmentId = bizProduct.getPattachmentId();
                if (pattachmentId != null && pattachmentId > 0L) {
                    Double price = bizProduct.getPattachmentPrice();
                    Double num = bizProduct.getPattachmentCount();
                    Double coefficient = bizProduct.getPattachmentCoefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentIdTotal = price * num * coefficient;
                        endRemark += "定位器";
                    }
                }

                Double pattachmentId1Total = new Double(0);
                Long pattachment1Id = bizProduct.getPattachment1Id();
                if (pattachment1Id != null && pattachment1Id > 0L) {
                    Double price = bizProduct.getPattachment1Price();
                    Double num = bizProduct.getPattachment1Count();
                    Double coefficient = bizProduct.getPattachment1Coefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentId1Total = price * num * coefficient;
                        endRemark += "电磁阀";
                    }
                }

                Double pattachmentId2Total = new Double(0);
                Long pattachment2Id = bizProduct.getPattachment2Id();
                if (pattachment2Id != null && pattachment2Id > 0L) {
                    Double price = bizProduct.getPattachment2Price();
                    Double num = bizProduct.getPattachment2Count();
                    Double coefficient = bizProduct.getPattachment2Coefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentId2Total = price * num * coefficient;
                        endRemark += "回信器数";
                    }
                }


                Double pattachmentId3Total = new Double(0);
                Long pattachment3Id = bizProduct.getPattachment3Id();
                if (pattachment3Id != null && pattachment3Id > 0L) {
                    Double price = bizProduct.getPattachment3Price();
                    Double num = bizProduct.getPattachment3Count();
                    Double coefficient = bizProduct.getPattachment3Coefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentId3Total = price * num * coefficient;
                        endRemark += "气源三连件";
                    }
                }

                Double pattachmentId4Total = new Double(0);
                Long pattachment4Id = bizProduct.getPattachment4Id();
                if (pattachment4Id != null && pattachment4Id > 0L) {
                    Double price = bizProduct.getPattachment4Price();
                    Double num = bizProduct.getPattachment4Count();
                    Double coefficient = bizProduct.getPattachment4Coefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentId4Total = price * num * coefficient;
                        endRemark += "可离合减速器";
                    }
                }



                Double totalAmount = new Double(0);
                totalAmount = productTotal + ref1Total + ref2Tota + actuatorTotal +
                        pattachmentIdTotal + pattachmentId1Total + pattachmentId2Total + pattachmentId3Total + pattachmentId4Total;

                sumTotalAmount = sumTotalAmount + totalAmount;

                //总单价
                Double productTotalPrice = Double.valueOf(totalAmount / Double.parseDouble(productNum));
                sumTotalPrice = sumTotalPrice + productTotalPrice;
                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(productTotalPrice), 1,textFont));//单价

                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(totalAmount), 1,textFont));//合计



                String startRemark = "";
                if (StringUtils.isNotEmpty(bizProduct.getValvebodyMaterial())) {startRemark += bizProduct.getValvebodyMaterial() + ",";}
                //if (StringUtils.isNotEmpty(bizProduct.getValveElement())) {startRemark += bizProduct.getValveElement() + ",";}
                if (StringUtils.isNotEmpty(bizProduct.getSealingMaterial())) {startRemark += bizProduct.getSealingMaterial() + ",";}
                if (StringUtils.isNotEmpty(bizProduct.getDriveForm())) {startRemark += bizProduct.getDriveForm() + ",";}
                if (StringUtils.isNotEmpty(bizProduct.getConnectionType())) {startRemark += bizProduct.getConnectionType() + ",";}
                if (StringUtils.isNotEmpty(bizProduct.getString15())) {startRemark += bizProduct.getString15() + ",";}
                if (startRemark.length() > 1) {
                    startRemark = startRemark.substring(0,startRemark.length() - 1);
                }
                table.addCell(PdfUtil.mergeCol(startRemark + " 含" + endRemark, 7,textFont));
            }


            //金额合计
            table.addCell(PdfUtil.mergeColRight("合计", 5,textFont));//4
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalNum), 1,textFont));//总数量
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalPrice), 1,textFont));//单价
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalAmount), 1,textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 7,textFont));//备注


            table.addCell(PdfUtil.mergeColRight("优惠价", 5,textFont));//4
            table.addCell(PdfUtil.mergeCol("", 1,textFont));//总数量
            table.addCell(PdfUtil.mergeCol("", 1,textFont));//单价
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(string14D), 1,textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 7,textFont));//备注



            table.addCell(PdfUtil.mergeColRight("大写人民币合计", 5,textFont));
            table.addCell(PdfUtil.mergeCol(StringUtils.convert(sumTotalAmount), 3,textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 7,textFont));//备注




            table.addCell(PdfUtil.mergeCol("二、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("特殊要求：220系列蝶阀的安装法兰必须选用承插焊法兰", 14,textFont));


            table.addCell(PdfUtil.mergeCol("三、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("产品执行标准；好利阀业有限公司生产标准，符合国家及行业标准；产品提供安装使用说明书，产品合格证；产品标识：好利标牌", 14,textFont));


            table.addCell(PdfUtil.mergeCol("四、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("产品验收标准：按国家标准验收。", 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("付款及运输：电汇结算，款到发货；货物采用纸箱包装，采用市内送货运输，运输费用卖方承担", 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("1、交货周期：合同签定后5个工作日发货（若未当日回传，发货期则从收到回传之日延后）", 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("2、收  货  人：" + StringUtils.trim(remark5), 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("3、交货地点：北京顺义区赵全营顺华峰商贸公司院内", 14,textFont));

            table.addCell(PdfUtil.mergeCol("五、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("质量保证按国家标准执行：质保期12个月（自出厂日算起）；质保期内如因产品本身质量问题，卖方予以免费更换。", 14,textFont));

            table.addCell(PdfUtil.mergeCol("六、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("违约责任：合同签订后，买卖双方严格执行双方所签订合同的条款，其中一方不履行或不完全履行合同者应承担相应的法律责任；解决合同纠纷方式：双方协商解决，解决不成由卖方所在北京仲裁委员会仲裁。", 14,textFont));

            table.addCell(PdfUtil.mergeCol("七、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("本合同一式贰份。双方各执一份，双方签字盖章后生效（传真件有效）。", 14,textFont));




            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("单位名称：" + companyName + "", 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("单位名称：" + StringUtils.trim(bizCustomer.getName()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("单位地址：北京大兴区榆垡镇榆顺路6号", 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("单位地址："  + StringUtils.trim(bizCustomer.getCompanyAddress()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("委托代理人：", 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("委托代理人：", 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("电    话：" + StringUtils.trim(remark6), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("传    真：" + StringUtils.trim(bizCustomer.getFax()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("开户银行：" + StringUtils.trim(remark7), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("开户银行：" + StringUtils.trim(bizCustomer.getString11()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("帐    号：" + StringUtils.trim(remark8), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("帐    号：" + StringUtils.trim(bizCustomer.getString12()), 7,textFont));

            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("税    号："  + StringUtils.trim(remark9), 7,textFont));
            table.addCell(PdfUtil.mergeColLeft("税    号：" + StringUtils.trim(bizCustomer.getString13()), 7,textFont));


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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Paragraph datePar = new Paragraph("打印日期：" + sdf.format(new Date()), PdfUtil.getPdfChineseFont());
            datePar.setAlignment(Element.ALIGN_RIGHT);
            datePar.setSpacingBefore(20);



            document.open();
            document.add(paragraph0);
            document.add(paragraph);
            //document.add(blankRow);
            //document.add(paragraph1);
            //document.add(blankRow);
            document.add(tbSubtitle);

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





    @GetMapping("/viewPdf")
    public void viewPdf(HttpServletRequest request,HttpServletResponse response) {
        createPdf(request,response,null);
    }


}
