package com.ruoyi.fmis.finance.mapper.extend;

import com.ruoyi.fmis.finance.domain.BizBillContract;
import com.ruoyi.fmis.finance.domain.vo.BizBillContractReqVo;
import com.ruoyi.fmis.finance.domain.vo.BizBillContractRespVo;

import java.util.List;

/**
 * 合同收款(合同分解)Mapper接口(额外)
 *
 * @author hedong
 * @date 2020-10-22
 */
public interface BizBillContractExtendMapper {
    /**
     * 查询合同收款(合同分解)
     *
     * @param bcId 合同收款(合同分解)ID
     * @return 合同收款(合同分解)
     */
    public BizBillContract selectBizBillContractById(Long bcId);

    /**
     * 查询合同收款(合同分解)列表
     *
     * @param bizBillContractReqVo 合同收款(合同分解)
     * @return 合同收款(合同分解)集合
     */
    public List<BizBillContractRespVo> selectBizBillContractListVo(BizBillContractReqVo bizBillContractReqVo);


}
