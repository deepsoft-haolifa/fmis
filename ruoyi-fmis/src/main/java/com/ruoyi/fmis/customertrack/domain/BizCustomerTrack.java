package com.ruoyi.fmis.customertrack.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 客户追踪对象 biz_customer_track
 *
 * @author frank
 * @date 2020-04-29
 */
public class BizCustomerTrack extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long trackId;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long customerId;

    /** 成单状态 */
    @Excel(name = "成单状态")
    private String singleState;

    /** 报价状态 */
    @Excel(name = "报价状态")
    private String reportpriceState;

    /** 合同待执行 */
    @Excel(name = "合同待执行")
    private String contractExecuted;

    /** 报价待追踪 */
    @Excel(name = "报价待追踪")
    private String offerTracked;

    /** 订货次数 */
    @Excel(name = "订货次数")
    private Long orderNumber;

    /** 追踪反馈 */
    @Excel(name = "追踪反馈")
    private String feedback;

    /** email */
    @Excel(name = "email")
    private String email;

    /** 闭环时间 */
    @Excel(name = "闭环时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date closedloopTime;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contacts;

    /** 电话 */
    @Excel(name = "电话")
    private String contactPhone;

    /** 传真 */
    @Excel(name = "传真")
    private String fax;

    @Excel(name = "客户名称")
    @Getter
    @Setter
    private String customerName;

    @Excel(name = "客户代号")
    @Getter
    @Setter
    private String codeName;

    /** 备用1 */
    private String string1;

    /** 备用2 */
    private String string2;

    /** 备用3 */
    private String string3;

    /** 备用4 */
    private String string4;

    /** 备用5 */
    private String string5;

    /** 备用6 */
    private String string6;

    /** 备用7 */
    private String string7;

    /** 备用8 */
    private String string8;

    /** 备用9 */
    private String string9;

    /** 备用10 */
    private String string10;

    /** 状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Long getTrackId() {
        return trackId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }
    public void setSingleState(String singleState) {
        this.singleState = singleState;
    }

    public String getSingleState() {
        return singleState;
    }
    public void setReportpriceState(String reportpriceState) {
        this.reportpriceState = reportpriceState;
    }

    public String getReportpriceState() {
        return reportpriceState;
    }
    public void setContractExecuted(String contractExecuted) {
        this.contractExecuted = contractExecuted;
    }

    public String getContractExecuted() {
        return contractExecuted;
    }
    public void setOfferTracked(String offerTracked) {
        this.offerTracked = offerTracked;
    }

    public String getOfferTracked() {
        return offerTracked;
    }
    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    public void setClosedloopTime(Date closedloopTime) {
        this.closedloopTime = closedloopTime;
    }

    public Date getClosedloopTime() {
        return closedloopTime;
    }
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContacts() {
        return contacts;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
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
            .append("trackId", getTrackId())
            .append("customerId", getCustomerId())
            .append("singleState", getSingleState())
            .append("reportpriceState", getReportpriceState())
            .append("contractExecuted", getContractExecuted())
            .append("offerTracked", getOfferTracked())
            .append("orderNumber", getOrderNumber())
            .append("feedback", getFeedback())
            .append("remark", getRemark())
            .append("email", getEmail())
            .append("closedloopTime", getClosedloopTime())
            .append("contacts", getContacts())
            .append("contactPhone", getContactPhone())
            .append("fax", getFax())
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
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
