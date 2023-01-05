package com.ruoyi.fmis.quotation.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.itextpdf.PdfUtil;
import com.ruoyi.common.utils.itextpdf.TextWaterMarkPdfPageEvent;
import com.ruoyi.common.utils.poi.ExcelProcessDataUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.BizProductExcel;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.finance.domain.QuotationEx;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.ruoyi.common.utils.itextpdf.PdfUtil.getAbsoluteFile;

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

    @Autowired
    private IBizProductService bizProductService;

    @RequiresPermissions("fmis:quotation:view")
    @GetMapping()
    public String quotation(ModelMap mmap) {
        int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
        mmap.put("roleType",roleType);
        mmap.put("loginId",ShiroUtils.getUserId());
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
        //临时用userId
        bizProcessData.setString30(ShiroUtils.getUserId() + "");
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRef(bizProcessData);
        result.put("contractNum", list.size());

        return AjaxResult.success(result);
    }


    public String getBh () {
        Long deptId = ShiroUtils.getSysUser().getDeptId();
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        SysDept sysDept1 = sysDeptService.selectDeptById(sysDept.getParentId());
        String areCode = "";
        if(Objects.nonNull(sysDept1)) {
            areCode = sysDept1.getAreCode();
        }
        String bh = areCode + "-" + DateUtils.dateTimeNow();
        return bh;
    }

    /**
     * 查询报价单列表
     */
    //@RequiresPermissions("fmis:quotation:list")
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
        if (bizQuotation.getCustomerId() != null){
            BizCustomer bizCustomer = bizCustomerService.selectBizCustomerById(Long.parseLong(bizQuotation.getCustomerId()));
            bizQuotation.setCustomerName(bizCustomer.getName());
        }
         //临时用userId
        bizQuotation.setString20(ShiroUtils.getUserId() + "");

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
     * 查询报价单产品
     * @param bizQuotation
     * @return
     */
    @PostMapping("/listProductNoPage")
    @ResponseBody
    public TableDataInfo listProductNoPage(BizQuotation bizQuotation) {
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
            Paragraph paragraph1 = new Paragraph("报价单", textFont);
            paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
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
            table.addCell(PdfUtil.mergeCol("公司：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(bizCustomer.getName(), 6,textFont));


            table.addCell(PdfUtil.mergeCol("报价人：", 2,textFont));
            table.addCell(PdfUtil.mergeCol(sysUser.getUserName(), 5,textFont));
            //第二行
            table.addCell(PdfUtil.mergeCol("收件人：", 2,textFont));
            table.addCell(PdfUtil.mergeCol("", 6,textFont));

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
            table.addCell(PdfUtil.mergeCol("型号", 1,textFont));
            table.addCell(PdfUtil.mergeCol("规格", 1,textFont));
            table.addCell(PdfUtil.mergeCol("压力", 1));//不需要
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

            DecimalFormat data = new DecimalFormat("#");
            for (int i = 0; i < bizQuotationProducts.size(); i++) {

                String remark = "含";

                BizQuotationProduct bizProduct = bizQuotationProducts.get(i);
                BizProduct bizProductObj = bizProduct.getBizProduct();
                if (bizProductObj == null) {
                    continue;
                }
                table.addCell(PdfUtil.mergeCol("" + (i + 1), 1,textFont));


                String productName = bizProductObj.getName();
                String model = bizProductObj.getModel();
                //执行器计算
                BizActuator bizActuator = bizProduct.getBizActuator();
                Double actuatorTotal = new Double(0);
                if (bizActuator != null) {
                    Double actuatorPrice = bizActuator.getPrice();
                    String actuatorNum = bizProduct.getActuatorNum();
                    String actuatorCoefficient = bizProduct.getActuatorCoefficient();
                    if (StringUtils.isNotEmpty(actuatorNum) && actuatorPrice > 0 && StringUtils.isNotEmpty(actuatorCoefficient)) {
                        actuatorTotal = Double.parseDouble(actuatorNum) * actuatorPrice * Double.parseDouble(actuatorCoefficient);

                        String type = bizActuator.getString2();
                        String repStr = "气动";
                        String appendStr = "6";
                        if ("1".equals(type)) {
                            repStr = "电动";
                            appendStr = "9";
                        }
                        productName = productName.replaceAll("无头",repStr);
                        String startS = model.substring(0, 1);
                        model = model.substring(2, model.length());
                        model = startS + appendStr + model;
                       /* if (model.startsWith("D")) {
                            model = model.substring(1,model.length());
                            model = "D" + appendStr + model;
                        }*/

                    }
                }
                table.addCell(PdfUtil.mergeCol(productName, 1,textFont));


                table.addCell(PdfUtil.mergeCol(model, 1,textFont));
                table.addCell(PdfUtil.mergeCol(bizProductObj.getSpecifications(), 1,textFont));//规格
                table.addCell(PdfUtil.mergeCol(bizProductObj.getNominalPressure(), 1,textFont));//压力
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
                        remark += "法兰";
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
                        remark += "螺栓";
                    }
                }



                Double totalAmount = new Double(0);
//                totalAmount = productTotal + ref1Total + ref2Tota + actuatorTotal;
                Double productTotalPrice = Double.valueOf(bizProduct.getTotalPriceOnly()); //单价
                totalAmount = productTotalPrice * Double.parseDouble(productNum);

                sumTotalAmount = sumTotalAmount + totalAmount;

                //总单价
//                Double productTotalPrice = Double.valueOf(totalAmount / Double.parseDouble(productNum));

                sumTotalPrice = sumTotalPrice + productTotalPrice;
                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString(productTotalPrice), 1,textFont));//单价

                table.addCell(PdfUtil.mergeCol(productTotalPrice * Double.parseDouble(productNum) + "", 1,textFont));//合计
//                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(totalAmount), 1,textFont));//合计

                if (remark.length() == 1) {
                    remark = "";
                }
                if (bizProduct.getBizProduct().getSeries().equals("其它")) {
                    table.addCell(PdfUtil.mergeCol(bizProduct.getString4(), 4,textFont));
                } else {
//                    table.addCell(PdfUtil.mergeCol(remark, 4,textFont));
                    table.addCell(PdfUtil.mergeCol(bizProduct.getString4(), 4,textFont));
                }


            }


            //金额合计
            String totalRemark = "阀门：" + sumTotalNum + " 台   法兰合计：" + sumTotalNumRef1 + " 片   螺栓合计：" + sumTotalNumRef2 + " 条   总金额：" + sumTotalAmount;
            table.addCell(PdfUtil.mergeColRight("合计", 9,textFont));
            table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalNum), 1,textFont));//总数量
            if (bizQuotation.getDiscount()!= null && !bizQuotation.getDiscount().equals("0") && !bizQuotation.getDiscount().equals("0.00")) {
//                table.addCell(PdfUtil.mergeCol("优惠金额：" + bizQuotation.getDiscount(), 1,textFont));//单价
                table.addCell(PdfUtil.mergeCol("", 1,textFont));//优惠金额
                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalAmount - Double.parseDouble(bizQuotation.getDiscount())), 1,textFont));//合计
                table.addCell(PdfUtil.mergeCol("", 3,textFont));//备注
            } else {
                table.addCell(PdfUtil.mergeCol(StringUtils.getDoubleString0(sumTotalAmount), 1,textFont));//合计
                table.addCell(PdfUtil.mergeCol("", 1,textFont));//优惠金额
                table.addCell(PdfUtil.mergeCol("", 3,textFont));//备注
            }
            /*table.addCell(PdfUtil.mergeCol("", 1,textFont));//单价
            table.addCell(PdfUtil.mergeCol("", 1,textFont));//合计
            table.addCell(PdfUtil.mergeCol("", 3,textFont));//备注*/

            table.addCell(PdfUtil.mergeCol("优惠金额：" + bizQuotation.getDiscount(), 3,textFont));
            table.addCell(PdfUtil.mergeCol("合计：" + StringUtils.getDoubleString0(sumTotalAmount), 3,textFont));
            table.addCell(PdfUtil.mergeCol("大写人民币合计:" + StringUtils.convert(sumTotalAmount - Double.parseDouble(bizQuotation.getDiscount())), 6,textFont));
            table.addCell(PdfUtil.mergeCol("", 3,textFont));//备注

            // 特别提醒
            Paragraph paragraphRemark = new Paragraph();
            Font remarkFont = PdfUtil.getPdfChineseFont(10, Font.NORMAL);
            Font fontMoney = PdfUtil.getPdfChineseFont(13, Font.NORMAL, BaseColor.RED);
            Font blackFont = PdfUtil.getPdfChineseFont(12, Font.NORMAL, BaseColor.BLACK);
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("", blackFont));
            paragraphRemark.add(Chunk.NEWLINE);
            String included_freight = dictDataService.selectDictLabel("included_freight",bizQuotation.getString11());
            paragraphRemark.add(new Chunk("        1、价格：" + included_freight, remarkFont));


            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("        2、供货周期： " + bizQuotationDict.getLeadTime() + "  个工作日", remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);
            String payMethod = dictDataService.selectDictLabel("payment_method",bizQuotationDict.getPaymentMethod());
            if(payMethod.equals("其他")) {
                payMethod = StringUtils.isNotEmpty(bizQuotation.getString6())? bizQuotation.getString6():payMethod;
            }
            paragraphRemark.add(new Chunk("        3、供货方式： " + payMethod , remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("        4、价格有效期：  " + bizQuotation.getString12() + "   天；", remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);
            paragraphRemark.add(new Chunk("        5、备注：" + bizQuotation.getRemark(), remarkFont));
            paragraphRemark.add(Chunk.NEWLINE);



            Paragraph paragraphRemark1 = new Paragraph();
            Font remarkFont1 = PdfUtil.getPdfChineseFont(10, Font.NORMAL);


            //sysUser 递归查询上级部门查找地址
            String remark = sysDeptService.getDeptRemarkForUserPdf(sysUser);
            String remark1 = "Add：北京市 东城区 广渠门内大街90号 新裕商务大厦506#  邮编：100062";
            String remark2 = "北京好利集团商务管理中心";
            String remark3 = "010-67110192";
            String remark4 = "010-67171220";
            if (bizQuotation.getString19()!=null && bizQuotation.getString19().equals("北京好利阀业集团有限公司")) {
                remark1 = "Add：北京市大兴区榆垡镇榆顺路6号";
                remark2 = "北京好利阀业集团有限公司";
                remark3 = "010-89291733";
                remark4 = "";
            }
            if (bizQuotation.getString19()!=null && bizQuotation.getString19().equals("上海好利阀门技术有限公司")) {
                remark1 = "Add：上海市闵行区兴梅路375号";
                remark2 = "上海好利阀门技术有限公司";
                remark3 = "021-24206461";
                remark4 = "";
            }
            if (bizQuotation.getString19()!=null && bizQuotation.getString19().equals("北京好利时代科技发展有限公司")) {
                remark1 = "Add：北京市东城区幸福大街幸福家园7号商务楼605室";
                remark2 = "北京好利时代科技发展有限公司";
                remark3 = "010-67134151";
                remark4 = "";
            }
            if (bizQuotation.getString19()!=null && bizQuotation.getString19().equals("北京大宇合力科技有限责任公司")) {
                remark1 = "Add：北京市东城区广渠门内大街90号新裕商务大厦506室";
                remark2 = "北京大宇合力科技有限责任公司";
                remark3 = "010-67110192";
                remark4 = "";
            }
            if (bizQuotation.getString19()!=null && bizQuotation.getString19().equals("山西好利阀机械制造有限公司")) {
                remark1 = "Add：侯马经济开发区旺旺北支路东侧";
                remark2 = "山西好利阀机械制造有限公司";
                remark3 = "0357-3563581";
                remark4 = "";
            }
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
            //document.add(blankRow);
            document.add(paragraph1);
            //document.add(blankRow);
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
            throw new BusinessException("导出失败，请联系网站管理员！");
        }
    }


    /**
     * 导出报价单excel
     */
    @RequiresPermissions("fmis:quotation:export")
    @PostMapping("/exportEx")
    @ResponseBody
    public AjaxResult exportEx(HttpServletRequest request,HttpServletResponse response,BizQuotation bizQuotationRequest) {
        String id = "";
        if (bizQuotationRequest == null) {
            id = request.getParameter("id");
        } else {
            id = bizQuotationRequest.getQuotationId().toString();
        }
        //报价单
        BizQuotation bizQuotation = bizQuotationService.selectBizQuotationById(Long.parseLong(id));
        //产品信息
        BizQuotationProduct bizQuotationProduct = new BizQuotationProduct();
        bizQuotationProduct.setQuotationId(bizQuotation.getQuotationId());
        List<BizQuotationProduct> bizQuotationProducts = bizQuotationProductService.selectBizQuotationProductDictList(bizQuotationProduct);
        List<QuotationEx> quotationExes = new ArrayList<>();
        int i = 1;
        for (BizQuotationProduct bizQuotationProduct1 : bizQuotationProducts) {
            QuotationEx quotationEx = new QuotationEx();
            quotationEx.setNumber(i);
            quotationEx.setProductId(bizQuotationProduct1.getBizProduct().getModel() + bizQuotationProduct1.getBizProduct().getSpecifications());
            quotationEx.setProductName(bizQuotationProduct1.getBizProduct().getName());
            quotationEx.setPressure(bizQuotationProduct1.getBizProduct().getNominalPressure());
            quotationEx.setSpecifications(bizQuotationProduct1.getBizProduct().getSpecifications());
            quotationEx.setpNumber(bizQuotationProduct1.getProductNum());
            quotationEx.setProductPrice(bizQuotationProduct1.getBizProduct().getPrice() + "");
            quotationEx.setTotal(Integer.parseInt(bizQuotationProduct1.getProductNum())* bizQuotationProduct1.getBizProduct().getPrice() + "");
            String caizhi = "阀体材质:" + bizQuotationProduct1.getBizProduct().getValvebodyMaterial();
            caizhi = caizhi + "\\r\\n" + "阀板材质:" + bizQuotationProduct1.getBizProduct().getValveMaterial();
            caizhi = caizhi + "\\r\\n"  + "密封材质:" + bizQuotationProduct1.getBizProduct().getSealingMaterial();
            caizhi = caizhi + "\\r\\n" + "阀芯材质:" + bizQuotationProduct1.getBizProduct().getValveElement();
            quotationEx.setCaizhi(caizhi);
            quotationEx.setRemark(bizQuotationProduct1.getRemark());
            quotationExes.add(quotationEx);
            i++;
        }
        ExcelUtil<QuotationEx> util = new ExcelUtil<QuotationEx>(QuotationEx.class);

        return util.exportExcel(quotationExes, "quotation");
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
        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(BizConstants.BIZ_contract);
        String lastRoleKey = "";
        for (String key : flowMap.keySet()) {
            lastRoleKey = key;
        }
        if (!"1".equals(lastRoleKey)) {
            bizQuotation.setFlowStatus(lastRoleKey + "0");
        }
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
            }/* else if ((!bizQuotation.getFlowStatus().equals("0")) && bizQuotation.getFlowStatus().equals(bizQuotation.getNormalFlag())) {
                it.remove();
            }*/
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
        if (bizQuotation.getSpecialExpenses() == null || bizQuotation.getSpecialExpenses().trim().equals("")) {
            bizQuotation.setSpecialExpenses("0");
        }
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
        String normalFlag = "2";
        int num = 2;
        String totalPrice = bizQuotation.getString9();
        String specialExpenses = bizQuotation.getSpecialExpenses();

        if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 1000000) {
            normalFlag = "5";
            num = 5;
        }
        /*else if ("1".equals(bizQuotation.getPaymentMethod()) || "6".equals(bizQuotation.getPaymentMethod())) {
            //到付
            normalFlag = "5";
        } */
        else if (StringUtils.isNotEmpty(specialExpenses) && !"0".equals(specialExpenses)) {
            //特殊费用
            normalFlag = "5";
            num = 5;
        } else if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 500000) {
            normalFlag = "4";
            num = 4;
        } else if (StringUtils.isNotEmpty(totalPrice) && Double.parseDouble(totalPrice) >= 200000) {
            normalFlag = "3";
            num = 3;
        }
        JSONArray productArray = JSONArray.parseArray(productArrayStr);
        Double minCoefficient = new Double(1);
        //未修改的并且系数为1的时候的价格
        Double price = 0.0;
        for (int i = 0; i < productArray.size(); i++) {
            JSONObject json = productArray.getJSONObject(i);
            BizQuotationProduct bizQuotationProduct = JSONObject.parseObject(json.toJSONString(), BizQuotationProduct.class);
            if (bizQuotationProduct.getProductId() != null) {
                /*String actuatorCoefficient = bizQuotationProduct.getActuatorCoefficient();
                if (StringUtils.isNotEmpty(actuatorCoefficient) && Double.parseDouble(actuatorCoefficient) < minCoefficient
                        && Double.parseDouble(actuatorCoefficient) > 0) {
                    minCoefficient = Double.parseDouble(actuatorCoefficient);
                }
                String productCoefficient = bizQuotationProduct.getProductCoefficient();
                if (StringUtils.isNotEmpty(productCoefficient) && Double.parseDouble(productCoefficient) < minCoefficient
                        && Double.parseDouble(productCoefficient) > 0) {
                    minCoefficient = Double.parseDouble(productCoefficient);
                }

                String productRef1Coefficient = bizQuotationProduct.getProductRef1Coefficient();
                if (StringUtils.isNotEmpty(productRef1Coefficient) && Double.parseDouble(productRef1Coefficient) < minCoefficient
                        && Double.parseDouble(productRef1Coefficient) > 0) {
                    minCoefficient = Double.parseDouble(productRef1Coefficient);
                }

                String productRef2Coefficient = bizQuotationProduct.getProductRef2Coefficient();
                if (StringUtils.isNotEmpty(productRef2Coefficient) && Double.parseDouble(productRef2Coefficient) < minCoefficient
                        && Double.parseDouble(productRef2Coefficient) > 0) {
                    minCoefficient = Double.parseDouble(productRef2Coefficient);
                }*/
                String productAllCoefficient = bizQuotationProduct.getString6();
                if (StringUtils.isNotEmpty(productAllCoefficient) && Double.parseDouble(productAllCoefficient) < minCoefficient
                        && Double.parseDouble(productAllCoefficient) > 0) {
                    minCoefficient = Double.parseDouble(productAllCoefficient);
                }
                if (bizQuotationProduct.getPrice1() == null) {
                    bizQuotationProduct.setPrice1(0.0);
                }
                if (bizQuotationProduct.getPrice2() == null) {
                    bizQuotationProduct.setPrice2(0.0);
                }
                if (bizQuotationProduct.getPrice5() == null) {
                    bizQuotationProduct.setPrice5(0.0);
                }
                if (bizQuotationProduct.getPrice4() == null) {
                    bizQuotationProduct.setPrice4(0.0);
                }
                price = price + Integer.parseInt(bizQuotationProduct.getProductNum())*bizQuotationProduct.getPrice1();
                price = price + Integer.parseInt(bizQuotationProduct.getActuatorNum())*bizQuotationProduct.getPrice4();
                price = price + Integer.parseInt(bizQuotationProduct.getProductRef1Num())*bizQuotationProduct.getPrice2();
                price = price + Integer.parseInt(bizQuotationProduct.getProductRef2Num())*bizQuotationProduct.getPrice5();
                if (bizQuotationProduct.getPattachment1Price() == null) {
                    bizQuotationProduct.setPattachment1Price(0.0);
                }
                if (bizQuotationProduct.getPattachment2Price() == null) {
                    bizQuotationProduct.setPattachment2Price(0.0);
                }
                if (bizQuotationProduct.getPattachment3Price() == null) {
                    bizQuotationProduct.setPattachment3Price(0.0);
                }
                if (bizQuotationProduct.getPattachment4Price() == null) {
                    bizQuotationProduct.setPattachment4Price(0.0);
                }
                if (bizQuotationProduct.getPattachmentPrice() == null) {
                    bizQuotationProduct.setPattachmentPrice(0.0);
                }

                if (bizQuotationProduct.getPattachment1Count() == null) {
                    bizQuotationProduct.setPattachment1Count(0.0);
                }
                if (bizQuotationProduct.getPattachment2Count() == null) {
                    bizQuotationProduct.setPattachment2Count(0.0);
                }
                if (bizQuotationProduct.getPattachment3Count() == null) {
                    bizQuotationProduct.setPattachment3Count(0.0);
                }
                if (bizQuotationProduct.getPattachment4Count() == null) {
                    bizQuotationProduct.setPattachment4Count(0.0);
                }
                if (bizQuotationProduct.getPattachmentCount() == null) {
                    bizQuotationProduct.setPattachmentCount(0.0);
                }
                price = price + bizQuotationProduct.getPattachment1Count()*bizQuotationProduct.getPattachment1Price();
                price = price + bizQuotationProduct.getPattachment2Count()*bizQuotationProduct.getPattachment2Price();
                price = price + bizQuotationProduct.getPattachment3Count()*bizQuotationProduct.getPattachment3Price();
                price = price + bizQuotationProduct.getPattachment4Count()*bizQuotationProduct.getPattachment4Price();
                price = price + bizQuotationProduct.getPattachmentCount()*bizQuotationProduct.getPattachmentPrice();
            }
        }
        System.out.println("price" + price);
            /**
         * 计算最低系数
         * 如果系数（报价员录入的系数）底于0.9，由必须由销售副总审核、总经理审批，流程结束；
         * 如果系数0.9--1.0，必须由销售副总审核，流程结束，
         * 如果报价系数大于1.0--1.1则由区域经理审批完成后流程结束；
         * 如果系数大于1.1，则由部门销售经理审核完成后流程结束
         */
            if (minCoefficient < 0.88) {
                normalFlag = "5";
            }else if ((minCoefficient >= 0.88 && minCoefficient < 0.95)) {
                if (num < 5) {
                    normalFlag = "4";
                    num = 4;
                }
            } else if ((minCoefficient >= 0.95 && minCoefficient < 1) ) {
                if (num < 4) {
                    normalFlag = "3";
                    num = 3;
                }
            } else {
                if (num < 3) {
                    normalFlag = "2";
                    num = 2;
                }
            }

        bizQuotation.setNormalFlag(normalFlag);

        /**
         * normalFlag 先把除报价员的权限范围做了
         *
         * 1=销售 2=销售经理 3=区域经理 4=副总 5=总经理
         */
        int roleType = sysRoleService.getRoleType(ShiroUtils.getUserId());
        if (roleType > 1) {
            if (normalFlag.equals(roleType + "")) {
//                bizQuotation.setNormalFlag(normalFlag);
                bizQuotation.setFlowStatus(normalFlag);
            } else {
                bizQuotation.setFlowStatus(roleType + "0");
            }
        }
        //如果高级别创建的不需要再高级别审批的直接同意
        if (roleType >=  Integer.parseInt(normalFlag)) {
            bizQuotation.setFlowStatus(roleType + "");
            bizQuotation.setNormalFlag(roleType + "");
        }
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


    @GetMapping("/selectPattachment")
    public String selectPattachment(ModelMap mmap) {
        String productId = getRequest().getParameter("productId");
        String type = getRequest().getParameter("type");
        mmap.put("productId", productId);
        mmap.put("type", type);
        return prefix + "/selectPattachment";
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
        //String examineStatus = bizQuotation.getString6();
        //String examineRemark = bizQuotation.getString5();
        String examineStatus = bizQuotation.getExamineStatus();
        String examineRemark = bizQuotation.getExamineRemark();
        String quotationId = bizQuotation.getQuotationId().toString();
        return toAjax(bizQuotationService.doExamine(quotationId,examineStatus,examineRemark));
    }

    @GetMapping("/upload")
    public String upload(ModelMap mmap) {
        return prefix + "/upload";
    }



    @PostMapping("/addQuotationProduct")
    @ResponseBody
    public JSONArray addQuotationProduct(){
        JSONArray jsonArray = new JSONArray();
        String ids = getRequest().getParameter("ids");
        String[] ids_ = ids.split(",");
        Map<Long,BizQuotation> map = new LinkedHashMap<>();
        for (String id : ids_) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            BizQuotation queryBizQuotation = new BizQuotation();
            queryBizQuotation.setQuotationId(Long.parseLong(id));
            List<BizQuotation> list = bizQuotationService.selectBizQuotationProductList(queryBizQuotation);
            for (BizQuotation bizQuotation : list) {
                Long productId = bizQuotation.getProductId();
                if (map.containsKey(productId)) {
                    BizQuotation oldQProduct = map.get(productId);
                    bizQuotation.setProductNum(StringUtils.addDouble(bizQuotation.getProductNum(),oldQProduct.getProductNum()));
                    bizQuotation.setActuatorNum(StringUtils.addDouble(bizQuotation.getActuatorNum(),oldQProduct.getActuatorNum()));
                    bizQuotation.setProductRef1Num(StringUtils.addDouble(bizQuotation.getProductRef1Num(),oldQProduct.getProductRef1Num()));
                    bizQuotation.setProductRef2Num(StringUtils.addDouble(bizQuotation.getProductRef2Num(),oldQProduct.getProductRef2Num()));
                    bizQuotation.setPattachmentCount(StringUtils.addDouble(bizQuotation.getPattachmentCount(),oldQProduct.getPattachmentCount()));
                    bizQuotation.setPattachment1Count(StringUtils.addDouble(bizQuotation.getPattachment1Count(),oldQProduct.getPattachment1Count()));
                    bizQuotation.setPattachment2Count(StringUtils.addDouble(bizQuotation.getPattachment2Count(),oldQProduct.getPattachment2Count()));
                    bizQuotation.setPattachment3Count(StringUtils.addDouble(bizQuotation.getPattachment3Count(),oldQProduct.getPattachment3Count()));
                    bizQuotation.setPattachment4Count(StringUtils.addDouble(bizQuotation.getPattachment4Count(),oldQProduct.getPattachment4Count()));

                    map.put(productId,bizQuotation);
                } else {
                    map.put(productId,bizQuotation);
                }
            }
        }
        for (Long productId : map.keySet()) {
            BizQuotation bizQuotation = map.get(productId);
            JSONObject json = new JSONObject();
            json.put("quotationId",bizQuotation.getQuotationId());
            json.put("quotationName",bizQuotation.getString1());
            json.put("productId",bizQuotation.getProductId());
            json.put("productName",bizQuotation.getProductName());
            json.put("model",bizQuotation.getModel());
            json.put("string1",bizQuotation.getSeries());
            json.put("specifications",bizQuotation.getSpecifications());
            json.put("nominalPressure",bizQuotation.getNominalPressure());
            json.put("valvebodyMaterial",bizQuotation.getValvebodyMaterial());
            json.put("valveElement",bizQuotation.getValveElement());
            json.put("sealingMaterial",bizQuotation.getSealingMaterial());
            json.put("driveForm",bizQuotation.getDriveForm());
            json.put("connectionType",bizQuotation.getConnectionType());
            json.put("productNum",bizQuotation.getProductNum());

            json.put("productCostPrice",bizQuotation.getProductCostPrice());

            json.put("productPrice",bizQuotation.getProductPrice());
            json.put("productCoefficient",bizQuotation.getProductCoefficient());
            json.put("actuatorId",bizQuotation.getActuatorId());
            json.put("actuatorName",bizQuotation.getActuatorName());
            json.put("actuatorPrice",bizQuotation.getActuatorPrice());

            json.put("actuatorCostPrice",bizQuotation.getActuatorCostPrice());

            json.put("actuatorNum",bizQuotation.getActuatorNum());
            json.put("actuatorCoefficient",bizQuotation.getActuatorCoefficient());
            json.put("productRef1Id",bizQuotation.getProductRef1Id());
            json.put("ref1Name",bizQuotation.getRef1Name());
            json.put("ref1Price",bizQuotation.getRef1Price());

            json.put("ref1CostPrice",bizQuotation.getRef1CostPrice());

            json.put("productRef1Num",bizQuotation.getProductRef1Num());
            json.put("productRef1Coefficient",bizQuotation.getProductRef1Coefficient());
            json.put("productRef2Id",bizQuotation.getProductRef2Id());
            json.put("ref2Name",bizQuotation.getRef2Name());
            json.put("ref2Price",bizQuotation.getRef2Price());

            json.put("ref2CostPrice",bizQuotation.getRef2CostPrice());

            json.put("productRef2Num",bizQuotation.getProductRef2Num());
            json.put("productRef2Coefficient",bizQuotation.getProductRef2Coefficient());

            json.put("pattachmentId",bizQuotation.getPattachmentId());
            json.put("pattachmentName",bizQuotation.getPattachmentName());
            json.put("pattachmentPrice",bizQuotation.getPattachmentPrice());

            json.put("pattachmentCostPrice",bizQuotation.getPattachmentCostPrice());
            json.put("pattachment1CostPrice",bizQuotation.getPattachment1CostPrice());
            json.put("pattachment2CostPrice",bizQuotation.getPattachment2CostPrice());
            json.put("pattachment3CostPrice",bizQuotation.getPattachment3CostPrice());
            json.put("pattachment4CostPrice",bizQuotation.getPattachment4CostPrice());

            json.put("pattachmentCount",bizQuotation.getPattachmentCount());
            json.put("pattachmentCoefficient",bizQuotation.getPattachmentCoefficient());

            json.put("pattachment1Id",bizQuotation.getPattachment1Id());
            json.put("pattachment1Name",bizQuotation.getPattachment1Name());
            json.put("pattachment1Price",bizQuotation.getPattachment1Price());
            json.put("pattachment1Count",bizQuotation.getPattachment1Count());
            json.put("pattachment1Coefficient",bizQuotation.getPattachment1Coefficient());

            json.put("pattachment2Id",bizQuotation.getPattachment2Id());
            json.put("pattachment2Name",bizQuotation.getPattachment2Name());
            json.put("pattachment2Price",bizQuotation.getPattachment2Price());
            json.put("pattachment2Count",bizQuotation.getPattachment2Count());
            json.put("pattachment2Coefficient",bizQuotation.getPattachment2Coefficient());

            json.put("pattachment3Id",bizQuotation.getPattachment3Id());
            json.put("pattachment3Name",bizQuotation.getPattachment3Name());
            json.put("pattachment3Price",bizQuotation.getPattachment3Price());
            json.put("pattachment3Count",bizQuotation.getPattachment3Count());
            json.put("pattachment3Coefficient",bizQuotation.getPattachment3Coefficient());

            json.put("pattachment4Id",bizQuotation.getPattachment4Id());
            json.put("pattachment4Name",bizQuotation.getPattachment4Name());
            json.put("pattachment4Price",bizQuotation.getPattachment4Price());
            json.put("pattachment4Count",bizQuotation.getPattachment4Count());
            json.put("pattachment4Coefficient",bizQuotation.getPattachment4Coefficient());
            json.put("string14","0");
            json.put("string15","RAL5010高光");
            jsonArray.add(json);
        }
        return jsonArray;
    }


    @PostMapping("/excelData")
    @ResponseBody
    public JSONObject excelData(){
        JSONObject retJson = new JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONArray errorArray = new JSONArray();
        String url = getRequest().getParameter("url");
        String realPath = Global.getFilePath() + url;
        List<BizProductExcel> list = new ArrayList<>();
        try {
            ExcelUtil<BizProductExcel> excelUtil = new ExcelUtil(BizProductExcel.class);
            list = excelUtil.importExcel("",realPath);

            if (CollectionUtils.isEmpty(list)) {
                JSONObject json = new JSONObject();
                json.put("msg","格式错误，请按照标准格式增加！");
                errorArray.add(json);
            }
            Map<Long,Long> map = new HashMap<>();

            int i = 1;
            for (BizProductExcel product : list) {
                JSONObject jsonData = new JSONObject();
                String model = product.getModel();
                if (StringUtils.isEmpty(model)) {
                    JSONObject json = new JSONObject();
                    json.put("msg","第" + (i + 1) + "行产品型号不能为空！");
                    errorArray.add(json);
                    i++;
                    continue;
                } else {
                    model = model.trim();
                }
                String string1 = product.getString1().trim();
                if (StringUtils.isNotEmpty(string1)) {
                    string1 = string1.trim();
                }
                String num = product.getNum();
                if (StringUtils.isNotEmpty(num)) {
                    num = num.trim();
                }
                if (!StringUtils.isNumeric(num)) {
                    JSONObject json = new JSONObject();
                    json.put("msg","第" + (i + 1) + "行数量格式错误！");
                    errorArray.add(json);
                    i++;
                    continue;
                }

                BizProduct queryBizProduct = new BizProduct();
                queryBizProduct.setModelEq(model);

                String specifications = product.getSpecifications();
                if (StringUtils.isNotEmpty(specifications)) {
                    queryBizProduct.setSpecificationsName(specifications);
                }
                if (StringUtils.isNotEmpty(string1)) {
                    queryBizProduct.setSeriesName(string1);
                }
                queryBizProduct.setString5("yes");
                List<BizProduct> bizProductList = bizProductService.selectBizProductList(queryBizProduct);
                if (!CollectionUtils.isEmpty(bizProductList)) {
                    /*if (bizProductList.size() > 1) {
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 1) + "行 查询出多条数据！");
                        errorArray.add(json);
                        i++;
                        continue;
                    }*/

                    BizProduct bizProduct = bizProductList.get(0);
                    Long productId = bizProduct.getProductId();
                    jsonData.put("productId",productId);
                    jsonData.put("productName",bizProduct.getName());
                    jsonData.put("model",bizProduct.getModel());
                    jsonData.put("string1",bizProduct.getString1());

                    jsonData.put("specifications",bizProduct.getSpecifications());
                    jsonData.put("nominalPressure",bizProduct.getNominalPressure());
                    jsonData.put("valvebodyMaterial",bizProduct.getValvebodyMaterial());
                    jsonData.put("valveElement",bizProduct.getValveElement());
                    jsonData.put("sealingMaterial",bizProduct.getSealingMaterial());
                    jsonData.put("driveForm",bizProduct.getDriveForm());
                    jsonData.put("connectionType",bizProduct.getConnectionType());
                    jsonData.put("productNum",num);

                    jsonData.put("productCostPrice",bizProduct.getCostPrice());

                    //执行器*1 法兰*2


                    jsonData.put("productPrice",bizProduct.getPrice());
                    jsonData.put("productCoefficient","1");
                    jsonData.put("productRef1Id","");
                    jsonData.put("ref1Name","请选择");

                    jsonData.put("ref1Price","0");
                    jsonData.put("productRef1Num","0");
                    jsonData.put("productRef1Coefficient","0");
                    jsonData.put("productRef2Id","");
                    jsonData.put("ref2Name","请选择");
                    jsonData.put("ref2Price","0");
                    jsonData.put("productRef2Num","0");
                    jsonData.put("productRef2Coefficient","0");
                    jsonData.put("actuatorId","");
                    jsonData.put("actuatorName","请选择");
                    jsonData.put("actuatorPrice","0");
                    jsonData.put("actuatorNum","0");
                    jsonData.put("actuatorCoefficient","0");
                    jsonData.put("productRemark","");
                    if (!map.containsKey(productId)) {
                        dataArray.add(jsonData);
                        map.put(productId,productId);
                    } else {
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 1) + "行数据重复！");
                        errorArray.add(json);
                    }
                } else {
                    JSONObject json = new JSONObject();
                    json.put("msg","第" + (i + 1) + "行数据不存在！");
                    errorArray.add(json);
                }
                i++;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("导出失败，请联系网站管理员！");
        }
        retJson.put("data",dataArray);
        if (errorArray.size() > 0) {
            retJson.put("error",errorArray);
        }
        return retJson;
    }

    /**
     * 导出报价单excel
     * @param bizQuotationParamter
     * @return
     */
    @PostMapping("/exportQuotationExcel")
    @ResponseBody
    public AjaxResult exportQuotationExcel(BizQuotation bizQuotationParamter) {
        try {
            String id =  bizQuotationParamter.getQuotationId().toString();
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

            Workbook workbook = new HSSFWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            Sheet sheet = workbook.createSheet("报价单");
            // 单元格样式
            CellStyle cellRightBlackFont = ExcelProcessDataUtils.cellRightBlackFont(workbook);
            Row row1 = sheet.createRow(0);
            row1.setHeight((short) 800);

            CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 10);
            sheet.addMergedRegion(cra1);
            Cell cell_title_1 = row1.createCell(0);
            cell_title_1.setCellValue("北京好利阀业集团有限公司报价清单");
            CellStyle cellTitle = ExcelProcessDataUtils.titleBigCell(workbook);
            cell_title_1.setCellStyle(cellTitle);

            Row row2 = sheet.createRow(1);
            row2.setHeight((short) 400);
            Cell cell_20 = row2.createCell(0);
            cell_20.setCellValue("报价单号：");
            cell_20.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
            Cell cell_22 = row2.createCell(2);
            cell_22.setCellValue(bizQuotation.getString1());
            cell_22.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 5));
            Cell cell_26 = row2.createCell(6);
            cell_26.setCellValue("报价日期：");
            cell_26.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
            Cell cell_28 = row2.createCell(8);
            cell_28.setCellValue(bizQuotation.getCreateTime());
            cell_28.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 10));
            List<Integer> row2CellList = Arrays.asList(1, 3, 4, 5, 7, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row2, cellRightBlackFont, row2CellList);

            String customerId = bizQuotation.getString2();
            BizCustomer bizCustomer = new BizCustomer();
            if (StringUtils.isNotEmpty(customerId)) {
                bizCustomer = bizCustomerService.selectBizCustomerById(Long.parseLong(customerId));
            }
            Row row3 = sheet.createRow(2);
            row3.setHeight((short) 400);
            Cell cell_30 = row3.createCell(0);
            cell_30.setCellValue("询价公司：");
            cell_30.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));
            Cell cell_32 = row3.createCell(2);
            cell_32.setCellValue(bizCustomer.getName());
            cell_32.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 5));
            Cell cell_36 = row3.createCell(6);
            cell_36.setCellValue("报价人：");
            cell_36.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(bizQuotation.getCreateBy()));
            Cell cell_38 = row3.createCell(8);
            cell_38.setCellValue(sysUser.getUserName());
            cell_38.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 8, 10));
            List<Integer> row3CellList = Arrays.asList(1, 3, 4, 5, 7, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row3, cellRightBlackFont, row3CellList);

            Row row4 = sheet.createRow(3);
            row4.setHeight((short) 400);
            Cell cell_40 = row4.createCell(0);
            cell_40.setCellValue("联系人：");
            cell_40.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
            Cell cell_42 = row4.createCell(2);
            cell_42.setCellValue(bizCustomer.getContactName());
            cell_42.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 5));
            Cell cell_46 = row4.createCell(6);
            cell_46.setCellValue("手机：");
            cell_46.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 7));
            Cell cell_48 = row4.createCell(8);
            cell_48.setCellValue(sysUser.getPhonenumber());
            cell_48.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 8, 10));
            List<Integer> row4CellList = Arrays.asList(1, 3, 4, 5, 7, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row4, cellRightBlackFont, row4CellList);

            Row row5 = sheet.createRow(4);
            row5.setHeight((short) 400);
            Cell cell_50 = row5.createCell(0);
            cell_50.setCellValue("手机：");
            cell_50.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
            Cell cell_52 = row5.createCell(2);
            cell_52.setCellValue(bizCustomer.getContactPhone());
            cell_52.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 5));
            Cell cell_56 = row5.createCell(6);
            cell_56.setCellValue("E-mail：");
            cell_56.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));
            Cell cell_58 = row5.createCell(8);
            cell_58.setCellValue(sysUser.getEmail());
            cell_58.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 8, 10));
            List<Integer> row5CellList = Arrays.asList(1, 3, 4, 5, 7, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row5, cellRightBlackFont, row5CellList);

            Row row6 = sheet.createRow(5);
            row6.setHeight((short) 400);
            Cell cell_60 = row6.createCell(0);
            cell_60.setCellValue("E-mail：");
            cell_60.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 1));
            Cell cell_62 = row6.createCell(2);
            cell_62.setCellValue(bizCustomer.getContactEmail());
            cell_62.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 5));
            Cell cell_66 = row6.createCell(6);
            cell_66.setCellValue("项目名称：");
            cell_66.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 7));
            Cell cell_68 = row6.createCell(8);
            cell_68.setCellValue(bizQuotation.getReportProject());
            cell_68.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 8, 10));
            List<Integer> row6CellList = Arrays.asList(1, 3, 4, 5, 7, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row6, cellRightBlackFont, row6CellList);

            Row row7 = sheet.createRow(6);
            row7.setHeight((short) 400);
            Cell cell_70 = row7.createCell(0);
            cell_70.setCellValue("以下为贵公司询价产品明细，请详阅。如有疑问，请及时与我司取得联系，我们将竭尽全力为您提供全面的服务，谢谢！");
            cell_70.setCellStyle(cellRightBlackFont);
            sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 10));
            List<Integer> row7CellList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row7, cellRightBlackFont, row7CellList);

            CellStyle cellTableStyle = ExcelProcessDataUtils.cellTableStyle(workbook);
            Row row8 = sheet.createRow(7);
            row8.setHeight((short) 500);
            Cell cell_80 = row8.createCell(0);
            cell_80.setCellValue("序号");
            cell_80.setCellStyle(cellTableStyle);
            Cell cell_81 = row8.createCell(1);
            cell_81.setCellValue("产品ID");
            cell_81.setCellStyle(cellTableStyle);
            Cell cell_82 = row8.createCell(2);
            cell_82.setCellValue("名称");
            cell_82.setCellStyle(cellTableStyle);
            Cell cell_83 = row8.createCell(3);
            cell_83.setCellValue("压力");
            cell_83.setCellStyle(cellTableStyle);
            Cell cell_84 = row8.createCell(4);
            cell_84.setCellValue("规格");
            cell_84.setCellStyle(cellTableStyle);
            Cell cell_85 = row8.createCell(5);
            cell_85.setCellValue("数量（套）");
            cell_85.setCellStyle(cellTableStyle);
            Cell cell_86 = row8.createCell(6);
            cell_86.setCellValue("单价(人民币)");
            cell_86.setCellStyle(cellTableStyle);
            Cell cell_87 = row8.createCell(7);
            cell_87.setCellValue("合计(人民币)");
            cell_87.setCellStyle(cellTableStyle);
            Cell cell_88 = row8.createCell(8);
            cell_88.setCellValue("材质");
            cell_88.setCellStyle(cellTableStyle);
            Cell cell_89 = row8.createCell(9);
            cell_89.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(7, 7, 8, 9));
            Cell cell_810 = row8.createCell(10);
            cell_810.setCellValue("备注");
            cell_810.setCellStyle(cellTableStyle);
            int rowCount = 7;
            int serial = 0;
            int count = 0;
            Double unitPrice = 0.0;
            Double totalPrice = 0.0;
            for (BizQuotationProduct bizProduct : bizQuotationProducts) {
                BizProduct bizProductObj = bizProduct.getBizProduct();
                if (bizProductObj == null) {
                    continue;
                }
                rowCount++;
                serial++;
                Row rowi = sheet.createRow(rowCount);
                rowi.setHeight((short) 1200);
                Cell cell = rowi.createCell(0);
                cell.setCellValue(serial);
                cell.setCellStyle(cellTableStyle);
                Cell cell1 = rowi.createCell(1);
                cell1.setCellValue(bizProductObj.getModel());
                cell1.setCellStyle(cellTableStyle);
                Cell cell2 = rowi.createCell(2);
                cell2.setCellValue(bizProductObj.getName());
                cell2.setCellStyle(cellTableStyle);
                Cell cell3 = rowi.createCell(3);
                cell3.setCellValue(bizProductObj.getNominalPressure());
                cell3.setCellStyle(cellTableStyle);
                Cell cell4 = rowi.createCell(4);
                cell4.setCellValue(bizProductObj.getSpecifications());
                cell4.setCellStyle(cellTableStyle);
                Cell cell5 = rowi.createCell(5);
                if (StringUtils.isNotEmpty(bizProduct.getProductNum())) {
                    count += Integer.parseInt(bizProduct.getProductNum());
                }
                cell5.setCellValue(bizProduct.getProductNum());
                cell5.setCellStyle(cellTableStyle);
                Cell cell6 = rowi.createCell(6);
                Double productTotalPrice = Double.valueOf(bizProduct.getTotalPriceOnly()); //单价
                unitPrice += productTotalPrice;
                cell6.setCellValue(productTotalPrice + "");
                cell6.setCellStyle(cellTableStyle);
                Cell cell7 = rowi.createCell(7);
                double v = productTotalPrice * Double.parseDouble(bizProduct.getProductNum());
                totalPrice += v;
                cell7.setCellValue(v + "");
                cell7.setCellStyle(cellTableStyle);
                Cell cell8 = rowi.createCell(8);
                String caizhi = "阀体材质:" + bizProductObj.getValvebodyMaterial() + ";" + "阀芯材质：" + bizProductObj.getValveElement()
                        + ";" + "密封材质：" + bizProductObj.getSealingMaterial() + ";" + "连接方式：" + bizProductObj.getConnectionType() + "。";
                cell8.setCellValue(caizhi);
                cell8.setCellStyle(cellTableStyle);
                Cell cell9 = rowi.createCell(9);
                cell9.setCellStyle(cellTableStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 8, 9));
                Cell cell10 = rowi.createCell(10);
                cell10.setCellValue(bizProduct.getRemark());
                cell10.setCellStyle(cellTableStyle);
            }

            int aa1 = rowCount++;
            int aa = rowCount++;
            Row row9 = sheet.createRow(aa);
            row9.setHeight((short) 400);
            Cell cell_90 = row9.createCell(0);
            cell_90.setCellValue("合计");
            cell_90.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(aa, aa, 0, 3));
            List<Integer> row9CellList = Arrays.asList(1, 2, 3, 4, 8, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row9, cellRightBlackFont, row9CellList);
            Cell cell_95 = row9.createCell(5);
            cell_95.setCellValue(count);
            cell_95.setCellStyle(cellTableStyle);
            Cell cell_96 = row9.createCell(6);
            cell_96.setCellValue("");
            cell_96.setCellStyle(cellTableStyle);
            Cell cell_97 = row9.createCell(7);
            cell_97.setCellValue(totalPrice);
            cell_97.setCellStyle(cellTableStyle);

            int bb = rowCount++;
            Row row10 = sheet.createRow(bb);
            row10.setHeight((short) 400);
            Cell cell_10 = row10.createCell(0);
            cell_10.setCellValue("以上价格为含13%增值税价格    人民币：");
            cell_10.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(bb, bb, 0, 3));
            List<Integer> row10CellList = Arrays.asList(1, 2, 5, 6, 7, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row10, cellRightBlackFont, row10CellList);
            Cell cell_14 = row10.createCell(4);
            cell_14.setCellValue(StringUtils.convert(totalPrice));
            cell_14.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(bb, bb, 4, 7));
            Cell cell_18 = row10.createCell(8);
            cell_18.setCellValue("优惠金额：" + bizQuotation.getDiscount());
            cell_18.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(bb, bb, 8, 10));

            int cc = rowCount++;
            Row row11 = sheet.createRow(cc);
            row11.setHeight((short) 400);
            Cell cell_11_0 = row11.createCell(0);
            cell_11_0.setCellValue("      优惠后     人民币：");
            cell_11_0.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(cc, cc, 0, 3));
            List<Integer> row11CellList = Arrays.asList(1, 2,3, 5, 6, 7, 9, 10);
            ExcelProcessDataUtils.fillInBlankCell(row11, cellRightBlackFont, row11CellList);
            Cell cell_114 = row11.createCell(4);
            cell_114.setCellValue(StringUtils.convert(totalPrice - Double.parseDouble(bizQuotation.getDiscount())));
            cell_114.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(cc, cc, 4, 7));
            Cell cell_118 = row11.createCell(8);
            cell_118.setCellValue("优惠合计：" + (totalPrice - Double.parseDouble(bizQuotation.getDiscount())));
            cell_118.setCellStyle(cellTableStyle);
            sheet.addMergedRegion(new CellRangeAddress(cc, cc, 8, 10));

            int dd = rowCount++;
            Row row12 = sheet.createRow(dd);
            row12.setHeight((short) 400);
            sheet.addMergedRegion(new CellRangeAddress(dd, dd, 0, 2));
            Cell cell_12_0 = row12.createCell(0);
            cell_12_0.setCellValue("备注");
            cell_12_0.setCellStyle(ExcelProcessDataUtils.strongBlackCenter(workbook));

            int ee = rowCount++;
            Row row13 = sheet.createRow(ee);
            row13.setHeight((short) 400);
            Cell cell_13_1 = row13.createCell(1);
            cell_13_1.setCellValue("供货周期：");
            cell_13_1.setCellStyle(ExcelProcessDataUtils.cellRight(workbook));
            sheet.addMergedRegion(new CellRangeAddress(ee, ee, 1, 2));
            Cell cell_13_2 = row13.createCell(3);
            cell_13_2.setCellValue(bizQuotation.getLeadTime() + "个工作日");
            cell_13_2.setCellStyle(ExcelProcessDataUtils.cellLeft(workbook));
            sheet.addMergedRegion(new CellRangeAddress(ee, ee, 3, 4));


            String payMethod = dictDataService.selectDictLabel("payment_method",bizQuotationDict.getPaymentMethod());
            if(payMethod.equals("其他")) {
                payMethod = StringUtils.isNotEmpty(bizQuotation.getString6())? bizQuotation.getString6():payMethod;
            }
            int ff = rowCount++;
            Row row14 = sheet.createRow(ff);
            row14.setHeight((short) 400);
            Cell cell_14_1 = row14.createCell(1);
            cell_14_1.setCellValue("价格条款：");
            cell_14_1.setCellStyle(ExcelProcessDataUtils.cellRight(workbook));
            sheet.addMergedRegion(new CellRangeAddress(ff, ff, 1, 2));
            Cell cell_14_2 = row14.createCell(3);
            cell_14_2.setCellValue(payMethod);
            cell_14_2.setCellStyle(ExcelProcessDataUtils.cellLeft(workbook));
            sheet.addMergedRegion(new CellRangeAddress(ff, ff, 3, 5));


            int gg = rowCount++;
            Row row15 = sheet.createRow(gg);
            row15.setHeight((short) 400);
            Cell cell_15_1 = row15.createCell(1);
            cell_15_1.setCellValue("价格有效期：");
            cell_15_1.setCellStyle(ExcelProcessDataUtils.cellRight(workbook));
            sheet.addMergedRegion(new CellRangeAddress(gg, gg, 1, 2));
            Cell cell_15_2 = row15.createCell(3);
            cell_15_2.setCellValue(bizQuotation.getString12() + "   天；");
            cell_15_2.setCellStyle(ExcelProcessDataUtils.cellLeft(workbook));


            String filename = ExcelUtil.encodingFilenameByXls("报价单");
            OutputStream out = new FileOutputStream(getAbsoluteFile(filename));
            workbook.write(out);
            out.flush();
            out.close();
            return AjaxResult.success(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

}
