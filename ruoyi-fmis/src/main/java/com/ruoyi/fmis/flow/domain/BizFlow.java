package com.ruoyi.fmis.flow.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程记录对象 biz_flow
 *
 * @author frank
 * @date 2020-03-18
 */
public class BizFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long flowId;

    /** 业务表名称 */
    @Excel(name = "业务表名称")
    private String bizTable;

    /** 业务ID */
    @Excel(name = "业务ID")
    private Long bizId;

    /** 审核人ID */
    @Excel(name = "审核人ID")
    private Long examineUserId;

    /** 0=上报 1=同意 -1=不同意 */
    @Excel(name = "0=上报 1=同意 -1=不同意")
    private String flowStatus;

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

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String examineUserName;
    public void setExamineUserName(String examineUserName) {
        this.examineUserName = examineUserName;
    }
    public String getExamineUserName() {
        return examineUserName;
    }


    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getFlowId() {
        return flowId;
    }
    public void setBizTable(String bizTable) {
        this.bizTable = bizTable;
    }

    public String getBizTable() {
        return bizTable;
    }
    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public Long getBizId() {
        return bizId;
    }
    public void setExamineUserId(Long examineUserId) {
        this.examineUserId = examineUserId;
    }

    public Long getExamineUserId() {
        return examineUserId;
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
            .append("flowId", getFlowId())
            .append("bizTable", getBizTable())
            .append("bizId", getBizId())
            .append("examineUserId", getExamineUserId())
            .append("flowStatus", getFlowStatus())
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
