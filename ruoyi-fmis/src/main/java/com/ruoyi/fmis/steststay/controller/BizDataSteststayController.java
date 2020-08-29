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
    @RequiresPermissions("fmis:steststay:list")
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

        BizDataSteststay bizDataStestn = new BizDataSteststay();

        if (!"0".equals(stayId) && StringUtils.isNotEmpty(stayId)) {
            bizDataStestn = bizDataSteststayService.selectBizDataSteststayById(Long.parseLong(stayId));
        }
        bizDataStestn.setStatusId(Long.parseLong(statusId));
        bizDataStestn.setNum(Double.parseDouble(stayNum));
        bizDataStestn.setString3(paramterId);
        bizDataStestn.setString4(dataId);
        bizDataStestn.setString5(childId);
        bizDataStestn.setString1("0");
        bizDataStestn.setRemark(remark);
        if ("0".equals(stayId) || StringUtils.isEmpty(stayId)) {
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

            /**
             * 放到 库存管理表 里面
             * 报检单号，orderNo=string3
             * 销售合同号，string4=string12
             * 采购合同号，string5=string10
             * 类型，string2
             * 名称，
             * 型号，
             * 规格、
             * 等级、
             * 价格、
             * 数量
             */
            BizProcessChild bizProcessChild = bizProcessChildService.selectBizProcessChildById(Long.parseLong(childId));
            String productId = bizProcessChild.getString2();
            String actuatorId = bizProcessChild.getString11();
            String ref1 = bizProcessChild.getString5();
            String ref2 = bizProcessChild.getString8();
            //定位器
            Long pattachmentId = bizProcessChild.getPattachmentId();
            Long pattachmentId1 = bizProcessChild.getPattachment1Id();
            Long pattachmentId2 = bizProcessChild.getPattachment2Id();
            Long pattachmentId3 = bizProcessChild.getPattachment3Id();
            Long pattachmentId4 = bizProcessChild.getPattachment4Id();
            String type = "0";

            BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
            //质检管理 数据
            BizProcessChild insertChild = new BizProcessChild();
            insertChild.setCreateBy(ShiroUtils.getUserId().toString());
            insertChild.setString1(childId);
            insertChild.setString4(bizProcessData.getString12());
            insertChild.setString5(bizProcessData.getString10());
            insertChild.setString11(stayNum);
            insertChild.setString12(bizDataStestn.getStayId().toString());
            if (StringUtils.isNotEmpty(productId)) {
                type = "1";
                BizProduct queryBizProduct = new BizProduct();
                queryBizProduct.setProductId(Long.parseLong(productId));
                BizProduct bizProduct = bizProductService.selectBizProductList(queryBizProduct).get(0);

                insertChild.setString6(bizProduct.getName());
                insertChild.setString7(bizProduct.getModel());
                insertChild.setString8(bizProduct.getSpecifications());
                insertChild.setString9(bizProduct.getString2());
                insertChild.setString10(StringUtils.getDoubleString0(bizProduct.getProcurementPrice()));

            } else if (StringUtils.isNotEmpty(actuatorId)) {
                type = "2";
                BizActuator queryActuator = new BizActuator();
                queryActuator.setActuatorId(Long.parseLong(actuatorId));
                BizActuator bizActuator = bizActuatorService.selectBizActuatorForRefList(queryActuator).get(0);

                insertChild.setString6(bizActuator.getName());
                insertChild.setString7(bizActuator.getString1());
                //insertChild.setString8(bizProduct.getSpecifications());
                insertChild.setString9(bizActuator.getQualityLevel());
                insertChild.setString10(bizActuator.getString6());

            } else if (StringUtils.isNotEmpty(ref1)) {
                type = "3";

                BizProductRef queryRef1 = new BizProductRef();
                queryRef1.setProductRefId(Long.parseLong(ref1));
                BizProductRef bizProductRef = bizProductRefService.selectBizProductRefList(queryRef1).get(0);

                insertChild.setString6(bizProductRef.getName());
                insertChild.setString7(bizProductRef.getModel());
                insertChild.setString8(bizProductRef.getSpecifications());
                insertChild.setString9(bizProductRef.getString1());
                insertChild.setString10(bizProductRef.getString2());

            } else if (StringUtils.isNotEmpty(ref2)) {
                type = "4";

                BizProductRef queryRef1 = new BizProductRef();
                queryRef1.setProductRefId(Long.parseLong(ref2));
                BizProductRef bizProductRef = bizProductRefService.selectBizProductRefList(queryRef1).get(0);

                insertChild.setString6(bizProductRef.getName());
                insertChild.setString7(bizProductRef.getModel());
                insertChild.setString8(bizProductRef.getSpecifications());
                insertChild.setString9(bizProductRef.getString1());
                insertChild.setString10(bizProductRef.getString2());

            } else if (pattachmentId != null && pattachmentId > 0L) {
                type = "5";
                BizProductAttachment queryProductAttachment = new BizProductAttachment();
                queryProductAttachment.setAttachmentId(pattachmentId);
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentList(queryProductAttachment).get(0);

                insertChild.setString6(bizProductAttachment.getChineseName());
                //insertChild.setString7(bizProductAttachment.getModel());
                insertChild.setString8(bizProductAttachment.getChineseSpecifications());
                insertChild.setString9(bizProductAttachment.getString1());
                insertChild.setString10(StringUtils.getDoubleString0(bizProductAttachment.getSettlementPrice()));

            } else if (pattachmentId1 != null && pattachmentId1 > 0L) {
                type = "6";
                BizProductAttachment queryProductAttachment = new BizProductAttachment();
                queryProductAttachment.setAttachmentId(pattachmentId1);
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentList(queryProductAttachment).get(0);

                insertChild.setString6(bizProductAttachment.getChineseName());
                //insertChild.setString7(bizProductAttachment.getModel());
                insertChild.setString8(bizProductAttachment.getChineseSpecifications());
                insertChild.setString9(bizProductAttachment.getString1());
                insertChild.setString10(StringUtils.getDoubleString0(bizProductAttachment.getSettlementPrice()));
            } else if (pattachmentId2 != null && pattachmentId2 > 0L) {
                type = "7";
                BizProductAttachment queryProductAttachment = new BizProductAttachment();
                queryProductAttachment.setAttachmentId(pattachmentId2);
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentList(queryProductAttachment).get(0);

                insertChild.setString6(bizProductAttachment.getChineseName());
                //insertChild.setString7(bizProductAttachment.getModel());
                insertChild.setString8(bizProductAttachment.getChineseSpecifications());
                insertChild.setString9(bizProductAttachment.getString1());
                insertChild.setString10(StringUtils.getDoubleString0(bizProductAttachment.getSettlementPrice()));
            } else if (pattachmentId3 != null && pattachmentId3 > 0L) {
                type = "8";
                BizProductAttachment queryProductAttachment = new BizProductAttachment();
                queryProductAttachment.setAttachmentId(pattachmentId3);
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentList(queryProductAttachment).get(0);

                insertChild.setString6(bizProductAttachment.getChineseName());
                //insertChild.setString7(bizProductAttachment.getModel());
                insertChild.setString8(bizProductAttachment.getChineseSpecifications());
                insertChild.setString9(bizProductAttachment.getString1());
                insertChild.setString10(StringUtils.getDoubleString0(bizProductAttachment.getSettlementPrice()));
            } else if (pattachmentId4 != null && pattachmentId4 > 0L) {
                type = "9";
                BizProductAttachment queryProductAttachment = new BizProductAttachment();
                queryProductAttachment.setAttachmentId(pattachmentId4);
                BizProductAttachment bizProductAttachment = bizProductAttachmentService.selectBizProductAttachmentList(queryProductAttachment).get(0);

                insertChild.setString6(bizProductAttachment.getChineseName());
                //insertChild.setString7(bizProductAttachment.getModel());
                insertChild.setString8(bizProductAttachment.getChineseSpecifications());
                insertChild.setString9(bizProductAttachment.getString1());
                insertChild.setString10(StringUtils.getDoubleString0(bizProductAttachment.getSettlementPrice()));
            }
            insertChild.setString2(type);
            insertChild.setString3(orderNo);
            //质检状态
            insertChild.setString20("1");

            BizProcessChild queryChildExist = new BizProcessChild();
            queryChildExist.setString1(childId);
            //queryChildExist.setString3(orderNo);
            List<BizProcessChild> bizProcessChildListExist = bizProcessChildService.selectBizProcessChildList(queryChildExist);
            if (CollectionUtils.isEmpty(bizProcessChildListExist)) {
                bizProcessChildService.insertBizProcessChild(insertChild);
            } else {
                BizProcessChild updateChild = bizProcessChildListExist.get(0);
                updateChild.setString20("1");
                updateChild.setString11((Integer.parseInt(updateChild.getString11()) + Integer.parseInt(stayNum)) + "");
                updateChild.setUpdateBy(ShiroUtils.getUserId().toString());
                updateChild.setUpdateTime(new Date());
                bizProcessChildService.updateBizProcessChild(updateChild);
            }


            /**
             * 寻找销售合同
             *   根据销售合同号查询合同
             *   string27 1=存在发货
             */
            String contractNo = bizProcessData.getString12();
            if (StringUtils.isNotEmpty(contractNo)) {
                String[] contractNoS = contractNo.split(",");
                for (String contractNo_ : contractNoS) {
                    BizProcessData queryContractData = new BizProcessData();
                    queryContractData.setString1(contractNo_);
                    List<BizProcessData> contractDatas = bizProcessDataService.selectBizProcessDataList(queryContractData);
                    if (!CollectionUtils.isEmpty(contractDatas)) {
                        //BizProcessData contract = contractDatas.get(0);
                        //contract.setString27("1");
                        //bizProcessDataService.updateBizProcessData(contract);
                    }
                }
            }
        } else {
            //bizDataSteststayService.updateBizDataSteststay(bizDataStestn);
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
