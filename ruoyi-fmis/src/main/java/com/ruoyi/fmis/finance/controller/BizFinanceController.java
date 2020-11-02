package com.ruoyi.fmis.finance.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.fmis.finance.domain.vo.ReceivableReqVo;
import com.ruoyi.fmis.finance.domain.vo.ReceivableRespVo;
import com.ruoyi.fmis.finance.domain.vo.StandAccountReqVo;
import com.ruoyi.fmis.finance.domain.vo.StandAccountRespVo;
import com.ruoyi.fmis.finance.service.IBizFinanceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String receivable() {
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


    @GetMapping("/standAccount")
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

}
