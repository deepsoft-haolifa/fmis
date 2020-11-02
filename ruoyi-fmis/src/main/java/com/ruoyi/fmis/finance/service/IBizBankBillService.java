package com.ruoyi.fmis.finance.service;

import com.ruoyi.fmis.finance.domain.BizBankBill;
import java.util.List;

/**
 * 银行日记账Service接口
 *
 * @author murphy.he
 * @date 2020-11-02
 */
public interface IBizBankBillService {
    /**
     * 查询银行日记账
     *
     * @param billId 银行日记账ID
     * @return 银行日记账
     */
    public BizBankBill selectBizBankBillById(Long billId);

    /**
     * 查询银行日记账列表
     *
     * @param bizBankBill 银行日记账
     * @return 银行日记账集合
     */
    public List<BizBankBill> selectBizBankBillList(BizBankBill bizBankBill);

    /**
     * 新增银行日记账
     *
     * @param bizBankBill 银行日记账
     * @return 结果
     */
    public int insertBizBankBill(BizBankBill bizBankBill);

    /**
     * 修改银行日记账
     *
     * @param bizBankBill 银行日记账
     * @return 结果
     */
    public int updateBizBankBill(BizBankBill bizBankBill);

    /**
     * 批量删除银行日记账
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizBankBillByIds(String ids);

    /**
     * 删除银行日记账信息
     *
     * @param billId 银行日记账ID
     * @return 结果
     */
    public int deleteBizBankBillById(Long billId);
}
