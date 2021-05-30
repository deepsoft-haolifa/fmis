package com.ruoyi.fmis.newdelivery.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发货管理Controller
 *
 * @author frank
 * @date 2020-05-05
 */
@Controller
@RequestMapping("/fmis/newdelivery")
public class BizProcessDataNewDeliveryController extends BaseController {
    private String prefix = "fmis/newdelivery";

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

    @RequiresPermissions("fmis:newdelivery:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }

    @GetMapping("/inventory")
    public String inventory() {
        return prefix + "/inventory";
    }

    @GetMapping("/outbound")
    public String outbound() {
        return prefix + "/outbound";
    }



    /**
     * 查询发货管理列表
     */
    @RequiresPermissions("fmis:newdelivery:list")
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
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefDelivery(bizProcessData);
        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                data.setLoginUserId(ShiroUtils.getUserId().toString());
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
    @GetMapping("/viewDetail")
    public String viewDetail(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/viewDetail";
    }
    @GetMapping("/updateViewDetail")
    public String updateViewDetail(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        mmap.put("isUpdate", 1);
        return prefix + "/updateViewDetail";
    }
    @PostMapping("/doExamine")
    @ResponseBody
    @Transactional
    public AjaxResult doExamine(BizProcessData bizProcessData) {
        String examineStatus = bizProcessData.getExamineStatus();
        String examineRemark = bizProcessData.getExamineRemark();
        String dataId = bizProcessData.getDataId().toString();

        if (!"1".equals(examineStatus)) {
            //不同意把库存减去
            BizProcessChild queryChild = new BizProcessChild();
            queryChild.setDataId(Long.parseLong(dataId));
            List<BizProcessChild> childList = bizProcessChildService.selectBizProcessChildInventoryList(queryChild);
            if (!CollectionUtils.isEmpty(childList)) {
                Long totalNum = 0L;
                String updateChildId = "";
                for (BizProcessChild child : childList) {
                    String string13 = child.getString13();
                    totalNum += StringUtils.toLong(string13);
                    updateChildId = child.getString15();
                }
                //查询主数据
                BizProcessChild updateChild = bizProcessChildService.selectBizProcessChildById(Long.parseLong(updateChildId));
                updateChild.setString11((StringUtils.toLong(updateChild.getString11()) + totalNum) + "");
                updateChild.setString20("0");
                bizProcessChildService.updateBizProcessChild(updateChild);
            }
        }

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
     * 导出发货管理列表
     */
    @RequiresPermissions("fmis:delivery:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProcessData bizProcessData) {
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataList(bizProcessData);
        ExcelUtil<BizProcessData> util = new ExcelUtil<BizProcessData>(BizProcessData.class);
        return util.exportExcel(list, "data");
    }

    /**
     * 新增发货管理
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }
    /**
     * 发货申请
     */
    @Log(title = "发货申请", businessType = BusinessType.UPDATE)
    @PostMapping("/reportFh")
    @ResponseBody
    @Transactional
    public AjaxResult reportFh(BizProcessData bizProcessData) {

//        BizProcessData bizProcessData1 = bizProcessDataService.selectBizProcessDataById(bizProcessData.getDataId());

        //自动上报
        bizProcessDataService.doExamine(bizProcessData.getDataId() + "", "1", "销售员上报", bizProcessData.getBizId());
        return toAjax(1);
    }
    /**
     * 新增保存发货管理
     */
    @RequiresPermissions("fmis:newdelivery:add")
    @Log(title = "发货管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProcessData bizProcessData) {
        bizProcessData.setFlowStatus("-2");

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
     * 修改发货管理
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
                BizProcessData bizProduct = bizProcessDataService.selectBizProcessDataById(Long.parseLong(productId));
                productNames += bizProduct.getString1() + ",";
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
        return prefix + "/edit";
    }
    @GetMapping("/editdelivery")
    public String editdelivery(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/editdelivery";
    }


    /**
     * 修改保存发货管理
     */
    @Log(title = "发货管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProcessData bizProcessData) {
        int updateReturn = bizProcessDataService.updateBizProcessData(bizProcessData);
        Long dataId = bizProcessData.getDataId();
        return toAjax(updateReturn);
    }

    @PostMapping( "/saveOutbound")
    @ResponseBody
    public AjaxResult saveOutbound(String childId) {

        BizProcessChild child = bizProcessChildService.selectBizProcessChildById(Long.parseLong(childId));

        //查看库存够不够 够减去 不够提示库存不够

        BizProcessChild bizProcessChild = new BizProcessChild();
        String string15  = "";
        //产品
        if (child.getString2().equals("1")) {
            bizProcessChild.setString7(child.getString7());
            bizProcessChild.setString5(child.getString5());
            bizProcessChild.setString8(child.getString8());
            List<BizProcessChild> inventoryChilds = bizProcessChildService.selectBizProcessChildListForKuCun(bizProcessChild);
            bizProcessChild.setString13(child.getString13());
            if (inventoryChilds == null || inventoryChilds.size() == 0) {
                return error("库存不够，请检查库存");
            }

            BizProcessChild inventoryChild = inventoryChilds.get(0);

            if (StringUtils.toLong(inventoryChild.getString11()) < StringUtils.toLong(bizProcessChild.getString13())) {
                return error("库存不够，请检查库存");
            }
            string15 = inventoryChild.getChildId() + "";
            String string16 = inventoryChild.getString16();
            inventoryChild.setString16(StringUtils.toLong(string16) + StringUtils.toLong(bizProcessChild.getString13()) + "");
            //inventoryChild 之前的报检的数据
            //bizProcessChild发货的数据
            inventoryChild.setString11((StringUtils.toLong(inventoryChild.getString11()) - StringUtils.toLong(bizProcessChild.getString13())) + "");
            inventoryChild.setUpdateTime(new Date());
            inventoryChild.setUpdateBy(ShiroUtils.getUserId() + "");
            inventoryChild.setString14("1");
            bizProcessChildService.updateBizProcessChild(inventoryChild);

        } else {
            if (StringUtils.toLong(child.getString2()) < 5) {
                bizProcessChild.setString7(child.getString7());
            } else {
                bizProcessChild.setString8(child.getString8());
            }
            if (StringUtils.toLong(child.getString2()) == 3) {
                bizProcessChild.setString8(child.getString8());
            }

            bizProcessChild.setString5(child.getString5());
            List<BizProcessChild> inventoryChilds = bizProcessChildService.selectBizProcessChildListForKuCun(bizProcessChild);
            bizProcessChild.setString13(child.getString13());
            if (inventoryChilds == null || inventoryChilds.size() == 0) {
                return error("库存不够，请检查库存");
            }
            BizProcessChild inventoryChild = inventoryChilds.get(0);
            String string13  = child.getString13();
            if (string13.contains(".")) {
                string13 =  new BigDecimal(Double.parseDouble(string13)).longValue() + "";
            }
            if (StringUtils.toLong(inventoryChild.getString11()) < StringUtils.toLong(string13)) {
                return error("库存不够，请检查库存");
            }

            string15 = inventoryChild.getChildId() + "";
            String string16 = inventoryChild.getString16();
            inventoryChild.setString16(StringUtils.toLong(string16) + StringUtils.toLong(string13) + "");
            inventoryChild.setString11((StringUtils.toLong(inventoryChild.getString11()) - StringUtils.toLong(string13)) + "");
            inventoryChild.setUpdateTime(new Date());
            inventoryChild.setUpdateBy(ShiroUtils.getUserId() + "");
            inventoryChild.setString14("1");
            bizProcessChildService.updateBizProcessChild(inventoryChild);
        }
        //出库的是哪个库存里面的
        child.setString15(string15);
        child.setUpdateBy(ShiroUtils.getUserId().toString());
        child.setUpdateTime(new Date());
        child.setString19("1");
        int i = bizProcessChildService.updateBizProcessChild(child);
        // 发货更新销售订单状态
        bizProcessDataService.deliveryUpdateStatus(child.getString5());

        return toAjax(i);
    }

    /**
     * 删除发货管理
     */
    @RequiresPermissions("fmis:newdelivery:remove")
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

}
