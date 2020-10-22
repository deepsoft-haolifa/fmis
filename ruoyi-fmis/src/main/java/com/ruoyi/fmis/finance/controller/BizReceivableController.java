package com.ruoyi.fmis.finance.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.finance.domain.vo.ReceivableReqVo;
import com.ruoyi.fmis.finance.domain.vo.ReceivableRespVo;
import com.ruoyi.fmis.finance.service.IBizReceivableService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 往来管理--应收管理
 *
 * @author murphy
 * @date 2020-10-13
 */
@Controller
@RequestMapping("/finance/receivable")
public class BizReceivableController extends BaseController {
    private String prefix = "/finance/receivable";


    @Autowired
    private IBizReceivableService bizReceivableService;

    @RequiresPermissions("finance:receivable:view")
    @GetMapping()
    public String data() {
        return prefix + "/data";
    }

    /**
     * 查询应收列表(销售合同列表)
     */
    @RequiresPermissions("finance:receivable:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ReceivableReqVo reqVo) {
        startPage();
        List<ReceivableRespVo> list = bizReceivableService.selectReceivableList(reqVo);
        return getDataTable(list);
    }

}
