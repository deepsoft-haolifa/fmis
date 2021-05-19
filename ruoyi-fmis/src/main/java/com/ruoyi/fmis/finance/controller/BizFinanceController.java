package com.ruoyi.fmis.finance.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.fmis.finance.domain.vo.*;
import com.ruoyi.fmis.finance.service.IBizFinanceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 往来管理
 *
 * @author murphy
 * @date 2020-10-13
 */
@Controller
@RequestMapping("/finance")
public class BizFinanceController extends BaseController {
    private String prefix = "finance";

    @Autowired
    private IBizFinanceService bizFinanceService;

    @GetMapping("/receivable")
    @RequiresPermissions("finance:receivable:view")
    public String receivable(ModelMap map) {
        map.put("customerId", getRequest().getParameter("customerId"));
        return prefix + "/receivable/receivable";
    }

    /**
     * 查询应收列表(销售合同列表)
     */
    @RequiresPermissions("finance:receivable:list")
    @PostMapping("/receivable/list")
    @ResponseBody
    public TableDataInfo receivableList(ReceivableReqVo reqVo) {
        startPage();
        List<ReceivableRespVo> list = bizFinanceService.selectReceivableList(reqVo);
        return getDataTable(list);
    }

    @GetMapping("/receivable/summary")
    @RequiresPermissions("finance:receivable:summary:view")
    public String receivableSummary() {
        return prefix + "/receivable/receivableSummary";
    }

    /**
     * 查询应收列表(销售合同列表)
     */
    @RequiresPermissions("finance:receivable:summary:list")
    @PostMapping("/receivable/summary/list")
    @ResponseBody
    public TableDataInfo receivableSummaryList(ReceivableReqVo reqVo) {
        startPage();
        List<ReceivableSummaryRespVo> list = bizFinanceService.selectReceivableSummaryList(reqVo);
        return getDataTable(list);
    }

    @GetMapping("/standAccount")
    @RequiresPermissions("finance:standAccount:view")
    public String standAccount() {
        return prefix + "/standAccount/standAccount";
    }

    /**
     * 查询财务挂账(采购合同列表,到货数量大于0)
     */
    @RequiresPermissions("finance:standAccount:list")
    @PostMapping("/standAccount/list")
    @ResponseBody
    public TableDataInfo standAccountList(StandAccountReqVo reqVo) {
        startPage();
        List<StandAccountRespVo> list = bizFinanceService.selectStandAccountList(reqVo);
        return getDataTable(list);
    }

    @GetMapping("/standAccount/summary")
    @RequiresPermissions("finance:standAccount:summary:view")
    public String standAccountSummary() {
        return prefix + "/standAccount/standAccountSummary";
    }

    /**
     * 查询财务挂账(采购合同列表,到货数量大于0)
     */
    @RequiresPermissions("finance:standAccount:summary:list")
    @PostMapping("/standAccount/summary/list")
    @ResponseBody
    public TableDataInfo standAccountSummaryList(StandAccountReqVo reqVo) {
        startPage();
        List<StandAccountSummaryRespVo> list = bizFinanceService.selectStandAccountSummaryList(reqVo);
        return getDataTable(list);
    }

    /**
     * 采购合同的汇总统计
     */
    @GetMapping("/procurement/summary")
    @RequiresPermissions("finance:procurement:summary:view")
    public String procurementSummary() {
        return prefix + "/summary/procurementSummary";
    }

    /**
     * 采购合同的汇总统计
     */
    @RequiresPermissions("finance:procurement:summary:view")
    @PostMapping("/procurement/summary/list")
    @ResponseBody
    public TableDataInfo procurementSummaryList(SummaryReqVo reqVo) {
        startPage();
        List<SummaryRespVo> list = bizFinanceService.selectProcurementSummary(reqVo);
        return getDataTable(list);
    }

    /**
     * 销售合同的汇总统计
     */
    @GetMapping("/saleContract/summary")
    @RequiresPermissions("finance:saleContract:summary:view")
    public String saleContractSummary() {
        return prefix + "/summary/saleContractSummary";
    }

    /**
     * 销售合同的汇总统计
     */
    @RequiresPermissions("finance:saleContract:summary:view")
    @PostMapping("/saleContract/summary/list")
    @ResponseBody
    public TableDataInfo saleContractSummaryList(SummaryReqVo reqVo) {
        startPage();
        List<SummaryRespVo> list = bizFinanceService.selectSaleContractSummary(reqVo);
        return getDataTable(list);
    }

}
