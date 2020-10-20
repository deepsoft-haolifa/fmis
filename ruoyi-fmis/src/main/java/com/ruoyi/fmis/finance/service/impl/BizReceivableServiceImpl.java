package com.ruoyi.fmis.finance.service.impl;

import com.ruoyi.fmis.finance.domain.vo.ReceivableReqVo;
import com.ruoyi.fmis.finance.domain.vo.ReceivableRespVo;
import com.ruoyi.fmis.finance.mapper.BizFinanceMapper;
import com.ruoyi.fmis.finance.service.IBizReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author murphy.he
 **/
@Service
public class BizReceivableServiceImpl implements IBizReceivableService {
    @Autowired
    private BizFinanceMapper bizFinanceMapper;

    @Override
    public List<ReceivableRespVo> selectReceivableList(ReceivableReqVo reqVo) {
        return bizFinanceMapper.selectReceivableList(reqVo);
    }
}
