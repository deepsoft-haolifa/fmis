package com.ruoyi.fmis.finance.service;

import com.ruoyi.fmis.finance.domain.BizBillContract;

import java.util.List;

/**
 * 合同收款(合同分解)Service接口
 *
 * @author hedong
 * @date 2020-10-22
 */
public interface IBizBillContractService {
    /**
     * 查询合同收款(合同分解)
     *
     * @param bcId 合同收款(合同分解)ID
     * @return 合同收款(合同分解)
     */
    public BizBillContract selectBizBillContractById(Long bcId);

    /**
     * 查询合同收款(合同分解)列表
     *
     * @param bizBillContract 合同收款(合同分解)
     * @return 合同收款(合同分解)集合
     */
    public List<BizBillContract> selectBizBillContractList(BizBillContract bizBillContract);

    /**
     * 新增合同收款(合同分解)
     *
     * @param bizBillContract 合同收款(合同分解)
     * @return 结果
     */
    public int insertBizBillContract(BizBillContract bizBillContract);

    /**
     * 修改合同收款(合同分解)
     *
     * @param bizBillContract 合同收款(合同分解)
     * @return 结果
     */
    public int updateBizBillContract(BizBillContract bizBillContract);

    /**
     * 批量删除合同收款(合同分解)
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizBillContractByIds(String ids);

    /**
     * 删除合同收款(合同分解)信息
     *
     * @param bcId 合同收款(合同分解)ID
     * @return 结果
     */
    public int deleteBizBillContractById(Long bcId);

}
