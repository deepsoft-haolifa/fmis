package com.ruoyi.fmis.customer.mapper;

import com.ruoyi.fmis.customer.domain.BizCustomer;
import java.util.List;

/**
 * 客户Mapper接口
 *
 * @author frank
 * @date 2020-03-02
 */
public interface BizCustomerMapper {
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
     * 删除客户
     *
     * @param customerId 客户ID
     * @return 结果
     */
    public int deleteBizCustomerById(Long customerId);

    /**
     * 批量删除客户
     *
     * @param customerIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizCustomerByIds(String[] customerIds);


    public Long selectCustomerFileNumber();
}
