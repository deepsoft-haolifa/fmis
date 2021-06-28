package com.ruoyi.fmis.finance.domain.vo;

import lombok.Data;

/**
 * 销售合同汇总的返回实体
 * @author murphy.he
 **/
@Data
public class SaleSummaryRespVo {

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户
     */
    private String customerId;

    /**
     * 合同额
     */
    private Double contractAmount;

    /**
     * 发货额
     */
    private Double deliveryAmount;

    /**
     * 出货应收
     */
    private Double deliveryNeedCollectAmount;

    /**
     * 开票应收
     */
    private Double invoiceNeedCollectAmount;
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


}
