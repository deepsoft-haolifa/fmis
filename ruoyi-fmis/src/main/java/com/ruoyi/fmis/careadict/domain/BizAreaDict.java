package com.ruoyi.fmis.careadict.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户区域字典对象 biz_area_dict
 *
 * @author frank
 * @date 2020-05-28
 */
public class BizAreaDict extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** null */
    @Excel(name = "null")
    private String areas;

    /** null */
    @Excel(name = "null")
    private String codeName;

    /** null */
    @Excel(name = "null")
    private String area;

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public String getAreas() {
        return areas;
    }
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeName() {
        return codeName;
    }
    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("areas", getAreas())
            .append("codeName", getCodeName())
            .append("area", getArea())
            .toString();
    }
}
