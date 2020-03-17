package com.ruoyi.fmis.suppliers.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.suppliers.mapper.BizSuppliersMapper;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.common.core.text.Convert;

/**
 * 供应商Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2020-03-06
 */
@Service
public class BizSuppliersServiceImpl implements IBizSuppliersService {
    @Autowired
    private BizSuppliersMapper bizSuppliersMapper;

    /**
     * 查询供应商
     *
     * @param suppliersId 供应商ID
     * @return 供应商
     */
    @Override
    public BizSuppliers selectBizSuppliersById(Long suppliersId) {
        return bizSuppliersMapper.selectBizSuppliersById(suppliersId);
    }

    /**
     * 查询供应商列表
     *
     * @param bizSuppliers 供应商
     * @return 供应商
     */
    @Override
    public List<BizSuppliers> selectBizSuppliersList(BizSuppliers bizSuppliers) {
        return bizSuppliersMapper.selectBizSuppliersList(bizSuppliers);
    }

    @Override
    public List<BizSuppliers> selectAllList() {
        BizSuppliers bizSuppliers = new BizSuppliers();
        bizSuppliers.setDelFlag("0");
        bizSuppliers.setStatus("0");
        return bizSuppliersMapper.selectBizSuppliersList(bizSuppliers);
    }
    /**
     * 新增供应商
     *
     * @param bizSuppliers 供应商
     * @return 结果
     */
    @Override
    public int insertBizSuppliers(BizSuppliers bizSuppliers) {
        bizSuppliers.setCreateTime(DateUtils.getNowDate());
        bizSuppliers.setCreateBy(ShiroUtils.getUserId().toString());
        return bizSuppliersMapper.insertBizSuppliers(bizSuppliers);
    }

    /**
     * 修改供应商
     *
     * @param bizSuppliers 供应商
     * @return 结果
     */
    @Override
    public int updateBizSuppliers(BizSuppliers bizSuppliers) {
        bizSuppliers.setUpdateTime(DateUtils.getNowDate());
        bizSuppliers.setUpdateBy(ShiroUtils.getUserId().toString());
        return bizSuppliersMapper.updateBizSuppliers(bizSuppliers);
    }

    /**
     * 删除供应商对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizSuppliersByIds(String ids) {
        return bizSuppliersMapper.deleteBizSuppliersByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除供应商信息
     *
     * @param suppliersId 供应商ID
     * @return 结果
     */
    @Override
    public int deleteBizSuppliersById(Long suppliersId) {
        return bizSuppliersMapper.deleteBizSuppliersById(suppliersId);
    }
}
