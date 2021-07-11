package com.ruoyi.fmis.finance.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 合同收款(合同分解)对象 biz_bill_contract
 *
 * @author hedong
 * @date 2020-10-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizBillContract extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long bcId;

    /** 收款id */
    @Excel(name = "收款id")
    private Long billId;

    /** 合同id */
    @Excel(name = "合同id")
    private Long dataId;

    /** 收款金额 */
    @Excel(name = "收款金额")
    private Double amount;

    /** 记账员 */
    @Excel(name = "记账员")
    private String bookKeeper;

    /** 销售订单号 */
    private String saleContractNo;
    /** 创建者名称 */
    private String createByName;



}
