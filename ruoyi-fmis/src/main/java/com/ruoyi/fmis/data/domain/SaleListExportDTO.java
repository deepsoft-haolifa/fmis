package com.ruoyi.fmis.data.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销货清单导出实体
 *
 * @author murphy.he
 **/
@ExcelIgnoreUnannotated
@ColumnWidth(16)
@HeadRowHeight(14)
@HeadFontStyle(fontHeightInPoints = 11)
@Data
public class SaleListExportDTO {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ExcelProperty(value = "操作序号")
    private Integer id;

    /** 操作模块 */
    @ExcelProperty(value = "货物或应税劳务、服务名称")
    private String productName;

    /** 业务类型（0其它 1新增 2修改 3删除） */
//    @ExcelProperty(value = "业务类型", converter = BusiTypeStringNumberConverter.class)
    @ExcelProperty(value = "计量单位")
    private String unitMeasurement;
    @ExcelProperty(value = "规格型号")
    private String specAndModel;
    @ExcelProperty(value = "数量")
    private String quantity;
    @ExcelProperty(value = "金额")
    private String amount;
    @ExcelProperty(value = "税率")
    private String taxRate;
    @ExcelProperty(value = "商品税目")
    private String productTax;
    @ExcelProperty(value = "折扣金额")
    private BigDecimal discountAmount;
    @ExcelProperty(value = "税额")
    private BigDecimal taxAmount;
    @ExcelProperty(value = "折扣税额")
    private BigDecimal discountTaxAmount;
    @ExcelProperty(value = "折扣率")
    private BigDecimal discountRate;
    @ExcelProperty(value = "单价")
    private BigDecimal price;
    @ExcelProperty(value = "价格方式")
    private Integer priceType;
    @ExcelProperty(value = "税收分类编码版本号")
    private String taxClassifyCodeVersion;
    @ExcelProperty(value = "税收分类编码")
    private String taxClassifyCode;
    @ExcelProperty(value = "企业商品编码")
    private String busProductCode;
    @ExcelProperty(value = "使用优惠政策标识")
    private String yhzcFlag;
    @ExcelProperty(value = "零税率标识")
    private String zeroTaxFlag;
    @ExcelProperty(value = "优惠政策说明")
    private String yhDesc;
    @ExcelProperty(value = "中外合作油气田标识")
    private Integer zwFlag;







}
