package com.ruoyi.fmis.customertrack.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.customertrack.mapper.BizCustomerTrackMapper;
import com.ruoyi.fmis.customertrack.domain.BizCustomerTrack;
import com.ruoyi.fmis.customertrack.service.IBizCustomerTrackService;
import com.ruoyi.common.core.text.Convert;

/**
 * 客户追踪Service业务层处理
 *
 * @author frank
 * @date 2020-04-29
 */
@Service
public class BizCustomerTrackServiceImpl implements IBizCustomerTrackService {
    @Autowired
    private BizCustomerTrackMapper bizCustomerTrackMapper;

    /**
     * 查询客户追踪
     *
     * @param trackId 客户追踪ID
     * @return 客户追踪
     */
    @Override
    public BizCustomerTrack selectBizCustomerTrackById(Long trackId) {
        return bizCustomerTrackMapper.selectBizCustomerTrackById(trackId);
    }

    /**
     * 查询客户追踪列表
     *
     * @param bizCustomerTrack 客户追踪
     * @return 客户追踪
     */
    @Override
    public List<BizCustomerTrack> selectBizCustomerTrackList(BizCustomerTrack bizCustomerTrack) {
        return bizCustomerTrackMapper.selectBizCustomerTrackList(bizCustomerTrack);
    }

    /**
     * 查询客户追踪列表
     *
     * @param bizCustomerTrack 客户追踪
     * @return 客户追踪
     */
    @Override
    public List<BizCustomerTrack> selectBizCustomerTrackListAs(BizCustomerTrack bizCustomerTrack) {
        return bizCustomerTrackMapper.selectBizCustomerTrackListAs(bizCustomerTrack);
    }

    /**
     * 新增客户追踪
     *
     * @param bizCustomerTrack 客户追踪
     * @return 结果
     */
    @Override
    public int insertBizCustomerTrack(BizCustomerTrack bizCustomerTrack) {
        bizCustomerTrack.setCreateTime(DateUtils.getNowDate());
        return bizCustomerTrackMapper.insertBizCustomerTrack(bizCustomerTrack);
    }

    /**
     * 修改客户追踪
     *
     * @param bizCustomerTrack 客户追踪
     * @return 结果
     */
    @Override
    public int updateBizCustomerTrack(BizCustomerTrack bizCustomerTrack) {
        bizCustomerTrack.setUpdateTime(DateUtils.getNowDate());
        return bizCustomerTrackMapper.updateBizCustomerTrack(bizCustomerTrack);
    }

    /**
     * 删除客户追踪对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizCustomerTrackByIds(String ids) {
        return bizCustomerTrackMapper.deleteBizCustomerTrackByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户追踪信息
     *
     * @param trackId 客户追踪ID
     * @return 结果
     */
    @Override
    public int deleteBizCustomerTrackById(Long trackId) {
        return bizCustomerTrackMapper.deleteBizCustomerTrackById(trackId);
    }
}
