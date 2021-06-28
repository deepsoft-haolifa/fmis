package com.ruoyi.fmis.finance.domain.vo;

import lombok.Data;

/**
 * 合同汇总的返回实体
 * @author murphy.he
 **/
@Data
public class ProcurementSummaryRespVo {

    /**
     * 供应商
     */
    private String supplierName;

    /**
     * 供应商
     */
    private String supplierId;
    /**
     * 合同额
     */
    private Double contractAmount;

    /**
     * 入账额
     */
    private Double entryAmount;

    /**
     * 入账欠款
     */
    private Double entryOweAmount;
    /**
     * 已付款
     */
    private Double payedAmount;
    /**
     * 回票欠款
     */
    private Double needInvoiceAmount;
    /**
     * 已开票
     */
    private Double invoicedAmount;

    /**
     * 入账欠票
     */
    private Double entryOweInvoiceAmount;



}
