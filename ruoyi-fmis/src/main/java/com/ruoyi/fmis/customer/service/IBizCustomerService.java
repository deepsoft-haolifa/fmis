package com.ruoyi.fmis.customer.service;

import com.ruoyi.fmis.customer.domain.BizCustomer;

import java.util.List;
import java.util.Set;

/**
 * 客户Service接口
 *
 * @author frank
 * @date 2020-03-02
 */
public interface IBizCustomerService {
    /**
     * 查询客户
     *
     * @param customerId 客户ID
     * @return 客户
     */
    public BizCustomer selectBizCustomerById(Long customerId);

    /**
     * 查询客户列表
     *
     * @param bizCustomer 客户
     * @return 客户集合
     */
    public List<BizCustomer> selectBizCustomerList(BizCustomer bizCustomer);
    public List<BizCustomer> selectBizCustomerListByName(BizCustomer bizCustomer);


    public List<BizCustomer> selectBizCustomerListNoAuth(BizCustomer bizCustomer);

    public List<BizCustomer> selectBizCustomerSelfList(BizCustomer bizCustomer);

    /**
     * 新增客户
     *
     * @param bizCustomer 客户
     * @return 结果
     */
    public int insertBizCustomer(BizCustomer bizCustomer);

    /**
     * 修改客户
     *
     * @param bizCustomer 客户
     * @return 结果
     */
    public int updateBizCustomer(BizCustomer bizCustomer);

    /**
     * 批量删除客户
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizCustomerByIds(String ids);

    /**
     * 删除客户信息
     *
     * @param customerId 客户ID
     * @return 结果
     */
    public int deleteBizCustomerById(Long customerId);

    /**
     * 更新客户类别任务
     */
    public void updateCustomerLeverJob();

    /**
     * 所有客户列表
     */
    public List<BizCustomer> selectCustomerAll(Set<String> customerIdSet);
}
