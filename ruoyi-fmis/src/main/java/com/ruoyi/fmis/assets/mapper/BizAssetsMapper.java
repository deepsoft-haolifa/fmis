package com.ruoyi.fmis.assets.mapper;

import com.ruoyi.fmis.assets.domain.BizAssets;
import java.util.List;

/**
 * 固定资产Mapper接口
 *
 * @author frank
 * @date 2020-07-01
 */
public interface BizAssetsMapper {
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
     * 删除固定资产
     *
     * @param assetsId 固定资产ID
     * @return 结果
     */
    public int deleteBizAssetsById(Long assetsId);

    /**
     * 批量删除固定资产
     *
     * @param assetsIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizAssetsByIds(String[] assetsIds);
}
