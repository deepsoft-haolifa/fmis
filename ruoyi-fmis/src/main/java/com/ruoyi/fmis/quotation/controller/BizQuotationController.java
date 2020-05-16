package com.ruoyi.fmis.quotation.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
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
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.service.IBizProductRefService;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotation.service.IBizQuotationService;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import com.ruoyi.fmis.quotationproduct.service.IBizQuotationProductService;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private ISysUserService sysUserService;

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

    @Autowired
    private IBizCustomerService bizCustomerService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private IBizProcessDataService bizProcessDataService;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @RequiresPermissions("fmis:quotation:view")
    @GetMapping()
    public String quotation(ModelMap mmap) {
        int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
        mmap.put("roleType",roleType);
        return prefix + "/quotation";
    }

    @GetMapping("/todoQuotation")
    public String todoQuotation(ModelMap mmap) {
        int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
        mmap.put("roleType",roleType);
        return prefix + "/todoQuotation";
    }

    @ResponseBody
    @PostMapping("/todoView")
    public AjaxResult todoView() {
        int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
        BizQuotation bizQuotation = new BizQuotation();
        bizQuotation.setString2(roleType + "");
        bizQuotation.setString6("1");
        HashMap<String, Object> result = new HashMap<>();
        result.put("quotationNum", bizQuotationService.selectBizQuotationFlowList(bizQuotation).size());

        //合同待办
        BizProcessData bizProcessData = new BizProcessData();
        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(BizConstants.BIZ_contract);
        String userFlowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            userFlowStatus = flowMap.keySet().iterator().next();
            bizProcessData.setRoleType(userFlowStatus);
        }
        bizProcessData.setQueryStatus("1");
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRef(bizProcessData);
        result.put("contractNum", list.size());

        return AjaxResult.success(result);
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


        String string6 = bizQuotation.getString6();
        if ("3".equals(string6)) {
            bizQuotation.setString2("-1");
        } else {
            int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
            bizQuotation.setString2(roleType + "");
        }


        startPage();
        List<BizQuotation> list = bizQuotationService.selectBizQuotationFlowList(bizQuotation);
        return getDataTable(list);
    }

    /**
     * 查询报价单产品
     * @param bizQuotation
     * @return
     */
    @PostMapping("/listProduct")
    @ResponseBody
    public TableDataInfo listProduct(BizQuotation bizQuotation) {


        startPage();
        List<BizQuotation> list = bizQuotationService.selectBizQuotationProductList(bizQuotation);
        return getDataTable(list);
    }




    /**
     * 导出报价单列表
     */
    @RequiresPermissions("fmis:quotation:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizQuotation bizQuotation) {
        return createPdf(null,null,bizQuotation);
    }

    public AjaxResult createPdf (HttpServletRequest request,HttpServletResponse response,BizQuotation bizQuotationParamter) {
        String id = "";
        if (bizQuotationParamter == null) {
            id = request.getParameter("id");
        } else {
            id = bizQuotationParamter.getQuotationId().toString();
        }
        //报价单
        BizQuotation bizQuotation = bizQuotationService.selectBizQuotationById(Long.parseLong(id));
        //产品信息
        BizQuotationProduct bizQuotationProduct = new BizQuotationProduct();
        bizQuotationProduct.setQuotationId(bizQuotation.getQuotationId());
        List<BizQuotationProduct> bizQuotationProducts = bizQuotationProductService.selectBizQuotationProductDictList(bizQuotationProduct);

        BizQuotation queryBizQuotation = new BizQuotation();
        queryBizQuotation.setQuotationId(bizQuotation.getQuotationId());
        List<BizQuotation> bizQuotationList = bizQuotationService.selectBizQuotationFlowList(queryBizQuotation);
        BizQuotation bizQuotationDict = bizQuotationList.get(0);
        try
        {
            String filename = PdfUtil.encodingFilename("报价单");
            String filePath = PdfUtil.getAbsoluteFile(filename);
            // step 1
            Document document = new Document(PageSize.A4);
            // step 2
            //new FileOutputStream(filePath)
            PdfWriter writer = null;
            if (response != null) {
                writer = PdfWriter.getInstance(document, response.getOutputStream());
            } else {
                writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            }
            // 只读
            writer.setEncryption(null, null, PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);
            //设置字体样式
            //正常
            Font textFont = PdfUtil.getPdfChineseFont(7,Font.NORMAL);
            //加粗
            Font boldFont = PdfUtil.getPdfChineseFont(11,Font.BOLD);
            //二级标题
            Font titleFont = PdfUtil.getPdfChineseFont(15,Font.BOLD);

            Paragraph paragraph = new Paragraph("北京好利阀业集团有限公司", titleFont);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            if (bizQuotationParamter != null) {
                writer.setPageEvent(new TextWaterMarkPdfPageEvent("北京好利"));
            }
            //空行
            Paragraph blankRow = new Paragraph(18f, " ");


            //添加副标题
            Font simheiMiddle = textFont;
            PdfPTable tbSubtitle = new PdfPTable(1);
            PdfPCell cellSubtitle = new PdfPCell();
            Paragraph pSubtitle = new Paragraph("Beijing HAOLIFA Valve Group Co.,LTD", simheiMiddle);
            pSubtitle.setAlignment(Paragraph.ALIGN_CENTER);
            cellSubtitle.addElement(pSubtitle);
            cellSubtitle.setPaddingBottom(5);
            cellSubtitle.setBorderWidthTop(0);
            cellSubtitle.setBorderWidthLeft(0);
            cellSubtitle.setBorderWidthRight(0);
            cellSubtitle.setBorderWidthBottom(2);
            cellSubtitle.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tbSubtitle.addCell(cellSubtitle);
            tbSubtitle.setHorizontalAlignment(Paragraph.ALIGN_CENTER);


            //总列数
            int totalColumn = 15;
            PdfPTable table = new PdfPTable(totalColumn);

            //table前间距
            table.setSpacingBefore(10f);
            // 设置各列列宽
            table.setTotalWidth(500);
            //锁定列宽
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);

            String customerId = bizQuotation.getString2();
            BizCustomer bizCustomer = new BizCustomer();
            if (StringUtils.isNotEmpty(customerId)) {
                bizCustomer = bizCustomerService.selectBizCustomerById(Long.parseLong(customerId));
            }
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(bizQuotation.getCreateBy()));

            // 第一行
            table.addCell(PdfUtil.mergeCol("收件人：", 2,textFont));
            table.addCell(PdfUtil.mergeCol("", 6,textFont));

            table.addCell(PdfUtil.mergeCol("报价人：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(sysUser.getUserName(), 5,textFont));
            //第二行
            table.addCell(PdfUtil.mergeCol("公司：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizCustomer.getName(), 6,textFont));
            table.addCell(PdfUtil.mergeCol("日期：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(DateUtils.dateTime(bizQuotation.getCreateTime()), 5,textFont));
            //第三行
            table.addCell(PdfUtil.mergeCol("电话号码：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizCustomer.getContactPhone(), 6,textFont));
            table.addCell(PdfUtil.mergeCol("电话：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(sysUser.getPhonenumber(), 5,textFont));
            //第四行
            table.addCell(PdfUtil.mergeCol("传真/邮箱：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizCustomer.getContactEmail(), 6,textFont));
            table.addCell(PdfUtil.mergeCol("传真/邮箱：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(sysUser.getEmail(), 5,textFont));

            //第五行
            table.addCell(PdfUtil.mergeCol("单号：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getString1(), 6,textFont));
            table.addCell(PdfUtil.mergeCol("制单：", 2,textFont));
            table.addCell(PdfUtil.mergeCol("", 5,textFont));

            //第六行
            table.addCell(PdfUtil.mergeCol("项目名称：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizQuotation.getReportProject(), 13,textFont));
            //联系人结束



            //第七行 产品数据开始 bizQuotationProducts
            table.addCell(PdfUtil.mergeCol("序号", 1,textFont));
            table.addCell(PdfUtil.mergeCol("名称", 1,textFont));
            table.addCell(PdfUtil.mergeCol("型号", 2,textFont));
            table.addCell(PdfUtil.mergeCol("规格", 1,textFont));
            //table.addCell(PdfUtil.mergeCol("压力", 1));//不需要
            table.addCell(PdfUtil.mergeCol("阀体材质", 1,textFont));
            table.addCell(PdfUtil.mergeCol("阀芯材质", 1,textFont));
            table.addCell(PdfUtil.mergeCol("密封材质", 1,textFont));
            //table.addCell(PdfUtil.mergeCol("驱动形式", 1));//不需要
            table.addCell(PdfUtil.mergeCol("连接方式", 1,textFont));
            table.addCell(PdfUtil.mergeCol("数量", 1,textFont));
            table.addCell(PdfUtil.mergeCol("单价", 1,textFont));
            table.addCell(PdfUtil.mergeCol("合计", 1,textFont));
            table.addCell(PdfUtil.mergeCol("备注", 3,textFont));

            Double sumTotalNum = new Double(0);
            Double sumTotalPrice = new Double(0);
            Double sumTotalAmount = new Double(0);
            Double sumTotalNumRef1 = new Double(0);
            Double sumTotalNumRef2 = new Double(0);

            DecimalFormat data = new DecimalFormat("#.0000");
            for (int i = 0; i < bizQuotationProducts.size(); i++) {
                BizQuotationProduct bizProduct = bizQuotationProducts.get(i);
                BizProduct bizProductObj = bizProduct.getBizProduct();
                if (bizProductObj == null) {
                    continue;
                }
                table.addCell(PdfUtil.mergeCol("" + (i + 1), 1,textFont));
                table.addCell(PdfUtil.mergeCol(bizProductObj.getName(), 1,textFont));
                table.addCell(PdfUtil.mergeCol(bizProductObj.getModel(), 2,textFont));
                table.addCell(PdfUtil.mergeCol(bizProductObj.getSpecifications(), 1,textFont));//规格
                //table.addCell(PdfUtil.mergeCol(bizProductObj.getNominalPressure(), 1));//压力
                table.addCell(PdfUtil.mergeCol(bizProductObj.getValvebodyMaterial(), 1,textFont));//阀体
                table.addCell(PdfUtil.mergeCol(bizProductObj.getValveElement(), 1,textFont));//阀芯
                //table.addCell(PdfUtil.mergeCol("", 1));//阀板
                table.addCell(PdfUtil.mergeCol(bizProductObj.getSealingMaterial(), 1,textFont));//密封材质
                //table.addCell(PdfUtil.mergeCol(bizProductObj.getDriveForm(), 1));//驱动形式
                table.addCell(PdfUtil.mergeCol(bizProductObj.getConnectionType(), 1,textFont));//连接方式
                table.addCell(PdfUtil.mergeCol(bizProduct.getProductNum(), 1,textFont));//数量

                //总价计算
                Double productPrice = bizProductObj.getPrice();
                String productNum = bizProduct.getProductNum();

                sumTotalNum = sumTotalNum + Double.parseDouble(productNum);

                Double productTotal = new Double(0);
                String productCoefficient = bizProduct.getProductCoefficient();
                if (StringUtils.isNotEmpty(productNum) && productPrice > 0 && StringUtils.isNotEmpty(productCoefficient)) {
                    productTotal = Double.parseDouble(productNum) * productPrice * Double.parseDouble(productCoefficient);
                }
                //法兰计算
                BizProductRef bizProductRef1 = bizProduct.getBizProductRef1();
                Double ref1Total = new Double(0);
                if (bizProductRef1 != null ) {
                    Double ref1Price = bizProductRef1.getPrice();
                    String ref1Num = bizProduct.getProductRef1Num();
                    String ref1Coefficient = bizProduct.getProductRef1Coefficient();
                    if (StringUtils.isNotEmpty(ref1Num) && ref1Price > 0 && StringUtils.isNotEmpty(ref1Coefficient)) {
                        ref1Total = Double.parseDouble(ref1Num) * ref1Price * Double.parseDouble(ref1Coefficient);
                        sumTotalNumRef1 = sumTotalNumRef1 + Double.parseDouble(ref1Num);
                    }
                }
                //螺栓计算
                BizProductRef bizProductRef2 = bizProduct.getBizProductRef2();
                Double ref2Tota = new Double(0);
                if (bizProductRef2 != null ) {
                    Double ref2Price = bizProductRef2.getPrice();
                    String ref2Num = bizProduct.getProductRef2Num();
                    String ref2Coefficient = bizProduct.getProductRef2Coefficient();
                    if (StringUtils.isNotEmpty(ref2Num) && ref2Price > 0 && StringUtils.isNotEmpty(ref2Coefficient)) {
                        ref2Tota = Double.parseDouble(ref2Num) * ref2Price * Double.parseDouble(ref2Coefficient);
                        sumTotalNumRef2 = sumTotalNumRef2 + Double.parseDouble(ref2Num);
                    }
                }

                //执行器计算
                BizActuator bizActuator = bizProduct.getBizActuator();
                Double actuatorTotal = new Double(0);
                if (bizActuator != null) {
                    Double actuatorPrice = bizActuator.getPrice();
                    String actuatorNum = bizProduct.getActuatorNum();
                    String actuatorCoefficient = bizProduct.getActuatorCoefficient();
                    if (StringUtils.isNotEmpty(actuatorNum) && actuatorPrice > 0 && StringUtils.isNotEmpty(actuatorCoefficient)) {
                        actuatorTotal = Double.parseDouble(actuatorNum) * actuatorPrice * Double.parseDouble(actuatorCoefficient);
                    }
                }

                Double totalAmount = new Double(0);
                totalAmount = productTotal + ref1Total + ref2Tota + actuatorTotal;

                sumTotalAmount = sumTotalAmount + totalAmount;

                //总单价
                Double productTotalPrice = totalAmount / Double.parseDouble(productNum);
                sumTotalPrice = sumTotalPrice + productTotalPrice;
                table.addCell(PdfUtil.mergeCol(data.format(productTotalPrice), 1,textFont));//单价

                table.addCell(PdfUtil.mergeCol(data.format(totalAmount), 1,textFont));//合计
                table.addCell(PdfUtil.mergeCol(bizProduct.getString4(), 4,textFont));
            }


            //金额合计
            String totalRemark = "阀门：" + sumTotalNum + " 台   法兰合计：" + sumTotalNumRef1 + " 片   螺栓合计：" + sumTotalNumRef2 + " 条   总金额：" + sumTotalAmount;
            table.addCell(PdfUtil.mergeCol(totalRemark, 15,boldFont));



            // 特别提醒
            Paragraph paragraphRemark = new Paragraph();
            Font remarkFont = PdfUtil.getPdfChineseFont(10, Font.NORMAL);
            Font fontMoney = PdfUtil.getPdfChineseFont(13, Font.NORMAL, BaseColor.RED);
            Font blackFont = PdfUtil.getPdfChineseFont(12, Font.NORMAL, BaseColor.BLACK);
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("", blackFont));
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("        1、价格：以上价格为含税不含运费成交价格；", remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("        2、供货周期： " + bizQuotationDict.getLeadTime() + "  个工作日", remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);
            String payMethod = dictDataService.selectDictLabel("payment_method",bizQuotationDict.getPaymentMethod());
            paragraphRemark.add(new Chunk("        3、供货方式： " + payMethod + " 款到发货；", remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("        4、价格有效期     天；", remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);



            Paragraph paragraphRemark1 = new Paragraph();
            Font remarkFont1 = PdfUtil.getPdfChineseFont(10, Font.NORMAL);


            //sysUser 递归查询上级部门查找地址
            String remark = sysDeptService.getDeptRemarkForUserPdf(sysUser);
            String remark1 = "Add：北京市 东城区 广渠门内大街90号 新裕商务大厦506#  邮编：100062";
            String remark2 = "北京好利集团商务管理中心";
            String remark3 = "010-67110192";
            String remark4 = "010-67171220";
            if (StringUtils.isNotEmpty(remark)) {
                String[] remarks = remark.split("###");
                if (remarks.length == 4) {
                    remark1 = remarks[0];
                    remark2 = remarks[1];
                    remark3 = remarks[2];
                    remark4 = remarks[3];
                }
            }
            paragraphRemark1.add(new Chunk(remark1, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark2, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark3, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.add(new Chunk(remark4, remarkFont1));
            paragraphRemark1.add(Chunk.NEWLINE);
            paragraphRemark1.setAlignment(Paragraph.ALIGN_RIGHT);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Paragraph datePar = new Paragraph("打印日期：" + sdf.format(new Date()), PdfUtil.getPdfChineseFont());
            datePar.setAlignment(Element.ALIGN_RIGHT);
            datePar.setSpacingBefore(20);



            document.open();
            document.add(paragraph);
            document.add(blankRow);
            document.add(tbSubtitle);

            // step 4 写入内容
            document.add(table);
            document.add(paragraphRemark);
            document.add(paragraphRemark1);
            document.add(datePar);

            // step 5
            document.close();
            writer.close();
            return AjaxResult.success(filename);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
    }

    @GetMapping("/viewPdf")
    public void viewPdf(HttpServletRequest request,HttpServletResponse response) {
        createPdf(request,response,null);
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
        Long userId = ShiroUtils.getUserId();

        bizQuotation.setNoLoginId(userId.toString());

        List<BizQuotation> list = bizQuotationService.selectBizQuotationList(bizQuotation);
        Iterator<BizQuotation> it = list.iterator();
        while(it.hasNext()){
            BizQuotation  bizQuotation1 = it.next();
            if (bizQuotation1.getQuotationId().compareTo(id) == 0) {
                it.remove();
            } else if ((!bizQuotation.getFlowStatus().equals("0")) && bizQuotation.getFlowStatus().equals(bizQuotation.getNormalFlag())) {
                it.remove();
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
                List<BizActuator> bizActuators = bizActuatorService.selectBizActuatorList(bizActuator);
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
        String specialExpenses = bizQuotation.getSpecialExpenses();
        if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 1000000) {
            normalFlag = "5";
        }
        /*else if ("1".equals(bizQuotation.getPaymentMethod()) || "6".equals(bizQuotation.getPaymentMethod())) {
            //到付
            normalFlag = "5";
        } */
        else if (StringUtils.isNotEmpty(specialExpenses) && !"0".equals(specialExpenses)) {
            //特殊费用
            normalFlag = "5";
        } else if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 500000) {
            normalFlag = "4";
        } else if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 200000) {
            normalFlag = "3";
        }  else {

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
            if (minCoefficient < 0.88) {
                normalFlag = "5";
            }else if (minCoefficient >= 0.88 && minCoefficient < 0.95) {
                normalFlag = "4";
            } else if (minCoefficient >= 0.95 && minCoefficient < 1) {
                normalFlag = "3";
            } else {
                normalFlag = "2";
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

    @GetMapping("/viewDetail")
    public String viewDetail(ModelMap mmap) {
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

        return prefix + "/viewDetail";
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
        String examineStatus = bizQuotation.getString6();
        String examineRemark = bizQuotation.getString5();
        String quotationId = bizQuotation.getQuotationId().toString();
        return toAjax(bizQuotationService.doExamine(quotationId,examineStatus,examineRemark));
    }
}
