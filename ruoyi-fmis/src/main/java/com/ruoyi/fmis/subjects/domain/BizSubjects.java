package com.ruoyi.fmis.subjects.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 科目对象 biz_subjects
 *
 * @author frank
 * @date 2020-06-28
 */
public class BizSubjects extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long subjectsId;

    @Getter
    @Setter
    private boolean flag;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 日期 */
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date d;

    /** 科目类别 */
    @Excel(name = "科目类别")
    private String type;

    /** 费用 */
    @Excel(name = "费用")
    private Double amount;

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
    public void setD(Date d) {
        this.d = d;
    }

    public Date getD() {
        return d;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
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
            .append("subjectsId", getSubjectsId())
            .append("name", getName())
            .append("d", getD())
            .append("type", getType())
            .append("amount", getAmount())
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
