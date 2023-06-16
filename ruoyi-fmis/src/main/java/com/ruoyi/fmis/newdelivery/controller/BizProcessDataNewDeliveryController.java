package com.ruoyi.fmis.newdelivery.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelProcessDataUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.flow.service.IBizFlowService;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.util.Util;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysRoleService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.itextpdf.PdfUtil.getAbsoluteFile;

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
    private ISysDictDataService sysDictDataService;

    @Autowired
    private IBizCustomerService bizCustomerService;

    @Autowired
    private IBizFlowService bizFlowService;

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
        // 上报之前清除历史审批记录
        bizFlowService.deleteBizFlowByBizId(bizProcessData.getDataId());
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
                    bizProcessChild.setPrice1(bizProcessChild.getPrice1() * Double.parseDouble(bizProcessChild.getString4()));
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

            long inventorySum = inventoryChilds.stream().mapToLong(e -> StringUtils.toLong(e.getString11())).sum();

            // 总的出库数量
            Long outTotalNum = StringUtils.toLong(bizProcessChild.getString13());
            if (inventorySum < outTotalNum) {
                return error("库存不够，请检查库存");
            }
            for (BizProcessChild inventoryChild : inventoryChilds) {
                Long string11Long = StringUtils.toLong(inventoryChild.getString11());
                if(string11Long <= outTotalNum){
                    string15 = inventoryChild.getChildId() + "";
                    String string16 = inventoryChild.getString16();
                    inventoryChild.setString16(StringUtils.toLong(string16) + string11Long + "");
                    //inventoryChild 之前的报检的数据
                    //bizProcessChild发货的数据
                    inventoryChild.setString11("0");
                    inventoryChild.setUpdateTime(new Date());
                    inventoryChild.setUpdateBy(ShiroUtils.getUserId() + "");
                    inventoryChild.setString14("1");
                    bizProcessChildService.updateBizProcessChild(inventoryChild);
                    outTotalNum = outTotalNum - string11Long;
                }else{
                    string15 = inventoryChild.getChildId() + "";
                    String string16 = inventoryChild.getString16();
                    inventoryChild.setString16(StringUtils.toLong(string16) + outTotalNum + "");
                    //inventoryChild 之前的报检的数据
                    //bizProcessChild发货的数据
                    inventoryChild.setString11((string11Long - outTotalNum) + "");
                    inventoryChild.setUpdateTime(new Date());
                    inventoryChild.setUpdateBy(ShiroUtils.getUserId() + "");
                    inventoryChild.setString14("1");
                    bizProcessChildService.updateBizProcessChild(inventoryChild);
                }
            }


        } else {
          /*  if (StringUtils.toLong(child.getString2()) < 5) {
                bizProcessChild.setString7(child.getString7());
            } else {
                bizProcessChild.setString8(child.getString8());
            }
            if (StringUtils.toLong(child.getString2()) == 3) {
                bizProcessChild.setString8(child.getString8());
            }*/

            bizProcessChild.setString5(child.getString5());
            bizProcessChild.setString6(child.getString6());
            bizProcessChild.setString7(child.getString7());
            bizProcessChild.setString8(child.getString8());
            List<BizProcessChild> inventoryChilds = bizProcessChildService.selectBizProcessChildListForKuCun(bizProcessChild);
            bizProcessChild.setString13(child.getString13());
            if (inventoryChilds == null || inventoryChilds.size() == 0) {
                return error("库存不够，请检查库存");
            }
            BizProcessChild inventoryChild = inventoryChilds.get(0);
            long kucun = 0;
            for (BizProcessChild inventoryChild1 : inventoryChilds) {
                kucun = kucun + StringUtils.toLong(inventoryChild1.getString11());
            }

            String string13  = child.getString13();
            if (string13.contains(".")) {
                string13 =  new BigDecimal(Double.parseDouble(string13)).longValue() + "";
            }
            if (kucun < StringUtils.toLong(string13)) {
                return error("库存不够，请检查库存");
            }
            long chuku =  StringUtils.toLong(string13);

            for (BizProcessChild inventoryChild2 : inventoryChilds) {
//                kucun = kucun + StringUtils.toLong(inventoryChild2.getString11());
                string15 = inventoryChild2.getChildId() + "";
                String string16 = inventoryChild2.getString16();
                long kucun1 = StringUtils.toLong(inventoryChild2.getString11());
                //出库完成
                if (chuku == 0) {
                    break;
                }
                if (kucun1 <= chuku) {
                    //单个小于出库 从多个出库
                    inventoryChild2.setString11("0");
                    chuku = chuku - kucun1;
                    inventoryChild2.setString16(StringUtils.toLong(string16) + kucun1 + "");
                } else {
                    //出库数量
                    inventoryChild2.setString16(StringUtils.toLong(string16) + chuku + "");
                    inventoryChild2.setString11((kucun1 - chuku) + "");
                }
                inventoryChild2.setUpdateTime(new Date());
                inventoryChild2.setUpdateBy(ShiroUtils.getUserId() + "");
                inventoryChild2.setString14("1");
                bizProcessChildService.updateBizProcessChild(inventoryChild2);
            }

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

        return toAjax(bizProcessDataService.deleteNewDeliveryById(ids));
    }

    @PostMapping("/report")
    @ResponseBody
    public AjaxResult report() {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizQuotation = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        return toAjax(bizProcessDataService.subReportBizQuotation(bizQuotation));
    }

    /**
     * 导出发货单
     * @return
     */
    @PostMapping("/exportNewdelivery")
    @ResponseBody
    public AjaxResult exportNewdelivery(Long dataId) {
        try {

            BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(dataId);

            BizProcessChild queryBizProcessChild = new BizProcessChild();
            queryBizProcessChild.setDataId(bizProcessData.getDataId());
            List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildList(queryBizProcessChild);

            Workbook workbook = new HSSFWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            Sheet sheet = workbook.createSheet("发货单");
            // 单元格样式
            CellStyle cellCenterBlackFont = ExcelProcessDataUtils.cellCenterBlackFont(workbook);
            CellStyle cellCenterBlack = ExcelProcessDataUtils.cellCenterBlack(workbook);

            Row row1 = sheet.createRow(0);
            row1.setHeight((short) 700);
            CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 11);
            sheet.addMergedRegion(cra1);
            Cell cell_title_1 = row1.createCell(0);
            cell_title_1.setCellValue("北京好利阀业集团有限公司发货单");
            CellStyle titleCell = ExcelProcessDataUtils.titleCell(workbook);
            cell_title_1.setCellStyle(titleCell);

            Row row3 = sheet.createRow(1);
            row3.setHeight((short) 700);
            Cell cell_30 = row3.createCell(0);
            cell_30.setCellValue("发货日期：");
            cell_30.setCellStyle(cellCenterBlackFont);
            Cell cell_31 = row3.createCell(1);
            cell_31.setCellValue(DateUtils.dateTime(bizProcessData.getDatetime1()));
            cell_31.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
            Cell cell_36 = row3.createCell(3);
            cell_36.setCellValue("客户订单号：");
            cell_36.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 4));
            Cell cell_37 = row3.createCell(5);
            cell_37.setCellValue(bizProcessData.getString2());
            cell_37.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));
            Cell cell_311 = row3.createCell(7);
            cell_311.setCellValue("内销合同号：");
            cell_311.setCellStyle(cellCenterBlackFont);
            Cell cell_314 = row3.createCell(8);
            cell_314.setCellValue(bizProcessData.getString1());
            cell_314.setCellStyle(cellCenterBlackFont);
            Cell cell_317 = row3.createCell(9);
            cell_317.setCellValue("运输方式：");
            cell_317.setCellStyle(cellCenterBlackFont);
            Cell cell_320 = row3.createCell(10);
            List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType("transport_type");
            Map<String, String> transportTypeMap = dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel, (a, b) -> a));
            cell_320.setCellValue(transportTypeMap.getOrDefault(bizProcessData.getString14(), ""));
            cell_320.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 11));
            List<Integer> row2CellList = Arrays.asList(2, 4, 6, 11);
            ExcelProcessDataUtils.fillInBlankCell(row3, cellCenterBlackFont, row2CellList);

            Row row4 = sheet.createRow(2);
            row4.setHeight((short) 700);
            Cell cell_40 = row4.createCell(0);
            cell_40.setCellValue("收货信息：");
            cell_40.setCellStyle(cellCenterBlackFont);
            Cell cell_41 = row4.createCell(1);
            cell_41.setCellValue(bizProcessData.getString7() + " " + bizProcessData.getString8() + " " + bizProcessData.getString9());
            cell_41.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 8));
            Cell cell_44 = row4.createCell(9);
            cell_44.setCellValue("运费支付方式：");
            cell_44.setCellStyle(cellCenterBlackFont);
            Cell cell_46 = row4.createCell(10);
            cell_46.setCellValue(bizProcessData.getString12());
            cell_46.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 10, 11));
            List<Integer> row3CellList = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 11);
            ExcelProcessDataUtils.fillInBlankCell(row4, cellCenterBlackFont, row3CellList);

            Row row44 = sheet.createRow(3);
            row4.setHeight((short) 700);
            Cell cell_47 = row44.createCell(0);
            cell_47.setCellValue("序号：");
            cell_47.setCellStyle(cellCenterBlackFont);
            Cell cell_48 = row44.createCell(1);
            cell_48.setCellValue("产品名称：");
            cell_48.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 2));
            Cell cell_43 = row44.createCell(3);
            cell_43.setCellValue("客户货号：");
            cell_43.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));
            Cell cell_45 = row44.createCell(5);
            cell_45.setCellValue("产品ID：");
            cell_45.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 6));
            Cell cell_477 = row44.createCell(7);
            cell_477.setCellValue("规格：");
            cell_477.setCellStyle(cellCenterBlackFont);
            Cell cell_488 = row44.createCell(8);
            cell_488.setCellValue("颜色：");
            cell_488.setCellStyle(cellCenterBlackFont);
            Cell cell_499 = row44.createCell(9);
            cell_499.setCellValue("压力：");
            cell_499.setCellStyle(cellCenterBlackFont);
            Cell cell_410 = row44.createCell(10);
            cell_410.setCellValue("数量：");
            cell_410.setCellStyle(cellCenterBlackFont);
            Cell cell_411 = row44.createCell(11);
            cell_411.setCellValue("备注：");
            cell_411.setCellStyle(cellCenterBlackFont);
            List<Integer> row4CellList = Arrays.asList(2, 4, 6);
            ExcelProcessDataUtils.fillInBlankCell(row44, cellCenterBlackFont, row4CellList);

            int rowCount = 3;
            int serial = 0;
            int count = 0;
            if (!CollectionUtils.isEmpty(bizProcessChildList)) {
                for (BizProcessChild bizProcessChild : bizProcessChildList) {
                    rowCount++;
                    serial++;

                    Row rowi = sheet.createRow(rowCount);
                    rowi.setHeight((short) 700);
                    Cell cell = rowi.createCell(0);
                    cell.setCellValue(serial);
                    cell.setCellStyle(cellCenterBlackFont);
                    Cell cell1 = rowi.createCell(1);
                    cell1.setCellValue(bizProcessChild.getString6());
                    cell1.setCellStyle(cellCenterBlackFont);
                    sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));
                    Cell cell3 = rowi.createCell(3);
                    cell3.setCellValue("");
                    cell3.setCellStyle(cellCenterBlackFont);
                    sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 3, 4));
                    Cell cell5 = rowi.createCell(5);
                    cell5.setCellValue(bizProcessChild.getString7());
                    cell5.setCellStyle(cellCenterBlackFont);
                    sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 5, 6));
                    Cell cell7 = rowi.createCell(7);
                    cell7.setCellValue(bizProcessChild.getString8());
                    cell7.setCellStyle(cellCenterBlackFont);
                    Cell cell8 = rowi.createCell(8);
                    cell8.setCellValue("");
                    cell8.setCellStyle(cellCenterBlackFont);
                    Cell cell9 = rowi.createCell(9);
                    cell9.setCellValue("");
                    cell9.setCellStyle(cellCenterBlackFont);
                    Cell cell10 = rowi.createCell(10);
                    if (StringUtils.isNotEmpty(bizProcessChild.getString13())) {
                        count += Integer.valueOf(bizProcessChild.getString13());
                    }
                    cell10.setCellValue(bizProcessChild.getString13());
                    cell10.setCellStyle(cellCenterBlackFont);
                    Cell cell11 = rowi.createCell(11);
                    cell11.setCellValue("");
                    cell11.setCellStyle(cellCenterBlackFont);
                    List<Integer> row5CellList = Arrays.asList(2, 4, 6);
                    ExcelProcessDataUtils.fillInBlankCell(rowi, cellCenterBlackFont, row5CellList);
                }
            }
            rowCount++;
            int aa = rowCount++;
            Row row5 = sheet.createRow(aa);
            row5.setHeight((short) 700);
            Cell cell_50 = row5.createCell(0);
            cell_50.setCellValue("");
            cell_50.setCellStyle(cellCenterBlackFont);
            Cell cell_51 = row5.createCell(1);
            cell_51.setCellValue("合计");
            cell_51.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(aa, aa, 1, 9));
            Cell cell_52 = row5.createCell(10);
            cell_52.setCellValue(count);
            cell_52.setCellStyle(cellCenterBlackFont);
            Cell cell_53 = row5.createCell(11);
            cell_53.setCellValue("");
            cell_53.setCellStyle(cellCenterBlackFont);
            List<Integer> row6CellList = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9);
            ExcelProcessDataUtils.fillInBlankCell(row5, cellCenterBlackFont, row6CellList);

            int bb = rowCount++;
            Row row6 = sheet.createRow(bb);
            row6.setHeight((short) 700);
            Cell cell_60 = row6.createCell(0);
            cell_60.setCellValue("备注：");
            cell_60.setCellStyle(cellCenterBlackFont);
            Cell cell_61 = row6.createCell(1);
            cell_61.setCellValue("现场补货急用，请帮忙安排。");
            cell_61.setCellStyle(cellCenterBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(bb, bb, 1, 11));
            List<Integer> row7CellList = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
            ExcelProcessDataUtils.fillInBlankCell(row6, cellCenterBlackFont, row7CellList);

            int cc = rowCount++;
            Row row7 = sheet.createRow(cc);
            row7.setHeight((short) 500);
            Cell cell_80 = row7.createCell(0);
            cell_80.setCellValue("是否要求物流返回签收单：");
            cell_80.setCellStyle(cellCenterBlack);
            sheet.addMergedRegion(new CellRangeAddress(cc, cc, 0, 2));

            int ee = rowCount++;
            Row row8 = sheet.createRow(ee);
            row8.setHeight((short) 500);
            Cell cell_90 = row8.createCell(0);
            cell_90.setCellValue("财务：");
            cell_90.setCellStyle(cellCenterBlack);
            Cell cell_91 = row8.createCell(4);
            cell_91.setCellValue("审核：");
            cell_91.setCellStyle(cellCenterBlack);
            Cell cell_92 = row8.createCell(7);
            cell_92.setCellValue("发货通知人：");
            cell_92.setCellStyle(cellCenterBlack);
            Cell cell_93 = row8.createCell(8);
            cell_93.setCellValue(bizProcessData.getString8());
            cell_93.setCellStyle(cellCenterBlack);
            Cell cell_94 = row8.createCell(10);
            cell_94.setCellValue("客户编号：");
            cell_94.setCellStyle(cellCenterBlack);
            Cell cell_95 = row8.createCell(11);
            cell_95.setCellValue(bizProcessData.getString4());
            cell_95.setCellStyle(cellCenterBlack);

            String filename = ExcelUtil.encodingFilenameByXls("发货单");
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
