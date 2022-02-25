package com.ruoyi.fmis.flow.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.domain.BizProcessDefine;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotation.service.IBizQuotationService;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.service.ISysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.service.IBizFlowService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程记录Controller
 *
 * @author frank
 * @date 2020-03-18
 */
@Controller
@RequestMapping("/fmis/flow")
public class BizFlowController extends BaseController {
    private String prefix = "fmis/flow";

    @Autowired
    private IBizFlowService bizFlowService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private IBizQuotationService bizQuotationService;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @RequiresPermissions("fmis:flow:view")
    @GetMapping()
    public String flow() {
        return prefix + "/flow";
    }

    /**
     * 查询流程记录列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizFlow bizFlow) {
        startPage();
        List<BizFlow> list = bizFlowService.selectBizFlowList(bizFlow);
        return getDataTable(list);
    }


    @PostMapping("/listView")
    @ResponseBody
    public TableDataInfo listView(BizFlow bizFlow) {
        //startPage();
        Long bizId = bizFlow.getBizId();
        String bizTable = bizFlow.getBizTable();
        String flowStatus = "";
        //节点结束
        String normalFlag = "";
        //已存在的角色列表
        Map<String, SysRole> roleMap = new LinkedHashMap<>();
        if ("biz_quotation".equals(bizTable)) {
            //报价单
            BizQuotation bizQuotation = bizQuotationService.selectBizQuotationById(bizId);
            if (bizQuotation != null) {
                flowStatus = bizQuotation.getFlowStatus();
                normalFlag = bizQuotation.getNormalFlag();
            }
        } else {
            BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(bizId);
            if (bizProcessData != null) {
                flowStatus = bizProcessData.getFlowStatus();
                normalFlag = bizProcessData.getNormalFlag();
            }
        }
        BizProcessDefine queryBizProcessDefine = new BizProcessDefine();
        queryBizProcessDefine.setTbName(bizTable);
        List<BizProcessDefine> bizProcessDefineList = bizProcessDefineService.selectBizProcessDefineList(queryBizProcessDefine);
        BizProcessDefine bizProcessDefine = null;
        List<BizFlow> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(bizProcessDefineList)) {
            bizProcessDefine = bizProcessDefineList.get(0);
            String defineFlow = bizProcessDefine.getDefineFlow();
            String defineRole = bizProcessDefine.getDefineRole();
            String[] defineRoles = defineRole.split("-");
            String[] defineFlows = defineFlow.split("-");
            List<BizFlow> list = bizFlowService.selectBizFlowViewList(bizFlow);
            for (int i = 0; i < defineFlows.length; i++) {
                BizFlow bizF = new BizFlow();
                String flowIndex = defineFlows[i];
                bizF.setFlowIndex(flowIndex);
                String roleKey = defineRoles[i];
                SysRole sysRole = sysRoleService.selectRoleByKey(roleKey);
                bizF.setRoleNames(sysRole.getRoleName());
                for (BizFlow flow : list) {
                    String string1 = flow.getString1();
                    if (StringUtils.isNotEmpty(string1)) {
                        if (string1.equals(flowIndex)) {
                            bizF.setFlowId(flow.getFlowId());
                            bizF.setExamineUserName(flow.getExamineUserName());
                            bizF.setFlowStatus(flow.getFlowStatus());
                            bizF.setRemark(flow.getRemark());
                            bizF.setCreateTime(flow.getCreateTime());
                        }
                    } else {
                        Long examineUserId = flow.getExamineUserId();
                        Set<String> roleKeySet = sysRoleService.selectRoleKeys(examineUserId);
                        if (roleKeySet.contains(roleKey)) {
                            bizF.setFlowId(flow.getFlowId());
                            bizF.setExamineUserName(flow.getExamineUserName());
                            bizF.setFlowStatus(flow.getFlowStatus());
                            bizF.setRemark(flow.getRemark());
                            bizF.setCreateTime(flow.getCreateTime());
                        }
                    }
                }
                resultList.add(bizF);
                if (flowIndex.equals(normalFlag)) {
                    break;
                }
            }
        }
        return getDataTable(resultList);
    }

    /**
     * 导出流程记录列表
     */
    @RequiresPermissions("fmis:flow:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizFlow bizFlow) {
        List<BizFlow> list = bizFlowService.selectBizFlowList(bizFlow);
        ExcelUtil<BizFlow> util = new ExcelUtil<BizFlow>(BizFlow.class);
        return util.exportExcel(list, "flow");
    }

    /**
     * 新增流程记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存流程记录
     */
    @RequiresPermissions("fmis:flow:add")
    @Log(title = "流程记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizFlow bizFlow) {
        return toAjax(bizFlowService.insertBizFlow(bizFlow));
    }

    /**
     * 修改流程记录
     */
    @GetMapping("/edit/{flowId}")
    public String edit(@PathVariable("flowId") Long flowId, ModelMap mmap) {
        BizFlow bizFlow = bizFlowService.selectBizFlowById(flowId);
        mmap.put("bizFlow", bizFlow);
        return prefix + "/edit";
    }

    /**
     * 修改保存流程记录
     */
    @RequiresPermissions("fmis:flow:edit")
    @Log(title = "流程记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizFlow bizFlow) {
        return toAjax(bizFlowService.updateBizFlow(bizFlow));
    }

    /**
     * 删除流程记录
     */
    @RequiresPermissions("fmis:flow:remove")
    @Log(title = "流程记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizFlowService.deleteBizFlowByIds(ids));
    }
}
