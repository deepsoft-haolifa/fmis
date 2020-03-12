package com.ruoyi.fmis.dict.mapper;

import com.ruoyi.fmis.dict.domain.BizDict;
import java.util.List;

/**
 * 业务数据字典Mapper接口
 *
 * @author Xianlu Tech
 * @date 2020-03-11
 */
public interface BizDictMapper {
    /**
     * 查询业务数据字典
     *
     * @param dictId 业务数据字典ID
     * @return 业务数据字典
     */
    public BizDict selectBizDictById(Long dictId);

    /**
     * 查询业务数据字典列表
     *
     * @param bizDict 业务数据字典
     * @return 业务数据字典集合
     */
    public List<BizDict> selectBizDictList(BizDict bizDict);

    /**
     * 新增业务数据字典
     *
     * @param bizDict 业务数据字典
     * @return 结果
     */
    public int insertBizDict(BizDict bizDict);

    /**
     * 修改业务数据字典
     *
     * @param bizDict 业务数据字典
     * @return 结果
     */
    public int updateBizDict(BizDict bizDict);

    /**
     * 删除业务数据字典
     *
     * @param dictId 业务数据字典ID
     * @return 结果
     */
    public int deleteBizDictById(Long dictId);

    /**
     * 批量删除业务数据字典
     *
     * @param dictIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizDictByIds(String[] dictIds);
}
