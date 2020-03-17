package com.ruoyi.fmis.web.controller.fmis;

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
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 产品关系Controller
 *
 * @author frank
 * @date 2020-03-17
 */
@Controller
@RequestMapping("/fmis/productRef")
public class BizProductRefController extends BaseController {
    private String prefix = "fmis/productRef";

    @Autowired
    private IBizProductRefService bizProductRefService;

    @RequiresPermissions("fmis:productRef:view")
    @GetMapping()
    public String productRef() {
        return prefix + "/productRef";
    }

    /**
     * 查询产品关系列表
     */
    @RequiresPermissions("fmis:productRef:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProductRef bizProductRef) {
        startPage();
        List<BizProductRef> list = bizProductRefService.selectBizProductRefList(bizProductRef);
        return getDataTable(list);
    }

    /**
     * 导出产品关系列表
     */
    @RequiresPermissions("fmis:productRef:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProductRef bizProductRef) {
        List<BizProductRef> list = bizProductRefService.selectBizProductRefList(bizProductRef);
        ExcelUtil<BizProductRef> util = new ExcelUtil<BizProductRef>(BizProductRef.class);
        return util.exportExcel(list, "productRef");
    }

    /**
     * 新增产品关系
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存产品关系
     */
    @RequiresPermissions("fmis:productRef:add")
    @Log(title = "产品关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProductRef bizProductRef) {
        return toAjax(bizProductRefService.insertBizProductRef(bizProductRef));
    }

    /**
     * 修改产品关系
     */
    @GetMapping("/edit/{productRefId}")
    public String edit(@PathVariable("productRefId") Long productRefId, ModelMap mmap) {
        BizProductRef bizProductRef = bizProductRefService.selectBizProductRefById(productRefId);
        mmap.put("bizProductRef", bizProductRef);
        return prefix + "/edit";
    }

    /**
     * 修改保存产品关系
     */
    @RequiresPermissions("fmis:productRef:edit")
    @Log(title = "产品关系", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProductRef bizProductRef) {
        return toAjax(bizProductRefService.updateBizProductRef(bizProductRef));
    }

    /**
     * 删除产品关系
     */
    @RequiresPermissions("fmis:productRef:remove")
    @Log(title = "产品关系", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizProductRefService.deleteBizProductRefByIds(ids));
    }
}
