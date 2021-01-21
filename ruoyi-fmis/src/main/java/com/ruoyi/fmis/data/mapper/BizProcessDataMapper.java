package com.ruoyi.fmis.data.mapper;

import com.ruoyi.fmis.data.domain.BizProcessData;
import java.util.List;

/**
 * 合同管理Mapper接口
 *
 * @author frank
 * @date 2020-05-05
 */
public interface BizProcessDataMapper {
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
    public List<BizProcessData> selectBizProcessDataListCg(BizProcessData bizProcessData);


    public List<BizProcessData> selectBizProcessDataListRef(BizProcessData bizProcessData);
    public List<BizProcessData> selectBizProcessDataListXs(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRefBill(BizProcessData bizProcessData);

    /**
     * 发货管理查询
     * @param bizProcessData
     * @return
     */
    public List<BizProcessData> selectBizProcessDataListRefDelivery(BizProcessData bizProcessData);

    /**
     * 付款申请查询
     * @param bizProcessData
     * @return
     */
    public List<BizProcessData> selectBizProcessDataListRefCPayment(BizProcessData bizProcessData);

    /**
     * 报销申请列表查询
     * @param bizProcessData
     * @return
     */
    public List<BizProcessData> selectBizProcessDataListRefPayment(BizProcessData bizProcessData);


    /**
     * 报销详情查询
     * @param dataId
     * @return
     */
    public BizProcessData selectBizProcessDataPaymentById(Long dataId);


    /**
     * 采购管理查询
     * @param bizProcessData
     * @return
     */
    public List<BizProcessData> selectBizProcessDataListRefProcurement(BizProcessData bizProcessData);

    /**
     * 借款申请
     * @param bizProcessData
     * @return
     */
    public List<BizProcessData> selectBizProcessDataVoRefBorrowing(BizProcessData bizProcessData);

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
     * 删除合同管理
     *
     * @param dataId 合同管理ID
     * @return 结果
     */
    public int deleteBizProcessDataById(Long dataId);

    /**
     * 批量删除合同管理
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessDataByIds(String[] dataIds);

    public Long selectProcurementMaxNo();
}
