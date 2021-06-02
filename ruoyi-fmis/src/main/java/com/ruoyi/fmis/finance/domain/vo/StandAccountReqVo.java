package com.ruoyi.fmis.finance.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author murphy.he
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class StandAccountReqVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 合同编号(采购合同)
     */
    private String purchaseContractNo;
    /**
     * 结算单位
     */
    private String chargeCompany;

    /**
     * 供应商
     */
    private String supplierName;
    /**
     * 需求方
     */
    private String needCompany;

    /**
     * 归属单位
     */
    private String deptName;

}
