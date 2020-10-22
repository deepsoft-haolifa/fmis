package com.ruoyi.fmis.finance.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fmis.finance.domain.BizBillContract;
import com.ruoyi.fmis.finance.service.IBizBillContractService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同收款(合同分解)Controller
 *
 * @author hedong
 * @date 2020-10-22
 */
@Controller
@RequestMapping("/finance/billContract")
public class BizBillContractController extends BaseController {
    private String prefix = "finance/billContract";

    @Autowired
    private IBizBillContractService bizBillContractService;

    @RequiresPermissions("finance:billContract:view")
    @GetMapping()
    public String billContract() {
        return prefix + "/billContract";
    }

    /**
     * 查询合同收款(合同分解)列表
     */
    @RequiresPermissions("finance:billContract:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizBillContract bizBillContract) {
        startPage();
        List<BizBillContract> list = bizBillContractService.selectBizBillContractList(bizBillContract);
        return getDataTable(list);
    }

    /**
     * 导出合同收款(合同分解)列表
     */
    @RequiresPermissions("finance:billContract:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizBillContract bizBillContract) {
        List<BizBillContract> list = bizBillContractService.selectBizBillContractList(bizBillContract);
        ExcelUtil<BizBillContract> util = new ExcelUtil<BizBillContract>(BizBillContract.class);
        return util.exportExcel(list, "billContract");
    }

    /**
     * 新增合同收款(合同分解)
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存合同收款(合同分解)
     */
    @RequiresPermissions("finance:billContract:add")
    @Log(title = "合同收款(合同分解)", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizBillContract bizBillContract) {
        return toAjax(bizBillContractService.insertBizBillContract(bizBillContract));
    }

    /**
     * 修改合同收款(合同分解)
     */
    @GetMapping("/edit/{bcId}")
    public String edit(@PathVariable("bcId") Long bcId, ModelMap mmap) {
        BizBillContract bizBillContract = bizBillContractService.selectBizBillContractById(bcId);
        mmap.put("bizBillContract", bizBillContract);
        return prefix + "/edit";
    }

    /**
     * 修改保存合同收款(合同分解)
     */
    @RequiresPermissions("finance:billContract:edit")
    @Log(title = "合同收款(合同分解)", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizBillContract bizBillContract) {
        return toAjax(bizBillContractService.updateBizBillContract(bizBillContract));
    }

    /**
     * 删除合同收款(合同分解)
     */
    @RequiresPermissions("finance:billContract:remove")
    @Log(title = "合同收款(合同分解)", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizBillContractService.deleteBizBillContractByIds(ids));
    }
}
