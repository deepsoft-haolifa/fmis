package com.ruoyi.fmis.customer.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
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
}
