package com.ruoyi.fmis.finance.service;

import com.ruoyi.fmis.finance.domain.BizBill;
import java.util.List;

/**
 * 现金日记账Service接口
 *
 * @author murphy.he
 * @date 2020-11-02
 */
public interface IBizBillService {
    /**
     * 查询现金日记账
     *
     * @param billId 现金日记账ID
     * @return 现金日记账
     */
    public BizBill selectBizBillById(Long billId);

    /**
     * 查询现金日记账列表
     *
     * @param bizBill 现金日记账
     * @return 现金日记账集合
     */
    public List<BizBill> selectBizBillList(BizBill bizBill);

    /**
     * 新增现金日记账
     *
     * @param bizBill 现金日记账
     * @return 结果
     */
    public int insertBizBill(BizBill bizBill);

    /**
     * 修改现金日记账
     *
     * @param bizBill 现金日记账
     * @return 结果
     */
    public int updateBizBill(BizBill bizBill);

    /**
     * 批量删除现金日记账
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizBillByIds(String ids);

    /**
     * 删除现金日记账信息
     *
     * @param billId 现金日记账ID
     * @return 结果
     */
    public int deleteBizBillById(Long billId);
}
