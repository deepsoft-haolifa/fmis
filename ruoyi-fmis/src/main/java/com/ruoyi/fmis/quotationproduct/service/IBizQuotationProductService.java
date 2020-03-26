package com.ruoyi.fmis.quotationproduct.service;

import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import java.util.List;

/**
 * 报价单产品Service接口
 *
 * @author Xianlu Tech
 * @date 2020-03-21
 */
public interface IBizQuotationProductService {
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
     * 批量删除报价单产品
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizQuotationProductByIds(String ids);

    /**
     * 删除报价单产品信息
     *
     * @param qpId 报价单产品ID
     * @return 结果
     */
    public int deleteBizQuotationProductById(Long qpId);
}
