package com.ruoyi.fmis.product.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.mapper.BizProductMapper;
import com.ruoyi.fmis.product.service.IBizProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 业务产品Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2020-02-26
 */
@Service
public class BizProductServiceImpl implements IBizProductService {
    @Autowired
    private BizProductMapper bizProductMapper;

    /**
     * 查询业务产品
     *
     * @param productId 业务产品ID
     * @return 业务产品
     */
    @Override
    public BizProduct selectBizProductById(Long productId) {
        return bizProductMapper.selectBizProductById(productId);
    }

    /**
     * 查询业务产品列表
     *
     * @param bizProduct 业务产品
     * @return 业务产品
     */
    @Override
    public List<BizProduct> selectBizProductList(BizProduct bizProduct) {
        return bizProductMapper.selectBizProductList(bizProduct);
    }

    /**
     * 新增业务产品
     *
     * @param bizProduct 业务产品
     * @return 结果
     */
    @Override
    public int insertBizProduct(BizProduct bizProduct) {
        bizProduct.setCreateTime(DateUtils.getNowDate());
        return bizProductMapper.insertBizProduct(bizProduct);
    }

    /**
     * 修改业务产品
     *
     * @param bizProduct 业务产品
     * @return 结果
     */
    @Override
    public int updateBizProduct(BizProduct bizProduct) {
        bizProduct.setUpdateTime(DateUtils.getNowDate());
        return bizProductMapper.updateBizProduct(bizProduct);
    }

    /**
     * 删除业务产品对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizProductByIds(String ids) {
        return bizProductMapper.deleteBizProductByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务产品信息
     *
     * @param productId 业务产品ID
     * @return 结果
     */
    @Override
    public int deleteBizProductById(Long productId) {
        return bizProductMapper.deleteBizProductById(productId);
    }
}
