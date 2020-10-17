package com.ruoyi.fmis.procurement.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvert {

    public static String formatDate(Date date) {
        String formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return formatDate;
    }
}
