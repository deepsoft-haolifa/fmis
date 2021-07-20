package com.ruoyi.fmis.finance.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.finance.domain.BizBill;
import com.ruoyi.fmis.finance.mapper.BizBillMapper;
import com.ruoyi.fmis.finance.service.IBizBillAmountService;
import com.ruoyi.fmis.finance.service.IBizBillService;
import com.ruoyi.framework.util.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 现金日记账Service业务层处理
 *
 * @author murphy.he
 * @date 2020-11-02
 */
@Service
public class BizBillServiceImpl implements IBizBillService {
    @Autowired
    private BizBillMapper bizBillMapper;
    @Autowired
    private IBizBillAmountService bizBillAmountService;

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

        // 设置上月结转
        bizBill.setPreMonthMoney(bizBillAmountService.getPreMonthAmount(2, null, null));

        // 设置余额 start
        // 查找最新一条记录的余额
        BigDecimal lastBalance = BigDecimal.ZERO;
        BizBill lastRecord = bizBillMapper.getLastRecord();
        if (lastRecord != null) {
            lastBalance = BigDecimal.valueOf(lastRecord.getBalance());
        }
        // 收款，上次余额 + 本次收款
        if (StringUtils.isNotEmpty(bizBill.getCollectionType())) {
            Double collectionMoney = bizBill.getCollectionMoney();
            double abs = Math.abs(collectionMoney);
            BigDecimal add = BigDecimal.valueOf(abs).add(lastBalance);
            bizBill.setBalance(add.doubleValue());
        } else if (StringUtils.isNotEmpty(bizBill.getPaymentType())) {
            // 付款 , 上次余额 - 本次付款
            Double payment = bizBill.getPayment();
            double abs = Math.abs(payment);
            BigDecimal subtract = lastBalance.subtract(BigDecimal.valueOf(abs));
            if (subtract.compareTo(BigDecimal.ZERO) < 0) {
                throw new BaseException("现金余额不足以付款");
            }
            bizBill.setBalance(subtract.doubleValue());
        }
        // 设置余额 end


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

    @Override
    public boolean existsByCertificateNumber(String certificateNumber) {
        List<BizBill> bizBills = bizBillMapper.selectBizBillList(new BizBill() {{
            setCertificateNumber(certificateNumber);
        }});
        return CollectionUtils.isNotEmpty(bizBills);
    }
}
