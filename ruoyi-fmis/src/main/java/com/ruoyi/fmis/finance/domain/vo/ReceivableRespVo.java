package com.ruoyi.fmis.finance.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 应收款项的返回实体
 * @author murphy.he
 **/
@Data
public class ReceivableRespVo {

    /**
     * 合同Id(销售合同)
     */
    private Long saleContractId;
    /**
     * 合同编号(销售合同)
     */
    private String saleContractNo;
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 供方单位
     */
    private String supplierName;

    /**
     * 签订日期
     */
    @Excel(name = "签订日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    /**
     * 归属部门
     */
    private String deptName;

    /**
     * 发票类别
     */
    private String invoiceType;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String deliveryTime;

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
     * 款项类别
     */
    private String payMethod;

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
     * 已发货金额
     */
    private Double deliveryAmount;

    /**
     * 合同状态
     */
    private String saleContractStatus;
}
