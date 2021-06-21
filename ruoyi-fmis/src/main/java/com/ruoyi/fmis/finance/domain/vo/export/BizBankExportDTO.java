package com.ruoyi.fmis.finance.domain.vo.export;

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
 * 银行凭证导出
 *
 * @author murphy.he
 **/
@ExcelIgnoreUnannotated
@ColumnWidth(16)
@HeadRowHeight(14)
@HeadFontStyle(fontHeightInPoints = 11)
@Data
public class BizBankExportDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "操作序号")
    private Integer id;

    @ExcelProperty(value = "制单日期")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date operateDate;

    @ExcelProperty(value = "科目编码")
    private String payAccount;
    @ExcelProperty(value = "结算方式编码")
    private String payWay;
    @ExcelProperty(value = "借方金额")
    private BigDecimal collectionMoney;
    @ExcelProperty(value = "贷方金额")
    private BigDecimal paymentMoney;
    @ExcelProperty(value = "供应商编码")
    private String collectCompany;
    @ExcelProperty(value = "客户编码")
    private String payCompany;
    @ExcelProperty(value = "现金流量项目", converter = BillTypeConverter.class)
    private String type;
    @ExcelProperty(value = "现金流量借方金额")
    private BigDecimal cashCollectionMoney;
    @ExcelProperty(value = "现金流量贷方金额")
    private BigDecimal cashPaymentMoney;
}
