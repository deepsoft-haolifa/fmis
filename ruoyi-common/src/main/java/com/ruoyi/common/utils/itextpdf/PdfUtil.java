package com.ruoyi.common.utils.itextpdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.ruoyi.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

public class PdfUtil {

    private static final Logger log = LoggerFactory.getLogger(PdfUtil.class);

    public static BaseFont getBaseFont() throws Exception {
        return BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
    }

    public static Font getPdfChineseFont() throws Exception {
        return getPdfChineseFont(12, Font.NORMAL);
    }

    public static Font getPdfChineseFont(int size, int style) throws Exception {
        BaseFont bfChinese = getBaseFont();
        Font fontChinese = new Font(bfChinese, size, style);
        return fontChinese;
    }

    public static Font getPdfChineseFont(int size, int style, BaseColor baseColor) throws Exception {
        BaseFont bfChinese = getBaseFont();
        Font fontChinese = new Font(bfChinese, size, style, baseColor);
        return fontChinese;
    }

    public static PdfPCell getPDFCell(String text) throws Exception{

        return getPDFCell(text, PdfUtil.getPdfChineseFont());
    }

    public static PdfPCell getPDFCell(String text, Font font){
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(20);
        Phrase phrase = new Phrase(text, font);
        cell.setPhrase(phrase);
        return cell;
    }

    public static PdfPCell mergeCol(String text, int mergeCount) throws  Exception {
        return mergeCol(text, mergeCount, PdfUtil.getPdfChineseFont());
    }

    public static PdfPCell mergeCol(String text, int mergeCount, Font font){
        PdfPCell cell = new PdfPCell();
        //设置表格行高
        cell.setMinimumHeight(20);
        Phrase phrase = new Phrase(text, font);
        cell.setPhrase(phrase);
        cell.setColspan(mergeCount);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename)
    {
        filename = UUID.randomUUID().toString() + "_" + filename + ".pdf";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public static String getAbsoluteFile(String filename)
    {
        String downloadPath = Global.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

}
