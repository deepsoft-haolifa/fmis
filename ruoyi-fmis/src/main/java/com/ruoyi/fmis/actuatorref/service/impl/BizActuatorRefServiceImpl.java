package com.ruoyi.fmis.actuatorref.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.actuatorref.mapper.BizActuatorRefMapper;
import com.ruoyi.fmis.actuatorref.domain.BizActuatorRef;
import com.ruoyi.fmis.actuatorref.service.IBizActuatorRefService;
import com.ruoyi.common.core.text.Convert;

/**
 * 执行器关系管理Service业务层处理
 *
 * @author frank
 * @date 2020-03-17
 */
@Service
public class BizActuatorRefServiceImpl implements IBizActuatorRefService {
    @Autowired
    private BizActuatorRefMapper bizActuatorRefMapper;

    /**
     * 查询执行器关系管理
     *
     * @param arId 执行器关系管理ID
     * @return 执行器关系管理
     */
    @Override
    public BizActuatorRef selectBizActuatorRefById(Long arId) {
        return bizActuatorRefMapper.selectBizActuatorRefById(arId);
    }

    /**
     * 查询执行器关系管理列表
     *
     * @param bizActuatorRef 执行器关系管理
     * @return 执行器关系管理
     */
    @Override
    public List<BizActuatorRef> selectBizActuatorRefList(BizActuatorRef bizActuatorRef) {
        return bizActuatorRefMapper.selectBizActuatorRefList(bizActuatorRef);
    }

    /**
     * 新增执行器关系管理
     *
     * @param bizActuatorRef 执行器关系管理
     * @return 结果
     */
    @Override
    public int insertBizActuatorRef(BizActuatorRef bizActuatorRef) {
        bizActuatorRef.setCreateTime(DateUtils.getNowDate());
        return bizActuatorRefMapper.insertBizActuatorRef(bizActuatorRef);
    }

    /**
     * 修改执行器关系管理
     *
     * @param bizActuatorRef 执行器关系管理
     * @return 结果
     */
    @Override
    public int updateBizActuatorRef(BizActuatorRef bizActuatorRef) {
        bizActuatorRef.setUpdateTime(DateUtils.getNowDate());
        return bizActuatorRefMapper.updateBizActuatorRef(bizActuatorRef);
    }

    /**
     * 删除执行器关系管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizActuatorRefByIds(String ids) {
        return bizActuatorRefMapper.deleteBizActuatorRefByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除执行器关系管理信息
     *
     * @param arId 执行器关系管理ID
     * @return 结果
     */
    @Override
    public int deleteBizActuatorRefById(Long arId) {
        return bizActuatorRefMapper.deleteBizActuatorRefById(arId);
    }
}
