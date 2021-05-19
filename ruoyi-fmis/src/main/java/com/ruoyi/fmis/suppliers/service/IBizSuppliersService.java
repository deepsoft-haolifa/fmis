package com.ruoyi.fmis.suppliers.service;

import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import java.util.List;

/**
 * 供应商Service接口
 *
 * @author Xianlu Tech
 * @date 2020-03-06
 */
public interface IBizSuppliersService {
    /**
     * 查询供应商
     *
     * @param suppliersId 供应商ID
     * @return 供应商
     */
    public BizSuppliers selectBizSuppliersById(Long suppliersId);

    /**
     * 查询供应商列表
     *
     * @param bizSuppliers 供应商
     * @return 供应商集合
     */
    public List<BizSuppliers> selectBizSuppliersList(BizSuppliers bizSuppliers);
    /**
     * 查询供应商列表
     *
     * @param name
     * 供应商
     * @return 供应商集合
     */
    public List<BizSuppliers> selectBizSuppliersListByName(String name);

    /**
     * 查询所有的供应商 除了删除和停用
     * @return
     */
    public List<BizSuppliers> selectAllList();
    /**
     * 新增供应商
     *
     * @param bizSuppliers 供应商
     * @return 结果
     */
    public int insertBizSuppliers(BizSuppliers bizSuppliers);

    /**
     * 修改供应商
     *
     * @param bizSuppliers 供应商
     * @return 结果
     */
    public int updateBizSuppliers(BizSuppliers bizSuppliers);

    /**
     * 批量删除供应商
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizSuppliersByIds(String ids);

    /**
     * 删除供应商信息
     *
     * @param suppliersId 供应商ID
     * @return 结果
     */
    public int deleteBizSuppliersById(Long suppliersId);
}
