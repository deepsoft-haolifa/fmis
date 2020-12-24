package com.ruoyi.fmis.finance.service.impl;

import com.ruoyi.fmis.finance.domain.BizBillContract;
import com.ruoyi.fmis.finance.domain.vo.ReceivableReqVo;
import com.ruoyi.fmis.finance.domain.vo.ReceivableRespVo;
import com.ruoyi.fmis.finance.domain.vo.StandAccountReqVo;
import com.ruoyi.fmis.finance.domain.vo.StandAccountRespVo;
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
        // 设置已付总额
        Set<Long> dataIdSet = receivableRespVos.stream().map(ReceivableRespVo::getSaleContractId).collect(Collectors.toSet());
        List<BizBillContract> bizBillContracts = bizBillContractService.selectBizBillContractByDataIds(dataIdSet);

        Map<Long, Double> doubleMap = bizBillContracts.stream().collect(Collectors.groupingBy(BizBillContract::getDataId, Collectors.summingDouble(BizBillContract::getAmount)));

        receivableRespVos.forEach(e -> {
            e.setPaidAmount(doubleMap.get(e.getSaleContractId()));
        });
        return receivableRespVos;
    }

    @Override
    public List<StandAccountRespVo> selectStandAccountList(StandAccountReqVo reqVo) {
        return bizFinanceMapper.selectStandAccountList(reqVo);
    }
}
