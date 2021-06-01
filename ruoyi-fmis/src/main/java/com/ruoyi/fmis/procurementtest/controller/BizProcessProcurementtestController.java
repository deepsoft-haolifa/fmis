package com.ruoyi.fmis.procurementtest.controller;

import java.util.*;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.Constant;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.pattachment.domain.BizProductAttachment;
import com.ruoyi.fmis.pattachment.service.IBizProductAttachmentService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import com.ruoyi.fmis.status.domain.BizDataStatus;
import com.ruoyi.fmis.status.service.IBizDataStatusService;
import com.ruoyi.fmis.stestn.domain.BizDataStestn;
import com.ruoyi.fmis.stestn.service.IBizDataStestnService;
import com.ruoyi.fmis.steststay.domain.BizDataSteststay;
import com.ruoyi.fmis.steststay.service.IBizDataSteststayService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.service.ISysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
 * 质检Controller
 *
 * @author frank
 * @date 2020-05-05
 */
@Controller
@RequestMapping("/fmis/procurementtest")
public class BizProcessProcurementtestController extends BaseController {
    private String prefix = "fmis/procurementtest";

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IBizDataStestnService bizDataStestnService;

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
    private IBizProductAttachmentService bizProductAttachmentService;

    @Autowired
    private IBizDataSteststayService bizDataSteststayService;

    @Autowired
    private IBizDataStatusService bizDataStatusService;

    @GetMapping()
    public String data() {
        return prefix + "/data";
    }


    @GetMapping("/history")
    public String history() {
        return prefix + "/history";
    }

    /**
     * 查询合同管理列表
     */
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
                if ("-2".equals(flowStatus)) {
                    flowStatusRemark = "待上报";
                } else if ("1".equals(flowStatus)) {
                    flowStatusRemark = "已上报";
                } else {
                    SysRole currentSysRole = CommonUtils.getLikeByMap(flowAllMap, flowStatus.replaceAll("-", ""));
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


    @GetMapping("/edit")
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


    @PostMapping("/selectBizTestChildList")
    @ResponseBody
    public TableDataInfo selectBizTestChildList(BizProcessChild bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setParamterId(bizProcessData.getParamterId());
        queryBizProcessChild.setChildId(bizProcessData.getChildId());
        queryBizProcessChild.setStayId(bizProcessData.getStayId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizTestChildList(queryBizProcessChild);
        for (BizProcessChild bizProcessChild : bizProcessChildList) {
            bizProcessChild.setContractNo(bizProcessData.getContractNo());
        }
        return getDataTable(bizProcessChildList);
    }


    @PostMapping("/selectBizTestChildHistoryList")
    @ResponseBody
    public TableDataInfo selectBizTestChildHistoryList(BizProcessChild bizProcessData) {

        startPage();
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizTestChildHistoryList(bizProcessData);
        return getDataTable(bizProcessChildList);
    }

    @Autowired
    private IBizProductRefService bizProductRefService;

    @Autowired
    private IBizActuatorService bizActuatorService;

    @PostMapping("/saveTest")
    @ResponseBody
    @Transactional
    public AjaxResult saveTest() {
        String testId = getRequest().getParameter("testId");
        String stayId = getRequest().getParameter("stayId");
        String dataId = getRequest().getParameter("dataId");//合同id
        String paramterId = getRequest().getParameter("paramterId");//业务数据id
        String childId = getRequest().getParameter("childId");//关联字表id
        String remark = getRequest().getParameter("remark");
        String yesNum = getRequest().getParameter("yesNum");
        String noNum = getRequest().getParameter("noNum");
        String statusId = getRequest().getParameter("statusId");
        String contractNo = getRequest().getParameter("contractNo");

        BizDataStestn bizDataStestn = new BizDataStestn();

        if (!"0".equals(testId) && StringUtils.isNotEmpty(testId)) {
            bizDataStestn = bizDataStestnService.selectBizDataStestnById(Long.parseLong(testId));
        }
        bizDataStestn.setStatusId(Long.parseLong(statusId));
        bizDataStestn.setString1(yesNum);
        bizDataStestn.setString2(noNum);
        bizDataStestn.setString3(paramterId);
        bizDataStestn.setString4(dataId);
        bizDataStestn.setString5(childId);
        bizDataStestn.setString6(stayId);
        bizDataStestn.setRemark(remark);
        if ("0".equals(testId) || StringUtils.isEmpty(testId)) {
            bizDataStestn.setCreateTime(new Date());
            bizDataStestn.setCreateBy(ShiroUtils.getUserId().toString());
            bizDataStestnService.insertBizDataStestn(bizDataStestn);
        } else {
            //bizDataStestnService.updateBizDataStestn(bizDataStestn);
        }

        BizDataSteststay bizDataSteststay = bizDataSteststayService.selectBizDataSteststayById(Long.parseLong(stayId));

        String stayNum = StringUtils.getDoubleString0(bizDataSteststay.getNum());
        String orderNo = bizDataSteststay.getString6();
        /**
         * 放到 库存管理表 里面
         * 报检单号，orderNo=string3
         * 销售合同号，string4=string12
         * 采购合同号，string5=string10
         * 类型，string2
         * 名称，
         * 型号，
         * 规格、
         * 等级、
         * 价格、
         * 数量
         */
        BizProcessChild bizProcessChild = bizProcessChildService.selectBizProcessChildById(Long.parseLong(childId));
        /*String productId = bizProcessChild.getString2();
        String actuatorId = bizProcessChild.getString11();
        String ref1 = bizProcessChild.getString5();
        String ref2 = bizProcessChild.getString8();
        //定位器
        Long pattachmentId = bizProcessChild.getPattachmentId();
        Long pattachmentId1 = bizProcessChild.getPattachment1Id();
        Long pattachmentId2 = bizProcessChild.getPattachment2Id();
        Long pattachmentId3 = bizProcessChild.getPattachment3Id();
        Long pattachmentId4 = bizProcessChild.getPattachment4Id();*/
        String type = "0";

        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        //质检管理 数据
        BizProcessChild insertChild = new BizProcessChild();
        insertChild.setCreateBy(ShiroUtils.getUserId().toString());
        insertChild.setString1(childId);
        insertChild.setString4(bizProcessData.getString12());
        insertChild.setString5(bizProcessData.getString10());
        if (!StringUtils.isEmpty(contractNo)) {
            insertChild.setString5(contractNo);
        }

        insertChild.setString11(yesNum);
        insertChild.setString12(stayId);

        /**
         * 查询出biz_data_status表中的type
         * type=1  产品
         * type=2 执行器
         * type=3 法兰
         * type=4 螺栓
         * 否则 定位设备
         */
        Long sId = bizDataSteststay.getStatusId();
        BizDataStatus dataStatus = bizDataStatusService.selectBizDataStatusById(sId);
        String busType = dataStatus.getType();
        if ("1".equals(busType)) {
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

        }
        insertChild.setString2(type);
        insertChild.setString3(orderNo);
        //质检状态
        insertChild.setString20("1");

        BizProcessChild queryChildExist = new BizProcessChild();
        queryChildExist.setString1(childId);
        queryChildExist.setString3(orderNo);
        List<BizProcessChild> bizProcessChildListExist = bizProcessChildService.selectBizProcessChildList(queryChildExist);
        if (CollectionUtils.isEmpty(bizProcessChildListExist)) {
            bizProcessChildService.insertBizProcessChild(insertChild);
        } else {
            BizProcessChild updateChild = bizProcessChildListExist.get(0);
            updateChild.setString20("1");
            updateChild.setString12(stayId);
            updateChild.setString11((Integer.parseInt(updateChild.getString11()) + Integer.parseInt(yesNum)) + "");
            updateChild.setUpdateBy(ShiroUtils.getUserId().toString());
            updateChild.setUpdateTime(new Date());
            bizProcessChildService.updateBizProcessChild(updateChild);
        }
        // 检验完成，更新采购合同状态和销售合同状态
        bizProcessDataService.testCompleteUpdateStatus(dataId);


        return toAjax(1);
    }




    @PostMapping("/removeTest")
    @ResponseBody
    public AjaxResult removeTest() {
        String testId = getRequest().getParameter("testId");
        if ("0".equals(testId)) {
            return toAjax(1);
        }
        bizDataStestnService.deleteBizDataStestnById(Long.parseLong(testId));
        return toAjax(1);
    }

    // 不合格品处理
    @GetMapping("/reject")
    public String  viewReject() {
        return prefix + "/reject";
    }

    @GetMapping("/reject/detail/{testId}")
    public String viewRejectDetail(@PathVariable(name = "testId") Long testId, ModelMap map) {
        BizDataStestn bizDataStestn = new BizDataStestn();
        bizDataStestn.setTestId(testId);
        BizDataStestn detail = bizDataStestnService.selectBizDataStestnById(testId);
        List<String> accessoryList = new ArrayList<>();
        String accessorys = detail.getAccessorys();
        if (StringUtils.isNotEmpty(accessorys)) {
            String[] split = accessorys.split("\\|");
            accessoryList.addAll(Arrays.asList(split));
        }
        map.put("detail", detail);
        map.put("accessoryList", accessoryList);
        return prefix + "/rejectDetail";
    }

    @RequiresPermissions("fmis:procurementtest:deal")
    @PostMapping("/reject/deal")
    @ResponseBody
    public AjaxResult dealReject(BizDataStestn bizDataStestn) {

        bizDataStestnService.updateBizDataStestn(bizDataStestn);
        return toAjax(1);
    }
}

