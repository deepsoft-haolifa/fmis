package com.ruoyi.common.utils.itextpdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;

public class PdfFooterEvent extends PdfPageEventHelper {

    public static PdfPTable footer;

    public static String webUrl;

    public PdfFooterEvent(PdfPTable footer, String webUrl) {
        PdfFooterEvent.footer = footer;
        PdfFooterEvent.webUrl = webUrl;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // 把页脚表格定位
        footer.writeSelectedRows(0, -1, 38, 30, writer.getDirectContent());
    }

    /**
     * 页脚是文字
     *
     * @param writer
     * @throws IOException
     * @throws DocumentException
     */
    public void setTableFooter(PdfWriter writer) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(520f);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(1);
        String string =
                "本回执仅做财务付款申请凭证，它用无效                                                                                         网址：" + webUrl;
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font FontChinese = new Font(bfChinese, 10, Font.NORMAL);
        FontChinese.setColor(new BaseColor(150, 150, 150));
        Paragraph p = new Paragraph(string, FontChinese);
        cell.setPaddingLeft(10f);
        cell.setPaddingTop(-2f);
        cell.addElement(p);
        table.addCell(cell);
        PdfFooterEvent event = new PdfFooterEvent(table, webUrl);
        writer.setPageEvent(event);
    }
}
