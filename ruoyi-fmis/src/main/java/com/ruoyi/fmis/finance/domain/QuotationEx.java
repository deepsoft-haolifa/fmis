package com.ruoyi.fmis.finance.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 导出报价单
 *
 */
@Data
public class QuotationEx extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /** 产品型号 */
    @Excel(name = "产品型号")
    private String model;
    /** 凭证号 */
    @Excel(name = "规格")
    private String specifications;
    @Excel(name = "系列")
    private String string1;
    @Excel(name = "销售底价")
    private String productPrice;

    @Excel(name = "系数")
    private String productCoefficient;
    @Excel(name = "数量")
    private String pNumber;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
}
