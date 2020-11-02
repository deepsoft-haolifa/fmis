package com.ruoyi.fmis.finance.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.finance.mapper.BizBankBillMapper;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.common.core.text.Convert;

/**
 * 银行日记账Service业务层处理
 *
 * @author murphy.he
 * @date 2020-11-02
 */
@Service
public class BizBankBillServiceImpl implements IBizBankBillService {
    @Autowired
    private BizBankBillMapper bizBankBillMapper;

    /**
     * 查询银行日记账
     *
     * @param billId 银行日记账ID
     * @return 银行日记账
     */
    @Override
    public BizBankBill selectBizBankBillById(Long billId) {
        return bizBankBillMapper.selectBizBankBillById(billId);
    }

    /**
     * 查询银行日记账列表
     *
     * @param bizBankBill 银行日记账
     * @return 银行日记账
     */
    @Override
    public List<BizBankBill> selectBizBankBillList(BizBankBill bizBankBill) {
        return bizBankBillMapper.selectBizBankBillList(bizBankBill);
    }

    /**
     * 新增银行日记账
     *
     * @param bizBankBill 银行日记账
     * @return 结果
     */
    @Override
    public int insertBizBankBill(BizBankBill bizBankBill) {
        bizBankBill.setCreateTime(DateUtils.getNowDate());
        return bizBankBillMapper.insertBizBankBill(bizBankBill);
    }

    /**
     * 修改银行日记账
     *
     * @param bizBankBill 银行日记账
     * @return 结果
     */
    @Override
    public int updateBizBankBill(BizBankBill bizBankBill) {
        bizBankBill.setUpdateTime(DateUtils.getNowDate());
        return bizBankBillMapper.updateBizBankBill(bizBankBill);
    }

    /**
     * 删除银行日记账对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizBankBillByIds(String ids) {
        return bizBankBillMapper.deleteBizBankBillByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除银行日记账信息
     *
     * @param billId 银行日记账ID
     * @return 结果
     */
    @Override
    public int deleteBizBankBillById(Long billId) {
        return bizBankBillMapper.deleteBizBankBillById(billId);
    }
}
