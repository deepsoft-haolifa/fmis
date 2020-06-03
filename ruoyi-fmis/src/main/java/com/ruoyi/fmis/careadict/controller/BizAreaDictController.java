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
import com.ruoyi.fmis.careadict.domain.BizAreaDict;
import com.ruoyi.fmis.careadict.service.IBizAreaDictService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户区域字典Controller
 *
 * @author frank
 * @date 2020-05-28
 */
@Controller
@RequestMapping("/fmis/careadict")
public class BizAreaDictController extends BaseController {
    private String prefix = "fmis/careadict";

    @Autowired
    private IBizAreaDictService bizAreaDictService;

    @RequiresPermissions("fmis:careadict:view")
    @GetMapping()
    public String careadict() {
        return prefix + "/careadict";
    }

    /**
     * 查询客户区域字典列表
     */
    @RequiresPermissions("fmis:careadict:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizAreaDict bizAreaDict) {
        startPage();
        List<BizAreaDict> list = bizAreaDictService.selectBizAreaDictList(bizAreaDict);
        return getDataTable(list);
    }

    /**
     * 导出客户区域字典列表
     */
    @RequiresPermissions("fmis:careadict:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizAreaDict bizAreaDict) {
        List<BizAreaDict> list = bizAreaDictService.selectBizAreaDictList(bizAreaDict);
        ExcelUtil<BizAreaDict> util = new ExcelUtil<BizAreaDict>(BizAreaDict.class);
        return util.exportExcel(list, "careadict");
    }

    /**
     * 新增客户区域字典
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存客户区域字典
     */
    @RequiresPermissions("fmis:careadict:add")
    @Log(title = "客户区域字典", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizAreaDict bizAreaDict) {
        return toAjax(bizAreaDictService.insertBizAreaDict(bizAreaDict));
    }

    /**
     * 修改客户区域字典
     */
    @GetMapping("/edit/{areas}")
    public String edit(@PathVariable("areas") String areas, ModelMap mmap) {
        BizAreaDict bizAreaDict = bizAreaDictService.selectBizAreaDictById(areas);
        mmap.put("bizAreaDict", bizAreaDict);
        return prefix + "/edit";
    }

    /**
     * 修改保存客户区域字典
     */
    @RequiresPermissions("fmis:careadict:edit")
    @Log(title = "客户区域字典", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizAreaDict bizAreaDict) {
        return toAjax(bizAreaDictService.updateBizAreaDict(bizAreaDict));
    }

    /**
     * 删除客户区域字典
     */
    @RequiresPermissions("fmis:careadict:remove")
    @Log(title = "客户区域字典", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizAreaDictService.deleteBizAreaDictByIds(ids));
    }
}
