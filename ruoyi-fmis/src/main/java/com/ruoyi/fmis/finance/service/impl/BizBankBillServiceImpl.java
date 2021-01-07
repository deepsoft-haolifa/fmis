package com.ruoyi.fmis.finance.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.finance.service.IBizBillAmountService;
import com.ruoyi.framework.util.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.finance.mapper.BizBankBillMapper;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.common.core.text.Convert;

/**
 * 银行日记账Service业务层处理
 *
 * @author murphy.he
 * @date 2020-11-02
 */
@Service
public class BizBankBillServiceImpl implements IBizBankBillService {
    @Autowired
    private BizBankBillMapper bizBankBillMapper;
    @Autowired
    private IBizCustomerService customerService;
    @Autowired
    private IBizBillAmountService bizBillAmountService;

    /**
     * 查询银行日记账
     *
     * @param billId 银行日记账ID
     * @return 银行日记账
     */
    @Override
    public BizBankBill selectBizBankBillById(Long billId) {
        return bizBankBillMapper.selectBizBankBillById(billId);
    }

    /**
     * 查询银行日记账列表
     *
     * @param bizBankBill 银行日记账
     * @return 银行日记账
     */
    @Override
    public List<BizBankBill> selectBizBankBillList(BizBankBill bizBankBill) {
        List<BizBankBill> bizBankBills = bizBankBillMapper.selectBizBankBillList(bizBankBill);
        // 如果是记账是收款；需要转义一下表中的付款单位，也就是客户的名称
        Set<String> customerIdSet = bizBankBills.stream().filter(e -> "1".equals(e.getType())).map(BizBankBill::getPayCompany).collect(Collectors.toSet());
        List<BizCustomer> bizCustomers = customerService.selectCustomerAll(customerIdSet);
        if (CollectionUtils.isNotEmpty(bizCustomers)) {
            Map<Long, String> customerMap = bizCustomers.stream().collect(Collectors.toMap(BizCustomer::getCustomerId, BizCustomer::getName));
            bizBankBills.forEach(e -> {
                if ("1".equals(e.getType())) {
                    e.setPayCompany(customerMap.get(Long.valueOf(e.getPayCompany())));
                }
            });
        }
        return bizBankBills;
    }

    /**
     * 新增银行日记账
     *
     * @param bizBankBill 银行日记账
     * @return 结果
     */
    @Override
    public int insertBizBankBill(BizBankBill bizBankBill) {
        bizBankBill.setCreateTime(DateUtils.getNowDate());
        bizBankBill.setCreateBy(ShiroUtils.getUserId().toString());

        // 设置上月结转
        bizBankBill.setPreMonthMoney(bizBillAmountService.getPreMonthAmount(1).doubleValue());

        // 设置余额 start
        // 查找最新一条记录的余额
        BigDecimal lastBalance = BigDecimal.ZERO;
        BizBankBill lastRecord = bizBankBillMapper.getLastRecord();
        if (lastRecord != null) {
            lastBalance = BigDecimal.valueOf(lastRecord.getBalance());
        }
        // 收款，上次余额 + 本次收款
        if (bizBankBill.getType().equals("1")) {
            Double collectionMoney = bizBankBill.getCollectionMoney();
            double abs = Math.abs(collectionMoney);
            BigDecimal add = BigDecimal.valueOf(abs).add(lastBalance);
            bizBankBill.setBalance(add.doubleValue());
        } else if (bizBankBill.getType().equals("2")) {
            // 付款 , 上次余额 - 本次付款
            Double payment = bizBankBill.getPayment();
            double abs = Math.abs(payment);
            BigDecimal subtract = lastBalance.subtract(BigDecimal.valueOf(abs));
            bizBankBill.setBalance(subtract.doubleValue());
        }
        // 设置余额 end

        return bizBankBillMapper.insertBizBankBill(bizBankBill);
    }

    /**
     * 修改银行日记账
     *
     * @param bizBankBill 银行日记账
     * @return 结果
     */
    @Override
    public int updateBizBankBill(BizBankBill bizBankBill) {
        bizBankBill.setUpdateTime(DateUtils.getNowDate());
        bizBankBill.setUpdateBy(ShiroUtils.getUserId().toString());
        return bizBankBillMapper.updateBizBankBill(bizBankBill);
    }

    /**
     * 删除银行日记账对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizBankBillByIds(String ids) {
        return bizBankBillMapper.deleteBizBankBillByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除银行日记账信息
     *
     * @param billId 银行日记账ID
     * @return 结果
     */
    @Override
    public int deleteBizBankBillById(Long billId) {
        return bizBankBillMapper.deleteBizBankBillById(billId);
    }
}
