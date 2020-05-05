package com.ruoyi.fmis.define.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业务流程定义对象 biz_process_define
 *
 * @author frank
 * @date 2020-05-05
 */
public class BizProcessDefine extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long defineId;

    /** 业余表名称 */
    @Excel(name = "业余表名称")
    @Getter
    @Setter
    private String tbName;

    /** 定义状态 1-2-3-4-5 */
    @Excel(name = "定义状态 1-2-3-4-5")
    private String defineFlow;

    /** 定义角色 process_ywy-process_xsjl-process_qyjl-process_fz-process_lz */
    @Excel(name = "定义角色 process_ywy-process_xsjl-process_qyjl-process_fz-process_lz")
    private String defineRole;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setDefineId(Long defineId) {
        this.defineId = defineId;
    }

    public Long getDefineId() {
        return defineId;
    }

    public void setDefineFlow(String defineFlow) {
        this.defineFlow = defineFlow;
    }

    public String getDefineFlow() {
        return defineFlow;
    }
    public void setDefineRole(String defineRole) {
        this.defineRole = defineRole;
    }

    public String getDefineRole() {
        return defineRole;
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
            .append("defineId", getDefineId())
            .append("tbName", getTbName())
            .append("defineFlow", getDefineFlow())
            .append("defineRole", getDefineRole())
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
