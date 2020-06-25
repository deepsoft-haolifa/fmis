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
import com.ruoyi.fmis.stestn.domain.BizDataStestn;
import com.ruoyi.fmis.stestn.service.IBizDataStestnService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 采购检测数量Controller
 *
 * @author frank
 * @date 2020-06-24
 */
@Controller
@RequestMapping("/fmis/stestn")
public class BizDataStestnController extends BaseController {
    private String prefix = "fmis/stestn";

    @Autowired
    private IBizDataStestnService bizDataStestnService;

    @RequiresPermissions("fmis:stestn:view")
    @GetMapping()
    public String stestn() {
        return prefix + "/stestn";
    }

    /**
     * 查询采购检测数量列表
     */
    @RequiresPermissions("fmis:stestn:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizDataStestn bizDataStestn) {
        startPage();
        List<BizDataStestn> list = bizDataStestnService.selectBizDataStestnList(bizDataStestn);
        return getDataTable(list);
    }

    /**
     * 导出采购检测数量列表
     */
    @RequiresPermissions("fmis:stestn:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizDataStestn bizDataStestn) {
        List<BizDataStestn> list = bizDataStestnService.selectBizDataStestnList(bizDataStestn);
        ExcelUtil<BizDataStestn> util = new ExcelUtil<BizDataStestn>(BizDataStestn.class);
        return util.exportExcel(list, "stestn");
    }

    /**
     * 新增采购检测数量
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存采购检测数量
     */
    @RequiresPermissions("fmis:stestn:add")
    @Log(title = "采购检测数量", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizDataStestn bizDataStestn) {
        return toAjax(bizDataStestnService.insertBizDataStestn(bizDataStestn));
    }

    /**
     * 修改采购检测数量
     */
    @GetMapping("/edit/{testId}")
    public String edit(@PathVariable("testId") Long testId, ModelMap mmap) {
        BizDataStestn bizDataStestn = bizDataStestnService.selectBizDataStestnById(testId);
        mmap.put("bizDataStestn", bizDataStestn);
        return prefix + "/edit";
    }

    /**
     * 修改保存采购检测数量
     */
    @RequiresPermissions("fmis:stestn:edit")
    @Log(title = "采购检测数量", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizDataStestn bizDataStestn) {
        return toAjax(bizDataStestnService.updateBizDataStestn(bizDataStestn));
    }

    /**
     * 删除采购检测数量
     */
    @RequiresPermissions("fmis:stestn:remove")
    @Log(title = "采购检测数量", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizDataStestnService.deleteBizDataStestnByIds(ids));
    }
}
