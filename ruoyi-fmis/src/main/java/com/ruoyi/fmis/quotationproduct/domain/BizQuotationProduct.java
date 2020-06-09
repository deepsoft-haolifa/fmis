package com.ruoyi.fmis.quotationproduct.domain;

import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报价单产品对象 biz_quotation_product
 *
 * @author Xianlu Tech
 * @date 2020-03-21
 */
public class BizQuotationProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String string6;
    @Getter
    @Setter
    private String string7;
    @Getter
    @Setter
    private String string8;
    @Getter
    @Setter
    private String string9;
    @Getter
    @Setter
    private String string10;
    @Getter
    @Setter
    private String string11;
    @Getter
    @Setter
    private String string12;
    @Getter
    @Setter
    private String string13;
    @Getter
    @Setter
    private String string14;
    @Getter
    @Setter
    private String string15;

    @Getter
    @Setter
    private Long pattachmentId;
    @Getter
    @Setter
    private Long pattachment1Id;
    @Getter
    @Setter
    private Long pattachment2Id;
    @Getter
    @Setter
    private Long pattachment3Id;
    @Getter
    @Setter
    private Long pattachment4Id;

    @Getter
    @Setter
    private Double pattachmentCount;
    @Getter
    @Setter
    private Double pattachment1Count;
    @Getter
    @Setter
    private Double pattachment2Count;
    @Getter
    @Setter
    private Double pattachment3Count;
    @Getter
    @Setter
    private Double pattachment4Count;

    @Getter
    @Setter
    private Double pattachmentPrice;
    @Getter
    @Setter
    private Double pattachment1Price;
    @Getter
    @Setter
    private Double pattachment2Price;
    @Getter
    @Setter
    private Double pattachment3Price;
    @Getter
    @Setter
    private Double pattachment4Price;

    @Getter
    @Setter
    private Double pattachmentCoefficient;
    @Getter
    @Setter
    private Double pattachment1Coefficient;
    @Getter
    @Setter
    private Double pattachment2Coefficient;
    @Getter
    @Setter
    private Double pattachment3Coefficient;
    @Getter
    @Setter
    private Double pattachment4Coefficient;

    /** ID */
    private Long qpId;

    /** 报价表ID */
    @Excel(name = "报价表ID")
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

    /** 备用1 产品数量*/
    @Excel(name = "备用1")
    private String string1;

    /** 备用2 产品系数*/
    @Excel(name = "备用2")
    private String string2;

    /** 备用3 系数*/
    @Excel(name = "备用3")
    private String string3;

    /** 备用4 产品备注*/
    @Excel(name = "备用4")
    private String string4;

    /** 备用5 */
    @Excel(name = "备用5")
    private String string5;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private BizProduct bizProduct;
    public void setBizProduct(BizProduct bizProduct) {
        this.bizProduct = bizProduct;
    }
    public BizProduct getBizProduct() {
        return bizProduct;
    }

    private BizProductRef bizProductRef1;
    public void setBizProductRef1(BizProductRef bizProductRef1) {
        this.bizProductRef1 = bizProductRef1;
    }
    public BizProductRef getBizProductRef1() {
        return bizProductRef1;
    }

    private BizProductRef bizProductRef2;
    public void setBizProductRef2(BizProductRef bizProductRef2) {
        this.bizProductRef2 = bizProductRef2;
    }
    public BizProductRef getBizProductRef2() {
        return bizProductRef2;
    }

    private BizActuator bizActuator;
    public void setBizActuator(BizActuator bizActuator) {
        this.bizActuator = bizActuator;
    }
    public BizActuator getBizActuator() {
        return bizActuator;
    }


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



    private String productNum;
    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }
    public String getProductNum() {
        return productNum;
    }

    private String productCoefficient;
    public void setProductCoefficient(String productCoefficient) {
        this.productCoefficient = productCoefficient;
    }
    public String getProductCoefficient() {
        return productCoefficient;
    }


    private String productRef1Coefficient;
    public void setProductRef1Coefficient(String productRef1Coefficient) {
        this.productRef1Coefficient = productRef1Coefficient;
    }
    public String getProductRef1Coefficient() {
        return productRef1Coefficient;
    }

    private String productRef2Coefficient;
    public void setProductRef2Coefficient(String productRef2Coefficient) {
        this.productRef2Coefficient = productRef2Coefficient;
    }
    public String getProductRef2Coefficient() {
        return productRef2Coefficient;
    }

    private String actuatorNum;
    public void setActuatorNum(String actuatorNum) {
        this.actuatorNum = actuatorNum;
    }
    public String getActuatorNum() {
        return actuatorNum;
    }

    private String actuatorCoefficient;
    public void setActuatorCoefficient(String actuatorCoefficient) {
        this.actuatorCoefficient = actuatorCoefficient;
    }
    public String getActuatorCoefficient() {
        return actuatorCoefficient;
    }



    public void setQpId(Long qpId) {
        this.qpId = qpId;
    }

    public Long getQpId() {
        return qpId;
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
            .append("qpId", getQpId())
            .append("quotationId", getQuotationId())
            .append("productId", getProductId())
            .append("productRef1Id", getProductRef1Id())
            .append("productRef1Num", getProductRef1Num())
            .append("productRef2Id", getProductRef2Id())
            .append("productRef2Num", getProductRef2Num())
            .append("actuatorId", getActuatorId())
            .append("remark", getRemark())
            .append("string1", getString1())
            .append("string2", getString2())
            .append("string3", getString3())
            .append("string4", getString4())
            .append("string5", getString5())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
