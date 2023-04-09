package com.ruoyi.fmis.finance.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.domain.BizBillContract;
import com.ruoyi.fmis.finance.mapper.BizBankBillMapper;
import com.ruoyi.fmis.finance.mapper.BizBillContractMapper;
import com.ruoyi.fmis.finance.mapper.extend.BizBillContractExtendMapper;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.fmis.finance.service.IBizBillContractService;
import com.ruoyi.framework.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 合同收款(合同分解)Service业务层处理
 *
 * @author hedong
 * @date 2020-10-22
 */
@Service
public class BizBillContractServiceImpl implements IBizBillContractService {
    @Autowired
    private BizBillContractMapper bizBillContractMapper;
    @Autowired
    private BizBankBillMapper bizBankBillMapper;
    @Autowired
    private IBizBillContractService bizBillContractService;
    @Autowired
    private IBizBankBillService bizBankBillService;

    @Autowired
    private BizBillContractExtendMapper bizBillContractExtendMapper;


    /**
     * 查询合同收款(合同分解)
     *
     * @param bcId 合同收款(合同分解)ID
     * @return 合同收款(合同分解)
     */
    @Override
    public BizBillContract selectBizBillContractById(Long bcId) {
        return bizBillContractMapper.selectBizBillContractById(bcId);
    }

    @Override
    public List<BizBillContract> selectBizBillContractByDataIds(Set<Long> dataIds) {
        return bizBillContractMapper.selectBizBillContractByDataIds(dataIds);
    }

    /**
     * 查询合同收款(合同分解)列表
     *
     * @param bizBillContract 合同收款(合同分解)
     * @return 合同收款(合同分解)
     */
    @Override
    public List<BizBillContract> selectBizBillContractList(BizBillContract bizBillContract) {
        return bizBillContractMapper.selectBizBillContractList(bizBillContract);
    }

    /**
     * 新增合同收款(合同分解)
     *
     * @param bizBillContract 合同收款(合同分解)
     * @return 结果
     */
    @Override
    public int insertBizBillContract(BizBillContract bizBillContract) {
        bizBillContract.setCreateTime(DateUtils.getNowDate());
        bizBillContract.setCreateBy(ShiroUtils.getUserId().toString());
        return bizBillContractMapper.insertBizBillContract(bizBillContract);
    }

    /**
     * 修改合同收款(合同分解)
     *
     * @param bizBillContract 合同收款(合同分解)
     * @return 结果
     */
    @Override
    public int updateBizBillContract(BizBillContract bizBillContract) {
        bizBillContract.setUpdateTime(DateUtils.getNowDate());
        bizBillContract.setUpdateBy(ShiroUtils.getUserId().toString());
        bizBillContractMapper.updateBizBillContract(bizBillContract);
        BizBillContract bizBillContractObj = bizBillContractMapper.selectBizBillContractById(bizBillContract.getBcId());

        BizBankBill bizBankBill = new BizBankBill();
        //二.判断银行日记账的金额是否够分
        // 1. 查询银行日记账的总价
        BizBankBill bill = bizBankBillService.selectBizBankBillById(bizBillContractObj.getBillId());
        Double collectionMoney = bill.getCollectionMoney();
        // 2. 查询该合同已经分解的金额
        BizBillContract query1 = new BizBillContract();
        query1.setBillId(bizBillContractObj.getBillId());
        List<BizBillContract> bizBillContracts = bizBillContractService.selectBizBillContractList(query1);
        double alreadyAmount = bizBillContracts.stream().mapToDouble(BizBillContract::getAmount).sum();

        if (collectionMoney == alreadyAmount) {
            bizBankBill.setContractStatus("2");
            bizBankBill.setBillId(bizBillContractObj.getBillId());
            bizBankBillMapper.updateBizBankBill(bizBankBill);
        }
        return 1;
    }

    /**
     * 删除合同收款(合同分解)对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizBillContractByIds(String ids) {
        return bizBillContractMapper.deleteBizBillContractByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除合同收款(合同分解)信息
     *
     * @param bcId 合同收款(合同分解)ID
     * @return 结果
     */
    @Override
    public int deleteBizBillContractById(Long bcId) {
        return bizBillContractMapper.deleteBizBillContractById(bcId);
    }
}
