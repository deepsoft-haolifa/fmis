package com.ruoyi.fmis.finance.service;

import com.ruoyi.fmis.finance.domain.vo.ReceivableReqVo;
import com.ruoyi.fmis.finance.domain.vo.ReceivableRespVo;

import java.util.List;

/**
 * 应收管理
 *
 */
public interface IBizReceivableService {


    /**
     * 应收款项列表
     */
    List<ReceivableRespVo> selectReceivableList(ReceivableReqVo reqVo);

}
