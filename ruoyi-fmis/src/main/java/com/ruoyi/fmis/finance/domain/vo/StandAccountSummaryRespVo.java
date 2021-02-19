package com.ruoyi.fmis.finance.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 财务挂账的返回实体
 * @author murphy.he
 **/
@Data
public class StandAccountSummaryRespVo {

    /**
     * 需求方
     */
    private String needCompany;

    /**
     * 供应商
     */
    private String supplierName;

    /**
     * 累计欠款额
     */
    private Double totalArrearsAmount;

    /**
     * 累计订货额
     */
    private Double totalContractAmount;

    /**
     * 累计入账额
     */
    private Double totalAccountAmount;

    /**
     * 已付货款
     */
    private Double paidAmount;

    /**
     * 计划付款额
     */
    private Double planPayAmount;

}
