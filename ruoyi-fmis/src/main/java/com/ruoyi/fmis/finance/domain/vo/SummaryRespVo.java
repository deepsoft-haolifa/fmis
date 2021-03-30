package com.ruoyi.fmis.finance.domain.vo;

import lombok.Data;

/**
 * 合同汇总的返回实体
 * @author murphy.he
 **/
@Data
public class SummaryRespVo {

    /**
     * 供应商
     */
    private String supplierName;

    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 合同额
     */
    private Double contractAmount;
    /**
     * 应收款
     */
    private Double needCollectAmount;
    /**
     * 已收款
     */
    private Double collectedAmount;
    /**
     * 未开票
     */
    private Double needInvoiceAmount;
    /**
     * 已开票
     */
    private Double invoicedAmount;

    /**
     * 应付款
     */
    private Double needPayAmount;
    /**
     * 已付款
     */
    private Double paidAmount;

}
