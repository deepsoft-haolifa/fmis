package com.ruoyi.fmis.quotation.domain;

import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 报价单对象 biz_quotation
 *
 * @author frank
 * @date 2020-03-18
 */
public class BizQuotation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long quotationId;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品配件法兰id */
    @Excel(name = "产品配件法兰id")
    private Long productRef1Id;

    /** 产品配件法兰数量 */
    @Excel(name = "产品配件法兰数量")
    private String productRef1Num;

    /** 产品配件螺栓id */
    @Excel(name = "产品配件螺栓id")
    private Long productRef2Id;

    /** 产品配件螺栓数量 */
    @Excel(name = "产品配件螺栓数量")
    private String productRef2Num;

    /** 执行器id */
    @Excel(name = "执行器id")
    private Long actuatorId;

    /** 特殊费用 */
    @Excel(name = "特殊费用")
    private String specialExpenses;

    /** 付款方式 */
    @Excel(name = "付款方式")
    private String paymentMethod;

    /** 运费价格 */
    @Excel(name = "运费价格")
    private Double freight;

    /** 供货周期 */
    @Excel(name = "供货周期")
    private String leadTime;

    /** 报备项目 默认非项目 */
    @Excel(name = "报备项目 默认非项目")
    private String reportProject;

    /** 2销售经理审批结束 3 区域经理审批结束 4副总审批结束 5 老总审批结束 */
    private String normalFlag;

    /** 申请人 */
    @Excel(name = "流程状态")
    private String flowStatus;

    /** 备用1 报价单号 */
    private String string1;

    /** 备用2 客户ID*/
    private String string2;

    /** 备用3 */
    private String string3;

    /** 备用4 特殊费用说明*/
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


    private List<BizQuotationProduct> quotationProductList;
    public void setQuotationProductList(List<BizQuotationProduct> quotationProductList) {
        this.quotationProductList = quotationProductList;
    }
    public List<BizQuotationProduct> getQuotationProductList() {
        return quotationProductList;
    }

    //pr1.name as productRef1Name,pr2.name as productRef2Name,ac.name as actuatorName
    private String productName;
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductName() {
        return productName;
    }

    private String productRef1Name;
    public void setProductRef1Name(String productRef1Name) {
        this.productRef1Name = productRef1Name;
    }
    public String getProductRef1Name() {
        return productRef1Name;
    }

    private String productRef2Name;
    public void setProductRef2Name(String productRef2Name) {
        this.productRef2Name = productRef2Name;
    }
    public String getProductRef2Name() {
        return productRef2Name;
    }

    private String actuatorName;
    public void setActuatorName(String actuatorName) {
        this.actuatorName = actuatorName;
    }
    public String getActuatorName() {
        return actuatorName;
    }

    private String customerName;
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public Long getQuotationId() {
        return quotationId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
    public void setProductRef1Id(Long productRef1Id) {
        this.productRef1Id = productRef1Id;
    }

    public Long getProductRef1Id() {
        return productRef1Id;
    }
    public void setProductRef1Num(String productRef1Num) {
        this.productRef1Num = productRef1Num;
    }

    public String getProductRef1Num() {
        return productRef1Num;
    }
    public void setProductRef2Id(Long productRef2Id) {
        this.productRef2Id = productRef2Id;
    }

    public Long getProductRef2Id() {
        return productRef2Id;
    }
    public void setProductRef2Num(String productRef2Num) {
        this.productRef2Num = productRef2Num;
    }

    public String getProductRef2Num() {
        return productRef2Num;
    }
    public void setActuatorId(Long actuatorId) {
        this.actuatorId = actuatorId;
    }

    public Long getActuatorId() {
        return actuatorId;
    }
    public void setSpecialExpenses(String specialExpenses) {
        this.specialExpenses = specialExpenses;
    }

    public String getSpecialExpenses() {
        return specialExpenses;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public Double getFreight() {
        return freight;
    }
    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public String getLeadTime() {
        return leadTime;
    }
    public void setReportProject(String reportProject) {
        this.reportProject = reportProject;
    }

    public String getReportProject() {
        return reportProject;
    }
    public void setNormalFlag(String normalFlag) {
        this.normalFlag = normalFlag;
    }

    public String getNormalFlag() {
        return normalFlag;
    }
    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getFlowStatus() {
        return flowStatus;
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
            .append("quotationId", getQuotationId())
            .append("productId", getProductId())
            .append("productRef1Id", getProductRef1Id())
            .append("productRef1Num", getProductRef1Num())
            .append("productRef2Id", getProductRef2Id())
            .append("productRef2Num", getProductRef2Num())
            .append("actuatorId", getActuatorId())
            .append("specialExpenses", getSpecialExpenses())
            .append("paymentMethod", getPaymentMethod())
            .append("freight", getFreight())
            .append("leadTime", getLeadTime())
            .append("reportProject", getReportProject())
            .append("normalFlag", getNormalFlag())
            .append("flowStatus", getFlowStatus())
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
