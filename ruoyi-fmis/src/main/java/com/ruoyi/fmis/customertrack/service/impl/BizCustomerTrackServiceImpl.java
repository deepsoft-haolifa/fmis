package com.ruoyi.fmis.customertrack.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.mapper.BizCustomerMapper;
import com.ruoyi.fmis.customertrack.domain.BizCustomerTrackVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.customertrack.mapper.BizCustomerTrackMapper;
import com.ruoyi.fmis.customertrack.domain.BizCustomerTrack;
import com.ruoyi.fmis.customertrack.service.IBizCustomerTrackService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.util.CollectionUtils;

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
    @Autowired
    private BizCustomerMapper bizCustomerMapper;

    /**
     * 查询客户列表（包含客户追踪信息）
     *
     * @param bizCustomer 客户
     * @return 客户
     */
    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizCustomerTrackVo> selectBizCustomerListAndTrack(BizCustomer bizCustomer) {
        List<BizCustomerTrackVo> list = new ArrayList<>();
        List<BizCustomer> bizCustomers = bizCustomerMapper.selectBizCustomerList(bizCustomer);
        if (!CollectionUtils.isEmpty(bizCustomers)) {
            bizCustomers.forEach(cu -> {
                BizCustomerTrackVo vo = new BizCustomerTrackVo();
                BeanUtils.copyProperties(cu, vo);
                List<BizCustomerTrack> customerTracks = bizCustomerTrackMapper.selectBizCustomerTrackList(new BizCustomerTrack() {{
                    setCustomerId(cu.getCustomerId());
                }});
                if (!CollectionUtils.isEmpty(customerTracks)) {
                    // 找出最新一条跟踪记录
                    List<BizCustomerTrack> tracks = customerTracks.stream().sorted(Comparator.comparing(BizCustomerTrack::getTrackId).reversed()).collect(Collectors.toList());
                    BizCustomerTrack bizCustomerTrack = tracks.get(0);
                    vo.setFeedback(bizCustomerTrack.getFeedback());
                    vo.setFeedbackDate(bizCustomerTrack.getCreateTime());
                }
                list.add(vo);
            });
        }
        return list;
    }

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
