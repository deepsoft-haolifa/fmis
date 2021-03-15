package com.ruoyi.fmis.datatrack.service;
import com.ruoyi.fmis.datatrack.domain.BizProcessDataTrack;

import java.util.List;

/**
 * 合同追踪Service接口
 *
 * @author Xianlu Tech
 * @date 2021-03-15
 */
public interface IBizProcessDataTrackService {
    /**
     * 查询合同追踪
     *
     * @param trackId 合同追踪ID
     * @return 合同追踪
     */
    public BizProcessDataTrack selectBizProcessDataTrackById(Long trackId);

    /**
     * 查询合同追踪列表
     *
     * @param bizProcessDataTrack 合同追踪
     * @return 合同追踪集合
     */
    public List<BizProcessDataTrack> selectBizProcessDataTrackList(BizProcessDataTrack bizProcessDataTrack);

    /**
     * 新增合同追踪
     *
     * @param bizProcessDataTrack 合同追踪
     * @return 结果
     */
    public int insertBizProcessDataTrack(BizProcessDataTrack bizProcessDataTrack);

    /**
     * 修改合同追踪
     *
     * @param bizProcessDataTrack 合同追踪
     * @return 结果
     */
    public int updateBizProcessDataTrack(BizProcessDataTrack bizProcessDataTrack);

    /**
     * 批量删除合同追踪
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessDataTrackByIds(String ids);

    /**
     * 删除合同追踪信息
     *
     * @param trackId 合同追踪ID
     * @return 结果
     */
    public int deleteBizProcessDataTrackById(Long trackId);
}