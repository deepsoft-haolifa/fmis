package com.ruoyi.fmis.status.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据状态对象 biz_data_status
 *
 * @author frank
 * @date 2020-06-16
 */
public class BizDataStatus extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long statusId;

    /** 1=产品 2=执行器 3=法兰 4=螺栓 5=定位器 6=电磁阀 7=回信器数 8=气源三连件 9=可离合减速器 */
    @Excel(name = "")
    private String type;

    /** 关联ID 关联 child表 将来也可以关联dataId */
    @Excel(name = "关联ID 关联 child表 将来也可以关联dataId")
    private Long childId;

    /** 例如合同状态 0=未处理 1=已处理 */
    @Excel(name = "例如合同状态 0=未处理 1=已处理")
    private String dataStatus;




    /** 产品 0=未处理 1=已处理 */
    @Excel(name = "产品 0=未处理 1=已处理")
    private String productStatus;

    /** 执行器 0=未处理 1=已处理 */
    @Excel(name = "执行器 0=未处理 1=已处理")
    private String actuatorStatus;

    /** 法兰 0=未处理 1=已处理 */
    @Excel(name = "法兰 0=未处理 1=已处理")
    private String ref1Status;

    /** 螺栓 0=未处理 1=已处理 */
    @Excel(name = "螺栓 0=未处理 1=已处理")
    private String ref2Status;

    /** 定位器 0=未处理 1=已处理 */
    @Excel(name = "定位器 0=未处理 1=已处理")
    private String pStatus;

    /** 电磁阀 0=未处理 1=已处理 */
    @Excel(name = "电磁阀 0=未处理 1=已处理")
    private String p1Status;

    /** 回信器数 0=未处理 1=已处理 */
    @Excel(name = "回信器数 0=未处理 1=已处理")
    private String p2Status;

    /** 气源三连件 0=未处理 1=已处理 */
    @Excel(name = "气源三连件 0=未处理 1=已处理")
    private String p3Status;

    /** 可离合减速器 0=未处理 1=已处理 */
    @Excel(name = "可离合减速器 0=未处理 1=已处理")
    private String p4Status;

    /** 备用1 数量*/
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

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getStatusId() {
        return statusId;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Long getChildId() {
        return childId;
    }
    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getDataStatus() {
        return dataStatus;
    }
    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductStatus() {
        return productStatus;
    }
    public void setActuatorStatus(String actuatorStatus) {
        this.actuatorStatus = actuatorStatus;
    }

    public String getActuatorStatus() {
        return actuatorStatus;
    }
    public void setRef1Status(String ref1Status) {
        this.ref1Status = ref1Status;
    }

    public String getRef1Status() {
        return ref1Status;
    }
    public void setRef2Status(String ref2Status) {
        this.ref2Status = ref2Status;
    }

    public String getRef2Status() {
        return ref2Status;
    }
    public void setPStatus(String pStatus) {
        this.pStatus = pStatus;
    }

    public String getPStatus() {
        return pStatus;
    }
    public void setP1Status(String p1Status) {
        this.p1Status = p1Status;
    }

    public String getP1Status() {
        return p1Status;
    }
    public void setP2Status(String p2Status) {
        this.p2Status = p2Status;
    }

    public String getP2Status() {
        return p2Status;
    }
    public void setP3Status(String p3Status) {
        this.p3Status = p3Status;
    }

    public String getP3Status() {
        return p3Status;
    }
    public void setP4Status(String p4Status) {
        this.p4Status = p4Status;
    }

    public String getP4Status() {
        return p4Status;
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
            .append("statusId", getStatusId())
            .append("type", getType())
            .append("childId", getChildId())
            .append("dataStatus", getDataStatus())
            .append("productStatus", getProductStatus())
            .append("actuatorStatus", getActuatorStatus())
            .append("ref1Status", getRef1Status())
            .append("ref2Status", getRef2Status())
            .append("pStatus", getPStatus())
            .append("p1Status", getP1Status())
            .append("p2Status", getP2Status())
            .append("p3Status", getP3Status())
            .append("p4Status", getP4Status())
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
