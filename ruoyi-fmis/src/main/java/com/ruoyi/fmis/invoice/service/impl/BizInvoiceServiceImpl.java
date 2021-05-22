package com.ruoyi.fmis.invoice.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.invoice.mapper.BizInvoiceMapper;
import com.ruoyi.fmis.invoice.bean.BizInvoice;
import com.ruoyi.fmis.invoice.service.IBizInvoiceService;
import com.ruoyi.common.core.text.Convert;

/**
 * 发票信息（进项发票）Service业务层处理
 *
 * @author hedong
 * @date 2021-05-22
 */
@Service
public class BizInvoiceServiceImpl implements IBizInvoiceService {
    @Autowired
    private BizInvoiceMapper bizInvoiceMapper;

    /**
     * 查询发票信息（进项发票）
     *
     * @param id 发票信息（进项发票）ID
     * @return 发票信息（进项发票）
     */
    @Override
    public BizInvoice selectBizInvoiceById(Long id) {
        return bizInvoiceMapper.selectBizInvoiceById(id);
    }

    /**
     * 查询发票信息（进项发票）列表
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 发票信息（进项发票）
     */
    @Override
    public List<BizInvoice> selectBizInvoiceList(BizInvoice bizInvoice) {
        return bizInvoiceMapper.selectBizInvoiceList(bizInvoice);
    }

    /**
     * 新增发票信息（进项发票）
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 结果
     */
    @Override
    public int insertBizInvoice(BizInvoice bizInvoice) {
        bizInvoice.setCreateTime(DateUtils.getNowDate());
        return bizInvoiceMapper.insertBizInvoice(bizInvoice);
    }

    /**
     * 修改发票信息（进项发票）
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 结果
     */
    @Override
    public int updateBizInvoice(BizInvoice bizInvoice) {
        bizInvoice.setUpdateTime(DateUtils.getNowDate());
        return bizInvoiceMapper.updateBizInvoice(bizInvoice);
    }

    /**
     * 删除发票信息（进项发票）对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizInvoiceByIds(String ids) {
        return bizInvoiceMapper.deleteBizInvoiceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除发票信息（进项发票）信息
     *
     * @param id 发票信息（进项发票）ID
     * @return 结果
     */
    @Override
    public int deleteBizInvoiceById(Long id) {
        return bizInvoiceMapper.deleteBizInvoiceById(id);
    }
}
