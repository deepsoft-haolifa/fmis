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
    /**
     * 结算单位
     */
    private String chargeCompany;

    /**
     * 供应商
     */
    private String supplierName;

    /**
     * 归属单位
     */
    private String deptName;

    public String getPurchaseContractNo() {
        return purchaseContractNo;
    }

    public void setPurchaseContractNo(String purchaseContractNo) {
        this.purchaseContractNo = purchaseContractNo;
    }

    public String getChargeCompany() {
        return chargeCompany;
    }

    public void setChargeCompany(String chargeCompany) {
        this.chargeCompany = chargeCompany;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String getDeptName() {
        return deptName;
    }

    @Override
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
