package com.ruoyi.fmis.data.service;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.fmis.Constant;
import com.ruoyi.fmis.common.BizContractLevel;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.domain.SaleListExportDTO;
import com.ruoyi.fmis.invoice.bean.InvoiceReqVo;
import com.ruoyi.fmis.invoice.bean.InvoiceRespVo;
import com.ruoyi.fmis.status.domain.BizDataStatus;
import com.ruoyi.fmis.stestn.domain.BizDataStestn;

import java.util.List;

/**
 * 合同管理Service接口
 *
 * @author frank
 * @date 2020-05-05
 */
public interface IBizProcessDataService {


    public List<BizContractLevel> listLevel (BizProcessData bizProcessData);
    public List<BizContractLevel> listLevelS (BizProcessData bizProcessData);
    public TableDataInfo listLevelProduct(BizProcessData bizProcessData);
    public TableDataInfo listLevelProductCaigou(BizProcessData bizProcessData);
    public TableDataInfo listLevelProductS(BizProcessData bizProcessData);
    public TableDataInfo listLevelActuator(BizProcessData bizProcessData);
    public TableDataInfo listLevelRef1(BizProcessData bizProcessData);
    public TableDataInfo listLevelRef2(BizProcessData bizProcessData);
    public TableDataInfo listLevelPA(BizProcessData bizProcessData);
    public TableDataInfo listLevelPA1(BizProcessData bizProcessData);
    public TableDataInfo listLevelPA2(BizProcessData bizProcessData);
    public TableDataInfo listLevelPA3(BizProcessData bizProcessData);
    public TableDataInfo listLevelPA4(BizProcessData bizProcessData);
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

    /**
     * 看采购合同购买的产品项
     * @param bizProcessData
     * @return
     */
    public List<BizProcessData> selectBizProcessDataListCg(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRef(BizProcessData bizProcessData);
    public List<BizProcessData> selectBizProcessDataListRefLiu(BizProcessData bizProcessData);

    /**
     * 查看销售合同中是否已经开始采购
     * @param bizProcessData
     * @return
     */
    public List<BizProcessData> selectBizProcessDataListXs(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRefBill(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRefDelivery(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRefCPayment(BizProcessData bizProcessData);

    public List<BizProcessData> selectBizProcessDataListRefPayment(BizProcessData bizProcessData);
    public BizProcessData selectBizProcessDataPaymentById(Long dataId);

    public List<BizProcessData> selectBizProcessDataListRefProcurement(BizProcessData bizProcessData);


    public List<BizProcessData> selectBizProcessDataListRefTrack(BizProcessData bizProcessData);


    public List<BizProcessData> selectBizProcessDataListRefInvoice(BizProcessData bizProcessData);

    /**
     * 开票明细
     * @return
     */
    public List<InvoiceRespVo> selectBizProcessChildListRefInvoice(InvoiceReqVo reqVo);


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


    /**
     * 检验完成更新采购订单和销售订单的状态
     * @param dataId 采购订单的dataId
     */
    public void  testCompleteUpdateStatus(String dataId);

    /**
     * 发货完成更新销售订单的状态（部分发货还是发货完成）
     * @param contractNo 销售订单No
     */
    public void  deliveryUpdateStatus(String contractNo);


    public List<BizProcessData> selectBizProcessDataForTodo(BizProcessData bizProcessData);


    public List<SaleListExportDTO> selectSaleListExport(Long dataId);

    /**
     * 删除发货单
     * @param ids
     * @return
     */
    int deleteNewDeliveryById(String ids);

    /**
     * 获取已审批通过 且未付款的借款单
     * @return
     */
    List<BizProcessData> selectAllBorrowingWithNoPayAndAgree();

}
