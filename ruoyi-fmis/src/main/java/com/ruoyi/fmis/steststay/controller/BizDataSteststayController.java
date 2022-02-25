package com.ruoyi.fmis.steststay.controller;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.pattachment.domain.BizProductAttachment;
import com.ruoyi.fmis.pattachment.service.IBizProductAttachmentService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.fmis.stestn.domain.BizDataStestn;
import com.ruoyi.fmis.stestn.service.IBizDataStestnService;
import com.ruoyi.framework.util.ShiroUtils;
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
import com.ruoyi.fmis.steststay.domain.BizDataSteststay;
import com.ruoyi.fmis.steststay.service.IBizDataSteststayService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 待质检Controller
 *
 * @author frank
 * @date 2020-07-26
 */
@Controller
@RequestMapping("/fmis/steststay")
public class BizDataSteststayController extends BaseController {
    private String prefix = "fmis/steststay";

    @Autowired
    private IBizDataSteststayService bizDataSteststayService;

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private IBizProcessChildService bizProcessChildService;

    @Autowired
    private IBizProductService bizProductService;

    @Autowired
    private IBizProductRefService bizProductRefService;

    @Autowired
    private IBizActuatorService bizActuatorService;

    @Autowired
    private IBizProductAttachmentService bizProductAttachmentService;

    @Autowired
    private IBizDataStestnService bizDataStestnService;

    @RequiresPermissions("fmis:steststay:view")
    @GetMapping()
    public String steststay() {
        return prefix + "/steststay";
    }

    /**
     * 查询待质检列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizDataSteststay bizDataSteststay) {
        startPage();
        List<BizDataSteststay> list = bizDataSteststayService.selectBizDataSteststayList(bizDataSteststay);
        return getDataTable(list);
    }

    /**
     * 导出待质检列表
     */
    @RequiresPermissions("fmis:steststay:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizDataSteststay bizDataSteststay) {
        List<BizDataSteststay> list = bizDataSteststayService.selectBizDataSteststayList(bizDataSteststay);
        ExcelUtil<BizDataSteststay> util = new ExcelUtil<BizDataSteststay>(BizDataSteststay.class);
        return util.exportExcel(list, "steststay");
    }

    /**
     * 新增待质检
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存待质检
     */
    @RequiresPermissions("fmis:steststay:add")
    @Log(title = "待质检", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizDataSteststay bizDataSteststay) {

        return toAjax(bizDataSteststayService.insertBizDataSteststay(bizDataSteststay));
    }

    /**
     * 修改待质检
     */
    @GetMapping("/edit/{stayId}")
    public String edit(@PathVariable("stayId") Long stayId, ModelMap mmap) {
        BizDataSteststay bizDataSteststay = bizDataSteststayService.selectBizDataSteststayById(stayId);
        mmap.put("bizDataSteststay", bizDataSteststay);
        return prefix + "/edit";
    }

    /**
     * 修改保存待质检
     */
    @RequiresPermissions("fmis:steststay:edit")
    @Log(title = "待质检", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizDataSteststay bizDataSteststay) {
        return toAjax(bizDataSteststayService.updateBizDataSteststay(bizDataSteststay));
    }

    /**
     * 删除待质检
     */
    @RequiresPermissions("fmis:steststay:remove")
    @Log(title = "待质检", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizDataSteststayService.deleteBizDataSteststayByIds(ids));
    }


    @PostMapping("/selectBizTestChildList")
    @ResponseBody
    public TableDataInfo selectBizTestChildList(BizProcessChild bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setParamterId(bizProcessData.getParamterId());
        queryBizProcessChild.setChildId(bizProcessData.getChildId());
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizTestStayChildList(queryBizProcessChild);
        return getDataTable(bizProcessChildList);
    }

    @PostMapping("/saveTest")
    @ResponseBody
    public AjaxResult saveTest() {
        String stayId = getRequest().getParameter("stayId");
        String dataId = getRequest().getParameter("dataId");//合同id
        String paramterId = getRequest().getParameter("paramterId");//业务数据id
        String childId = getRequest().getParameter("childId");//关联字表id
        String remark = getRequest().getParameter("remark");
        String stayNum = getRequest().getParameter("stayNum");
        String statusId = getRequest().getParameter("statusId");
        if (stayNum.equals("0")) {
            return toAjax(-1);
        }

        BizDataSteststay bizDataStestn = new BizDataSteststay();

        if (!"0".equals(stayId) && StringUtils.isNotEmpty(stayId)) {
            bizDataStestn = bizDataSteststayService.selectBizDataSteststayById(Long.parseLong(stayId));
        }
        bizDataStestn.setStatusId(Long.parseLong(statusId));

        bizDataStestn.setString3(paramterId);
        bizDataStestn.setString4(dataId);
        bizDataStestn.setString5(childId);
        bizDataStestn.setString1("0");
        bizDataStestn.setRemark(remark);
        if ("0".equals(stayId) || StringUtils.isEmpty(stayId)) {
            // 新增
            bizDataStestn.setNum(Double.parseDouble(stayNum));
            //string6 报检单号
            String noStart = "BJ";
            noStart += DateUtils.dateTime();
            Long no = bizDataSteststayService.selectMaxNo();
            if (no == null) {
                no = 1L;
            }
            String orderNo = noStart + new DecimalFormat("000").format(no);
            bizDataStestn.setString6(orderNo);

            bizDataStestn.setCreateTime(new Date());
            bizDataStestn.setCreateBy(ShiroUtils.getUserId().toString());
            bizDataSteststayService.insertBizDataSteststay(bizDataStestn);

        } else {
            // 更新
            bizDataSteststayService.updateBizDataSteststay(bizDataStestn);
        }
        return toAjax(1);
    }


    @PostMapping("/removeTest")
    @ResponseBody
    public AjaxResult removeTest() {
        String stayId = getRequest().getParameter("stayId");
        if ("0".equals(stayId)) {
            return toAjax(1);
        }
        bizDataSteststayService.deleteBizDataSteststayById(Long.parseLong(stayId));
        return toAjax(1);
    }
}
