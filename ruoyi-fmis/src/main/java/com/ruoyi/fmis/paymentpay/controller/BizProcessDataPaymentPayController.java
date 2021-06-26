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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        return prefix + "/viewDetail";
    }

    @GetMapping("/viewDetail1")
    public String viewDetail1(ModelMap mmap) {
        String dataId = getRequest().getParameter("dataId");
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(Long.parseLong(dataId));
        mmap.put("bizProcessData", bizProcessData);
        return prefix + "/viewDetail1";
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
                    bizBill.setString2(bizProcessData.getString10());
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
        return toAjax(updateReturn);
    }


    @GetMapping("/edit1/{dataId}")
    public String edit1(@PathVariable("dataId") Long dataId, ModelMap mmap) {
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataPaymentById(dataId);
        mmap.put("bizProcessData", bizProcessData);
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
                    bizBill.setString1(bizProcessData.getString10());
                    bizBill.setString2(bizProcessData.getString5());
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
