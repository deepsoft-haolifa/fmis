package com.ruoyi.fmis.quotationtrack.service;

import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.fmis.quotationtrack.domain.BizQuotationTrack;
import java.util.List;

/**
 * 报价追踪Service接口
 *
 * @author Xianlu Tech
 * @date 2020-04-30
 */
public interface IBizQuotationTrackService {
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
     * @param pageDomain
     * @return 报价追踪集合
     */
    public List<BizQuotationTrack> selectBizQuotationTrackList(BizQuotationTrack bizQuotationTrack, PageDomain pageDomain);

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
     * 批量删除报价追踪
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizQuotationTrackByIds(String ids);

    /**
     * 删除报价追踪信息
     *
     * @param trackId 报价追踪ID
     * @return 结果
     */
    public int deleteBizQuotationTrackById(Long trackId);
}
