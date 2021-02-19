package com.ruoyi.fmis.finance.service.impl;

import com.ruoyi.fmis.finance.domain.BizBillContract;
import com.ruoyi.fmis.finance.domain.vo.*;
import com.ruoyi.fmis.finance.mapper.BizFinanceMapper;
import com.ruoyi.fmis.finance.service.IBizBillContractService;
import com.ruoyi.fmis.finance.service.IBizFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<ReceivableRespVo> selectReceivableList(ReceivableReqVo reqVo) {
        List<ReceivableRespVo> receivableRespVos = bizFinanceMapper.selectReceivableList(reqVo);
        return receivableRespVos;
    }

    @Override
    public List<ReceivableSummaryRespVo> selectReceivableSummaryList(ReceivableReqVo reqVo) {
        return null;
    }

    @Override
    public List<StandAccountRespVo> selectStandAccountList(StandAccountReqVo reqVo) {
        return bizFinanceMapper.selectStandAccountList(reqVo);
    }

    @Override
    public List<StandAccountSummaryRespVo> selectStandAccountSummaryList(StandAccountReqVo reqVo) {
        return bizFinanceMapper.selectStandAccountSummaryList(reqVo);
    }
}
