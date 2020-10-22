//package com.ruoyi.fmis.bcontract.mapper;
//
//import com.ruoyi.fmis.bcontract.domain.BizBillContract;
//import java.util.List;
//
///**
// * 合同收款Mapper接口
// *
// * @author frank
// * @date 2020-06-26
// */
//public interface BizBillContractMapper {
//    /**
//     * 查询合同收款
//     *
//     * @param bcId 合同收款ID
//     * @return 合同收款
//     */
//    public BizBillContract selectBizBillContractById(Long bcId);
//
//    /**
//     * 查询合同收款列表
//     *
//     * @param bizBillContract 合同收款
//     * @return 合同收款集合
//     */
//    public List<BizBillContract> selectBizBillContractList(BizBillContract bizBillContract);
//
//    /**
//     * 新增合同收款
//     *
//     * @param bizBillContract 合同收款
//     * @return 结果
//     */
//    public int insertBizBillContract(BizBillContract bizBillContract);
//
//    /**
//     * 修改合同收款
//     *
//     * @param bizBillContract 合同收款
//     * @return 结果
//     */
//    public int updateBizBillContract(BizBillContract bizBillContract);
//
//    /**
//     * 删除合同收款
//     *
//     * @param bcId 合同收款ID
//     * @return 结果
//     */
//    public int deleteBizBillContractById(Long bcId);
//
//    /**
//     * 批量删除合同收款
//     *
//     * @param bcIds 需要删除的数据ID
//     * @return 结果
//     */
//    public int deleteBizBillContractByIds(String[] bcIds);
//}
