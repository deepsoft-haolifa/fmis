package com.ruoyi.fmis.finance.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.bill.domain.BizBill;
import com.ruoyi.fmis.finance.mapper.BizBillMapper;
import com.ruoyi.fmis.finance.service.IBizBillService;
import com.ruoyi.framework.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 现金日记账Service业务层处理
 *
 * @author frank
 * @date 2020-06-25
 */
@Service
public class BizBillServiceImpl implements IBizBillService {
    @Autowired
    private BizBillMapper bizBillMapper;

    /**
     * 查询现金日记账
     *
     * @param billId 现金日记账ID
     * @return 现金日记账
     */
    @Override
    public BizBill selectBizBillById(Long billId) {
        return bizBillMapper.selectBizBillById(billId);
    }

    /**
     * 查询现金日记账列表
     *
     * @param bizBill 现金日记账
     * @return 现金日记账
     */
    @Override
    public List<BizBill> selectBizBillList(BizBill bizBill) {
        return bizBillMapper.selectBizBillList(bizBill);
    }

    /**
     * 新增现金日记账
     *
     * @param bizBill 现金日记账
     * @return 结果
     */
    @Override
    public int insertBizBill(BizBill bizBill) {
        bizBill.setCreateTime(DateUtils.getNowDate());
        bizBill.setCreateBy(ShiroUtils.getUserId().toString());
        return bizBillMapper.insertBizBill(bizBill);
    }

    /**
     * 修改现金日记账
     *
     * @param bizBill 现金日记账
     * @return 结果
     */
    @Override
    public int updateBizBill(BizBill bizBill) {
        bizBill.setUpdateTime(DateUtils.getNowDate());
        bizBill.setUpdateBy(ShiroUtils.getUserId().toString());
        return bizBillMapper.updateBizBill(bizBill);
    }

    /**
     * 删除现金日记账对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizBillByIds(String ids) {
        return bizBillMapper.deleteBizBillByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除现金日记账信息
     *
     * @param billId 现金日记账ID
     * @return 结果
     */
    @Override
    public int deleteBizBillById(Long billId) {
        return bizBillMapper.deleteBizBillById(billId);
    }
}
