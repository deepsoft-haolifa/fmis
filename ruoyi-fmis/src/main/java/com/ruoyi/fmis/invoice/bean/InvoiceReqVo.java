package com.ruoyi.fmis.invoice.bean;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发票申请请求实体
 *
 * @author murphy.he
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceReqVo extends BaseEntity {
    private static final long serialVersionUID = 1L;


    private String pString1;
    private String pString2;
    private String pString3;
    private String pString5;
    private String pString6;
    /**
     * 开票状态
     */
    private String string7;

    private String string2;
    private String createByName;



}
