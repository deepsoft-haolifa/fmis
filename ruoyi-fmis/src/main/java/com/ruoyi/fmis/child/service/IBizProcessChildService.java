package com.ruoyi.fmis.child.service;

import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.domain.ProcessDataDTO;

import java.util.List;

/**
 * 流程数据字Service接口
 *
 * @author Xianlu Tech
 * @date 2020-05-05
 */
public interface IBizProcessChildService {
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


    public List<BizProcessChild> selectBizTestProductList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizTestActuatorList(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizTestRef1List(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizTestRef2List(BizProcessChild bizProcessChild);

    public List<BizProcessChild> selectBizTestPAList(BizProcessChild bizProcessChild);

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

    public int updateBizProcessChildFH(BizProcessChild bizProcessChild);

    /**
     * 批量删除流程数据字
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessChildByIds(String ids);

    /**
     * 删除流程数据字信息
     *
     * @param childId 流程数据字ID
     * @return 结果
     */
    public int deleteBizProcessChildById(Long childId);

    /**
     * 删除流程数据字信息
     *
     * @param dataIds 流程数据父ID
     * @return 结果
     */
    public int deleteBizProcessChildByDataIds(String dataIds);

    /**
     * 获取某段时间内的费用报销费用
     *
     * @return
     */
    public double selectPaymentedPrice(String subjectId, String deptId, String startDate, String endDate);


    /**
     * 获取销售合同的已发货数量和合同数量
     *
     * @return
     */
    public int getDeliveryQtyByContractNo(String contractNo);
    /**
     * 获取销售合同合同数量
     *
     * @return
     */
    public ProcessDataDTO getSaleQtyByContractNo(String contractNo);

}
