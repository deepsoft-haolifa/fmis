package com.ruoyi.fmis.status.mapper;

import com.ruoyi.fmis.status.domain.BizDataStatus;
import java.util.List;

/**
 * 数据状态Mapper接口
 *
 * @author frank
 * @date 2020-06-16
 */
public interface BizDataStatusMapper {
    /**
     * 查询数据状态
     *
     * @param statusId 数据状态ID
     * @return 数据状态
     */
    public BizDataStatus selectBizDataStatusById(Long statusId);

    /**
     * 查询数据状态列表
     *
     * @param bizDataStatus 数据状态
     * @return 数据状态集合
     */
    public List<BizDataStatus> selectBizDataStatusList(BizDataStatus bizDataStatus);

    /**
     * 新增数据状态
     *
     * @param bizDataStatus 数据状态
     * @return 结果
     */
    public int insertBizDataStatus(BizDataStatus bizDataStatus);

    /**
     * 修改数据状态
     *
     * @param bizDataStatus 数据状态
     * @return 结果
     */
    public int updateBizDataStatus(BizDataStatus bizDataStatus);

    /**
     * 删除数据状态
     *
     * @param statusId 数据状态ID
     * @return 结果
     */
    public int deleteBizDataStatusById(Long statusId);

    /**
     * 批量删除数据状态
     *
     * @param statusIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizDataStatusByIds(String[] statusIds);
}
