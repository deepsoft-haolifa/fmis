package com.ruoyi.fmis.customer.controller;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelProduct;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.BizCustomerImport;
import com.ruoyi.fmis.common.BizProductImport;
import com.ruoyi.fmis.dict.domain.BizDict;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.impl.util.CollectionUtil;
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

    @PostMapping("/checkCustomerName")
    @ResponseBody
    public String checkCustomerName(BizCustomer bizCustomer)
    {
        String name = bizCustomer.getName();
        //0=增加 1=编辑
        String status = bizCustomer.getStatus();
        Long id = bizCustomer.getCustomerId();

        if (StringUtils.isEmpty(name)) {
            return BizConstants.VALIDATE_IS_EXIST;
        }
        BizCustomer queryBizCustomer = new BizCustomer();
        queryBizCustomer.setName(name);
        List<BizCustomer> list = bizCustomerService.selectBizCustomerList(queryBizCustomer);
        Iterator<BizCustomer> iterator = list.iterator();
        while(iterator.hasNext()){
            BizCustomer o = iterator.next();
            if(o.getCustomerId().compareTo(id) == 0) {
                iterator.remove();   //注意这个地方
            }
        }
        if (CollectionUtil.isEmpty(list) || list.size() == 0) {
            return BizConstants.VALIDATE_IS_NOT_EXIST;
        }
        return BizConstants.VALIDATE_IS_EXIST;
    }


    @PostMapping("/checkContactName")
    @ResponseBody
    public String checkContactName(BizCustomer bizCustomer)
    {
        String name = bizCustomer.getContactName();
        //0=增加 1=编辑
        String status = bizCustomer.getStatus();
        Long id = bizCustomer.getCustomerId();
        if (StringUtils.isEmpty(name)) {
            return BizConstants.VALIDATE_IS_EXIST;
        }
        BizCustomer queryBizCustomer = new BizCustomer();
        queryBizCustomer.setContactName(name);
        List<BizCustomer> list = bizCustomerService.selectBizCustomerList(queryBizCustomer);
        Iterator<BizCustomer> iterator = list.iterator();
        while(iterator.hasNext()){
            BizCustomer o = iterator.next();
            if(o.getCustomerId().compareTo(id) == 0) {
                iterator.remove();   //注意这个地方
            }
        }
        if (CollectionUtil.isEmpty(list) || list.size() == 0) {
            return BizConstants.VALIDATE_IS_NOT_EXIST;
        }
        return BizConstants.VALIDATE_IS_EXIST;
    }

    @PostMapping("/checkContactPhone")
    @ResponseBody
    public String checkContactPhone(BizCustomer bizCustomer)
    {
        String name = bizCustomer.getContactPhone();
        //0=增加 1=编辑
        String status = bizCustomer.getStatus();
        Long id = bizCustomer.getCustomerId();
        if (StringUtils.isEmpty(name)) {
            return BizConstants.VALIDATE_IS_EXIST;
        }
        BizCustomer queryBizCustomer = new BizCustomer();
        queryBizCustomer.setContactPhone(name);
        List<BizCustomer> list = bizCustomerService.selectBizCustomerList(queryBizCustomer);
        Iterator<BizCustomer> iterator = list.iterator();
        while(iterator.hasNext()){
            BizCustomer o = iterator.next();
            if(o.getCustomerId().compareTo(id) == 0) {
                iterator.remove();   //注意这个地方
            }
        }
        if (CollectionUtil.isEmpty(list) || list.size() == 0) {
            return BizConstants.VALIDATE_IS_NOT_EXIST;
        }
        return BizConstants.VALIDATE_IS_EXIST;
    }

    /**
     * 修改客户
     */
    @GetMapping("/edit/{customerId}")
    public String edit(@PathVariable("customerId") Long customerId, ModelMap mmap) {
        BizCustomer bizCustomer = bizCustomerService.selectBizCustomerById(customerId);
        SysUser selUser = userService.selectUserById(Long.parseLong(bizCustomer.getOwnerUserId()));
        if (selUser == null) {
            selUser = new SysUser();
        }
        mmap.put("usersel", selUser);
        mmap.put("users", userService.selectUserList(new SysUser()));
        mmap.put("bizCustomer", bizCustomer);
        mmap.put("fileUrl", Global.getFileUrl());
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

    @GetMapping("/upload")
    public String upload(ModelMap mmap) {
        return prefix + "/upload";
    }

    @PostMapping("/importExcel")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject importExcel(){
        com.alibaba.fastjson.JSONObject retJson = new com.alibaba.fastjson.JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONArray errorArray = new JSONArray();
        String url = getRequest().getParameter("url");
        String realPath = Global.getFilePath() + url;
        List<BizCustomerImport> list = new ArrayList<>();
        Date now = new Date();
        try {
            ExcelUtil<BizCustomerImport> excelUtil = new ExcelUtil(BizCustomerImport.class);
            int rows = excelUtil.getRowLength("",realPath);
            if (rows > 50000) {
                JSONObject json = new JSONObject();
                json.put("msg","一次不能超过50000条数据！");
                errorArray.add(json);
                retJson.put("error",errorArray);
            } else {
                list = excelUtil.importExcel("",realPath);

                logger.info("list size=" + list.size());
                String delFlag = "0";
                for (int i = 0; i < list.size(); i++) {
                    BizCustomerImport customer = list.get(i);




                }
                JSONObject json = new JSONObject();
                json.put("msg","成功导入" + list.size() + "条数据！");
                dataArray.add(json);
                retJson.put("data",dataArray);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("导出失败，请联系网站管理员！");
        }

        return retJson;
    }
}
