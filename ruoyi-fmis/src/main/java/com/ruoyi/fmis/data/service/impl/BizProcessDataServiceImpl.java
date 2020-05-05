package com.ruoyi.fmis.data.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.mapper.BizFlowMapper;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.data.mapper.BizProcessDataMapper;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.util.CollectionUtils;

/**
 * 合同管理Service业务层处理
 *
 * @author frank
 * @date 2020-05-05
 */
@Service
public class BizProcessDataServiceImpl implements IBizProcessDataService {

    @Autowired
    private BizProcessDataMapper bizProcessDataMapper;

    @Autowired
    private BizFlowMapper bizFlowMapper;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;
    /**
     * 查询合同管理
     *
     * @param dataId 合同管理ID
     * @return 合同管理
     */
    @Override
    public BizProcessData selectBizProcessDataById(Long dataId) {
        return bizProcessDataMapper.selectBizProcessDataById(dataId);
    }

    /**
     * 查询合同管理列表
     *
     * @param bizProcessData 合同管理
     * @return 合同管理
     */
    @Override
    public List<BizProcessData> selectBizProcessDataList(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataList(bizProcessData);
    }

    @Override
    public List<BizProcessData> selectBizProcessDataListRef(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRef(bizProcessData);
    }

    @Override
    public List<BizProcessData> selectBizProcessDataListRefDelivery(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefDelivery(bizProcessData);
    }


    /**
     * 审批
     * @param dataId
     * @param status
     * @param remark
     * @return
     */
    @Override
    public int doExamine(String dataId,String status,String remark,String bizId) {

        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        //Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);

        BizProcessData bizProcessData = bizProcessDataMapper.selectBizProcessDataById(Long.parseLong(dataId));
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        bizProcessData.setUpdateBy(ShiroUtils.getUserId().toString());
        //1=同意 -1=不同意

        //1=销售 2=销售经理 3=区域经理 4=副总 5=总经理
        //流程状态0=未上报  1=销售员上报  2=销售经理同意 -2=销售经理不同意 3=区域经理同意 -3=区域经理不同意 4=副总同意 -4=副总不同意 5=老总同意 -5=老总不同意
        //int roleType = roleService.getRoleType(ShiroUtils.getUserId());

        String flowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            String currentUserFlowStatus = flowMap.keySet().iterator().next();
            if (status.equals("1")) {
                flowStatus = currentUserFlowStatus;
            } else {
                flowStatus = "-" + currentUserFlowStatus;
            }
        }
        bizProcessData.setFlowStatus(flowStatus);
        int updateCount = bizProcessDataMapper.updateBizProcessData(bizProcessData);
        BizFlow bizFlow = new BizFlow();
        bizFlow.setCreateTime(DateUtils.getNowDate());
        bizFlow.setCreateBy(ShiroUtils.getUserId().toString());
        bizFlow.setBizId(bizProcessData.getDataId());
        bizFlow.setBizTable(bizId);
        bizFlow.setExamineUserId(ShiroUtils.getUserId());
        bizFlow.setRemark(remark);
        bizFlow.setFlowStatus(status);
        bizFlowMapper.insertBizFlow(bizFlow);

        return updateCount;
    }


    /**
     * 新增合同管理
     *
     * @param bizProcessData 合同管理
     * @return 结果
     */
    @Override
    public int insertBizProcessData(BizProcessData bizProcessData) {
        bizProcessData.setCreateTime(DateUtils.getNowDate());
        return bizProcessDataMapper.insertBizProcessData(bizProcessData);
    }

    /**
     * 修改合同管理
     *
     * @param bizProcessData 合同管理
     * @return 结果
     */
    @Override
    public int updateBizProcessData(BizProcessData bizProcessData) {
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        return bizProcessDataMapper.updateBizProcessData(bizProcessData);
    }

    /**
     * 删除合同管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessDataByIds(String ids) {
        return bizProcessDataMapper.deleteBizProcessDataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除合同管理信息
     *
     * @param dataId 合同管理ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessDataById(Long dataId) {
        return bizProcessDataMapper.deleteBizProcessDataById(dataId);
    }

    @Override
    public int subReportBizQuotation(BizProcessData bizProcessData) {
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        bizProcessData.setUpdateBy(ShiroUtils.getUserId().toString());
        bizProcessData.setFlowStatus(BizConstants.FLOW_STATUS_1);
        int updateCount = bizProcessDataMapper.updateBizProcessData(bizProcessData);
        insertFlow(bizProcessData);
        return updateCount;
    }
    /**
     * 上报报价单
     */
    public void insertFlow (BizProcessData bizProcessData) {
        BizFlow bizFlow = new BizFlow();
        bizFlow.setCreateTime(DateUtils.getNowDate());
        bizFlow.setCreateBy(ShiroUtils.getUserId().toString());
        bizFlow.setBizId(bizProcessData.getDataId());
        bizFlow.setBizTable(bizProcessData.getBizId());
        bizFlow.setExamineUserId(ShiroUtils.getUserId());
        bizFlow.setFlowStatus(BizConstants.FLOW_STATUS_0);
        bizFlowMapper.insertBizFlow(bizFlow);
    }
}
