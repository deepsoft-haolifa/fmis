package com.ruoyi.fmis.data.controller;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.config.RedisUtil;
import com.ruoyi.common.constant.Constants;
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
import com.ruoyi.fmis.common.BizProductExcel;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.pattachment.domain.BizProductAttachment;
import com.ruoyi.fmis.pattachment.service.IBizProductAttachmentService;
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

    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private IBizProductAttachmentService bizProductAttachmentService;

    @Autowired
    private RedisUtil redisUtil;


    @RequiresPermissions("fmis:data:view")
    @GetMapping()
    public String data(ModelMap mmap) {
        String toDo = getRequest().getParameter("todo");
        if ("1".equals(toDo)) {
            mmap.put("todo","1");
        }
        return prefix + "/data";
    }
    @GetMapping("/produce")
    public String produce(ModelMap mmap) {
        String toDo = getRequest().getParameter("todo");
        if ("1".equals(toDo)) {
            mmap.put("todo","1");
        }
        return prefix + "/dataProduce";
    }
    @GetMapping("/liuzhuan")
    public String liuzhuan(ModelMap mmap) {
        String toDo = getRequest().getParameter("todo");
        if ("1".equals(toDo)) {
            mmap.put("todo","1");
        }
        return prefix + "/dataLiu";
    }
    @GetMapping("/applyDeliver")
    public String applyDeliver(ModelMap mmap) {
        String toDo = getRequest().getParameter("todo");
        if ("1".equals(toDo)) {
            mmap.put("todo","1");
        }
        return prefix + "/applyDeliver";
    }
    /**
     * 查询合同管理列表
     */
    //@RequiresPermissions("fmis:data:list")
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
        //临时用userId
        bizProcessData.setString30(ShiroUtils.getUserId() + "");

        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRef(bizProcessData);


        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                String flowStatus = data.getFlowStatus();
                //结束标识
                String normalFlag = data.getNormalFlag();
                String flowStatusRemark = "待上报";
                data.setLoginUserId(ShiroUtils.getUserId().toString());
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
    /**
     * 已流转的合同
     */
    //@RequiresPermissions("fmis:data:list")
    @PostMapping("/listLiu")
    @ResponseBody
    public TableDataInfo listLiu(BizProcessData bizProcessData) {

        bizProcessData.setBizId("contract");
        bizProcessData.setString13("2");
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefLiu(bizProcessData);

        return getDataTable(list);
    }

    /**
     * 查询合同管理列表
     */
    //@RequiresPermissions("fmis:data:listForProduce")
    @PostMapping("/listForProduce")
    @ResponseBody
    public TableDataInfo listForProduce(BizProcessData bizProcessData) {

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
        //临时用userId
        bizProcessData.setString30(ShiroUtils.getUserId() + "");
        bizProcessData.setRoleType("0");//表示可以查看所有的
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRef(bizProcessData);


        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                String flowStatus = data.getFlowStatus();
                //结束标识
                String normalFlag = data.getNormalFlag();
                String flowStatusRemark = "待上报";
                data.setLoginUserId(ShiroUtils.getUserId().toString());
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
                    if (bizQuotation != null) {
                        productNames += bizQuotation.getString1() + ",";
                        bizProcessChild.setBizQuotation(bizQuotation);
                    }

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

    @GetMapping("/viewInventory")
    public String viewInventory(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/viewInventory";
    }
    @GetMapping("/viewDeliver")
    public String viewDeliver(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        String bizId = bizProcessData.getBizId();
        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        String userFlowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            userFlowStatus = flowMap.keySet().iterator().next();
            bizProcessData.setRoleType(userFlowStatus);
        }

        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildListForKuCun(queryBizProcessChild);
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
        mmap.put("userFlowStatus", userFlowStatus);
        return prefix + "/viewDeliver";
    }

    @GetMapping("/viewDetail")
    public String viewDetail(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));


        String bizId = bizProcessData.getBizId();
        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        String userFlowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            userFlowStatus = flowMap.keySet().iterator().next();
            bizProcessData.setRoleType(userFlowStatus);
        }

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
        mmap.put("userFlowStatus", userFlowStatus);
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
        String string6 = getRequest().getParameter("string6");
        mmap.put("customerId",customerId);
        mmap.put("string6",string6);
        return prefix + "/selectNewQuotation";
    }

    @GetMapping("/selectNewProduct")
    public String selectNewProduct(ModelMap mmap) {
        String customerId = getRequest().getParameter("customerId");
        mmap.put("customerId",customerId);

        mmap.put("seriesSelect",bizDictService.selectBizDictByProductType(BizConstants.productTypeCode));
        mmap.put("suppliers",bizSuppliersService.selectAllList());

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

        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizProcessData.getBizId());
        if (!CollectionUtils.isEmpty(flowAllMap)) {
            for (String key : flowAllMap.keySet()) {
                bizProcessData.setNormalFlag(key);
            }
        }
        String conNo = "XS-" + DateUtils.dateTimeNow();
        bizProcessData.setString1(conNo);
        setNormalFlag(bizProcessData,productArrayStr);
        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);
        Long dataId = bizProcessData.getDataId();
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                if (StringUtils.isNotEmpty(bizProcessChild.getString2())) {
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

    /**
     * 查询报价单产品
     * @return
     */
    @PostMapping("/listProductNoPageFH")
    @ResponseBody
    public TableDataInfo listProductNoPageFH(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizQuotationProductList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }
    @PostMapping("/listProductChild")
    @ResponseBody
    public TableDataInfo listProductChild(BizProcessData bizProcessData) {
        //@RequestParam String dataId,@RequestParam String level
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setString2(bizProcessData.getLevel());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }



    @PostMapping("/listLevelProduct")
    @ResponseBody
    public TableDataInfo listLevelProduct(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelProduct(bizProcessData);
    }

    @PostMapping("/listLevelActuator")
    @ResponseBody
    public TableDataInfo listLevelActuator(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelActuator(bizProcessData);
    }

    @PostMapping("/listLevelRef1")
    @ResponseBody
    public TableDataInfo listLevelRef1(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelRef1(bizProcessData);
    }

    @PostMapping("/listLevelRef2")
    @ResponseBody
    public TableDataInfo listLevelRef2(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelRef2(bizProcessData);
    }

    @PostMapping("/listLevelPA")
    @ResponseBody
    public TableDataInfo listLevelPA(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelPA(bizProcessData);
    }

    @PostMapping("/listLevelPA1")
    @ResponseBody
    public TableDataInfo listLevelPA1(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelPA1(bizProcessData);
    }
    @PostMapping("/listLevelPA2")
    @ResponseBody
    public TableDataInfo listLevelPA2(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelPA2(bizProcessData);
    }
    @PostMapping("/listLevelPA3")
    @ResponseBody
    public TableDataInfo listLevelPA3(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelPA3(bizProcessData);
    }
    @PostMapping("/listLevelPA4")
    @ResponseBody
    public TableDataInfo listLevelPA4(BizProcessData bizProcessData) {
        return bizProcessDataService.listLevelPA4(bizProcessData);
    }
    @PostMapping("/listLevel")
    @ResponseBody
    public TableDataInfo listLevel(BizProcessData bizProcessData) {
        //@RequestParam String dataId,@RequestParam String dataStatus
        return getDataTable(bizProcessDataService.listLevel(bizProcessData));
    }
    @PostMapping("/listLevelS")
    @ResponseBody
    public TableDataInfo listLevelS(BizProcessData bizProcessData) {
        //@RequestParam String dataId,@RequestParam String dataStatus
        return getDataTable(bizProcessDataService.listLevelS(bizProcessData));
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
     * 保存 发货
     */
    @Log(title = "发货保存", businessType = BusinessType.UPDATE)
    @PostMapping("/saveInventory")
    @ResponseBody
    @Transactional
    public AjaxResult saveInventory(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizQuotationProductList(queryBizProcessChild);

        String productArrayStr = bizProcessData.getProductParmters();
        String[] childs = productArrayStr.split(",");
        String string1 = bizProcessData.getString1();
        String string2 = DateUtils.dateTimeNow();//发货通知单号
        //BizProcessData newData = new BizProcessData();
        bizProcessData.setString1(string1);
        bizProcessData.setString2(string2);
        bizProcessData.setBizId(BizConstants.BIZ_newdelivery);
        bizProcessData.setNormalFlag("4");
        bizProcessData.setFlowStatus("2");


        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);

        //自动上报
        bizProcessDataService.doExamine(bizProcessData.getDataId() + "","1","销售员上报",bizProcessData.getBizId());

        for (BizProcessChild bizProcessChild : bizProcessChildList) {
            if (!Arrays.asList(childs).contains(bizProcessChild.getChildId() + "")) {
                continue;
            }
            //已申请发货
            bizProcessChild.setString20("1");
            bizProcessChildService.updateBizProcessChildFH(bizProcessChild);
            String childId = bizProcessChild.getChildId() + "";
            bizProcessChild.setDataId(bizProcessData.getDataId());
            bizProcessChild.setChildId(null);
            bizProcessChild.setString14("0");
            //setDataId 销售合同id String1 合同子表中的id，sting2 类型 1=产品 2=执行器 3=法兰 4=螺栓 5=定位器 6=电磁阀 7=回信器数 8=气源三连件 9=可离合减速器
            //string3 报检单号 string4 采购合同 string5 销售合同 string6 各种名称 string7 型号 string8规格 string13 出库数量 string11 string15 报检合格数的id也就是库存id

            /*if ("1".equals(busType)) {
                type = "1";
                BizProduct queryBizProduct = new BizProduct();
                queryBizProduct.setProductId(Long.parseLong(paramterId));
                BizProduct bizProduct = bizProductService.selectBizProductList(queryBizProduct).get(0);

                insertChild.setString6(bizProduct.getName());
                insertChild.setString7(bizProduct.getModel());
                insertChild.setString8(bizProduct.getSpecifications());
                insertChild.setString9(bizProduct.getString2());
                insertChild.setString10(StringUtils.getDoubleString0(bizProduct.getProcurementPrice()));
            } else if ("2".equals(busType)) {
                type = "2";
                BizActuator queryActuator = new BizActuator();
                queryActuator.setActuatorId(Long.parseLong(paramterId));
                BizActuator bizActuator = bizActuatorService.selectBizActuatorForRefList(queryActuator).get(0);

                insertChild.setString6(bizActuator.getName());
                insertChild.setString7(bizActuator.getString1());
                //insertChild.setString8(bizProduct.getSpecifications());
                insertChild.setString9(bizActuator.getQualityLevel());
                insertChild.setString10(bizActuator.getString6());

            } else if ("3".equals(busType)) {
                type = "3";
                BizProductRef queryRef1 = new BizProductRef();
                queryRef1.setProductRefId(Long.parseLong(paramterId));
                BizProductRef bizProductRef = bizProductRefService.selectBizProductRefList(queryRef1).get(0);

                insertChild.setString6(bizProductRef.getName());
                insertChild.setString7(bizProductRef.getModel());
                insertChild.setString8(bizProductRef.getSpecifications());
                insertChild.setString9(bizProductRef.getString1());
                insertChild.setString10(bizProductRef.getString2());

            } else if ("4".equals(busType)) {
                type = "4";

                BizProductRef queryRef1 = new BizProductRef();
                queryRef1.setProductRefId(Long.parseLong(paramterId));
                BizProductRef bizProductRef = bizProductRefService.selectBizProductRefList(queryRef1).get(0);

                insertChild.setString6(bizProductRef.getName());
                insertChild.setString7(bizProductRef.getModel());
                insertChild.setString8(bizProductRef.getSpecifications());
                insertChild.setString9(bizProductRef.getString1());
                insertChild.setString10(bizProductRef.getString2());

            } else {
                type = "5";
                BizProductAttachment queryProductAttachment = new BizProductAttachment();
                queryProductAttachment.setAttachmentId(Long.parseLong(paramterId));
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentById(Long.parseLong(paramterId));

                insertChild.setString6(bizProductAttachment.getChineseName());
                //insertChild.setString7(bizProductAttachment.getModel());
                insertChild.setString8(bizProductAttachment.getChineseSpecifications());
                insertChild.setString9(bizProductAttachment.getString1());
                insertChild.setString10(StringUtils.getDoubleString0(bizProductAttachment.getSettlementPrice()));

            }*/



            //产品
            if (StringUtils.toLong(bizProcessChild.getProductNum()) != 0) {
                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("1");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProcessChild.getProductName());
                bizProcessChild1.setString7(bizProcessChild.getModel());
                bizProcessChild1.setString8(bizProcessChild.getSpecifications());
                bizProcessChild1.setString13(bizProcessChild.getProductNum());
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }
            //执行器
            if (StringUtils.toLong(bizProcessChild.getActuatorNum()) != 0) {
                BizActuator queryActuator = new BizActuator();
                queryActuator.setActuatorId(Long.parseLong(bizProcessChild.getActuatorId()));
                BizActuator bizActuator = bizActuatorService.selectBizActuatorForRefList(queryActuator).get(0);
                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("2");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProcessChild.getActuatorName());
                //执行器id
                bizProcessChild1.setString7(bizActuator.getString1());
                bizProcessChild1.setString13(bizProcessChild.getActuatorNum());
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }

            //法兰
            if (bizProcessChild.getProductRef1Num() != 0) {
                BizProductRef queryRef1 = new BizProductRef();
                queryRef1.setProductRefId(Long.parseLong(bizProcessChild.getProductRef1Id()));
                BizProductRef bizProductRef = bizProductRefService.selectBizProductRefList(queryRef1).get(0);
                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("3");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProcessChild.getRef1Name());
                bizProcessChild1.setString7(bizProductRef.getModel());
                bizProcessChild1.setString8(bizProductRef.getSpecifications());
                bizProcessChild1.setString13(bizProcessChild.getProductRef1Num() + "");
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }

            //螺栓
            if (bizProcessChild.getProductRef2Num() != 0) {
                BizProductRef queryRef1 = new BizProductRef();
                queryRef1.setProductRefId(Long.parseLong(bizProcessChild.getProductRef1Id()));
                BizProductRef bizProductRef = bizProductRefService.selectBizProductRefList(queryRef1).get(0);
                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("4");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProcessChild.getRef1Name());
                bizProcessChild1.setString7(bizProductRef.getModel());
                bizProcessChild1.setString8(bizProductRef.getSpecifications());
                bizProcessChild1.setString13(bizProcessChild.getProductRef1Num() + "");
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }

            //定位器
            if (bizProcessChild.getPattachmentCount() != null && bizProcessChild.getPattachmentCount() != 0) {
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentById(bizProcessChild.getPattachmentId());

                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("5");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProductAttachment.getChineseName());
                bizProcessChild1.setString8(bizProductAttachment.getChineseSpecifications());
                bizProcessChild1.setString13(bizProcessChild.getPattachmentCount() + "");
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }

            //电磁阀
            if (bizProcessChild.getPattachment1Count() != null && bizProcessChild.getPattachment1Count() != 0) {
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentById(bizProcessChild.getPattachment1Id());

                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("6");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProductAttachment.getChineseName());
                bizProcessChild1.setString8(bizProductAttachment.getChineseSpecifications());
                bizProcessChild1.setString13(bizProcessChild.getPattachment1Count() + "");
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }

            //回信器数
            if (bizProcessChild.getPattachment2Count() != null &&bizProcessChild.getPattachment2Count() != 0) {
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentById(bizProcessChild.getPattachment2Id());

                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("7");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProductAttachment.getChineseName());
                bizProcessChild1.setString8(bizProductAttachment.getChineseSpecifications());
                bizProcessChild1.setString13(bizProcessChild.getPattachment2Count() + "");
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }

            //气源三连件
            if (bizProcessChild.getPattachment3Count() != null && bizProcessChild.getPattachment3Count() != 0) {
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentById(bizProcessChild.getPattachment3Id());

                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("8");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProductAttachment.getChineseName());
                bizProcessChild1.setString8(bizProductAttachment.getChineseSpecifications());
                bizProcessChild1.setString13(bizProcessChild.getPattachment3Count() + "");
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }
//            可离合减速器
            if (bizProcessChild.getPattachment4Count() != null && bizProcessChild.getPattachment4Count() != 0) {
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentById(bizProcessChild.getPattachment4Id());

                BizProcessChild bizProcessChild1 = new BizProcessChild();
                bizProcessChild1.setDataId(bizProcessChild.getDataId());
                bizProcessChild1.setString2("9");
                bizProcessChild1.setString5(bizProcessChild.getContractNo());
                bizProcessChild1.setString6(bizProductAttachment.getChineseName());
                bizProcessChild1.setString8(bizProductAttachment.getChineseSpecifications());
                bizProcessChild1.setString13(bizProcessChild.getPattachment4Count() + "");
                bizProcessChild1.setString15(childId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild1);
            }

        }

      /*  if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                if (StringUtils.isNotEmpty(bizProcessChild.getString2())) {
                    bizProcessChild.setDataId(bizProcessData.getDataId());
                    bizProcessChild.setChildId(null);
                    bizProcessChild.setString14("0");
                    bizProcessChildService.insertBizProcessChild(bizProcessChild);

                    //减去 库存数
                    String inventoryChildId = bizProcessChild.getString15();
                    BizProcessChild inventoryChild = bizProcessChildService.selectBizProcessChildById(Long.parseLong(inventoryChildId));

                    String string16 = inventoryChild.getString16();
                    inventoryChild.setString16(StringUtils.toLong(string16) + StringUtils.toLong(bizProcessChild.getString13()) + "");
                    //inventoryChild 之前的报检的数据
                    //bizProcessChild发货的数据
                    inventoryChild.setString11((StringUtils.toLong(inventoryChild.getString11()) - StringUtils.toLong(bizProcessChild.getString13())) + "");
                    inventoryChild.setUpdateTime(new Date());
                    inventoryChild.setUpdateBy(ShiroUtils.getUserId() + "");
                    inventoryChild.setString14("1");
                    bizProcessChildService.updateBizProcessChild(inventoryChild);
                }
            }
        }*/
        return toAjax(insertReturn);
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
                if (StringUtils.isNotEmpty(bizProcessChild.getString2())) {
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
            String payType = bizProcessData.getString18();
            String payType1 = bizProcessData.getString8();

            String string15 = bizProcessData.getString15();//账期
            String string17 = bizProcessData.getString17();//账期天数
            String string26 = bizProcessData.getString26();//质保期
            if (StringUtils.isEmpty(string26)) {
                string26 = "0";
            }
            if ("1".equals(payType) && ("1".equals(payType1) || "2".equals(payType1) || "3".equals(payType1) || "4".equals(payType1))) {
                //预付款	预付（0，20,30,50）%，发货前付至100%	销售人员	销售经理
                normalFlag = "2";
            } else if ("2".equals(payType) && ("1".equals(string15) || "2".equals(string15))) {
                //货到□/票到□30/45天 销售总经理
                normalFlag = "3";
            } else if ("2".equals(payType) && ("3".equals(string15))) {
                //货到□/票到□60 天（可选项30天/45天/60天/90天/其它）
                normalFlag = "4";
            } else if ("2".equals(payType) && ("4".equals(string15) || "5".equals(string15))) {
                //货到□/票到□90 天/其它
                normalFlag = "5";
            } else if ("3".equals(payType)) {
                //协议付款
                normalFlag = "5";
                //price6=发货前付款
                //price7=货到款天数
                //price11=质保金
                //price9=调试款
                /**
                 * 发货前付款低于65%；货到款超过90天；质保金超过10%；质保期超过12个月
                 * 发货前付款低于65%；货到款、调试款超过90天；质保金超过10%；质保期超过12个月  副总
                 */
                Double price6 = bizProcessData.getPrice6() == null ? 0 : bizProcessData.getPrice6();
                Double price7 = bizProcessData.getPrice7() == null ? 0 : bizProcessData.getPrice7();
                Double price11 = bizProcessData.getPrice11() == null ? 0 : bizProcessData.getPrice11();
                Double price9 = bizProcessData.getPrice9() == null ? 0 : bizProcessData.getPrice9();
                if (price6 < 65 || price7 > 90 || price11 > 10 || Integer.parseInt(string26) > 12 || price9 > 90) {
                    normalFlag = "4";
                }




            } else {
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
                if (minCoefficient < 0.88 || (minOtherCoefficient > 0 && minOtherCoefficient < 0.92)) {
                    normalFlag = "5";
                }else if ((minCoefficient >= 0.88 && minCoefficient < 0.95) || (minOtherCoefficient >= 0.92 && minOtherCoefficient < 0.97)) {
                    normalFlag = "4";
                } else if ((minCoefficient >= 0.95 && minCoefficient < 1) || (minOtherCoefficient >= 0.97 && minOtherCoefficient <= 1)) {
                    normalFlag = "3";
                } else {
                    normalFlag = "2";
                }
            }
        }
        bizProcessData.setNormalFlag(normalFlag);
        /**
         * normalFlag 先把除报价员的权限范围做了
         *
         * 1=销售 2=销售经理 3=区域经理 4=副总 5=总经理
         */
        int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
        if (roleType > 1) {
            if (normalFlag.equals(roleType + "")) {
//                bizQuotation.setNormalFlag(normalFlag);
                bizProcessData.setFlowStatus(normalFlag);
            } else {
                bizProcessData.setFlowStatus(roleType + "");
            }
        }
        //如果高级别创建的不需要再高级别审批的直接同意
        if (roleType >=  Integer.parseInt(normalFlag)) {
            bizProcessData.setFlowStatus(roleType + "");
            bizProcessData.setNormalFlag(roleType + "");
        }
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
    //流转到合同审理员
    @PostMapping("/goPool")
    @ResponseBody
    public AjaxResult goPool() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        bizQuotation.setString13("2");
        bizQuotation.setUpdateBy(ShiroUtils.getUserId().toString());
        return toAjax(bizProcessDataService.updateBizProcessData(bizQuotation));
    }
    //流转到采购池
    @PostMapping("/goPoolLiu")
    @ResponseBody
    public AjaxResult goPoolLiu() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        bizQuotation.setString13("1");
        bizQuotation.setUpdateBy(ShiroUtils.getUserId().toString());
        return toAjax(bizProcessDataService.updateBizProcessData(bizQuotation));
    }
    //回退到重新上传合同
    @PostMapping("/unPoolLiu")
    @ResponseBody
    public AjaxResult unPoolLiu() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        bizQuotation.setString13("0");
        bizQuotation.setUpdateBy(ShiroUtils.getUserId().toString());
        return toAjax(bizProcessDataService.updateBizProcessData(bizQuotation));
    }
    @PostMapping("/uploadUrl")
    @ResponseBody
    public AjaxResult uploadUrl() {
        String dataId = getRequest().getParameter("dataId");
        String url = getRequest().getParameter("url");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        bizProcessData.setString20(url);
        //做为流转状态的标示
        bizProcessData.setString13("4");
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
     * 选择系统用户
     */
    @GetMapping("/selectActuator")
    public String selectActuator(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        mmap.put("productId", productId);
        return prefix + "/selectActuator";
    }


    /**
     * 选择产品配件法兰
     */
    @GetMapping("/selectProductRef1")
    public String selectProductRef1(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        mmap.put("productId", productId);
        return prefix + "/selectProductRef1";
    }
    /**
     * 选择产品配件螺栓
     */
    @GetMapping("/selectProductRef2")
    public String selectProductRef2(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        mmap.put("productId", productId);
        return prefix + "/selectProductRef2";
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
            if (bizProcessDataParamter.getDataId() != null) {
                id = bizProcessDataParamter.getDataId().toString();
            } else {
                id = request.getParameter("id");
            }

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

                String actuatorId = bizProduct.getActuatorId();
                BizActuator bizActuator = null;
                if (StringUtils.isNotEmpty(actuatorId)) {
                    bizActuator = bizActuatorService.selectBizActuatorById(Long.parseLong(actuatorId));
                }

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
                        if (endRemark.length()>0) {
                            endRemark += ",";
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
                String ref1Id = bizProduct.getProductRef1Id();
                if (StringUtils.isNotEmpty(ref1Id)) {
                    Double ref1Price = bizProduct.getPrice2();
                    String ref1Num = bizProduct.getString6();
                    String ref1Coefficient = bizProduct.getProductRef1Coefficient();
                    if (StringUtils.isNotEmpty(ref1Num) && ref1Price!= null && ref1Price > 0 && StringUtils.isNotEmpty(ref1Coefficient)) {
                        ref1Total = Double.parseDouble(ref1Num) * ref1Price * Double.parseDouble(ref1Coefficient);
                        sumTotalNumRef1 = sumTotalNumRef1 + Double.parseDouble(ref1Num);
                    }
                    if (endRemark.length()>0) {
                        endRemark += ",";
                    }
                    endRemark += "法兰";
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
                    }
                    if (endRemark.length()>0) {
                        endRemark += ",";
                    }
                    endRemark += "螺栓";
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

                    }
                    if (endRemark.length()>0) {
                        endRemark += ",";
                    }
                    endRemark += "定位器";
                }

                Double pattachmentId1Total = new Double(0);
                Long pattachment1Id = bizProduct.getPattachment1Id();
                if (pattachment1Id != null && pattachment1Id > 0L) {
                    Double price = bizProduct.getPattachment1Price();
                    Double num = bizProduct.getPattachment1Count();
                    Double coefficient = bizProduct.getPattachment1Coefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentId1Total = price * num * coefficient;
                    }
                    if (endRemark.length()>0) {
                        endRemark += ",";
                    }
                    endRemark += "电磁阀";
                }

                Double pattachmentId2Total = new Double(0);
                Long pattachment2Id = bizProduct.getPattachment2Id();
                if (pattachment2Id != null && pattachment2Id > 0L) {
                    Double price = bizProduct.getPattachment2Price();
                    Double num = bizProduct.getPattachment2Count();
                    Double coefficient = bizProduct.getPattachment2Coefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentId2Total = price * num * coefficient;
                    }
                    if (endRemark.length()>0) {
                        endRemark += ",";
                    }
                    endRemark += "回信器数";
                }


                Double pattachmentId3Total = new Double(0);
                Long pattachment3Id = bizProduct.getPattachment3Id();
                if (pattachment3Id != null && pattachment3Id > 0L) {
                    Double price = bizProduct.getPattachment3Price();
                    Double num = bizProduct.getPattachment3Count();
                    Double coefficient = bizProduct.getPattachment3Coefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentId3Total = price * num * coefficient;
                    }
                    if (endRemark.length()>0) {
                        endRemark += ",";
                    }
                    endRemark += "气源三连件";
                }

                Double pattachmentId4Total = new Double(0);
                Long pattachment4Id = bizProduct.getPattachment4Id();
                if (pattachment4Id != null && pattachment4Id > 0L) {
                    Double price = bizProduct.getPattachment4Price();
                    Double num = bizProduct.getPattachment4Count();
                    Double coefficient = bizProduct.getPattachment4Coefficient();
                    if (price > 0 && num > 0 && coefficient > 0) {
                        pattachmentId4Total = price * num * coefficient;
                    }
                    if (endRemark.length()>0) {
                        endRemark += ",";
                    }
                    endRemark += "可离合减速器";
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
                if (StringUtils.isNotEmpty(bizProduct.getValvebodyMaterial())) {startRemark += "阀体材质：" + bizProduct.getValvebodyMaterial() + ",";}
                //if (StringUtils.isNotEmpty(bizProduct.getValveElement())) {startRemark += bizProduct.getValveElement() + ",";}
                if (StringUtils.isNotEmpty(bizProduct.getSealingMaterial())) {startRemark += "密封材质：" + bizProduct.getSealingMaterial() + ",";}
                if (StringUtils.isNotEmpty(bizProduct.getDriveForm())) {startRemark += "驱动形式：" + bizProduct.getDriveForm() + ",";}
                if (StringUtils.isNotEmpty(bizProduct.getConnectionType())) {startRemark += "连接方式：" + bizProduct.getConnectionType() + ",";}
                if (StringUtils.isNotEmpty(bizProduct.getString15())) {startRemark += "颜色：" +bizProduct.getString15() + ",";}
                if (startRemark.length() > 1) {
                    startRemark = startRemark.substring(0,startRemark.length() - 1);
                }
                table.addCell(PdfUtil.mergeCol(startRemark + " 含" + endRemark, 7,textFont));
            }


            //金额合计
            table.addCell(PdfUtil.mergeColRight("合计", 5,textFont));//4
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalNum), 1,textFont));//总数量
            table.addCell(PdfUtil.mergeCol("", 1,textFont));//单价
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalAmount), 1,textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 7,textFont));//备注

            if (string14D > 0) {
                table.addCell(PdfUtil.mergeColRight("优惠价", 5,textFont));//4
                table.addCell(PdfUtil.mergeCol("", 1,textFont));//总数量
                table.addCell(PdfUtil.mergeCol("", 1,textFont));//单价
                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(string14D), 1,textFont));//合计
                table.addCell(PdfUtil.mergeCol("", 7,textFont));//备注
            }

            table.addCell(PdfUtil.mergeColRight("大写人民币合计", 5,textFont));
            table.addCell(PdfUtil.mergeCol(StringUtils.convert(sumTotalAmount), 3,textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 7,textFont));//备注




            table.addCell(PdfUtil.mergeCol("二、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("特殊要求：" + StringUtils.trim(bizProcessData.getString25()), 14,textFont));
           if (bizProcessDataParamter != null && bizProcessDataParamter.getString27() != null) {
               table.addCell(PdfUtil.mergeCol("", 1,textFont));
               table.addCell(PdfUtil.mergeColLeft("生产要求：" + StringUtils.trim(bizProcessData.getString27()), 14,textFont));

           }

            table.addCell(PdfUtil.mergeCol("三、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("产品执行标准；好利阀业有限公司生产标准，符合国家及行业标准；产品提供安装使用说明书，产品合格证；产品标识：好利标牌", 14,textFont));


            table.addCell(PdfUtil.mergeCol("四、", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("产品验收标准：按国家标准验收。", 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            //电汇结算，款到发货；货物采用纸箱包装，采用市内送货运输，运输费用卖方承担

            String payRemark = "";
            String string18 = StringUtils.trim(bizProcessData.getString18());
            String string7 = StringUtils.trim(bizProcessData.getString7());

            String paymentType = dictDataService.selectDictLabel("payment_type",string18);
            String transportType = dictDataService.selectDictLabel("transport_type",string7);

            if (StringUtils.isNotEmpty(paymentType)) {
                payRemark += paymentType;
                if ("1".equals(string18)) {
                    //预付
                    String string8 = StringUtils.trim(bizProcessData.getString8());
                    if (StringUtils.isNotEmpty(string8)) {
                        String string8Name = dictDataService.selectDictLabel("payment_method",string8);
                        payRemark += " : " + string8Name;
                    }

                } else if ("2".equals(string18)) {
                    //账期
                    String string15 = StringUtils.trim(bizProcessData.getString15());
                    if (StringUtils.isNotEmpty(string15)) {
                        String string15Name = dictDataService.selectDictLabel("payment_days",string15);
                        payRemark += " : " + string15Name + " " + StringUtils.trim(bizProcessData.getString17()) + " 天";
                    }

                } else if ("3".equals(string18)) {
                    //协议付款
                    payRemark += " : 预付" + StringUtils.getDoubleString0(bizProcessData.getPrice5()) + " % ";
                    payRemark += "发货前付款" + StringUtils.getDoubleString0(bizProcessData.getPrice6()) + " % ";
                    payRemark += "货到" + StringUtils.getDoubleString0(bizProcessData.getPrice7()) + " 天付 ";
                    payRemark += StringUtils.getDoubleString0(bizProcessData.getPrice8()) + " % ";
                    payRemark += "安装调试" + StringUtils.getDoubleString0(bizProcessData.getPrice9()) + " 天 ";
                    payRemark += "付" + StringUtils.getDoubleString0(bizProcessData.getPrice10()) + " % ";
                    payRemark += "质保金" + StringUtils.getDoubleString0(bizProcessData.getPrice11()) + " % ";
                }
            }

            if (StringUtils.isNotEmpty(transportType)) {
                payRemark = payRemark + "," + transportType;
            }

            table.addCell(PdfUtil.mergeColLeft("付款及运输：" + payRemark, 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            //合同签定后5个工作日发货（若未当日回传，发货期则从收到回传之日延后）
            table.addCell(PdfUtil.mergeColLeft("1、交货周期：" + StringUtils.trim(bizProcessData.getString24()), 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("2、收  货  人：" + StringUtils.trim(bizProcessData.getString11()) + " " + StringUtils.trim(bizProcessData.getString12()), 14,textFont));
            table.addCell(PdfUtil.mergeCol("", 1,textFont));
            table.addCell(PdfUtil.mergeColLeft("3、交货地点：" + StringUtils.trim(bizProcessData.getString9()), 14,textFont));

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


    @GetMapping("/viewPdfProduce")
    public void viewPdfProduce(HttpServletRequest request,HttpServletResponse response) {
        BizProcessData bizProcessData = new BizProcessData();
        bizProcessData.setString27("1");//生产单
        createPdf(request,response,bizProcessData);
    }
    @PostMapping("/selectBizTestProductList")
    @ResponseBody
    public TableDataInfo selectBizTestProductList(BizProcessData bizProcessData) {
        //@RequestParam String dataId,@RequestParam String level
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setString2(bizProcessData.getLevel());
        queryBizProcessChild.setQueryStatus(bizProcessData.getQueryStatus());

        queryBizProcessChild.setOrderNo(bizProcessData.getOrderNo());
        queryBizProcessChild.setContractNo(bizProcessData.getContractNo());
        queryBizProcessChild.setProcurementNo(bizProcessData.getProcurementNo());

        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizTestProductList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }
    @PostMapping("/selectBizTestActuatorList")
    @ResponseBody
    public TableDataInfo selectBizTestActuatorList(BizProcessData bizProcessData) {
        //@RequestParam String dataId,@RequestParam String level
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setString2(bizProcessData.getLevel());
        queryBizProcessChild.setQueryStatus(bizProcessData.getQueryStatus());
        queryBizProcessChild.setOrderNo(bizProcessData.getOrderNo());
        queryBizProcessChild.setContractNo(bizProcessData.getContractNo());
        queryBizProcessChild.setProcurementNo(bizProcessData.getProcurementNo());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizTestActuatorList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }
    @PostMapping("/selectBizTestRef1List")
    @ResponseBody
    public TableDataInfo selectBizTestRef1List(BizProcessData bizProcessData) {
        //@RequestParam String dataId,@RequestParam String level
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setString2(bizProcessData.getLevel());
        queryBizProcessChild.setQueryStatus(bizProcessData.getQueryStatus());
        queryBizProcessChild.setOrderNo(bizProcessData.getOrderNo());
        queryBizProcessChild.setContractNo(bizProcessData.getContractNo());
        queryBizProcessChild.setProcurementNo(bizProcessData.getProcurementNo());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizTestRef1List(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }
    @PostMapping("/selectBizTestRef2List")
    @ResponseBody
    public TableDataInfo selectBizTestRef2List(BizProcessData bizProcessData) {
        //@RequestParam String dataId,@RequestParam String level
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setString2(bizProcessData.getLevel());
        queryBizProcessChild.setQueryStatus(bizProcessData.getQueryStatus());
        queryBizProcessChild.setOrderNo(bizProcessData.getOrderNo());
        queryBizProcessChild.setContractNo(bizProcessData.getContractNo());
        queryBizProcessChild.setProcurementNo(bizProcessData.getProcurementNo());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizTestRef2List(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }
    @PostMapping("/selectBizTestPAList")
    @ResponseBody
    public TableDataInfo selectBizTestPAList(BizProcessData bizProcessData) {
        //@RequestParam String dataId,@RequestParam String level
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setString2(bizProcessData.getLevel());
        queryBizProcessChild.setQueryStatus(bizProcessData.getQueryStatus());
        queryBizProcessChild.setOrderNo(bizProcessData.getOrderNo());
        queryBizProcessChild.setContractNo(bizProcessData.getContractNo());
        queryBizProcessChild.setProcurementNo(bizProcessData.getProcurementNo());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizTestPAList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }

    @GetMapping("/uploadContract")
    public String uploadContract(ModelMap mmap) {
        return prefix + "/uploadContract";
    }

    @PostMapping("/excelData")
    @ResponseBody
    public JSONObject excelData(){
        JSONObject retJson = new JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONArray errorArray = new JSONArray();
        String url = getRequest().getParameter("url");
        String realPath = Global.getFilePath() + url;
        List<BizProductExcel> list = new ArrayList<>();
        try {
            ExcelUtil<BizProductExcel> excelUtil = new ExcelUtil(BizProductExcel.class);
            list = excelUtil.importExcel("",realPath);

            if (CollectionUtils.isEmpty(list)) {
                JSONObject json = new JSONObject();
                json.put("msg","格式错误，请按照标准格式增加！");
                errorArray.add(json);
            }
            Map<Long,Long> map = new HashMap<>();

            int i = 1;
            for (BizProductExcel product : list) {
                JSONObject jsonData = new JSONObject();
                String model = product.getModel();
                if (StringUtils.isEmpty(model)) {
                    JSONObject json = new JSONObject();
                    json.put("msg","第" + (i + 1) + "行产品型号不能为空！");
                    errorArray.add(json);
                    i++;
                    continue;
                } else {
                    model = model.trim();
                }
                String level = product.getLevel().trim();
                if (StringUtils.isNotEmpty(level)) {
                    level = level.trim();
                }
                String num = product.getNum();
                if (StringUtils.isNotEmpty(num)) {
                    num = num.trim();
                }
                if (!StringUtils.isNumeric(num)) {
                    JSONObject json = new JSONObject();
                    json.put("msg","第" + (i + 1) + "行数量格式错误！");
                    errorArray.add(json);
                    i++;
                    continue;
                }

                BizProduct queryBizProduct = new BizProduct();
                queryBizProduct.setModelEq(model);

                String specifications = product.getSpecifications();
                if (StringUtils.isNotEmpty(specifications)) {
                    queryBizProduct.setSpecificationsName(specifications);
                }
                if (StringUtils.isNotEmpty(level)) {
                    queryBizProduct.setString2(level);
                }
                List<BizProduct> bizProductList = bizProductService.selectBizProductList(queryBizProduct);
                if (!CollectionUtils.isEmpty(bizProductList)) {
                    /*if (bizProductList.size() > 1) {
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 1) + "行 查询出多条数据！");
                        errorArray.add(json);
                        i++;
                        continue;
                    }*/

                    BizProduct bizProduct = bizProductList.get(0);
                    Long productId = bizProduct.getProductId();
                    jsonData.put("productId",productId);
                    jsonData.put("productName",bizProduct.getName());
                    jsonData.put("model",bizProduct.getModel());
                    jsonData.put("specifications",bizProduct.getSpecifications());
                    jsonData.put("nominalPressure",bizProduct.getNominalPressure());
                    jsonData.put("valvebodyMaterial",bizProduct.getValvebodyMaterial());
                    jsonData.put("valveElement",bizProduct.getValveElement());
                    jsonData.put("sealingMaterial",bizProduct.getSealingMaterial());
                    jsonData.put("driveForm",bizProduct.getDriveForm());
                    jsonData.put("connectionType",bizProduct.getConnectionType());
                    jsonData.put("productNum",num);

                    //执行器*1 法兰*2
                    jsonData.put("productCostPrice",bizProduct.getCostPrice());

                    jsonData.put("productPrice",bizProduct.getPrice());
                    jsonData.put("productCoefficient","1");
                    jsonData.put("productRef1Id","");
                    jsonData.put("ref1Name","请选择");

                    jsonData.put("ref1Price","0");
                    jsonData.put("productRef1Num","0");
                    jsonData.put("productRef1Coefficient","0");
                    jsonData.put("productRef2Id","");
                    jsonData.put("ref2Name","请选择");
                    jsonData.put("ref2Price","0");
                    jsonData.put("productRef2Num","0");
                    jsonData.put("productRef2Coefficient","0");
                    jsonData.put("actuatorId","");
                    jsonData.put("actuatorName","请选择");
                    jsonData.put("actuatorPrice","0");
                    jsonData.put("actuatorNum","0");
                    jsonData.put("actuatorCoefficient","0");
                    jsonData.put("productRemark","");
                    jsonData.put("string14","0");
                    jsonData.put("totalPrice","0");

                    jsonData.put("pattachmentName","请选择");
                    jsonData.put("pattachmentPrice","0");
                    jsonData.put("pattachmentCount","0");
                    jsonData.put("pattachmentCoefficient","0");

                    jsonData.put("pattachment1Name","请选择");
                    jsonData.put("pattachment1Price","0");
                    jsonData.put("pattachment1Count","0");
                    jsonData.put("pattachment1Coefficient","0");

                    jsonData.put("pattachment2Name","请选择");
                    jsonData.put("pattachment2Price","0");
                    jsonData.put("pattachment2Count","0");
                    jsonData.put("pattachment2Coefficient","0");

                    jsonData.put("pattachment3Name","请选择");
                    jsonData.put("pattachment3Price","0");
                    jsonData.put("pattachment3Count","0");
                    jsonData.put("pattachment3Coefficient","0");

                    jsonData.put("pattachment4Name","请选择");
                    jsonData.put("pattachment4Price","0");
                    jsonData.put("pattachment4Count","0");
                    jsonData.put("pattachment4Coefficient","0");

                    jsonData.put("string15","RAL5010高光");


                    if (!map.containsKey(productId)) {
                        dataArray.add(jsonData);
                        map.put(productId,productId);
                    } else {
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 1) + "行数据重复！");
                        errorArray.add(json);
                    }
                } else {
                    JSONObject json = new JSONObject();
                    json.put("msg","第" + (i + 1) + "行数据不存在！");
                    errorArray.add(json);
                }
                i++;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("导出失败，请联系网站管理员！");
        }
        retJson.put("data",dataArray);
        if (errorArray.size() > 0) {
            retJson.put("error",errorArray);
        }
        return retJson;
    }
}
