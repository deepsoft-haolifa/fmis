package com.ruoyi.fmis.actuator.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.actuator.mapper.BizActuatorMapper;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.common.core.text.Convert;

/**
 * 执行器Service业务层处理
 *
 * @author frank
 * @date 2020-03-17
 */
@Service
public class BizActuatorServiceImpl implements IBizActuatorService {
    @Autowired
    private BizActuatorMapper bizActuatorMapper;

    /**
     * 查询执行器
     *
     * @param actuatorId 执行器ID
     * @return 执行器
     */
    @Override
    public BizActuator selectBizActuatorById(Long actuatorId) {
        return bizActuatorMapper.selectBizActuatorById(actuatorId);
    }

    /**
     * 查询执行器列表
     *
     * @param bizActuator 执行器
     * @return 执行器
     */
    @Override
    public List<BizActuator> selectBizActuatorList(BizActuator bizActuator) {
        return bizActuatorMapper.selectBizActuatorList(bizActuator);
    }

    /**
     * 新增执行器
     *
     * @param bizActuator 执行器
     * @return 结果
     */
    @Override
    public int insertBizActuator(BizActuator bizActuator) {
        bizActuator.setCreateTime(DateUtils.getNowDate());
        return bizActuatorMapper.insertBizActuator(bizActuator);
    }

    /**
     * 修改执行器
     *
     * @param bizActuator 执行器
     * @return 结果
     */
    @Override
    public int updateBizActuator(BizActuator bizActuator) {
        bizActuator.setUpdateTime(DateUtils.getNowDate());
        return bizActuatorMapper.updateBizActuator(bizActuator);
    }

    /**
     * 删除执行器对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizActuatorByIds(String ids) {
        return bizActuatorMapper.deleteBizActuatorByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除执行器信息
     *
     * @param actuatorId 执行器ID
     * @return 结果
     */
    @Override
    public int deleteBizActuatorById(Long actuatorId) {
        return bizActuatorMapper.deleteBizActuatorById(actuatorId);
    }
}
