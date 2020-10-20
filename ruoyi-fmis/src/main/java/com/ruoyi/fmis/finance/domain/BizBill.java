package com.ruoyi.fmis.finance.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.SysDept;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 现金日记账对象 biz_bill
 *
 * @author frank
 * @date 2020-06-25
 */
public class BizBill extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Double billAmount;

    @Getter
    @Setter
    private SysDept dept;
    /** ID */
    private Long billId;

    /** 序号 */
    @Excel(name = "序号")
    private String serialNo;

    /** 日期 */
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operateDate;

    /** 凭证号 */
    @Excel(name = "凭证号")
    private String certificateNumber;

    /** 收付款方式 */
    @Excel(name = "收付款方式")
    private String payWay;

    /** 收付款账户 */
    @Excel(name = "收付款账户")
    private String payAccount;

    /** 收付款单位 */
    @Excel(name = "收付款单位")
    private String payCompany;

    /** 上月结转 */
    @Excel(name = "上月结转")
    private Double preMonthMoney;

    /** 收款 */
    @Excel(name = "收款")
    private Double collectionMoney;

    /** 收款类别 */
    @Excel(name = "收款类别")
    private String collectionType;

    /** 付款 */
    @Excel(name = "付款")
    private Double payment;

    /** 付款类别 */
    @Excel(name = "付款类别")
    private String paymentType;

    /** 余额 */
    @Excel(name = "余额")
    private Double balance;

    /** 类型 1.收款；2.付款 */
    @Excel(name = "类型 1.收款；2.付款")
    private String type;

    /** 部门ID */
    @Excel(name = "部门ID")
    private String deptId;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getBillId() {
        return billId;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSerialNo() {
        return serialNo;
    }
    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Date getOperateDate() {
        return operateDate;
    }
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayWay() {
        return payWay;
    }
    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayAccount() {
        return payAccount;
    }
    public void setPayCompany(String payCompany) {
        this.payCompany = payCompany;
    }

    public String getPayCompany() {
        return payCompany;
    }
    public void setPreMonthMoney(Double preMonthMoney) {
        this.preMonthMoney = preMonthMoney;
    }

    public Double getPreMonthMoney() {
        return preMonthMoney;
    }
    public void setCollectionMoney(Double collectionMoney) {
        this.collectionMoney = collectionMoney;
    }

    public Double getCollectionMoney() {
        return collectionMoney;
    }
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getCollectionType() {
        return collectionType;
    }
    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getPayment() {
        return payment;
    }
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptId() {
        return deptId;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("billId", getBillId())
                .append("serialNo", getSerialNo())
                .append("operateDate", getOperateDate())
                .append("certificateNumber", getCertificateNumber())
                .append("payWay", getPayWay())
                .append("payAccount", getPayAccount())
                .append("payCompany", getPayCompany())
                .append("preMonthMoney", getPreMonthMoney())
                .append("collectionMoney", getCollectionMoney())
                .append("collectionType", getCollectionType())
                .append("payment", getPayment())
                .append("paymentType", getPaymentType())
                .append("balance", getBalance())
                .append("type", getType())
                .append("deptId", getDeptId())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }

}
