package com.ruoyi.fmis.quotationtrack.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.quotationtrack.mapper.BizQuotationTrackMapper;
import com.ruoyi.fmis.quotationtrack.domain.BizQuotationTrack;
import com.ruoyi.fmis.quotationtrack.service.IBizQuotationTrackService;
import com.ruoyi.common.core.text.Convert;

/**
 * 报价追踪Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2020-04-30
 */
@Service
public class BizQuotationTrackServiceImpl implements IBizQuotationTrackService {
    @Autowired
    private BizQuotationTrackMapper bizQuotationTrackMapper;

    /**
     * 查询报价追踪
     *
     * @param trackId 报价追踪ID
     * @return 报价追踪
     */
    @Override
    public BizQuotationTrack selectBizQuotationTrackById(Long trackId) {
        return bizQuotationTrackMapper.selectBizQuotationTrackById(trackId);
    }

    /**
     * 查询报价追踪列表
     *
     * @param bizQuotationTrack 报价追踪
     * @return 报价追踪
     */
    @Override
    public List<BizQuotationTrack> selectBizQuotationTrackList(BizQuotationTrack bizQuotationTrack) {
        return bizQuotationTrackMapper.selectBizQuotationTrackList(bizQuotationTrack);
    }

    /**
     * 新增报价追踪
     *
     * @param bizQuotationTrack 报价追踪
     * @return 结果
     */
    @Override
    public int insertBizQuotationTrack(BizQuotationTrack bizQuotationTrack) {
        bizQuotationTrack.setCreateTime(DateUtils.getNowDate());
        return bizQuotationTrackMapper.insertBizQuotationTrack(bizQuotationTrack);
    }

    /**
     * 修改报价追踪
     *
     * @param bizQuotationTrack 报价追踪
     * @return 结果
     */
    @Override
    public int updateBizQuotationTrack(BizQuotationTrack bizQuotationTrack) {
        bizQuotationTrack.setUpdateTime(DateUtils.getNowDate());
        return bizQuotationTrackMapper.updateBizQuotationTrack(bizQuotationTrack);
    }

    /**
     * 删除报价追踪对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizQuotationTrackByIds(String ids) {
        return bizQuotationTrackMapper.deleteBizQuotationTrackByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除报价追踪信息
     *
     * @param trackId 报价追踪ID
     * @return 结果
     */
    @Override
    public int deleteBizQuotationTrackById(Long trackId) {
        return bizQuotationTrackMapper.deleteBizQuotationTrackById(trackId);
    }
}
