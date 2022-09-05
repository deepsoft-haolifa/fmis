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

    @Excel(name = "序号")
    private Integer number;
    /** 产品ID */
    @Excel(name = "产品ID")
    private String productId;
    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;
    /** 产品压力 */
    @Excel(name = "产品压力")
    private String pressure;
    /** 规格*/
    @Excel(name = "规格")
    private String specifications;
    @Excel(name = "数量")
    private String pNumber;
    @Excel(name = "单价（人民币）")
    private String productPrice;
    @Excel(name = "合计（人民币）")
    private String total;
    @Excel(name = "材质")
    private String caizhi;
    @Excel(name = "配置方案")
    private String pzfa;
    @Excel(name = "备注")
    private String remark;


    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCaizhi() {
        return caizhi;
    }

    public void setCaizhi(String caizhi) {
        this.caizhi = caizhi;
    }

    public String getPzfa() {
        return pzfa;
    }

    public void setPzfa(String pzfa) {
        this.pzfa = pzfa;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
