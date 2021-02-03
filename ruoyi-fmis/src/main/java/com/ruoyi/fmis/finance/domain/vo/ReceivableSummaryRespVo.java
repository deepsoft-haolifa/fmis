package com.ruoyi.fmis.finance.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 应收款项汇总的返回实体
 * @author murphy.he
 **/
@Data
public class ReceivableSummaryRespVo {

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 付款方式
     */
    private String payType;

    /**
     * 销售总量
     */
    private Integer saleQty;
    /**
     * 合同总额
     */
    private Double saleContractAmount;

    /**
     * 已付款额
     */
    private Double paidAmount;

    /**
     * 已开发票
     */
    private Double invoicedAmount;

    /**
     * 已发货数
     */
    private Integer deliveryQty;

    /**
     * 合同状态
     */
    private String saleContractStatus;
}
