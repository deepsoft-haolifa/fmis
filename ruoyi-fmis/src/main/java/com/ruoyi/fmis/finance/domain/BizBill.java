package com.ruoyi.fmis.finance.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.SysDept;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 现金日记账对象 biz_bill
 *
 * @author frank
 * @date 2020-06-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizBill extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long billId;

    @Getter
    @Setter
    private String createByName;

    @Getter
    @Setter
    private Double billAmount;

    @Getter
    @Setter
    private SysDept dept;
    /** 类型 1.收款；2.付款 */
    private String type;

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
    private String payWay;

    /** 收付款账户 */
    private String payAccount;

    /** 收付款单位 */
    private String payCompany;

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

    /** 部门ID */
    @Excel(name = "部门ID")
    private String deptId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

}
