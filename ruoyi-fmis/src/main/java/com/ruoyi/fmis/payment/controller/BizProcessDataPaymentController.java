package com.ruoyi.fmis.payment.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.budget.domain.BizCostBudget;
import com.ruoyi.fmis.budget.mapper.BizCostBudgetMapper;
import com.ruoyi.fmis.budget.service.IBizCostBudgetService;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.subjects.domain.BizSubjects;
import com.ruoyi.fmis.subjects.service.IBizSubjectsService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.mapper.SysDeptMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 费用报销-申请
 *
 * @author frank
 * @date 2020-05-05
 */
@Controller
@RequestMapping("/fmis/payment")
public class BizProcessDataPaymentController extends BaseController {
    private String prefix = "fmis/payment";

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @Autowired
    private IBizProcessChildService bizProcessChildService;
    @Autowired
    private IBizSubjectsService bizSubjectsService;
    @Autowired
    private IBizCostBudgetService bizCostBudgetService;
    @Autowired
    private BizCostBudgetMapper bizCostBudgetMapper;
    @Autowired
    private SysDeptMapper sysDeptService;


    @RequiresPermissions("fmis:payment:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }


    /**
     * 查询报销申请列表
     */
    @RequiresPermissions("fmis:payment:list")
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
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefPayment(bizProcessData);

        Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);
        if (!CollectionUtils.isEmpty(flowMap)) {
            //计算流程描述
            for (BizProcessData data : list) {
                String flowStatus = data.getFlowStatus();
                //结束标识
                String normalFlag = data.getNormalFlag();
                data.setLoginUserId(ShiroUtils.getUserId().toString());
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
                        //String userFlowStatus = flowMap.keySet().iterator().next();
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
     * 查询报价单产品
     *
     * @return
     */
    @PostMapping("/listChild")
    @ResponseBody
    public TableDataInfo listChild(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }

    @GetMapping("/examineEdit")
    public String examineEdit(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
       if("2".equals(bizProcessData.getString1())){
           return prefix + "/examineEdit1";
       }else{
           return prefix + "/examineEdit";
       }
    }

//    @GetMapping("/examineEdit1")
//    public String examineEdit1(ModelMap mmap) {
//        String dataId = getRequest().getParameter("dataId");
//        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(Long.parseLong(dataId));
//        mmap.put("bizProcessData", bizProcessData);
//        return prefix + "/examineEdit1";
//    }

    @GetMapping("/viewDetail")
    public String viewDetail(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataBorrowingById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/viewDetail";
    }

    @GetMapping("/viewDetail1")
    public String viewDetail1(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/viewDetail1";
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
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        Long deptId = ShiroUtils.getSysUser().getDeptId();
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        mmap.put("deptId", deptId);
        mmap.put("deptName", sysDept.getDeptName());

        // 借款单
        List<BizProcessData> bizProcessData = bizProcessDataService.selectAllBorrowingWithNoPayAndAgree();
        Map<String, Double> borrowingMap = new HashMap<>();
        List<String> borrowingNoList = new ArrayList<>();
        for (BizProcessData data : bizProcessData) {
            borrowingNoList.add(data.getString2());
            borrowingMap.put(data.getString2(), data.getPrice1());
        }
        mmap.put("borrowingNoList", borrowingNoList);
        mmap.put("borrowingMap", borrowingMap);
        return prefix + "/add";
    }

    @GetMapping("/add1")
    public String add1(ModelMap mmap) {
        List<BizSubjects> bizSubjects = bizSubjectsService.selectBizSubjectsListNoParent(new BizSubjects());
        List<String> subjectNameList = Optional.ofNullable(bizSubjects).orElse(new ArrayList<>()).stream().map(BizSubjects::getName).collect(Collectors.toList());
        mmap.put("subjects", subjectNameList);
        Long deptId = ShiroUtils.getSysUser().getDeptId();
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        mmap.put("deptId", deptId);
        mmap.put("deptName", sysDept.getDeptName());
        return prefix + "/add1";
    }

    /**
     * 新增保存合同管理
     */
    @RequiresPermissions("fmis:payment:add")
    @Log(title = "差旅报销添加", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(BizProcessData bizProcessData) {
        bizProcessData.setFlowStatus("0");
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

        // 获取总价格
        BigDecimal totalPrice = BigDecimal.ZERO;
        String productArrayStr = bizProcessData.getProductParmters();
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                if (bizProcessChild.getPrice1() != null && bizProcessChild.getPrice1() > 0) {
                    totalPrice = totalPrice.add(new BigDecimal(bizProcessChild.getPrice1()));
                }
                if (bizProcessChild.getPrice2() != null && bizProcessChild.getPrice2() > 0) {
                    totalPrice = totalPrice.add(new BigDecimal(bizProcessChild.getPrice2()));
                }
                if (bizProcessChild.getPrice3() != null && bizProcessChild.getPrice3() > 0) {
                    totalPrice = totalPrice.add(new BigDecimal(bizProcessChild.getPrice3()));
                }
            }
        }
        bizProcessData.setPrice1(totalPrice.doubleValue());
        bizProcessData.setString2("FP" + DateUtils.dateTimeNow() + RandomStringUtils.randomNumeric(3));
        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);
        Long dataId = bizProcessData.getDataId();

        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                bizProcessChild.setDataId(dataId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild);
            }
        }
        return toAjax(insertReturn);
    }


    /**
     * 新增保存合同管理
     */
    @RequiresPermissions("fmis:payment:add")
    @Log(title = "费用报销添加", businessType = BusinessType.INSERT)
    @PostMapping("/add1")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave1(BizProcessData bizProcessData) {
        bizProcessData.setFlowStatus("0");
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

        // 获取总价格
        BigDecimal totalPrice = BigDecimal.ZERO;
        String productArrayStr = bizProcessData.getProductParmters();
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                if (bizProcessChild.getPrice1() != null && bizProcessChild.getPrice1() > 0) {
                    Date date = bizProcessChild.getDatetime1();
                    String subjectId = bizProcessChild.getString2();
                    if (date == null || StringUtils.isEmpty(subjectId)) {
                        return error("报销项日期/科目 是必填项");
                    }
                    // 根据id查询费用科目的名称
                    BizSubjects bizSubjects = bizSubjectsService.selectBizSubjectsById(Long.valueOf(subjectId));
                    bizProcessChild.setString3(bizSubjects.getName());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    String monthStr = String.valueOf(month);
                    if (month < 10) {
                        monthStr = "0" + month;
                    }
                    // 判断此科目的费用是否够
                    String finalMonthStr = monthStr;
                    List<BizCostBudget> bizCostBudgets = bizCostBudgetMapper.selectBizCostBudgetList(new BizCostBudget() {{
                        setDeptId(Long.parseLong(bizProcessData.getString6()));
                        setSubjectsId(Long.parseLong(subjectId));
                        setY(String.valueOf(year));
                        setM(finalMonthStr);
                    }});
                    double totalAmount = bizCostBudgets.stream().mapToDouble(BizCostBudget::getAmount).sum();
                    // 获取已经报销的费用
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String firstDay = format.format(calendar.getTime());
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    String lastDay = format.format(calendar.getTime());
                    double paymentedPrice = bizProcessChildService.selectPaymentedPrice(subjectId, bizProcessData.getString6(), firstDay, lastDay);
                    if (totalAmount < paymentedPrice + bizProcessChild.getPrice1()) {
                        return error("【" + bizProcessChild.getString3() + " 】此科目预算报销的金额不足，请确认");
                    }
                    totalPrice = totalPrice.add(BigDecimal.valueOf(bizProcessChild.getPrice1()));
                }
            }
        }
        bizProcessData.setPrice1(totalPrice.doubleValue());
        bizProcessData.setString2("FP" + DateUtils.dateTimeNow() + RandomStringUtils.randomNumeric(3));
        int insertReturn = bizProcessDataService.insertBizProcessData(bizProcessData);
        Long dataId = bizProcessData.getDataId();

        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                bizProcessChild.setDataId(dataId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild);
            }
        }
        return toAjax(insertReturn);
    }

    /**
     * 修改合同管理
     */
    @GetMapping("/edit/{dataId}")
    public String edit(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(dataId);

        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/edit";
    }

    /**
     * 修改合同管理
     */
    @GetMapping("/edit1/{dataId}")
    public String edit1(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(dataId);
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/edit1";
    }

    /**
     * 修改保存合同管理
     */
    @RequiresPermissions("fmis:payment:edit")
    @Log(title = "报销管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProcessData bizProcessData) {

        String productArrayStr = bizProcessData.getProductParmters();
        // 获取总价格
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                if (bizProcessChild.getPrice1() != null && bizProcessChild.getPrice1() > 0) {
                    totalPrice = totalPrice.add(new BigDecimal(bizProcessChild.getPrice1()));
                }
                if (bizProcessChild.getPrice2() != null && bizProcessChild.getPrice2() > 0) {
                    totalPrice = totalPrice.add(new BigDecimal(bizProcessChild.getPrice2()));
                }
                if (bizProcessChild.getPrice3() != null && bizProcessChild.getPrice3() > 0) {
                    totalPrice = totalPrice.add(new BigDecimal(bizProcessChild.getPrice3()));
                }
            }
        }
        bizProcessData.setPrice1(totalPrice.doubleValue());
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
                bizProcessChild.setDataId(dataId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild);

            }
        }

        return toAjax(updateReturn);
    }

    /**
     * 修改保存合同管理
     */
    @RequiresPermissions("fmis:payment:edit")
    @Log(title = "报销管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit1")
    @ResponseBody
    public AjaxResult editSave1(BizProcessData bizProcessData) {

        String productArrayStr = bizProcessData.getProductParmters();
        // 获取总价格
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizProcessChild bizProcessChild = JSONObject.parseObject(json.toJSONString(), BizProcessChild.class);
                if (bizProcessChild.getPrice1() != null && bizProcessChild.getPrice1() > 0) {
                    totalPrice = totalPrice.add(new BigDecimal(bizProcessChild.getPrice1()));
                }
            }
        }
        bizProcessData.setPrice1(totalPrice.doubleValue());
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
                bizProcessChild.setDataId(dataId);
                bizProcessChildService.insertBizProcessChild(bizProcessChild);

            }
        }

        return toAjax(updateReturn);
    }

    /**
     * 删除合同管理
     */
    @RequiresPermissions("fmis:payment:remove")
    @Log(title = "报销管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult remove(String ids) {
        int i = bizProcessDataService.deleteBizProcessDataByIds(ids);
        bizProcessChildService.deleteBizProcessChildByDataIds(ids);
        return toAjax(i);
    }

    @PostMapping("/report")
    @ResponseBody
    public AjaxResult report() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        return toAjax(bizProcessDataService.subReportBizQuotationBorrowing(bizQuotation));
    }


}
