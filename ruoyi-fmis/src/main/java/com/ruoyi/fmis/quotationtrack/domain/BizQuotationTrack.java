package com.ruoyi.fmis.quotationtrack.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * 报价追踪对象 biz_quotation_track
 *
 * @author Xianlu Tech
 * @date 2020-04-30
 */
public class BizQuotationTrack extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long trackId;

    /** 报价单ID */
    @Excel(name = "报价单ID")
    private Long quotationId;

    /** 报价单状态 quotation_state 已成交/未成交/未反馈/待跟进 */
    @Excel(name = "报价单状态 quotation_state 已成交/未成交/未反馈/待跟进")
    private String quotationState;

    /** 报价顺序号 */
    @Excel(name = "报价顺序号")
    private String serialNumber;

    /** 溢价比例 */
    @Excel(name = "溢价比例")
    private String premiumRate;

    /** 追踪反馈 */
    @Excel(name = "追踪反馈")
    private String feedback;

    /** 闭环时间 */
    @Excel(name = "闭环时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date closedloopTime;

    /** 报价备案时间 */
    @Excel(name = "报价备案时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recordTime;

    /** 报价时效 */
    @Excel(name = "报价时效")
    private String limitation;

    /** email */
    @Excel(name = "email")
    private String email;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contacts;

    /** 电话 */
    @Excel(name = "电话")
    private String contactPhone;

    /** 传真 */
    @Excel(name = "传真")
    private String fax;

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


    //q.create_time as ,c.area_code as ,c.new_status as ,q.string1 as ,c.code_name as ,c.,c.name as ,c.project_ame as ,
    //        q.string9 as ,(select count(1) from biz_quotation_product p where p.quotation_id=q.quotation_id) as ,u.user_name as

    @Getter
    @Setter
    private Date qcreateTime;

    @Getter
    @Setter
    private String areaCode;


    @Getter
    @Setter
    private String newStatus;

    @Getter
    @Setter
    private String qOrderno;

    @Getter
    @Setter
    private String codeName;

    @Getter
    @Setter
    private String area;
    @Getter
    @Setter
    private String customerName;
    @Getter
    @Setter
    private String projectAme;
    @Getter
    @Setter
    private String totalPrice;
    @Getter
    @Setter
    private String totalNum;
    @Getter
    @Setter
    private String qcreateName;
    @Getter
    @Setter
    private String companyCode;
    @Getter
    @Setter
    private List<Long> deptIdList;
    @Getter
    @Setter
    private Long userId;




    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Long getTrackId() {
        return trackId;
    }
    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public Long getQuotationId() {
        return quotationId;
    }
    public void setQuotationState(String quotationState) {
        this.quotationState = quotationState;
    }

    public String getQuotationState() {
        return quotationState;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setPremiumRate(String premiumRate) {
        this.premiumRate = premiumRate;
    }

    public String getPremiumRate() {
        return premiumRate;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }
    public void setClosedloopTime(Date closedloopTime) {
        this.closedloopTime = closedloopTime;
    }

    public Date getClosedloopTime() {
        return closedloopTime;
    }
    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Date getRecordTime() {
        return recordTime;
    }
    public void setLimitation(String limitation) {
        this.limitation = limitation;
    }

    public String getLimitation() {
        return limitation;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
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
            .append("quotationId", getQuotationId())
            .append("quotationState", getQuotationState())
            .append("serialNumber", getSerialNumber())
            .append("premiumRate", getPremiumRate())
            .append("feedback", getFeedback())
            .append("closedloopTime", getClosedloopTime())
            .append("recordTime", getRecordTime())
            .append("limitation", getLimitation())
            .append("email", getEmail())
            .append("contacts", getContacts())
            .append("contactPhone", getContactPhone())
            .append("fax", getFax())
            .append("remark", getRemark())
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
