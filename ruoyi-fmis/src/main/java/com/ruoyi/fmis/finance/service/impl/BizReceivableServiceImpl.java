package com.ruoyi.fmis.finance.service.impl;

import com.ruoyi.fmis.finance.domain.vo.*;
import com.ruoyi.fmis.finance.mapper.BizFinanceMapper;
import com.ruoyi.fmis.finance.service.IBizBillContractService;
import com.ruoyi.fmis.finance.service.IBizFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author murphy.he
 **/
@Service
public class BizReceivableServiceImpl implements IBizFinanceService {
    @Autowired
    private BizFinanceMapper bizFinanceMapper;
    @Autowired
    private IBizBillContractService bizBillContractService;

    @Override
//    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<ReceivableRespVo> selectReceivableList(ReceivableReqVo reqVo) {
        return bizFinanceMapper.selectReceivableList(reqVo);
    }

    @Override
    public List<ReceivableSummaryRespVo> selectReceivableSummaryList(ReceivableReqVo reqVo) {
        return null;
    }

    @Override
//    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<StandAccountRespVo> selectStandAccountList(StandAccountReqVo reqVo) {
        return bizFinanceMapper.selectStandAccountList(reqVo);
    }

    @Override
    public List<StandAccountSummaryRespVo> selectStandAccountSummaryList(StandAccountReqVo reqVo) {
        return bizFinanceMapper.selectStandAccountSummaryList(reqVo);
    }

    @Override
    public List<ProcurementSummaryRespVo> selectProcurementSummary(SummaryReqVo reqVo) {
        return bizFinanceMapper.selectProcurementSummary(reqVo);
    }

    @Override
    public List<SaleSummaryRespVo> selectSaleContractSummary(SummaryReqVo reqVo) {
        return bizFinanceMapper.selectSaleContractSummary(reqVo);
    }
}
