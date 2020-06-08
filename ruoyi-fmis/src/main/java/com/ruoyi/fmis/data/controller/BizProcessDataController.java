package com.ruoyi.fmis.data.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotation.service.IBizQuotationService;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
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
}
