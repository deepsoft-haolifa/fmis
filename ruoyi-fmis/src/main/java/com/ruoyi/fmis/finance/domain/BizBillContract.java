package com.ruoyi.fmis.finance.domain;

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

    public void setBcId(Long bcId) {
        this.bcId = bcId;
    }

    public Long getBcId() {
        return bcId;
    }
    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getBillId() {
        return billId;
    }
    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Long getDataId() {
        return dataId;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }
    public void setBookKeeper(String bookKeeper) {
        this.bookKeeper = bookKeeper;
    }

    public String getBookKeeper() {
        return bookKeeper;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("bcId", getBcId())
            .append("billId", getBillId())
            .append("dataId", getDataId())
            .append("amount", getAmount())
            .append("bookKeeper", getBookKeeper())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
