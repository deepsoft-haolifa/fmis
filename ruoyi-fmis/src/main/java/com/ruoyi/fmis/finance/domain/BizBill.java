package com.ruoyi.fmis.finance.domain;

import com.ruoyi.system.domain.SysDept;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 现金日记账对象 biz_bill
 *
 * @author murphy.he
 * @date 2020-11-02
 */
public class BizBill extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String createByName;

    @Getter
    @Setter
    private Double billAmount;

    @Getter
    @Setter
    private String deptName;

    @Getter
    @Setter
    private SysDept dept;

    /** ID */
    private Long billId;

    /** 序号 */
    @Excel(name = "序号")
    private String xh;

    /** 日期 */
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date d;

    /** 凭证号 */
    @Excel(name = "凭证号")
    private String certificateNumber;

    /** 结算类别 */
    @Excel(name = "结算类别")
    private String settlementType;

    /** 结算票号 */
    @Excel(name = "结算票号")
    private String clearingBanks;

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

    /** 类型 1=现金日记账 2=银行日记账 */
    @Excel(name = "类型 1=现金日记账 2=银行日记账")
    private String type;

    /** 部门ID */
    @Excel(name = "部门ID")
    private String deptId;

    /** 备用1 */
    @Excel(name = "备用1")
    private String string1;

    /** 备用2 */
    @Excel(name = "备用2")
    private String string2;

    /** 备用3 */
    @Excel(name = "备用3")
    private String string3;

    /** 备用4 */
    @Excel(name = "备用4")
    private String string4;

    /** 备用5 */
    @Excel(name = "备用5")
    private String string5;

    /** 备用6 */
    @Excel(name = "备用6")
    private String string6;

    /** 备用7 */
    @Excel(name = "备用7")
    private String string7;

    /** 备用8 */
    @Excel(name = "备用8")
    private String string8;

    /** 备用9 */
    @Excel(name = "备用9")
    private String string9;

    /** 备用10 */
    @Excel(name = "备用10")
    private String string10;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getBillId() {
        return billId;
    }
    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXh() {
        return xh;
    }
    public void setD(Date d) {
        this.d = d;
    }

    public Date getD() {
        return d;
    }
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }
    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    public String getSettlementType() {
        return settlementType;
    }
    public void setClearingBanks(String clearingBanks) {
        this.clearingBanks = clearingBanks;
    }

    public String getClearingBanks() {
        return clearingBanks;
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
    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString1() {
        return string1;
    }
    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString2() {
        return string2;
    }
    public void setString3(String string3) {
        this.string3 = string3;
    }

    public String getString3() {
        return string3;
    }
    public void setString4(String string4) {
        this.string4 = string4;
    }

    public String getString4() {
        return string4;
    }
    public void setString5(String string5) {
        this.string5 = string5;
    }

    public String getString5() {
        return string5;
    }
    public void setString6(String string6) {
        this.string6 = string6;
    }

    public String getString6() {
        return string6;
    }
    public void setString7(String string7) {
        this.string7 = string7;
    }

    public String getString7() {
        return string7;
    }
    public void setString8(String string8) {
        this.string8 = string8;
    }

    public String getString8() {
        return string8;
    }
    public void setString9(String string9) {
        this.string9 = string9;
    }

    public String getString9() {
        return string9;
    }
    public void setString10(String string10) {
        this.string10 = string10;
    }

    public String getString10() {
        return string10;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("billId", getBillId())
            .append("xh", getXh())
            .append("d", getD())
            .append("certificateNumber", getCertificateNumber())
            .append("settlementType", getSettlementType())
            .append("clearingBanks", getClearingBanks())
            .append("preMonthMoney", getPreMonthMoney())
            .append("collectionMoney", getCollectionMoney())
            .append("collectionType", getCollectionType())
            .append("payment", getPayment())
            .append("paymentType", getPaymentType())
            .append("balance", getBalance())
            .append("type", getType())
            .append("deptId", getDeptId())
            .append("string1", getString1())
            .append("string2", getString2())
            .append("string3", getString3())
            .append("string4", getString4())
            .append("string5", getString5())
            .append("string6", getString6())
            .append("string7", getString7())
            .append("string8", getString8())
            .append("string9", getString9())
            .append("string10", getString10())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
