package com.ruoyi.fmis.invoice.bean.export;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销项发票开票信息导出实体
 *
 * @author murphy.he
 **/
@ExcelIgnoreUnannotated
@ColumnWidth(16)
@HeadRowHeight(14)
@HeadFontStyle(fontHeightInPoints = 11)
@Data
public class InvoiceExportDTO {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ExcelProperty(value = "操作序号")
    private Integer id;
    @ExcelProperty(value = "发票代码")
    private String invoiceCode;
    @ExcelProperty(value = "发票号码")
    private String invoiceNo;
    @ExcelProperty(value = "购方企业名称")
    private String customerName;
    @ExcelProperty(value = "购方税号")
    private String customerTaxNo;
    @ExcelProperty(value = "银行账户")
    private String customerBankAccount;
    @ExcelProperty(value = "地址电话")
    private String customerAddrPhone;
    @ExcelProperty(value = "开票日期")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date invoiceDate;
    @ExcelProperty(value = "商品编码版本号")
    private String productCodeVersion;
    @ExcelProperty(value = "单据号")
    private String busNo;
    @ExcelProperty(value = "商品名称")
    private String productName;
    @ExcelProperty(value = "规格")
    private String spec;
    @ExcelProperty(value = "单位")
    private String unit;
    @ExcelProperty(value = "数量")
    private Integer quantity;
    @ExcelProperty(value = "单价")
    private BigDecimal price;
    @ExcelProperty(value = "金额")
    private String amount;
    @ExcelProperty(value = "税率")
    private String taxRate;
    @ExcelProperty(value = "税额")
    private String taxAmount;
    @ExcelProperty(value = "税收分类编码")
    private String taxClassifyCode;








}
