package com.ruoyi.fmis.finance.service;

import com.ruoyi.fmis.finance.domain.vo.*;

import java.util.List;

/**
 * 应收管理
 */
public interface IBizFinanceService {


    /**
     * 应收款项列表（销售合同）
     */
    List<ReceivableRespVo> selectReceivableList(ReceivableReqVo reqVo);


    /**
     * 应收款项汇总根据客户汇总（销售合同）
     */
    List<ReceivableSummaryRespVo> selectReceivableSummaryList(ReceivableReqVo reqVo);

    /**
     * 财务挂账列表（采购合同）
     */
    List<StandAccountRespVo> selectStandAccountList(StandAccountReqVo reqVo);

    /**
     * 应付款项汇总 根据收款单位（付款计划）
     */
    List<StandAccountSummaryRespVo> selectStandAccountSummaryList(StandAccountReqVo reqVo);

    /**
     * 采购合同汇总（按照供应商）
     */
    List<ProcurementSummaryRespVo> selectProcurementSummary(SummaryReqVo reqVo);

    /**
     * 销售合同汇总（按照客户）
     */
    List<SaleSummaryRespVo> selectSaleContractSummary(SummaryReqVo reqVo);


}
