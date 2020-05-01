package com.ruoyi.fmis.quotationtrack.mapper;

import com.ruoyi.fmis.quotationtrack.domain.BizQuotationTrack;
import java.util.List;

/**
 * 报价追踪Mapper接口
 *
 * @author Xianlu Tech
 * @date 2020-04-30
 */
public interface BizQuotationTrackMapper {
    /**
     * 查询报价追踪
     *
     * @param trackId 报价追踪ID
     * @return 报价追踪
     */
    public BizQuotationTrack selectBizQuotationTrackById(Long trackId);

    /**
     * 查询报价追踪列表
     *
     * @param bizQuotationTrack 报价追踪
     * @return 报价追踪集合
     */
    public List<BizQuotationTrack> selectBizQuotationTrackList(BizQuotationTrack bizQuotationTrack);

    /**
     * 新增报价追踪
     *
     * @param bizQuotationTrack 报价追踪
     * @return 结果
     */
    public int insertBizQuotationTrack(BizQuotationTrack bizQuotationTrack);

    /**
     * 修改报价追踪
     *
     * @param bizQuotationTrack 报价追踪
     * @return 结果
     */
    public int updateBizQuotationTrack(BizQuotationTrack bizQuotationTrack);

    /**
     * 删除报价追踪
     *
     * @param trackId 报价追踪ID
     * @return 结果
     */
    public int deleteBizQuotationTrackById(Long trackId);

    /**
     * 批量删除报价追踪
     *
     * @param trackIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizQuotationTrackByIds(String[] trackIds);
}
