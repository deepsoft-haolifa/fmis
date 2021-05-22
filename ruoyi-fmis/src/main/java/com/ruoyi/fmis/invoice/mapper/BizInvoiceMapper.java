package com.ruoyi.fmis.invoice.mapper;

import com.ruoyi.fmis.invoice.bean.BizInvoice;
import java.util.List;

/**
 * 发票信息（进项发票）Mapper接口
 *
 * @author hedong
 * @date 2021-05-22
 */
public interface BizInvoiceMapper {
    /**
     * 查询发票信息（进项发票）
     *
     * @param id 发票信息（进项发票）ID
     * @return 发票信息（进项发票）
     */
    public BizInvoice selectBizInvoiceById(Long id);

    /**
     * 查询发票信息（进项发票）列表
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 发票信息（进项发票）集合
     */
    public List<BizInvoice> selectBizInvoiceList(BizInvoice bizInvoice);

    /**
     * 新增发票信息（进项发票）
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 结果
     */
    public int insertBizInvoice(BizInvoice bizInvoice);

    /**
     * 修改发票信息（进项发票）
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 结果
     */
    public int updateBizInvoice(BizInvoice bizInvoice);

    /**
     * 删除发票信息（进项发票）
     *
     * @param id 发票信息（进项发票）ID
     * @return 结果
     */
    public int deleteBizInvoiceById(Long id);

    /**
     * 批量删除发票信息（进项发票）
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizInvoiceByIds(String[] ids);
}
