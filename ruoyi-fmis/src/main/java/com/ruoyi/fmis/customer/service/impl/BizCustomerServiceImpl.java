package com.ruoyi.fmis.customer.service.impl;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.mapper.BizProcessDataMapper;
import com.ruoyi.framework.util.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.customer.mapper.BizCustomerMapper;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.common.core.text.Convert;

/**
 * 客户Service业务层处理
 *
 * @author frank
 * @date 2020-03-02
 */
@Service
public class BizCustomerServiceImpl implements IBizCustomerService {


    @Autowired
    private BizCustomerMapper bizCustomerMapper;

    @Autowired
    private BizProcessDataMapper bizProcessDataMapper;
    /**
     * 查询客户
     *
     * @param customerId 客户ID
     * @return 客户
     */
    @Override
    public BizCustomer selectBizCustomerById(Long customerId) {
        return bizCustomerMapper.selectBizCustomerById(customerId);
    }

    /**
     * 查询客户列表
     *
     * @param bizCustomer 客户
     * @return 客户
     */
    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizCustomer> selectBizCustomerList(BizCustomer bizCustomer) {
        return bizCustomerMapper.selectBizCustomerList(bizCustomer);
    }

    /**
     * 新增客户
     *
     * @param bizCustomer 客户
     * @return 结果
     */
    @Override
    public int insertBizCustomer(BizCustomer bizCustomer) {
        Long fileNumberN = bizCustomerMapper.selectCustomerFileNumber();
        if (fileNumberN == null) {
            fileNumberN = 1L;
        }

        bizCustomer.setFileNumber("HL-" + DateUtils.getSysYear() + "-" + new DecimalFormat("0000").format(fileNumberN));
        bizCustomer.setCreateTime(DateUtils.getNowDate());
        bizCustomer.setCreateBy(ShiroUtils.getUserId().toString());
        return bizCustomerMapper.insertBizCustomer(bizCustomer);
    }

    /**
     * 修改客户
     *
     * @param bizCustomer 客户
     * @return 结果
     */
    @Override
    public int updateBizCustomer(BizCustomer bizCustomer) {
        bizCustomer.setUpdateTime(DateUtils.getNowDate());
        bizCustomer.setUpdateBy(ShiroUtils.getUserId().toString());
        return bizCustomerMapper.updateBizCustomer(bizCustomer);
    }

    /**
     * 删除客户对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizCustomerByIds(String ids) {
        return bizCustomerMapper.deleteBizCustomerByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户信息
     *
     * @param customerId 客户ID
     * @return 结果
     */
    @Override
    public int deleteBizCustomerById(Long customerId) {
        return bizCustomerMapper.deleteBizCustomerById(customerId);
    }

    /**
     * 更新客户类别任务
     */
    @Override
    public void updateCustomerLeverJob () {
        BizProcessData queryBizProcessData = new BizProcessData();
        queryBizProcessData.setBizId(BizConstants.BIZ_contract);
        queryBizProcessData.setRoleType("0");
        List<BizProcessData> list = bizProcessDataMapper.selectBizProcessDataListRef(queryBizProcessData);
        Date now = new Date();
        if (CollectionUtils.isNotEmpty(list)) {
            for (BizProcessData bizProcessData : list) {
                String customerId = bizProcessData.getString2();
                if (StringUtils.isNotEmpty(customerId)) {
                    BizCustomer bizCustomer = bizCustomerMapper.selectBizCustomerById(StringUtils.toLong(customerId));
                    if (bizCustomer != null) {
                        String recordDate = bizCustomer.getRecordDate();
                        if (StringUtils.isEmpty(recordDate)) {
                            continue;
                        }
                        String customerLevel = bizCustomer.getCustomerLevel();
                        //成单客户（新）	=1        成单客户（老）=2
                        Date recordDateD = DateUtils.parseDate(recordDate);
                        if (recordDateD != null) {
                            //非无效客户
                            if (!"5".equals(customerLevel)) {
                                bizCustomer.setCustomerLevel("1");
                                if (DateUtils.getDatePoorDay(now,recordDateD) >= 365) {
                                    bizCustomer.setCustomerLevel("2");
                                }
                                bizCustomerMapper.updateBizCustomer(bizCustomer);
                            }
                        }
                    }

                }
            }
        }
    }

}
