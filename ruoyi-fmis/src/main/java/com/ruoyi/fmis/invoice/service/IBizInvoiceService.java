package com.ruoyi.fmis.invoice.service;

import com.ruoyi.fmis.invoice.bean.BizInvoice;
import java.util.List;

/**
 * 发票信息（进项发票）Service接口
 *
 * @author hedong
 * @date 2021-05-22
 */
public interface IBizInvoiceService {
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
     * 批量删除发票信息（进项发票）
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizInvoiceByIds(String ids);

    /**
     * 删除发票信息（进项发票）信息
     *
     * @param id 发票信息（进项发票）ID
     * @return 结果
     */
    public int deleteBizInvoiceById(Long id);

    /**
     * 导入数据
     *
     * @param list 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    String  importList(List<BizInvoice> list,Boolean isUpdateSupport);
}
