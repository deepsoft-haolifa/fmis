package com.ruoyi.fmis.dict.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业务数据字典对象 biz_dict
 *
 * @author Xianlu Tech
 * @date 2020-03-11
 */
public class BizDict extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long dictId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 名称1 */
    private String name1;

    /** Code */
    @Excel(name = "Code")
    private String code;

    /** ID */
    private Long parentId;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 上级type */
    @Excel(name = "上级type")
    private String parentType;


    /** 是否存在此标识 默认不存在 */
    private boolean flag = false;


    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public Long getDictId() {
        return dictId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName1() {
        return name1;
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
    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getParentType() {
        return parentType;
    }


    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictId", getDictId())
            .append("name", getName())
            .append("name1", getName1())
            .append("code", getCode())
            .append("parentId", getParentId())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("parentType", getParentType())
            .toString();
    }
}
