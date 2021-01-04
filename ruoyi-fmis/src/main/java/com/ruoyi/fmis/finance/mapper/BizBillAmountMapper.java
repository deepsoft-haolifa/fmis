package com.ruoyi.fmis.finance.mapper;

import com.ruoyi.fmis.finance.domain.BizBillAmount;
import java.util.List;

/**
 * 日记账金额Mapper接口
 *
 * @author Xianlu Tech
 * @date 2021-01-04
 */
public interface BizBillAmountMapper {
    /**
     * 查询日记账金额
     *
     * @param id 日记账金额ID
     * @return 日记账金额
     */
    public BizBillAmount selectBizBillAmountById(Long id);

    /**
     * 查询日记账金额列表
     *
     * @param bizBillAmount 日记账金额
     * @return 日记账金额集合
     */
    public List<BizBillAmount> selectBizBillAmountList(BizBillAmount bizBillAmount);

    /**
     * 新增日记账金额
     *
     * @param bizBillAmount 日记账金额
     * @return 结果
     */
    public int insertBizBillAmount(BizBillAmount bizBillAmount);

    /**
     * 修改日记账金额
     *
     * @param bizBillAmount 日记账金额
     * @return 结果
     */
    public int updateBizBillAmount(BizBillAmount bizBillAmount);

    /**
     * 删除日记账金额
     *
     * @param id 日记账金额ID
     * @return 结果
     */
    public int deleteBizBillAmountById(Long id);

    /**
     * 批量删除日记账金额
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizBillAmountByIds(String[] ids);
}
