package com.ruoyi.fmis.index.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.mapper.BizProcessDataMapper;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.mapper.BizFlowMapper;
import com.ruoyi.fmis.index.domain.Done;
import com.ruoyi.fmis.index.domain.Todo;
import com.ruoyi.fmis.index.dto.OrderAuditDTO;
import com.ruoyi.fmis.index.service.ITodoService;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotation.mapper.BizQuotationMapper;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.fmis.common.BizConstants.*;

@Slf4j
@Service
public class TodoServiceImpl implements ITodoService {

    @Autowired
    private BizFlowMapper bizFlowMapper;

    @Autowired
    private BizQuotationMapper bizQuotationMapper;

    @Autowired
    private BizProcessDataMapper bizProcessDataMapper;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 获取用户待办工单
     * 1.查询用户关联的角色
     * 2.通过角色查询关联的流程节点
     * 3、根据工单类型和待办流程节点查询待办列表
     *
     * 注：不同部门的人员互相数据隔离
     *
     * @param orderAuditDTO
     * @return
     * @throws Exception
     */
    @Override
    public TableDataInfo getPageListForTodo(OrderAuditDTO orderAuditDTO) throws Exception {

        // 获取该流程下当前用户具有审批权限的流程节点
        Map<String, SysRole> roleFlowMap = bizProcessDefineService.getRoleFlowMap(orderAuditDTO.getOrderType());
        // 获取流程节点
        Set<String> flows = roleFlowMap.keySet();

        if (CollectionUtils.isEmpty(flows)) {
            return new TableDataInfo();
        }
        // 获取用户所在部门的所有人，增加创建人筛选
        SysUser sysUser = sysUserService.selectUserById(orderAuditDTO.getUserId());
        Long deptId = sysUser.getDeptId();
        SysUser selectUserByDeptId = new SysUser();
        selectUserByDeptId.setDeptId(deptId);
        List<SysUser> sysUsers = sysUserService.selectUserList(selectUserByDeptId);
        Set<String> userIds = sysUsers.stream().map(t->String.valueOf(t.getUserId())).collect(Collectors.toSet());
        List<Todo> todoList = new ArrayList<>();
        // 其它流程
        TableDataInfo tableDataInfo = new TableDataInfo();
        Set<String> collect = flows.parallelStream().map(Integer::parseInt).map(t -> t - 1).map(String::valueOf).collect(Collectors.toSet());
        if (BIZ_QUOTATION_TABLE.equals(orderAuditDTO.getOrderType())) {
            // 报价单
            PageInfo<BizQuotation> pageInfo = PageHelper.startPage(orderAuditDTO.getPageNum(), orderAuditDTO.getPageSize(), "create_time desc").doSelectPageInfo(() -> bizQuotationMapper.selectBizQuotationByFlowStatus(collect, userIds));
            List<BizQuotation> bizQuotationList = pageInfo.getList();
            tableDataInfo.setTotal(pageInfo.getTotal());
            for (BizQuotation bizQuotation : bizQuotationList) {
                Todo todo = new Todo();
                todo.setOrderNo(bizQuotation.getString1());
                todo.setApplyTime(bizQuotation.getCreateTime());
                todo.setApplyUser(obtainUserName(bizQuotation.getCreateBy()));
                todo.setOrderType(orderAuditDTO.getOrderType());
                todoList.add(todo);
            }
        } else {
            PageInfo<BizProcessData> pageInfo = PageHelper.startPage(orderAuditDTO.getPageNum(), orderAuditDTO.getPageSize(), "create_time desc").doSelectPageInfo(() -> bizProcessDataMapper.selectBizProcessDataByFlowStatus(orderAuditDTO.getOrderType(), collect, userIds));
            List<BizProcessData> bizProcessDatas = pageInfo.getList();
            tableDataInfo.setTotal(pageInfo.getTotal());
            for (BizProcessData bizProcessDatum : bizProcessDatas) {
                Todo todo = new Todo();
                todo.setOrderNo(getOrderNo(bizProcessDatum, orderAuditDTO.getOrderType()));
                todo.setApplyTime(bizProcessDatum.getCreateTime());
                todo.setOrderType(orderAuditDTO.getOrderType());
                todo.setApplyUser(obtainUserName(bizProcessDatum.getCreateBy()));
                todoList.add(todo);
            }
        }


        tableDataInfo.setCode(0);
        tableDataInfo.setRows(todoList);
        return tableDataInfo;
    }

    private String obtainUserName(String createBy) {
        // 获取用户名
        try{
            SysUser sysUser = sysUserService.selectUserById(Long.valueOf(createBy));
            return sysUser.getUserName();
        } catch (Exception e) {
            log.error("sysUser obtain error, userId={}", createBy, e);
        }
        return "";
    }

    /**
     * 获取用户已办理工单
     *
     * @param orderAuditDTO
     * @return
     */
    @Override
    public TableDataInfo getPageListForDone(OrderAuditDTO orderAuditDTO) {
        BizFlow bizFlow = new BizFlow();
        bizFlow.setBizTable(orderAuditDTO.getOrderType());
        bizFlow.setExamineUserId(orderAuditDTO.getUserId());
        PageHelper.startPage(orderAuditDTO.getPageNum(), orderAuditDTO.getPageSize(), "");
        List<BizFlow> bizFlows = bizFlowMapper.selectBizFlowViewList(bizFlow);
        List<Done> doneList = assembleDoneList(bizFlows, orderAuditDTO.getOrderType());
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setTotal(((Page) bizFlows).getTotal());
        tableDataInfo.setCode(0);
        tableDataInfo.setRows(doneList);
        return tableDataInfo;
    }

    /**
     * 封装已办列表
     * 注：报价 与 其它工单原数据不在一张表：报价存在于：biz_quotation 其它：biz_process_data
     *
     * @param bizFlows
     * @param orderType
     * @return
     */
    private List<Done> assembleDoneList(List<BizFlow> bizFlows, String orderType) {
        List<Done> doneList = new ArrayList<>();
        if (BIZ_QUOTATION_TABLE.equals(orderType)) {
            for (BizFlow bizFlow : bizFlows) {
                // 查询报价单单号
                try {
                    BizQuotation bizQuotation = bizQuotationMapper.selectBizQuotationById(bizFlow.getBizId());
                    Done done = new Done();
                    done.setApprovalUser(obtainUserName(bizQuotation.getCreateBy()));
                    done.setAuditTime(bizFlow.getCreateTime());
                    done.setCreateTime(bizQuotation.getCreateTime());
                    done.setOrderNo(bizQuotation.getString1());
                    done.setOrderType(orderType);
                    doneList.add(done);
                } catch (Exception e) {
                    log.error("bizFlow-bizId obj not exists. bizFlow={}", bizFlow, e);
                }

            }
        } else {
            for (BizFlow bizFlow : bizFlows) {
                try {
                    // 查询报价单单号
                    BizProcessData bizProcessData = bizProcessDataMapper.selectBizProcessDataById(bizFlow.getBizId());

                    Done done = new Done();
                    done.setApprovalUser(obtainUserName(bizProcessData.getCreateBy()));
                    done.setAuditTime(bizFlow.getCreateTime());
                    done.setOrderNo(getOrderNo(bizProcessData, orderType));
                    done.setCreateTime(bizProcessData.getCreateTime());
                    done.setOrderType(orderType);
                    doneList.add(done);
                } catch (Exception e) {
                    log.error("bizFlow-bizId obj not exists. bizFlow={}", bizFlow, e);
                }
            }
        }
        return doneList;
    }

    /**
     * 获取不同类型的工单的编号
     *
     * @param bizProcessData
     * @param orderType
     * @return
     */
    private String getOrderNo(BizProcessData bizProcessData, String orderType) {
        switch (orderType) {
            case BIZ_contract:
                return bizProcessData.getString1();
            case BIZ_procurement:
            case BIZ_cpayment:
                return bizProcessData.getString12();
            case BIZ_newdelivery:
            case BIZ_borrowing:
            case BIZ_payment:
                return bizProcessData.getString2();
            case BIZ_invoice:
            default:
                return null;
        }
    }
}
