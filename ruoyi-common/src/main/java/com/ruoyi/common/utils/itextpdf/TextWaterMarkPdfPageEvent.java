package com.ruoyi.common.utils.itextpdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;

public class TextWaterMarkPdfPageEvent extends PdfPageEventHelper {

    public String waterMarkText;

    public TextWaterMarkPdfPageEvent(String waterMarkText) {
        this.waterMarkText = waterMarkText;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            // 获取pdf内容正文页面宽度
            float pageWidth = document.right() + document.left();
            // 获取pdf内容正文页面高度
            float pageHeight = document.top() + document.bottom();
            // 设置水印字体格式
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font waterMarkFont = new Font(base, 15, Font.BOLD, new BaseColor(230, 230, 230));
            PdfContentByte waterMarkPdfContent = writer.getDirectContentUnder();
            Phrase phrase = new Phrase(waterMarkText, waterMarkFont);
            ColumnText.showTextAligned(waterMarkPdfContent, Element.ALIGN_CENTER, phrase, pageWidth * 0.25f, pageHeight * 0.2f, 45);
            ColumnText.showTextAligned(waterMarkPdfContent, Element.ALIGN_CENTER, phrase, pageWidth * 0.25f, pageHeight * 0.5f, 45);
            ColumnText.showTextAligned(waterMarkPdfContent, Element.ALIGN_CENTER, phrase, pageWidth * 0.25f, pageHeight * 0.8f, 45);
            ColumnText.showTextAligned(waterMarkPdfContent, Element.ALIGN_CENTER, phrase, pageWidth * 0.65f, pageHeight * 0.2f, 45);
            ColumnText.showTextAligned(waterMarkPdfContent, Element.ALIGN_CENTER, phrase, pageWidth * 0.65f, pageHeight * 0.5f, 45);
            ColumnText.showTextAligned(waterMarkPdfContent, Element.ALIGN_CENTER, phrase, pageWidth * 0.65f, pageHeight * 0.8f, 45);
        } catch (DocumentException de) {
            de.printStackTrace();
        } catch (IOException de) {
            de.printStackTrace();
        }
    }
}
