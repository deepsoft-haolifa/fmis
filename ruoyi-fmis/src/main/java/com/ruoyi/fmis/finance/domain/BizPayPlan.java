package com.ruoyi.fmis.finance.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 付款计划（基于付款申请记录）对象 biz_pay_plan
 *
 * @author murphy
 * @date 2020-11-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizPayPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long payPlanId;

    /** 付款申请Id（biz_process_data 里面的data_id） */
    private Long payDataId;

    /** 申请编号 */
    @Excel(name = "申请编号")
    private String applyNo;

    /** 申请日期 */
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyDate;

    /** 采购合同ID */
    private String contractId;

    /** 采购合同号 */
    @Excel(name = "合同号")
    private String contractNo;

    /** 采购合同付款方式 */
    @Excel(name = "合同付款方式")
    private String contractPayWay;

    /** 申请付款单位 */
    @Excel(name = "付款单位")
    private String applyPayCompany;

    /** 收款单位 */
    @Excel(name = "收款单位")
    private String applyCollectionCompany;

    /** 付款金额 */
    @Excel(name = "付款金额")
    private Double applyAmount;

    /** 付款备注 */
    @Excel(name = "付款备注")
    private String applyRemark;

    /** 付款单位 */
    private String payCompany;

    /** 付款账号 */
    private String payAccount;

    /** 付款方式; 字典：pay_way */
    @Excel(name = "付款方式")
    private String payWay;

    /** 付款类型: 默认是货款 */
    private String paymentType;

    /** 付款状态：0.未付；1.已付； 字典：pay_status */
    @Excel(name = "付款状态",readConverterExp = "0=未付款,1=已付款")
    private String status;

    /** 付款日期 */
    @Excel(name = "付款日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payDate;

    /** 记账方式 */
    @Excel(name = "记账方式",readConverterExp = "1=现金日记账,2=银行日记账")
    private String bookingType;

    /** 数据状态：1. 老总选择； 2. 财务主管选择； 3.出纳付款 */
    private String dataStatus;

    /** 数据状态LIST：1. 老总选择； 2. 财务主管选择； 3.出纳付款 */
    private List<String> dataStatusList;

    private String startTime;
    private String endTime;

    // 承兑金额
    private double acceptAmount;
    // 支票金额
    private double chequeAmount;
    // 电汇金额
    private double wireAmount;
}