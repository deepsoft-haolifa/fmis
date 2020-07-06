package com.ruoyi.fmis.assets.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.assets.mapper.BizAssetsMapper;
import com.ruoyi.fmis.assets.domain.BizAssets;
import com.ruoyi.fmis.assets.service.IBizAssetsService;
import com.ruoyi.common.core.text.Convert;

/**
 * 固定资产Service业务层处理
 *
 * @author frank
 * @date 2020-07-01
 */
@Service
public class BizAssetsServiceImpl implements IBizAssetsService {
    @Autowired
    private BizAssetsMapper bizAssetsMapper;

    /**
     * 查询固定资产
     *
     * @param assetsId 固定资产ID
     * @return 固定资产
     */
    @Override
    public BizAssets selectBizAssetsById(Long assetsId) {
        return bizAssetsMapper.selectBizAssetsById(assetsId);
    }

    /**
     * 查询固定资产列表
     *
     * @param bizAssets 固定资产
     * @return 固定资产
     */
    @Override
    public List<BizAssets> selectBizAssetsList(BizAssets bizAssets) {
        return bizAssetsMapper.selectBizAssetsList(bizAssets);
    }

    /**
     * 新增固定资产
     *
     * @param bizAssets 固定资产
     * @return 结果
     */
    @Override
    public int insertBizAssets(BizAssets bizAssets) {
        bizAssets.setCreateTime(DateUtils.getNowDate());
        return bizAssetsMapper.insertBizAssets(bizAssets);
    }

    /**
     * 修改固定资产
     *
     * @param bizAssets 固定资产
     * @return 结果
     */
    @Override
    public int updateBizAssets(BizAssets bizAssets) {
        bizAssets.setUpdateTime(DateUtils.getNowDate());
        return bizAssetsMapper.updateBizAssets(bizAssets);
    }

    /**
     * 删除固定资产对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizAssetsByIds(String ids) {
        return bizAssetsMapper.deleteBizAssetsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除固定资产信息
     *
     * @param assetsId 固定资产ID
     * @return 结果
     */
    @Override
    public int deleteBizAssetsById(Long assetsId) {
        return bizAssetsMapper.deleteBizAssetsById(assetsId);
    }
}
