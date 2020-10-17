package com.ruoyi.fmis.procurement.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvert {

    public static String formatDate(Date date) {
        String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return formatDate;
    }
}
