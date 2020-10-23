package com.ruoyi.fmis.finance.service;

import com.ruoyi.fmis.finance.domain.vo.ReceivableReqVo;
import com.ruoyi.fmis.finance.domain.vo.ReceivableRespVo;
import com.ruoyi.fmis.finance.domain.vo.StandAccountReqVo;
import com.ruoyi.fmis.finance.domain.vo.StandAccountRespVo;

import java.util.List;

/**
 * 应收管理
 *
 */
public interface IBizFinanceService {


    /**
     * 应收款项列表（销售合同）
     */
    List<ReceivableRespVo> selectReceivableList(ReceivableReqVo reqVo);


    /**
     * 财务挂账列表（采购合同）
     */
    List<StandAccountRespVo> selectStandAccountList(StandAccountReqVo reqVo);


}
