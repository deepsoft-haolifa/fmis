package com.ruoyi.fmis.actuatorref.service;

import com.ruoyi.fmis.actuatorref.domain.BizActuatorRef;
import java.util.List;

/**
 * 执行器关系管理Service接口
 *
 * @author frank
 * @date 2020-03-17
 */
public interface IBizActuatorRefService {
    /**
     * 查询执行器关系管理
     *
     * @param arId 执行器关系管理ID
     * @return 执行器关系管理
     */
    public BizActuatorRef selectBizActuatorRefById(Long arId);

    /**
     * 查询执行器关系管理列表
     *
     * @param bizActuatorRef 执行器关系管理
     * @return 执行器关系管理集合
     */
    public List<BizActuatorRef> selectBizActuatorRefList(BizActuatorRef bizActuatorRef);

    /**
     * 新增执行器关系管理
     *
     * @param bizActuatorRef 执行器关系管理
     * @return 结果
     */
    public int insertBizActuatorRef(BizActuatorRef bizActuatorRef);

    /**
     * 修改执行器关系管理
     *
     * @param bizActuatorRef 执行器关系管理
     * @return 结果
     */
    public int updateBizActuatorRef(BizActuatorRef bizActuatorRef);

    /**
     * 批量删除执行器关系管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizActuatorRefByIds(String ids);

    /**
     * 删除执行器关系管理信息
     *
     * @param arId 执行器关系管理ID
     * @return 结果
     */
    public int deleteBizActuatorRefById(Long arId);
}
