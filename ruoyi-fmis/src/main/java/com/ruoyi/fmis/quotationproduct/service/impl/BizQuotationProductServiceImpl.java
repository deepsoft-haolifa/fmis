package com.ruoyi.fmis.quotationproduct.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.mapper.BizProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.quotationproduct.mapper.BizQuotationProductMapper;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import com.ruoyi.fmis.quotationproduct.service.IBizQuotationProductService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.util.CollectionUtils;

/**
 * 报价单产品Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2020-03-21
 */
@Service
public class BizQuotationProductServiceImpl implements IBizQuotationProductService {
    @Autowired
    private BizQuotationProductMapper bizQuotationProductMapper;

    @Autowired
    private BizProductMapper bizProductMapper;

    /**
     * 查询报价单产品
     *
     * @param qpId 报价单产品ID
     * @return 报价单产品
     */
    @Override
    public BizQuotationProduct selectBizQuotationProductById(Long qpId) {
        return bizQuotationProductMapper.selectBizQuotationProductById(qpId);
    }

    /**
     * 查询报价单产品列表
     *
     * @param bizQuotationProduct 报价单产品
     * @return 报价单产品
     */
    @Override
    public List<BizQuotationProduct> selectBizQuotationProductList(BizQuotationProduct bizQuotationProduct) {
        return bizQuotationProductMapper.selectBizQuotationProductList(bizQuotationProduct);
    }

    @Override
    public List<BizQuotationProduct> selectBizQuotationProductDictList(BizQuotationProduct bizQuotationProduct) {
        List<BizQuotationProduct> bizQuotationProducts = bizQuotationProductMapper.selectBizQuotationProductDictList(bizQuotationProduct);

        for (BizQuotationProduct bizQuotationProduct1 : bizQuotationProducts) {
            BizProduct bizProduct = new BizProduct();
            bizProduct.setProductId(bizQuotationProduct1.getProductId());
            List<BizProduct> bizProducts = bizProductMapper.selectBizProductList(bizProduct);
            if (!CollectionUtils.isEmpty(bizProducts)) {
                bizQuotationProduct1.setBizProduct(bizProducts.get(0));
            }
        }

        return bizQuotationProducts;
    }

    /**
     * 新增报价单产品
     *
     * @param bizQuotationProduct 报价单产品
     * @return 结果
     */
    @Override
    public int insertBizQuotationProduct(BizQuotationProduct bizQuotationProduct) {
        bizQuotationProduct.setCreateTime(DateUtils.getNowDate());
        return bizQuotationProductMapper.insertBizQuotationProduct(bizQuotationProduct);
    }

    /**
     * 修改报价单产品
     *
     * @param bizQuotationProduct 报价单产品
     * @return 结果
     */
    @Override
    public int updateBizQuotationProduct(BizQuotationProduct bizQuotationProduct) {
        bizQuotationProduct.setUpdateTime(DateUtils.getNowDate());
        return bizQuotationProductMapper.updateBizQuotationProduct(bizQuotationProduct);
    }

    /**
     * 删除报价单产品对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizQuotationProductByIds(String ids) {
        return bizQuotationProductMapper.deleteBizQuotationProductByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除报价单产品信息
     *
     * @param qpId 报价单产品ID
     * @return 结果
     */
    @Override
    public int deleteBizQuotationProductById(Long qpId) {
        return bizQuotationProductMapper.deleteBizQuotationProductById(qpId);
    }
}
