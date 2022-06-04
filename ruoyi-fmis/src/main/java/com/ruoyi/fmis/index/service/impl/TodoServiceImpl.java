package com.ruoyi.fmis.index.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.mapper.BizProcessDataMapper;
import com.ruoyi.fmis.data.service.impl.BizProcessDataServiceImpl;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.mapper.BizFlowMapper;
import com.ruoyi.fmis.index.domain.Done;
import com.ruoyi.fmis.index.domain.Todo;
import com.ruoyi.fmis.index.dto.OrderAuditDTO;
import com.ruoyi.fmis.index.service.ITodoService;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotation.mapper.BizQuotationMapper;
import com.ruoyi.fmis.quotation.service.impl.BizQuotationServiceImpl;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    @Autowired
    private BizProcessDataServiceImpl bizProcessDataService;

    @Autowired
    private BizQuotationServiceImpl bizQuotationService;

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
        Set<String> flows = new HashSet<>();
        HashMap<String,Set<String>> flowConfig = new HashMap<>();
        // 查询全部
        if(orderAuditDTO.getOrderType().equals("-1")) {
            HashMap<String, String> orderTypeMaps = getOrderTypeMaps();
            for (String orderType : orderTypeMaps.keySet()) {
                Set<String> collect = getQueryFlowStatusSet(orderType);
                if(!CollectionUtils.isEmpty(collect)) {
                    flowConfig.put(orderType, collect);
                }
            }
        } else {
            Set<String> collect = getQueryFlowStatusSet(orderAuditDTO.getOrderType());
            flows.addAll(collect);
        }
        List<Todo> todoList = new ArrayList<>();
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(0);
        tableDataInfo.setRows(todoList);
        if (CollectionUtils.isEmpty(flows) && CollectionUtils.isEmpty(flowConfig)) {
            return tableDataInfo;
        }
        if (BIZ_QUOTATION_TABLE.equals(orderAuditDTO.getOrderType())) {
            // 报价单
            BizQuotation bizQuotationTodo = new BizQuotation();
            bizQuotationTodo.setFlows(flows);
            PageInfo<BizQuotation> pageInfo = PageHelper.startPage(orderAuditDTO.getPageNum(), orderAuditDTO.getPageSize(), "q.create_time desc").doSelectPageInfo(() -> bizQuotationService.selectBizQuotationByFlowStatus(bizQuotationTodo));
            List<BizQuotation> bizQuotationList = pageInfo.getList();
            tableDataInfo.setTotal(pageInfo.getTotal());
            for (BizQuotation bizQuotation : bizQuotationList) {
                if (bizQuotation.getNormalFlag().equals(bizQuotation.getFlowStatus())) {
                    continue;
                }
                if (!StringUtils.isEmpty(bizQuotation.getFlowStatus()) && Integer.parseInt(bizQuotation.getFlowStatus()) == 0) {
                    continue;
                }
                Todo todo = new Todo();
                todo.setId(bizQuotation.getQuotationId());
                todo.setOrderNo(bizQuotation.getString1());
                todo.setApplyTime(bizQuotation.getCreateTime());
                todo.setApplyUser(obtainUserName(bizQuotation.getCreateBy()));
                todo.setOrderType(orderAuditDTO.getOrderType());
                todoList.add(todo);
            }
        } else {
            BizProcessData bizProcessData = new BizProcessData();
            bizProcessData.setBizId(orderAuditDTO.getOrderType());
            bizProcessData.setFlows(flows);
            bizProcessData.setFlowConfig(flowConfig);
            PageInfo<BizProcessData> pageInfo = PageHelper.startPage(orderAuditDTO.getPageNum(), orderAuditDTO.getPageSize(), "d.create_time desc").doSelectPageInfo(() -> bizProcessDataService.selectBizProcessDataForTodo(bizProcessData));
            List<BizProcessData> bizProcessDatas = pageInfo.getList();
            tableDataInfo.setTotal(pageInfo.getTotal());
            for (BizProcessData bizProcessDatum : bizProcessDatas) {
                if (bizProcessDatum.getNormalFlag().equals(bizProcessDatum.getFlowStatus())) {
                    continue;
                }
                if (!StringUtils.isEmpty(bizProcessDatum.getFlowStatus()) && Integer.parseInt(bizProcessDatum.getFlowStatus()) == 0) {
                    continue;
                }
                Todo todo = new Todo();
                todo.setId(bizProcessDatum.getDataId());
                todo.setOrderNo(getOrderNo(bizProcessDatum, bizProcessDatum.getBizId()));
                todo.setApplyTime(bizProcessDatum.getCreateTime());
                todo.setOrderType(bizProcessDatum.getBizId());
                todo.setApplyUser(obtainUserName(bizProcessDatum.getCreateBy()));
                todoList.add(todo);
            }
        }
        tableDataInfo.setRows(todoList);
        return tableDataInfo;
    }

    private Set<String> getQueryFlowStatusSet(String orderType) {
        Map<String, SysRole> roleFlowMap = bizProcessDefineService.getRoleFlowMap(orderType);
        Set<String> strings = roleFlowMap.keySet();
        return strings.parallelStream().map(Integer::parseInt).map(t -> t - 1).map(String::valueOf).collect(Collectors.toSet());
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

    @Override
    public HashMap<String, String> getOrderTypeMaps() {
        HashMap<String, String> orderTypes = new HashMap<>();
        orderTypes.put("contract", "销售合同工单");
        orderTypes.put("procurement", "采购合同工单");
        orderTypes.put("cpayment", "付款审批工单");
        orderTypes.put("newdelivery", "发货审批工单");
        orderTypes.put("borrowing", "借款审批工单");
        orderTypes.put("payment", "报销审批工单");
        orderTypes.put("invoice", "发票审批工单");
        return orderTypes;
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
                    if (!bizQuotation.getNormalFlag().equals(bizQuotation.getFlowStatus())) {
                        continue;
                    }
                    Done done = new Done();
                    done.setId(bizFlow.getBizId());
                    if(Objects.isNull(bizQuotation)) {
                        done.setAuditTime(bizFlow.getCreateTime());
                        done.setOrderType(orderType);
                        log.warn("bizFlow-bizId obj not exists. bizFlow={}", bizFlow);
                    } else {
                        done.setApprovalUser(obtainUserName(bizQuotation.getCreateBy()));
                        done.setAuditTime(bizFlow.getCreateTime());
                        done.setCreateTime(bizQuotation.getCreateTime());
                        done.setOrderNo(bizQuotation.getString1());
                        done.setOrderType(orderType);
                    }
                    doneList.add(done);
                } catch (Exception e) {
                    log.error("bizFlow-bizId obj query exception. bizFlow={}", bizFlow, e);
                }

            }
        } else {
            for (BizFlow bizFlow : bizFlows) {
                try {
                    // 查询报价单单号
                    Done done = new Done();
                    done.setId(bizFlow.getBizId());
                    BizProcessData bizProcessData = bizProcessDataMapper.selectBizProcessDataById(bizFlow.getBizId());
                    if(Objects.isNull(bizProcessData)) {
                        done.setAuditTime(bizFlow.getCreateTime());
                        done.setOrderType(bizFlow.getBizTable());
                        log.warn("bizFlow-bizId obj not exists. bizFlow={}", bizFlow);
                    } else {
                        done.setApprovalUser(obtainUserName(bizProcessData.getCreateBy()));
                        done.setAuditTime(bizFlow.getCreateTime());
                        done.setOrderNo(getOrderNo(bizProcessData, bizFlow.getBizTable()));
                        done.setCreateTime(bizProcessData.getCreateTime());
                        done.setOrderType(bizFlow.getBizTable());
                    }
                    doneList.add(done);
                } catch (Exception e) {
                    log.error("bizFlow-bizId obj query exception. bizFlow={}", bizFlow, e);
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
