package com.ruoyi.fmis.paymentpay.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.domain.BizBill;
import com.ruoyi.fmis.finance.domain.BizPayPlan;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.fmis.finance.service.IBizBillService;
import com.ruoyi.fmis.paymentpay.bean.PaymentPayExportVo;
import com.ruoyi.fmis.util.BeanUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 费用报销-付款
 */
@Controller
@RequestMapping("/fmis/paymentpay")
public class BizProcessDataPaymentPayController extends BaseController {
    private String prefix = "fmis/paymentpay";

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private IBizProcessChildService bizProcessChildService;

    @Autowired
    private IBizBankBillService bizBankBillService;

    @Autowired
    private IBizBillService bizBillService;


    @RequiresPermissions("fmis:paymentpay:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
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

    /**
     * 查询审核完成的报销单
     */
    @RequiresPermissions("fmis:paymentpay:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProcessData bizProcessData) {
        bizProcessData.setRoleType("0");
        startPage();
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefPayment(bizProcessData);
        return getDataTable(list);
    }

    @RequiresPermissions("fmis:paymentpay:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProcessData bizProcessData) {
        bizProcessData.setRoleType("0");
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefPayment(bizProcessData);
        List<PaymentPayExportVo> exportList = BeanUtil.copyProperties(list, PaymentPayExportVo.class);
        ExcelUtil<PaymentPayExportVo> util = new ExcelUtil<PaymentPayExportVo>(PaymentPayExportVo.class);
        return util.exportExcel(exportList, "报销支付");
    }


    @GetMapping("/viewDetail")
    public String viewDetail(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        // 借款单
        wrapBorrowingInfos(mmap, Long.valueOf(bizProcessData.getCreateBy()));
        return prefix + "/viewDetail";
    }

    @GetMapping("/viewDetail1")
    public String viewDetail1(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        // 借款单
        wrapBorrowingInfos(mmap, Long.valueOf(bizProcessData.getCreateBy()));
        return prefix + "/viewDetail1";
    }

    private void wrapBorrowingInfos(ModelMap mmap, Long userId) {
        // 借款单
        List<BizProcessData> bizProcessData = bizProcessDataService.selectAllBorrowingWithNoPayAndAgree(userId);
        Map<String, Double> borrowingMap = new HashMap<>();
        List<String> borrowingNoList = new ArrayList<>();
        for (BizProcessData data : bizProcessData) {
            borrowingNoList.add(data.getString2());
            borrowingMap.put(data.getString2(), data.getPrice1());
        }
        mmap.put("borrowingNoList", borrowingNoList);
        mmap.put("borrowingMap", borrowingMap);
    }

    @GetMapping("/viewExamineHistory")
    public String viewExamine(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        String bizId = getRequest().getParameter("bizId");
        mmap.put("bizId", dataId);
        mmap.put("bizTable", bizId);
        return prefix + "/viewExamineHistory";
    }

    @GetMapping("/edit/{dataId}")
    public String edit(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(dataId);
        mmap.put("bizProcessData", bizProcessData);
        // 借款单
        wrapBorrowingInfos(mmap, Long.valueOf(bizProcessData.getCreateBy()));
        return prefix + "/edit";
    }


    @RequiresPermissions("fmis:paymentpay:edit")
    @Log(title = "差旅报销付款", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult editSave(BizProcessData bizProcessData) {
        int updateReturn = bizProcessDataService.updateBizProcessData(bizProcessData);
        if (updateReturn > 0 && "1".equals(bizProcessData.getString11())) {
            // 已付款
            // 判断是否为冲抵报销
            if (bizProcessData.getPaymentType() == 1) {
                // 如果为冲抵报销，需要计算 冲抵之后金额并进行记账，同时更改冲抵借款单的状态
                double balanceAmount = bizProcessData.getBalanceAmount();
                double price1 = bizProcessData.getPrice1();
                double actualAmount = price1 - balanceAmount;
                String bookingType = bizProcessData.getString13();
                insertKeepAccountsRecord(bookingType, bizProcessData, actualAmount);
            } else {
                String bookingType = bizProcessData.getString13();
                if ("1".equals(bookingType)) {
                    // 添加现金日记账
                    if (!bizBillService.existsByCertificateNumber(bizProcessData.getString2())) {
                        BizBill bizBill = new BizBill();
                        bizBill.setType("1");
                        bizBill.setDeptId(bizProcessData.getString6());
                        bizBill.setCertificateNumber(bizProcessData.getString2());
                        bizBill.setD(bizProcessData.getDatetime3());
                        bizBill.setPaymentType("1");// 费用报销
                        bizBill.setPayment(bizProcessData.getPrice1());
                        bizBill.setRemark(bizProcessData.getRemark());
                        // 付款单位
                        bizBill.setString2(bizProcessData.getString10());
                        // 收款单位
                        bizBill.setString1(bizProcessData.getString3());
                        bizBillService.insertBizBill(bizBill);
                    }
                } else if ("2".equals(bookingType)) {
                    // 添加银行日记账
                    if (!bizBankBillService.existsByCertificateNumber(bizProcessData.getString2())) {
                        BizBankBill bizBankBill = new BizBankBill();
                        bizBankBill.setType("2");
                        bizBankBill.setDeptId(bizProcessData.getString6());
                        bizBankBill.setCertificateNumber(bizProcessData.getString2());
                        bizBankBill.setOperateDate(bizProcessData.getDatetime3());
                        bizBankBill.setPaymentType("1");
                        bizBankBill.setPayment(bizProcessData.getPrice1());
                        bizBankBill.setRemark(bizProcessData.getRemark());
                        bizBankBill.setPayCompany(bizProcessData.getString10());
                        bizBankBill.setPayAccount(bizProcessData.getString12());
                        bizBankBill.setCollectCompany(bizProcessData.getString3());
                        bizBankBillService.insertBizBankBill(bizBankBill);
                    }
                }
            }

        }
        return toAjax(updateReturn);
    }

    /**
     * 区分记账类型
     *
     * @param bookingType
     * @param bizProcessData
     * @param actualAmount
     */
    private void insertKeepAccountsRecord(String bookingType, BizProcessData bizProcessData, double actualAmount) {
        if ("1".equals(bookingType)) {
            // 现金日记账
            insertKeepAccountsRecordForBill(bizProcessData, actualAmount);
        } else {
            // 银行日记账
            insertKeepAccountsRecordForBank(bizProcessData, actualAmount);
        }

    }

    /**
     * 银行日记账
     *
     * @param bizProcessData
     * @param actualAmount
     */
    private void insertKeepAccountsRecordForBank(BizProcessData bizProcessData, double actualAmount) {
        // 报销单放款记账
        insertBankBillRecord(0, "1", bizProcessData.getString6(), bizProcessData.getString2(),
                bizProcessData.getDatetime3(), bizProcessData.getPrice1(), bizProcessData.getRemark(),
                bizProcessData.getString10(), bizProcessData.getString3(), bizProcessData.getString12());
        if (actualAmount >= 0) {
            // 借款单金额全部冲抵
            // 生成借款单 回款 记账
            // 生成报销单 放款 记账
            // 更新借款单状态为已结清

            // 借款单还款
            insertBankBillRecord(1, "3", bizProcessData.getString6(), bizProcessData.getString2(), bizProcessData.getDatetime3(), bizProcessData.getBalanceAmount(),
                    String.format("报销单冲抵还款，单号：s%，金额：s%", bizProcessData.getString2(),
                            bizProcessData.getPrice1()), bizProcessData.getString3(), bizProcessData.getString10(), "");
            // 更新状态：借款单单号， 完结状态 ，已结算金额
            updateBorrowingStatusAndAmount(bizProcessData.getBalanceBorrowNo(), 2, bizProcessData.getBalanceAmount());
        }

        if (actualAmount < 0) {
            // 借款单金额部分冲抵
            // 生成借款单 回款 记账
            // 生成报销单 放款 记账
            // 更新借款单状态为 部分结清， 结算金额
            // 借款单还款记账
            insertBankBillRecord(1, "3", bizProcessData.getString6(), bizProcessData.getString2(), bizProcessData.getDatetime3(), bizProcessData.getPrice1(),
                    String.format("报销单冲抵还款，单号：s%，金额：s%", bizProcessData.getString2(),
                            bizProcessData.getPrice1()), bizProcessData.getString3(), bizProcessData.getString10(), "");
            updateBorrowingStatusAndAmount(bizProcessData.getBalanceBorrowNo(), 1, bizProcessData.getPrice1());

        }
    }

    private void insertBankBillRecord(int wayType, String paymentType, String deptId, String certificateNumber, Date d, double price1, String remark, String payer, String payee, String payAccount) {

        BizBankBill bizBankBill = new BizBankBill();
        bizBankBill.setType("2");
        bizBankBill.setDeptId(deptId);
        bizBankBill.setCertificateNumber(certificateNumber);
        bizBankBill.setOperateDate(d);
        if(wayType == 0) {
            bizBankBill.setPaymentType(paymentType);
        } else {
            bizBankBill.setCollectionType(paymentType);
        }
        bizBankBill.setPayment(price1);
        bizBankBill.setRemark(remark);
        bizBankBill.setPayCompany(payer);
        bizBankBill.setPayAccount(payAccount);
        bizBankBill.setCollectCompany(payee);
        bizBankBillService.insertBizBankBill(bizBankBill);

    }


    // 现金日记账
    private void insertKeepAccountsRecordForBill(BizProcessData bizProcessData, double actualAmount) {
        // 报销单放款记账
        insertBillRecord(0, "1", bizProcessData.getString6(), bizProcessData.getString2(), bizProcessData.getDatetime3(), bizProcessData.getPrice1(), bizProcessData.getRemark(), bizProcessData.getString10(), bizProcessData.getString3());
        if (actualAmount >= 0) {
            // 借款单金额全部冲抵
            // 生成借款单 回款 记账
            // 生成报销单 放款 记账
            // 更新借款单状态为已结清

            // 借款单还款
            insertBillRecord(1, "3", bizProcessData.getString6(), bizProcessData.getString2(), bizProcessData.getDatetime3(), bizProcessData.getBalanceAmount(),
                    String.format("报销单冲抵还款，单号：s%，金额：s%", bizProcessData.getString2(),
                            bizProcessData.getPrice1()), bizProcessData.getString3(), bizProcessData.getString10());
            // 更新状态：借款单单号， 完结状态 ，已结算金额
            updateBorrowingStatusAndAmount(bizProcessData.getBalanceBorrowNo(), 2, bizProcessData.getBalanceAmount());
        }

        if (actualAmount < 0) {
            // 借款单金额部分冲抵
            // 生成借款单 回款 记账
            // 生成报销单 放款 记账
            // 更新借款单状态为 部分结清， 结算金额
            // 借款单还款记账
            insertBillRecord(1, "3", bizProcessData.getString6(), bizProcessData.getString2(), bizProcessData.getDatetime3(), bizProcessData.getPrice1(),
                    String.format("报销单冲抵还款，单号：s%，金额：s%", bizProcessData.getString2(),
                            bizProcessData.getPrice1()), bizProcessData.getString3(), bizProcessData.getString10());
            updateBorrowingStatusAndAmount(bizProcessData.getBalanceBorrowNo(), 1, bizProcessData.getPrice1());

        }
    }

    /**
     * 更新借款单数据
     * @param balanceBorrowNo
     * @param borrowingStatus 0 未还款 1 部分结清 2 已结清
     * @param price
     */
    private void updateBorrowingStatusAndAmount(String balanceBorrowNo, int borrowingStatus, Double price) {
        BizProcessData bizProcessData = new BizProcessData();
        bizProcessData.setBizId("borrowing");
        bizProcessData.setString2(balanceBorrowNo);
        List<BizProcessData> bizProcessData1 = bizProcessDataService.selectBizProcessDataVoRefBorrowing(bizProcessData);
        if (!CollectionUtils.isEmpty(bizProcessData1)) {
            BizProcessData bizProcessData2 = bizProcessData1.get(0);
            //设置结算字段
            bizProcessData2.setPrice2(price);
            //设置结算状态
            bizProcessData2.setString13(String.valueOf(borrowingStatus));
            bizProcessDataService.updateBizProcessData(bizProcessData2);
        }
    }

    /**
     *
     * @param wayType 0 放款 1 收款
     * @param paymentType wayType = 0 时 1 // 费用  wayType = 1 时 3 // 借款
     * @param deptId
     * @param certificateNumber
     * @param d
     * @param price1
     * @param remark
     * @param payer
     * @param payee
     */
    private void insertBillRecord(int wayType, String paymentType, String deptId, String certificateNumber, Date d, double price1, String remark, String payer, String payee) {
        BizBill bizBill = new BizBill();
        bizBill.setType("1");
        bizBill.setDeptId(deptId);
        bizBill.setCertificateNumber(certificateNumber);
        bizBill.setD(d);
        if (wayType == 0) {
            // 付款
            bizBill.setPaymentType(paymentType);
        } else {
            // 收款
            bizBill.setCollectionType(paymentType);
        }
        bizBill.setPayment(price1);
        bizBill.setRemark(remark);
        // 付款单位
        bizBill.setString2(payer);
        // 收款单位
        bizBill.setString1(payee);
        bizBillService.insertBizBill(bizBill);
    }


    @GetMapping("/edit1/{dataId}")
    public String edit1(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(dataId);
        mmap.put("bizProcessData", bizProcessData);
        // 借款单
        wrapBorrowingInfos(mmap, Long.valueOf(bizProcessData.getCreateBy()));
        return prefix + "/edit1";
    }


    @RequiresPermissions("fmis:paymentpay:edit")
    @Log(title = "费用报销付款", businessType = BusinessType.UPDATE)
    @PostMapping("/edit1")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult editSave1(BizProcessData bizProcessData) {
        int updateReturn = bizProcessDataService.updateBizProcessData(bizProcessData);
        if (updateReturn > 0 && "1".equals(bizProcessData.getString11())) {
            String bookingType = bizProcessData.getString13();
            if ("1".equals(bookingType)) {
                // 添加现金日记账
                if (!bizBillService.existsByCertificateNumber(bizProcessData.getString2())) {
                    BizBill bizBill = new BizBill();
                    bizBill.setType("1");
                    bizBill.setDeptId(bizProcessData.getString6());
                    bizBill.setCertificateNumber(bizProcessData.getString2());
                    bizBill.setD(bizProcessData.getDatetime3());
                    bizBill.setPaymentType("1");// 费用报销
                    bizBill.setPayment(bizProcessData.getPrice1());
                    bizBill.setRemark(bizProcessData.getRemark());
                    bizBill.setString1(bizProcessData.getString3());
                    bizBill.setString2(bizProcessData.getString10());
                    bizBillService.insertBizBill(bizBill);
                }
            } else if ("2".equals(bookingType)) {
                // 添加银行日记账
                if (!bizBankBillService.existsByCertificateNumber(bizProcessData.getString2())) {
                    BizBankBill bizBankBill = new BizBankBill();
                    bizBankBill.setType("2");
                    bizBankBill.setDeptId(bizProcessData.getString6());
                    bizBankBill.setCertificateNumber(bizProcessData.getString2());
                    bizBankBill.setOperateDate(bizProcessData.getDatetime3());
                    bizBankBill.setPaymentType("1");
                    bizBankBill.setPayment(bizProcessData.getPrice1());
                    bizBankBill.setRemark(bizProcessData.getRemark());
                    bizBankBill.setPayCompany(bizProcessData.getString10());
                    bizBankBill.setPayAccount(bizProcessData.getString12());
                    bizBankBill.setCollectCompany(bizProcessData.getString5());
                    bizBankBillService.insertBizBankBill(bizBankBill);
                }
            }
        }
        return toAjax(updateReturn);
    }

}
