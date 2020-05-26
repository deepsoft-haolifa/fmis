package com.ruoyi.fmis.chistory.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.chistory.mapper.BizCustomerHistoryMapper;
import com.ruoyi.fmis.chistory.domain.BizCustomerHistory;
import com.ruoyi.fmis.chistory.service.IBizCustomerHistoryService;
import com.ruoyi.common.core.text.Convert;

/**
 * 客户分配记录Service业务层处理
 *
 * @author frank
 * @date 2020-05-26
 */
@Service
public class BizCustomerHistoryServiceImpl implements IBizCustomerHistoryService {
    @Autowired
    private BizCustomerHistoryMapper bizCustomerHistoryMapper;

    /**
     * 查询客户分配记录
     *
     * @param historyId 客户分配记录ID
     * @return 客户分配记录
     */
    @Override
    public BizCustomerHistory selectBizCustomerHistoryById(Long historyId) {
        return bizCustomerHistoryMapper.selectBizCustomerHistoryById(historyId);
    }

    /**
     * 查询客户分配记录列表
     *
     * @param bizCustomerHistory 客户分配记录
     * @return 客户分配记录
     */
    @Override
    public List<BizCustomerHistory> selectBizCustomerHistoryList(BizCustomerHistory bizCustomerHistory) {
        return bizCustomerHistoryMapper.selectBizCustomerHistoryList(bizCustomerHistory);
    }

    /**
     * 新增客户分配记录
     *
     * @param bizCustomerHistory 客户分配记录
     * @return 结果
     */
    @Override
    public int insertBizCustomerHistory(BizCustomerHistory bizCustomerHistory) {
        bizCustomerHistory.setCreateTime(DateUtils.getNowDate());
        return bizCustomerHistoryMapper.insertBizCustomerHistory(bizCustomerHistory);
    }

    /**
     * 修改客户分配记录
     *
     * @param bizCustomerHistory 客户分配记录
     * @return 结果
     */
    @Override
    public int updateBizCustomerHistory(BizCustomerHistory bizCustomerHistory) {
        bizCustomerHistory.setUpdateTime(DateUtils.getNowDate());
        return bizCustomerHistoryMapper.updateBizCustomerHistory(bizCustomerHistory);
    }

    /**
     * 删除客户分配记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizCustomerHistoryByIds(String ids) {
        return bizCustomerHistoryMapper.deleteBizCustomerHistoryByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户分配记录信息
     *
     * @param historyId 客户分配记录ID
     * @return 结果
     */
    @Override
    public int deleteBizCustomerHistoryById(Long historyId) {
        return bizCustomerHistoryMapper.deleteBizCustomerHistoryById(historyId);
    }
}
