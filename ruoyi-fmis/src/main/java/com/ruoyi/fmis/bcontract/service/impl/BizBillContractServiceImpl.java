package com.ruoyi.fmis.bcontract.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.bcontract.mapper.BizBillContractMapper;
import com.ruoyi.fmis.bcontract.domain.BizBillContract;
import com.ruoyi.fmis.bcontract.service.IBizBillContractService;
import com.ruoyi.common.core.text.Convert;

/**
 * 合同收款Service业务层处理
 *
 * @author frank
 * @date 2020-06-26
 */
@Service
public class BizBillContractServiceImpl implements IBizBillContractService {
    @Autowired
    private BizBillContractMapper bizBillContractMapper;

    /**
     * 查询合同收款
     *
     * @param bcId 合同收款ID
     * @return 合同收款
     */
    @Override
    public BizBillContract selectBizBillContractById(Long bcId) {
        return bizBillContractMapper.selectBizBillContractById(bcId);
    }

    /**
     * 查询合同收款列表
     *
     * @param bizBillContract 合同收款
     * @return 合同收款
     */
    @Override
    public List<BizBillContract> selectBizBillContractList(BizBillContract bizBillContract) {
        return bizBillContractMapper.selectBizBillContractList(bizBillContract);
    }

    /**
     * 新增合同收款
     *
     * @param bizBillContract 合同收款
     * @return 结果
     */
    @Override
    public int insertBizBillContract(BizBillContract bizBillContract) {
        bizBillContract.setCreateTime(DateUtils.getNowDate());
        return bizBillContractMapper.insertBizBillContract(bizBillContract);
    }

    /**
     * 修改合同收款
     *
     * @param bizBillContract 合同收款
     * @return 结果
     */
    @Override
    public int updateBizBillContract(BizBillContract bizBillContract) {
        bizBillContract.setUpdateTime(DateUtils.getNowDate());
        return bizBillContractMapper.updateBizBillContract(bizBillContract);
    }

    /**
     * 删除合同收款对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizBillContractByIds(String ids) {
        return bizBillContractMapper.deleteBizBillContractByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除合同收款信息
     *
     * @param bcId 合同收款ID
     * @return 结果
     */
    @Override
    public int deleteBizBillContractById(Long bcId) {
        return bizBillContractMapper.deleteBizBillContractById(bcId);
    }
}
