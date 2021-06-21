package com.ruoyi.fmis.finance.mapper;

import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.domain.vo.export.BizBankExportDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 银行日记账Mapper接口
 *
 * @author murphy.he
 * @date 2020-11-02
 */
public interface BizBankBillMapper {
    /**
     * 查询银行日记账
     *
     * @param billId 银行日记账ID
     * @return 银行日记账
     */
    public BizBankBill selectBizBankBillById(Long billId);

    /**
     * 查询银行日记账列表
     *
     * @param bizBankBill 银行日记账
     * @return 银行日记账集合
     */
    public List<BizBankBill> selectBizBankBillList(BizBankBill bizBankBill);

    /**
     * 新增银行日记账
     *
     * @param bizBankBill 银行日记账
     * @return 结果
     */
    public int insertBizBankBill(BizBankBill bizBankBill);

    /**
     * 修改银行日记账
     *
     * @param bizBankBill 银行日记账
     * @return 结果
     */
    public int updateBizBankBill(BizBankBill bizBankBill);

    /**
     * 删除银行日记账
     *
     * @param billId 银行日记账ID
     * @return 结果
     */
    public int deleteBizBankBillById(Long billId);

    /**
     * 批量删除银行日记账
     *
     * @param billIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizBankBillByIds(String[] billIds);

    /**
     * 获取最新一条数据
     * @return
     */
    BizBankBill getLastRecord(@Param("companyQuery") String companyQuery, @Param("accountQuery") String accountQuery);

    List<BizBankExportDTO> selectyyExport(BizBankBill bizBankBill);

}
