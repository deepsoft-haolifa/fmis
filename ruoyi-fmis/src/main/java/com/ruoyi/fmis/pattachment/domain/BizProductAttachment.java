package com.ruoyi.fmis.pattachment.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 定位器对象 biz_product_attachment
 *
 * @author frank
 * @date 2020-06-08
 */
public class BizProductAttachment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long attachmentId;

    @Getter
    @Setter
    private String supplierName;

    /** 类型 1=定位器 2=电磁阀，3=回信器数，4=气源三连件，5=可离合减速器 */
    private String type;

    /** 商品编号 */
    @Excel(name = "商品编号")
    private String bh;

    /** 中文品名 */
    @Excel(name = "中文品名")
    private String chineseName;

    /** 中文规格 */
    @Excel(name = "中文规格")
    private String chineseSpecifications;

    /** 英文品名 */
    @Excel(name = "英文品名")
    private String englishName;

    /** 英文规格 */
    @Excel(name = "英文规格")
    private String englishSpecifications;

    /** 中文包装 */
    @Excel(name = "中文包装")
    private String chinesePackaging;

    /** 英文包装 */
    @Excel(name = "英文包装")
    private String englishPackaging;

    /** 中文单位 */
    @Excel(name = "中文单位")
    private String chineseUnit;

    /** 英文单位 */
    @Excel(name = "英文单位")
    private String englishUnit;

    /** 压力 */
    @Excel(name = "压力")
    private String pressure;

    /** 材质 */
    @Excel(name = "材质")
    private String material;

    /** 条形码 */
    @Excel(name = "条形码")
    private String barCode;

    /** 海关编码 */
    @Excel(name = "海关编码")
    private String customsBh;

    /** 操作费 */
    @Excel(name = "操作费")
    private String handlingFee;

    /** 颜色 */
    @Excel(name = "颜色")
    private String color;

    /** 开发人员 */
    @Excel(name = "开发人员")
    private String developer;

    /** 商品分类 */
    @Excel(name = "商品分类")
    private String goodsCategory;

    /** 供应商ID */
    @Excel(name = "供应商ID")
    private String supplier;

    /** 箱率 */
    @Excel(name = "箱率")
    private String box;

    /** 内盒数量 */
    @Excel(name = "内盒数量")
    private String innerboxCount;

    /** 体积 */
    @Excel(name = "体积")
    private String bulk;

    /** 长 */
    @Excel(name = "长")
    private String l;

    /** 宽 */
    @Excel(name = "宽")
    private String w;

    /** 高 */
    @Excel(name = "高")
    private String h;

    /** 毛重 */
    @Excel(name = "毛重")
    private String grossWeight;

    /** 净重 */
    @Excel(name = "净重")
    private String netWeight;

    /** 成本价 */
    @Excel(name = "成本价")
    private Double costPrice;

    /** 结算价 */
    @Excel(name = "结算价")
    private Double settlementPrice;

    /** 销售底价 */
    @Excel(name = "销售底价")
    private Double basePrice;

    /** 销售价 */
    @Excel(name = "销售价")
    private Double price;

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

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getBh() {
        return bh;
    }
    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }
    public void setChineseSpecifications(String chineseSpecifications) {
        this.chineseSpecifications = chineseSpecifications;
    }

    public String getChineseSpecifications() {
        return chineseSpecifications;
    }
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getEnglishName() {
        return englishName;
    }
    public void setEnglishSpecifications(String englishSpecifications) {
        this.englishSpecifications = englishSpecifications;
    }

    public String getEnglishSpecifications() {
        return englishSpecifications;
    }
    public void setChinesePackaging(String chinesePackaging) {
        this.chinesePackaging = chinesePackaging;
    }

    public String getChinesePackaging() {
        return chinesePackaging;
    }
    public void setEnglishPackaging(String englishPackaging) {
        this.englishPackaging = englishPackaging;
    }

    public String getEnglishPackaging() {
        return englishPackaging;
    }
    public void setChineseUnit(String chineseUnit) {
        this.chineseUnit = chineseUnit;
    }

    public String getChineseUnit() {
        return chineseUnit;
    }
    public void setEnglishUnit(String englishUnit) {
        this.englishUnit = englishUnit;
    }

    public String getEnglishUnit() {
        return englishUnit;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPressure() {
        return pressure;
    }
    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }
    public void setCustomsBh(String customsBh) {
        this.customsBh = customsBh;
    }

    public String getCustomsBh() {
        return customsBh;
    }
    public void setHandlingFee(String handlingFee) {
        this.handlingFee = handlingFee;
    }

    public String getHandlingFee() {
        return handlingFee;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDeveloper() {
        return developer;
    }
    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplier() {
        return supplier;
    }
    public void setBox(String box) {
        this.box = box;
    }

    public String getBox() {
        return box;
    }
    public void setInnerboxCount(String innerboxCount) {
        this.innerboxCount = innerboxCount;
    }

    public String getInnerboxCount() {
        return innerboxCount;
    }
    public void setBulk(String bulk) {
        this.bulk = bulk;
    }

    public String getBulk() {
        return bulk;
    }
    public void setL(String l) {
        this.l = l;
    }

    public String getL() {
        return l;
    }
    public void setW(String w) {
        this.w = w;
    }

    public String getW() {
        return w;
    }
    public void setH(String h) {
        this.h = h;
    }

    public String getH() {
        return h;
    }
    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getGrossWeight() {
        return grossWeight;
    }
    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getNetWeight() {
        return netWeight;
    }
    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getCostPrice() {
        return costPrice;
    }
    public void setSettlementPrice(Double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public Double getSettlementPrice() {
        return settlementPrice;
    }
    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getBasePrice() {
        return basePrice;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
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
            .append("attachmentId", getAttachmentId())
            .append("type", getType())
            .append("bh", getBh())
            .append("chineseName", getChineseName())
            .append("chineseSpecifications", getChineseSpecifications())
            .append("englishName", getEnglishName())
            .append("englishSpecifications", getEnglishSpecifications())
            .append("chinesePackaging", getChinesePackaging())
            .append("englishPackaging", getEnglishPackaging())
            .append("chineseUnit", getChineseUnit())
            .append("englishUnit", getEnglishUnit())
            .append("pressure", getPressure())
            .append("material", getMaterial())
            .append("barCode", getBarCode())
            .append("customsBh", getCustomsBh())
            .append("handlingFee", getHandlingFee())
            .append("color", getColor())
            .append("developer", getDeveloper())
            .append("goodsCategory", getGoodsCategory())
            .append("supplier", getSupplier())
            .append("box", getBox())
            .append("innerboxCount", getInnerboxCount())
            .append("bulk", getBulk())
            .append("l", getL())
            .append("w", getW())
            .append("h", getH())
            .append("grossWeight", getGrossWeight())
            .append("netWeight", getNetWeight())
            .append("costPrice", getCostPrice())
            .append("settlementPrice", getSettlementPrice())
            .append("basePrice", getBasePrice())
            .append("price", getPrice())
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
