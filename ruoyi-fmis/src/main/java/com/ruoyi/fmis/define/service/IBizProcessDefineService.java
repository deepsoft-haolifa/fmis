package com.ruoyi.fmis.define.service;

import com.ruoyi.fmis.define.domain.BizProcessDefine;
import com.ruoyi.system.domain.SysRole;

import java.util.List;
import java.util.Map;

/**
 * 业务流程定义Service接口
 *
 * @author frank
 * @date 2020-05-05
 */
public interface IBizProcessDefineService {
    /**
     * 查询业务流程定义
     *
     * @param defineId 业务流程定义ID
     * @return 业务流程定义
     */
    public BizProcessDefine selectBizProcessDefineById(Long defineId);


    public Map<String,SysRole> getFlowAllMap (String bizId);

    public Map<String, SysRole> getRoleFlowMap (String bizId);
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
     * 批量删除业务流程定义
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessDefineByIds(String ids);

    /**
     * 删除业务流程定义信息
     *
     * @param defineId 业务流程定义ID
     * @return 结果
     */
    public int deleteBizProcessDefineById(Long defineId);



}
