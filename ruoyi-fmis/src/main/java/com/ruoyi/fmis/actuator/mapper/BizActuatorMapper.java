package com.ruoyi.fmis.actuator.mapper;

import com.ruoyi.fmis.actuator.domain.BizActuator;
import java.util.List;

/**
 * 执行器Mapper接口
 *
 * @author frank
 * @date 2020-03-17
 */
public interface BizActuatorMapper {
    /**
     * 查询执行器
     *
     * @param actuatorId 执行器ID
     * @return 执行器
     */
    public BizActuator selectBizActuatorById(Long actuatorId);

    /**
     * 查询执行器列表
     *
     * @param bizActuator 执行器
     * @return 执行器集合
     */
    public List<BizActuator> selectBizActuatorList(BizActuator bizActuator);

    /**
     * 查询执行器列表 通过关系查询
     *
     * @param bizActuator 执行器
     * @return 执行器集合
     */
    public List<BizActuator> selectBizActuatorForRefList(BizActuator bizActuator);

    /**
     * 新增执行器
     *
     * @param bizActuator 执行器
     * @return 结果
     */
    public int insertBizActuator(BizActuator bizActuator);

    /**
     * 修改执行器
     *
     * @param bizActuator 执行器
     * @return 结果
     */
    public int updateBizActuator(BizActuator bizActuator);

    /**
     * 删除执行器
     *
     * @param actuatorId 执行器ID
     * @return 结果
     */
    public int deleteBizActuatorById(Long actuatorId);

    /**
     * 批量删除执行器
     *
     * @param actuatorIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizActuatorByIds(String[] actuatorIds);
}
