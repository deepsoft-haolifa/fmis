package com.ruoyi.fmis.finance.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 日记账金额对象 biz_bill_amount
 *
 * @author Xianlu Tech
 * @date 2021-01-04
 */
public class BizBillAmount extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 年 */
    @Excel(name = "年")
    private String year;

    /** 月 */
    @Excel(name = "月")
    private String month;

    /** 1.银行日记账；2.现金日记账 */
    @Excel(name = "1.银行日记账；2.现金日记账")
    private Integer type;

    /** 公司 */
    @Excel(name = "公司")
    private String company;

    /** 账户 */
    @Excel(name = "账户")
    private String account;

    /** 上个月期初金额（上一条记录的期初金额） */
    @Excel(name = "上个月期初金额", readConverterExp = "上=一条记录的期初金额")
    private Double preAmount;

    /** 本月期初金额（记账中最新一条记录的余额） */
    @Excel(name = "本月期初金额", readConverterExp = "记=账中最新一条记录的余额")
    private Double nextAmount;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }
    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }
    public void setPreAmount(Double preAmount) {
        this.preAmount = preAmount;
    }

    public Double getPreAmount() {
        return preAmount;
    }
    public void setNextAmount(Double nextAmount) {
        this.nextAmount = nextAmount;
    }

    public Double getNextAmount() {
        return nextAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("year", getYear())
                .append("month", getMonth())
                .append("type", getType())
                .append("company", getCompany())
                .append("account", getAccount())
                .append("preAmount", getPreAmount())
                .append("nextAmount", getNextAmount())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}