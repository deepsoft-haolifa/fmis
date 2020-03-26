package com.ruoyi.fmis.quotation.mapper;

import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;

import java.util.List;

/**
 * 报价单Mapper接口
 *
 * @author frank
 * @date 2020-03-18
 */
public interface BizQuotationMapper {
    /**
     * 查询报价单
     *
     * @param quotationId 报价单ID
     * @return 报价单
     */
    public BizQuotation selectBizQuotationById(Long quotationId);

    /**
     * 查询报价单列表
     *
     * @param bizQuotation 报价单
     * @return 报价单集合
     */
    public List<BizQuotation> selectBizQuotationList(BizQuotation bizQuotation);

    public List<BizQuotationProduct> selectBizQuotationProductDictList(BizQuotationProduct bizQuotationProduct);

    /**
     * 新增报价单
     *
     * @param bizQuotation 报价单
     * @return 结果
     */
    public int insertBizQuotation(BizQuotation bizQuotation);

    /**
     * 修改报价单
     *
     * @param bizQuotation 报价单
     * @return 结果
     */
    public int updateBizQuotation(BizQuotation bizQuotation);

    /**
     * 删除报价单
     *
     * @param quotationId 报价单ID
     * @return 结果
     */
    public int deleteBizQuotationById(Long quotationId);

    /**
     * 批量删除报价单
     *
     * @param quotationIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizQuotationByIds(String[] quotationIds);

    /**
     * 查询报价单列表
     *
     * @param bizQuotation 报价单
     * @return 报价单集合
     */
    public List<BizQuotation> selectBizQuotationFlowList(BizQuotation bizQuotation);
}
