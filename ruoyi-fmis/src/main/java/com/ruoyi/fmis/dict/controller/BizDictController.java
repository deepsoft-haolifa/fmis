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
import com.ruoyi.fmis.dict.domain.BizDict;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业务数据字典Controller
 *
 * @author Xianlu Tech
 * @date 2020-03-11
 */
@Controller
@RequestMapping("/fmis/dict")
public class BizDictController extends BaseController {
    private String prefix = "fmis/dict";

    @Autowired
    private IBizDictService bizDictService;

    @RequiresPermissions("fmis:dict:view")
    @GetMapping()
    public String dict() {
        return prefix + "/dict";
    }

    /**
     * 查询业务数据字典列表
     */
    @RequiresPermissions("fmis:dict:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizDict bizDict) {
        startPage();
        List<BizDict> list = bizDictService.selectBizDictList(bizDict);
        return getDataTable(list);
    }

    /**
     * 导出业务数据字典列表
     */
    @RequiresPermissions("fmis:dict:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizDict bizDict) {
        List<BizDict> list = bizDictService.selectBizDictList(bizDict);
        ExcelUtil<BizDict> util = new ExcelUtil<BizDict>(BizDict.class);
        return util.exportExcel(list, "dict");
    }

    /**
     * 新增业务数据字典
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存业务数据字典
     */
    @RequiresPermissions("fmis:dict:add")
    @Log(title = "业务数据字典", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizDict bizDict) {
        return toAjax(bizDictService.insertBizDict(bizDict));
    }

    /**
     * 修改业务数据字典
     */
    @GetMapping("/edit/{dictId}")
    public String edit(@PathVariable("dictId") Long dictId, ModelMap mmap) {
        BizDict bizDict = bizDictService.selectBizDictById(dictId);
        mmap.put("bizDict", bizDict);
        return prefix + "/edit";
    }

    /**
     * 修改保存业务数据字典
     */
    @RequiresPermissions("fmis:dict:edit")
    @Log(title = "业务数据字典", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizDict bizDict) {
        return toAjax(bizDictService.updateBizDict(bizDict));
    }

    /**
     * 删除业务数据字典
     */
    @RequiresPermissions("fmis:dict:remove")
    @Log(title = "业务数据字典", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizDictService.deleteBizDictByIds(ids));
    }
}
