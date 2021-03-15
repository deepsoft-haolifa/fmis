package com.ruoyi.fmis.datatrack.service.impl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.datatrack.domain.BizProcessDataTrack;
import com.ruoyi.fmis.datatrack.mapper.BizProcessDataTrackMapper;
import com.ruoyi.fmis.datatrack.service.IBizProcessDataTrackService;
import com.ruoyi.framework.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 合同追踪Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2021-03-15
 */
@Service
public class BizProcessDataTrackServiceImpl implements IBizProcessDataTrackService {
    @Autowired
    private BizProcessDataTrackMapper bizProcessDataTrackMapper;

    /**
     * 查询合同追踪
     *
     * @param trackId 合同追踪ID
     * @return 合同追踪
     */
    @Override
    public BizProcessDataTrack selectBizProcessDataTrackById(Long trackId) {
        return bizProcessDataTrackMapper.selectBizProcessDataTrackById(trackId);
    }

    /**
     * 查询合同追踪列表
     *
     * @param bizProcessDataTrack 合同追踪
     * @return 合同追踪
     */
    @Override
    public List<BizProcessDataTrack> selectBizProcessDataTrackList(BizProcessDataTrack bizProcessDataTrack) {
        return bizProcessDataTrackMapper.selectBizProcessDataTrackList(bizProcessDataTrack);
    }

    /**
     * 新增合同追踪
     *
     * @param bizProcessDataTrack 合同追踪
     * @return 结果
     */
    @Override
    public int insertBizProcessDataTrack(BizProcessDataTrack bizProcessDataTrack) {
        bizProcessDataTrack.setCreateTime(DateUtils.getNowDate());
        bizProcessDataTrack.setUpdateTime(DateUtils.getNowDate());
        bizProcessDataTrack.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        return bizProcessDataTrackMapper.insertBizProcessDataTrack(bizProcessDataTrack);
    }

    /**
     * 修改合同追踪
     *
     * @param bizProcessDataTrack 合同追踪
     * @return 结果
     */
    @Override
    public int updateBizProcessDataTrack(BizProcessDataTrack bizProcessDataTrack) {
        bizProcessDataTrack.setUpdateTime(DateUtils.getNowDate());
        return bizProcessDataTrackMapper.updateBizProcessDataTrack(bizProcessDataTrack);
    }

    /**
     * 删除合同追踪对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessDataTrackByIds(String ids) {
        return bizProcessDataTrackMapper.deleteBizProcessDataTrackByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除合同追踪信息
     *
     * @param trackId 合同追踪ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessDataTrackById(Long trackId) {
        return bizProcessDataTrackMapper.deleteBizProcessDataTrackById(trackId);
    }
}