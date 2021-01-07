package com.ruoyi.fmis.finance.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.finance.mapper.BizBillAmountMapper;
import com.ruoyi.fmis.finance.domain.BizBillAmount;
import com.ruoyi.fmis.finance.service.IBizBillAmountService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.util.CollectionUtils;

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
    public BigDecimal getPreMonthAmount(Integer type) {
        BizBillAmount bizBillAmount = new BizBillAmount();
        bizBillAmount.setType(type);
        LocalDateTime now = LocalDateTime.now();
        bizBillAmount.setYear(String.valueOf(now.getYear()));
        bizBillAmount.setMonth(String.valueOf(now.getMonthValue()));
        List<BizBillAmount> bizBillAmountList = bizBillAmountMapper.selectBizBillAmountList(bizBillAmount);
        if (CollectionUtils.isEmpty(bizBillAmountList)) {
            return BigDecimal.ZERO;
        }
        return bizBillAmountList.get(0).getNextAmount();
    }
}
