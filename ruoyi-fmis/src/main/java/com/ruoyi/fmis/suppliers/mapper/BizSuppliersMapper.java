package com.ruoyi.fmis.suppliers.mapper;

import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import java.util.List;

/**
 * 供应商Mapper接口
 *
 * @author Xianlu Tech
 * @date 2020-03-06
 */
public interface BizSuppliersMapper {
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
     * @param name 供应商
     * @return 供应商集合
     */
    public List<BizSuppliers> selectBizSuppliersListByName(String name);

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
     * 删除供应商
     *
     * @param suppliersId 供应商ID
     * @return 结果
     */
    public int deleteBizSuppliersById(Long suppliersId);

    /**
     * 批量删除供应商
     *
     * @param suppliersIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizSuppliersByIds(String[] suppliersIds);
}
