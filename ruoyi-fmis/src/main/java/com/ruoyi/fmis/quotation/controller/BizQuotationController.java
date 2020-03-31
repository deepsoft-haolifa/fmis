package com.ruoyi.fmis.quotation.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.itextpdf.PdfUtil;
import com.ruoyi.common.utils.itextpdf.TextWaterMarkPdfPageEvent;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotation.service.IBizQuotationService;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import com.ruoyi.fmis.quotationproduct.service.IBizQuotationProductService;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 报价单Controller
 *
 * @author frank
 * @date 2020-03-18
 */
@Controller
@RequestMapping("/fmis/quotation")
public class BizQuotationController extends BaseController {
    private String prefix = "fmis/quotation";

    @Autowired
    private IBizQuotationService bizQuotationService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IBizQuotationProductService bizQuotationProductService;

    @Autowired
    private IBizDictService bizDictService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @Autowired
    private IBizProductRefService bizProductRefService;

    @Autowired
    private IBizActuatorService bizActuatorService;

    @Autowired
    private ISysDeptService sysDeptService;

    @RequiresPermissions("fmis:quotation:view")
    @GetMapping()
    public String quotation(ModelMap mmap) {
        int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
        mmap.put("roleType",roleType);
        return prefix + "/quotation";
    }


    public String getBh () {
        Long deptId = ShiroUtils.getSysUser().getDeptId();
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        SysDept sysDept1 = sysDeptService.selectDeptById(sysDept.getParentId());
        String areCode = sysDept1.getAreCode();
        String bh = areCode + "-" + DateUtils.dateTimeNow();
        return bh;
    }

    /**
     * 查询报价单列表
     */
    @RequiresPermissions("fmis:quotation:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizQuotation bizQuotation) {

        int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
        bizQuotation.setString2(roleType + "");

        startPage();
        List<BizQuotation> list = bizQuotationService.selectBizQuotationFlowList(bizQuotation);
        return getDataTable(list);
    }

    /**
     * 导出报价单列表
     */
    @RequiresPermissions("fmis:quotation:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizQuotation bizQuotation) {
        //报价单
        bizQuotation = bizQuotationService.selectBizQuotationById(275L);
        //产品信息
        BizQuotationProduct bizQuotationProduct = new BizQuotationProduct();
        bizQuotationProduct.setQuotationId(bizQuotation.getQuotationId());
        List<BizQuotationProduct> bizQuotationProducts = bizQuotationProductService.selectBizQuotationProductDictList(bizQuotationProduct);
        try
        {
            String filename = PdfUtil.encodingFilename("报价单");
            String filePath = PdfUtil.getAbsoluteFile(filename);
            // step 1
            Document document = new Document(PageSize.A4);
            // step 2
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            // 只读
            writer.setEncryption(null, null, PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);
            // 通过PDF页面事件模式添加文字水印功能
            writer.setPageEvent(new TextWaterMarkPdfPageEvent("水印"));
            //设置字体样式
            //正常
            Font textFont = PdfUtil.getPdfChineseFont(11,Font.NORMAL);
            //加粗
            Font boldFont = PdfUtil.getPdfChineseFont(11,Font.BOLD);
            //二级标题
            Font titleFont = PdfUtil.getPdfChineseFont(15,Font.BOLD);

            Paragraph paragraph = new Paragraph("报 价 单", titleFont);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);

            //添加8列表格
            PdfPTable table = new PdfPTable(8);
            //table前间距
            table.setSpacingBefore(10f);
            // 设置各列列宽
            table.setTotalWidth(500);
            //锁定列宽
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            // 第一行
            table.addCell(PdfUtil.mergeCol("订单编号：" + bizQuotation.getString1(), 8));

            table.addCell(PdfUtil.getPDFCell("客户名称"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getCustomerName(), 7));
            table.addCell(PdfUtil.getPDFCell("特殊费用"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getCustomerName(), 3));
            table.addCell(PdfUtil.getPDFCell("费用说明"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getCustomerName(), 3));
            table.addCell(PdfUtil.getPDFCell("付款方式"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getCustomerName(), 7));
            table.addCell(PdfUtil.getPDFCell("运费价格"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getCustomerName(), 7));
            table.addCell(PdfUtil.getPDFCell("供货周期"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getCustomerName(), 7));
            table.addCell(PdfUtil.getPDFCell("报备项目"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getCustomerName(), 7));

            table.addCell(PdfUtil.getPDFCell("产品合计金额"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getString2(), 3));
            table.addCell(PdfUtil.getPDFCell("总金额"));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getString9(), 3));


            // step 3
            document.open();
            // step 4 写入内容
            document.add(paragraph);
            document.add(table);

            // 特别提醒
            Paragraph paragraphRemark = new Paragraph();
            Font remarkFont = PdfUtil.getPdfChineseFont(10, Font.NORMAL);
            Font fontMoney = PdfUtil.getPdfChineseFont(13, Font.NORMAL, BaseColor.RED);
            Font blackFont = PdfUtil.getPdfChineseFont(12, Font.NORMAL, BaseColor.BLACK);
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("特别提醒:", blackFont));
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("1.您的总金额为", remarkFont));
            paragraphRemark.add(new Chunk(bizQuotation.getString9(), fontMoney));
            paragraphRemark.add(new Chunk("。请确认。", remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("2.其他说明项。", remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);
            document.add(paragraphRemark);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            paragraph = new Paragraph("打印日期：" + sdf.format(new Date()), PdfUtil.getPdfChineseFont());
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            paragraph.setSpacingBefore(20);
            document.add(paragraph);
            // step 5
            document.close();
            return AjaxResult.success(filename);
        }
        catch (Exception e)
        {
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
    }



    /**
     * 新增报价单
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("bh",getBh());
        return prefix + "/add";
    }

    /**
     * 新增保存报价单
     */
    @RequiresPermissions("fmis:quotation:add")
    @Log(title = "报价单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizQuotation bizQuotation) {

        String productArrayStr = bizQuotation.getString10();
        bizQuotation.setString10("");
        setNormalFlag(bizQuotation,productArrayStr);
        int insertReturn = bizQuotationService.insertBizQuotation(bizQuotation);
        Long quotationId = bizQuotation.getQuotationId();
        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizQuotationProduct bizQuotationProduct = JSONObject.parseObject(json.toJSONString(), BizQuotationProduct.class);
                if (bizQuotationProduct.getProductId() != null) {
                    bizQuotationProduct.setQuotationId(quotationId);
                    bizQuotationProductService.insertBizQuotationProduct(bizQuotationProduct);
                }

            }
        }
        return toAjax(insertReturn);
    }


    @PostMapping("/checkQuotationNo")
    @ResponseBody
    public String checkQuotationNo(BizQuotation bizQuotation)
    {
        String string3 = bizQuotation.getString3();
        Long id = 0L;
        if ("1".equals(string3)) {
            id = new Long(bizQuotation.getQuotationId());
            bizQuotation.setQuotationId(null);
        }

        List<BizQuotation> list = bizQuotationService.selectBizQuotationList(bizQuotation);
        for (BizQuotation bizQuotation1 : list) {
            if (bizQuotation1.getQuotationId().compareTo(id) == 0) {
                list.remove(bizQuotation1);
            }
        }


        if (CollectionUtil.isEmpty(list) || list.size() == 0) {
            return BizConstants.VALIDATE_IS_NOT_EXIST;
        }
        return BizConstants.VALIDATE_IS_EXIST;
    }


    @PostMapping("/checkCustomer")
    @ResponseBody
    public String checkCustomer(BizQuotation bizQuotation)
    {
        String string3 = bizQuotation.getString3();
        Long id = 0L;
        if ("1".equals(string3)) {
            id = new Long(bizQuotation.getQuotationId());
            bizQuotation.setQuotationId(null);
        }

        List<BizQuotation> list = bizQuotationService.selectBizQuotationList(bizQuotation);
        for (BizQuotation bizQuotation1 : list) {
            if (bizQuotation1.getQuotationId().compareTo(id) == 0) {
                list.remove(bizQuotation1);
            }
        }


        if (CollectionUtil.isEmpty(list) || list.size() == 0) {
            return BizConstants.VALIDATE_IS_NOT_EXIST;
        }
        return BizConstants.VALIDATE_IS_EXIST;
    }


    @PostMapping("/report")
    @ResponseBody
    public AjaxResult report() {
        String quotationId = getRequest().getParameter("quotationId");
        BizQuotation bizQuotation = bizQuotationService.selectBizQuotationById(Long.parseLong(quotationId));
        return toAjax(bizQuotationService.subReportBizQuotation(bizQuotation));
    }

    /**
     * 修改报价单
     */
    @GetMapping("/edit/{quotationId}")
    public String edit(@PathVariable("quotationId") Long quotationId, ModelMap mmap) {
        BizQuotation bizQuotationQuery = new BizQuotation();
        bizQuotationQuery.setQuotationId(quotationId);
        List<BizQuotation> bizQuotationList = bizQuotationService.selectBizQuotationFlowList(bizQuotationQuery);

        BizQuotation bizQuotation = bizQuotationList.get(0);

        BizQuotationProduct bizQuotationProduct = new BizQuotationProduct();
        bizQuotationProduct.setQuotationId(bizQuotation.getQuotationId());
        List<BizQuotationProduct> bizQuotationProducts = bizQuotationProductService.selectBizQuotationProductDictList(bizQuotationProduct);
        bizQuotation.setQuotationProductList(bizQuotationProducts);

        String productNames = "";
        String productIds = "";
        for (BizQuotationProduct bizQuotationProduct1 : bizQuotationProducts) {
            productNames += bizQuotationProduct1.getProductName() + ",";
            productIds += bizQuotationProduct1.getProductId() + ",";


            Long productRef1Id = bizQuotationProduct1.getProductRef1Id();
            BizProductRef bizProductRef1 = new BizProductRef();
            if (productRef1Id != null && productRef1Id > 0L) {
                bizProductRef1.setProductRefId(productRef1Id);
                List<BizProductRef> bizProductRefs = bizProductRefService.selectBizProductRefList(bizProductRef1);
                if (!CollectionUtils.isEmpty(bizProductRefs)) {
                    bizQuotationProduct1.setBizProductRef1(bizProductRefs.get(0));
                }
            } else {
                bizQuotationProduct1.setBizProductRef1(bizProductRef1);
            }
            Long productRef2Id = bizQuotationProduct1.getProductRef2Id();
            BizProductRef bizProductRef2 = new BizProductRef();
            if (productRef2Id != null && productRef2Id > 0L) {
                bizProductRef2.setProductRefId(productRef2Id);
                List<BizProductRef> bizProductRefs = bizProductRefService.selectBizProductRefList(bizProductRef2);
                if (!CollectionUtils.isEmpty(bizProductRefs)) {
                    bizQuotationProduct1.setBizProductRef2(bizProductRefs.get(0));
                }
            } else {
                bizQuotationProduct1.setBizProductRef2(bizProductRef1);
            }
            Long actuatorId = bizQuotationProduct1.getActuatorId();
            BizActuator bizActuator = new BizActuator();
            if (actuatorId != null && actuatorId > 0L) {
                bizActuator.setActuatorId(actuatorId);
                List<BizActuator> bizActuators = bizActuatorService.selectBizActuatorForRefList(bizActuator);
                if (!CollectionUtils.isEmpty(bizActuators)) {
                    bizQuotationProduct1.setBizActuator(bizActuators.get(0));
                }
            } else {
                bizQuotationProduct1.setBizActuator(bizActuator);
            }
        }
        mmap.put("productNames", productNames);
        mmap.put("productIds", productIds);


        mmap.put("bizQuotation", bizQuotation);
        return prefix + "/edit";
    }

    /**
     * 修改保存报价单
     */
    @RequiresPermissions("fmis:quotation:edit")
    @Log(title = "报价单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizQuotation bizQuotation) {

        String productArrayStr = bizQuotation.getString10();
        bizQuotation.setString10("");
        setNormalFlag(bizQuotation,productArrayStr);
        int updateReturn = bizQuotationService.updateBizQuotation(bizQuotation);
        Long quotationId = bizQuotation.getQuotationId();

        BizQuotationProduct bizQuotationProduct1 = new BizQuotationProduct();
        bizQuotationProduct1.setQuotationId(bizQuotation.getQuotationId());
        List<BizQuotationProduct> bizQuotationProducts = bizQuotationProductService.selectBizQuotationProductDictList(bizQuotationProduct1);
        for (BizQuotationProduct bizQuotationProduct : bizQuotationProducts) {
            bizQuotationProductService.deleteBizQuotationProductById(bizQuotationProduct.getQpId());
        }


        if (StringUtils.isNotEmpty(productArrayStr)) {
            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizQuotationProduct bizQuotationProduct = JSONObject.parseObject(json.toJSONString(), BizQuotationProduct.class);
                if (bizQuotationProduct.getProductId() != null) {
                    bizQuotationProduct.setQuotationId(quotationId);
                    bizQuotationProductService.insertBizQuotationProduct(bizQuotationProduct);
                }
            }
        }
        return toAjax(updateReturn);
    }

    /**
     *
     * 2销售经理审批结束 3 区域经理审批结束 4副总审批结束 5 老总审批结束
     * @param bizQuotation
     * @return
     */
    public String setNormalFlag (BizQuotation bizQuotation,String productArrayStr) {
        String normalFlag = "";
        String totalPrice = bizQuotation.getString9();
        if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 500000) {
            normalFlag = "5";
        } else if (!"5".equals(bizQuotation.getPaymentMethod())) {
            //到付
            normalFlag = "5";
        } else {

            JSONArray productArray = JSONArray.parseArray(productArrayStr);
            Double minCoefficient = new Double(10000000);
            for (int i = 0; i < productArray.size(); i++) {
                JSONObject json = productArray.getJSONObject(i);
                BizQuotationProduct bizQuotationProduct = JSONObject.parseObject(json.toJSONString(), BizQuotationProduct.class);
                if (bizQuotationProduct.getProductId() != null) {
                    String actuatorCoefficient = bizQuotationProduct.getActuatorCoefficient();
                    if (StringUtils.isNotEmpty(actuatorCoefficient) && Double.parseDouble(actuatorCoefficient) < minCoefficient) {
                        minCoefficient = Double.parseDouble(actuatorCoefficient);
                    }
                    String productCoefficient = bizQuotationProduct.getProductCoefficient();
                    if (StringUtils.isNotEmpty(productCoefficient) && Double.parseDouble(productCoefficient) < minCoefficient) {
                        minCoefficient = Double.parseDouble(productCoefficient);
                    }

                    String productRef1Coefficient = bizQuotationProduct.getProductRef1Coefficient();
                    if (StringUtils.isNotEmpty(productRef1Coefficient) && Double.parseDouble(productRef1Coefficient) < minCoefficient) {
                        minCoefficient = Double.parseDouble(productRef1Coefficient);
                    }

                    String productRef2Coefficient = bizQuotationProduct.getProductRef2Coefficient();
                    if (StringUtils.isNotEmpty(productRef2Coefficient) && Double.parseDouble(productRef2Coefficient) < minCoefficient) {
                        minCoefficient = Double.parseDouble(productRef2Coefficient);
                    }
                }
            }
            /**
             * 计算最低系数
             * 如果系数（报价员录入的系数）底于0.9，由必须由销售副总审核、总经理审批，流程结束；
             * 如果系数0.9--1.0，必须由销售副总审核，流程结束，
             * 如果报价系数大于1.0--1.1则由区域经理审批完成后流程结束；
             * 如果系数大于1.1，则由部门销售经理审核完成后流程结束
             */
            if (minCoefficient <= 0.9) {
                normalFlag = "5";
            } else if (minCoefficient >= 1.1) {
                normalFlag = "2";
            } else if (minCoefficient >= 0.9 && minCoefficient <= 1) {
                normalFlag = "4";
            }else if (minCoefficient >= 1 && minCoefficient <= 1.1) {
                normalFlag = "3";
            }
        }
        bizQuotation.setNormalFlag(normalFlag);
        return normalFlag;
    }

    /**
     * 删除报价单
     */
    @RequiresPermissions("fmis:quotation:remove")
    @Log(title = "报价单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizQuotationService.deleteBizQuotationByIds(ids));
    }

    /**
     * 选择系统用户
     */
    @GetMapping("/selectProduct")
    public String selectProduct(ModelMap mmap) {

        mmap.put("seriesSelect",bizDictService.selectBizDictByProductType(BizConstants.productTypeCode));
        mmap.put("suppliers",bizSuppliersService.selectAllList());

        return prefix + "/selectProduct";
    }
    /**
     * 选择客户
     */
    @GetMapping("/selectCustomer")
    public String selectCustomer() {
        return prefix + "/selectCustomer";
    }
    /**
     * 选择系统用户
     */
    @GetMapping("/selectActuator")
    public String selectActuator(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        mmap.put("productId", productId);
        return prefix + "/selectActuator";
    }


    /**
     * 选择产品配件法兰
     */
    @GetMapping("/selectProductRef1")
    public String selectProductRef1(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        mmap.put("productId", productId);
        return prefix + "/selectProductRef1";
    }

    /**
     * 选择产品配件螺栓
     */
    @GetMapping("/selectProductRef2")
    public String selectProductRef2(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        mmap.put("productId", productId);
        return prefix + "/selectProductRef2";
    }

    @GetMapping("/examineEdit")
    public String examineEdit(ModelMap mmap) {
        String quotationId = getRequest().getParameter("quotationId");
        /*
        BizQuotation bizQuotationQuery = new BizQuotation();
        bizQuotationQuery.setQuotationId(Long.parseLong(quotationId));
        List<BizQuotation> bizQuotationList = bizQuotationService.selectBizQuotationFlowList(bizQuotationQuery);
        mmap.put("bizQuotation", bizQuotationList.get(0));*/


        //
        BizQuotation bizQuotationQuery = new BizQuotation();
        bizQuotationQuery.setQuotationId(Long.parseLong(quotationId));
        List<BizQuotation> bizQuotationList = bizQuotationService.selectBizQuotationFlowList(bizQuotationQuery);

        BizQuotation bizQuotation = bizQuotationList.get(0);

        BizQuotationProduct bizQuotationProduct = new BizQuotationProduct();
        bizQuotationProduct.setQuotationId(bizQuotation.getQuotationId());
        List<BizQuotationProduct> bizQuotationProducts = bizQuotationProductService.selectBizQuotationProductDictList(bizQuotationProduct);
        bizQuotation.setQuotationProductList(bizQuotationProducts);

        String productNames = "";
        String productIds = "";
        for (BizQuotationProduct bizQuotationProduct1 : bizQuotationProducts) {
            productNames += bizQuotationProduct1.getProductName() + ",";
            productIds += bizQuotationProduct1.getProductId() + ",";


            Long productRef1Id = bizQuotationProduct1.getProductRef1Id();
            BizProductRef bizProductRef1 = new BizProductRef();
            if (productRef1Id != null && productRef1Id > 0L) {
                bizProductRef1.setProductRefId(productRef1Id);
                List<BizProductRef> bizProductRefs = bizProductRefService.selectBizProductRefList(bizProductRef1);
                if (!CollectionUtils.isEmpty(bizProductRefs)) {
                    bizQuotationProduct1.setBizProductRef1(bizProductRefs.get(0));
                }
            } else {
                bizQuotationProduct1.setBizProductRef1(bizProductRef1);
            }
            Long productRef2Id = bizQuotationProduct1.getProductRef2Id();
            BizProductRef bizProductRef2 = new BizProductRef();
            if (productRef2Id != null && productRef2Id > 0L) {
                bizProductRef2.setProductRefId(productRef2Id);
                List<BizProductRef> bizProductRefs = bizProductRefService.selectBizProductRefList(bizProductRef2);
                if (!CollectionUtils.isEmpty(bizProductRefs)) {
                    bizQuotationProduct1.setBizProductRef2(bizProductRefs.get(0));
                }
            } else {
                bizQuotationProduct1.setBizProductRef2(bizProductRef1);
            }
            Long actuatorId = bizQuotationProduct1.getActuatorId();
            BizActuator bizActuator = new BizActuator();
            if (actuatorId != null && actuatorId > 0L) {
                bizActuator.setActuatorId(actuatorId);
                List<BizActuator> bizActuators = bizActuatorService.selectBizActuatorForRefList(bizActuator);
                if (!CollectionUtils.isEmpty(bizActuators)) {
                    bizQuotationProduct1.setBizActuator(bizActuators.get(0));
                }
            } else {
                bizQuotationProduct1.setBizActuator(bizActuator);
            }
        }
        mmap.put("productNames", productNames);
        mmap.put("productIds", productIds);

        mmap.put("bizQuotation", bizQuotation);

        return prefix + "/examineEdit";
    }

    @GetMapping("/viewExamine")
    public String viewExamine(ModelMap mmap) {

        String quotationId = getRequest().getParameter("quotationId");
        mmap.put("bizId", quotationId);
        mmap.put("bizTable", BizConstants.BIZ_QUOTATION_TABLE);
        return prefix + "/viewExamine";
    }

    @PostMapping("/doExamine")
    @ResponseBody
    public AjaxResult doExamine(BizQuotation bizQuotation) {
        String examineStatus = bizQuotation.getString4();
        String examineRemark = bizQuotation.getString5();
        String quotationId = bizQuotation.getQuotationId().toString();
        return toAjax(bizQuotationService.doExamine(quotationId,examineStatus,examineRemark));
    }
}
