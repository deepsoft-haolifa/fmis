package com.ruoyi.fmis.customer.controller;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.process.user.service.IActIdUserService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
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
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户Controller
 *
 * @author frank
 * @date 2020-03-02
 */
@Controller
@RequestMapping("/fmis/customer")
public class BizCustomerController extends BaseController {
    private String prefix = "fmis/customer";


    @Autowired
    private ISysUserService userService;
    @Autowired
    private IActIdUserService actIdUserService;

    @Autowired
    private IBizCustomerService bizCustomerService;

    @Autowired
    private ISysDeptService sysDeptService;

    @RequiresPermissions("fmis:customer:view")
    @GetMapping()
    public String customer() {
        return prefix + "/customer";
    }

    /**
     * 查询客户列表
     */
    @RequiresPermissions("fmis:customer:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizCustomer bizCustomer) {
        startPage();
        List<BizCustomer> list = bizCustomerService.selectBizCustomerList(bizCustomer);
        return getDataTable(list);
    }

    @PostMapping("/listNoAuth")
    @ResponseBody
    public TableDataInfo listNoAuth(BizCustomer bizCustomer) {
        startPage();
        List<BizCustomer> list = bizCustomerService.selectBizCustomerList(bizCustomer);
        return getDataTable(list);
    }

    public String getBh () {
        String areCode = "";
        Long deptId = ShiroUtils.getSysUser().getDeptId();
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        if (sysDept != null) {
            SysDept sysDept1 = sysDeptService.selectDeptById(sysDept.getParentId());
            if (sysDept1 != null) {
                areCode = sysDept1.getAreCode();
            }
        }
        return areCode;
    }

    /**
     * 导出客户列表
     */
    @RequiresPermissions("fmis:customer:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizCustomer bizCustomer) {
        List<BizCustomer> list = bizCustomerService.selectBizCustomerList(bizCustomer);
        ExcelUtil<BizCustomer> util = new ExcelUtil<BizCustomer>(BizCustomer.class);
        return util.exportExcel(list, "customer");
    }

    /**
     * 新增客户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("users", userService.selectUserList(new SysUser()));
        mmap.put("areaCode",getBh());
        return prefix + "/add";
    }

    /**
     * 新增保存客户
     */
    @RequiresPermissions("fmis:customer:add")
    @Log(title = "客户", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizCustomer bizCustomer) {
        return toAjax(bizCustomerService.insertBizCustomer(bizCustomer));
    }

    /**
     * 修改客户
     */
    @GetMapping("/edit/{customerId}")
    public String edit(@PathVariable("customerId") Long customerId, ModelMap mmap) {
        BizCustomer bizCustomer = bizCustomerService.selectBizCustomerById(customerId);
        mmap.put("usersel", userService.selectUserById(Long.parseLong(bizCustomer.getOwnerUserId())));
        mmap.put("users", userService.selectUserList(new SysUser()));
        mmap.put("bizCustomer", bizCustomer);
        return prefix + "/edit";
    }

    /**
     * 修改保存客户
     */
    @RequiresPermissions("fmis:customer:edit")
    @Log(title = "客户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizCustomer bizCustomer) {
        return toAjax(bizCustomerService.updateBizCustomer(bizCustomer));
    }

    /**
     * 删除客户
     */
    @RequiresPermissions("fmis:customer:remove")
    @Log(title = "客户", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizCustomerService.deleteBizCustomerByIds(ids));
    }
}
