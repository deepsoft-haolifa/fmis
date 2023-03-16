package com.ruoyi.fmis.actuator.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 执行器对象 biz_actuator
 *
 * @author frank
 * @date 2020-03-17
 */
public class BizActuator extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Getter
    @Setter
    private String supplier;

    /** ID */
    private Long actuatorId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String name;

    /** 执行器品牌 */
    @Excel(name = "执行器品牌")
    private String brand;

    /** 生产厂家 */
    @Excel(name = "生产厂家")
    private String manufacturer;

    /** 安装形式 气动：类别*/
    @Excel(name = "安装形式")
    private String setupType;

    /** 输出力距 */
    @Excel(name = "输出力距")
    private String outputTorque;

    /** 开启时间 */
    @Excel(name = "开启时间")
    private String actionType;

    /** 控制电路 */
    @Excel(name = "控制电路")
    private String controlCircuit;

    /** 适用电压 */
    @Excel(name = "适用电压")
    private String adaptableVoltage;

    /** 防护等级 */
    @Excel(name = "防护等级")
    private String protectionLevel;

    /** 品质等级 */
    @Excel(name = "品质等级")
    private String qualityLevel;

    /** 防爆等级 */
    @Excel(name = "防爆等级")
    private String explosionLevel;

    /** 价格 */
    @Excel(name = "价格")
    private Double price;

    public Double getFacePrice() {
        return facePrice;
    }

    public void setFacePrice(Double facePrice) {
        this.facePrice = facePrice;
    }

    /** 价格 */
    @Excel(name = "面价")
    private Double facePrice;

    /** 备用1 型号*/
    private String string1;

    /** 备用2 类型 1=电动 2=气动*/
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

    private String color;
    private String press;

    public void setActuatorId(Long actuatorId) {
        this.actuatorId = actuatorId;
    }

    public Long getActuatorId() {
        return actuatorId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public void setSetupType(String setupType) {
        this.setupType = setupType;
    }

    public String getSetupType() {
        return setupType;
    }
    public void setOutputTorque(String outputTorque) {
        this.outputTorque = outputTorque;
    }

    public String getOutputTorque() {
        return outputTorque;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }
    public void setControlCircuit(String controlCircuit) {
        this.controlCircuit = controlCircuit;
    }

    public String getControlCircuit() {
        return controlCircuit;
    }
    public void setAdaptableVoltage(String adaptableVoltage) {
        this.adaptableVoltage = adaptableVoltage;
    }

    public String getAdaptableVoltage() {
        return adaptableVoltage;
    }
    public void setProtectionLevel(String protectionLevel) {
        this.protectionLevel = protectionLevel;
    }

    public String getProtectionLevel() {
        return protectionLevel;
    }
    public void setQualityLevel(String qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public String getQualityLevel() {
        return qualityLevel;
    }
    public void setExplosionLevel(String explosionLevel) {
        this.explosionLevel = explosionLevel;
    }

    public String getExplosionLevel() {
        return explosionLevel;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("actuatorId", getActuatorId())
            .append("name", getName())
            .append("brand", getBrand())
            .append("manufacturer", getManufacturer())
            .append("setupType", getSetupType())
            .append("outputTorque", getOutputTorque())
            .append("actionType", getActionType())
            .append("controlCircuit", getControlCircuit())
            .append("adaptableVoltage", getAdaptableVoltage())
            .append("protectionLevel", getProtectionLevel())
            .append("qualityLevel", getQualityLevel())
            .append("explosionLevel", getExplosionLevel())
            .append("price", getPrice())
            .append("facePrice", getFacePrice())
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
            .append("color", getColor())
            .append("press", getPress())
            .toString();
    }
}
