package com.ruoyi.fmis.actuatorref.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 执行器关系管理对象 biz_actuator_ref
 *
 * @author frank
 * @date 2020-03-17
 */
public class BizActuatorRef extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long arId;

    /** 品牌名称 */
    @Excel(name = "品牌名称")
    private String brand;

    /** 驱动类型 */
    @Excel(name = "驱动类型")
    private String driveType;

    /** 介质类型 */
    @Excel(name = "介质类型")
    private String mediaType;

    /** 阀门规格 对应产品表规格 */
    @Excel(name = "阀门规格 对应产品表规格")
    private String valveType;

    /** 上法兰 */
    @Excel(name = "上法兰")
    private String topFlange;

    /** 压力 对应产品表压力 */
    @Excel(name = "压力 对应产品表压力")
    private String pressure;

    /** 扭矩 */
    @Excel(name = "扭矩")
    private String torsion;

    /** 倍率 */
    @Excel(name = "倍率")
    private String multiplyingPower;

    /** 驱动器型号 */
    @Excel(name = "驱动器型号")
    private String driveMode;

    /** 适配系列 */
    @Excel(name = "适配系列")
    private String fitType;

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

    public void setArId(Long arId) {
        this.arId = arId;
    }

    public Long getArId() {
        return arId;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }
    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getDriveType() {
        return driveType;
    }
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }
    public void setValveType(String valveType) {
        this.valveType = valveType;
    }

    public String getValveType() {
        return valveType;
    }
    public void setTopFlange(String topFlange) {
        this.topFlange = topFlange;
    }

    public String getTopFlange() {
        return topFlange;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPressure() {
        return pressure;
    }
    public void setTorsion(String torsion) {
        this.torsion = torsion;
    }

    public String getTorsion() {
        return torsion;
    }
    public void setMultiplyingPower(String multiplyingPower) {
        this.multiplyingPower = multiplyingPower;
    }

    public String getMultiplyingPower() {
        return multiplyingPower;
    }
    public void setDriveMode(String driveMode) {
        this.driveMode = driveMode;
    }

    public String getDriveMode() {
        return driveMode;
    }
    public void setFitType(String fitType) {
        this.fitType = fitType;
    }

    public String getFitType() {
        return fitType;
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
            .append("arId", getArId())
            .append("brand", getBrand())
            .append("driveType", getDriveType())
            .append("mediaType", getMediaType())
            .append("valveType", getValveType())
            .append("topFlange", getTopFlange())
            .append("pressure", getPressure())
            .append("torsion", getTorsion())
            .append("multiplyingPower", getMultiplyingPower())
            .append("driveMode", getDriveMode())
            .append("fitType", getFitType())
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
