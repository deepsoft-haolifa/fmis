package com.ruoyi.fmis.product.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.beans.Transient;

/**
 * 业务产品对象 biz_product
 *
 * @author Xianlu Tech
 * @date 2020-02-26
 */
public class BizProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 产品id */
    private Long productId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String name;

    /** 系列 数据字典 */
    @Excel(name = "系列 数据字典")
    private String series;

    /** 产品型号 */
    @Excel(name = "产品型号")
    private String model;

    @Setter
    @Getter
    @Excel(name = "颜色")
    private String color;

    @Setter
    @Getter
    private String modelEq;

    /** 规格 */
    @Excel(name = "规格")
    private String specifications;

    @Getter
    @Setter
    private String specificationsName;


    @Getter
    @Setter
    private String seriesName;

    /** 公称压力 */
    @Excel(name = "公称压力")
    private String nominalPressure;

    /** 连接方式 */
    @Excel(name = "连接方式")
    private String connectionType;

    /** 阀体材质 */
    @Excel(name = "阀体材质")
    private String valvebodyMaterial;

    /** 阀板材质 */
    @Excel(name = "阀板材质")
    private String valveMaterial;

    /** 密封材质 */
    @Excel(name = "密封材质")
    private String sealingMaterial;

    /** 阀芯材质 */
    @Excel(name = "阀芯材质")
    private String valveElement;

    /** 驱动形式 */
    @Excel(name = "驱动形式")
    private String driveForm;





    /** 供应商代码 */
    @Excel(name = "供应商代码")
    private String supplier;

    @Getter
    @Setter
    /** 供应商名称*/
    private String supplierName;
    @Getter
    @Setter
    private String nickName;
    private String newSupplier;
    public void setNewSupplier(String newSupplier) {
        this.newSupplier = newSupplier;
    }
    public String getNewSupplier() {
        return newSupplier;
    }

    private String medium;
    public void setMedium(String medium) {
        this.medium = medium;
    }
    public String getMedium() {
        return medium;
    }

    private String temperature;
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public String getTemperature() {
        return temperature;
    }

    private String other;
    public void setOther(String other) {
        this.other = other;
    }
    public String getOther() {
        return other;
    }

    /**
     * 系列
     */
    private String string1;
    public void setString1(String string1) {
        this.string1 = string1;
    }
    public String getString1() {
        return string1;
    }

    /**
     * 产品等级
     */
    private String string2;
    public void setString2(String string2) {
        this.string2 = string2;
    }
    public String getString2() {
        return string2;
    }

    private String string3;
    public void setString3(String string3) {
        this.string3 = string3;
    }
    public String getString3() {
        return string3;
    }

    private String string4;
    public void setString4(String string4) {
        this.string4 = string4;
    }
    public String getString4() {
        return string4;
    }

    private String string5;
    public void setString5(String string5) {
        this.string5 = string5;
    }
    public String getString5() {
        return string5;
    }

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


    /** 结构形式 */
    @Excel(name = "结构形式")
    private String structuralStyle;
    public void setStructuralStyle(String structuralStyle) {
        this.structuralStyle = structuralStyle;
    }
    public String getStructuralStyle() {
        return structuralStyle;
    }


    /** 产品单价 */
    @Excel(name = "产品单价 销售底价")
    private Double price;

    @Excel(name = "采购价")
    private Double procurementPrice;
    public void setProcurementPrice(Double procurementPrice) {
        this.procurementPrice = procurementPrice;
    }
    public Double getProcurementPrice() {
        return procurementPrice;
    }


    @Excel(name = "成本价")
    private Double costPrice;
    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }
    public Double getCostPrice() {
        return costPrice;
    }



    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeries() {
        return series;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }
    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getSpecifications() {
        return specifications;
    }
    public void setNominalPressure(String nominalPressure) {
        this.nominalPressure = nominalPressure;
    }

    public String getNominalPressure() {
        return nominalPressure;
    }
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }
    public void setValvebodyMaterial(String valvebodyMaterial) {
        this.valvebodyMaterial = valvebodyMaterial;
    }

    public String getValvebodyMaterial() {
        return valvebodyMaterial;
    }
    public void setValveMaterial(String valveMaterial) {
        this.valveMaterial = valveMaterial;
    }

    public String getValveMaterial() {
        return valveMaterial;
    }
    public void setSealingMaterial(String sealingMaterial) {
        this.sealingMaterial = sealingMaterial;
    }

    public String getSealingMaterial() {
        return sealingMaterial;
    }
    public void setValveElement(String valveElement) {
        this.valveElement = valveElement;
    }

    public String getValveElement() {
        return valveElement;
    }
    public void setDriveForm(String driveForm) {
        this.driveForm = driveForm;
    }

    public String getDriveForm() {
        return driveForm;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplier() {
        return supplier;
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
            .append("productId", getProductId())
            .append("name", getName())
            .append("series", getSeries())
            .append("model", getModel())
            .append("specifications", getSpecifications())
            .append("nominalPressure", getNominalPressure())
            .append("connectionType", getConnectionType())
            .append("valvebodyMaterial", getValvebodyMaterial())
            .append("valveMaterial", getValveMaterial())
            .append("sealingMaterial", getSealingMaterial())
            .append("valveElement", getValveElement())
            .append("driveForm", getDriveForm())
            .append("price", getPrice())
            .append("supplier", getSupplier())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("structuralStyle", getStructuralStyle())
            .toString();
    }
}
