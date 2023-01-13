package com.ruoyi.common.utils.poi;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;

/**
 * @ClassName ExcelUtils
 * @Author liuyaofei
 * @Date 2022/10/31
 * @Description TODO
 **/
public class ExcelProcessDataUtils {


    public final static String[] yunshu = new String[] { "货运", "快递", "航空", "专用", "自提"};
    public final static String[] yunfei = new String[] { "卖方付", "买方付"};
    public final static String[] shifouxuyaosong = new String[] { "送货", "货站自提"};
    public final static String[] anzhuang = new String[] { "不提供安装与调试", "电视或视频指导", "另付收费协议"};
    public final static String[] anzhuang2 = new String[] { "电话或视频指导", "免费现场指导", "免费提供安装于调试"};
    public final static String[] jioahuozhouqi = new String[] { "合同生效日期起     天发货", "预付款到账后     天发货"};
    public final static String[] baozhiqi = new String[] { "12个月", "18个月", "____个月"};
    public final static String[] biaopai = new String[] { "好利", "大宇", "好利英语","买方"};
    public final static String[] baozhuangfangshi = new String[] { "木箱", "甲方要求木箱", "协议木箱","托盘", "布袋+木箱", "发泡+木箱"};
    public final static String[] fukuanxingshi = new String[] { "月结", "货到付款"};
    public final static String[] shouhuodizhi = new String[] { "北京-崔海峰，15011572622，北京市大兴区榆垡镇榆顺路6号；", "上海-王丽，15021883348，上海市闵行区景联路189号12号楼1楼；","山西-聂超，19935711990，山西省侯马市香邑开发区山西好利阀业；", "直发客户"};



    /**
     * 下拉选
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     * @param strs
     */
    public static void addValidationData (Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, String[] strs) {
        CellRangeAddressList regions3 = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DVConstraint constraint3 = DVConstraint.createExplicitListConstraint(strs);
        HSSFDataValidation dataValidation3 = new HSSFDataValidation(regions3, constraint3);
        sheet.addValidationData(dataValidation3);
    }

    public static CellStyle createBottomStyle (Workbook workbook) {
        CellStyle cellBottomStyle = workbook.createCellStyle();
        //下边框
        cellBottomStyle.setBorderBottom(BorderStyle.THIN);
        return cellBottomStyle;
    }

    public static CellStyle cellLeft (Workbook workbook) {
        CellStyle cellLeft = workbook.createCellStyle();
        cellLeft.setAlignment(HorizontalAlignment.LEFT);
        cellLeft.setWrapText(true);
        return cellLeft;
    }

    public static CellStyle cellCenter (Workbook workbook) {
        CellStyle cellLeft = workbook.createCellStyle();
        cellLeft.setAlignment(HorizontalAlignment.CENTER);
        cellLeft.setWrapText(true);
        return cellLeft;
    }

    public static CellStyle cellRight (Workbook workbook) {
        CellStyle cellLeft = workbook.createCellStyle();
        cellLeft.setAlignment(HorizontalAlignment.RIGHT);
        cellLeft.setWrapText(true);
        return cellLeft;
    }

    public static CellStyle cellRightBlackFont (Workbook workbook) {
        CellStyle cellLeft = workbook.createCellStyle();
        cellLeft.setAlignment(HorizontalAlignment.RIGHT);
        //下边框
        cellLeft.setBorderBottom(BorderStyle.THIN);
        //cellLeft
        cellLeft.setBorderLeft(BorderStyle.THIN);
        //上边框
        cellLeft.setBorderTop(BorderStyle.THIN);
        //右边框
        cellLeft.setBorderRight(BorderStyle.THIN);
        //增加垂直居中样式
        cellLeft.setVerticalAlignment(VerticalAlignment.CENTER);
        //自动换行样式增加
        cellLeft.setWrapText(true);
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("黑体");
        cellLeft.setFont(font);
        return cellLeft;
    }


    public static CellStyle strongBlackCenter (Workbook workbook) {
        CellStyle cellTitle = workbook.createCellStyle();
        cellTitle.setAlignment(HorizontalAlignment.CENTER);
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("黑体");
        cellTitle.setFont(font);
        return cellTitle;
    }


    public static CellStyle titleCell (Workbook workbook) {
        CellStyle cellTitle = workbook.createCellStyle();
        cellTitle.setAlignment(HorizontalAlignment.CENTER);

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);//设置字体大小
        cellTitle.setFont(font);
        return cellTitle;
    }

    public static CellStyle titleBigCell (Workbook workbook) {
        CellStyle cellTitle = workbook.createCellStyle();
        cellTitle.setAlignment(HorizontalAlignment.CENTER);

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 20);//设置字体大小
        cellTitle.setFont(font);
        return cellTitle;
    }

    public static CellStyle cellTableStyle (Workbook workbook) {
        CellStyle cellTableStyle = workbook.createCellStyle();
        //下边框
        cellTableStyle.setBorderBottom(BorderStyle.THIN);
        //左边框
        cellTableStyle.setBorderLeft(BorderStyle.THIN);
        //上边框
        cellTableStyle.setBorderTop(BorderStyle.THIN);
        //右边框
        cellTableStyle.setBorderRight(BorderStyle.THIN);
        //增加水平居中样式
        cellTableStyle.setAlignment(HorizontalAlignment.CENTER);
        //增加垂直居中样式
        cellTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //自动换行样式增加
        cellTableStyle.setWrapText(true);
        return cellTableStyle;
    }

    /**
     * 填充空白cell
     * @param row
     * @param cellRightBlackFont
     * @param rowCellList
     */
    public static void fillInBlankCell(Row row, CellStyle cellRightBlackFont, List<Integer> rowCellList) {
        for (Integer integer : rowCellList) {
            Cell cell = row.createCell(integer);
            cell.setCellStyle(cellRightBlackFont);
        }
    }
}
