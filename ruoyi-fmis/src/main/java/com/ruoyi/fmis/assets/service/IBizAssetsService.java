package com.ruoyi.fmis.assets.service;

import com.ruoyi.fmis.assets.domain.BizAssets;
import java.util.List;

/**
 * 固定资产Service接口
 *
 * @author frank
 * @date 2020-07-01
 */
public interface IBizAssetsService {
    /**
     * 查询固定资产
     *
     * @param assetsId 固定资产ID
     * @return 固定资产
     */
    public BizAssets selectBizAssetsById(Long assetsId);

    /**
     * 查询固定资产列表
     *
     * @param bizAssets 固定资产
     * @return 固定资产集合
     */
    public List<BizAssets> selectBizAssetsList(BizAssets bizAssets);

    /**
     * 新增固定资产
     *
     * @param bizAssets 固定资产
     * @return 结果
     */
    public int insertBizAssets(BizAssets bizAssets);

    /**
     * 修改固定资产
     *
     * @param bizAssets 固定资产
     * @return 结果
     */
    public int updateBizAssets(BizAssets bizAssets);

    /**
     * 批量删除固定资产
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizAssetsByIds(String ids);

    /**
     * 删除固定资产信息
     *
     * @param assetsId 固定资产ID
     * @return 结果
     */
    public int deleteBizAssetsById(Long assetsId);
}
