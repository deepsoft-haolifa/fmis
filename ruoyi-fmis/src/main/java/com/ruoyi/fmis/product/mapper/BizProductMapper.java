package com.ruoyi.fmis.product.mapper;

import com.ruoyi.fmis.product.domain.BizProduct;
import java.util.List;

/**
 * 业务产品Mapper接口
 *
 * @author Xianlu Tech
 * @date 2020-02-26
 */
public interface BizProductMapper {
    /**
     * 查询业务产品
     *
     * @param productId 业务产品ID
     * @return 业务产品
     */
    public BizProduct selectBizProductById(Long productId);

    /**
     * 查询业务产品列表
     *
     * @param bizProduct 业务产品
     * @return 业务产品集合
     */
    public List<BizProduct> selectBizProductList(BizProduct bizProduct);
    public List<BizProduct> selectBizProductListNodel(BizProduct bizProduct);

    /**
     * 新增业务产品
     *
     * @param bizProduct 业务产品
     * @return 结果
     */
    public int insertBizProduct(BizProduct bizProduct);

    /**
     * 修改业务产品
     *
     * @param bizProduct 业务产品
     * @return 结果
     */
    public int updateBizProduct(BizProduct bizProduct);

    /**
     * 删除业务产品
     *
     * @param productId 业务产品ID
     * @return 结果
     */
    public int deleteBizProductById(Long productId);

    /**
     * 批量删除业务产品
     *
     * @param productIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProductByIds(String[] productIds);
}
