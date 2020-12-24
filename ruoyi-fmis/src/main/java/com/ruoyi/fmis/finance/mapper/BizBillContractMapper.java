package com.ruoyi.fmis.finance.mapper;

import com.ruoyi.fmis.finance.domain.BizBillContract;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 合同收款(合同分解)Mapper接口
 *
 * @author hedong
 * @date 2020-10-22
 */
public interface BizBillContractMapper {
    /**
     * 查询合同收款(合同分解)
     *
     * @param bcId 合同收款(合同分解)ID
     * @return 合同收款(合同分解)
     */
    public BizBillContract selectBizBillContractById(Long bcId);

    public List<BizBillContract> selectBizBillContractByDataIds(@Param("dataIds") Set<Long> dataIds);
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
     * 删除合同收款(合同分解)
     *
     * @param bcId 合同收款(合同分解)ID
     * @return 结果
     */
    public int deleteBizBillContractById(Long bcId);

    /**
     * 批量删除合同收款(合同分解)
     *
     * @param bcIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizBillContractByIds(String[] bcIds);
}
