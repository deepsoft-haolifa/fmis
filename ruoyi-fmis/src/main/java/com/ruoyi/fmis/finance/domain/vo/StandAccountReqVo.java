package com.ruoyi.fmis.finance.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @author murphy.he
 **/
public class StandAccountReqVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 合同编号(采购合同)
     */
    private String purchaseContractNo;

    public String getPurchaseContractNo() {
        return purchaseContractNo;
    }

    public void setPurchaseContractNo(String purchaseContractNo) {
        this.purchaseContractNo = purchaseContractNo;
    }
}
