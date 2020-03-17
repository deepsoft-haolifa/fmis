package com.ruoyi.fmis.productref.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.productref.mapper.BizProductRefMapper;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.common.core.text.Convert;

/**
 * 产品关系Service业务层处理
 *
 * @author frank
 * @date 2020-03-17
 */
@Service
public class BizProductRefServiceImpl implements IBizProductRefService {
    @Autowired
    private BizProductRefMapper bizProductRefMapper;

    /**
     * 查询产品关系
     *
     * @param productRefId 产品关系ID
     * @return 产品关系
     */
    @Override
    public BizProductRef selectBizProductRefById(Long productRefId) {
        return bizProductRefMapper.selectBizProductRefById(productRefId);
    }

    /**
     * 查询产品关系列表
     *
     * @param bizProductRef 产品关系
     * @return 产品关系
     */
    @Override
    public List<BizProductRef> selectBizProductRefList(BizProductRef bizProductRef) {
        return bizProductRefMapper.selectBizProductRefList(bizProductRef);
    }

    /**
     * 新增产品关系
     *
     * @param bizProductRef 产品关系
     * @return 结果
     */
    @Override
    public int insertBizProductRef(BizProductRef bizProductRef) {
        bizProductRef.setCreateTime(DateUtils.getNowDate());
        return bizProductRefMapper.insertBizProductRef(bizProductRef);
    }

    /**
     * 修改产品关系
     *
     * @param bizProductRef 产品关系
     * @return 结果
     */
    @Override
    public int updateBizProductRef(BizProductRef bizProductRef) {
        bizProductRef.setUpdateTime(DateUtils.getNowDate());
        return bizProductRefMapper.updateBizProductRef(bizProductRef);
    }

    /**
     * 删除产品关系对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizProductRefByIds(String ids) {
        return bizProductRefMapper.deleteBizProductRefByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除产品关系信息
     *
     * @param productRefId 产品关系ID
     * @return 结果
     */
    @Override
    public int deleteBizProductRefById(Long productRefId) {
        return bizProductRefMapper.deleteBizProductRefById(productRefId);
    }
}
