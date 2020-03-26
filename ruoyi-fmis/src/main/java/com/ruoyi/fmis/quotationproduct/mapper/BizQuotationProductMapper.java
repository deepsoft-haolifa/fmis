package com.ruoyi.fmis.quotationproduct.mapper;

import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import java.util.List;

/**
 * 报价单产品Mapper接口
 *
 * @author Xianlu Tech
 * @date 2020-03-21
 */
public interface BizQuotationProductMapper {
    /**
     * 查询报价单产品
     *
     * @param qpId 报价单产品ID
     * @return 报价单产品
     */
    public BizQuotationProduct selectBizQuotationProductById(Long qpId);

    /**
     * 查询报价单产品列表
     *
     * @param bizQuotationProduct 报价单产品
     * @return 报价单产品集合
     */
    public List<BizQuotationProduct> selectBizQuotationProductList(BizQuotationProduct bizQuotationProduct);

    /**
     * 查询报价单产品列表
     *
     * @param bizQuotationProduct 报价单产品
     * @return 报价单产品集合
     */
    public List<BizQuotationProduct> selectBizQuotationProductDictList(BizQuotationProduct bizQuotationProduct);

    /**
     * 新增报价单产品
     *
     * @param bizQuotationProduct 报价单产品
     * @return 结果
     */
    public int insertBizQuotationProduct(BizQuotationProduct bizQuotationProduct);

    /**
     * 修改报价单产品
     *
     * @param bizQuotationProduct 报价单产品
     * @return 结果
     */
    public int updateBizQuotationProduct(BizQuotationProduct bizQuotationProduct);

    /**
     * 删除报价单产品
     *
     * @param qpId 报价单产品ID
     * @return 结果
     */
    public int deleteBizQuotationProductById(Long qpId);

    /**
     * 批量删除报价单产品
     *
     * @param qpIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizQuotationProductByIds(String[] qpIds);
}
