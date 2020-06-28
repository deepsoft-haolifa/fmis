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
import com.ruoyi.fmis.subjects.domain.BizSubjects;
import com.ruoyi.fmis.subjects.service.IBizSubjectsService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 科目Controller
 *
 * @author frank
 * @date 2020-06-28
 */
@Controller
@RequestMapping("/fmis/subjects")
public class BizSubjectsController extends BaseController {
    private String prefix = "fmis/subjects";

    @Autowired
    private IBizSubjectsService bizSubjectsService;

    @RequiresPermissions("fmis:subjects:view")
    @GetMapping()
    public String subjects() {
        return prefix + "/subjects";
    }

    /**
     * 查询科目列表
     */
    @RequiresPermissions("fmis:subjects:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizSubjects bizSubjects) {
        startPage();
        List<BizSubjects> list = bizSubjectsService.selectBizSubjectsList(bizSubjects);
        return getDataTable(list);
    }

    /**
     * 导出科目列表
     */
    @RequiresPermissions("fmis:subjects:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizSubjects bizSubjects) {
        List<BizSubjects> list = bizSubjectsService.selectBizSubjectsList(bizSubjects);
        ExcelUtil<BizSubjects> util = new ExcelUtil<BizSubjects>(BizSubjects.class);
        return util.exportExcel(list, "subjects");
    }

    /**
     * 新增科目
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存科目
     */
    @RequiresPermissions("fmis:subjects:add")
    @Log(title = "科目", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizSubjects bizSubjects) {
        return toAjax(bizSubjectsService.insertBizSubjects(bizSubjects));
    }

    /**
     * 修改科目
     */
    @GetMapping("/edit/{subjectsId}")
    public String edit(@PathVariable("subjectsId") Long subjectsId, ModelMap mmap) {
        BizSubjects bizSubjects = bizSubjectsService.selectBizSubjectsById(subjectsId);
        mmap.put("bizSubjects", bizSubjects);
        return prefix + "/edit";
    }

    /**
     * 修改保存科目
     */
    @RequiresPermissions("fmis:subjects:edit")
    @Log(title = "科目", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizSubjects bizSubjects) {
        return toAjax(bizSubjectsService.updateBizSubjects(bizSubjects));
    }

    /**
     * 删除科目
     */
    @RequiresPermissions("fmis:subjects:remove")
    @Log(title = "科目", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizSubjectsService.deleteBizSubjectsByIds(ids));
    }
}
