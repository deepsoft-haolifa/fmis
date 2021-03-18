package com.ruoyi.fmis.finance.mapper;

import com.ruoyi.fmis.finance.domain.vo.*;

import java.util.List;

/**
 * 应收款项Mapper接口
 *
 */
public interface BizFinanceMapper {
    /**
     * 查询应收款项列表
     *
     */
    List<ReceivableRespVo> selectReceivableList(ReceivableReqVo reqVo);


    List<StandAccountRespVo> selectStandAccountList(StandAccountReqVo reqVo);


    List<StandAccountSummaryRespVo> selectStandAccountSummaryList(StandAccountReqVo reqVo);

    List<SummaryRespVo> selectProcurementSummary(SummaryReqVo reqVo);

    List<SummaryRespVo> selectSaleContractSummary(SummaryReqVo reqVo);
}
