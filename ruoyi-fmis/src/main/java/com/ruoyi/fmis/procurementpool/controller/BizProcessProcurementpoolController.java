package com.ruoyi.fmis.procurementpool.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.config.RedisUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.BizContractLevel;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.file.domain.BizAccessory;
import com.ruoyi.fmis.file.service.IBizAccessoryService;
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
@RequestMapping("/fmis/procurementpool")
public class BizProcessProcurementpoolController extends BaseController {
    private String prefix = "fmis/procurementpool";

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
    private RedisUtil redisUtil;
    @Autowired
    private IBizAccessoryService bizAccessoryService;

    @RequiresPermissions("fmis:procurementpool:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }

    /**
     * 查询合同管理列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessData bizProcessData) {

        BizProcessData newBizProcessData = new BizProcessData();
        String bizId = bizProcessData.getBizId();
        //采购池
        newBizProcessData.setString13("1");
        newBizProcessData.setBizId(BizConstants.BIZ_contract);
        newBizProcessData.setString30(bizProcessData.getString30());
        newBizProcessData.setDataStatus("-1");
//        newBizProcessData.setString1(bizProcessData.getString1());
        bizProcessData.setDataStatus("-1");
        if (bizProcessData.getDataStatus().equals("1")) {
            newBizProcessData.setDataStatus("-1");
            bizProcessData.setDataStatus("-1");
        }
        if (bizProcessData.getDataStatus().equals("2")) {
            newBizProcessData.setDataStatus("1");
            bizProcessData.setDataStatus("1");
        }
        if (!StringUtils.isEmpty(bizProcessData.getString12())) {
            String xs = bizProcessData.getString3();
            newBizProcessData.setString1(xs);
            newBizProcessData.setContractNoList(Arrays.asList(xs.split(",")));
        }
        if (StringUtils.isNotEmpty(bizProcessData.getString1()) && bizProcessData.getString1().startsWith("XS")) {
            newBizProcessData.setString5(bizProcessData.getString1());
        }
        if (StringUtils.isNotEmpty(bizProcessData.getString3()) && bizProcessData.getString3().startsWith("XS")) {
            newBizProcessData.setString5(bizProcessData.getString3());
        }
        if (bizProcessData.getString30() != null && bizProcessData.getString30().equals("2")) {
            startPage(); //已完成处理分页查询
        }
        //startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListXs(newBizProcessData);
        newBizProcessData.setString1(bizProcessData.getString1());

        List<BizProcessData> newList = new ArrayList<>();

        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        //if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {


                BizProcessData queryLevelData = new BizProcessData();
                queryLevelData.setDataStatus(bizProcessData.getDataStatus());
                queryLevelData.setDataId(data.getDataId());
                queryLevelData.setBizEditFlag(bizProcessData.getBizEditFlag());
                //queryLevelData.setSupplierId(bizProcessData.getString6());
                //queryLevelData.setProcurementId(bizProcessData.getProcurementId());
                //过滤数据
                //List<BizContractLevel> levelList = bizProcessDataService.listLevel(queryLevelData);
                //if (CollectionUtils.isEmpty(levelList)) {
                //    continue;
                //}

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
                        if (!CollectionUtils.isEmpty(flowMap)) {
                            String userFlowStatus = flowMap.keySet().iterator().next();
                            int userFlowStatusInt = Integer.parseInt(userFlowStatus);
                            if (userFlowStatusInt == flowStatusInt + 1) {
                                data.setOperationExamineStatus(true);
                            }
                        }
                    }
                }
                //是否有附件
                BizAccessory bizAccessory = new BizAccessory();
                bizAccessory.setBizId(Integer.valueOf(data.getDataId() + ""));
                bizAccessory.setFileType(Integer.valueOf(2));
                List<com.ruoyi.fmis.file.domain.BizAccessory>  list1 = bizAccessoryService.selectBizAccessoryByBizId(bizAccessory);
                if (list1 != null && list1.size() > 0) {
                    data.setIsAtt(1);
                } else {
                    data.setIsAtt(0);
                }
                newList.add(data);
            }
        //}
        return getDataTable(list);
    }
    @GetMapping("/selectProduct")
    public String selectProduct(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        BizProduct bizProduct = null;
        BizProduct queryBizProduct = new BizProduct();
        queryBizProduct.setProductId(Long.parseLong(productId));
        List<BizProduct> bizProductList = bizProductService.selectBizProductList(queryBizProduct);
        bizProduct = bizProductList.get(0);
        mmap.put("seriesSelect",bizDictService.selectBizDictByProductType(BizConstants.productTypeCode));
        mmap.put("suppliers",bizSuppliersService.selectAllList());

        mmap.put("bizProductModel",bizProduct.getModel());
        mmap.put("bizProductSpecifications",bizProduct.getSpecifications());

        mmap.put("oldProductId",productId);
        return prefix + "/selectProduct";
    }

    /**
     * 保存临时文件
     */
    @PostMapping("/saveSessionId")
    @ResponseBody
    public AjaxResult saveSessionId(BizProcessChild bizProcessChild) {
        String pSessionId = bizProcessChild.getPSessionId();
        String productId = bizProcessChild.getProductId();
        String newProductId = bizProcessChild.getNewProductId();
        redisUtil.set(pSessionId + "_" + productId,newProductId,600);
        return toAjax(0);
    }
}
