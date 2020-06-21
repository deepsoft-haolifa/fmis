package com.ruoyi.fmis.status.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.status.mapper.BizDataStatusMapper;
import com.ruoyi.fmis.status.domain.BizDataStatus;
import com.ruoyi.fmis.status.service.IBizDataStatusService;
import com.ruoyi.common.core.text.Convert;

/**
 * 数据状态Service业务层处理
 *
 * @author frank
 * @date 2020-06-16
 */
@Service
public class BizDataStatusServiceImpl implements IBizDataStatusService {
    @Autowired
    private BizDataStatusMapper bizDataStatusMapper;

    /**
     * 查询数据状态
     *
     * @param statusId 数据状态ID
     * @return 数据状态
     */
    @Override
    public BizDataStatus selectBizDataStatusById(Long statusId) {
        return bizDataStatusMapper.selectBizDataStatusById(statusId);
    }

    /**
     * 查询数据状态列表
     *
     * @param bizDataStatus 数据状态
     * @return 数据状态
     */
    @Override
    public List<BizDataStatus> selectBizDataStatusList(BizDataStatus bizDataStatus) {
        return bizDataStatusMapper.selectBizDataStatusList(bizDataStatus);
    }

    /**
     * 新增数据状态
     *
     * @param bizDataStatus 数据状态
     * @return 结果
     */
    @Override
    public int insertBizDataStatus(BizDataStatus bizDataStatus) {
        bizDataStatus.setCreateTime(DateUtils.getNowDate());
        bizDataStatus.setCreateBy(ShiroUtils.getUserId() + "");
        return bizDataStatusMapper.insertBizDataStatus(bizDataStatus);
    }

    /**
     * 修改数据状态
     *
     * @param bizDataStatus 数据状态
     * @return 结果
     */
    @Override
    public int updateBizDataStatus(BizDataStatus bizDataStatus) {
        bizDataStatus.setUpdateTime(DateUtils.getNowDate());
        return bizDataStatusMapper.updateBizDataStatus(bizDataStatus);
    }

    /**
     * 删除数据状态对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizDataStatusByIds(String ids) {
        return bizDataStatusMapper.deleteBizDataStatusByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除数据状态信息
     *
     * @param statusId 数据状态ID
     * @return 结果
     */
    @Override
    public int deleteBizDataStatusById(Long statusId) {
        return bizDataStatusMapper.deleteBizDataStatusById(statusId);
    }
}
