package com.ruoyi.fmis.define.mapper;

import com.ruoyi.fmis.define.domain.BizProcessDefine;
import java.util.List;

/**
 * 业务流程定义Mapper接口
 *
 * @author frank
 * @date 2020-05-05
 */
public interface BizProcessDefineMapper {
    /**
     * 查询业务流程定义
     *
     * @param defineId 业务流程定义ID
     * @return 业务流程定义
     */
    public BizProcessDefine selectBizProcessDefineById(Long defineId);

    /**
     * 查询业务流程定义列表
     *
     * @param bizProcessDefine 业务流程定义
     * @return 业务流程定义集合
     */
    public List<BizProcessDefine> selectBizProcessDefineList(BizProcessDefine bizProcessDefine);

    /**
     * 新增业务流程定义
     *
     * @param bizProcessDefine 业务流程定义
     * @return 结果
     */
    public int insertBizProcessDefine(BizProcessDefine bizProcessDefine);

    /**
     * 修改业务流程定义
     *
     * @param bizProcessDefine 业务流程定义
     * @return 结果
     */
    public int updateBizProcessDefine(BizProcessDefine bizProcessDefine);

    /**
     * 删除业务流程定义
     *
     * @param defineId 业务流程定义ID
     * @return 结果
     */
    public int deleteBizProcessDefineById(Long defineId);

    /**
     * 批量删除业务流程定义
     *
     * @param defineIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessDefineByIds(String[] defineIds);
}
