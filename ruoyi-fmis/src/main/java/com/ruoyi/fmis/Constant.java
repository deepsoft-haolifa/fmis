package com.ruoyi.fmis;

/**
 * 常量定义
 *
 * @author murphy.he
 **/
public interface Constant {

    interface processDataBizId{
        String PROCUREMENT="procurement";
        String CONTRACT="contract";
    }

    /**
     * 采购状态
     */
    interface procurementStatus {
        // 未采购
        String NEW = "1";
        // 审批中
        String AUDIT = "2";
        // 采购中
        String ING = "3";
        // 采购完成
        String DONE = "4";
        // 已回票
        String INVOICE = "5";
        // 已付款
        String PAYED = "6";
    }

    /**
     * 销售合同状态
     */
    interface contractStatus {
        // 新建
        String NEW = "0";
        // 审批中
        String AUDIT = "1";
        // 待采购
        String TO_BE_PURCHASE = "2";
        // 采购中
        String PURCHASE_ING = "3";
        //已入库
        String STORED = "4";
        // 部分发货
        String PART_DELIVERY = "5";
        // 全部发货
        String ALL_DELIVERY = "6";
    }

    /**
     * 销售合同开票状态
     */
    interface invoiceStatus{
        // 未开票
        String NOT = "1";
        // 已开票
        String ALREADY = "2";
    }
    /**
     * 销售合同回款状态
     */
    interface collectionStatus{
        // 未回款
        String NOT = "1";
        // 部分回款
        String PART = "2";
        // 已回款
        String ALREADY = "3";
    }
}
