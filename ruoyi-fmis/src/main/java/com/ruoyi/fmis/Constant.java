package com.ruoyi.fmis;

/**
 * 常量定义
 *
 * @author murphy.he
 **/
public interface Constant {

    /**
     * 采购状态
     */
    interface procurementStatus {
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
        // 已开票
        String INVOICE = "7";
        // 已收款
        String COLLECTED = "8";
    }
}
