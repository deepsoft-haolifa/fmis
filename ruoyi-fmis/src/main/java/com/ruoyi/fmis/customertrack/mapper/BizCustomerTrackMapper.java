package com.ruoyi.fmis.customertrack.mapper;

import com.ruoyi.fmis.customertrack.domain.BizCustomerTrack;
import java.util.List;

/**
 * 客户追踪Mapper接口
 *
 * @author frank
 * @date 2020-04-29
 */
public interface BizCustomerTrackMapper {
    /**
     * 查询客户追踪
     *
     * @param trackId 客户追踪ID
     * @return 客户追踪
     */
    public BizCustomerTrack selectBizCustomerTrackById(Long trackId);

    /**
     * 查询客户追踪列表
     *
     * @param bizCustomerTrack 客户追踪
     * @return 客户追踪集合
     */
    public List<BizCustomerTrack> selectBizCustomerTrackList(BizCustomerTrack bizCustomerTrack);

    /**
     * 查询客户追踪列表
     *
     * @param bizCustomerTrack 客户追踪
     * @return 客户追踪集合
     */
    public List<BizCustomerTrack> selectBizCustomerTrackListAs(BizCustomerTrack bizCustomerTrack);

    /**
     * 新增客户追踪
     *
     * @param bizCustomerTrack 客户追踪
     * @return 结果
     */
    public int insertBizCustomerTrack(BizCustomerTrack bizCustomerTrack);

    /**
     * 修改客户追踪
     *
     * @param bizCustomerTrack 客户追踪
     * @return 结果
     */
    public int updateBizCustomerTrack(BizCustomerTrack bizCustomerTrack);

    /**
     * 删除客户追踪
     *
     * @param trackId 客户追踪ID
     * @return 结果
     */
    public int deleteBizCustomerTrackById(Long trackId);

    /**
     * 批量删除客户追踪
     *
     * @param trackIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizCustomerTrackByIds(String[] trackIds);
}
