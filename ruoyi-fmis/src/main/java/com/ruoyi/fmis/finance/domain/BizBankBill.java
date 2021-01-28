package com.ruoyi.fmis.finance.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 银行日记账对象
 * biz_bank_bill
 *
 */
@Data
public class BizBankBill extends BaseEntity {
    private static final long serialVersionUID = 1L;


    private String startOperateDate;

    private String endOperateDate;

    /** ID */
    private Long billId;

    /** 公司（收款是收款公司，付款是付款公司） */
    @Excel(name = "公司", readConverterExp = "收款是收款公司，付款是付款公司")
    private String company;

    /** 账户（公司下面的某个银行)用来计算余额 */
    @Excel(name = "账户", readConverterExp = "账户（公司下面的某个银行)用来计算余额")
    private String account;

    /** 序号 */
    @Excel(name = "序号")
    private String serialNo;

    /** 日期 */
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operateDate;

    /** 凭证号 */
    @Excel(name = "凭证号")
    private String certificateNumber;

    /** 收付款方式 */
    @Excel(name = "收付款方式")
    private String payWay;

    /** 收付款账户 */
    @Excel(name = "收付款账户")
    private String payAccount;

    /** 付款单位 */
    @Excel(name = "付款单位")
    private String payCompany;

    /** 付款单位 */
    @Excel(name = "收款单位")
    private String collectCompany;

    /** 上月结转 */
    @Excel(name = "上月结转")
    private Double preMonthMoney;

    /** 收款 */
    @Excel(name = "收款")
    private Double collectionMoney;

    /** 收款类别 */
    @Excel(name = "收款类别")
    private String collectionType;

    /** 付款 */
    @Excel(name = "付款")
    private Double payment;

    /** 付款类别 */
    @Excel(name = "付款类别")
    private String paymentType;

    /** 余额 */
    @Excel(name = "余额")
    private Double balance;

    /** 类型 1.收款；2.付款 */
    @Excel(name = "类型 1.收款；2.付款")
    private String type;

    /** 部门ID */
    @Excel(name = "部门ID")
    private String deptId;

    /** 收款合同分解状态；0未完成；1.完成 */
    private String contractStatus;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;


}
