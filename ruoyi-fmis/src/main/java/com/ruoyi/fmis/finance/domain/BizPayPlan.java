package com.ruoyi.fmis.finance.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 付款计划（基于付款申请记录）对象 biz_pay_plan
 *
 * @author murphy
 * @date 2020-11-06
 */
public class BizPayPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long payPlanId;

    /** 付款申请Id（biz_process_data 里面的data_id） */
    private Long payDataId;

    /** 申请编号 */
    @Excel(name = "申请编号")
    private String applyNo;

    /** 申请日期 */
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyDate;

    /** 采购合同ID */
    private String contractId;

    /** 采购合同号 */
    @Excel(name = "合同号")
    private String contractNo;

    /** 采购合同付款方式 */
    @Excel(name = "合同付款方式")
    private String contractPayWay;

    /** 申请付款单位 */
    @Excel(name = "付款单位")
    private String applyPayCompany;

    /** 收款单位 */
    @Excel(name = "收款单位")
    private String applyCollectionCompany;

    /** 付款金额 */
    @Excel(name = "付款金额")
    private Double applyAmount;

    /** 付款备注 */
    @Excel(name = "付款备注")
    private String applyRemark;

    /** 付款单位 */
    private String payCompany;

    /** 付款账号 */
    private String payAccount;

    /** 付款方式; 字典：pay_way */
    @Excel(name = "付款方式")
    private String payWay;

    /** 付款类型: 默认是货款 */
    private String paymentType;

    /** 付款状态：0.未付；1.已付； 字典：pay_status */
    @Excel(name = "付款状态",readConverterExp = "0=未付款,1=已付款")
    private String status;

    /** 付款日期 */
    @Excel(name = "付款日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payDate;

    /** 记账方式 */
    @Excel(name = "记账方式",readConverterExp = "1=现金日记账,2=银行日记账")
    private String bookingType;

    public void setPayPlanId(Long payPlanId) {
        this.payPlanId = payPlanId;
    }

    public Long getPayPlanId() {
        return payPlanId;
    }
    public void setPayDataId(Long payDataId) {
        this.payDataId = payDataId;
    }

    public Long getPayDataId() {
        return payDataId;
    }
    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getApplyNo() {
        return applyNo;
    }
    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getApplyDate() {
        return applyDate;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractId() {
        return contractId;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractNo() {
        return contractNo;
    }
    public void setContractPayWay(String contractPayWay) {
        this.contractPayWay = contractPayWay;
    }

    public String getContractPayWay() {
        return contractPayWay;
    }
    public void setApplyPayCompany(String applyPayCompany) {
        this.applyPayCompany = applyPayCompany;
    }

    public String getApplyPayCompany() {
        return applyPayCompany;
    }
    public void setApplyCollectionCompany(String applyCollectionCompany) {
        this.applyCollectionCompany = applyCollectionCompany;
    }

    public String getApplyCollectionCompany() {
        return applyCollectionCompany;
    }
    public void setApplyAmount(Double applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Double getApplyAmount() {
        return applyAmount;
    }
    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public String getApplyRemark() {
        return applyRemark;
    }
    public void setPayCompany(String payCompany) {
        this.payCompany = payCompany;
    }

    public String getPayCompany() {
        return payCompany;
    }
    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayAccount() {
        return payAccount;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayWay() {
        return payWay;
    }
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getPayDate() {
        return payDate;
    }
    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getBookingType() {
        return bookingType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("payPlanId", getPayPlanId())
                .append("payDataId", getPayDataId())
                .append("applyNo", getApplyNo())
                .append("applyDate", getApplyDate())
                .append("contractId", getContractId())
                .append("contractNo", getContractNo())
                .append("contractPayWay", getContractPayWay())
                .append("applyPayCompany", getApplyPayCompany())
                .append("applyCollectionCompany", getApplyCollectionCompany())
                .append("applyAmount", getApplyAmount())
                .append("applyRemark", getApplyRemark())
                .append("payCompany", getPayCompany())
                .append("payAccount", getPayAccount())
                .append("payWay", getPayWay())
                .append("paymentType", getPaymentType())
                .append("status", getStatus())
                .append("payDate", getPayDate())
                .append("bookingType", getBookingType())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}