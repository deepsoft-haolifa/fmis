package com.ruoyi.fmis.assets.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 固定资产对象 biz_assets
 *
 * @author frank
 * @date 2020-07-01
 */
public class BizAssets extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long assetsId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String name;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String bh;

    /** 类别名称 数据字典 */
    @Excel(name = "类别名称 数据字典")
    private String type;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specifications;

    /** 设备数量 */
    @Excel(name = "设备数量")
    private String num;

    /** 部门 */
    @Excel(name = "部门")
    private String deptId;

    /** 领用人 */
    @Excel(name = "领用人")
    private String userName;

    /** 增加方式 数据字典 */
    @Excel(name = "增加方式 数据字典")
    private String addType;

    /** 存放地点 */
    @Excel(name = "存放地点")
    private String location;

    /** 设备状态 数据字典 */
    @Excel(name = "设备状态 数据字典")
    private String equipmentState;

    /** 生产厂家 */
    @Excel(name = "生产厂家")
    private String manufacturer;

    /** 采购时间 */
    @Excel(name = "采购时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchasingTime;

    /** 采购金额 */
    @Excel(name = "采购金额")
    private Double price;

    /** 使用年限 */
    @Excel(name = "使用年限")
    private String useYear;

    /** 折旧方法 */
    @Excel(name = "折旧方法")
    private String depreciationMethod;

    /** 开始使用日期 */
    @Excel(name = "开始使用日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 已计提月份 */
    @Excel(name = "已计提月份")
    private String accruedMonth;

    /** 净产值率 */
    @Excel(name = "净产值率")
    private String outputRate;

    /** 净残值 */
    @Excel(name = "净残值")
    private String salvageValue;

    /** 累计折旧 */
    @Excel(name = "累计折旧")
    private String accumulatedDepreciation;

    /** 月折旧率 */
    @Excel(name = "月折旧率")
    private String monthRate;

    /** 月折旧额 */
    @Excel(name = "月折旧额")
    private String monthDepreciation;

    /** 净值 */
    @Excel(name = "净值")
    private String netWorth;

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

    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    public Long getAssetsId() {
        return assetsId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getBh() {
        return bh;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getSpecifications() {
        return specifications;
    }
    public void setNum(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptId() {
        return deptId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
    public void setAddType(String addType) {
        this.addType = addType;
    }

    public String getAddType() {
        return addType;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
    public void setEquipmentState(String equipmentState) {
        this.equipmentState = equipmentState;
    }

    public String getEquipmentState() {
        return equipmentState;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public void setPurchasingTime(Date purchasingTime) {
        this.purchasingTime = purchasingTime;
    }

    public Date getPurchasingTime() {
        return purchasingTime;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }
    public void setUseYear(String useYear) {
        this.useYear = useYear;
    }

    public String getUseYear() {
        return useYear;
    }
    public void setDepreciationMethod(String depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public String getDepreciationMethod() {
        return depreciationMethod;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void setAccruedMonth(String accruedMonth) {
        this.accruedMonth = accruedMonth;
    }

    public String getAccruedMonth() {
        return accruedMonth;
    }
    public void setOutputRate(String outputRate) {
        this.outputRate = outputRate;
    }

    public String getOutputRate() {
        return outputRate;
    }
    public void setSalvageValue(String salvageValue) {
        this.salvageValue = salvageValue;
    }

    public String getSalvageValue() {
        return salvageValue;
    }
    public void setAccumulatedDepreciation(String accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public String getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }
    public void setMonthRate(String monthRate) {
        this.monthRate = monthRate;
    }

    public String getMonthRate() {
        return monthRate;
    }
    public void setMonthDepreciation(String monthDepreciation) {
        this.monthDepreciation = monthDepreciation;
    }

    public String getMonthDepreciation() {
        return monthDepreciation;
    }
    public void setNetWorth(String netWorth) {
        this.netWorth = netWorth;
    }

    public String getNetWorth() {
        return netWorth;
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
            .append("assetsId", getAssetsId())
            .append("name", getName())
            .append("bh", getBh())
            .append("type", getType())
            .append("specifications", getSpecifications())
            .append("num", getNum())
            .append("deptId", getDeptId())
            .append("userName", getUserName())
            .append("addType", getAddType())
            .append("location", getLocation())
            .append("equipmentState", getEquipmentState())
            .append("manufacturer", getManufacturer())
            .append("purchasingTime", getPurchasingTime())
            .append("price", getPrice())
            .append("useYear", getUseYear())
            .append("depreciationMethod", getDepreciationMethod())
            .append("startTime", getStartTime())
            .append("accruedMonth", getAccruedMonth())
            .append("outputRate", getOutputRate())
            .append("salvageValue", getSalvageValue())
            .append("accumulatedDepreciation", getAccumulatedDepreciation())
            .append("monthRate", getMonthRate())
            .append("monthDepreciation", getMonthDepreciation())
            .append("netWorth", getNetWorth())
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
