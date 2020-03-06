package com.ruoyi.fmis.suppliers.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 供应商对象 biz_suppliers
 *
 * @author Xianlu Tech
 * @date 2020-03-06
 */
public class BizSuppliers extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long suppliersId;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String name;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 电话 */
    @Excel(name = "电话")
    private String telphone;

    /** 传真 */
    @Excel(name = "传真")
    private String fax;

    /** Email */
    @Excel(name = "Email")
    private String email;

    /** 网址 */
    @Excel(name = "网址")
    private String siteUrl;

    /** 负责人 */
    @Excel(name = "负责人")
    private String ownerId;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contact;

    /** 经营形态 */
    @Excel(name = "经营形态")
    private String manageState;

    /** 市场分布 */
    @Excel(name = "市场分布")
    private String marketDistribution;

    /** 目标客户 */
    @Excel(name = "目标客户")
    private String targetCustomer;

    /** 人力资源状况 */
    @Excel(name = "人力资源状况")
    private String humanCapitalMeasure;

    /** 一般纳税人 */
    @Excel(name = "一般纳税人")
    private String taxpayer;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setSuppliersId(Long suppliersId) {
        this.suppliersId = suppliersId;
    }

    public Long getSuppliersId() {
        return suppliersId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getTelphone() {
        return telphone;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getSiteUrl() {
        return siteUrl;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }
    public void setManageState(String manageState) {
        this.manageState = manageState;
    }

    public String getManageState() {
        return manageState;
    }
    public void setMarketDistribution(String marketDistribution) {
        this.marketDistribution = marketDistribution;
    }

    public String getMarketDistribution() {
        return marketDistribution;
    }
    public void setTargetCustomer(String targetCustomer) {
        this.targetCustomer = targetCustomer;
    }

    public String getTargetCustomer() {
        return targetCustomer;
    }
    public void setHumanCapitalMeasure(String humanCapitalMeasure) {
        this.humanCapitalMeasure = humanCapitalMeasure;
    }

    public String getHumanCapitalMeasure() {
        return humanCapitalMeasure;
    }
    public void setTaxpayer(String taxpayer) {
        this.taxpayer = taxpayer;
    }

    public String getTaxpayer() {
        return taxpayer;
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
            .append("suppliersId", getSuppliersId())
            .append("name", getName())
            .append("address", getAddress())
            .append("telphone", getTelphone())
            .append("fax", getFax())
            .append("email", getEmail())
            .append("siteUrl", getSiteUrl())
            .append("ownerId", getOwnerId())
            .append("contact", getContact())
            .append("manageState", getManageState())
            .append("marketDistribution", getMarketDistribution())
            .append("targetCustomer", getTargetCustomer())
            .append("humanCapitalMeasure", getHumanCapitalMeasure())
            .append("taxpayer", getTaxpayer())
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
