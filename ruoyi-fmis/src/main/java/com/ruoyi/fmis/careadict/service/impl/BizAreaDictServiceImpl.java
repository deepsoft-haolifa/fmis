package com.ruoyi.fmis.careadict.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.careadict.mapper.BizAreaDictMapper;
import com.ruoyi.fmis.careadict.domain.BizAreaDict;
import com.ruoyi.fmis.careadict.service.IBizAreaDictService;
import com.ruoyi.common.core.text.Convert;

/**
 * 客户区域字典Service业务层处理
 *
 * @author frank
 * @date 2020-05-28
 */
@Service
public class BizAreaDictServiceImpl implements IBizAreaDictService {
    @Autowired
    private BizAreaDictMapper bizAreaDictMapper;

    /**
     * 查询客户区域字典
     *
     * @param areas 客户区域字典ID
     * @return 客户区域字典
     */
    @Override
    public BizAreaDict selectBizAreaDictById(String areas) {
        return bizAreaDictMapper.selectBizAreaDictById(areas);
    }

    /**
     * 查询客户区域字典列表
     *
     * @param bizAreaDict 客户区域字典
     * @return 客户区域字典
     */
    @Override
    public List<BizAreaDict> selectBizAreaDictList(BizAreaDict bizAreaDict) {
        return bizAreaDictMapper.selectBizAreaDictList(bizAreaDict);
    }

    /**
     * 新增客户区域字典
     *
     * @param bizAreaDict 客户区域字典
     * @return 结果
     */
    @Override
    public int insertBizAreaDict(BizAreaDict bizAreaDict) {
        return bizAreaDictMapper.insertBizAreaDict(bizAreaDict);
    }

    /**
     * 修改客户区域字典
     *
     * @param bizAreaDict 客户区域字典
     * @return 结果
     */
    @Override
    public int updateBizAreaDict(BizAreaDict bizAreaDict) {
        return bizAreaDictMapper.updateBizAreaDict(bizAreaDict);
    }

    /**
     * 删除客户区域字典对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizAreaDictByIds(String ids) {
        return bizAreaDictMapper.deleteBizAreaDictByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户区域字典信息
     *
     * @param areas 客户区域字典ID
     * @return 结果
     */
    @Override
    public int deleteBizAreaDictById(String areas) {
        return bizAreaDictMapper.deleteBizAreaDictById(areas);
    }
}
