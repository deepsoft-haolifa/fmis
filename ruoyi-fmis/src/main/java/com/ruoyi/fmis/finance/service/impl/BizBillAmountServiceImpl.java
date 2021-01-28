package com.ruoyi.fmis.finance.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.domain.BizBill;
import com.ruoyi.fmis.finance.mapper.BizBankBillMapper;
import com.ruoyi.fmis.finance.mapper.BizBillMapper;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.fmis.finance.service.IBizBillService;
import com.ruoyi.framework.web.service.DictService;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.finance.mapper.BizBillAmountMapper;
import com.ruoyi.fmis.finance.domain.BizBillAmount;
import com.ruoyi.fmis.finance.service.IBizBillAmountService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 日记账金额Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2021-01-04
 */
@Service
public class BizBillAmountServiceImpl implements IBizBillAmountService {
    @Autowired
    private BizBillAmountMapper bizBillAmountMapper;
    @Autowired
    private BizBankBillMapper bizBankBillMapper;
    @Autowired
    private BizBillMapper bizBillMapper;
    @Autowired
    private ISysDictDataService dictDataService;
    /**
     * 查询日记账金额
     *
     * @param id 日记账金额ID
     * @return 日记账金额
     */
    @Override
    public BizBillAmount selectBizBillAmountById(Long id) {
        return bizBillAmountMapper.selectBizBillAmountById(id);
    }

    /**
     * 查询日记账金额列表
     *
     * @param bizBillAmount 日记账金额
     * @return 日记账金额
     */
    @Override
    public List<BizBillAmount> selectBizBillAmountList(BizBillAmount bizBillAmount) {
        return bizBillAmountMapper.selectBizBillAmountList(bizBillAmount);
    }

    /**
     * 新增日记账金额
     *
     * @param bizBillAmount 日记账金额
     * @return 结果
     */
    @Override
    public int insertBizBillAmount(BizBillAmount bizBillAmount) {
        bizBillAmount.setCreateTime(DateUtils.getNowDate());
        return bizBillAmountMapper.insertBizBillAmount(bizBillAmount);
    }

    /**
     * 修改日记账金额
     *
     * @param bizBillAmount 日记账金额
     * @return 结果
     */
    @Override
    public int updateBizBillAmount(BizBillAmount bizBillAmount) {
        bizBillAmount.setUpdateTime(DateUtils.getNowDate());
        return bizBillAmountMapper.updateBizBillAmount(bizBillAmount);
    }

    /**
     * 删除日记账金额对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizBillAmountByIds(String ids) {
        return bizBillAmountMapper.deleteBizBillAmountByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除日记账金额信息
     *
     * @param id 日记账金额ID
     * @return 结果
     */
    @Override
    public int deleteBizBillAmountById(Long id) {
        return bizBillAmountMapper.deleteBizBillAmountById(id);
    }

    @Override
    public Double getPreMonthAmount(Integer type, String payCompany, String payAccount) {
        BizBillAmount bizBillAmount = new BizBillAmount();
        bizBillAmount.setType(type);
        LocalDateTime now = LocalDateTime.now();
        bizBillAmount.setYear(String.valueOf(now.getYear()));
        bizBillAmount.setMonth(String.valueOf(now.getMonthValue()));
        if (StringUtils.isNotEmpty(payCompany)) {
            bizBillAmount.setCompany(payCompany);
        }
        if (StringUtils.isNotEmpty(payCompany)) {
            bizBillAmount.setAccount(payAccount);
        }

        List<BizBillAmount> bizBillAmountList = bizBillAmountMapper.selectBizBillAmountList(bizBillAmount);
        if (CollectionUtils.isEmpty(bizBillAmountList)) {
            return 0D;
        }
        return bizBillAmountList.get(0).getNextAmount();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBillAmountJob() {
        // 将上个月最后一条记账记录插入到金额
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonthValue());

        // 获取公司和银行的上月结转数据
        List<SysDictData> companyDataList = dictDataService.selectDictDataByType("pay_company");
        List<SysDictData> accountDataList = dictDataService.selectDictDataByType("pay_account");
        for (SysDictData company : companyDataList) {
            String companyValue = company.getDictValue();
            for (SysDictData account : accountDataList) {
                String accountValue = account.getDictValue();
                // 银行日记账,获取
                BizBankBill bankLastRecord = bizBankBillMapper.getLastRecord(companyValue,accountValue);
                if (bankLastRecord != null) {
                    BizBillAmount bizBillAmount = new BizBillAmount();
                    bizBillAmount.setYear(year);
                    bizBillAmount.setCompany(companyValue);
                    bizBillAmount.setAccount(accountValue);
                    bizBillAmount.setMonth(month);
                    bizBillAmount.setType(1);
                    bizBillAmount.setNextAmount(null == bankLastRecord.getBalance() ? 0D : bankLastRecord.getBalance());
                    bizBillAmount.setPreAmount(null == bankLastRecord.getPreMonthMoney() ? 0D : bankLastRecord.getPreMonthMoney());
                    bizBillAmountMapper.insertBizBillAmount(bizBillAmount);
                }
            }
        }



        // 现金日记账
        BizBill billLastRecord = bizBillMapper.getLastRecord();
        if (billLastRecord != null) {
            BizBillAmount bizBillAmount1 = new BizBillAmount();
            bizBillAmount1.setYear(year);
            bizBillAmount1.setMonth(month);
            bizBillAmount1.setType(2);
            bizBillAmount1.setNextAmount(null == billLastRecord.getBalance() ? 0D : billLastRecord.getBalance());
            bizBillAmount1.setPreAmount(null == billLastRecord.getPreMonthMoney() ? 0D : billLastRecord.getPreMonthMoney());
            bizBillAmountMapper.insertBizBillAmount(bizBillAmount1);
        }

    }
}
