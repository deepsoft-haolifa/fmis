package com.ruoyi.fmis.finance.domain.vo;

import lombok.Data;

/**
 * 回款分解reqVo
 *
 * @author hedong
 * @date 2020-10-22
 */
@Data
public class BizBillContractReqVo{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long bcId;

    /** 收款id */
    private Long billId;

    /** 合同id */
    private Long dataId;

    /** 收款金额 */
    private Double amount;

    /** 备注 */
    private String remark;

}
