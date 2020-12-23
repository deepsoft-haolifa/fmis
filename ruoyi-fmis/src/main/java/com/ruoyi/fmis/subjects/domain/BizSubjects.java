package com.ruoyi.fmis.subjects.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 费用科目对象 biz_subjects
 *
 * @author hedong
 * @date 2020-12-20
 */
public class BizSubjects extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private boolean flag;

    /** ID */
    private Long subjectsId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 科目类别，固定，变动，可节约 数据字典 */
    @Excel(name = "科目类别，固定，变动，可节约 数据字典")
    private String type;

    /** 父部门Id */
    private Long parentId;

    @Excel(name = "父部门名称")
    private String parentName;

    /** 级别 */
    @Excel(name = "级别")
    private Integer level;

    /** 代码 */
    @Excel(name = "代码")
    private String code;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setSubjectsId(Long subjectsId) {
        this.subjectsId = subjectsId;
    }

    public Long getSubjectsId() {
        return subjectsId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
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
            .append("subjectsId", getSubjectsId())
            .append("name", getName())
            .append("type", getType())
            .append("parentId", getParentId())
            .append("level", getLevel())
            .append("code", getCode())
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
