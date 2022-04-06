package com.ruoyi.fmis.child.mapper;

import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.domain.ProcessDataDTO;
import com.ruoyi.fmis.invoice.bean.InvoiceReqVo;
import com.ruoyi.fmis.invoice.bean.export.InvoiceExportDTO;
import com.ruoyi.fmis.stestn.domain.BizDataStestn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程数据字Mapper接口
 *
 * @author Xianlu Tech
 * @date 2020-05-05
 */
public interface BizProcessChildMapper {
    /**
     * 查询流程数据字
     *
     * @param childId 流程数据字ID
     * @return 流程数据字
     */
    public BizProcessChild selectBizProcessChildById(Long childId);

    /**
     * 查询流程数据字列表
     *
     * @param bizProcessChild 流程数据字
     * @return 流程数据字集合
     */
    public List<BizProcessChild> selectBizProcessChildList(BizProcessChild bizProcessChild);
    public List<BizProcessChild> selectBizProcessChildListForKuCun(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizProcessChildInventoryList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizQuotationProductList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizChildProductList(BizProcessChild bizProcessChild);
    public List<BizProcessChild> selectBizChildProductListCaigou(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizTestProductList(BizProcessChild bizProcessChild);
    public List<BizProcessChild> selectBizTestActuatorList(BizProcessChild bizProcessChild);
    public List<BizProcessChild> selectBizTestRef1List(BizProcessChild bizProcessChild);
    public List<BizProcessChild> selectBizTestRef2List(BizProcessChild bizProcessChild);
    public List<BizProcessChild> selectBizTestPAList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizTestChildList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizTestChildHistoryList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizTestStayChildList(BizProcessChild bizProcessChild);


    public List<BizProcessChild> selectBizChildActuatorList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizChildRef1List(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizChildRef2List(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizChildPAList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizChildPA1List(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizChildPA2List(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizChildPA3List(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizChildPA4List(BizProcessChild bizProcessChild);
    /**
     * 新增流程数据字
     *
     * @param bizProcessChild 流程数据字
     * @return 结果
     */
    public int insertBizProcessChild(BizProcessChild bizProcessChild);

    /**
     * 修改流程数据字
     *
     * @param bizProcessChild 流程数据字
     * @return 结果
     */
    public int updateBizProcessChild(BizProcessChild bizProcessChild);
    /**
     * 修改发货
     *
     * @param bizProcessChild 流程数据字
     * @return 结果
     */
    public int updateBizProcessChildFH(BizProcessChild bizProcessChild);

    /**
     * 删除流程数据字
     *
     * @param childId 流程数据字ID
     * @return 结果
     */
    public int deleteBizProcessChildById(Long childId);

    /**
     * 批量删除流程数据字
     *
     * @param childIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessChildByIds(String[] childIds);

    /**
     * 批量删除流程
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessChildByDataIds(String[] dataIds);


    double selectPaymentedPrice(@Param("subjectId") String subjectId, @Param("deptId") String deptId, @Param("startDate") String startDate,@Param("endDate") String endDate);

    Integer getDeliveryQtyByContractNo(@Param("contractNo")String contractNo);
    ProcessDataDTO getSaleQtyByContractNo(@Param("contractNo")String contractNo);


    List<InvoiceExportDTO> yyInvoiceExport(InvoiceReqVo invoiceReqVo);
}
