//package com.ruoyi.fmis.web.controller.fmis;
//
//import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.ruoyi.common.annotation.Log;
//import com.ruoyi.common.enums.BusinessType;
//import com.ruoyi.fmis.bcontract.domain.BizBillContract;
//import com.ruoyi.fmis.bcontract.service.IBizBillContractService;
//import com.ruoyi.common.core.controller.BaseController;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.utils.poi.ExcelUtil;
//import com.ruoyi.common.core.page.TableDataInfo;
//
///**
// * 合同收款Controller
// *
// * @author frank
// * @date 2020-06-26
// */
//@Controller
//@RequestMapping("/fmis/bcontract")
//public class BizBillContractController extends BaseController {
//    private String prefix = "fmis/bcontract";
//
//    @Autowired
//    private IBizBillContractService bizBillContractService;
//
//    @RequiresPermissions("fmis:bcontract:view")
//    @GetMapping()
//    public String bcontract() {
//        return prefix + "/bcontract";
//    }
//
//    /**
//     * 查询合同收款列表
//     */
//    @RequiresPermissions("fmis:bcontract:list")
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(BizBillContract bizBillContract) {
//        startPage();
//        List<BizBillContract> list = bizBillContractService.selectBizBillContractList(bizBillContract);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出合同收款列表
//     */
//    @RequiresPermissions("fmis:bcontract:export")
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(BizBillContract bizBillContract) {
//        List<BizBillContract> list = bizBillContractService.selectBizBillContractList(bizBillContract);
//        ExcelUtil<BizBillContract> util = new ExcelUtil<BizBillContract>(BizBillContract.class);
//        return util.exportExcel(list, "bcontract");
//    }
//
//    /**
//     * 新增合同收款
//     */
//    @GetMapping("/add")
//    public String add() {
//        return prefix + "/add";
//    }
//
//    /**
//     * 新增保存合同收款
//     */
//    @RequiresPermissions("fmis:bcontract:add")
//    @Log(title = "合同收款", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(BizBillContract bizBillContract) {
//        return toAjax(bizBillContractService.insertBizBillContract(bizBillContract));
//    }
//
//    /**
//     * 修改合同收款
//     */
//    @GetMapping("/edit/{bcId}")
//    public String edit(@PathVariable("bcId") Long bcId, ModelMap mmap) {
//        BizBillContract bizBillContract = bizBillContractService.selectBizBillContractById(bcId);
//        mmap.put("bizBillContract", bizBillContract);
//        return prefix + "/edit";
//    }
//
//    /**
//     * 修改保存合同收款
//     */
//    @RequiresPermissions("fmis:bcontract:edit")
//    @Log(title = "合同收款", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(BizBillContract bizBillContract) {
//        return toAjax(bizBillContractService.updateBizBillContract(bizBillContract));
//    }
//
//    /**
//     * 删除合同收款
//     */
//    @RequiresPermissions("fmis:bcontract:remove")
//    @Log(title = "合同收款", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids) {
//        return toAjax(bizBillContractService.deleteBizBillContractByIds(ids));
//    }
//}
