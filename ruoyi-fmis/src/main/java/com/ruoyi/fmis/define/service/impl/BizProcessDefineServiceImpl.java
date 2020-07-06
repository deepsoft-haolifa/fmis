package com.ruoyi.fmis.define.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.define.mapper.BizProcessDefineMapper;
import com.ruoyi.fmis.define.domain.BizProcessDefine;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.util.CollectionUtils;

/**
 * 业务流程定义Service业务层处理
 *
 * @author frank
 * @date 2020-05-05
 */
@Service
public class BizProcessDefineServiceImpl implements IBizProcessDefineService {

    @Autowired
    private BizProcessDefineMapper bizProcessDefineMapper;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 查询业务流程定义
     *
     * @param defineId 业务流程定义ID
     * @return 业务流程定义
     */
    @Override
    public BizProcessDefine selectBizProcessDefineById(Long defineId) {
        return bizProcessDefineMapper.selectBizProcessDefineById(defineId);
    }


    /**
     * 返回模块有序流程
     * @param bizId
     * @return <1@process_ywy,role>
     */
    @Override
    public Map<String,SysRole> getFlowAllMap (String bizId) {

        List<SysRole> roleList = sysRoleService.selectRoleListNoAuth(new SysRole());
        //List<SysRole> roleList = sysRoleService.selectRolesByUserId(ShiroUtils.getUserId());

        Map<String,SysRole> flowMap = new LinkedHashMap<>();
        BizProcessDefine queryBizProcessDefine = new BizProcessDefine();
        queryBizProcessDefine.setTbName(bizId);
        List<BizProcessDefine> bizProcessDefineList = bizProcessDefineMapper.selectBizProcessDefineList(queryBizProcessDefine);
        if (!CollectionUtils.isEmpty(bizProcessDefineList)) {
            BizProcessDefine bizProcessDefine = bizProcessDefineList.get(0);
            String defineFlow = bizProcessDefine.getDefineFlow();
            String defineRole = bizProcessDefine.getDefineRole();
            String[] defineFlows = defineFlow.split("-");
            String[] defineRoles = defineRole.split("-");
            for (int i = 0;i < defineFlows.length; i++) {
                String flow = defineFlows[i];
                String role = defineRoles[i];
                if (!CollectionUtils.isEmpty(roleList)) {
                    for (SysRole sysRole : roleList) {
                        String roleKey = sysRole.getRoleKey();
                        if (roleKey.contains(role)) {
                            sysRole.setFlowRoleKey(role);
                            flowMap.put(flow,sysRole);
                            break;
                        }
                    }
                }
            }
        }
        return flowMap;
    }

    /**
     * 返回模块 登录用户的权限
     * @param bizId
     * @return <1,role>
     */
    @Override
    public Map<String,SysRole> getRoleFlowMap (String bizId) {
        Map<String,SysRole> map = new LinkedHashMap<>();
        //流程节点
        Map<String,SysRole> flowAllMap = getFlowAllMap(bizId);

        List<SysRole> userRoles = roleMapper.selectRolesByUserId(ShiroUtils.getUserId());
        for (String key : flowAllMap.keySet()) {
            for (SysRole sysRole : userRoles) {
                String roleKey = sysRole.getRoleKey();
                SysRole keySysRole = flowAllMap.get(key);
                if (roleKey.contains((keySysRole.getFlowRoleKey()))) {
                    map.put(key,sysRole);
                    break;
                }
            }
        }
        return map;
    }

    /**
     * 查询业务流程定义列表
     *
     * @param bizProcessDefine 业务流程定义
     * @return 业务流程定义
     */
    @Override
    public List<BizProcessDefine> selectBizProcessDefineList(BizProcessDefine bizProcessDefine) {
        return bizProcessDefineMapper.selectBizProcessDefineList(bizProcessDefine);
    }

    /**
     * 新增业务流程定义
     *
     * @param bizProcessDefine 业务流程定义
     * @return 结果
     */
    @Override
    public int insertBizProcessDefine(BizProcessDefine bizProcessDefine) {
        bizProcessDefine.setCreateTime(DateUtils.getNowDate());
        return bizProcessDefineMapper.insertBizProcessDefine(bizProcessDefine);
    }

    /**
     * 修改业务流程定义
     *
     * @param bizProcessDefine 业务流程定义
     * @return 结果
     */
    @Override
    public int updateBizProcessDefine(BizProcessDefine bizProcessDefine) {
        bizProcessDefine.setUpdateTime(DateUtils.getNowDate());
        return bizProcessDefineMapper.updateBizProcessDefine(bizProcessDefine);
    }

    /**
     * 删除业务流程定义对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessDefineByIds(String ids) {
        return bizProcessDefineMapper.deleteBizProcessDefineByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务流程定义信息
     *
     * @param defineId 业务流程定义ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessDefineById(Long defineId) {
        return bizProcessDefineMapper.deleteBizProcessDefineById(defineId);
    }
}
