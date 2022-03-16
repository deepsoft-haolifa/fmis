package com.ruoyi.fmis.invoice.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 发票信息（进项发票）对象 biz_invoice
 *
 * @author hedong
 * @date 2021-05-22
 */
public class BizInvoice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private String serialNo;

    /** 发票代码 */
    @Excel(name = "发票代码")
    private String invoiceCode;

    /** 发票号码 */
    @Excel(name = "发票号码")
    private String invoiceNo;

    /** 开票日期 */
    @Excel(name = "开票日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date billDate;

    /** 纳税人识别号 */
    @Excel(name = "纳税人识别号")
    private String taxpayerIdNo;

    /** 供应商Id */
    @Excel(name = "销方名称")
    private String supplierName;
    /** 供应商ID */
    @Excel(name = "销方名称")
    private String supplierId;

    /** 发票金额 */
    @Excel(name = "发票金额")
    private Double amount;



    /** 税率 */
    @Excel(name = "税率")
    private String taxRate;

    @Excel(name = "有效税额")
    private Double taxAmount;
    /** 认证日期 */
    @Excel(name = "确认/认证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date authDate;
    /** 采购合同号（多个逗号分割） */
    private String contractNo;

    /** 发票类型（1.增值税普通发票; 2.增值税专用发票;） */
    @Excel(name = "发票类型（1.普通;2.专用）请输入数字", readConverterExp = "1=.增值税普通发票;,2=.增值税专用发票;")
    private String invoiceType;

    /** 发票状态 0非正常;1正常 */
    @Excel(name = "发票状态(非正常/正常)，请输入数字", readConverterExp = "0=.非正常;,1=.正常;")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSerialNo() {
        return serialNo;
    }
    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Date getBillDate() {
        return billDate;
    }
    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public Date getAuthDate() {
        return authDate;
    }
    public void setTaxpayerIdNo(String taxpayerIdNo) {
        this.taxpayerIdNo = taxpayerIdNo;
    }

    public String getTaxpayerIdNo() {
        return taxpayerIdNo;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierId() {
        return supplierId;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }
    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxRate() {
        return taxRate;
    }
    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractNo() {
        return contractNo;
    }
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() {
        return invoiceType;
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
                .append("id", getId())
                .append("serialNo", getSerialNo())
                .append("invoiceCode", getInvoiceCode())
                .append("invoiceNo", getInvoiceNo())
                .append("billDate", getBillDate())
                .append("authDate", getAuthDate())
                .append("taxpayerIdNo", getTaxpayerIdNo())
                .append("supplierName", getSupplierName())
                .append("amount", getAmount())
                .append("taxRate", getTaxRate())
                .append("rateAmount", getTaxAmount())
                .append("contractNo", getContractNo())
                .append("invoiceType", getInvoiceType())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}