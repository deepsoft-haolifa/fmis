package com.ruoyi.fmis.quotation.service;

import com.ruoyi.fmis.quotation.domain.BizQuotation;
import java.util.List;

/**
 * 报价单Service接口
 *
 * @author frank
 * @date 2020-03-18
 */
public interface IBizQuotationService {
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
    public List<BizQuotation> selectBizQuotationFlowList(BizQuotation bizQuotation);

    /**
     * 报价单产品集合
     * @param bizQuotation
     * @return
     */
    public List<BizQuotation> selectBizQuotationProductList(BizQuotation bizQuotation);
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
     * 批量删除报价单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizQuotationByIds(String ids);

    /**
     * 删除报价单信息
     *
     * @param quotationId 报价单ID
     * @return 结果
     */
    public int deleteBizQuotationById(Long quotationId);

    /**
     * 上报报价单
     * @param bizQuotation
     * @return
     */
    public int subReportBizQuotation(BizQuotation bizQuotation);

    /**
     * 审批
     * @param quotationId
     * @param status
     * @param remark
     * @return
     */
    public int doExamine(String quotationId,String status,String remark);

    /**
     * 查询待办
     * @param bizQuotationTodo
     * @return
     */
    List<BizQuotation> selectBizQuotationByFlowStatus(BizQuotation bizQuotationTodo);
}

