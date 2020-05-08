package com.ruoyi.fmis.customer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 客户对象 biz_customer
 *
 * @author frank
 * @date 2020-03-02
 */
@Data
public class BizCustomer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 客户ID */
    private Long customerId;

    @Setter
    @Getter
    private String allocationDatestartTime;
    @Setter
    @Getter
    private String allocationDateendTime;

    /** 备案日期 */
    @Excel(name = "备案日期")
    private String recordDate;

    /** 商务公司代码 */
    @Excel(name = "商务公司代码")
    private String companyCode;

    /** 客户所属区域 */
    @Excel(name = "客户所属区域")
    private String area;

    /** 项目备案号 */
    @Excel(name = "项目备案号")
    private String recordCode;

    /** 业务负责人 */
    @Excel(name = "业务负责人")
    private String ownerUserId;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String name;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectAme;

    /** 联系人姓名 */
    @Excel(name = "联系人姓名")
    private String contactName;

    /** 联系人职务 */
    @Excel(name = "联系人职务")
    private String contactPosition;

    /** 联系人电话 */
    @Excel(name = "联系人电话")
    private String contactPhone;

    /** 联系人邮箱 */
    @Excel(name = "联系人邮箱")
    private String contactEmail;

    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;

    /** 客户/信息 */
    @Excel(name = "客户/信息")
    private String info;

    /** 涉及产品 */
    @Excel(name = "涉及产品")
    private String productInfo;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    @Getter
    @Setter
    private String areaCode;

    @Getter
    @Setter
    private String source;

    @Getter
    @Setter
    private String recordType;

    @Getter
    @Setter
    private String recordNum;

    @Getter
    @Setter
    private String telephone;

    @Getter
    @Setter
    private String fax;

    @Getter
    @Setter
    private String companyAddress;

    @Getter
    @Setter
    private Long deptId;

    @Getter
    @Setter
    private String deptName;

    @Getter
    @Setter
    private String customerLevel;

    @Getter
    @Setter
    private String codeName;

    @Getter
    @Setter
    private String industryDivision;

    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date allocationDate;

    @Getter
    @Setter
    private String assignNumber;

    @Getter
    @Setter
    private String fileNumber;

    @Getter
    @Setter
    private String filePaths;

    @Getter
    @Setter
    private String newStatus;



    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }
    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }
    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getRecordCode() {
        return recordCode;
    }
    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setProjectAme(String projectAme) {
        this.projectAme = projectAme;
    }

    public String getProjectAme() {
        return projectAme;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }
    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    public String getContactPosition() {
        return contactPosition;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getProductInfo() {
        return productInfo;
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
            .append("customerId", getCustomerId())
            .append("recordDate", getRecordDate())
            .append("companyCode", getCompanyCode())
            .append("area", getArea())
            .append("recordCode", getRecordCode())
            .append("ownerUserId", getOwnerUserId())
            .append("name", getName())
            .append("projectAme", getProjectAme())
            .append("contactName", getContactName())
            .append("contactPosition", getContactPosition())
            .append("contactPhone", getContactPhone())
            .append("contactEmail", getContactEmail())
            .append("brand", getBrand())
            .append("info", getInfo())
            .append("productInfo", getProductInfo())
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
