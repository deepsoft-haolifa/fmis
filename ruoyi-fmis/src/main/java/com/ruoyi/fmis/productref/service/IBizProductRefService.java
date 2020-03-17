package com.ruoyi.fmis.productref.service;

import com.ruoyi.fmis.productref.domain.BizProductRef;
import java.util.List;

/**
 * 产品关系Service接口
 *
 * @author frank
 * @date 2020-03-17
 */
public interface IBizProductRefService {
    /**
     * 查询产品关系
     *
     * @param productRefId 产品关系ID
     * @return 产品关系
     */
    public BizProductRef selectBizProductRefById(Long productRefId);

    /**
     * 查询产品关系列表
     *
     * @param bizProductRef 产品关系
     * @return 产品关系集合
     */
    public List<BizProductRef> selectBizProductRefList(BizProductRef bizProductRef);

    /**
     * 新增产品关系
     *
     * @param bizProductRef 产品关系
     * @return 结果
     */
    public int insertBizProductRef(BizProductRef bizProductRef);

    /**
     * 修改产品关系
     *
     * @param bizProductRef 产品关系
     * @return 结果
     */
    public int updateBizProductRef(BizProductRef bizProductRef);

    /**
     * 批量删除产品关系
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProductRefByIds(String ids);

    /**
     * 删除产品关系信息
     *
     * @param productRefId 产品关系ID
     * @return 结果
     */
    public int deleteBizProductRefById(Long productRefId);
}
