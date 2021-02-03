package com.ruoyi.fmis.paymentpay.bean;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 导出列表
 *
 * @author murphy.he
 **/
@Data
public class PaymentPayExportVo {
    @Excel(name = "编号")
    private String string2;

    @Excel(name = "类型", readConverterExp = "1=差旅费报销,2=费用报销")
    private String string1;

    /** 申请日期 */
    @Excel(name = "报销日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date datetime1;

    @Excel(name = "收款账户")
    private String string3;
    @Excel(name = "收款账号")
    private String string4;

    @Excel(name = "开户行")
    private String string5;

    @Excel(name = "报销金额")
    private double price1;

    @Excel(name = "报销部门")
    private String deptName;

    @Excel(name = "备注")
    private String remark;

    @Excel(name = "报销人")
    private String createByName;

    @Excel(name = "付款状态", readConverterExp = "0=未付款,1=已付款")
    private String string11;
}
