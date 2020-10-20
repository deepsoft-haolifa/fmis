package com.ruoyi.fmis.finance.mapper;


import com.ruoyi.fmis.finance.domain.BizBill;

import java.util.List;

/**
 * 现金日记账Mapper接口
 *
 * @author frank
 * @date 2020-06-25
 */
public interface BizBillMapper {
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
     * 删除现金日记账
     *
     * @param billId 现金日记账ID
     * @return 结果
     */
    public int deleteBizBillById(Long billId);

    /**
     * 批量删除现金日记账
     *
     * @param billIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizBillByIds(String[] billIds);
}
