package com.ruoyi.fmis.data.service;

import com.ruoyi.fmis.data.domain.BizProcessData;
import java.util.List;

/**
 * 合同管理Service接口
 *
 * @author frank
 * @date 2020-05-05
 */
public interface IBizProcessDataService {
    /**
     * 查询合同管理
     *
     * @param dataId 合同管理ID
     * @return 合同管理
     */
    public BizProcessData selectBizProcessDataById(Long dataId);

    public BizProcessData selectBizProcessDataBorrowingById(Long dataId);
    /**
     * 查询合同管理列表
     *
     * @param bizProcessData 合同管理
     * @return 合同管理集合
     */
    public List<BizProcessData> selectBizProcessDataList(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRef(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRefBill(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRefDelivery(BizProcessData bizProcessData);


    public List<BizProcessData> selectBizProcessDataListRefProcurement(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataVoRefBorrowing(BizProcessData bizProcessData);

    public int doExamine(String dataId,String status,String remark,String bizId);
    /**
     * 新增合同管理
     *
     * @param bizProcessData 合同管理
     * @return 结果
     */
    public int insertBizProcessData(BizProcessData bizProcessData);

    /**
     * 修改合同管理
     *
     * @param bizProcessData 合同管理
     * @return 结果
     */
    public int updateBizProcessData(BizProcessData bizProcessData);

    /**
     * 批量删除合同管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessDataByIds(String ids);

    /**
     * 删除合同管理信息
     *
     * @param dataId 合同管理ID
     * @return 结果
     */
    public int deleteBizProcessDataById(Long dataId);

    public int subReportBizQuotation(BizProcessData bizProcessData);

    public int subReportBizQuotationBorrowing(BizProcessData bizProcessData);
    /**
     * 开始质检
     * @param bizProcessData
     * @return
     */
    public int subTestBizQuotation(BizProcessData bizProcessData);

    public Long selectProcurementMaxNo ();
}
