package com.ruoyi.fmis.careadict.service;

import com.ruoyi.fmis.careadict.domain.BizAreaDict;
import java.util.List;

/**
 * 客户区域字典Service接口
 *
 * @author frank
 * @date 2020-05-28
 */
public interface IBizAreaDictService {
    /**
     * 查询客户区域字典
     *
     * @param areas 客户区域字典ID
     * @return 客户区域字典
     */
    public BizAreaDict selectBizAreaDictById(String areas);

    /**
     * 查询客户区域字典列表
     *
     * @param bizAreaDict 客户区域字典
     * @return 客户区域字典集合
     */
    public List<BizAreaDict> selectBizAreaDictList(BizAreaDict bizAreaDict);

    /**
     * 新增客户区域字典
     *
     * @param bizAreaDict 客户区域字典
     * @return 结果
     */
    public int insertBizAreaDict(BizAreaDict bizAreaDict);

    /**
     * 修改客户区域字典
     *
     * @param bizAreaDict 客户区域字典
     * @return 结果
     */
    public int updateBizAreaDict(BizAreaDict bizAreaDict);

    /**
     * 批量删除客户区域字典
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizAreaDictByIds(String ids);

    /**
     * 删除客户区域字典信息
     *
     * @param areas 客户区域字典ID
     * @return 结果
     */
    public int deleteBizAreaDictById(String areas);
}
