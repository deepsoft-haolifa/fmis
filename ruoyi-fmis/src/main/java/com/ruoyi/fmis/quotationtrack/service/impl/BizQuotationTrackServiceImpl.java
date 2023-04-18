package com.ruoyi.fmis.quotationtrack.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.quotationtrack.mapper.BizQuotationTrackMapper;
import com.ruoyi.fmis.quotationtrack.domain.BizQuotationTrack;
import com.ruoyi.fmis.quotationtrack.service.IBizQuotationTrackService;
import com.ruoyi.common.core.text.Convert;

import javax.annotation.Resource;

import static com.github.pagehelper.page.PageMethod.startPage;

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
    @Resource
    private SysDeptMapper deptMapper;
    @Resource
    private ISysUserService sysUserService;

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
     * @param pageDomain
     * @return 报价追踪
     */
    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizQuotationTrack> selectBizQuotationTrackList(BizQuotationTrack bizQuotationTrack, PageDomain pageDomain) {
        // 获取当前登录用户
        SysUser sysUser = ShiroUtils.getSysUser();
        Long deptId = sysUser.getDeptId();
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        List<Long> collect = children.stream().map(sysDept -> sysDept.getDeptId()).collect(Collectors.toList());
        bizQuotationTrack.setDeptIdList(collect);
        bizQuotationTrack.setUserId(sysUser.getUserId());
        if (Objects.nonNull(pageDomain)) {
            PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
        }
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
