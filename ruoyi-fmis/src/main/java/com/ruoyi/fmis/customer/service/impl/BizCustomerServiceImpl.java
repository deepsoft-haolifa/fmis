package com.ruoyi.fmis.customer.service.impl;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.customertrack.domain.BizCustomerTrack;
import com.ruoyi.fmis.customertrack.mapper.BizCustomerTrackMapper;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.mapper.BizProcessDataMapper;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class BizCustomerServiceImpl implements IBizCustomerService {


    @Autowired
    private BizCustomerMapper bizCustomerMapper;

    @Autowired
    private BizProcessDataMapper bizProcessDataMapper;

    @Autowired
    private BizCustomerTrackMapper bizCustomerTrackMapper;
    // 30天
    private final long durationTime = 30 * 24 * 60 * 60 * 1000;

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
     * 查询客户列表
     *
     * @param bizCustomer 客户
     * @return 客户
     */
    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizCustomer> selectBizCustomerListByName(BizCustomer bizCustomer) {
        return bizCustomerMapper.selectBizCustomerListByName(bizCustomer);
    }

    @Override
    public List<BizCustomer> selectBizCustomerListNoAuth(BizCustomer bizCustomer) {
        return bizCustomerMapper.selectBizCustomerList(bizCustomer);
    }

    @Override
    public List<BizCustomer> selectBizCustomerSelfList(BizCustomer bizCustomer) {
        return bizCustomerMapper.selectBizCustomerSelfList(bizCustomer);
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
    public void updateCustomerLeverJob() {
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
                                if (DateUtils.getDatePoorDay(now, recordDateD) >= 365) {
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

    /**
     * 获取客户列表（all）
     */
    @Override
    public List<BizCustomer> selectCustomerAll(Set<String> customerIdSet) {
        return bizCustomerMapper.selectCustomerAll(customerIdSet);
    }

    @Override
    public void cleanCustomerAdmin() {
        // 获取所有已分配的客户
        List<BizCustomer> bizCustomers = bizCustomerMapper.selectBizCustomerListWithAdmin();
        // 判断最近一次回访时间
        if (CollectionUtils.isNotEmpty(bizCustomers)) {
            long currentTime = System.currentTimeMillis();
            for (BizCustomer customer : bizCustomers) {
                try {
                    BizCustomerTrack bizCustomerTrack = new BizCustomerTrack();
                    bizCustomerTrack.setCustomerId(customer.getCustomerId());
                    List<BizCustomerTrack> bizCustomerTracks = bizCustomerTrackMapper.selectBizCustomerTrackList(bizCustomerTrack);
                    if(CollectionUtils.isNotEmpty(bizCustomerTracks)) {
                        // 获取最大的id。
                        int id = 0;
                        Map<Long, List<BizCustomerTrack>> collect = bizCustomerTracks.stream().collect(Collectors.groupingBy(BizCustomerTrack::getTrackId));
                        Optional<Long> reduce = collect.keySet().stream().reduce((a, b) -> a > b ? a : b);
                        BizCustomerTrack bizCustomerTrack1 = collect.get(reduce.get()).get(0);
                        long time = bizCustomerTrack1.getCreateTime().getTime();

                        if(currentTime - time > durationTime) {
                            // 清空
                            bizCustomerMapper.updateBizCustomerOwnerUserId(customer.getCustomerId());
                        }
                    }
                } catch (Exception e) {
                    log.error("schedule clean customer admin error. customer is {}", customer, e);
                }
            }
        }
    }
}
