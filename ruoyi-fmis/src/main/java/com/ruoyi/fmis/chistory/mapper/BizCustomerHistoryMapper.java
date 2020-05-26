package com.ruoyi.fmis.chistory.mapper;

import com.ruoyi.fmis.chistory.domain.BizCustomerHistory;
import java.util.List;

/**
 * 客户分配记录Mapper接口
 *
 * @author frank
 * @date 2020-05-26
 */
public interface BizCustomerHistoryMapper {
    /**
     * 查询客户分配记录
     *
     * @param historyId 客户分配记录ID
     * @return 客户分配记录
     */
    public BizCustomerHistory selectBizCustomerHistoryById(Long historyId);

    /**
     * 查询客户分配记录列表
     *
     * @param bizCustomerHistory 客户分配记录
     * @return 客户分配记录集合
     */
    public List<BizCustomerHistory> selectBizCustomerHistoryList(BizCustomerHistory bizCustomerHistory);

    /**
     * 新增客户分配记录
     *
     * @param bizCustomerHistory 客户分配记录
     * @return 结果
     */
    public int insertBizCustomerHistory(BizCustomerHistory bizCustomerHistory);

    /**
     * 修改客户分配记录
     *
     * @param bizCustomerHistory 客户分配记录
     * @return 结果
     */
    public int updateBizCustomerHistory(BizCustomerHistory bizCustomerHistory);

    /**
     * 删除客户分配记录
     *
     * @param historyId 客户分配记录ID
     * @return 结果
     */
    public int deleteBizCustomerHistoryById(Long historyId);

    /**
     * 批量删除客户分配记录
     *
     * @param historyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizCustomerHistoryByIds(String[] historyIds);
}
