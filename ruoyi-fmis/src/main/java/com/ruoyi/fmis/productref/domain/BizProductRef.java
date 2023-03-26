package com.ruoyi.fmis.productref.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 产品关系对象 biz_product_ref
 *
 * @author frank
 * @date 2020-03-17
 */
public class BizProductRef extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long productRefId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 类型 1=法兰 2=螺栓 */
    private String type;

    /** 型号 */
    @Excel(name = "型号")
    private String model;

    /** 规格 */
    @Excel(name = "规格")
    private String specifications;

    /** 材质 */
    @Excel(name = "材质")
    private String valvebodyMaterial;

    /** 材质要求 */
    @Excel(name = "材质要求")
    private String materialRequire;

    /** 单价 */
    @Excel(name = "单价")
    private Double price;

    /** 单价 */
    @Excel(name = "单价")
    private Double facePrice;

    /** 供应商 */
    @Excel(name = "供应商")
    private Long suppliersId;

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

    private String suppliersName;
    public void setSuppliersName(String suppliersName) {
        this.suppliersName = suppliersName;
    }

    public String getSuppliersName() {
        return suppliersName;
    }

    public void setProductRefId(Long productRefId) {
        this.productRefId = productRefId;
    }

    public Long getProductRefId() {
        return productRefId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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
    public void setValvebodyMaterial(String valvebodyMaterial) {
        this.valvebodyMaterial = valvebodyMaterial;
    }

    public String getValvebodyMaterial() {
        return valvebodyMaterial;
    }
    public void setMaterialRequire(String materialRequire) {
        this.materialRequire = materialRequire;
    }

    public String getMaterialRequire() {
        return materialRequire;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }
    public void setSuppliersId(Long suppliersId) {
        this.suppliersId = suppliersId;
    }

    public Long getSuppliersId() {
        return suppliersId;
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

    public Double getFacePrice() {
        return facePrice;
    }

    public void setFacePrice(Double facePrice) {
        this.facePrice = facePrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productRefId", getProductRefId())
            .append("name", getName())
            .append("type", getType())
            .append("model", getModel())
            .append("specifications", getSpecifications())
            .append("valvebodyMaterial", getValvebodyMaterial())
            .append("materialRequire", getMaterialRequire())
            .append("price", getPrice())
            .append("facePrice", getFacePrice())
            .append("remark", getRemark())
            .append("suppliersId", getSuppliersId())
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
