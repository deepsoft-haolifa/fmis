package com.ruoyi.fmis.finance.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 财务挂账的返回实体
 * @author murphy.he
 **/
@Data
public class StandAccountRespVo {

    /**
     * 合同Id(采购合同)
     */
    private Long purchaseContractId;
    /**
     * 合同编号(采购合同)
     */
    private String purchaseContractNo;
    /**
     * 合同状态
     */
    private String purchaseStatus;
    /**
     * 结算单位
     */
    private String chargeCompany;

    /**
     * 供应商
     */
    private String supplierName;

    /**
     * 归属单位
     */
    private String deptName;

    /**
     * 交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String deliveryDate;

    /**
     * 发货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String outDate;

    /**
     * 到货数量
     */
    private Integer arrivalQty;

    /**
     * 到货货款
     */
    private Double arrivalAmount;

    /**
     * 已付货款
     */
    private Double paidAmount;

    /**
     * 付款方式(字典：contract_paytype)
     */
    private String payWay;
    /**
     * 合同金额
     */
    private Double contractAmount;

}
