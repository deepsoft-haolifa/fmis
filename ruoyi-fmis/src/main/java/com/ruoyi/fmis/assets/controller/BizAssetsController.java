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
import com.ruoyi.fmis.assets.domain.BizAssets;
import com.ruoyi.fmis.assets.service.IBizAssetsService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 固定资产Controller
 *
 * @author frank
 * @date 2020-07-01
 */
@Controller
@RequestMapping("/fmis/assets")
public class BizAssetsController extends BaseController {
    private String prefix = "fmis/assets";

    @Autowired
    private IBizAssetsService bizAssetsService;

    @RequiresPermissions("fmis:assets:view")
    @GetMapping()
    public String assets() {
        return prefix + "/assets";
    }

    /**
     * 查询固定资产列表
     */
    @RequiresPermissions("fmis:assets:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizAssets bizAssets) {
        startPage();
        List<BizAssets> list = bizAssetsService.selectBizAssetsList(bizAssets);
        return getDataTable(list);
    }

    /**
     * 导出固定资产列表
     */
    @RequiresPermissions("fmis:assets:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizAssets bizAssets) {
        List<BizAssets> list = bizAssetsService.selectBizAssetsList(bizAssets);
        ExcelUtil<BizAssets> util = new ExcelUtil<BizAssets>(BizAssets.class);
        return util.exportExcel(list, "assets");
    }

    /**
     * 新增固定资产
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存固定资产
     */
    @RequiresPermissions("fmis:assets:add")
    @Log(title = "固定资产", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizAssets bizAssets) {
        return toAjax(bizAssetsService.insertBizAssets(bizAssets));
    }

    /**
     * 修改固定资产
     */
    @GetMapping("/edit/{assetsId}")
    public String edit(@PathVariable("assetsId") Long assetsId, ModelMap mmap) {
        BizAssets bizAssets = bizAssetsService.selectBizAssetsById(assetsId);
        mmap.put("bizAssets", bizAssets);
        return prefix + "/edit";
    }

    /**
     * 修改保存固定资产
     */
    @RequiresPermissions("fmis:assets:edit")
    @Log(title = "固定资产", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizAssets bizAssets) {
        return toAjax(bizAssetsService.updateBizAssets(bizAssets));
    }

    /**
     * 删除固定资产
     */
    @RequiresPermissions("fmis:assets:remove")
    @Log(title = "固定资产", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizAssetsService.deleteBizAssetsByIds(ids));
    }
}
