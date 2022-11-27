package com.ruoyi.fmis.child.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 流程数据字对象 biz_process_child
 *
 * @author Xianlu Tech
 * @date 2020-05-05
 */
public class BizProcessChild extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private String procurementId;

    @Getter
    @Setter
    private String contractSpecial;

    @Getter
    @Setter
    private String costPrice;

    @Getter
    @Setter
    private String productCostPrice;
    @Getter
    @Setter
    private String ref1CostPrice;
    @Getter
    @Setter
    private String ref2CostPrice;
    @Getter
    @Setter
    private String pattachmentCostPrice;
    @Getter
    @Setter
    private String pattachment1CostPrice;
    @Getter
    @Setter
    private String pattachment2CostPrice;
    @Getter
    @Setter
    private String pattachment3CostPrice;
    @Getter
    @Setter
    private String pattachment4CostPrice;
    @Getter
    @Setter
    private String actuatorCostPrice;

    @Getter
    @Setter
    private String bjNo;

    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stayCreateTime;

    @Getter
    @Setter
    private String contractNo;

    @Getter
    @Setter
    private String procurementNo;

    @Getter
    @Setter
    private String paType;
    @Getter
    @Setter
    private String statusId;
    @Getter
    @Setter
    private String createByName;

    @Setter
    @Getter
    private String orderNo;
    @Getter
    @Setter
    private String stayId;

    @Getter
    @Setter
    private Double stayNum;

    @Getter
    @Setter
    private String testId;

    @Getter
    @Setter
    private String yesNum;
    @Getter
    @Setter
    private String noNum;

    @Getter
    @Setter
    private String bizEditFlag;
    @Getter
    @Setter
    private String levelValue;



    @Getter
    @Setter
    private String actuatorId;

    @Getter
    @Setter
    private String productRef1Id;
    @Getter
    @Setter
    private String productRef2Id;

    @Getter
    @Setter
    private String productStatus;

    @Getter
    @Setter
    private String queryStatus;

    @Getter
    @Setter
    private String dataStatus;
    @Getter
    @Setter
    private String actuatorStatus;
    @Getter
    @Setter
    private String ref1Status;
    @Getter
    @Setter
    private String ref2Status;
    @Getter
    @Setter
    private String pStatus;
    @Getter
    @Setter
    private String p1Status;
    @Getter
    @Setter
    private String p2Status;
    @Getter
    @Setter
    private String p3Status;
    @Getter
    @Setter
    private String p4Status;

    //回货时间
    @Getter
    @Setter
    private String goodsTime;

    @Getter
    @Setter
    private String bh;
    @Getter
    @Setter
    private String chineseName;
    @Getter
    @Setter
    private String chineseSpecifications;
    @Getter
    @Setter
    private String englishName;
    @Getter
    @Setter
    private String englishSpecifications;
    @Getter
    @Setter
    private String chinesePackaging;
    @Getter
    @Setter
    private String englishPackaging;
    @Getter
    @Setter
    private String chineseUnit;
    @Getter
    @Setter
    private String englishUnit;
    @Getter
    @Setter
    private String pressure;
    @Getter
    @Setter
    private String material;
    @Getter
    @Setter
    private String barCode;
    @Getter
    @Setter
    private String customsBh;
    @Getter
    @Setter
    private String handlingFee;
    @Getter
    @Setter
    private String color;
    @Getter
    @Setter
    private String guishudanwei;
    @Getter
    @Setter
    private String developer;
    @Getter
    @Setter
    private String goodsCategory;
    @Getter
    @Setter
    private String supplierName;
    @Getter
    @Setter
    private Double procurementPrice;



    @Getter
    @Setter
    private String ref1String2;
    @Getter
    @Setter
    private String ref1Specifications;
    @Getter
    @Setter
    private String ref1ValvebodyMaterial;
    @Getter
    @Setter
    private String ref1MaterialRequire;
    @Getter
    @Setter
    private String suppliersName;


    @Getter
    @Setter
    private String actuatorBrand;
    @Getter
    @Setter
    private String actuatorManufacturer;
    @Getter
    @Setter
    private String actuatorString4;
    @Getter
    @Setter
    private String actuatorString1;
    @Getter
    @Setter
    private String actuatorSetupType;
    @Getter
    @Setter
    private String actuatorOutputTorque;
    @Getter
    @Setter
    private String actuatorString3;
    @Getter
    @Setter
    private String actuatorActionType;
    @Getter
    @Setter
    private String actuatorControlCircuit;
    @Getter
    @Setter
    private String actuatorAdaptableVoltage;
    @Getter
    @Setter
    private String actuatorProtectionLevel;
    @Getter
    @Setter
    private String actuatorQualityLevel;
    @Getter
    @Setter
    private String actuatorExplosionLevel;
    @Getter
    @Setter
    private String actuatorString6;



    @Getter
    @Setter
    private Double productProcurementPrice;

    @Getter
    @Setter
    private String levelAB;

    @Getter
    @Setter
    private String levelLabel;

    @Getter
    @Setter
    private String pattachmentName;
    @Getter
    @Setter
    private String pattachment1Name;
    @Getter
    @Setter
    private String pattachment2Name;
    @Getter
    @Setter
    private String pattachment3Name;
    @Getter
    @Setter
    private String pattachment4Name;

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
    private BizActuator bizActuator;

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

    @Getter
    @Setter
    private String quotationId;

    @Getter
    @Setter
    private String productId;

    @Getter
    @Setter
    private String newProductId;

    @Getter
    @Setter
    private String pSessionId;



    @Getter
    @Setter
    private Double productPrice;
    @Getter
    @Setter
    private Double ref1Price;


    @Getter
    @Setter
    private Double productRef1Num;

    @Getter
    @Setter
    private Double productRef2Num;

    @Getter
    @Setter
    private Double ref2Price;
    @Getter
    @Setter
    private Double actuatorPrice;

    @Getter
    @Setter
    private String quotationName;

    @Getter
    @Setter
    private String productNum;

    @Getter
    @Setter
    private String productCoefficient;

    @Getter
    @Setter
    private String productRef1Coefficient;

    @Getter
    @Setter
    private String productRef2Coefficient;
    @Getter
    @Setter
    private String actuatorName;
    @Getter
    @Setter
    private String actuatorNum;

    @Getter
    @Setter
    private String actuatorCoefficient;

    @Getter
    @Setter
    private String ref1Name;

    @Getter
    @Setter
    private String ref2Name;

    @Getter
    @Setter
    private String productName;


    //产品 start
    @Getter
    @Setter
    private String series;
    @Getter
    @Setter
    private String model;
    @Getter
    @Setter
    private String specifications;
    @Getter
    @Setter
    private String nominalPressure;
    @Getter
    @Setter
    private String connectionType;
    @Getter
    @Setter
    private String valvebodyMaterial;
    @Getter
    @Setter
    private String valveMaterial;
    @Getter
    @Setter
    private String sealingMaterial;
    @Getter
    @Setter
    private String valveElement;
    @Getter
    @Setter
    private String driveForm;
    @Getter
    @Setter
    private String structuralStyle;
    @Getter
    @Setter
    private String supplier;
    @Getter
    @Setter
    private String supplierId;
    @Getter
    @Setter
    private String newSupplier;
    @Getter
    @Setter
    private String medium;
    @Getter
    @Setter
    private String temperature;
    @Getter
    @Setter
    private String other;
    @Getter
    @Setter
    private String productString1;
    @Getter
    @Setter
    private String productString2;
    @Getter
    @Setter
    private double contractPrice;

    //产品 end


    /** ID */
    private Long childId;

    @Setter
    @Getter
    private String paramterId;

    @Setter
    @Getter
    private BizProcessData bizProcessData;


    @Setter
    @Getter
    private BizProductRef ref1;

    @Setter
    @Getter
    private BizProductRef ref2;

    @Setter
    @Getter
    private BizQuotation bizQuotation;

    @Setter
    @Getter
    private BizProduct bizProduct;


    /** 数据ID */
    @Excel(name = "数据ID")
    private Long dataId;

    /** 价格1 */
    @Excel(name = "价格1")
    private Double price1;

    /** 价格2 */
    @Excel(name = "价格2")
    private Double price2;

    /** 价格3 */
    @Excel(name = "价格3")
    private Double price3;

    /** 价格4 */
    @Excel(name = "价格4")
    private Double price4;

    /** 价格5 */
    @Excel(name = "价格5")
    private Double price5;

    /** 价格6 */
    @Excel(name = "价格6")
    private Double price6;

    /** 价格7 */
    @Excel(name = "价格7")
    private Double price7;

    /** 价格8 */
    @Excel(name = "价格8")
    private Double price8;

    /** 价格9 */
    @Excel(name = "价格9")
    private Double price9;

    /** 价格10 */
    @Excel(name = "价格10")
    private Double price10;

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

    /** 备用11 */
    @Excel(name = "备用11")
    private String string11;

    /** 备用12 */
    @Excel(name = "备用12")
    private String string12;

    /** 备用13 */
    @Excel(name = "备用13")
    private String string13;

    /** 备用14 */
    @Excel(name = "备用14")
    private String string14;

    /** 备用15 */
    @Excel(name = "备用15")
    private String string15;

    /** 备用16 */
    @Excel(name = "备用16")
    private String string16;

    /** 备用17 */
    @Excel(name = "备用17")
    private String string17;

    /** 备用18 */
    @Excel(name = "备用18")
    private String string18;

    /** 备用19 */
    @Excel(name = "备用19")
    private String string19;

    /** 备用20 */
    @Excel(name = "备用20")
    private String string20;

    /** 时间1 */
    @Excel(name = "时间1", width = 30, dateFormat = "yyyy-MM-dd")
    private Date datetime1;

    /** 时间2 */
    @Excel(name = "时间2", width = 30, dateFormat = "yyyy-MM-dd")
    private Date datetime2;

    /** 时间3 */
    @Excel(name = "时间3", width = 30, dateFormat = "yyyy-MM-dd")
    private Date datetime3;

    /** 时间4 */
    @Excel(name = "时间4", width = 30, dateFormat = "yyyy-MM-dd")
    private Date datetime4;

    /** 时间5 */
    @Excel(name = "时间5", width = 30, dateFormat = "yyyy-MM-dd")
    private Date datetime5;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 查询标记：0 全部 1 不合格品>0   */
    private int isReject;

    /** 不合格品处理结果*/
    private String processMode;

    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }

    public int getIsReject() {
        return isReject;
    }

    public void setIsReject(int isReject) {
        this.isReject = isReject;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Long getChildId() {
        return childId;
    }
    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Long getDataId() {
        return dataId;
    }
    public void setPrice1(Double price1) {
        this.price1 = price1;
    }

    public Double getPrice1() {
        return price1;
    }
    public void setPrice2(Double price2) {
        this.price2 = price2;
    }

    public Double getPrice2() {
        return price2;
    }
    public void setPrice3(Double price3) {
        this.price3 = price3;
    }

    public Double getPrice3() {
        return price3;
    }
    public void setPrice4(Double price4) {
        this.price4 = price4;
    }

    public Double getPrice4() {
        return price4;
    }
    public void setPrice5(Double price5) {
        this.price5 = price5;
    }

    public Double getPrice5() {
        return price5;
    }
    public void setPrice6(Double price6) {
        this.price6 = price6;
    }

    public Double getPrice6() {
        return price6;
    }
    public void setPrice7(Double price7) {
        this.price7 = price7;
    }

    public Double getPrice7() {
        return price7;
    }
    public void setPrice8(Double price8) {
        this.price8 = price8;
    }

    public Double getPrice8() {
        return price8;
    }
    public void setPrice9(Double price9) {
        this.price9 = price9;
    }

    public Double getPrice9() {
        return price9;
    }
    public void setPrice10(Double price10) {
        this.price10 = price10;
    }

    public Double getPrice10() {
        return price10;
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
    public void setString11(String string11) {
        this.string11 = string11;
    }

    public String getString11() {
        return string11;
    }
    public void setString12(String string12) {
        this.string12 = string12;
    }

    public String getString12() {
        return string12;
    }
    public void setString13(String string13) {
        this.string13 = string13;
    }

    public String getString13() {
        return string13;
    }
    public void setString14(String string14) {
        this.string14 = string14;
    }

    public String getString14() {
        return string14;
    }
    public void setString15(String string15) {
        this.string15 = string15;
    }

    public String getString15() {
        return string15;
    }
    public void setString16(String string16) {
        this.string16 = string16;
    }

    public String getString16() {
        return string16;
    }
    public void setString17(String string17) {
        this.string17 = string17;
    }

    public String getString17() {
        return string17;
    }
    public void setString18(String string18) {
        this.string18 = string18;
    }

    public String getString18() {
        return string18;
    }
    public void setString19(String string19) {
        this.string19 = string19;
    }

    public String getString19() {
        return string19;
    }
    public void setString20(String string20) {
        this.string20 = string20;
    }

    public String getString20() {
        return string20;
    }
    public void setDatetime1(Date datetime1) {
        this.datetime1 = datetime1;
    }

    public Date getDatetime1() {
        return datetime1;
    }
    public void setDatetime2(Date datetime2) {
        this.datetime2 = datetime2;
    }

    public Date getDatetime2() {
        return datetime2;
    }
    public void setDatetime3(Date datetime3) {
        this.datetime3 = datetime3;
    }

    public Date getDatetime3() {
        return datetime3;
    }
    public void setDatetime4(Date datetime4) {
        this.datetime4 = datetime4;
    }

    public Date getDatetime4() {
        return datetime4;
    }
    public void setDatetime5(Date datetime5) {
        this.datetime5 = datetime5;
    }

    public Date getDatetime5() {
        return datetime5;
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
            .append("childId", getChildId())
            .append("dataId", getDataId())
            .append("remark", getRemark())
            .append("price1", getPrice1())
            .append("price2", getPrice2())
            .append("price3", getPrice3())
            .append("price4", getPrice4())
            .append("price5", getPrice5())
            .append("price6", getPrice6())
            .append("price7", getPrice7())
            .append("price8", getPrice8())
            .append("price9", getPrice9())
            .append("price10", getPrice10())
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
            .append("string11", getString11())
            .append("string12", getString12())
            .append("string13", getString13())
            .append("string14", getString14())
            .append("string15", getString15())
            .append("string16", getString16())
            .append("string17", getString17())
            .append("string18", getString18())
            .append("string19", getString19())
            .append("string20", getString20())
            .append("datetime1", getDatetime1())
            .append("datetime2", getDatetime2())
            .append("datetime3", getDatetime3())
            .append("datetime4", getDatetime4())
            .append("datetime5", getDatetime5())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
