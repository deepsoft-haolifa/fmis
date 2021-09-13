package com.ruoyi.fmis.finance.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author murphy.he
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ReceivableReqVo extends BaseEntity {

    /**
     * 合同编号(销售合同)
     */
    private String saleContractNo;

    /**
     * 客户编号
     */
    private String customerId;
    private String customerName;
    private String signTime;
    private String deptName;
    private String deliveryTime;
    private String supplierName;
    private String string30;

}
